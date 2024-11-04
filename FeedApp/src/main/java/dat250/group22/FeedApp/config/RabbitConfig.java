package dat250.group22.FeedApp.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue pollQueue() {
        return new Queue("poll_queue", true); // 'true' makes the queue durable
    }
}