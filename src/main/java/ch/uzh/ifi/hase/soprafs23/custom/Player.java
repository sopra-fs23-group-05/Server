package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.Embeddable;

@Embeddable
public class Player {
    private String name;
    private int personalScore;
    private boolean leader;

    public Player(String username, boolean isLeader) {
        this.name = username;
        this.leader = isLeader;
        this.personalScore = 0;
    }

    public Player() {
    }

    public int getPersonalScore() {
        return personalScore;
    }

    public void setPersonalScore(int personalScore) {
        this.personalScore = personalScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public void increaseScore() {
        this.personalScore++;
    }
}
