package dat250.group22.FeedApp.controller;


import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api/votes")
public class VoteController {

    private final DomainManager manager;

    @Autowired
    public VoteController(DomainManager manager) { this.manager = manager; }


    @PostMapping
    public ResponseEntity<String> createOrUpdateVote(@RequestBody Vote vote) {
        // Find the vote by user and poll (now using the poll object instead of pollId)
        Vote existingVote = manager.findVoteByUserAndPoll(vote.getVotedBy(), vote.getPoll().getId());

        if (existingVote != null){
            try{
                existingVote.setPublishAt(Instant.now());
                existingVote.setVoteOption(vote.getVoteOption());  // Set voteOption instead of voteOptionId
                manager.updateVote(existingVote.getId(), existingVote);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                // Create a new vote using the poll and voteOption objects
                Vote newVote = new Vote(vote.getVotedBy(), vote.getVoteOption(), vote.getPoll());
                newVote.setPublishAt(Instant.now());
                manager.addVote(newVote);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping
    public Collection<Vote> getVotes() {
        return manager.getVotes();
    }

    @DeleteMapping("/{voteId}")
    public void deleteVote(@PathVariable UUID voteId) {
        manager.removeVote(voteId);
    }

    @PutMapping("/{voteId}")
    public void updateVote(@PathVariable UUID voteId, @RequestBody Vote newVote) {
        manager.updateVote(voteId, newVote);
    }
}