# Use the official OpenJDK image as a base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /FeedApp

# Copy the JAR file into the container
COPY build/libs/FeedApp-0.0.1-SNAPSHOT.jar feedapp-docker.jar

# Copy the wait-for-it.sh script into the container and make it executable
COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh

# Expose the port your application will run on (default for Spring Boot is 8080)
EXPOSE 8080

# Run the JAR file when the container starts (wait for rabbitmq and mongodb before launching feedapp)
ENTRYPOINT ["sh", "-c", "/usr/local/bin/wait-for-it.sh rabbitmq:5672 -- /usr/local/bin/wait-for-it.sh mongodb:27017 -- java -jar feedapp-docker.jar"]