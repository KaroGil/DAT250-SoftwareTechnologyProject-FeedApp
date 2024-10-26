package dat250.group22.FeedApp.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "vote_option")
@Embeddable
public class VoteOption  implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String option;
    private int presentationOrder;
}