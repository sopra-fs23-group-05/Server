package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Player;

import java.util.List;

public class TeamGetDTO {

    private int teamId;


    private Role aRole;

    private int points;

    private List<Player> players;

    private int idxClueGiver;

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

}
