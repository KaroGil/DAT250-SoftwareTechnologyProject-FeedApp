package dat250.group22.FeedApp.controller;


import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.User;
import dat250.group22.FeedApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final DomainManager manager;

    @Autowired
    public UserController(DomainManager manager) { this.manager = manager; }

    @PostMapping("/adduser")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            manager.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping()
    public Collection<User> getUsers() { return manager.getUsers(); }

    @GetMapping("/get/{userId}")
    public User getUser(@PathVariable UUID userId) { return manager.getUser(userId); }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable UUID userId) { manager.deleteUser(userId); }

    @PutMapping("/update/{userId}")
    public void updateUser(@PathVariable UUID userId, @RequestBody User newUser) { manager.updateUser(userId, newUser); }
}