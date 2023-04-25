package ch.uzh.ifi.hase.soprafs23.custom;

import ch.uzh.ifi.hase.soprafs23.constant.Topic;

public class Settings {
    private int rounds;
    private int roundTime;   // in seconds
    private Topic topic;

    public Settings() {
        rounds = 7;
        roundTime = 120;
        topic = ch.uzh.ifi.hase.soprafs23.constant.Topic.MOVIES;
    }

    public int getRounds() {
        return rounds;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setRoundTime(int time) {
        this.roundTime = time;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
