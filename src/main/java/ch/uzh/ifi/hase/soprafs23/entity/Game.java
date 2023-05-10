package ch.uzh.ifi.hase.soprafs23.entity;


import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.custom.Turn;

import javax.persistence.*;

@Entity
@Table(name = "GAME")
public class Game {
    @Id
    private int accessCode;

    @Embedded
    private Settings settings;
    @Column
    private int roundsPlayed;
    @Embedded
    private Turn turn;

    @OneToOne
    private Team team1;

    @OneToOne
    private Team team2;
    @Embedded
    private Player leader;

    public Game() {
        this.roundsPlayed = 1;
    }

    public Game(int accessCode, Settings settings, Team team1, Team team2, Player lobbyLeader) {
        this.accessCode = accessCode;
        this.settings = settings;
        this.team1 = team1;
        this.team2 = team2;
        turn = new Turn();
        this.roundsPlayed = 1;
        this.leader = lobbyLeader;
    }

    public int getAccessCode() {
        return accessCode;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public Settings getSettings() {
        return settings;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setAccessCode(int accessCode) {
        this.accessCode = accessCode;
    }

    public void incrementRoundsPlayed() {
        roundsPlayed++;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public Player getLeader() {
        return leader;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }


}
