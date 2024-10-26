package dat250.group22.FeedApp.controller;

import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final DomainManager manager;
    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    public UserController(DomainManager manager) {
        this.manager = manager;
    }

    @GetMapping()
    public Collection<User> getUsers() {
        logger.info("Getting users...");
        return manager.getUsers();
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            manager.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/get/{userId}")
    public User getUser(@PathVariable Long userId) {
        return manager.getUser(userId);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        manager.deleteUser(userId);
    }

    @PutMapping("/update/{userId}")
    public void updateUser(@PathVariable Long userId, @RequestBody User newUser) {
        logger.info("Updating user...");
        manager.updateUser(userId, newUser);
        logger.info("User with id: {} updated.", userId);
    }
}