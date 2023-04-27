package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LobbyController.class)
class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    // Test POST for lobby
    @Test
    void createLobby_validInput_lobbyCreated() throws Exception{
        // given
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());

        given(lobbyService.createLobby()).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies");

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessCode", is(lobby.getAccessCode())))
                .andExpect(jsonPath("$.lobbyLeader", is(lobby.getLobbyLeader())))
                .andExpect(jsonPath("$.lobbyUsers", is(lobby.getLobbyUsers())))
                .andExpect(jsonPath("$.team1", is(lobby.getTeam1())))
                .andExpect(jsonPath("$.team2", is(lobby.getTeam2())))
                .andExpect(jsonPath("$.settings.rounds", is(7)))
                .andExpect(jsonPath("$.settings.roundTime", is(120)))
                .andExpect(jsonPath("$.settings.topic", is("MOVIES")));

    }
    @Test
    void getLobby_validInput() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());
        lobby.setAccessCode(123456);

        given(lobbyService.getLobby(123456)).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbies/123456");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessCode", is(lobby.getAccessCode())))
                .andExpect(jsonPath("$.lobbyLeader", is(lobby.getLobbyLeader())))
                .andExpect(jsonPath("$.lobbyUsers", is(lobby.getLobbyUsers())))
                .andExpect(jsonPath("$.team1", is(lobby.getTeam1())))
                .andExpect(jsonPath("$.team2", is(lobby.getTeam2())))
                .andExpect(jsonPath("$.settings.rounds", is(7)))
                .andExpect(jsonPath("$.settings.roundTime", is(120)))
                .andExpect(jsonPath("$.settings.topic", is("MOVIES")));
    }
    @Test
    void joinLobbyTeam_validInput() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());
        lobby.setAccessCode(123456);
        User user = new User();
        user.setUsername("testName");
        user.setId(1L);
        lobby.getLobbyUsers().add(user);

        given(lobbyService.joinLobbyTeam(123456, 1,1)).willReturn(lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456/teams/1/additions/users/1");

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());


    }

}