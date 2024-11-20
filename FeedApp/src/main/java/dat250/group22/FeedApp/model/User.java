package dat250.group22.FeedApp.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
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

    // One-to-Many relationship with Poll
    @OneToMany(mappedBy = "creatorUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Poll> polls = new HashSet<>();
}