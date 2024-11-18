package dat250.group22.FeedApp.service;

import dat250.group22.FeedApp.document.PollDocument;
import dat250.group22.FeedApp.document.VoteOptionDocument;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import dat250.group22.FeedApp.model.Poll;
import dat250.group22.FeedApp.model.VoteOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final RabbitTemplate rabbitTemplate;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AnalyticsService(RabbitTemplate rabbitTemplate, MongoTemplate mongoTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    public void sendAnalyticsData(Poll poll, Map<VoteOption, Integer> votes) {
        // Prepare the message payload with poll ID and vote counts
        Map<String, Object> message = new HashMap<>();
        message.put("type", "analytics");
        message.put("pollId", poll.getId().toString());
        message.put("question", poll.getQuestion());
        message.put("publishedAt", poll.getPublishedAt());
        message.put("validUntil", poll.getValidUntil());

        // List of maps, where each entry contains the vote option's ID, caption, and count
        List<Map<String, Object>> voteCounts = votes.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> voteData = new HashMap<>();
                    voteData.put("optionId", entry.getKey().getId().toString());
                    voteData.put("caption", entry.getKey().getCaption());
                    voteData.put("count", entry.getValue());
                    return voteData;
                })
                .collect(Collectors.toList());

        // Add the vote options and counts to the message
        message.put("votes", voteCounts);

        // Send the message to the "poll_queue" RabbitMQ queue
        rabbitTemplate.convertAndSend("poll_queue", "", message);

        // Save the poll data to MongoDB
        PollDocument pollDocument = new PollDocument();
        pollDocument.setId(String.valueOf(poll.getId()));
        pollDocument.setQuestion(poll.getQuestion());
        pollDocument.setPublishedAt(poll.getPublishedAt());
        pollDocument.setValidUntil(poll.getValidUntil());
        pollDocument.setPublic(poll.isPublic());

        // Create and add vote options to the document
        List<VoteOptionDocument> optionDocuments = votes.keySet().stream().map(integer -> {
            VoteOptionDocument optionDoc = new VoteOptionDocument();
            optionDoc.setId(String.valueOf(integer.getId()));
            optionDoc.setCaption(integer.getCaption());
            return optionDoc;
        }).collect(Collectors.toList());

        pollDocument.setOptions(optionDocuments);

        // Save to MongoDB
        mongoTemplate.save(pollDocument);
    }
}