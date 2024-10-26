package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "user_entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
}
