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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        testTeam = new Team();
        List<Player> players = new ArrayList<>();
        testTeam.setPlayers(players);
        testTeam.setaRole(Role.GUESSINGTEAM);
        testTeam.setTeamId(1);


        testUser = new User();
        testUser.setId(1L);
        testUser.setLeader(true);
        testUser.setUsername("testUsername");

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
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
        user.setUsername("testUsername");
        user.setLeader(true);
        user.setId(1L);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        Player player = teamService.convertUserToPlayer(1);

        assertEquals(player.getName(), user.getUsername());
        assertEquals(player.getPersonalScore(), 0);
        assertEquals(player.isLeader(), user.isLeader());
    }

    @Test
    void isInTeam_validInput() {
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(testTeam);
        Player player = new Player();
        player.setName("a");
        List<Player> players = new ArrayList<>();
        players.add(player);

        testTeam.setPlayers(players);

        assertTrue(teamService.isInTeam(testTeam.getTeamId(), "a"));
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
        assertEquals(testTeam.getaRole(), Role.BUZZINGTEAM);
        assertEquals(testTeam1.getaRole(), Role.GUESSINGTEAM);
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
        assertEquals(testTeam2.getaRole(), Role.GUESSINGTEAM);
        assertEquals(testTeam1.getaRole(), Role.BUZZINGTEAM);
        assertEquals(0, testTeam2.getPoints());
        assertEquals(5, testTeam1.getPoints());
    }


}