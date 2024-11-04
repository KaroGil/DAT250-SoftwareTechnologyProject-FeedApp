package dat250.group22.FeedApp.repository;

import dat250.group22.FeedApp.document.PollDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollDocumentRepository extends MongoRepository<PollDocument, String> { }
