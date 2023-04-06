package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
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
public class LobbyService {

  private final Logger log = LoggerFactory.getLogger(LobbyService.class);

  private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final TeamRepository teamRepository;
  @Autowired
  public LobbyService(@Qualifier("userRepository") UserRepository userRepository, LobbyRepository lobbyRepository, TeamRepository teamRepository) {
    this.userRepository = userRepository;
      this.lobbyRepository = lobbyRepository;
      this.teamRepository = teamRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }
  public Team getTeam(int teamId){return this.teamRepository.findById(teamId);}

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

    public Lobby createLobby(User leader) {
      leader = userRepository.findByUsername(leader.getUsername());
      Lobby newLobby = new Lobby();
      newLobby.setAccessCode();
      newLobby.setLobbyLeader(leader);
      newLobby.setSettings(new Settings());
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }
    //updates the team at the end of a round with the points they amde and switches the roles
    public Team updateTeam(int points,Team team){
      team.setPoints(team.getPoints()+points);
      int teamSize = team.getPlayers().size();
      if (team.getIdxClueGiver()<teamSize){
          team.setIdxClueGiver(team.getIdxClueGiver()+1);
      }else{
          team.setIdxClueGiver(0);
      }
      if (team.getaRole()== Role.BUZZINGTEAM){
          team.setaRole(Role.GUESSINGTEAM);
      }else{
          team.setaRole(Role.BUZZINGTEAM);
      }
      return team;
    }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws ResponseStatusException
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
}
