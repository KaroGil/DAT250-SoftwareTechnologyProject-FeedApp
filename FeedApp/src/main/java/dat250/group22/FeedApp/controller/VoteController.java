package dat250.group22.FeedApp.controller;


import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Vote;
import dat250.group22.FeedApp.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    @Autowired
    public VoteController(DomainManager manager) { this.manager = manager; }

    @PostMapping
    public ResponseEntity<String> createOrUpdateVote(@RequestHeader("Authorization") String token, @RequestBody Vote vote) {
        logger.info("Received Authorization header: {}", token);
        String jwt = token.replace("Bearer ", "").trim();
        logger.info("JWT extracted: {}", jwt);
        UUID userId = JwtService.parseToken(jwt);
        logger.info("UserId from token: {}", userId);
        vote.setVotedBy(userId);

        logger.info("Creating or updating vote: {}", vote);
        Vote existingVote = manager.findVoteByUserAndPoll(vote.getVotedBy(), vote.getPollId());
        if (existingVote != null){
            try{
                existingVote.setPublishAt(Instant.now());
                existingVote.setVoteOptionId(vote.getVoteOptionId());
                manager.updateVote(existingVote.getId(), existingVote);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                Vote newVote = new Vote(vote.getVotedBy(), vote.getVoteOptionId(), vote.getPollId());
                newVote.setPublishAt(Instant.now());
                manager.addVote(newVote);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping
    public Collection<Vote> getVotes() { return manager.getVotes(); }

    @DeleteMapping("/{voteId}")
    public void deleteVote(@PathVariable UUID voteId) { manager.removeVote(voteId); }

    @PutMapping("/{voteId}")
    public void updateVote(@PathVariable UUID voteId, @RequestBody Vote newVote) { manager.updateVote(voteId, newVote); }
}