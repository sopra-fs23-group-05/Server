package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;
    @Test
    void createGame_validInput_gameCreated() throws Exception{
        // given
        Game game = new Game();

        given(gameService.createGame(123456)).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/123456");

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessCode", is(game.getAccessCode())))
                .andExpect(jsonPath("$.team1", is(game.getTeam1())))
                .andExpect(jsonPath("$.team2", is(game.getTeam2())))
                .andExpect(jsonPath("$.settings", is(game.getSettings())))
                .andExpect(jsonPath("$.turn", is(game.getTurn())))
                .andExpect(jsonPath("$.roundsPlayed", is(1)))
                .andExpect(jsonPath("$.turn", is(game.getTurn())));
    }
    @Test
    void getGame_validAccessCode_gameReturned() throws Exception {
        // given
        int accessCode = 123456;
        Game game = new Game();
        game.setAccessCode(accessCode);

        given(gameService.getGame(accessCode)).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/games/" + accessCode);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessCode", is(accessCode)));
    }
}
