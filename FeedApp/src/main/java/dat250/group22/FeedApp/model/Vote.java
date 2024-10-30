package dat250.group22.FeedApp.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "votes")
public class Vote implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID votedBy;
    private Instant publishAt;
    private UUID pollId;
    private UUID voteOptionId;

    public Vote(UUID votedBy, UUID voteOptionId, UUID pollId) {
        this.votedBy = votedBy;
         this.voteOptionId = voteOptionId;
         this.pollId = pollId;
    }

    public Vote() { }
}
