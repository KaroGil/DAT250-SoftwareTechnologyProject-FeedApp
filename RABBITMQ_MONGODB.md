### RabbitMQ Set-Up
First I added the following dependency in the `build.gradle.kts` file:
>implementation("org.springframework.boot:spring-boot-starter-amqp")

Then I configured the `RabbitMQ` connection in `application.properties`:
>spring.rabbitmq.host=localhost
>spring.rabbitmq.port=5672
>spring.rabbitmq.username=admin_pollapp
>spring.rabbitmq.password=admin_pollapp

### MongoDB Set-Up

Added `MongoDB` dependecy by adding  the dependencies to the `build.gradle.kts` file:
``` 
implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
```
And I modified `applications.properties` by adding the configurations for MongoDB.
```  
# MongoDB configurations
spring.data.mongodb.uri=mongodb://admin:admin@localhost:27017/feedapp_mongodb
spring.data.mongodb.database=feedapp_mongodb
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.authentication-database=mongodb_admin
spring.data.mongodb.username=mongodb_admin
spring.data.mongodb.password=mongodb_admin
```
Then I opened `MongoDB Compass` and created a new database called `feedapp_mongodb` with the collection `users`.

After this, I created the `Document`-classes for `MongoDB`, the repositories for the documents, and a service- and 
listener-class for the `Analytics Component`. I also modified the `DomainManager`-class to send analytics data to the
`AnalyticsService`-class in addition to the `JpaRepositories`. Lastly, I created a configuration class which declares 
the `poll_queue` I used for the analytics components. 

### Test that it works
I started RabbitMQ with Docker by opening a new terminal and running the following command to start a RabbitMQ container
with the management plugin enabled:
>docker run -d --hostname my-rabbit --name some-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management

``` 
-d:                     Run the container in detached mode (in the background).
--hostname my-rabbit:   Sets a custom hostname for the RabbitMQ server.
--name some-rabbit:     Assigns a name to the container.
-p 5672:5672:           Maps RabbitMQ’s default port for AMQP (the protocol used for messaging) to the host.
-p 15672:15672:         Maps the management interface port to the host.
```
and then I checked that the container was running: 
```
$ docker ps
CONTAINER ID   IMAGE                   COMMAND                  CREATED              STATUS              PORTS                                                                      
                                   NAMES
f1ad6a743a56   rabbitmq:3-management   "docker-entrypoint.s…"   About a minute ago   Up About a minute   4369/tcp, 5671/tcp, 0.0.0.0:5672->5672/tcp, 15671/tcp, 15691-15692/tcp, 25672/tcp, 0.0.0.0:15672->15672/tcp   some-rabbit
```
When I ran the application and went to `http://localhost:15672/` I could log into RabbitMQ with username/password: guest
and I saw that the "poll_queue" was created in "Queues and Streams".

For testing that MongoDB was working, I added a test-implementation to `build.gradle.kts`:
> testImplementation ("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.0.0")

And started creating a `@SpringBootTest` for testing that the `MongoDB` connection was working. After the test passed
I went back to `MongoDB Compass` and could see the newly generated `polls` collection. I connected the database to 
`IntelliJ` and could see the collection there as well.