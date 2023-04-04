package ch.uzh.ifi.hase.soprafs23.entity;


import ch.uzh.ifi.hase.soprafs23.constant.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TEAM")
public class Team {
    @Id
    private int teamId;

    @Column(nullable = false, unique = true)
    private String teamName;

    @Column(nullable = false)
    private Role aRole;

    @Column(nullable = false)
    private int points;
    @OneToMany
    @Column(nullable = false)
    private List<User> players;

    @Column(nullable = false)
    private int idxClueGiver;

    public void setIdxClueGiver(int idxClueGiver) {
        this.idxClueGiver = idxClueGiver;
    }

    public int getIdxClueGiver() {
        return idxClueGiver;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public void setaRole(Role aRole) {
        this.aRole = aRole;
    }

    public Role getaRole() {
        return aRole;
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
}
