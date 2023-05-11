package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.custom.Turn;
import ch.uzh.ifi.hase.soprafs23.entity.Team;

public class GameGetDTO {

    private int accessCode;

    private Settings settings;

    private int roundsPlayed;

    private Turn turn;

    private Team team1;

    private Team team2;
    private Player leader;

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public void setAccessCode(int accessCode) {
        this.accessCode = accessCode;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Settings getSettings() {
        return settings;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public int getAccessCode() {
        return accessCode;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Turn getTurn() {
        return turn;
    }

    public Team getTeam2() {
        return team2;
    }

    public Team getTeam1() {
        return team1;
    }

    public Player getLeader() {
        return leader;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }
}
