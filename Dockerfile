FROM openjdk:21
COPY FeedApp/build/libs/*.jar feedapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "feedapp.jar"]
