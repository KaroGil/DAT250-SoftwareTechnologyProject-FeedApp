package dat250.group22.FeedApp.controller;

import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Poll;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/polls")
public class PollController {

    private final DomainManager manager;

    public PollController(DomainManager manager){
        this.manager = manager;
    }

    @GetMapping
    public Collection<Poll> getAllPolls() {
        return manager.getAllPolls();
    }

    @PostMapping
    public void createPoll(@RequestBody Poll poll) {
        manager.addPoll(poll);
    }

    @GetMapping("/{pollId}")
    public Poll getPoll(@PathVariable UUID pollId) {
        return manager.getPoll(pollId);
    }

    @PutMapping("/{pollId}")
    public void updatePoll(@PathVariable UUID pollId, @RequestBody Poll poll) {
        manager.updatePoll(pollId, poll);
    }

    @DeleteMapping
    public void deleteAllPolls() {
        manager.removeAllPolls();
    }

    @DeleteMapping("/{pollId}")
    public void deletePoll(@PathVariable UUID pollId) {
        manager.removePoll(pollId);
    }
}
