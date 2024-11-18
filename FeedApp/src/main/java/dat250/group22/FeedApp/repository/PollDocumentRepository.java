package dat250.group22.FeedApp.repository;

import dat250.group22.FeedApp.document.PollDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PollDocumentRepository extends MongoRepository<PollDocument, String> {

    // Custom query using MongoDB's query language (useful is performing more complex queries than just CRUD)
    @Query("{ 'creatorUserID': ?0 }")
    List<PollDocument> findPollsByCreatorUser(String creatorUserID);
}
