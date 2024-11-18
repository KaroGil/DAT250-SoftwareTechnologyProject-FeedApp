package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "voteoptions")
public class VoteOption implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String caption;
    private int presentationOrder;

    // One-to-Many relationship with Vote
    @OneToMany(mappedBy = "voteOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vote> votes = new HashSet<>();

    // Many-to-One relationship with Poll
    @ManyToOne
    @JoinColumn(name = "poll_id")  // Foreign key for the poll
    private Poll poll;
}