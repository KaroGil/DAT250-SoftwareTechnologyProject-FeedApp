package dat250.group22.FeedApp.document;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.UUID;

@Document(collection = "users")
@Data
public class UserDocument {
    @Id
    private String id; // MongoDB ID
    private String username;
    private String email;
    private String password;
}