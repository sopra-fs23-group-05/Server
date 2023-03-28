package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.User;

public class LobbyGetDTO {

    private Long accessCode;

    private User lobbyLeader;

    private Settings aSettings;

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
}
