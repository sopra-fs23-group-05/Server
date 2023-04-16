package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    // Test for issue Server #48
    // Test if after playing multiple rounds the roles are distributed correctly
    // And the correct player is the clue giver.
    @Test
    void changeRole() {
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            players.add(new Player());
        }
        Team team = new Team(players, Role.GUESSINGTEAM);   // Player 0 should be clue giver
        team.changeRole();
        team.changeRole();  // Player 1 should be clue giver
        team.changeRole();
        team.changeRole();  // Player 0 should be clue giver
        team.changeRole();
        team.changeRole();  // Player 1 should be clue giver
        assertEquals(Role.GUESSINGTEAM, team.getaRole());
        assertEquals(1, team.getIdxClueGiver());
    }
}