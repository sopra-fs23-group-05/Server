package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
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
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository, LobbyRepository lobbyRepository) {
    this.userRepository = userRepository;
      this.lobbyRepository = lobbyRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.OFFLINE);
    validateUsername(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

    public Lobby createLobby() {
      Lobby newLobby = new Lobby();
      newLobby.setSettings(new Settings());
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void validateUsername(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "The username provided is not unique. Therefore, the user could not be created!");
    }else if(userToBeCreated.getUsername().equals("")){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username provided is empty. Therefore, the user could not be created!");
    }
  }

    public Lobby joinLobbyTeam(int accessCode, int teamNr, int userId) {
      Lobby existingLobby = lobbyRepository.findByAccessCode(accessCode);
        User userInput = userRepository.findById(userId);
        if(existingLobby == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the user could not be added to the lobby team!");
        }
        if(userInput == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided does not exist. Therefore, the user could not be added to the lobby team!");
        }else if (!existingLobby.isUserInLobby(userInput)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided is not in the lobby. Therefore, the user could not be added to the lobby team!");
        }

        if(teamNr == 1){
          existingLobby.addUserToTeam1(userInput);
      }else if(teamNr == 2){
          existingLobby.addUserToTeam2(userInput);
      }else{
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
        if(existingLobby == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the user could not be removed from the lobby team!");
        }else if(userInput == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided does not exist. Therefore, the user could not be removed from the lobby team!");
        }
        if(teamNr != 1 && teamNr != 2){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The team number provided is not valid. Therefore, the user could not be removed from the lobby team!");
        }else if(teamNr == 1 && existingLobby.isUserInTeam1(userInput)){
            existingLobby.removeUserFromTeam1(userInput);
        }else if(teamNr == 2 && existingLobby.isUserInTeam2(userInput)){
            existingLobby.removeUserFromTeam2(userInput);
        }else{
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

        if(existingLobby == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the user could not be added to the lobby!");
        }else if(existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided does not exist. Therefore, the user could not be added to the lobby!");
        }
        if(existingLobby.getLobbyLeader() == null && existingUser.isLeader()){
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

        if(existingLobby == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the access code provided does not exist. Therefore, the user could not be removed from the lobby!");
        }else if(existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the id provided does not exist. Therefore, the user could not be removed from the lobby!");
        }
        existingLobby.removeUserFromLobby(existingUser);
        existingLobby = lobbyRepository.save(existingLobby);
        lobbyRepository.flush();
        log.debug("Removed User from lobby: {}", existingLobby);
        return existingLobby;
    }

    public Lobby getLobby(int accessCode) {
        Lobby existingLobby = lobbyRepository.findByAccessCode(accessCode);
        System.out.println("-----------------" + existingLobby.getAccessCode() + "-----------------");
        return existingLobby;
    }
}
