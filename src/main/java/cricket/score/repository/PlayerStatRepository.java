package cricket.score.repository;

import cricket.score.entity.CricketMatch;
import cricket.score.entity.PlayerStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerStatRepository extends JpaRepository<PlayerStat, Long> {

    List<PlayerStat> findByMatch(CricketMatch match);
}