package dat250.group22.FeedApp.manager;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import java.util.Collection;
import java.util.UUID;
import java.util.HashMap;

import dat250.group22.FeedApp.model.Poll;
import dat250.group22.FeedApp.model.Vote;
import dat250.group22.FeedApp.model.VoteOption;
import dat250.group22.FeedApp.model.User;

@Component
public class DomainManager {
    
    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    // HashMaps to store the data
    HashMap<UUID, Poll> polls = new HashMap<UUID, Poll>();
    HashMap<UUID, User> users = new HashMap<UUID, User>();
    HashMap<UUID, Vote> votes = new HashMap<UUID, Vote>();
    HashMap<UUID, VoteOption> voteOptions = new HashMap<UUID, VoteOption>();


    // Vote methods 
    public void addVote(Vote vote) {
        logger.info("Adding vote: " + vote);
        votes.put(vote.getId(), vote);
    }

    public Collection<Vote> getAllVotes() {
        logger.info("Getting all votes");
        return  votes.values();
    }

    public Vote getVote(UUID voteId) {
        logger.info("Getting vote: " + voteId);
        return votes.get(voteId);
    }

    public void removeAllVotes() {
        votes.clear();
    }

    public void removeVote(UUID voteId) {
        logger.info("Removing vote: " + voteId);
        votes.remove(voteId);
    }

    public void updateVote(UUID voteId, Vote newVote) {
        logger.info("Updating vote: " + voteId + " with " + newVote);
        Vote existingVote = votes.get(voteId);
        if (existingVote != null) {
            logger.info("Updating vote: " + voteId + " with new voteOptionId: " + newVote.getVoteOptionId());
    
            // Only update the voteOptionId, keep the other fields unchanged
            existingVote.setVoteOptionId(newVote.getVoteOptionId());
    
            // Save the updated vote
            votes.put(voteId, existingVote);
        } else {
            logger.info("Vote not found: " + voteId);
            throw new IllegalArgumentException("Vote not found with id: " + voteId);
        }
    }


    // User methods
    public Collection<User> getUsers(){
        return users.values();
    }

    public User getUser(UUID userId){
        return users.get(userId);
    }

    public void addUser(User user){
        users.put(user.getId(), user);
    }

    public void deleteUser(UUID userId){
        users.remove(userId);
    }

    public void updateUser(UUID userId, User user){
        logger.info("Updating user: " + user.getName());
        users.put(userId, user);
    }



    // Poll methods
    public Collection<Poll> getAllPolls() {
        return polls.values();
    }

    public void addPoll(Poll poll) {
        polls.put(poll.getId(),poll);
    }

    public Poll getPoll(UUID pollId) {
        return polls.get(pollId);
    }

    public void updatePoll(UUID pollId, Poll poll) {
        polls.put(pollId, poll);
    }

    public void removeAllPolls() {
        polls.clear();
    }

    public void removePoll(UUID pollId) {
        polls.remove(pollId);

        // Removes all votes associated with this poll
        votes.values().removeIf(vote -> (vote.getPollId()).equals(pollId));
    }

}
