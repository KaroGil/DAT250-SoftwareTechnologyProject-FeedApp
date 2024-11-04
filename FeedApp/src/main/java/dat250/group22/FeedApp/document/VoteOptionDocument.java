package dat250.group22.FeedApp.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "voteoptions")
@Data
public class VoteOptionDocument {

    // MongoDB ID
    @Id
    private String id;

    private String caption;
    private int presentationOrder;
}
