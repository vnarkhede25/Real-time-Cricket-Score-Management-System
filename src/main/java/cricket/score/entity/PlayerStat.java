package cricket.score.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;

    private String team;

    private Integer runs;

    private Integer balls;

    private Integer fours;

    private Integer sixes;

    private Integer wickets;

    private Double economy;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private CricketMatch match;
}