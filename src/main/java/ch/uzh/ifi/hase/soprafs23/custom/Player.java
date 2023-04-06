package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.Embeddable;

@Embeddable
public class Player {
    private String name;
    private int personalScore;
    private boolean leader;



    public int getPersonalScore() {
        return personalScore;
    }

    public String getName() {
        return name;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setPersonalScore(int personalScore) {this.personalScore = personalScore;}

    public void setName(String name) {
        this.name = name;
    }

    public void increaseScoreBy(int points){this.personalScore += points;}
}
