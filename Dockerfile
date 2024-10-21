FROM gradle:8.10.2-jdk21 AS builder
COPY FeedApp-0.0.1-SNAPSHOT.jar feedapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "feedapp.jar"]
