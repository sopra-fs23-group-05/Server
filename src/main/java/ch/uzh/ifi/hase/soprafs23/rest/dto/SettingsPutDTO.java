package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.Topic;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;

public class SettingsPutDTO {
    private int rounds;
    private int roundTime;   // in seconds
    private Topic Topic;

    public int getRounds() {
        return rounds;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public Topic getTopic() {
        return Topic;
    }

    public void setTopic(Topic Topic) {
        this.Topic = Topic;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
