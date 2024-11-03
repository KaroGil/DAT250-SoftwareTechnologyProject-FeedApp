package dat250.group22.FeedApp.document;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Document(collection = "polls")
@Data
public class PollDocument {

    // MongoDB ID
    @Id
    private String id;

    // Store all ID's as String for MongoDB
    private String creatorUserID;
    private String question;

    private Instant publishedAt;
    private Instant validUntil;
    private boolean isPublic;

    // Do I not need annotations to connect the id's for the polls to the id's of the cote options in MongoDB like I do in Spring Data?
    private List<VoteOptionDocument> options = new ArrayList<>();
}
