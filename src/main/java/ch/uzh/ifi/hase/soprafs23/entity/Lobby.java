package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
    private List<User> lobbyUsers;

    @OneToMany
    private List<User> team1;

    @OneToMany
    private List<User> team2;

    public Lobby() {
        setAccessCode();
        lobbyUsers = new ArrayList<>();
        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
        System.out.println("--------------------I got here------------------");
    }

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

        // Get predictable behaviour for testing with postman
        accessCode = 123456;
    }

    public void setLobbyLeader(User lobbyLeader) {
        this.lobbyLeader = lobbyLeader;
    }

    public void setSettings(Settings aSettings) {
        this.aSettings = aSettings;
    }

    public List<User> getLobbyUsers() {
        return lobbyUsers;
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

    public void addUserToLobby(User userInput) {
        lobbyUsers.add(userInput);
    }

    public void removeUserFromLobby(User userInput) {
        lobbyUsers.remove(userInput);
    }

    public boolean isUserInLobby(User userInput) {
        return lobbyUsers.contains(userInput);
    }

    public List<User> getTeam1() {
        return team1;
    }

    public List<User> getTeam2() {
        return team2;
    }
}
