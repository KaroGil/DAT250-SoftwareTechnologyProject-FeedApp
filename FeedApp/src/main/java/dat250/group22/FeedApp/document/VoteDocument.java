package dat250.group22.FeedApp.document;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "votes")
@Data
public class VoteDocument {

    // MongoDB ID
    @Id
    private String id;

    // Store all ID's as String for MongoDB
    private String votedBy;
    private String pollId;
    private String voteOptionId;

    private Instant publishedAt;

    // Constructor
    public VoteDocument(String votedBy, String pollId, String voteOptionId, Instant publishedAt) {
        this.votedBy = votedBy;
        this.pollId = pollId;
        this.voteOptionId = voteOptionId;
        this.publishedAt = publishedAt;
    }
    public VoteDocument() { }
}