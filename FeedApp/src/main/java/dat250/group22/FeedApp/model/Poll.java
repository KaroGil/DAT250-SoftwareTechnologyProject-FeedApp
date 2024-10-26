package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;

@Entity
@Data
@Table(name = "poll")
public class Poll implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long creatorUserID;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private boolean state;

    @ElementCollection
    private ArrayList<VoteOption> options;


    public boolean getState() {
        return state;
    }
}