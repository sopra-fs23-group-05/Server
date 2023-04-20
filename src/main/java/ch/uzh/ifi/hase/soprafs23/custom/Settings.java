package ch.uzh.ifi.hase.soprafs23.custom;

import ch.uzh.ifi.hase.soprafs23.constant.aTopic;

public class Settings {
    private int rounds;
    private int roundTime;   // in seconds
    private aTopic aTopic;

    public Settings() {
        rounds = 7;
        roundTime = 120;
        aTopic = ch.uzh.ifi.hase.soprafs23.constant.aTopic.MOVIES;
    }

    public int getRounds() {
        return rounds;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public aTopic getaTopic() {
        return aTopic;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setRoundTime(int time) {
        this.roundTime = time;
    }

    public void setaTopic(aTopic aTopic) {
        this.aTopic = aTopic;
    }
}
