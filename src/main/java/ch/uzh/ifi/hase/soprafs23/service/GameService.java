package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.websockets.CardWebSocketHandler;
import ch.uzh.ifi.hase.soprafs23.websockets.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final GameRepository gameRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    private final LobbyRepository lobbyRepository;

    private CardWebSocketHandler cardWebSocketHandler;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, TeamService teamService, LobbyRepository lobbyRepository, TeamRepository teamRepository, UserService userService, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.teamService = teamService;
        this.lobbyRepository = lobbyRepository;
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void initializeCardWebSocketHandler(CardWebSocketHandler cardWebSocketHandler) {
        this.cardWebSocketHandler = cardWebSocketHandler;
    }

    public Game getGame(int accessCode) {
        return this.gameRepository.findByAccessCode(accessCode);
    }
    //updates the team at the end of a round with the points they made and switches the roles

    public Game createGame(int accessCode) {
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        Game newGame = new Game(lobby.getAccessCode(), lobby.getSettings(), teamService.createTeam(lobby.getTeam1(), Role.GUESSINGTEAM), teamService.createTeam(lobby.getTeam2(), Role.BUZZINGTEAM));
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created Information for Lobby: {}", newGame);
        return newGame;
    }


    public void nextTurn(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + accessCode + " does not exist");
        }
        int scoredPoints = existingGame.getTurn().getTurnPoints();
        existingGame.incrementRoundsPlayed();
        teamService.changeTurn(existingGame.getTeam1().getTeamId(), existingGame.getTeam2().getTeamId(), scoredPoints);
        gameRepository.save(existingGame);
        gameRepository.flush();
    }


    public Card drawCard(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + accessCode + " does not exist");
        }
        Card outCard = existingGame.getTurn().drawCard();
        gameRepository.flush();
        return outCard;
    }

    public Card buzz(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + accessCode + " does not exist");
        }
        Card outCard = existingGame.getTurn().buzz();
        gameRepository.flush();
        return outCard;
    }

    public PlayerRole getPlayerRole(int accessCode, String userName) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (teamService.isInTeam(existingGame.getTeam1().getTeamId(), userName)) {
            if (existingGame.getTeam1().getaRole() == Role.BUZZINGTEAM) {
                return PlayerRole.BUZZER;
            }
            else if (teamService.isClueGiver(existingGame.getTeam1().getTeamId(), userName)) {
                return PlayerRole.CLUEGIVER;
            }
            return PlayerRole.GUESSER;
        }
        else {
            if (existingGame.getTeam2().getaRole() == Role.BUZZINGTEAM) {
                return PlayerRole.BUZZER;
            }
            else if (teamService.isClueGiver(existingGame.getTeam2().getTeamId(), userName)) {
                return PlayerRole.CLUEGIVER;
            }
            return PlayerRole.GUESSER;
        }
    }

    public void guessWord(Message guess) {
        Game existingGame = gameRepository.findByAccessCode(guess.getAccessCode());
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + guess.getAccessCode() + " does not exist");
        }

        // Send a new card to the front end in case the guess is correct
        if (existingGame.getTurn().guess(guess.getContent())) {
            cardWebSocketHandler.callBack(existingGame.getTurn().drawCard());
        }
        gameRepository.flush(); // I might have changed the turn points and drawn a new card, so I need to flush.
    }

    public Card skip(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + accessCode + " does not exist");
        }
        Card outCard = existingGame.getTurn().skip();
        gameRepository.flush();
        return outCard;
    }

    public void createCard(int accessCode, Card card) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + accessCode + " does not exist");
        }
        existingGame.getTurn().addCard(card);
        gameRepository.flush();
    }
    public void deleteGame(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + accessCode + " does not exist");
        }
        List<User> users = lobbyRepository.findByAccessCode(accessCode).getLobbyUsers();
        userRepository.deleteAll(users);
        lobbyRepository.delete(lobbyRepository.findByAccessCode(accessCode));
        teamRepository.delete(existingGame.getTeam1());
        teamRepository.delete(existingGame.getTeam2());
        gameRepository.delete(existingGame);
        gameRepository.flush();
        teamRepository.flush();
        userRepository.flush();
        lobbyRepository.flush();
    }
}
