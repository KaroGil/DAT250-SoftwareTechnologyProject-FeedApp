package dat250.group22.FeedApp.manager;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import dat250.group22.FeedApp.repository.*;
import dat250.group22.FeedApp.model.*;

@Component
public class DomainManager {
    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    // JPARepository for model classes
    private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public DomainManager(UserRepository userRepository, PollRepository pollRepository,
                         VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
    }

    // Vote methods
    public void addVote(Vote vote) {
        logger.info("Vote created: {}", vote);
        voteRepository.save(vote);
    }

    public Collection<Vote> getVotes() {
        logger.info("Getting all votes...");
        if (voteRepository.findAll().isEmpty()) {
            logger.info("No votes created yet.");
        }
        return voteRepository.findAll();
    }

    public Vote getVote(UUID voteId) {
        logger.info("Getting vote with id: {}", voteId);
        if (voteRepository.findById(voteId).isEmpty()) {
            logger.info("Vote with id: {} not found.", voteId);
        }
        return voteRepository.findById(voteId).get();
    }

    public void removeAllVotes(){
        logger.info("Removing all votes...");
        voteRepository.deleteAll();
        logger.info("Removed all votes.");
    }

    public void removeVote(UUID voteID) {
        logger.info("Removing vote with id: {}", voteID);
        voteRepository.deleteById(voteID);
        logger.info("Removed vote with id: {}", voteID);
    }

    public void updateVote(UUID voteID, Vote newVote) {
        logger.info("Updating vote..");

        // Find existing vote
        Vote existingVote = voteRepository.findById(voteID).get();

        if (voteRepository.findById(existingVote.getId()).isPresent()) {
            logger.info("Updating vote with id: {}", voteID);

            // Only update the voteOptionID, keep the other fields
            Vote voteToUpdate = voteRepository.findById(existingVote.getId()).get();
            voteToUpdate.setVoteOptionId(newVote.getVoteOptionId());

            // Save updated vote
            Vote updatedVote = voteRepository.save(voteToUpdate);
            logger.info("Updated vote with id {} with new voteOptionID {}", voteID, updatedVote.getVoteOptionId());
        } else {
            logger.warn("Vote with id {} not found.", voteID);
            throw new RuntimeException("Vote with id " + voteID + " not found.");
        }
    }

    public Vote findVoteByUserAndPoll(UUID votedBy, UUID pollID) {
        logger.info("Finding vote by user id {} and poll id {}", votedBy, pollID);
        for (Vote vote : voteRepository.findAll()) {
            if (vote.getVotedBy().equals(votedBy) && vote.getPollId().equals(pollID)) {
                return vote;
            }
        }
        logger.warn("Vote with user id {} and poll id {} not found.", votedBy, pollID);
        return null;
    }


    // User methods
    public void addUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()){
            logger.warn("User with email {} already exists.", user.getEmail());
            throw new RuntimeException("Email is already in use.");
        }
        userRepository.save(user);
        logger.info("User created: {}", user);
    }

    public Collection<User> getUsers() {
        logger.info("Getting all users...");
        if (userRepository.findAll().isEmpty()) {
            logger.info("No users created yet.");
        }
        return userRepository.findAll();
    }

    public User getUser(UUID userID) {
        logger.info("Getting user with id: {}", userID);
        if (userRepository.findById(userID).isEmpty()) {
            logger.info("User with id: {} not found.", userID);
        }
        return userRepository.findById(userID).get();
    }

    public void deleteAllUsers(){
        logger.info("Removing all users...");
        userRepository.deleteAll();
        logger.info("Removed all users.");
    }

    public void deleteUser(UUID userID) {
        logger.info("Removing user with id: {}", userID);
        userRepository.deleteById(userID);
        logger.info("Removed user with id: {}", userID);
    }

    public void updateUser(UUID userID, User newUser) {
        logger.info("Updating user with id: {}", userID);

        // Find the existing user
        Optional<User> existingUser = userRepository.findById(userID);

        if (existingUser.isEmpty()) {
            logger.info("User with id: {} not found.", userID);
        } else {
            // Copy existing fields to the new user
            User current = existingUser.get();

            // Update only the fields that are not null or empty into the new user
            current.setUsername(newUser.getUsername() != null ? newUser.getUsername() : current.getUsername());
            current.setEmail(newUser.getEmail() != null ? newUser.getEmail() : current.getEmail());
            current.setPassword(newUser.getPassword() != null ? newUser.getPassword() : current.getPassword());

            // Save the new user
            User updatedUser = userRepository.save(current);
            logger.info("Updated user with id {} to new user: {}", userID, updatedUser);
        }
    }

    public User login(String username, String password) {
        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }



    // Poll methods
    public void addPoll(Poll poll) {
        pollRepository.save(poll);
        logger.info("Poll created: {}", poll);
    }

    public Collection<Poll> getPolls() {
        logger.info("Getting all polls...");
        if (pollRepository.findAll().isEmpty()) {
            logger.info("No polls created yet.");
        }
        return pollRepository.findAll();
    }

    public Poll getPoll(UUID pollID) {
        logger.info("Getting poll with id: {}", pollID);
        if (pollRepository.findById(pollID).isEmpty()) {
            logger.info("Poll with id: {} not found.", pollID);
        }
        return pollRepository.findById(pollID).get();
    }

    public void deleteAllPolls(){
        logger.info("Removing all polls...");
        pollRepository.deleteAll();
        logger.info("Removed all polls.");
    }

    public void deletePoll(UUID pollID) {
        logger.info("Removing poll with id: {}", pollID);
        List<Vote> votesToDelete = voteRepository.findByPollId(pollID);

        if (!votesToDelete.isEmpty()){
            voteRepository.deleteAll(votesToDelete);
            logger.info("Removed {} votes associated with poll id: {}", votesToDelete.size(), pollID);
        }
        pollRepository.deleteById(pollID);
        logger.info("Removed poll with id: {}", pollID);
    }

    public void updatePoll(UUID pollID, Poll newPoll) {
        logger.info("Updating poll with id: {}", pollID);

        // Find the existing user
        Optional<Poll> existingPoll = pollRepository.findById(pollID);

        if (existingPoll.isEmpty()) {
            logger.info("Poll with id: {} not found.", pollID);
        } else {
            // Copy existing fields to the new poll
            Poll current = existingPoll.get();

            // Update only the fields that are not null or empty into the new user
            current.setQuestion(newPoll.getQuestion() != null ? newPoll.getQuestion() : current.getQuestion());
            current.setPublishedAt(newPoll.getPublishedAt() != null ? newPoll.getPublishedAt() : current.getPublishedAt());
            current.setValidUntil(newPoll.getValidUntil() != null ? newPoll.getValidUntil() : current.getValidUntil());
            current.setPublic(newPoll.isPublic());

            // Save the new poll
            Poll updatedPoll = pollRepository.save(current);
            logger.info("Updated poll with id {} to new poll: {}", pollID, updatedPoll);
        }
    }
}
