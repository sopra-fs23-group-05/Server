package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.util.List;

public class TeamGetDTO {

    private int teamId;

    private String teamName;

    private Role aRole;

    private int points;

    private List<User> players;

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