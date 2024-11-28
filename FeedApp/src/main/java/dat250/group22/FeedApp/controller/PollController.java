package dat250.group22.FeedApp.controller;


import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Poll;
import dat250.group22.FeedApp.util.JWTUtil;
import dat250.group22.FeedApp.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.UUID;
import java.time.Instant;

@RestController
@CrossOrigin
@RequestMapping("/api/polls")
public class PollController {
    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    private final DomainManager manager;

    @Autowired
    public PollController(DomainManager manager){ this.manager = manager; }

    @PostMapping
    public ResponseEntity<String> createPoll( @RequestHeader("Authorization") String token, @RequestBody Poll poll) {
        try {
            logger.info("Received Authorization header: {}", token);
            String jwt = token.replace("Bearer ", "").trim();
            logger.info("JWT extracted: {}", jwt);
            UUID userId = JwtService.parseToken(jwt);
            logger.info("UserId from token: {}", userId);
            poll.setCreatorUserID(userId);

            poll.setPublishedAt(Instant.now());
            manager.addPoll(poll);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping
    public Collection<Poll> getPolls() { return manager.getPolls(); }

    @GetMapping("/{pollId}")
    public Poll getPoll(@PathVariable UUID pollId) { return manager.getPoll(pollId); }

    @DeleteMapping
    public void deleteAllPolls() {
        manager.deleteAllPolls();
    }

    @DeleteMapping("/{pollId}")
    public void deletePoll(@RequestHeader("Authorization") String token, @PathVariable UUID pollId) {
        String jwt = token.replace("Bearer ", "").trim();
        UUID userId = JwtService.parseToken(jwt);
        logger.info("UserId from token: {}", userId);
        logger.info("PollId from token: {}", pollId);
        logger.info("UserId equals to pollcreatorid: {}", userId == getPoll(pollId).getCreatorUserID());
        if(userId.equals(getPoll(pollId).getCreatorUserID())){
            manager.deletePoll(pollId);
        }
    }

    @PutMapping("/{pollId}")
    public void updatePoll(@PathVariable UUID pollId, @RequestBody Poll poll) { manager.updatePoll(pollId, poll); }
}