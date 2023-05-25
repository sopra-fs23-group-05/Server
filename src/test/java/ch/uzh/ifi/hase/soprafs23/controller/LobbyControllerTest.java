package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.SettingsPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LobbyController.class)
class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void createLobby_validInput_lobbyCreated() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());

        given(lobbyService.createLobby()).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies");

        mockMvc.perform(postRequest).andExpect(status().isCreated()).andExpect(jsonPath("$.accessCode", is(lobby.getAccessCode()))).andExpect(jsonPath("$.lobbyLeader", is(lobby.getLobbyLeader()))).andExpect(jsonPath("$.lobbyUsers", is(lobby.getLobbyUsers()))).andExpect(jsonPath("$.team1", is(lobby.getTeam1()))).andExpect(jsonPath("$.team2", is(lobby.getTeam2()))).andExpect(jsonPath("$.settings.rounds", is(7))).andExpect(jsonPath("$.settings.roundTime", is(120))).andExpect(jsonPath("$.settings.topic", is("ANIMALS")));
        Mockito.verify(lobbyService, times(1)).createLobby();
    }

    @Test
    void getLobby_validInput() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setSettings(new Settings());
        lobby.setAccessCode(123456);

        given(lobbyService.getLobby(123456)).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbies/123456");

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$.accessCode", is(lobby.getAccessCode()))).andExpect(jsonPath("$.lobbyLeader", is(lobby.getLobbyLeader()))).andExpect(jsonPath("$.lobbyUsers", is(lobby.getLobbyUsers()))).andExpect(jsonPath("$.team1", is(lobby.getTeam1()))).andExpect(jsonPath("$.team2", is(lobby.getTeam2()))).andExpect(jsonPath("$.settings.rounds", is(7))).andExpect(jsonPath("$.settings.roundTime", is(120))).andExpect(jsonPath("$.settings.topic", is("ANIMALS")));
        Mockito.verify(lobbyService, times(1)).getLobby(123456);
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

        mockMvc.perform(putRequest).andExpect(status().isOk());

        Mockito.verify(lobbyService, times(1)).joinLobbyTeam(123456, 1, 1);
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

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$[0].accessCode", is(lobby.getAccessCode()))).andExpect(jsonPath("$[0].lobbyLeader", is(lobby.getLobbyLeader()))).andExpect(jsonPath("$[0].lobbyUsers", is(lobby.getLobbyUsers()))).andExpect(jsonPath("$[0].team1", is(lobby.getTeam1()))).andExpect(jsonPath("$[0].team2", is(lobby.getTeam2()))).andExpect(jsonPath("$[0].settings.rounds", is(7))).andExpect(jsonPath("$[0].settings.roundTime", is(120))).andExpect(jsonPath("$[0].settings.topic", is("ANIMALS"))).andExpect(jsonPath("$[1].accessCode", is(lobby2.getAccessCode()))).andExpect(jsonPath("$[1].lobbyLeader", is(lobby2.getLobbyLeader()))).andExpect(jsonPath("$[1].lobbyUsers", is(lobby2.getLobbyUsers()))).andExpect(jsonPath("$[1].team1", is(lobby2.getTeam1()))).andExpect(jsonPath("$[1].team2", is(lobby2.getTeam2()))).andExpect(jsonPath("$[1].settings.rounds", is(7))).andExpect(jsonPath("$[1].settings.roundTime", is(120))).andExpect(jsonPath("$[1].settings.topic", is("ANIMALS")));
        Mockito.verify(lobbyService, Mockito.times(1)).getLobbies();
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

        mockMvc.perform(putRequest).andExpect(status().isOk()).andExpect(jsonPath("$.accessCode", is(lobby.getAccessCode()))).andExpect(jsonPath("$.lobbyLeader", is(lobby.getLobbyLeader()))).andExpect(jsonPath("$.lobbyUsers[0].username", is("testName")));

        Mockito.verify(lobbyService, Mockito.times(1)).joinLobby(123456, 1);
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

        mockMvc.perform(putRequest).andExpect(status().isOk());

        Mockito.verify(lobbyService, Mockito.times(1)).leaveLobby(123456, 1);

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

        mockMvc.perform(putRequest).andExpect(status().isOk());

        Mockito.verify(lobbyService, Mockito.times(1)).leaveLobbyTeam(123456, 1, 1);
    }

    @Test
    void getIsUserInLobby_validInput() throws Exception {

        given(lobbyService.userIsInLobby(1, 123456)).willReturn(true);
        MockHttpServletRequestBuilder getRequest = get("/lobbies/123456/users/1");

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$", is(true)));

        Mockito.verify(lobbyService, Mockito.times(1)).userIsInLobby(1, 123456);

    }

    @Test
    void testDeleteLobbyAndUsers() throws Exception {
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/123456");
        mockMvc.perform(deleteRequest).andExpect(status().isOk());

        Mockito.verify(lobbyService, Mockito.times(1)).deleteLobbyAndUsers(123456);
    }

    @Test
    void testChangeSettings() throws Exception {
        SettingsPutDTO settingsPutDTO = new SettingsPutDTO();
        int accessCode = 123456;
        mockMvc.perform(put("/lobbies/{accessCode}/settings", accessCode).contentType(MediaType.APPLICATION_JSON).content(asJsonString(settingsPutDTO))).andExpect(status().isOk());

        Mockito.verify(lobbyService, times(1)).changeSettings(eq(accessCode), any(Settings.class));
    }

    @Test
    void allUsersInTeam() throws Exception {
        given(lobbyService.allUsersInTeam(123456)).willReturn(true);
        MockHttpServletRequestBuilder getRequest = get("/lobbies/123456/users/teams");

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$", is(true)));

        Mockito.verify(lobbyService, Mockito.times(1)).allUsersInTeam(123456);
    }

    @Test
    void getRemainingPlayersNeeded() throws Exception {
        given(lobbyService.getRemainingPlayersNeeded(123456)).willReturn(2);
        MockHttpServletRequestBuilder getRequest = get("/lobbies/123456/remainingUsers");

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$", is(2)));
        Mockito.verify(lobbyService, Mockito.times(1)).getRemainingPlayersNeeded(123456);
    }

    @Test
    void deleteUserFromLobby() throws Exception {
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/123456/users/1");
        mockMvc.perform(deleteRequest).andExpect(status().isOk());

        Mockito.verify(lobbyService, Mockito.times(1)).leaveLobby(123456, 1);
    }

}