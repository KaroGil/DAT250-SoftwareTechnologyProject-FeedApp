package dat250.group22.FeedApp.controller;

import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.User;
import dat250.group22.FeedApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    private final DomainManager manager;

    @Autowired
    public UserController(DomainManager manager) { this.manager = manager; }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            manager.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public Collection<User> getUsers() { return manager.getUsers(); }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable UUID userId) { return manager.getUser(userId); }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable UUID userId) { manager.deleteUser(userId); }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable UUID userId, @RequestBody User newUser) { manager.updateUser(userId, newUser); }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        if(user == null || user.getUsername() == null || user.getPassword() == null) {
            logger.info("user/name/pass is null");
            return null;
        }
        logger.info("user gotten for login: " + user.getUsername() + ", password: " + user.getPassword());
        logger.info("logging in " + user.getUsername());
        User userFound = manager.login(user.getUsername(), user.getPassword());
        if(userFound == null) {
            logger.info("user not found");
            return null;
        }
        logger.info("user found: " + userFound.getUsername());

        return userFound;
    }
}
