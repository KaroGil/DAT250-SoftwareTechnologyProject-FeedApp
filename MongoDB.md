# Dockerize Application and Database
## Step 1: Pull Images from Docker
To pull the images from the Docker Hub, I ran the commands 
``` 
docker pull openjdk:21  // JAVA IMAGE
docker pull mongo       // MONGO IMAGE
```
and then I started the `MongoDB` container with `docker run --name mongodb -d mongo`.

## Step 2: Build the JAR
I *cd'ed* into the `FeedApp` directory, and ran the command `./gradlew bootJar` to configure a `.jar` file for the project
so that I could write the `Dockerfile`.

## Step 3: Create the Dockerfile
I created a new `Dockerfile` in the root repository, and added the lines 
``` 
FROM openjdk:21
COPY FeedApp/build/libs/*.jar feedapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "feedapp.jar"]
```

## Step 4: Create the docker-compose file
For this, I created another file in the root repository called `docker-compose.yaml` as a configuration file for Docker 
Compose to deploy, combine and configure a docker container for the application and for the database.

## Step 5: Build Docker Image for Project and Database & Create the Containers
To create the image for the project and database, I ran `docker-compose build`, and then I ran `docker-compose up -d` to
create their containers and run the project. This created a new container for MongoDB, so I had to check the IPAddresses 
with the `docker inspect`command 
``` 
$ docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED              STATUS              PORTS                      NAMES
580b52ff82be   mongo:latest   "docker-entrypoint.s…"   About a minute ago   Up About a minute   0.0.0.0:27017->27017/tcp   mongodb_container
11336e3e97f4   mongo          "docker-entrypoint.s…"   13 minutes ago       Up 13 minutes       27017/tcp                  mongodb

$ docker inspect 580b52ff82be | grep IPAddress
            "SecondaryIPAddresses": null,
            "IPAddress": "",
                    "IPAddress": "172.19.0.2",

$ docker inspect 11336e3e97f4 | grep IPAddress
            "SecondaryIPAddresses": null,
            "IPAddress": "172.17.0.2",
                    "IPAddress": "172.17.0.2",
```

The network is created by default, but I checked it by running `docker network ls`. 

## Configure MongoDB
I modified `application.properties` by adding the following lines 
```
server.port=8080

# MongoDB Configurations
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.host=${MONGODB_HOSTNAME}
spring.data.mongodb.port=${MONGODB_PORT}
spring.data.mongodb.database=${MONGODB_NAME}
```
## Create Database and User-Instance
In the `MongoDB Compass` desktop application, I created a new table called "feedapp_db" in the connection 
`localhost:27017`, and added a user-instance. 

## Start the Application
Run the command 
``` 
docker run -e MONGODB_HOSTNAME="172.19.0.2" \
           -e MONGODB_PORT="27017" \
           -e MONGODB_NAME="feedapp_db" \ 
           -e MONGODB_URI="mongodb://172.19.0.3:27017/feedapp" \ 
           -e MONGODB_USERNAME="admin" \
           -e MONGODB_PASSWORD="admin" \
           -p 8080:8080 \
           dat250-softwaretechnologyproject-feedapp-feedapp_image
```
or the one-liner
`docker run -e MONGODB_HOSTNAME="172.19.0.2" -e MONGODB_PORT="27017" -e MONGODB_NAME="feedapp_db" -e MONGODB_URI="mongodb://172.19.0.3:27017/feedapp" -e MONGODB_USERNAME="admin" -e MONGODB_PASSWORD="admin" -p 8080:8080 dat250-softwaretechnologyproject-feedapp-feedapp_image`
to start the application.

The arguments in the command above stands for 
```  
-e = Environemental variables for 
    - MONGODB_HOSTNAME  = IPAddress for hostport of container
    - MONGODB_PORT      = Port for MongoDB container
    - MONGODB_NAME      = Name of the database
    - MONGODB_URI       = URI identifier for database
    - MONGODB_USERNAME  = Username for database
    - MONGOSB_password  = Password for database 
-p                      = hostport:containerport
and lastly the image name 

```
