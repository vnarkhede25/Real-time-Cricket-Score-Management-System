package cricket.score.repository;

import cricket.score.entity.CricketMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<CricketMatch, Long> {

    List<CricketMatch> findByStatus(String status);
}