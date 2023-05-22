package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.SettingsPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
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
public class LobbyController {

    private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }


    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby() {
        // create lobby
        Lobby createdLobby = lobbyService.createLobby();
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @GetMapping("/lobbies/{accessCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable int accessCode) {
        // create lobby
        Lobby createdLobby = lobbyService.getLobby(accessCode);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {

        List<Lobby> lobbies = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbyGetDTOS = new ArrayList<>();

        for (Lobby lobby : lobbies) {
            lobbyGetDTOS.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        }
        return lobbyGetDTOS;
    }

    @PutMapping("/lobbies/{accessCode}/additions/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO joinLobby(@PathVariable int accessCode, @PathVariable int userId) {
        // join lobby
        Lobby joinedLobby = lobbyService.joinLobby(accessCode, userId);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(joinedLobby);
    }

    @PutMapping("/lobbies/{accessCode}/removals/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leaveLobby(@PathVariable int accessCode, @PathVariable int userId) {
        // leave lobby
        lobbyService.leaveLobby(accessCode, userId);
    }

    @PutMapping("/lobbies/{accessCode}/teams/{teamNr}/additions/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void joinLobbyTeam(@PathVariable int accessCode, @PathVariable int teamNr, @PathVariable int userId) {
        // join lobby team
        lobbyService.joinLobbyTeam(accessCode, teamNr, userId);
    }

    @PutMapping("/lobbies/{accessCode}/teams/{teamNr}/removals/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leaveLobbyTeam(@PathVariable int accessCode, @PathVariable int teamNr, @PathVariable int userId) {
        // join lobby
        lobbyService.leaveLobbyTeam(accessCode, teamNr, userId);

    }

    @PutMapping("/lobbies/{accessCode}/settings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void changeSettings(@RequestBody SettingsPutDTO settingsPutDTO, @PathVariable int accessCode) {
        // change settings
        Settings settingsInput = DTOMapper.INSTANCE.convertSettingsPutDTOtoEntity(settingsPutDTO);
        lobbyService.changeSettings(accessCode, settingsInput);

    }

    @GetMapping("/lobbies/{accessCode}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean isUserInLobby(@PathVariable int accessCode, @PathVariable int userId) {
        // check if user is in lobby
        return lobbyService.userIsInLobby(userId, accessCode);
    }

    @GetMapping("/lobbies/{accessCode}/users/teams")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean allUsersInTeam(@PathVariable int accessCode) {
        // check if users are in a team
        return lobbyService.allUsersInTeam(accessCode);
    }

    @DeleteMapping("/lobbies/{accessCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteLobbyAndUsers(@PathVariable int accessCode) {
        lobbyService.deleteLobbyAndUsers(accessCode);
    }

}



