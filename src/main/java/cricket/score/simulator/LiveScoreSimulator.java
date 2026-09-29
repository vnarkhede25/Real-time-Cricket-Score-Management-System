package cricket.score.simulator;

import cricket.score.entity.CricketMatch;
import cricket.score.repository.MatchRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class LiveScoreSimulator {

    private final MatchRepository matchRepository;

    private final Random random = new Random();

    public LiveScoreSimulator(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    /*
     * Every 8 seconds a new scoring event is generated.
     */
    @Scheduled(fixedRate = 8000)
    public void updateLiveScores() {

        List<CricketMatch> liveMatches =
                matchRepository.findByStatus("LIVE");

        for (CricketMatch match : liveMatches) {

            int event = random.nextInt(7);

            int runs;

            if (event == 0) {
                runs = 0;
            } else if (event <= 3) {
                runs = 1;
            } else if (event == 4) {
                runs = 2;
            } else if (event == 5) {
                runs = 4;
            } else {
                runs = 6;
            }

            boolean battingTeamA =
                    match.getTeamA().equals(match.getBattingTeam());

            if (battingTeamA) {

                int currentRuns =
                        match.getTeamARuns() == null
                                ? 0
                                : match.getTeamARuns();

                match.setTeamARuns(currentRuns + runs);

                updateOvers(match, true);

                if (event == 0 && random.nextBoolean()) {

                    int wickets =
                            match.getTeamAWickets() == null
                                    ? 0
                                    : match.getTeamAWickets();

                    match.setTeamAWickets(
                            Math.min(wickets + 1, 10)
                    );
                }

            } else {

                int currentRuns =
                        match.getTeamBRuns() == null
                                ? 0
                                : match.getTeamBRuns();

                match.setTeamBRuns(currentRuns + runs);

                updateOvers(match, false);

                if (event == 0 && random.nextBoolean()) {

                    int wickets =
                            match.getTeamBWickets() == null
                                    ? 0
                                    : match.getTeamBWickets();

                    match.setTeamBWickets(
                            Math.min(wickets + 1, 10)
                    );
                }
            }

            matchRepository.save(match);
        }
    }


    private void updateOvers(
            CricketMatch match,
            boolean teamA) {

        double overs = teamA
                ? match.getTeamAOvers()
                : match.getTeamBOvers();

        int completedOvers = (int) overs;
        int balls = (int) Math.round(
                (overs - completedOvers) * 10
        );

        balls++;

        if (balls >= 6) {
            completedOvers++;
            balls = 0;
        }

        double newOvers =
                completedOvers + (balls / 10.0);

        newOvers =
                Math.round(newOvers * 10.0) / 10.0;

        if (teamA) {
            match.setTeamAOvers(newOvers);
        } else {
            match.setTeamBOvers(newOvers);
        }
    }
}