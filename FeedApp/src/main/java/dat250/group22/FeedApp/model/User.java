package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String email;
    private String password;
}
