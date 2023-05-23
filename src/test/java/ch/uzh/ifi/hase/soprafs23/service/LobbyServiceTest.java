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

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;
    @Mock
    private UserRepository userRepository;

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


        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        Mockito.when(lobbyRepository.save(testLobby)).thenReturn(testLobby);
    }

    @Test
    void createAccessCode_noDuplicate() {
        int testCode1 = lobbyService.createAccessCode();
        int testCode2 = lobbyService.createAccessCode();
        int testCode3 = lobbyService.createAccessCode();

        assertNotEquals(testCode1, testCode2);
        assertNotEquals(testCode3, testCode2);
    }

    @Test
    void createLobby_validInput() {
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        assertEquals(lobbyService.createLobby(), testLobby);
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();
    }

    @Test
    void joinLobbyTeam_LobbyDoesntExist() {
        Mockito.when(lobbyRepository.findByAccessCode(123445)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobbyTeam(123445, 1, 1));
    }


    @Test
    void joinLobbyTeam_UserDoesntExist() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        Mockito.when(userRepository.findById(1L)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobbyTeam(123456, 1, 1));
    }

    @Test
    void joinLobbyTeam_UserNotInLobby() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        User user = new User();
        user.setId(5L);
        user.setLeader(false);
        Mockito.when(userRepository.findById(5L)).thenReturn(user);
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobbyTeam(123456, 1, 1));
    }


    @Test
    void joinLobbyTeam_joinTeam1ValidInput() {
        List<User> users = new ArrayList<>();
        users.add(testUser);

        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        testLobby = lobbyService.joinLobbyTeam(testLobby.getAccessCode(), 1, 1);

        assertEquals(testLobby.getTeam1(), users);
    }

    @Test
    void joinLobbyTeam_joinTeam2ValidInput() {
        List<User> users = new ArrayList<>();
        users.add(testUser);

        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        testLobby = lobbyService.joinLobbyTeam(testLobby.getAccessCode(), 2, 1);

        assertEquals(testLobby.getTeam2(), users);
    }


    @Test
    void leaveTeam_team2() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        testLobby = lobbyService.joinLobbyTeam(testLobby.getAccessCode(), 2, 1);
        assertEquals(testLobby.getTeam2(), users);

        testLobby = lobbyService.leaveLobbyTeam(testLobby.getAccessCode(), 2, 1);
        users.clear();
        assertEquals(testLobby.getTeam2(), users);
    }

    @Test
    void leaveTeam_team1() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        testLobby = lobbyService.joinLobbyTeam(testLobby.getAccessCode(), 1, 1);
        assertEquals(testLobby.getTeam1(), users);

        testLobby = lobbyService.leaveLobbyTeam(testLobby.getAccessCode(), 1, 1);
        users.clear();
        assertEquals(testLobby.getTeam1(), users);
    }

    @Test
    void joinLobby_validInput() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);

        assertEquals(testLobby.getLobbyUsers(), users);

    }

    @Test
    void joinLobbyTeam_teamDoesntExist() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobbyTeam(123456, 3, 1));
    }

    @Test
    void leaveLobby_validInput() {
        List<User> users = new ArrayList<>();
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);


        testLobby = lobbyService.leaveLobby(testLobby.getAccessCode(), 1);

        assertEquals(testLobby.getLobbyUsers(), users);

    }

    @Test
    void leaveLobby_invalidLobby() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.leaveLobby(123123, 1));
    }

    @Test
    void leaveLobby_invalidUser() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.leaveLobby(123123, 1));
    }

    @Test
    void leaveLobbyTeam_invalidLobby() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.leaveLobbyTeam(123123, 1, 1));
    }

    @Test
    void leaveLobbyTeam_invalidUser() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.leaveLobbyTeam(123123, 1, 1));
    }

    @Test
    void leaveLobbyTeam_invalidTeam() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        assertThrows(ResponseStatusException.class, () -> lobbyService.leaveLobbyTeam(123123, 3, 1));
    }

    @Test
    void changeSettings_validInput() {
        Settings settings = new Settings();
        settings.setRounds(2);
        settings.setTopic(Topic.FOOD);
        settings.setRoundTime(2);

        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);

        lobbyService.changeSettings(testLobby.getAccessCode(), settings);

        assertEquals(testLobby.getSettings(), settings);

    }

    @Test
    void changeSettings_invalidLobby() {
        Settings settings = new Settings();
        settings.setRounds(2);
        settings.setTopic(Topic.FOOD);
        settings.setRoundTime(2);

        assertThrows(ResponseStatusException.class, () -> lobbyService.changeSettings(123123, settings));

    }

    @Test
    void getLobby_validInput() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);

        Lobby lobby = lobbyService.getLobby(testLobby.getAccessCode());

        assertEquals(lobby, testLobby);
    }


    @Test
    void joinTeam_invalidJoin() {
        //Create Lobby
        Lobby lobby = new Lobby();
        lobby.setAccessCode(123456);

        //Create Users
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        User user3 = new User();
        user3.setId(3L);
        User user4 = new User();
        user4.setId(4L);

        //Add Users to Lobby
        lobby.addUserToLobby(user1);
        lobby.addUserToLobby(user2);
        lobby.addUserToLobby(user3);
        lobby.addUserToLobby(user4);

        //Join Teams
        lobby.addUserToTeam1(user1);
        lobby.addUserToTeam1(user2);
        lobby.addUserToTeam2(user3);

        // adding a 3rd user to team 1 should throw an exception
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobbyTeam(lobby.getAccessCode(), 1, Math.toIntExact(user4.getId())));
    }

    @Test
    void deleteLobbyAndUsers_invalidInput() {
        assertThrows(ResponseStatusException.class, () -> lobbyService.deleteLobbyAndUsers(123456));
    }

    @Test
    void deleteLobbyAndUsers_validInput() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        lobbyService.deleteLobbyAndUsers(123456);
        Mockito.verify(lobbyRepository, Mockito.times(1)).delete(testLobby);
        Mockito.verify(userRepository, Mockito.times(1)).deleteAll(testLobby.getLobbyUsers());
        Mockito.verify(userRepository, Mockito.times(1)).flush();
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();
    }

    @Test
    void userIsInLobby_invalidInput_LobbyNotFound() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.userIsInLobby(1L, 123456));
    }

    @Test
    void userIsInLobby_invalidInput_userNotFound() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.userIsInLobby(1L, 123456));
    }

    @Test
    void userIsInLobby_validInput() {
        Mockito.when(lobbyRepository.findByAccessCode(123456)).thenReturn(testLobby);
        Mockito.when(userRepository.findById(1L)).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        assertTrue(lobbyService.userIsInLobby(1L, 123456));
    }

    @Test
    void getLobbies_validInput() {
        List<Lobby> lobbies = new ArrayList<>();
        lobbies.add(testLobby);
        Mockito.when(lobbyRepository.findAll()).thenReturn(lobbies);
        assertEquals(lobbies, lobbyService.getLobbies());
    }

    @Test
    void getLobby_invalidInput() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.getLobby(123456));
    }

    @Test
    void joinLobby_invalidInput() {
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(123456, 1L));
    }

    @Test
    void joinLobby_userNotExists(){
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(123456, 1L));
    }

    @Test
    void joinLobby_validInput_Leader(){
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        lobbyService.joinLobby(123456, 1L);
        assertEquals(testLobby.getLobbyLeader(), testUser);
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(testLobby);
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();
    }

    @Test
    void leaveLobbyTeam_userNotInTeam(){
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        testLobby.addUserToLobby(testUser);
        assertThrows(ResponseStatusException.class, () -> lobbyService.leaveLobbyTeam(123456, 1, 1));
    }

    @Test
    void allUsersInTeam_validInput(){
        User user1 = new User();
        User user2 = new User();
        testLobby.addUserToLobby(user1);
        testLobby.addUserToLobby(user2);
        testLobby.addUserToTeam1(user1);
        testLobby.addUserToTeam2(user2);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        assertTrue(lobbyService.allUsersInTeam(123456));
    }

    @Test
    void allUsersInTeam_invalidInput(){
        User user1 = new User();
        User user2 = new User();
        testLobby.addUserToLobby(user1);
        testLobby.addUserToLobby(user2);
        testLobby.addUserToTeam1(user1);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        assertFalse(lobbyService.allUsersInTeam(123456));
    }

    @Test
    void getRemainingPlayersNeeded_validInput_smallerThan4(){
        User user1 = new User();
        User user2 = new User();
        testLobby.addUserToLobby(user1);
        testLobby.addUserToLobby(user2);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        assertEquals(2, lobbyService.getRemainingPlayersNeeded(123456));
    }

    @Test
    void getRemainingPlayersNeeded_validInput_largerThan4(){
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();
        User user5 = new User();
        testLobby.addUserToLobby(user1);
        testLobby.addUserToLobby(user2);
        testLobby.addUserToLobby(user3);
        testLobby.addUserToLobby(user4);
        testLobby.addUserToLobby(user5);
        Mockito.when(lobbyRepository.findByAccessCode(Mockito.anyInt())).thenReturn(testLobby);
        assertEquals(0, lobbyService.getRemainingPlayersNeeded(123456));
    }


}
