let allMatches = [];
let selectedMatchId = null;


/* =========================================
   INITIAL LOAD
========================================= */

document.addEventListener("DOMContentLoaded", () => {

    loadMatches();

    /*
     * Poll the REST API every 5 seconds.
     *
     * Backend itself changes the score every 8 seconds.
     */
    setInterval(loadMatches, 5000);

});


/* =========================================
   LOAD MATCHES
========================================= */

async function loadMatches() {

    try {

        const response =
            await fetch("/api/matches");

        allMatches =
            await response.json();

        renderLiveMatches();
        renderRecentMatches();

        const live =
            allMatches.find(
                match => match.status === "LIVE"
            );

        if (live) {

            if (!selectedMatchId) {

                selectedMatchId = live.id;

            }

            loadMatchDetails(selectedMatchId);
        }

        showToast("Live data synchronized");

    } catch (error) {

        console.error(error);

        showToast("Unable to connect to server");

    }

}


/* =========================================
   LIVE MATCH CARDS
========================================= */

function renderLiveMatches() {

    const container =
        document.getElementById("liveMatches");

    const liveMatches =
        allMatches.filter(
            match => match.status === "LIVE"
        );

    if (liveMatches.length === 0) {

        container.innerHTML = `
            <div class="match-card">
                <h3>No live matches</h3>
                <p class="overs">
                    Waiting for the next match...
                </p>
            </div>
        `;

        return;
    }


    container.innerHTML =
        liveMatches.map(match => `

            <div class="match-card"
                 onclick="selectMatch(${match.id})">

                <div class="match-card-top">

                    <span class="live-badge">
                        ● LIVE
                    </span>

                    <span class="overs">
                        ${match.matchType}
                    </span>

                </div>

                <h3>
                    ${match.teamA}
                    <span style="color:#8490a3"> vs </span>
                    ${match.teamB}
                </h3>

                <div class="score-small">

                    ${match.teamARuns}
                    /
                    ${match.teamAWickets}

                </div>

                <div class="overs">

                    ${match.teamAOvers} overs ·
                    ${match.venue}

                </div>

            </div>

        `).join("");

}


/* =========================================
   SELECT MATCH
========================================= */

function selectMatch(id) {

    selectedMatchId = id;

    loadMatchDetails(id);

    document
        .querySelector(".score-section")
        .scrollIntoView({
            behavior: "smooth"
        });

}


/* =========================================
   MATCH DETAILS
========================================= */

async function loadMatchDetails(id) {

    try {

        const response =
            await fetch(`/api/matches/${id}`);

        const match =
            await response.json();

        updateScoreboard(match);

        loadPlayerStats(id);

    } catch (error) {

        console.error(error);

    }

}


/* =========================================
   SCOREBOARD
========================================= */

function updateScoreboard(match) {

    document.getElementById("scoreTitle")
        .innerText =
        `${match.teamA} vs ${match.teamB}`;

    document.getElementById("venue")
        .innerText =
        match.venue;

    document.getElementById("matchType")
        .innerText =
        match.matchType;

    document.getElementById("teamA")
        .innerText =
        match.teamA;

    document.getElementById("teamB")
        .innerText =
        match.teamB;

    document.getElementById("runsA")
        .innerText =
        match.teamARuns;

    document.getElementById("wicketsA")
        .innerText =
        match.teamAWickets;

    document.getElementById("oversA")
        .innerText =
        match.teamAOvers;

    document.getElementById("runsB")
        .innerText =
        match.teamBRuns;

    document.getElementById("wicketsB")
        .innerText =
        match.teamBWickets;

    document.getElementById("oversB")
        .innerText =
        match.teamBOvers;

    document.getElementById("target")
        .innerText =
        match.target;

    document.getElementById("status")
        .innerText =
        match.status;

    document.getElementById("innings")
        .innerText =
        match.currentInnings;


    /*
     * Calculate innings progress.
     */

    let overs;

    if (match.battingTeam === match.teamA) {

        overs = match.teamAOvers;

    } else {

        overs = match.teamBOvers;

    }

    let progress =
        Math.min(
            (overs / 20) * 100,
            100
        );

    document.getElementById("progress")
        .style.width =
        `${progress}%`;

    document.getElementById("progressText")
        .innerText =
        `${Math.round(progress)}%`;

}


/* =========================================
   PLAYER STATISTICS
========================================= */

async function loadPlayerStats(id) {

    try {

        const response =
            await fetch(
                `/api/matches/${id}/players`
            );

        const players =
            await response.json();

        const table =
            document.getElementById("playerStats");

        table.innerHTML =
            players.map(player => `

                <tr>

                    <td>
                        ${player.playerName}
                    </td>

                    <td>
                        ${player.team}
                    </td>

                    <td>
                        ${player.runs}
                    </td>

                    <td>
                        ${player.balls}
                    </td>

                    <td>
                        ${player.fours}
                    </td>

                    <td>
                        ${player.sixes}
                    </td>

                    <td>
                        ${player.wickets}
                    </td>

                </tr>

            `).join("");

    } catch (error) {

        console.error(error);

    }

}


/* =========================================
   RECENT MATCHES
========================================= */

function renderRecentMatches() {

    const container =
        document.getElementById("recentMatches");

    const completed =
        allMatches.filter(
            match => match.status === "COMPLETED"
        );

    container.innerHTML =
        completed.map(match => `

            <div class="recent-card">

                <h3>
                    ${match.teamA}
                    vs
                    ${match.teamB}
                </h3>

                <div class="recent-score">

                    ${match.teamARuns}/
                    ${match.teamAWickets}

                    &nbsp; — &nbsp;

                    ${match.teamBRuns}/
                    ${match.teamBWickets}

                </div>

                <div class="result">
                    ${match.result}
                </div>

                <div class="recent-venue">
                    ${match.venue} · ${match.matchDate}
                </div>

            </div>

        `).join("");

}


/* =========================================
   SEARCH
========================================= */

function filterMatches() {

    const value =
        document.getElementById("search")
            .value
            .toLowerCase();

    const cards =
        document.querySelectorAll(
            ".recent-card"
        );

    cards.forEach(card => {

        const text =
            card.innerText.toLowerCase();

        card.style.display =
            text.includes(value)
                ? "block"
                : "none";

    });

}


/* =========================================
   TOAST
========================================= */

function showToast(message) {

    const toast =
        document.getElementById("toast");

    toast.innerText =
        message;

    toast.classList.add("show");

    setTimeout(() => {

        toast.classList.remove("show");

    }, 1500);

}