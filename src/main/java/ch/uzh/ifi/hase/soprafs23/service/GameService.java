package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
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
    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, TeamService teamService, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.teamService = teamService;
        this.userRepository = userRepository;
    }


    public Game getGame(int accessCode){return this.gameRepository.findByAccessCode( accessCode);}
    //updates the team at the end of a round with the points they made and switches the roles

    public Game createGame(int accessCode) {
        Game newGame = new Game();
        newGame.setSettings(new Settings());
        newGame.setAccessCode(accessCode);
        Team team1 = teamService.createTeam();
        Team team2 = teamService.createTeam();
        newGame.setTeam1(team1);
        newGame.setTeam2(team2);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created Information for Lobby: {}", newGame);
        return newGame;
    }

    public Game addPlayer(int accessCode, int teamId, int userID){
        Game game = gameRepository.findByAccessCode(accessCode);

        if (teamId==1){
            Team team = game.getTeam1();
            List<Player> players = team.getPlayers();
            players.add(convertUserToPlayer(userID));
            team.setPlayers(players);
            game.setTeam1(team);
        }else if (teamId == 2) {
            Team team = game.getTeam2();
            List<Player> players = team.getPlayers();
            players.add(convertUserToPlayer(userID));
            team.setPlayers(players);
            game.setTeam2(team);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The team with the access code provided does not exist. Therefore, the user could not be added from the lobby!");
        }
        return game;
    }

    public Player convertUserToPlayer(int userId){
        User user = userRepository.findById(userId);
        Player player = new Player();
        player.setName(user.getUsername());
        player.setLeader(user.isLeader());
        player.setPersonalScore(0);
        return player;
    }

}
