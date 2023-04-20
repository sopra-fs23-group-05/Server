package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.aTopic;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;

public class SettingsPutDTO {
    private int rounds;
    private int roundTime;   // in seconds
    private aTopic aTopic;

    public int getRounds() {
        return rounds;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public aTopic getaTopic() {
        return aTopic;
    }

    public void setaTopic(aTopic aTopic) {
        this.aTopic = aTopic;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
