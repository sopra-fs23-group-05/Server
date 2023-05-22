package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
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
import ch.uzh.ifi.hase.soprafs23.websockets.CardWebSocketHandler;
import ch.uzh.ifi.hase.soprafs23.websockets.ChatWebSocketHandler;
import ch.uzh.ifi.hase.soprafs23.websockets.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;

    private CardWebSocketHandler cardWebSocketHandler;
    private ChatWebSocketHandler chatWebSocketHandler;

    private final String gameWithCode = "Game with accessCode ";
    private final String doesNotExist = " does not exist";

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
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }
        return existingGame;
    }
    //updates the team at the end of a round with the points they made and switches the roles

    public Game createGame(int accessCode) {
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        /* TODO We create two Player objects for the lobby leader. This is bad. Remove Player lobbyLeader as parameter constructor.
        *   Instead, within the Game constructor, iterate over all Player objects, check if the Player is the leader, and if so set
        *   the Game.leader field. */
        Game newGame = new Game(lobby.getAccessCode(), lobby.getSettings(), teamService.createTeam(lobby.getTeam1(), Role.GUESSINGTEAM), teamService.createTeam(lobby.getTeam2(), Role.BUZZINGTEAM), teamService.convertUserToPlayer(lobby.getLobbyLeader().getId()));
        // saves the given entity but data is only persisted in the database once flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created Information for Lobby: {}", newGame);
        return newGame;
    }


    public void nextTurn(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }

        int scoredPoints = existingGame.getTurn().getTurnPoints();

        existingGame.incrementRoundsPlayed();

        teamService.changeTurn(existingGame.getTeam1().getTeamId(), existingGame.getTeam2().getTeamId(), scoredPoints);
        existingGame.getTurn().setTurnPoints(0);
        gameRepository.save(existingGame);
        gameRepository.flush();
    }


    public Card drawCard(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }
        Card outCard = existingGame.getTurn().drawCard();
        chatWebSocketHandler.sendInformationCallBack(accessCode);
        gameRepository.flush();
        existingGame.getTurn().resetBuzzCounter();
        return outCard;
    }

    public Card buzz(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }

        Card outCard = existingGame.getTurn().buzz();
        if(outCard != null){
            chatWebSocketHandler.sendInformationCallBack(accessCode);
            gameRepository.flush();
        }else{
            outCard = existingGame.getTurn().getDrawnCard();
        }
        return outCard;
    }

    public Card skip(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }
        Card outCard = existingGame.getTurn().skip();
        chatWebSocketHandler.sendInformationCallBack(accessCode);
        gameRepository.flush();
        existingGame.getTurn().resetBuzzCounter();
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

    public Player getMPVPlayer(int accessCode) {
        int maxScore = 0;
        Player MVPPlayer = null;
        Game gameByAccessCode = gameRepository.findByAccessCode(accessCode);
        List<Player> playersTeam1 = gameByAccessCode.getTeam1().getPlayers();
        for (Player player : playersTeam1) {
            if (player.getPersonalScore() > maxScore) {
                maxScore = player.getPersonalScore();
                MVPPlayer = player;
            }
        }

        List<Player> playersTeam2 = gameByAccessCode.getTeam2().getPlayers();
        for (Player player : playersTeam2) {
            if (player.getPersonalScore() > maxScore) {
                maxScore = player.getPersonalScore();
                MVPPlayer = player;
            }
        }
        return MVPPlayer;
    }


    public void guessWord(Message guess) {
        Game existingGame = gameRepository.findByAccessCode(guess.getAccessCode());
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + guess.getAccessCode() + doesNotExist);
        }

        /* Send a new card to the front end and increase the guessers personal score
         * in case the guess is correct */
        if (existingGame.getTurn().guess(guess.getContent())) {
            String guessingUser = userService.getUser(guess.getSenderId()).getUsername();   // guessers username
            // teamId of the players team
            int teamId = (teamService.isInTeam(existingGame.getTeam1().getTeamId(), guessingUser)) ? existingGame.getTeam1().getTeamId() : existingGame.getTeam2().getTeamId();
            // Check if the guesser is in the guessing team and not the clue giver
            if (teamService.getTeamRole(teamId) == Role.GUESSINGTEAM && !teamService.isClueGiver(teamId, guessingUser)) {
                teamService.increasePlayerScore(teamId, guessingUser);
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player is not in the guessing team or is the clue giver");
            }

            // Call the card websocket, so it sends a new card to the clients.
            cardWebSocketHandler.callBack(guess.getAccessCode(), existingGame.getTurn().drawCard(), existingGame.getTurn().getTurnPoints());
            // Call the chat websocket, so it sends a new message to the clients.
            chatWebSocketHandler.sendInformationCallBack(guess.getAccessCode());
        }
        gameRepository.flush(); // I might have changed the turn points, drawn a new card and changed a Player, so I need to flush.
    }

    public void createCard(int accessCode, Card card) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }
        existingGame.getTurn().addCard(card);
        gameRepository.flush();
    }

    public Integer getTurnPoints(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }
        return existingGame.getTurn().getTurnPoints();
    }


    public void deleteGameTeamsUsersAndLobby(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        userRepository.deleteAll(lobby.getLobbyUsers());
        lobbyRepository.delete(lobbyRepository.findByAccessCode(accessCode));
        teamRepository.delete(existingGame.getTeam1());
        teamRepository.delete(existingGame.getTeam2());
        gameRepository.delete(existingGame);
        gameRepository.flush();
        teamRepository.flush();
        userRepository.flush();
        lobbyRepository.flush();
    }

    public void deletePlayerFromTeam(Team team, String playerName, int accessCode) {
        List<Player> playersTeam = team.getPlayers();
        int playerIdx = 0;
        for (Player player : playersTeam) {
            if (player.getName().equals(playerName)) {
                team.getPlayers().remove(playerIdx);
                gameRepository.flush();
                return;
            }
            playerIdx++;
        }
    }

    public void deletePlayerFromGame(int accessCode, String playerName){
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }
        if(teamService.isInTeam(existingGame.getTeam1().getTeamId(), playerName)){
            deletePlayerFromTeam(existingGame.getTeam1(), playerName, accessCode);
        }
        else if(teamService.isInTeam(existingGame.getTeam2().getTeamId(), playerName)){
            deletePlayerFromTeam(existingGame.getTeam2(), playerName, accessCode);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with name " + playerName + doesNotExist);
        }
    }

    public void finishGame(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }
        Settings settings = existingGame.getSettings();
        settings.setRounds(existingGame.getRoundsPlayed());
        existingGame.setSettings(settings);
        gameRepository.flush();
    }

    public void initializeChatWebSocketHandler(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    public Card getDrawnCard(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + accessCode + " does not exist");
        }
        return existingGame.getTurn().getDrawnCard();
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public void shuffleCards(int accessCode) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, gameWithCode + accessCode + doesNotExist);
        }

        existingGame.getTurn().getDeck().shuffle();
        existingGame.getTurn().drawCard();
        gameRepository.save(existingGame);
        gameRepository.flush();
    }
}
