package dat250.group22.FeedApp.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Data
@Table(name = "vote")
public class Vote implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long votedBy;
    private Long pollId;
    private Long voteOptionId;
    private Instant publishAt;

    public Vote(Long votedBy, Long voteOptionId, Long pollId) {
        this.votedBy = votedBy;
         this.voteOptionId = voteOptionId;
         this.pollId = pollId;
    }

    public Vote() { }
}
