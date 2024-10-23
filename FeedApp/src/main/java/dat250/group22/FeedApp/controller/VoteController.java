package dat250.group22.FeedApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dat250.group22.FeedApp.manager.DomainManager;
import dat250.group22.FeedApp.model.Vote;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Collection;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api/vote")
public class VoteController {
    private final DomainManager manager;

    public VoteController(DomainManager manager) {
        this.manager = manager;
    }

    @GetMapping
    public Collection<Vote> getVotes() {
        return manager.getAllVotes();
    }

    @PostMapping
    public void createOrUpdateVote(@RequestBody Vote vote) {
        Vote existingVote = manager.findVoteByUserAndPoll(vote.getVotedBy(), vote.getPollId());
        if (existingVote != null){
            existingVote.setVoteOptionId(vote.getVoteOptionId());
            manager.updateVote(existingVote.getId(), existingVote);
        }else {
            Vote newVote = new Vote(vote.getVotedBy(), vote.getVoteOptionId(), vote.getPollId());
            manager.addVote(newVote);
        }
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
