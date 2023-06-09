package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.rest.dto.CardDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
        Game createdGame = gameService.createGame(accessCode);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getAllGames() {
        List<Game> games = gameService.getAllGames();
        List<GameGetDTO> gameGetDTOS = new ArrayList<>();

        for (Game game : games) {
            gameGetDTOS.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
        return gameGetDTOS;
    }

    @GetMapping("/games/{accessCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable int accessCode) {
        // create lobby
        Game createdGame = gameService.getGame(accessCode);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @PutMapping("/games/{accessCode}/turns")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void nextTurn(@PathVariable int accessCode) {
        gameService.nextTurn(accessCode);
    }

    @PutMapping("/games/{accessCode}/finishes")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void finishGame(@PathVariable int accessCode) {
        gameService.finishGame(accessCode);
    }

    @GetMapping("/games/{accessCode}/users/{playerName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerRole getPlayerRole(@PathVariable int accessCode, @PathVariable String playerName) {
        // create lobby
        // convert internal representation of user back to API
        return gameService.getPlayerRole(accessCode, playerName);
    }


    @GetMapping("/games/{accessCode}/players/MVP")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player getMVPPlayer(@PathVariable int accessCode) {
        // create lobby
        // convert internal representation of user back to API
        return gameService.getMPVPlayer(accessCode);
    }


    @PostMapping("/games/{accessCode}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createCard(@PathVariable int accessCode, @RequestBody CardDTO cardDTO) {
        Card inputCard = DTOMapper.INSTANCE.convertCardDTOtoEntity(cardDTO);
        gameService.createCard(accessCode, inputCard);
    }

    @PutMapping("/games/{accessCode}/cards")
    @ResponseStatus(HttpStatus.OK)
    public void shuffleCards(@PathVariable int accessCode) {
        gameService.shuffleCards(accessCode);
    }


    @DeleteMapping("/games/{accessCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteGameTeamsAndPlayers(@PathVariable int accessCode) {
        gameService.deleteGameTeamsUsersAndLobby(accessCode);
    }

    @DeleteMapping("/games/{accessCode}/{playerName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deletePlayerFromGame(@PathVariable int accessCode, @PathVariable String playerName) {
        gameService.deletePlayerFromGame(accessCode, playerName);
    }
}



