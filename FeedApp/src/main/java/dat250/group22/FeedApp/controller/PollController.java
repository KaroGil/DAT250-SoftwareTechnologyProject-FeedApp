package dat250.group22.FeedApp.controller;


import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/polls")
public class PollController {

    private final DomainManager manager;

    @Autowired
    public PollController(DomainManager manager){ this.manager = manager; }

    @PostMapping("/addpoll")
    public ResponseEntity<String> createPoll(@RequestBody Poll poll) {
        try {
            manager.addPoll(poll);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping
    public Collection<Poll> getPolls() { return manager.getPolls(); }

    @GetMapping("/get/{pollId}")
    public Poll getPoll(@PathVariable UUID pollId) { return manager.getPoll(pollId); }

    @DeleteMapping("/delete/polls")
    public void deleteAllPolls() { manager.deleteAllPolls(); }

    @DeleteMapping("/delete/{pollId}")
    public void deletePoll(@PathVariable UUID pollId) { manager.deletePoll(pollId); }

    @PutMapping("/update/{pollId}")
    public void updatePoll(@PathVariable UUID pollId, @RequestBody Poll poll) { manager.updatePoll(pollId, poll); }
}