package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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

    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final GameRepository gameRepository;
    private final TeamService teamService;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, TeamService teamService, UserRepository userRepository, LobbyRepository lobbyRepository) {
        this.gameRepository = gameRepository;
        this.teamService = teamService;
        this.userRepository = userRepository;
        this.lobbyRepository = lobbyRepository;
    }


    public Game getGame(int accessCode){return this.gameRepository.findByAccessCode( accessCode);}
    //updates the team at the end of a round with the points they made and switches the roles

    public Game createGame(int accessCode) {
        Game newGame = new Game();
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);

        newGame.setSettings(lobby.getSettings());
        newGame.setAccessCode(accessCode);

        Team team1 = teamService.createTeam(lobby.getTeam1());
        Team team2 = teamService.createTeam(lobby.getTeam2());
        newGame.setTeam1(team1);
        newGame.setTeam2(team2);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created Information for Lobby: {}", newGame);
        return newGame;
    }




}