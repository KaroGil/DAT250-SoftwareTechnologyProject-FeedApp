#!/usr/bin/env bash

wait_for() {
  local HOST=$1
  local PORT=$2
  local SERVICE_NAME=$3

  echo "Waiting for $SERVICE_NAME on $HOST:$PORT..."
  for attempt in $(seq 1 12); do # Retry up to 12 times (1 minute)
    nc -z "$HOST" "$PORT" > /dev/null 2>&1
    if [ $? -eq 0 ]; then
      echo "$SERVICE_NAME is ready on $HOST:$PORT!"
      return 0
    fi
    echo "Attempt $attempt/12: $SERVICE_NAME not ready yet. Retrying in 10 seconds..."
    sleep 10
  done
  echo "ERROR: $SERVICE_NAME failed to become ready on $HOST:$PORT after 12 attempts."
  return 1
}

# Wait for both services in parallel
(
  wait_for "rabbitmq" "5672" "RabbitMQ" &
#  wait_for "mongodb" "27017" "MongoDB" &
  wait
)

echo "Both services are ready. Starting FeedApp..."
exec java -jar feedapp-docker.jar
