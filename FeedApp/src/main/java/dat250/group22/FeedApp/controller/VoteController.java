package dat250.group22.FeedApp.controller;

import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;


@RestController
@CrossOrigin
@RequestMapping("/api/votes")
public class VoteController {

    private final DomainManager manager;
    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    public VoteController(DomainManager manager) {
        this.manager = manager;
    }

    @GetMapping
    public Collection<Vote> getVotes() {
        logger.info("Getting votes...");
        return manager.getAllVotes();
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdateVote(@RequestBody Vote vote) {
        Vote existingVote = manager.findVoteByUserAndPoll(vote.getVotedBy(), vote.getPollId());

        if (existingVote != null){
            try{
                existingVote.setVoteOptionId(vote.getVoteOptionId());
                manager.updateVote(existingVote.getId(), existingVote);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                Vote newVote = new Vote(vote.getVotedBy(), vote.getVoteOptionId(), vote.getPollId());
                manager.addVote(newVote);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @DeleteMapping("delete/{voteId}")
    public void deleteVote(@PathVariable Long voteId) {
        manager.removeVote(voteId);
    }

    @PutMapping("update/{voteId}")
    public void updateVote(@PathVariable Long voteId, @RequestBody Vote newVote) {
        logger.info("Updating vote...");
        manager.updateVote(voteId, newVote);
        logger.info("Vote with id {} updated.", voteId);

    }
}
