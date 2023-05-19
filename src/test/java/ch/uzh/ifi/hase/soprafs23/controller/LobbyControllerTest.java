package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void createLobby_validInput_lobbyCreated() throws Exception {
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
                .andExpect(jsonPath("$.settings.topic", is("ANIMALS")));

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
                .andExpect(jsonPath("$.settings.topic", is("ANIMALS")));
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

        given(lobbyService.joinLobbyTeam(123456, 1, 1)).willReturn(lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456/teams/1/additions/users/1");

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());


    }

    @Test
    void getLobbies_validInput() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());
        lobby.setAccessCode(123456);

        Lobby lobby2 = new Lobby();
        lobby2.setSettings(new Settings());
        lobby2.setAccessCode(123457);

        List<Lobby> lobbies = List.of(lobby, lobby2);

        given(lobbyService.getLobbies()).willReturn(lobbies);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbies");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accessCode", is(lobby.getAccessCode())))
                .andExpect(jsonPath("$[0].lobbyLeader", is(lobby.getLobbyLeader())))
                .andExpect(jsonPath("$[0].lobbyUsers", is(lobby.getLobbyUsers())))
                .andExpect(jsonPath("$[0].team1", is(lobby.getTeam1())))
                .andExpect(jsonPath("$[0].team2", is(lobby.getTeam2())))
                .andExpect(jsonPath("$[0].settings.rounds", is(7)))
                .andExpect(jsonPath("$[0].settings.roundTime", is(120)))
                .andExpect(jsonPath("$[0].settings.topic", is("ANIMALS")))
                .andExpect(jsonPath("$[1].accessCode", is(lobby2.getAccessCode())))
                .andExpect(jsonPath("$[1].lobbyLeader", is(lobby2.getLobbyLeader())))
                .andExpect(jsonPath("$[1].lobbyUsers", is(lobby2.getLobbyUsers())))
                .andExpect(jsonPath("$[1].team1", is(lobby2.getTeam1())))
                .andExpect(jsonPath("$[1].team2", is(lobby2.getTeam2())))
                .andExpect(jsonPath("$[1].settings.rounds", is(7)))
                .andExpect(jsonPath("$[1].settings.roundTime", is(120)))
                .andExpect(jsonPath("$[1].settings.topic", is("ANIMALS")));

    }

    @Test
    void joinLobby_validInput() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());
        lobby.setAccessCode(123456);
        User user = new User();
        user.setUsername("testName");
        user.setId(1L);
        lobby.getLobbyUsers().add(user);

        given(lobbyService.joinLobby(123456, 1)).willReturn(lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456/additions/users/1");

        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessCode", is(lobby.getAccessCode())))
                .andExpect(jsonPath("$.lobbyLeader", is(lobby.getLobbyLeader())))
                .andExpect(jsonPath("$.lobbyUsers[0].username", is("testName")));
    }

    @Test
    void leaveLobby_validInput() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());
        lobby.setAccessCode(123456);
        User user = new User();
        user.setUsername("testName");
        user.setId(1L);
        lobby.getLobbyUsers().add(user);

        given(lobbyService.leaveLobby(123456, 1)).willReturn(lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456/removals/users/1");

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());

    }

    @Test
    void leaveLobbyTeam_validInput() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());
        lobby.setAccessCode(123456);
        User user = new User();
        user.setUsername("testName");
        user.setId(1L);
        lobby.getLobbyUsers().add(user);

        given(lobbyService.leaveLobbyTeam(123456, 1, 1)).willReturn(lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456/teams/1/removals/users/1");

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }


    @Test
    void getIsUserInLobby_validInput() throws Exception {
        given(lobbyService.userIsInLobby(1, 123456)).willReturn(true);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/123456/users/1");

        mockMvc.perform(getRequest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(true)));

    }

    @Test
    void deleteLobbyAndUsers_validInput() throws Exception {
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/123456");

        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk());
    }

}