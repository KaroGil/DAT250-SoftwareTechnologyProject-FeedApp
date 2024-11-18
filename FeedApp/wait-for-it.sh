#!/usr/bin/env bash

# Function that waits for services to become available on a specific port
wait_for() {
  local HOST=$1
  local PORT=$2
  local SERVICE_NAME=$3

  echo "Waiting for $SERVICE_NAME on $HOST:$PORT to be ready..."
  for i in $(seq 90); do # Wait for 90 seconds before trying to connect to port
    nc -z "$HOST" "$PORT" > /dev/null 2>&1
    result=$?
    if [ $result -eq 0 ]; then
      echo "$SERVICE_NAME is available on $HOST:$PORT!"
      return 0
    fi
    sleep 5 # Sleep for 5 seconds before trying again
  done
  echo "$SERVICE_NAME is not available on $HOST:$PORT after 3 minutes."
  return 1
}

# Wait for RabbitMQ and MongoDB
wait_for "rabbitmq" "5672" "RabbitMQ"
wait_for "mongodb" "27017" "MongoDB"

# Once both services are available, start the application
echo "Both services are ready, starting FeedApp..."
exec java -jar feedapp-docker.jar