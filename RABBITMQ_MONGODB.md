### RabbitMQ Set-Up
First I added the following dependency in the `build.gradle.kts` file:
>implementation("org.springframework.boot:spring-boot-starter-amqp")

Then I configured the `RabbitMQ` connection in `application.properties`:
>spring.rabbitmq.host=localhost
>spring.rabbitmq.port=5672
>spring.rabbitmq.username=admin_pollapp
>spring.rabbitmq.password=admin_pollapp

After which I created a RabbitMQ configuration class and an AnalyticsComponent class. 