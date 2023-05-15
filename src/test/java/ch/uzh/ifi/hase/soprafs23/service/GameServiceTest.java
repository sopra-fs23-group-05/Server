package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.websockets.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;
    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    private Lobby testLobby;

    private Team testTeam;

    private Team testTeam2;
    private Game testGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        testTeam2 = new Team();

        testTeam = new Team();
        List<Player> players = new ArrayList<>();
        Player p1 = new Player();
        p1.setName("testName");
        players.add(p1);
        testTeam.setPlayers(players);
        testTeam.setaRole(Role.GUESSINGTEAM);
        testTeam.setTeamId(1);


        testGame = new Game(123456, new Settings(), testTeam, testTeam2,new Player());

        testLobby = new Lobby();
        testLobby.setAccessCode(123456);
        testLobby.setSettings(new Settings());


        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        Mockito.when(teamRepository.save(Mockito.any())).thenReturn(testTeam);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }


    // #50, Test if the nextTurn method throws an exception when the game is not found

    @Test
    void nextTurn_invalidInputs_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.nextTurn(123456));
    }


    @Test
    void getGame_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Game game = gameService.getGame(123456);
        assertEquals(testGame.getAccessCode(), game.getAccessCode());
        assertEquals(testGame.getSettings(), game.getSettings());
        assertEquals(testGame.getTeam1(), game.getTeam1());
        assertEquals(testGame.getTeam2(), game.getTeam2());
        assertEquals(testGame.getTurn(), game.getTurn());
    }

    @Test
    void getGame_invalidInputs_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertEquals(null, gameService.getGame(123456));
    }

    @Test
    void createGame_invalidInputs_gameAlreadyExists() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        assertThrows(NullPointerException.class, () -> gameService.createGame(testGame.getAccessCode()));
    }

    @Test
    void drawCard_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Card card = new Card();
        testGame.getTurn().getDeck().addCard(card);
        Card testCard = gameService.drawCard(123456);
        assertEquals(testCard, card);

    }

    @Test
    void drawCard_invalidInputs_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.drawCard(123456));
    }

    @Test
    void drawCard_invalidInputs_turnNull() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        testGame.setTurn(null);
        assertThrows(NullPointerException.class, () -> gameService.drawCard(123456));
    }

    @Test
    void buzz_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Card testCard = gameService.drawCard(123456);
        Card card = gameService.buzz(123456);
        assertEquals(card, testCard);
    }

    @Test
    void doubleBuzz_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Card testCard = gameService.drawCard(123456);
        Card card = gameService.buzz(123456);
        assertEquals(card, testCard);
        Card card2 = gameService.buzz(123456);
        assertNotEquals(card2, testCard);
    }

    @Test
    void finishGame_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        gameService.finishGame(123456);
        assertEquals(testGame.getRoundsPlayed(), testGame.getSettings().getRounds());
    }

    @Test
    void finishGame_invalidInputs_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.finishGame(123456));
    }

    @Test
    void deleteGameTeamsUsersAndLobby_inValidInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.deleteGameTeamsUsersAndLobby(123456));
    }

    @Test
    void getTurnPoints_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        assertEquals(0, gameService.getTurnPoints(123456));
    }

    @Test
    void getTurnPointsAfterSkip_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        gameService.skip(123456);
        assertEquals(-1, gameService.getTurnPoints(123456));
    }

    @Test
    void getTurnPoints_invalidInputs_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.getTurnPoints(123456));
    }

    @Test
    void createCard_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Card card = new Card();
        gameService.createCard(123456, card);
        assertEquals(card, testGame.getTurn().getDeck().getCards().get(0));
    }

    @Test
    void createCard_invalidInputs_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.createCard(123456, new Card()));
    }

    @Test
    void skip_validInputs_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Card card = gameService.drawCard(123456);
        Card skipcard =gameService.skip(123456);
        assertNotEquals(card, skipcard);
        assertEquals(testGame.getTurn().getTurnPoints(), -1);
    }

    @Test
    void skip_invalidInput_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.skip(123456));
    }

    @Test
    void guessWord_invalidInput_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        Message message = new Message(123456, 1,"test", MessageType.DESCRIPTION);
        assertThrows(ResponseStatusException.class, () -> gameService.guessWord(message));
    }


}