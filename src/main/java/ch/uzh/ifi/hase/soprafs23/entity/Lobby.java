package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;

import javax.persistence.*;

@Entity
@Table(name = "LOBBY")
public class Lobby {
    // TODO implement serializable to generate an access code.

    @Id
    private Long accessCode;

    @OneToOne
    private User lobbyLeader;

    @Embedded
    private Settings aSettings;

    public Long getAccessCode() {
        return accessCode;
    }

    public User getLobbyLeader() {
        return lobbyLeader;
    }

    public Settings getSettings() {
        return aSettings;
    }

    public void setAccessCode(Long accessCode) {
        this.accessCode = accessCode;
    }

    public void setLobbyLeader(User lobbyLeader) {
        this.lobbyLeader = lobbyLeader;
    }

    public void setSettings(Settings aSettings) {
        this.aSettings = aSettings;
    }
}
