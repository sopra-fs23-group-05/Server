package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.Topic;

public class SettingsPutDTO {
    private int rounds;
    private int roundTime;   // in seconds
    private Topic topic;

    public void setRoundTime(int roundTime) {
        this.roundTime = roundTime;
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

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
