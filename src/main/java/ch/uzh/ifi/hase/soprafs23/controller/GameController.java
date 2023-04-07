package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class GameController {

  private final GameService gameService;

  GameController(GameService gameService) {
    this.gameService = gameService;
  }


    @PostMapping("/games/{accessCode}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@PathVariable int accessCode) {
        // create lobby
        Game createdGame = gameService.createGame(accessCode);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @PutMapping ("/games/{accessCode}/additions/teams/{teamID}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO addPlayerToTeam(@PathVariable int accessCode,@PathVariable int teamID, @PathVariable int userId) {
        // join lobby
        Game game = gameService.addPlayer(accessCode, teamID,userId);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }


}



