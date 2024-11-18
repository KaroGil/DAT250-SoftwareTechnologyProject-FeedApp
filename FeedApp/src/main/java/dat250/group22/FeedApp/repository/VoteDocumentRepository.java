package dat250.group22.FeedApp.repository;

import dat250.group22.FeedApp.document.VoteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VoteDocumentRepository extends MongoRepository<VoteDocument, String> {
    // Custom query using MongoDB's query language (useful is performing more complex queries than just CRUD)
    @Query("{ 'creatorUserID': ?0 }")
    List<VoteDocument> findVotesByUser(String creatorUserID);
}
