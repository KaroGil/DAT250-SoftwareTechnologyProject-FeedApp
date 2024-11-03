package dat250.group22.FeedApp.repository;

import dat250.group22.FeedApp.document.VoteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteDocumentRepository extends MongoRepository<VoteDocument, String> { }
