package ch.uzh.ifi.hase.soprafs23.custom;

import ch.uzh.ifi.hase.soprafs23.constant.Topic;

public class Settings {
    private int rounds;
    private int roundTime;   // in seconds
    private Topic aTopic;

    public Settings() {
        rounds = 7;
        roundTime = 120;
        aTopic = Topic.MOVIES;
    }

    public int getRounds() {
        return rounds;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public Topic getTopic() {
        return aTopic;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setRoundTime(int time) {
        this.roundTime = time;
    }

    public void setTopic(Topic aTopic) {
        this.aTopic = aTopic;
    }
}
