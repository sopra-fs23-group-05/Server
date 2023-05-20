package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Source: https://stackoverflow.com/questions/37899268/test-websocket-in-spring-boot-application

@SpringBootTest
@WebMvcTest(WebSocketConfig.class)
class WebSocketConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    GameService gameService;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {
    }

    @Test
    void testWebSocketConnection() throws Exception {
        // Send a GET request to the TIMER_ENDPOINT
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/timers/123456"));

        // Assert that the request was successful (HTTP 200 OK)
        resultActions.andExpect(status().isOk());
    }
}