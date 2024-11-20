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


# Run the JAR file when the container starts
#ENTRYPOINT ["java", "-cp", "feedapp-docker.jar", "FeedAppApplication"]