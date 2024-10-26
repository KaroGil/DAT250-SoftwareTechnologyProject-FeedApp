package dat250.group22.FeedApp.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "voteoption")
public class VoteOption  implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;
    private int presentationOrder;
}