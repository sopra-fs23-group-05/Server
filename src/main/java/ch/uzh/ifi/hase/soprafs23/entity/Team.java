package ch.uzh.ifi.hase.soprafs23.entity;


import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Player;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TEAM")
public class Team {
    @GeneratedValue
    @Id
    private int teamId;

    @Column
    private Role aRole;

    @Column
    private int points;

    @ElementCollection
    @Column
    private List<Player> players;

    @Column
    private int idxClueGiver;

    public Team() {
        idxClueGiver = 0;
        points = 0;
    }

    public Team(List<Player> players, Role startingRole) {
        this.players = players;
        aRole = startingRole;
        idxClueGiver = 0;
        points = 0;
    }

    public int getIdxClueGiver() {
        return idxClueGiver;
    }

    public void setIdxClueGiver(int idxClueGiver) {
        this.idxClueGiver = idxClueGiver;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Role getaRole() {
        return aRole;
    }

    public void setaRole(Role aRole) {
        this.aRole = aRole;
    }

    public void changeTurn(int scoredPoints) {
        if (aRole == Role.GUESSINGTEAM) {
            addPoints(scoredPoints);
            aRole = Role.BUZZINGTEAM;
        }
        else {
            aRole = Role.GUESSINGTEAM;
            idxClueGiver = (idxClueGiver + 1) % players.size();
        }
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int scoredPoints) {
        points += scoredPoints;
    }

    public void increasePlayerScore(String username) {
        for (Player player : players) {
            if (player.getName().equals(username)) {
                player.increaseScore();
            }
        }
    }
}
