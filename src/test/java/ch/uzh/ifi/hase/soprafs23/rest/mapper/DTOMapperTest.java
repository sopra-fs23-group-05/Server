package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.constant.Topic;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    // create UserPostDTO
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setLeader(true);
    userPostDTO.setUsername("username");

    // MAP -> Create user
    User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check content
    assertEquals(userPostDTO.isLeader(), user.isLeader());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    User user = new User();
    user.setLeader(true);
    user.setUsername("firstname@lastname");


    // MAP -> Create UserGetDTO
    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    // check content
    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.isLeader(), userGetDTO.isLeader());
    assertEquals(user.getUsername(), userGetDTO.getUsername());

  }
  @Test
    void testGetTeam_fromTeam_toTeamGetDTO(){
      Team team = new Team();
      team.setaRole(Role.BUZZINGTEAM);
      team.setTeamId(1);
      List<Player> players = new ArrayList<>();
      team.setPlayers(players);
      team.setPoints(0);
      team.setIdxClueGiver(0);
      team.setTeamName("a");

      TeamGetDTO teamGetDTO = DTOMapper.INSTANCE.convertEntityToTeamGetDTO(team);

      assertEquals(team.getTeamId(),teamGetDTO.getTeamId());
      assertEquals(team.getPoints(),teamGetDTO.getPoints());
      assertEquals(team.getaRole(),teamGetDTO.getaRole());
      assertEquals(team.getPlayers(),teamGetDTO.getPlayers());

  }
    @Test
    void testSettings_fromSettingsPutDTO_toSettings(){
        SettingsPutDTO settingsPutDTO = new SettingsPutDTO();
        settingsPutDTO.setRounds(7);
        settingsPutDTO.setRoundTime(120);
        settingsPutDTO.setTopic(Topic.MOVIES);

        Settings settings = DTOMapper.INSTANCE.convertSettingsPutDTOtoEntity(settingsPutDTO);

        assertEquals(settings.getRounds(),settingsPutDTO.getRounds());
        assertEquals(settings.getRoundTime(),settingsPutDTO.getRoundTime());
        assertEquals(settings.getTopic(),settingsPutDTO.getTopic());
    }
    @Test
    void testLobby_fromLobby_toLobbyGetDTO(){
        Lobby lobby = new Lobby();

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        assertEquals(lobby.getAccessCode(),lobbyGetDTO.getAccessCode());
        assertEquals(lobby.getTeam1(),lobbyGetDTO.getTeam1());
        assertEquals(lobby.getTeam2(),lobbyGetDTO.getTeam2());
    }
    @Test
    void testGame_fromGame_toGameGetDTO(){
        Game game= new Game(123456,new Settings(),new Team(),new Team());

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

        assertEquals(gameGetDTO.getAccessCode(),game.getAccessCode());
        assertEquals(gameGetDTO.getTeam1(),game.getTeam1());
        assertEquals(gameGetDTO.getTeam2(),game.getTeam2());
        assertEquals(gameGetDTO.getRoundsPlayed(),game.getRoundsPlayed());
        assertEquals(gameGetDTO.getTurn(),game.getTurn());
        assertEquals(gameGetDTO.getSettings(),game.getSettings());
    }
}
