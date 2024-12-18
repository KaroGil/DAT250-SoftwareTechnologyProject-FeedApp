package dat250.group22.FeedApp.controller;

import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.User;
import dat250.group22.FeedApp.util.JWTUtil;
import dat250.group22.FeedApp.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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


    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            logger.info("Removed Bearer from token: {}", jwtToken);
            logger.info("Token as bytes: {}", Arrays.toString(jwtToken.getBytes()));

            UUID userId = JwtService.parseToken(jwtToken); // Extract userId from the token
            logger.info("Parsed userId: {}", userId);
            User currentUser = manager.getUser(userId);
            return ResponseEntity.ok(currentUser);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable UUID userId) { manager.deleteUser(userId); }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable UUID userId, @RequestBody User newUser) { manager.updateUser(userId, newUser); }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        if(user == null || user.getEmail() == null || user.getPassword() == null) {
            logger.info("user/email/pass is null");
            return null;
        }
        logger.info("user gotten for login: {}, password: {}", user.getEmail(), user.getPassword());
        logger.info("logging in {}", user.getEmail());
        User userFound = manager.login(user.getEmail(), user.getPassword());
        if(userFound == null) {
            logger.info("user not found");
            return null;
        }
        logger.info("user found: {}", userFound.getEmail());

        // Create JWT token for the logged in user
        String token = JWTUtil.generateToken(userFound.getId());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
