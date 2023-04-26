package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Topic;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    private Lobby testLobby;
    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testLobby = new Lobby();
        testLobby.setAccessCode(123456);
        testLobby.setSettings(new Settings());

        testUser = new User();
        testUser.setId(1L);
        testUser.setLeader(true);
        testUser.setUsername("testUsername");

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);

        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
    }

    @Test
    void createAccessCode_noDuplicate() {
        int testCode1 = lobbyService.createAccessCode();
        int testCode2 = lobbyService.createAccessCode();
        int testCode3 = lobbyService.createAccessCode();

        assertNotEquals(testCode1,testCode2);
        assertNotEquals(testCode3,testCode2);
    }
    @Test
    void createLobby_validInput(){
        Lobby lobby = lobbyService.createLobby();
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(lobby.getSettings(),testLobby.getSettings());
        assertEquals(lobby.getAccessCode(),testLobby.getAccessCode());

    }
    @Test
    void joinLobbyTeam_LobbyDoesntExist() {
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobbyTeam(123445, 1, 1));
    }
    @Test
    void joinLobbyTeam_UserDoesntExist() {
        Lobby lobby = new Lobby();
        int code = lobby.getAccessCode();
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobbyTeam(code, 1, 1));
    }
    @Test
    void joinLobbyTeam_UserNotInLobby() {
        Lobby lobby = new Lobby();
        User user =new User();
        Long id = user.getId();
        int code = lobby.getAccessCode();
        assertThrows(NullPointerException.class, () -> lobbyService.joinLobbyTeam(code, 1, Math.toIntExact(id)));
    }
    @Test
    void joinLobbyTeam_joinTeam1ValidInput() {
        List<User> users = new ArrayList<>();
        users.add(testUser);

        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        testLobby = lobbyService.joinLobbyTeam(testLobby.getAccessCode(),1, 1);

        assertEquals(testLobby.getTeam1(),users);
    }
    @Test
    void joinLobbyTeam_joinTeam2ValidInput() {
        List<User> users = new ArrayList<>();
        users.add(testUser);

        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        testLobby = lobbyService.joinLobbyTeam(testLobby.getAccessCode(),2, 1);

        assertEquals(testLobby.getTeam2(),users);
    }
    @Test
    void leaveTeam_team2() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        testLobby = lobbyService.joinLobbyTeam(testLobby.getAccessCode(),2, 1);
        assertEquals(testLobby.getTeam2(),users);

        testLobby = lobbyService.leaveLobbyTeam(testLobby.getAccessCode(),2,1);
        users.clear();
        assertEquals(testLobby.getTeam2(),users);
    }
    @Test
    void leaveTeam_team1() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        testLobby = lobbyService.joinLobbyTeam(testLobby.getAccessCode(),1, 1);
        assertEquals(testLobby.getTeam1(),users);

        testLobby = lobbyService.leaveLobbyTeam(testLobby.getAccessCode(),1,1);
        users.clear();
        assertEquals(testLobby.getTeam1(),users);
    }
    @Test
    void joinLobby_validInput() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);

        assertEquals(testLobby.getLobbyUsers(),users);

    }
    @Test
    void leaveLobby_validInput() {
        List<User> users = new ArrayList<>();
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);


        testLobby = lobbyService.leaveLobby(testLobby.getAccessCode(), 1);

        assertEquals(testLobby.getLobbyUsers(),users);

    }
    @Test
    void changeSettings_validInput() {
        Settings settings = new Settings();
        settings.setRounds(2);
        settings.setTopic(Topic.FOOD);
        settings.setRoundTime(2);

        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);

        lobbyService.changeSettings(testLobby.getAccessCode(), settings);

        assertEquals(testLobby.getSettings(),settings);

    }
    @Test
    void changeSettings_invalidLobby() {
        Settings settings = new Settings();
        settings.setRounds(2);
        settings.setTopic(Topic.FOOD);
        settings.setRoundTime(2);

        assertThrows(ResponseStatusException.class, () -> lobbyService.changeSettings(123123,settings));

    }




}
