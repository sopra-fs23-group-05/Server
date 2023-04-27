package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.util.List;

public class LobbyGetDTO {

    private Long accessCode;

    private User lobbyLeader;

    private Settings aSettings;

    private List<User> lobbyUsers;

    private List<User> team1;

    private List<User> team2;

    public Long getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(Long accessCode) {
        this.accessCode = accessCode;
    }

    public User getLobbyLeader() {
        return lobbyLeader;
    }

    public void setLobbyLeader(User lobbyLeader) {
        this.lobbyLeader = lobbyLeader;
    }

    public Settings getSettings() {
        return aSettings;
    }

    public void setSettings(Settings aSettings) {
        this.aSettings = aSettings;
    }

    public List<User> getLobbyUsers() {
        return lobbyUsers;
    }

    public void setLobbyUsers(List<User> lobbyUsers) {
        this.lobbyUsers = lobbyUsers;
    }

    public List<User> getTeam1() {
        return team1;
    }

    public void setTeam1(List<User> team1) {
        this.team1 = team1;
    }

    public List<User> getTeam2() {
        return team2;
    }

    public void setTeam2(List<User> team2) {
        this.team2 = team2;
    }
}
