package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "votes")
public class Vote implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID votedBy;
    private Instant publishAt;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "vote_option_id")
    private VoteOption voteOption;

    public Vote(UUID votedBy, VoteOption voteOption, Poll poll) {
        this.votedBy = votedBy;
        this.voteOption = voteOption;
        this.poll = poll;
    }

    public Vote() { }
}