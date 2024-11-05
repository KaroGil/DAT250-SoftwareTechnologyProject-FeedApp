package dat250.group22.FeedApp.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "polls")
public class Poll implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID creatorUserID;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private boolean isPublic;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poll_id")
    private Set<VoteOption> options = new HashSet<>();

}