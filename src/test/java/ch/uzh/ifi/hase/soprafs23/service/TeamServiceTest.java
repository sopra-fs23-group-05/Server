package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    private Team testTeam;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testTeam = new Team();
        List<Player> players = new ArrayList<>();
        testTeam.setPlayers(players);
        testTeam.setaRole(Role.GUESSINGTEAM);
        testTeam.setTeamId(1);

        Mockito.when(teamRepository.save(Mockito.any())).thenReturn(testTeam);
    }

    @Test
    public void getTeam_validInput() {
        Mockito.when(teamRepository.findById(Mockito.anyInt())).thenReturn(testTeam);
        assertEquals(teamService.getTeam(1),testTeam);
    }
    @Test
    public void createTeam_validInput() {
        List<User> players = new ArrayList<>();
        Team team = teamService.createTeam(players,Role.GUESSINGTEAM);

        Mockito.verify(teamRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(team.getPlayers(),testTeam.getPlayers());
        assertEquals(team.getaRole(),testTeam.getaRole());
        assertEquals(team.getTeamId(),testTeam.getTeamId());
        assertEquals(team.getIdxClueGiver(),testTeam.getIdxClueGiver());
        assertEquals(team.getPoints(),testTeam.getPoints());

    }
}