package cricket.score.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CricketMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamA;

    private String teamB;

    private String venue;

    private String matchType;

    private String status;

    private String battingTeam;

    private String currentInnings;

    private Integer teamARuns;

    private Integer teamAWickets;

    private Double teamAOvers;

    private Integer teamBRuns;

    private Integer teamBWickets;

    private Double teamBOvers;

    private Integer target;

    private String result;

    private String matchDate;
}