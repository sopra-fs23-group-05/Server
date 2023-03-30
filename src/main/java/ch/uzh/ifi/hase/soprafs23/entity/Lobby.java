package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Random;

@Entity
@Table(name = "LOBBY")
public class Lobby {
    // TODO implement serializable to generate an access code.

    @Id
    private int accessCode;

    @OneToOne
    private User lobbyLeader;

    @Embedded
    private Settings aSettings;

    @OneToMany
    private ArrayList<User> team1;

    @OneToMany
    private ArrayList<User> team2;

    public int getAccessCode() {
        return accessCode;
    }

    public User getLobbyLeader() {
        return lobbyLeader;
    }

    public Settings getSettings() {
        return aSettings;
    }

    public void setAccessCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        accessCode = random.nextInt((max - min) + 1) + min;
    }

    public void setLobbyLeader(User lobbyLeader) {
        this.lobbyLeader = lobbyLeader;
    }

    public void setSettings(Settings aSettings) {
        this.aSettings = aSettings;
    }

    public void addUserToTeam1(User user) {
        team1.add(user);
    }

    public void addUserToTeam2(User user) {
        team2.add(user);
    }

    public void removeUserFromTeam1(User user) {
        team1.remove(user);
    }

    public void removeUserFromTeam2(User user) {
        team2.remove(user);
    }
}
