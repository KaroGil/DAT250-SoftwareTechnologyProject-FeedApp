package dat250.group22.FeedApp.manager;


import dat250.group22.FeedApp.service.AnalyticsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import dat250.group22.FeedApp.repository.*;
import dat250.group22.FeedApp.model.*;
import dat250.group22.FeedApp.document.*;

@Component
public class DomainManager {
    private static final Logger logger = LoggerFactory.getLogger(DomainManager.class);

    // JPARepository for entity classes
    private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;

    // MongoRepository for document classes
    private final UserDocumentRepository userDocumentRepository;
    private final PollDocumentRepository pollDocumentRepository;
    private final VoteDocumentRepository voteDocumentRepository;

    // Service to send analytics data to RabbitMQ
    private final AnalyticsService analyticsService;

    // Constructor-based dependency injection for repositories and analytics service
    @Autowired
    public DomainManager(UserRepository userRepository, PollRepository pollRepository, VoteRepository voteRepository,
                         UserDocumentRepository userDocumentRepository,
                         PollDocumentRepository pollDocumentRepository,
                         VoteDocumentRepository voteDocumentRepository,
                         AnalyticsService analyticsService) {
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
        this.userDocumentRepository = userDocumentRepository;
        this.pollDocumentRepository = pollDocumentRepository;
        this.voteDocumentRepository = voteDocumentRepository;
        this.analyticsService = analyticsService;
    }


    /* Analytics method */
    private void collectAnalyticsData(Vote vote) {
        // Retrieve the poll using the poll ID from the vote
        Poll poll = getPoll(vote.getPoll().getId());

        // Create a map of VoteOptions with their vote counts for analytics
        Map<VoteOption, Integer> votes = poll.getOptions().stream()
                .collect(Collectors.toMap(option -> option, option -> {
                    // Count votes for each option
                    return (int) voteRepository.findAll().stream()
                            .filter(v -> v.getVoteOption().getId().equals(option.getId()))
                            .count(); // Count the number of votes for this option
                }));

        // Send the analytics data through the AnalyticsService
        analyticsService.sendAnalyticsData(poll, votes);
    }


