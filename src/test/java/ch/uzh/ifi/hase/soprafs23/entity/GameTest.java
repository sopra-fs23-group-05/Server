package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void settersAndGetters_validInput(){
        Team team1 = new Team();
        Team team2 = new Team();
        Player player1 = new Player();
        Player player2 = new Player();
        Game game = new Game(123456,new Settings(),team1,team2, player1);

        assertEquals(123456, game.getAccessCode());
        assertEquals(team1, game.getTeam1());
        assertEquals(team2, game.getTeam2());
        assertEquals(player1, game.getLeader());

        game.setAccessCode(123455);
        game.setTeam1(team2);
        game.setTeam2(team1);
        game.setLeader(player2);

        assertEquals(123455, game.getAccessCode());
        assertEquals(team2, game.getTeam1());
        assertEquals(team1, game.getTeam2());
        assertEquals(player2, game.getLeader());


    }
}
