package dat250.group22.FeedApp.repository;

import dat250.group22.FeedApp.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PollRepository  extends JpaRepository<Poll, UUID> { }
