package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;

import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.websockets.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;



@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final GameRepository gameRepository;
    private final TeamService teamService;

    private final LobbyRepository lobbyRepository;
    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, TeamService teamService , LobbyRepository lobbyRepository) {
        this.gameRepository = gameRepository;
        this.teamService = teamService;
        this.lobbyRepository = lobbyRepository;
    }


    public Game getGame(int accessCode){return this.gameRepository.findByAccessCode( accessCode);}
    //updates the team at the end of a round with the points they made and switches the roles

    public Game createGame(int accessCode) {
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        Game newGame = new Game(lobby.getAccessCode(),lobby.getSettings(),teamService.createTeam(lobby.getTeam1(), Role.GUESSINGTEAM),teamService.createTeam(lobby.getTeam2(),Role.BUZZINGTEAM));
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created Information for Lobby: {}", newGame);
        return newGame;
    }


    public void nextTurn(int accessCode, int scoredPoints) {
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + accessCode + " does not exist");
        }
        existingGame.incrementRoundsPlayed();
        teamService.changeTurn(existingGame.getTeam1().getTeamId(), existingGame.getTeam2().getTeamId(), scoredPoints);
        gameRepository.save(existingGame);
        gameRepository.flush();
    }

    public PlayerRole getPlayerRole(int accessCode,String userName){
        Game existingGame = gameRepository.findByAccessCode(accessCode);
        if (teamService.isInTeam(existingGame.getTeam1().getTeamId(),userName)){
            if(existingGame.getTeam1().getaRole()==Role.BUZZINGTEAM){
                return PlayerRole.BUZZER;
            }else if (teamService.isClueGiver(existingGame.getTeam1().getTeamId(),userName)){
                return PlayerRole.CLUEGIVER;
            }
            return PlayerRole.GUESSER;
        }else {
            if(existingGame.getTeam2().getaRole()==Role.BUZZINGTEAM){
                return PlayerRole.BUZZER;
            }else if (teamService.isClueGiver(existingGame.getTeam2().getTeamId(),userName)){
                return PlayerRole.CLUEGIVER;
            }
            return PlayerRole.GUESSER;
        }



    public void guessWord(Message guess){
        Game existingGame = gameRepository.findByAccessCode(guess.getAccessCode());
        if (existingGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with accessCode " + guess.getAccessCode() + " does not exist");
        }
        // TODO Send a new card to the front end in case the following method returns true
        existingGame.getTurn().guess(guess.getContent());
        gameRepository.flush(); // I might have changed the turn points, so I need to flush.

        // Idea: This method could return a boolean for a true guess. If it does, I could clear the chat.
    }
}
