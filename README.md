# OverNova — Real-Time Cricket Score Management System

OverNova is a Spring Boot web application for viewing live and completed cricket matches, player statistics, and automatically simulated live scores.

## Aim

To provide a simple, responsive dashboard and REST API for managing and viewing cricket match scores and player statistics in real time.

## Objectives

- Display live match scores, wickets, overs, innings, venue, and match status.
- Display player batting and bowling statistics associated with each match.
- Keep the live dashboard refreshed and simulate scoring events automatically.
- Show completed matches and their results in a searchable archive.
- Persist match and player data using Spring Data JPA, with MySQL support and an H2 in-memory default for local development and tests.

## Features

- Responsive OverNova dashboard served by Spring Boot.
- REST endpoints for all matches, live matches, individual match details, and player statistics.
- Demo data is inserted on startup when the database has no matches: a live India–Australia T20, a completed England–New Zealand ODI, and player records for the live match.
- The backend simulates a scoring event every 8 seconds. The browser refreshes match data automatically.
- Hibernate creates or updates tables using `spring.jpa.hibernate.ddl-auto=update`.

## Software requirements and versions

| Software / technology | Version / configuration | Purpose |
| --- | --- | --- |
| Java JDK | 26 (project compiler target in `pom.xml`) | Compile and run the application |
| Spring Boot | 4.1.1 | Application framework and REST server |
| Maven Wrapper | Maven 3.9.16; wrapper 3.3.4 | Build and run without a separate Maven installation |
| Spring Data JPA / Hibernate | Versions managed by the Spring Boot 4.1.1 parent | Persistence and ORM |
| MySQL Server | Optional; use your installed version. This project was connected locally on port 3309. | Persistent storage when configured |
| H2 Database | Runtime dependency; in-memory database is the default | Local development and self-contained tests |
| HTML, CSS, JavaScript | Browser-native; no frontend package manager required | Dashboard UI and API polling |

> The MySQL Connector/J and other Spring dependencies are version-managed by the Spring Boot parent in `pom.xml` rather than pinned individually.

## Run locally

### Default (in-memory H2)

From the project root, run:

```powershell
.\mvnw.cmd spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080). The H2 in-memory database is reset when the application stops, so use MySQL if you need data to persist across restarts.

### MySQL (port 3309 example)

1. Start MySQL and create the database in MySQL Workbench or a SQL client:

   ```sql
   CREATE DATABASE IF NOT EXISTS cricket_score;
   ```

2. In the same PowerShell window from which you start the app, set the connection details. Replace the username and password placeholders with your MySQL account values; do not commit real credentials.

   ```powershell
   $env:DB_URL = "jdbc:mysql://localhost:3309/cricket_score"
   $env:DB_USERNAME = "your_mysql_username"
   $env:DB_PASSWORD = "your_mysql_password"
   .\mvnw.cmd spring-boot:run
   ```

The application creates the `matches` and `player_stats` tables on startup. If `matches` is empty, it inserts the demo data. In Workbench, refresh the schema and run:

```sql
USE cricket_score;
SELECT * FROM matches;
SELECT * FROM player_stats;
```

### Run tests

```powershell
.\mvnw.cmd test
```

## REST API

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/matches` | Return all matches |
| `GET` | `/api/matches/live` | Return live matches |
| `GET` | `/api/matches/{id}` | Return one match by ID |
| `GET` | `/api/matches/{id}/players` | Return player statistics for a match |

Example: open [http://localhost:8080/api/matches](http://localhost:8080/api/matches) to view match data as JSON while the application is running.

## Project structure

```text
.
├── README.md                       # Project documentation
├── mvnw / mvnw.cmd                 # Maven Wrapper launchers
├── .mvn/wrapper/                   # Maven Wrapper configuration
├── pom.xml                         # Dependencies and build configuration
├── screenshots/
│   └── overnova-dashboard.png      # Dashboard output screenshot
└── src/
    ├── main/
    │   ├── java/cricket/score/
    │   │   ├── ScoreApplication.java
    │   │   ├── config/DataInitializer.java
    │   │   ├── controller/MatchController.java
    │   │   ├── entity/               # CricketMatch and PlayerStat
    │   │   ├── repository/           # Spring Data repositories
    │   │   ├── service/MatchService.java
    │   │   └── simulator/LiveScoreSimulator.java
    │   └── resources/
    │       ├── application.properties
    │       └── static/               # index.html, style.css, app.js
    └── test/java/cricket/score/
        └── ScoreApplicationTests.java
```

## Screenshots / Output

The screenshot below shows the live match dashboard, scoreboard, player statistics, and completed-match archive. The live score shown is dynamic and changes while the simulator is running.

![OverNova live cricket dashboard](screenshots/overnova-dashboard.png)

The REST API returns JSON. For example, `GET /api/matches` returns an array of match records containing team names, scores, wickets, overs, status, venue, and result.