    /* Vote Entity methods */
    public void addVote(Vote vote) {
        logger.info("Vote created: {}", vote);
        voteRepository.save(vote);
        collectAnalyticsData(vote);
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
            voteToUpdate.setVoteOption(newVote.getVoteOption());

            // Save updated vote
            Vote updatedVote = voteRepository.save(voteToUpdate);
            logger.info("Updated vote with id {} with new voteOptionID {}", voteID, updatedVote.getVoteOption().getId());
        } else {
            logger.warn("Vote with id {} not found.", voteID);
            throw new RuntimeException("Vote with id " + voteID + " not found.");
        }
    }

    public Vote findVoteByUserAndPoll(UUID votedBy, UUID pollID) {
        logger.info("Finding vote by user id {} and poll id {}", votedBy, pollID);
        for (Vote vote : voteRepository.findAll()) {
            if (vote.getVotedBy().equals(votedBy) && vote.getPoll().getId().equals(pollID)) {
                return vote;
            }
        }
        logger.warn("Vote with user id {} and poll id {} not found.", votedBy, pollID);
        return null;
    }


    /* Vote Document methods */
    public void addVoteDocument(VoteDocument voteDocument) {
        voteDocumentRepository.save(voteDocument);
        logger.info("VoteDocument created: {}", voteDocument);
    }

    public Collection<VoteDocument> getVoteDocuments() {
        logger.info("Getting all VoteDocuments...");
        return voteDocumentRepository.findAll();
    }

    public VoteDocument getVoteDocument(String voteId) {
        logger.info("Getting VoteDocument with id: {}", voteId);
        return voteDocumentRepository.findById(voteId).orElse(null);
    }

    public void deleteVoteDocument(String voteId) {
        logger.info("Deleting VoteDocument with id: {}", voteId);
        voteDocumentRepository.deleteById(voteId);
        logger.info("VoteDocument with id {} deleted", voteId);
    }

    public void updateVoteDocument(String voteId, VoteDocument newVoteDocument) {
        logger.info("Updating VoteDocument with id: {}", voteId);
        VoteDocument existingVote = voteDocumentRepository.findById(voteId).orElse(null);
        if (existingVote != null) {
            existingVote.setVoteOptionId(newVoteDocument.getVoteOptionId());
            existingVote.setVotedBy(newVoteDocument.getVotedBy());
            existingVote.setPollId(newVoteDocument.getPollId());
            voteDocumentRepository.save(existingVote);
            logger.info("VoteDocument with id {} updated", voteId);
        } else {
            logger.warn("VoteDocument with id {} not found.", voteId);
        }
    }


    /* User Entity methods */
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
            logger.info("User with id: {} not found for getUser(UUID userID).", userID);
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
            logger.info("User with id: {} not found for updateUser(UUID userID, User newUser).", userID);
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

    public User login(String email, String password) {
        for (User user : userRepository.findAll()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }


    /* User Document methods */
    public void addUserDocument(UserDocument userDocument) {
        userDocumentRepository.save(userDocument);
        logger.info("UserDocument created: {}", userDocument);
    }

    public Collection<UserDocument> getUserDocuments() {
        logger.info("Getting all UserDocuments...");
        return userDocumentRepository.findAll();
    }

    public UserDocument getUserDocument(String userId) {
        logger.info("Getting UserDocument with id: {}", userId);
        return userDocumentRepository.findById(userId).orElse(null);
    }

    public void deleteUserDocument(String userId) {
        logger.info("Deleting UserDocument with id: {}", userId);
        userDocumentRepository.deleteById(userId);
        logger.info("UserDocument with id {} deleted", userId);
    }

    public void updateUserDocument(String userId, UserDocument newUserDocument) {
        logger.info("Updating UserDocument with id: {}", userId);
        UserDocument existingUser = userDocumentRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(newUserDocument.getUsername());
            existingUser.setEmail(newUserDocument.getEmail());
            existingUser.setPassword(newUserDocument.getPassword());
            userDocumentRepository.save(existingUser);
            logger.info("UserDocument with id {} updated", userId);
        } else {
            logger.warn("UserDocument with id {} not found.", userId);
        }
    }


    /*  Poll Entity methods */
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
            logger.info("Poll with id: {} not found for getPoll(UUID pollID).", pollID);
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
            logger.info("Poll with id: {} not found for updatePoll(UUID pollID, Poll newPoll).", pollID);
        } else {
            // Copy existing fields to the new poll
            Poll current = getPoll(newPoll, existingPoll);

            // Save the new poll
            Poll updatedPoll = pollRepository.save(current);
            logger.info("Updated poll with id {} to new poll: {}", pollID, updatedPoll);
        }
    }

    private static Poll getPoll(Poll newPoll, Optional<Poll> existingPoll) {
        Poll current = existingPoll.get();

        // Update only the fields that are not null or empty into the new user
        current.setQuestion(newPoll.getQuestion() != null ? newPoll.getQuestion() : current.getQuestion());
        current.setPublishedAt(newPoll.getPublishedAt() != null ? newPoll.getPublishedAt() : current.getPublishedAt());
        current.setValidUntil(newPoll.getValidUntil() != null ? newPoll.getValidUntil() : current.getValidUntil());
        current.setPublic(newPoll.isPublic());
        return current;
    }


    /* Poll Document methods */
    public void addPollDocument(PollDocument pollDocument) {
        pollDocumentRepository.save(pollDocument);
        logger.info("PollDocument created: {}", pollDocument);
    }

    public Collection<PollDocument> getPollDocuments() {
        logger.info("Getting all PollDocuments...");
        return pollDocumentRepository.findAll();
    }

    public PollDocument getPollDocument(String pollId) {
        logger.info("Getting PollDocument with id: {}", pollId);
        return pollDocumentRepository.findById(pollId).orElse(null);
    }

    public void deletePollDocument(String pollId) {
        logger.info("Deleting PollDocument with id: {}", pollId);
        pollDocumentRepository.deleteById(pollId);
        logger.info("PollDocument with id {} deleted", pollId);
    }

    public void updatePollDocument(String pollId, PollDocument newPollDocument) {
        logger.info("Updating PollDocument with id: {}", pollId);
        PollDocument existingPoll = pollDocumentRepository.findById(pollId).orElse(null);
        if (existingPoll != null) {
            existingPoll.setQuestion(newPollDocument.getQuestion());
            existingPoll.setPublishedAt(newPollDocument.getPublishedAt());
            existingPoll.setValidUntil(newPollDocument.getValidUntil());
            existingPoll.setPublic(newPollDocument.isPublic());
            pollDocumentRepository.save(existingPoll);
            logger.info("PollDocument with id {} updated", pollId);
        } else {
            logger.warn("PollDocument with id {} not found.", pollId);
        }
    }

}