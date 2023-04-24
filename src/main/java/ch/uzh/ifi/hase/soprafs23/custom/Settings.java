package ch.uzh.ifi.hase.soprafs23.custom;

import ch.uzh.ifi.hase.soprafs23.constant.Topic;

public class Settings {
    private int rounds;
    private int roundTime;   // in seconds
    private Topic Topic;

    public Settings() {
        rounds = 7;
        roundTime = 120;
        Topic = ch.uzh.ifi.hase.soprafs23.constant.Topic.MOVIES;
    }

    public int getRounds() {
        return rounds;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public Topic getTopic() {
        return Topic;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setRoundTime(int time) {
        this.roundTime = time;
    }

    public void setTopic(Topic Topic) {
        this.Topic = Topic;
    }
}
