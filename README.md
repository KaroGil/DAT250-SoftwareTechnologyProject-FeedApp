# DAT250-SoftwareTechnologyProject-FeedApp
**Group members**: Kaja, Karolina, Mampenda, Mina

This is the main repository for the DAT250 group project. It will contain both the frontend and backend code for a 
FeedApp developed in these technologies:
- Java with Springboot
- Gradle
- Spring Web MVC for implementing HTTP/REST API
- VUE for frontend
- Jakarta Persistence API with Hibernate and H2 database
- MongoDB as NoSQL database
- RabbitMQ
- Docker

---

## Run Locally 
Run RabbitMQ container, then build project and run it
```
docker run -d --hostname my-rabbit --name some-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:4.0.1-management
./gradlew clean build
./gradlew bootRun
```

## Run Dockerized Application 
Start Docker Desktop
```shell
docker build -t feedapp-image1.5 .
docker run -d --name feedapp-backend -p 8080:8080 feedapp-image1.5:latest
```

#### Docker commands:
```shell
$ docker run -d --name feedapp-backend -p 8080:8080 feedapp-image1.3:latest # Run the image of the application
$ docker images                   # List running images
$ docker-compose up --build       # Run the Dockerized application and its dependencies
$ docker ps                       # List running containers 

# Run the following commands in separate terminals to view logs while docker-compose is running
$ docker-compose logs -f feedapp  # Get logs for the application 
$ docker-compose logs -f rabbitmq # Get logs for RabbitMQ 
$ docker-compose logs -f mongodb  # Get logs for MongoDB 


$ docker-compose down             # Shut down the Dockerized application and its dependencies
$ docker-compose restart feedapp  # Restart the Dockerized application without shutting down
```