package dat250.group22.FeedApp.controller;


import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api/votes")
public class VoteController {

    private final DomainManager manager;

    public VoteController(DomainManager manager) { this.manager = manager; }

    @PostMapping("/addvote")
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

    @GetMapping
    public Collection<Vote> getVotes() { return manager.getVotes(); }

    @DeleteMapping("/delete/{voteId}")
    public void deleteVote(@PathVariable UUID voteId) { manager.removeVote(voteId); }

    @PutMapping("/update/{voteId}")
    public void updateVote(@PathVariable UUID voteId, @RequestBody Vote newVote) { manager.updateVote(voteId, newVote); }
}