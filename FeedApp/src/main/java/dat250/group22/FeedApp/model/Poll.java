package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "poll")
public class Poll implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID creatorUserID;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private boolean state;

    @ElementCollection
    @CollectionTable(name = "poll_options", joinColumns = @JoinColumn(name = "poll_id"))
    private Set<VoteOption> options;

    public boolean getState() { return state; }
}