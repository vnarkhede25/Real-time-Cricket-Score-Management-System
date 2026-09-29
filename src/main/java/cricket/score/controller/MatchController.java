package cricket.score.controller;

import cricket.score.entity.CricketMatch;
import cricket.score.entity.PlayerStat;
import cricket.score.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public List<CricketMatch> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/live")
    public List<CricketMatch> getLiveMatches() {
        return matchService.getLiveMatches();
    }

    @GetMapping("/{id}")
    public CricketMatch getMatch(@PathVariable Long id) {
        return matchService.getMatch(id);
    }

    @GetMapping("/{id}/players")
    public List<PlayerStat> getPlayerStats(
            @PathVariable Long id) {

        return matchService.getPlayerStats(id);
    }
}