package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.*;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.ArrayList;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;


    private Team testTeam;
    private Team testTeam2;

    private Game testGame;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testTeam = new Team();
        List<Player> players = new ArrayList<>();
        testTeam.setPlayers(players);
        testTeam.setaRole(Role.GUESSINGTEAM);
        testTeam.setTeamId(1);

        testTeam2 = new Team();
        testTeam2.setPlayers(players);
        testTeam2.setaRole(Role.BUZZINGTEAM);
        testTeam2.setTeamId(2);

        testGame = new Game(123456,new Settings(),testTeam,testTeam2);
        List<Card> cards = new ArrayList<>();
        Card card1 = new Card();
        cards.add(card1);
        testGame.setTurn(new Turn(new Deck(cards)));


        Mockito.when(teamRepository.save(Mockito.any())).thenReturn(testTeam);
        Mockito.when(teamRepository.save(Mockito.any())).thenReturn(testTeam2);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }

    // #50, Test if the nextTurn method throws an exception when the game is not found

    @Test
    void nextTurn_invalidInputs_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.nextTurn(123456, 3));
    }


}