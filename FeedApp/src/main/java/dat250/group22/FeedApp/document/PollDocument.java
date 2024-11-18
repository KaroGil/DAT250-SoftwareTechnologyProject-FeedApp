package dat250.group22.FeedApp.document;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "polls")
@Data
public class PollDocument {

    // MongoDB ID
    @Id
    private String id;

    @Indexed(unique = true) // Index field, if queries on certain fields are frequent, this optimizes query performance
    private String creatorUserID;

    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private boolean isPublic;

    private List<VoteOptionDocument> options = new ArrayList<>();
}