package ch.uzh.ifi.hase.soprafs23.entity;


import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.custom.Turn;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GAME")
public class Game {
    @Id
    private int accessCode;

    @Embedded
    private Settings settings;

    private int roundsPlayed;

    private Turn turn;

    @OneToOne
    private Team team1;

    @OneToOne
    private Team team2;

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

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
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
}
