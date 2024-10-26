package dat250.group22.FeedApp.manager;


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import dat250.group22.FeedApp.model.Poll;
import dat250.group22.FeedApp.model.Vote;
import dat250.group22.FeedApp.model.VoteOption;
import dat250.group22.FeedApp.model.User;

@Component
public class DomainManager {
    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    // HashMaps to store the data
    HashMap<UUID, Poll> polls = new HashMap<>();
    HashMap<UUID, User> users = new HashMap<>();
    HashMap<UUID, Vote> votes = new HashMap<>();
    HashMap<UUID, VoteOption> voteOptions = new HashMap<>();

    // Vote methods 
    public void addVote(Vote vote) {
        votes.put(vote.getId(), vote);
        logger.info("Vote created: {}", vote);
    }

    public Collection<Vote> getVotes() {
        logger.info("Getting all votes...");
        if (votes.values().isEmpty()) {
            logger.info("No votes created yet.");
        }
        return  votes.values();
    }

    public Vote getVote(UUID voteId) {
        logger.info("Getting vote with id: {}", voteId);
        return votes.get(voteId);
    }

    public void removeAllVotes() {
        votes.clear();
        logger.info("Deleted all votes.");

    }

    public void removeVote(UUID voteId) {
        votes.remove(voteId);
        logger.info("Deleted vote with id: {}", voteId);
    }

    public void updateVote(UUID voteId, Vote newVote) {
        logger.info("Updating vote...");
        Vote existingVote = votes.get(voteId);
        if (existingVote != null) {
            logger.info("Updating vote: {} with new vote with voteOptionId: {}", voteId, newVote.getVoteOptionId());
    
            // Only update the voteOptionId, keep the other fields unchanged
            existingVote.setVoteOptionId(newVote.getVoteOptionId());
    
            // Save the updated vote
            votes.put(voteId, existingVote);
        } else {
            logger.info("Vote not found: {}", voteId);
            throw new IllegalArgumentException("Vote not found with id: " + voteId);
        }
    }

    public Vote findVoteByUserAndPoll (UUID votedBy, UUID pollId){
        logger.info("Finding vote by user id: {} and poll id: {}", votedBy, pollId);
        for (Vote vote : votes.values()){
            if (vote.getVotedBy().equals(votedBy) && vote.getPollId().equals(pollId)){
                return vote;
            }
        }
        return null;
    }


    // User methods
    public Collection<User> getUsers(){
        logger.info("Getting all users...");
        if (users.values().isEmpty()) {
            logger.info("No users created yet.");
        }
        return users.values();
    }

    public User getUser(UUID userId){
        logger.info("Getting user with id: {}", userId);
        return users.get(userId);
    }

    public void addUser(User user){
        for (User existingUser : users.values()){
            if (existingUser.getEmail().equals(user.getEmail())){
                throw new IllegalArgumentException("Email already in use: " + user.getEmail());
            }
        }
        users.put(user.getId(), user);
        logger.info("User created: {}", user);
    }

    public void deleteUser(UUID userId){
        users.remove(userId);
        logger.info("Deleted user with id: {}", userId);
    }

    public void updateUser(UUID userId, User user){
        logger.info("Updating user...");
        users.put(userId, user);
        logger.info("User with id: {} updated.", userId);
    }


    // Poll methods
    public Collection<Poll> getPolls() {
        logger.info("Getting polls...");
        if (polls.values().isEmpty()){
            logger.info("No polls created yet.");
        }
        return polls.values();
    }

    public void addPoll(Poll poll) {
        polls.put(poll.getId(),poll);
        logger.info("Poll created: {}", poll);
    }

    public Poll getPoll(UUID pollId) {
        logger.info("Getting poll with id: {}", pollId);
        return polls.get(pollId);
    }

    public void updatePoll(UUID pollId, Poll poll) {
        logger.info("Updating poll...");
        polls.put(pollId, poll);
        logger.info("Poll with id {} updated.", pollId);
    }

    public void removeAllPolls() {
        polls.clear();
        logger.info("Deleted all polls.");
    }

    public void removePoll(UUID pollId) {
        polls.remove(pollId);

        // Removes all votes associated with this poll
        votes.values().removeIf(vote -> (vote.getPollId()).equals(pollId));
        logger.info("Deleted poll with id: {}", pollId);
    }
}
