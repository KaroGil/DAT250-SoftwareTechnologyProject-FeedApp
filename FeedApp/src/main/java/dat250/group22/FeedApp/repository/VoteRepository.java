package dat250.group22.FeedApp.repository;

import dat250.group22.FeedApp.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> { }