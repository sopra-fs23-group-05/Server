package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
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

class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;
    @Mock

    private Team testTeam;

    @Mock
    private UserRepository userRepository;

    private User testUser;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        List<Player> players = new ArrayList<>();
        testTeam = new Team(players, Role.GUESSINGTEAM);
        testTeam.setTeamId(1);

        testUser = new User();
        testUser.setId(1L);
        testUser.setLeader(true);
        testUser.setUsername("testName");


        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        Mockito.when(teamRepository.save(Mockito.any())).thenReturn(testTeam);
    }

    @Test
    void getTeam_validInput() {
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(testTeam);
        assertEquals(teamService.getTeam(1), testTeam);
    }

    @Test
    void convertUserToPlayer() {
        User user = new User();
        user.setUsername("testName");
        user.setLeader(true);
        user.setId(1L);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        Player player = teamService.convertUserToPlayer(1);

        assertEquals(player.getName(), user.getUsername());
        assertEquals(player.getPersonalScore(), 0);
        assertEquals(player.isLeader(), user.isLeader());
    }


    @Test
    void isClueGiver_validInput() {
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(testTeam);
        Player player = new Player();
        player.setName("a");
        List<Player> players = new ArrayList<>();
        players.add(player);

        testTeam.setPlayers(players);

        assertTrue(teamService.isClueGiver(testTeam.getTeamId(), "a"));
    }

    @Test
    void changeTurn_validInputTeam1Guessing() {
        Team testTeam1 = new Team();
        testTeam1.setTeamId(2);
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {   // 2 players
            players.add(new Player());
        }
        testTeam1.setaRole(Role.BUZZINGTEAM);
        testTeam1.setPlayers(players);
        testTeam.setPlayers(players);
        Mockito.when(teamRepository.findById(2)).thenReturn(testTeam1);
        Mockito.when(teamRepository.findById(1)).thenReturn(testTeam);

        teamService.changeTurn(1, 2, 5);

        Mockito.verify(teamRepository, Mockito.times(2)).save(Mockito.any());
        assertEquals(Role.BUZZINGTEAM, testTeam.getaRole());
        assertEquals(Role.GUESSINGTEAM, testTeam1.getaRole());
        assertEquals(5, testTeam.getPoints());
        assertEquals(0, testTeam1.getPoints());

    }

    @Test
    void changeTurn_validInputTeam2Guessing() {
        Team testTeam1 = new Team();
        testTeam1.setTeamId(1);
        testTeam1.setaRole(Role.GUESSINGTEAM);
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {   // 2 players
            players.add(new Player());
        }
        testTeam1.setPlayers(players);

        Team testTeam2 = new Team();
        testTeam2.setTeamId(2);
        testTeam2.setaRole(Role.BUZZINGTEAM);
        testTeam2.setPlayers(players);
        Mockito.when(teamRepository.findById(1)).thenReturn(testTeam1);
        Mockito.when(teamRepository.findById(2)).thenReturn(testTeam2);

        teamService.changeTurn(2, 1, 5);
        Mockito.verify(teamRepository, Mockito.times(2)).save(Mockito.any());
        assertEquals(Role.GUESSINGTEAM, testTeam2.getaRole());
        assertEquals(Role.BUZZINGTEAM, testTeam1.getaRole());
        assertEquals(0, testTeam2.getPoints());
        assertEquals(5, testTeam1.getPoints());
    }


    @Test
    void increasePlayerScore_validInput() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Felix", true));
        players.add(new Player("Tom", false));

        testTeam.setPlayers(players);   // Overwrite the test team with my own team
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(testTeam);

        teamService.increasePlayerScore(testTeam.getTeamId(), "Tom"); // Call UUT
        assertEquals(1, testTeam.getPlayers().get(1).getPersonalScore());
    }

    @Test
    void increasePlayerScore_invalidInput() {
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(null);
        int teamId = testTeam.getTeamId();
        // Call UUT and make assertion
        assertThrows(ResponseStatusException.class, () -> teamService.increasePlayerScore(teamId, "Tom"));
    }

    @Test
    void getTeamRole_validInput() {
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(testTeam);
        assertEquals(testTeam.getaRole(), teamService.getTeamRole(testTeam.getTeamId()));
    }

    @Test
    void getTeamRole_invalidInput() {
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(null);
        int teamId = testTeam.getTeamId();
        // Call UUT and make assertion
        assertThrows(ResponseStatusException.class, () -> teamService.getTeamRole(teamId));
    }

    @Test
    void createTeam_teamSizeTooSmall() {
        Mockito.when(userRepository.findById(1)).thenReturn(testUser);
        List<User> users = new ArrayList<>();
        users.add(testUser);
        assertThrows(ResponseStatusException.class, () -> teamService.createTeam(users, Role.GUESSINGTEAM));
    }

    @Test
    void changeTurn_team1NotExists() {
        Mockito.when(teamRepository.findById(1)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> teamService.changeTurn(1, 2, 5));
    }

    @Test
    void changeTurn_team2NotExists() {
        Mockito.when(teamRepository.findById(1)).thenReturn(testTeam);
        Mockito.when(teamRepository.findById(2)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> teamService.changeTurn(1, 2, 5));
    }

    @Test
    void isInTeam_userNotFound() {
        Mockito.when(userRepository.findById(1)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> teamService.isInTeam(1, "testName"));
    }

    @Test
    void isInTeam_userNotInTeam() {
        Mockito.when(teamRepository.findById(1)).thenReturn(testTeam);
        Mockito.when(userRepository.findByUsername("testName")).thenReturn(testUser);

        assertEquals(false, teamService.isInTeam(1, "testName"));
    }

    @Test
    void inInTeam_userInTeam() {
        Mockito.when(teamRepository.findById(1)).thenReturn(testTeam);
        Mockito.when(userRepository.findByUsername("testName")).thenReturn(testUser);
        List<Player> players = new ArrayList<>();
        players.add(new Player("testName", true));
        testTeam.setPlayers(players);

        assertEquals(true, teamService.isInTeam(1, "testName"));
    }

    @Test
    void isClueGiver_isNot() {
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(testTeam);
        Player player = new Player();
        player.setName("a");
        Player player2 = new Player();
        player2.setName("b");
        List<Player> players = new ArrayList<>();
        players.add(player);

        testTeam.setPlayers(players);

        assertFalse(teamService.isClueGiver(testTeam.getTeamId(), "b"));
    }
}