package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.rest.dto.CardDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
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

@WebMvcTest(GameController.class)
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void createGame_validInput_gameCreated() throws Exception {
        Game game = new Game();

        given(gameService.createGame(123456)).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/123456");

        mockMvc.perform(postRequest).andExpect(status().isCreated()).andExpect(jsonPath("$.accessCode", is(game.getAccessCode()))).andExpect(jsonPath("$.team1", is(game.getTeam1()))).andExpect(jsonPath("$.team2", is(game.getTeam2()))).andExpect(jsonPath("$.settings", is(game.getSettings()))).andExpect(jsonPath("$.turn", is(game.getTurn()))).andExpect(jsonPath("$.roundsPlayed", is(0))).andExpect(jsonPath("$.turn", is(game.getTurn())));
        Mockito.verify(gameService).createGame(123456);
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

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$.accessCode", is(accessCode)));
        Mockito.verify(gameService).getGame(accessCode);
    }

    @Test
    void getGames_validInput_gamesReturned() throws Exception {
        Game game1 = new Game();
        game1.setAccessCode(123456);
        Game game2 = new Game();
        game2.setAccessCode(123457);
        List<Game> games = List.of(game1, game2);

        given(gameService.getAllGames()).willReturn(games);

        MockHttpServletRequestBuilder getRequest = get("/games");

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$[0].accessCode", is(game1.getAccessCode()))).andExpect(jsonPath("$[1].accessCode", is(game2.getAccessCode())));
        Mockito.verify(gameService).getAllGames();
    }

    @Test
    void nextTurn_validInput() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/games/123456/turns");

        mockMvc.perform(putRequest).andExpect(status().isOk());
        Mockito.verify(gameService).nextTurn(123456);
    }

    @Test
    void finishGame_validInput() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/games/123456/finishes");

        mockMvc.perform(putRequest).andExpect(status().isOk());
        Mockito.verify(gameService).finishGame(123456);
    }

    @Test
    void getPlayerRole_validInput() throws Exception {

        Mockito.when(gameService.getPlayerRole(123456, "player1")).thenReturn(PlayerRole.GUESSER);

        MockHttpServletRequestBuilder getRequest = get("/games/123456/users/player1");

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$", is(PlayerRole.GUESSER.toString())));
        Mockito.verify(gameService).getPlayerRole(123456, "player1");
    }

    @Test
    void getMVPPlayer_validInput() throws Exception {
        Player player = new Player();
        player.setName("player1");

        Mockito.when(gameService.getMPVPlayer(123456)).thenReturn(player);

        MockHttpServletRequestBuilder getRequest = get("/games/123456/players/MVP");

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$.name", is("player1")));
        Mockito.verify(gameService).getMPVPlayer(123456);
    }

    @Test
    void deleteGameTeamsAndPlayers() throws Exception {
        MockHttpServletRequestBuilder deleteRequest = delete("/games/123456");

        mockMvc.perform(deleteRequest).andExpect(status().isOk());
        Mockito.verify(gameService).deleteGameTeamsUsersAndLobby(123456);
    }

    @Test
    void deletePlayerFromGame() throws Exception {
        MockHttpServletRequestBuilder deleteRequest = delete("/games/123456/player1");

        mockMvc.perform(deleteRequest).andExpect(status().isOk());
        Mockito.verify(gameService).deletePlayerFromGame(123456, "player1");
    }

    @Test
    void shuffleCards() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/games/123456/cards");

        mockMvc.perform(putRequest).andExpect(status().isOk());
        Mockito.verify(gameService).shuffleCards(123456);
    }

    @Test
    void testCreateCard() throws Exception {
        CardDTO cardDTO = new CardDTO();
        int accessCode = 123;

        mockMvc.perform(post("/games/{accessCode}/cards", accessCode).contentType(MediaType.APPLICATION_JSON).content(asJsonString(cardDTO))).andExpect(status().isCreated());
        Mockito.verify(gameService, times(1)).createCard(eq(accessCode), any(Card.class));
    }


}
