### RabbitMQ Set-Up
First we added the following dependency in the `build.gradle.kts` file:
> implementation("org.springframework.boot:spring-boot-starter-amqp")

Then we configured the `RabbitMQ` connection in `application.properties`:
>spring.rabbitmq.host=localhost
>spring.rabbitmq.port=5672
>spring.rabbitmq.username=guest
>spring.rabbitmq.password=guest

Run the RabbitMQ Daemon and use the RabbitMQ "management" version with the command:
`docker run -d --hostname feedapp-rabbit --name feedapp-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:4.0.1-management`
The first port is the management, and the second is the port we connect through the application.

Then check `localhost:15672` and log in with username/password "guest", and run the application locally and make sure 
that you get something like this in the console: 
``` 
2024-11-20T11:58:23.492+01:00  INFO 23248 --- [FeedApp] [           main] o.s.a.r.c.CachingConnectionFactory       : Created new connection: rabbitConnectionFactory#197c3101:0/SimpleConnection@d3e9629 [delegate=amqp://guest@127.0.0.1:5672/, localPort=63524]
```





### MongoDB Set-Up

Implemented `MongoDB` dependency by adding  the dependencies to the `build.gradle.kts` file:
```kotlin
implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.0.0")
```
And modified `applications.properties` by adding the configurations for MongoDB.
```properties
spring.data.mongodb.uri=mongodb://admin:admin@mongodb:27017/feedapp_mongodb
```
Then we opened `MongoDB Compass` and created a new database called `feedapp_mongodb` with the collection `users`.

After this, we created the `Document`-classes for `MongoDB`, the repositories for the documents, and a service- and 
listener-class for the `Analytics Component`. I also modified the `DomainManager`-class to send analytics data to the
`AnalyticsService`-class in addition to the `JpaRepositories`. Lastly, I created a configuration class which declares 
the `poll_queue` I used for the analytics components. 

### Test that it works
The configurations worked on a local machine, but we had to `Dockerize` it to make sure it ran on all machines who might 
deploy the application.



## Prerequisites for MongoDB
To make sure MongoDB has something to work with, we implemented `@Document`-model classes, similar to the `@Entity`-
model classes for `H2`. Afterward, we implemented repositories for them, as well as modifying the `DomainManager` to
handle the logic for the `Documents` as well as the `Entities`.

Since we were short on time, we chose to keep the methods in the DomainManager separate, instead of handling both an
Entity and a Document in the same methods. This is better for the development phase anyway.

### Why Keeping Separate Methods is a Good Choice
#### Isolation of Logic:
By handling each repository separately, we ensure that the logic for the `JPA` and `MongoDB`
repositories stay isolated. This allows us to clearly focus on each part without mixing concerns, especially since
they serve different purposes.

#### Easier Testing:
Since we've already tested with one repository (e.g., `JPA`), it's easier to isolate problems and debug issues if we
keep the operations separate. It also reduces the risk of introducing bugs when working with both repositories
simultaneously.

#### Scalability:
As we progress, separating the logic allows us to scale and optimize both the relational and NoSQL aspects of our
application independently. We might find that MongoDB needs different handling as our use of it grows (e.g., handling
large numbers of votes).

#### Maintainability:
If we separate the logic, we'll have a clearer structure in the code, which is easier to maintain. If we decide to
modify one part (e.g., switch to a different database for votes), it won't affect other parts of your application.


For testing that MongoDB was working, we added a test-implementation to `build.gradle.kts`:
> testImplementation ("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.0.0")

And started creating a `@SpringBootTest` for testing that the `MongoDB` connection was working. After the test passed
I went back to `MongoDB Compass` and could see the newly generated `polls` collection. we connected the database to
`IntelliJ` on a local machine and could see the collection there as well. But when merging with the main branch, the
application would not build. So we tried to `Dockerize` the whole application.

## Shell Script to Make the Application Wait until RabbitMQ is Running Before trying to Connect
To actually implement `Docker`, the first thing to do was create a script that would make `Docker` wait to launch the
application until the other images for `RabbitMQ` and `MongoDB` were up and running on their respective ports. This was
done by creating a `wait-for-it.sh` script in the same directory as where the `Dockerfile` would be, and write a short
program for it:
```shell
#!/usr/bin/env bash

# Function that waits for services to become available on a specific port
wait_for() {
  local HOST=$1
  local PORT=$2
  local SERVICE_NAME=$3

  echo "Waiting for $SERVICE_NAME on $HOST:$PORT to be ready..."
  for i in $(seq 30); do # Wait for 30 seconds before trying to connect to port
    nc -z "$HOST" "$PORT" > /dev/null 2>&1
    result=$?
    if [ $result -eq 0 ]; then
      echo "$SERVICE_NAME is available on $HOST:$PORT!"
      return 0
    fi
    sleep 5 # Sleep for 5 seconds before trying again
  done
  echo "$SERVICE_NAME is not available on $HOST:$PORT after 1 minute."
  return 1
}

# Wait for both dependencies in parallel. This reduces time spent waiting.
(
  wait_for "rabbitmq" "5672" "RabbitMQ" &
  wait_for "mongodb" "27017" "MongoDB" &
  wait
)

# Once both services are available, start the application
echo "Both services are ready, starting FeedApp..."
exec java -jar feedapp-docker.jar
```
