package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.websockets.TeamWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;


/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;

    private final SecureRandom random = new SecureRandom();

    private TeamWebSocketHandler teamWebSocketHandler;

    @Autowired
    public LobbyService(@Qualifier("userRepository") UserRepository userRepository, LobbyRepository lobbyRepository) {
        this.userRepository = userRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public void initializeTeamWebSocketHandler(TeamWebSocketHandler teamWebSocketHandler) {
        this.teamWebSocketHandler = teamWebSocketHandler;
    }

    public Lobby createLobby() {
        Lobby newLobby = new Lobby();
        newLobby.setSettings(new Settings());
        newLobby.setAccessCode(createAccessCode());
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }

    public int createAccessCode() {
        int min = 100000;
        int max = 999999;

        // Generate a unique access code

        while (true) {
            int accessCode = random.nextInt((max - min) + 1) + min;

            if (!checkIfLobbyExists(accessCode)) {
                return accessCode;
            }
        }
    }

    private boolean checkIfLobbyExists(int accessCode) {
        return lobbyRepository.findByAccessCode(accessCode) != null;
    }


    public Lobby joinLobbyTeam(int accessCode, int teamNr, int userId) {
        Lobby existingLobby = lobbyRepository.findByAccessCode(accessCode);
        User userInput = userRepository.findById(userId);
        if (existingLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the user could not be added to the lobby team!");
        }
        if (userInput == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided does not exist. Therefore, the user could not be added to the lobby team!");
        }
        else if (!existingLobby.isUserInLobby(userInput)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided is not in the lobby. Therefore, the user could not be added to the lobby team!");
        }

        if (!existingLobby.isFairJoin(teamNr, userInput)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Joining this team would lead to an unfair game. Therefore, wait until more users have joined the lobby or join the other team!");
        }

        //check if user already is in a team
        if (existingLobby.isUserInTeam1(userInput)) {
            existingLobby.removeUserFromTeam1(userInput);
            teamWebSocketHandler.callBack(accessCode, 1, userId);
        }

        if (existingLobby.isUserInTeam2(userInput)) {
            existingLobby.removeUserFromTeam2(userInput);
            teamWebSocketHandler.callBack(accessCode, 2, userId);
        }

        if (teamNr == 1) {
            existingLobby.addUserToTeam1(userInput);
        }
        else if (teamNr == 2) {
            existingLobby.addUserToTeam2(userInput);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The team number provided is not valid. Therefore, the user could not be added to the lobby team!");
        }
        existingLobby = lobbyRepository.save(existingLobby);
        lobbyRepository.flush();
        log.debug("Added User to lobby team: {}", teamNr);
        return existingLobby;
    }

    public Lobby leaveLobbyTeam(int accessCode, int teamNr, int userId) {
        Lobby existingLobby = lobbyRepository.findByAccessCode(accessCode);
        User userInput = userRepository.findById(userId);
        // Check if lobby exists
        if (existingLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the user could not be removed from the lobby team!");
        }
        else if (userInput == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided does not exist. Therefore, the user could not be removed from the lobby team!");
        }
        if (teamNr != 1 && teamNr != 2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The team number provided is not valid. Therefore, the user could not be removed from the lobby team!");
        }
        else if (teamNr == 1 && existingLobby.isUserInTeam1(userInput)) {
            existingLobby.removeUserFromTeam1(userInput);
        }
        else if (teamNr == 2 && existingLobby.isUserInTeam2(userInput)) {
            existingLobby.removeUserFromTeam2(userInput);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user with the id provided is not in a lobby team.");
        }
        existingLobby = lobbyRepository.save(existingLobby);
        lobbyRepository.flush();
        log.debug("Removed User from lobby team: {}", teamNr);
        return existingLobby;
    }

    public Lobby joinLobby(int accessCode, long userId) {
        Lobby existingLobby = lobbyRepository.findByAccessCode(accessCode);
        User existingUser = userRepository.findById(userId);

        if (existingLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the user could not be added to the lobby!");
        }
        else if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided does not exist. Therefore, the user could not be added to the lobby!");
        }
        if (existingLobby.getLobbyLeader() == null && existingUser.isLeader()) {
            existingLobby.setLobbyLeader(existingUser);
        }
        existingLobby.addUserToLobby(existingUser);
        existingLobby = lobbyRepository.save(existingLobby);
        lobbyRepository.flush();
        log.debug("Added User to lobby: {}", existingLobby);
        return existingLobby;
    }

    public Lobby leaveLobby(int accessCode, int userId) {
        Lobby existingLobby = lobbyRepository.findByAccessCode(accessCode);
        User existingUser = userRepository.findById(userId);

        if (existingLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the user could not be removed from the lobby!");
        }
        else if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided does not exist. Therefore, the user could not be removed from the lobby!");
        }
        if (existingLobby.getTeam1().contains(existingUser)) {
            existingLobby.removeUserFromTeam1(existingUser);
        }
        else if (existingLobby.getTeam2().contains(existingUser)) {
            existingLobby.removeUserFromTeam2(existingUser);
        }
        existingLobby.removeUserFromLobby(existingUser);
        userRepository.delete(existingUser);
        userRepository.flush();
        existingLobby = lobbyRepository.save(existingLobby);
        lobbyRepository.flush();
        log.debug("Removed User from lobby: {}", existingLobby);
        return existingLobby;
    }

    public Lobby getLobby(int accessCode) {
        Lobby existingLobby = lobbyRepository.findByAccessCode(accessCode);
        if (existingLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the lobby could not be returned!");
        }
        return existingLobby;
    }

    public void changeSettings(int accessCode, Settings settings) {
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with accessCode " + accessCode + " does not exist");
        }
        lobby.setSettings(settings);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public boolean userIsInLobby(long userId, int accessCode) {
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        User user = userRepository.findById(userId);
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with accessCode " + accessCode + " does not exist");
        }
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " does not exist");
        }
        return lobby.isUserInLobby(user);
    }

    public void deleteLobbyAndUsers(int accessCode) {
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with accessCode " + accessCode + " does not exist");
        }
        List<User> users = lobbyRepository.findByAccessCode(accessCode).getLobbyUsers();
        userRepository.deleteAll(users);
        lobbyRepository.delete(lobby);
        userRepository.flush();
        lobbyRepository.flush();
    }

    public boolean allUsersInTeam(int accessCode) {
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        List<User> allUsers = lobby.getLobbyUsers();
        for (User user : allUsers) {
            if (!lobby.isUserInTeam1(user) && !lobby.isUserInTeam2(user)) {
                return false;
            }
        }
        return true;
    }

    public int getRemainingPlayersNeeded(int accessCode) {
        int remainingPlayers = 0;
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        List<User> allUsers = lobby.getLobbyUsers();
        remainingPlayers = 4 - allUsers.size();
        if (remainingPlayers < 0) {
            remainingPlayers = 0;
        }
        return remainingPlayers;
    }

    public void deleteUsersNotInTeam(int accessCode) {
        Lobby lobby = lobbyRepository.findByAccessCode(accessCode);
        List<User> allUsers = lobby.getLobbyUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (!lobby.isUserInTeam1(allUsers.get(i)) && !lobby.isUserInTeam2(allUsers.get(i))) {
                leaveLobby(accessCode, Math.toIntExact(allUsers.get(i).getId()));
            }
        }
        userRepository.flush();
    }

}
