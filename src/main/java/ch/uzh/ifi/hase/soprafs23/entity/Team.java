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
    private String teamName;

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

    public Team(List<Player> players, Role startingRole){
        this.players = players;
        aRole = startingRole;
        idxClueGiver = 0;
        points = 0;
    }

    public void setIdxClueGiver(int idxClueGiver) {this.idxClueGiver = idxClueGiver;}

    public int getIdxClueGiver() {
        return idxClueGiver;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setaRole(Role aRole) {
        this.aRole = aRole;
    }

    public Role getaRole() {
        return aRole;
    }

    public void changeRole() {
        if (aRole == Role.GUESSINGTEAM) {
            aRole = Role.BUZZINGTEAM;
        } else {
            aRole = Role.GUESSINGTEAM;
            idxClueGiver = (idxClueGiver + 1) % players.size();
        }
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void addPoints(int scoredPoints) {
        points += scoredPoints;
    }
}
