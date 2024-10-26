package dat250.group22.FeedApp.controller;

import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Poll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping("/api/polls")
public class PollController {

    private final DomainManager manager;
    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    public PollController(DomainManager manager){
        this.manager = manager;
    }

    @GetMapping
    public Collection<Poll> getAllPolls() {
        logger.info("Getting users...");
        return manager.getPolls();
    }

    @PostMapping
    public ResponseEntity<String> createPoll(@RequestBody Poll poll) {
        try {
            manager.addPoll(poll);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/get/{pollId}")
    public Poll getPoll(@PathVariable Long pollId) {
        return manager.getPoll(pollId);
    }

    @DeleteMapping("/delete/polls")
    public void deleteAllPolls() {
        manager.removeAllPolls();
    }

    @DeleteMapping("delete/{pollId}")
    public void deletePoll(@PathVariable Long pollId) {
        manager.removePoll(pollId);
    }

    @PutMapping("/update/{pollId}")
    public void updatePoll(@PathVariable Long pollId, @RequestBody Poll poll) {
        logger.info("Updating poll...");
        manager.updatePoll(pollId, poll);
        logger.info("Poll with id {} updated.", pollId);
    }
}