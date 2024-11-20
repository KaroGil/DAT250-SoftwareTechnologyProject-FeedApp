package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.*;
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

    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private boolean isPublic;

    // One-to-Many relationship with VoteOption
    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VoteOption> options = new HashSet<>();

    // Many-to-One relationship with User (creator of the poll)
    @ManyToOne
    @JoinColumn(name = "creator_user_id")  // Foreign key for the creator
    private User creatorUser;
}