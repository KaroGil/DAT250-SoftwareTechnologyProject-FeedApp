package dat250.group22.FeedApp.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AnalyticsListener {
    // Consumer class for analytics data
    @RabbitListener(queues = "poll_queue")
    public void receiveAnalyticsData(Map<String, Object> message) {

        // Process the analytics data
        System.out.println("Received analytics data: " + message);
        //TODO: Handle the message, e.g., save it to MongoDB
    }
}