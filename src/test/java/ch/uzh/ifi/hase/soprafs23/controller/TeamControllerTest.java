package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ch.uzh.ifi.hase.soprafs23.entity.Team;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
public class TeamControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @Test
    void getTeam_validInput() throws Exception {
        Team team = new Team();
        team.setTeamId(1);
        team.setaRole(Role.BUZZINGTEAM);
        List<Player> players = new ArrayList<>();
        Player player = new Player();
        player.setName("test");
        players.add(player);
        team.setPlayers(players);

        given(teamService.getTeam(1)).willReturn(team);

        MockHttpServletRequestBuilder getRequest = get("/teams/1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamId", is(team.getTeamId())))
                .andExpect(jsonPath("$.aRole", is("BUZZINGTEAM")))
                .andExpect(jsonPath("$.players[0].name", is(player.getName())));

    }
}
