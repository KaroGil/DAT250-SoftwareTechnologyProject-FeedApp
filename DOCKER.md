### Docker Set-Up
To implement `Docker`, the first thing to do was create a script that would make `Docker` wait to launch the application
until the other images were up and running on their respective ports. This was done by creating a `wait-for-it.sh` 
script in the same directory as where the `Dockerfile` would be, and write a short program for it: 
```shell
#!/usr/bin/env bash

# Function that waits for services to become available on a specific port
wait_for() {
  local HOST=$1
  local PORT=$2
  local SERVICE_NAME=$3

  echo "Waiting for $SERVICE_NAME on $HOST:$PORT to be ready..."
  for i in $(seq 30); do
    nc -z "$HOST" "$PORT" > /dev/null 2>&1
    result=$?
    if [ $result -eq 0 ]; then
      echo "$SERVICE_NAME is available on $HOST:$PORT!"
      return 0
    fi
    sleep 1
  done
  echo "$SERVICE_NAME is not available on $HOST:$PORT after 30 seconds."
  return 1
}

# Wait for RabbitMQ and MongoDB
wait_for "rabbitmq" "5672" "RabbitMQ"
wait_for "mongodb" "27017" "MongoDB"

# Once both services are available, start the application
echo "Both services are ready, starting FeedApp..."
exec java -jar feedapp-docker.jar
```

The second thing that was needed was a `Dockerfile` and a `docker-compose.yml`-file. 
They were implemented as follows: 
```Dockerfile 
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
```

```yaml
services:
  rabbitmq:
    image: rabbitmq:4.0.1-management
    container_name: rabbitmq-feedapp
    ports:
      - "5672:5672"    # RabbitMQ port
      - "15672:15672"  # RabbitMQ management UI on port 15672
    environment:
      RABBITMQ_DEFAULT_USER: rabbitmq_admin
      RABBITMQ_DEFAULT_PASS: rabbitmq_admin
    networks:
      - feedapp-network  # Connect to the 'feedapp-network'

  mongodb:
    image: mongo:latest
    container_name: mongodb-feedapp
    environment:
      MONGO_INITDB_ROOT_USERNAME: mongodb_admin
      MONGO_INITDB_ROOT_PASSWORD: mongodb_admin
      MONGO_INITDB_DATABASE: feedapp_mongodb
    ports:
      - "27017:27017"  # Expose MongoDB on port 27017
    networks:
      - feedapp-network  # Connect to the 'feedapp-network'

  feedapp:
    build:
      context: ./FeedApp  # Path to backend service
    image: dat250-feedapp:latest  # Specify image name for the feedapp service
    container_name: feedapp-container
    ports:
      - "8080:8080"  # Expose the backend on port 8080
    depends_on:
      - mongodb      # Ensures that MongoDB is started before the backend
      - rabbitmq     # Ensures that RabbitMQ is started before the backend
    command: ["java", "-jar", "feedapp-docker.jar"]
    # Make sure the JAR file path is correct
    networks:
      - feedapp-network  # Connect to the 'feedapp-network'

networks:
  feedapp-network:
    driver: bridge  # Use the bridge driver for inter-service communication
```