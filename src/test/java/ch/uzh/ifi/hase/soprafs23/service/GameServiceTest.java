package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.custom.Deck;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.websockets.ChatWebSocketHandler;
import ch.uzh.ifi.hase.soprafs23.websockets.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
    private ChatWebSocketHandler chatWebSocketHandler;

    @Mock
    private TeamService teamService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    private Lobby testLobby;

    private Team testTeam;

    private Team testTeam2;
    private Game testGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        testTeam2 = new Team();

        testTeam = new Team();
        Player p1 = new Player();
        p1.setName("testName");
        Player p2 = new Player();
        p2.setName("testName2");
        Player p3 = new Player();
        p3.setName("testName3");
        Player p4 = new Player();
        p4.setName("testName4");
        List<Player> players = new ArrayList<>();
        players.add(p1);
        p1.setPersonalScore(3);
        players.add(p2);
        List<Player> players2 = new ArrayList<>();
        players2.add(p3);
        players2.add(p4);
        testTeam2.setaRole(Role.BUZZINGTEAM);
        testTeam2.setTeamId(2);
        testTeam2.setPlayers(players2);
        testTeam.setPlayers(players);
        testTeam.setaRole(Role.GUESSINGTEAM);
        testTeam.setTeamId(1);


        testGame = new Game(123456, new Settings(), testTeam, testTeam2, new Player());

        testLobby = new Lobby();
        testLobby.setAccessCode(123456);
        testLobby.setSettings(new Settings());
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);


        gameService.initializeChatWebSocketHandler(chatWebSocketHandler);
    }


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
        assertThrows(ResponseStatusException.class, () -> gameService.getGame(123456));
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
        Card skipcard = gameService.skip(123456);
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
        Message message = new Message(123456, 1, "test", MessageType.DESCRIPTION);
        assertThrows(ResponseStatusException.class, () -> gameService.guessWord(message));
    }

    @Test
    void guessWord_invalidInput_incorrectGuess() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Card card = new Card("test1", "1", "2", "3", "4", "5");
        User user = new User();
        user.setUsername("testName2");
        user.setId(2L);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findById(2)).thenReturn(user);
        Mockito.when(userService.getUser(2)).thenReturn(user);

        testGame.getTurn().getDeck().getCards().add(card);
        testGame.getTurn().drawCard();

        Message message = new Message(123456, 2, "test", MessageType.DESCRIPTION);
        gameService.guessWord(message);
        Mockito.verify(gameRepository, Mockito.times(1)).flush();
    }

    @Test
    void guessWord_invalidInput_guesser(){
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Card card = new Card("test", "1", "2", "3", "4", "5");
        User user = new User();
        user.setUsername("testName2");
        user.setId(2L);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findById(2)).thenReturn(user);
        Mockito.when(userService.getUser(2)).thenReturn(user);

        testGame.getTurn().getDeck().getCards().add(card);
        testGame.getTurn().drawCard();

        Message message = new Message(123456, 2, "test", MessageType.DESCRIPTION);
        assertThrows(ResponseStatusException.class, () -> gameService.guessWord(message));
    }

    @Test
    void deletePlayerFromGame_validInput_team1() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName")).thenReturn(true);
        gameService.deletePlayerFromGame(123456, "testName");
        assertEquals(1, testGame.getTeam1().getPlayers().size());
    }


    @Test
    void deletePlayerFromGame_validInput_player2(){
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName2")).thenReturn(true);
        gameService.deletePlayerFromGame(123456, "testName2");
        assertEquals(1, testGame.getTeam1().getPlayers().size());
    }

    @Test
    void deletePlayerFromGame_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.deletePlayerFromGame(123456, "testName"));
    }

    @Test
    void deletePlayerFromGame_playerNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName")).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> gameService.deletePlayerFromGame(123456, "testName"));
    }

    @Test
    void deletePlayerFromTeam_validInput_team1() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        gameService.deletePlayerFromTeam(testTeam, "testName", 123456);
        assertEquals(1, testGame.getTeam1().getPlayers().size());
    }

    @Test
    void deletePlayerFromTeam_validInput_team2() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        gameService.deletePlayerFromTeam(testTeam2, "testName3", 123456);
        assertEquals(1, testGame.getTeam2().getPlayers().size());
    }

    @Test
    void deleteGameTeamsUsersAndLobby_validInput_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        gameService.deleteGameTeamsUsersAndLobby(123456);
        Mockito.verify(userRepository, Mockito.times(1)).deleteAll(testLobby.getLobbyUsers());
        Mockito.verify(gameRepository, Mockito.times(1)).delete(testGame);
        Mockito.verify(teamRepository, Mockito.times(1)).delete(testTeam);
        Mockito.verify(teamRepository, Mockito.times(1)).delete(testTeam2);
        Mockito.verify(lobbyRepository, Mockito.times(1)).delete(testLobby);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
        Mockito.verify(gameRepository, Mockito.times(1)).flush();
        Mockito.verify(teamRepository, Mockito.times(1)).flush();
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();
    }

    @Test
    void getMVPPlayer_validInput_team1() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        assertEquals(testGame.getTeam1().getPlayers().get(0), gameService.getMPVPlayer(123456));
    }

    @Test
    void getMVPPlayer_validInput_team2(){
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        testGame.getTeam2().getPlayers().get(0).setPersonalScore(10);
        assertEquals(testGame.getTeam2().getPlayers().get(0), gameService.getMPVPlayer(123456));
    }

    @Test
    void getPlayerRole_validInput_Buzzer() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        assertEquals(PlayerRole.BUZZER, gameService.getPlayerRole(123456, "testName"));
    }

    @Test
    void getPlayerRole_validInput_GuesserTeam1() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName2")).thenReturn(true);
        Mockito.when(teamService.isClueGiver(1, "testName2")).thenReturn(false);
        assertEquals(PlayerRole.GUESSER, gameService.getPlayerRole(123456, "testName2"));
    }

    @Test
    void getPlayerRole_validInput_GuesserTeam2() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName2")).thenReturn(false);
        Mockito.when(teamService.isClueGiver(2, "testName2")).thenReturn(false);
        testGame.getTeam2().setaRole(Role.GUESSINGTEAM);
        assertEquals(PlayerRole.GUESSER, gameService.getPlayerRole(123456, "testName2"));
    }

    @Test
    void getPlayerRole_validInput_BuzzerTeam1() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName2")).thenReturn(true);
        testGame.getTeam1().setaRole(Role.BUZZINGTEAM);
        assertEquals(PlayerRole.BUZZER, gameService.getPlayerRole(123456, "testName2"));
    }

    @Test
    void getPlayerRole_validInput_BuzzerTeam2() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName2")).thenReturn(false);
        testGame.getTeam2().setaRole(Role.BUZZINGTEAM);
        assertEquals(PlayerRole.BUZZER, gameService.getPlayerRole(123456, "testName2"));
    }

    @Test
    void getPlayerRole_validInput_clueGiverTeam1() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName2")).thenReturn(true);
        Mockito.when(teamService.isClueGiver(1, "testName2")).thenReturn(true);
        assertEquals(PlayerRole.CLUEGIVER, gameService.getPlayerRole(123456, "testName2"));
    }

    @Test
    void getPlayerRole_validInput_clueGiverTeam2() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(teamService.isInTeam(1, "testName2")).thenReturn(false);
        Mockito.when(teamService.isClueGiver(2, "testName2")).thenReturn(true);
        testGame.getTeam2().setaRole(Role.GUESSINGTEAM);
        assertEquals(PlayerRole.CLUEGIVER, gameService.getPlayerRole(123456, "testName2"));
    }

    @Test
    void buzz_invalidInput_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.buzz(123456));
    }

    @Test
    void nextTurn_validInput_success() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        gameService.nextTurn(123456);
        Mockito.verify(gameRepository, Mockito.times(1)).save(testGame);
        Mockito.verify(gameRepository, Mockito.times(1)).flush();
        assertEquals(1, testGame.getRoundsPlayed());
        assertEquals(0, testGame.getTurn().getTurnPoints());
    }

    @Test
    void createGame_validInput_success() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        User user = new User();
        user.setUsername("testName");
        user.setLeader(true);
        user.setId(1L);
        User user2 = new User();
        user2.setUsername("testName2");
        user2.setLeader(false);
        user2.setId(2L);
        User user3 = new User();
        user3.setUsername("testName3");
        user3.setLeader(false);
        user3.setId(3L);
        User user4 = new User();
        user4.setUsername("testName4");
        user4.setLeader(false);
        testLobby.setLobbyLeader(user);
        testLobby.addUserToLobby(user2);
        testLobby.addUserToLobby(user3);
        testLobby.addUserToLobby(user4);


        Game game = gameService.createGame(123456);
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(gameRepository, Mockito.times(1)).flush();
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(game);
        assertEquals("testName",game.getTeam1().getPlayers().get(0).getName());
        assertEquals("testName3",game.getTeam2().getPlayers().get(0).getName());
        assertEquals("testName2",game.getTeam1().getPlayers().get(1).getName());
        assertEquals("testName4",game.getTeam2().getPlayers().get(1).getName());
        assertEquals(123456,game.getAccessCode() );
        assertEquals(0,game.getRoundsPlayed());
    }

    @Test
    void getDrawnCard_success() {
        Card aCard = new Card("testWord", "testTaboo1", "testTaboo2", "testTaboo3", "testTaboo4", "testTaboo5");
        testGame.getTurn().addCard(aCard);
        testGame.getTurn().drawCard();
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        assertEquals(aCard, gameService.getDrawnCard(123456));
    }

    @Test
    void getDrawnCard_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.getDrawnCard(123456));
    }

    @Test
    void getGames_validInput_success() {
        List<Game> testGames = new ArrayList<>();
        testGames.add(testGame);
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.findAll()).thenReturn(testGames);
        assertEquals(testGames, gameService.getAllGames());
    }

    @Test
    void shuffleCards_invalidInput_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.shuffleCards(123456));
    }

    @Test
    void shuffleCards_validInput_success(){
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        gameService.shuffleCards(123456);
        Mockito.verify(gameRepository, Mockito.times(1)).save(testGame);
        Mockito.verify(gameRepository, Mockito.times(1)).flush();
    }

}