package cricket.score.config;

import cricket.score.entity.CricketMatch;
import cricket.score.entity.PlayerStat;
import cricket.score.repository.MatchRepository;
import cricket.score.repository.PlayerStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MatchRepository matchRepository;
    private final PlayerStatRepository playerStatRepository;

    @Override
    public void run(String... args) {

        if (matchRepository.count() > 0) {
            return;
        }

        CricketMatch liveMatch = new CricketMatch();

        liveMatch.setTeamA("India");
        liveMatch.setTeamB("Australia");
        liveMatch.setVenue("Wankhede Stadium, Mumbai");
        liveMatch.setMatchType("T20");
        liveMatch.setStatus("LIVE");
        liveMatch.setBattingTeam("India");
        liveMatch.setCurrentInnings("1st Innings");

        liveMatch.setTeamARuns(82);
        liveMatch.setTeamAWickets(2);
        liveMatch.setTeamAOvers(10.3);

        liveMatch.setTeamBRuns(0);
        liveMatch.setTeamBWickets(0);
        liveMatch.setTeamBOvers(0.0);

        liveMatch.setTarget(0);
        liveMatch.setResult("Match in progress");
        liveMatch.setMatchDate("29 Sep 2026");

        liveMatch = matchRepository.save(liveMatch);


        createPlayer(
                "Rohit Sharma",
                "India",
                42,
                28,
                5,
                1,
                0,
                0.0,
                liveMatch
        );

        createPlayer(
                "Virat Kohli",
                "India",
                31,
                24,
                3,
                1,
                0,
                0.0,
                liveMatch
        );

        createPlayer(
                "Jasprit Bumrah",
                "India",
                0,
                0,
                0,
                0,
                1,
                7.20,
                liveMatch
        );

        createPlayer(
                "Mitchell Marsh",
                "Australia",
                0,
                0,
                0,
                0,
                0,
                0.0,
                liveMatch
        );


        CricketMatch completedMatch = new CricketMatch();

        completedMatch.setTeamA("England");
        completedMatch.setTeamB("New Zealand");
        completedMatch.setVenue("Lord's, London");
        completedMatch.setMatchType("ODI");
        completedMatch.setStatus("COMPLETED");
        completedMatch.setBattingTeam("New Zealand");
        completedMatch.setCurrentInnings("Completed");

        completedMatch.setTeamARuns(276);
        completedMatch.setTeamAWickets(8);
        completedMatch.setTeamAOvers(50.0);

        completedMatch.setTeamBRuns(278);
        completedMatch.setTeamBWickets(6);
        completedMatch.setTeamBOvers(48.4);

        completedMatch.setTarget(277);
        completedMatch.setResult("New Zealand won by 4 wickets");
        completedMatch.setMatchDate("28 Sep 2026");

        matchRepository.save(completedMatch);
    }


    private void createPlayer(
            String name,
            String team,
            int runs,
            int balls,
            int fours,
            int sixes,
            int wickets,
            double economy,
            CricketMatch match) {

        PlayerStat player = new PlayerStat();

        player.setPlayerName(name);
        player.setTeam(team);
        player.setRuns(runs);
        player.setBalls(balls);
        player.setFours(fours);
        player.setSixes(sixes);
        player.setWickets(wickets);
        player.setEconomy(economy);
        player.setMatch(match);

        playerStatRepository.save(player);
    }
}