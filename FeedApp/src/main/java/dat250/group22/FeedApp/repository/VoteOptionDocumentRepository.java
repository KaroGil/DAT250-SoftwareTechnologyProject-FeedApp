package dat250.group22.FeedApp.repository;

import dat250.group22.FeedApp.document.VoteOptionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteOptionDocumentRepository extends MongoRepository<VoteOptionDocument, String> { }
