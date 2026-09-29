package cricket.score.service;

import cricket.score.entity.CricketMatch;
import cricket.score.entity.PlayerStat;
import cricket.score.repository.MatchRepository;
import cricket.score.repository.PlayerStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final PlayerStatRepository playerStatRepository;

    public List<CricketMatch> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<CricketMatch> getLiveMatches() {
        return matchRepository.findByStatus("LIVE");
    }

    public CricketMatch getMatch(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Match not found"));
    }

    public List<PlayerStat> getPlayerStats(Long matchId) {

        CricketMatch match = getMatch(matchId);

        return playerStatRepository.findByMatch(match);
    }

    public CricketMatch updateMatch(CricketMatch match) {
        return matchRepository.save(match);
    }
}