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
> ./gradlew clean build
> 
> ./gradlew bootRun

## Run Dockerized Application 
Start Docker Desktop
> docker build -t feedapp-image1.3 .
> 
> docker run -d --name feedapp-backend -p 8080:8080 feedapp-image1.3:latest
