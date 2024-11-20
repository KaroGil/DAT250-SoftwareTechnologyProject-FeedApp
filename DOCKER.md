# Docker Set-Up
First of all, make sure to have `Docker Desktop` running.

## Test RabbitMQ Locally
Start RabbitMQ with Docker by opening a new terminal and running the following command to start a RabbitMQ
container with the management plugin enabled:
> docker run -d --hostname my-rabbit --name some-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:4.0.1-management

``` 
-d:                     Run the container in detached mode (in the background).
--hostname my-rabbit:   Sets a custom hostname for the RabbitMQ server.
--name some-rabbit:     Assigns a name to the container.
-p 5672:5672:           Maps RabbitMQ’s default port for AMQP (the protocol used for messaging) to the host.
-p 15672:15672:         Maps the management interface port to the host.
```
and then we checked that the container was running:
```
$ docker ps
CONTAINER ID   IMAGE                   COMMAND                  CREATED              STATUS              PORTS                                                                      
                                   NAMES
f1ad6a743a56   rabbitmq:3-management   "docker-entrypoint.s…"   About a minute ago   Up About a minute   4369/tcp, 5671/tcp, 0.0.0.0:5672->5672/tcp, 15671/tcp, 15691-15692/tcp, 25672/tcp, 0.0.0.0:15672->15672/tcp   some-rabbit
```
When we ran the application and went to `http://localhost:15672/` we could log into RabbitMQ with username/password: 
"guest".

## Create an Image of the Application 
Create the dockerfile for the image of the application
```Dockerfile 
# Use the official OpenJDK image as a base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /FeedApp

# Copy the JAR file into the container
COPY build/libs/FeedApp-0.0.1-SNAPSHOT.jar feedapp-docker.jar

# Expose the port your application will run on (default for Spring Boot is 8080)
EXPOSE 8080

# Run the JAR file to build the image of the app
CMD ["java","-jar","feedapp-docker.jar"]
```

Build the image with the command: `docker build -t feedapp-image1.5 .`
Run a container with the command: `docker run -d --name feedapp-backend -p 8080:8080 feedapp-image1.5:latest`
Then try to test it through Postman.

## Using docker-compose To Build The Application With RabbitMQ
Create the `docker-compose`-file:
```yaml
x-project:
  name: feedapp_project
services:
  feedapp:
    build:
      # Path to backend service
      context: ./FeedApp
    # Specify image- and container names for the feedapp service
    image: feedapp-image1.4:latest
    # Ensures that RabbitMQ is started and working before the backend
    depends_on:
      - rabbitmq
    ports:
      # Expose the backend on port 8080
      - "8080:8080"

  rabbitmq:
    image: rabbitmq:4.0.1-management
    container_name: feedapp-rabbit-docker
    ports:
      - "5672:5672"    # RabbitMQ port
      - "15672:15672"  # RabbitMQ management UI on port 15672
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
```

#### Docker commands:
```shell
$ docker ps                       # List running containers 
$ docker-compose up --build       # Run the Dockerized application and its dependencies
$ docker-compose down             # Shut down the Dockerized application and its dependencies
$ docker-compose restart feedapp  # Restart the Dockerized application without shutting down
$ docker stop dat250-softwaretechnologyproject-feedapp     # Stop the container

# Run the following commands in separate terminals to view logs while docker-compose is running
$ docker-compose logs -f feedapp  # Get logs for the application 
$ docker-compose logs -f rabbitmq # Get logs for RabbitMQ 
$ docker-compose logs -f mongodb  # Get logs for MongoDB 
```

## Test H2 Database Locally
`docker run --name feedapp-h2 -d buildo/h2database`
`docker run --name feedapp-h2 -p 8081:8081 -d buildo/h2database`
`docker run -e DATABASE_SERVER=jdbc:h2:mem:./fullstack_db -dp 8080:8080 feedapp-image1.4`

## Test MongoDB Locally