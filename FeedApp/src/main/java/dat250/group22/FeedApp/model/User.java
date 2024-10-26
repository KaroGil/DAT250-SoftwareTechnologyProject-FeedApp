package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Entity
@Data
@Table(name = "user_entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    private String username;
    private String email;
    private String password;
}
