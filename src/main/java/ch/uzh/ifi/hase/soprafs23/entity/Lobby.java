package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LOBBY")
public class Lobby {

    @Id
    private int accessCode;

    @OneToOne
    private User lobbyLeader;

    @Embedded
    private Settings aSettings;

    @OneToMany
    private List<User> lobbyUsers;

    // Low priority; Implement a class Teams to handle team related logic in a separate class.
    @OneToMany
    private List<User> team1;

    @OneToMany
    private List<User> team2;


    public Lobby() {
        lobbyUsers = new ArrayList<>();
        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
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


    public void setAccessCode(int accessCode) {
        this.accessCode = accessCode;

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

    public boolean isUserInTeam1(User userInput) {
        return team1.contains(userInput);
    }

    public boolean isUserInTeam2(User userInput) {
        return team2.contains(userInput);
    }

    public List<User> getTeam1() {
        return team1;
    }

    public List<User> getTeam2() {
        return team2;
    }

    //returns true if the difference in the number of users in each team is less than or equal to 1
    public boolean isFairJoin(int teamNr) {
        if (teamNr == 1) {
            return team1.size() - team2.size() <= 1;
        } else {
            return team2.size() - team1.size() <= 1;
        }
    }

}
