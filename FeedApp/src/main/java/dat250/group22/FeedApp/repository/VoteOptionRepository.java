package dat250.group22.FeedApp.repository;

import dat250.group22.FeedApp.model.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VoteOptionRepository extends JpaRepository<VoteOption, UUID> { }
