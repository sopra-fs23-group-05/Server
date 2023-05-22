package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TimerWebSocketHandlerTest {
    @Mock
    private WebSocketSession session;

    @Mock
    private GameService gameService;

    private TimerWebSocketHandler timerWebSocketHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        timerWebSocketHandler = new TimerWebSocketHandler(gameService);
    }

    @Test
    void afterConnectionEstablished_shouldAddSessionToWebSocketSessions() {
        int accessCode = 123456;
        when(session.getUri()).thenReturn(URI.create(getUriWithAccessCode(accessCode)));
        Game game = new Game();
        game.setAccessCode(accessCode);
        game.setSettings(new Settings());
        Mockito.when(gameService.getGame(accessCode)).thenReturn(game);
        timerWebSocketHandler.afterConnectionEstablished(session);

        HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = timerWebSocketHandler.getWebSocketSessions();
        assertTrue(webSocketSessions.containsKey(accessCode));
        assertTrue( webSocketSessions.get(accessCode).contains(session));
    }


    private String getUriWithAccessCode(int accessCode) {
        return "/websocket/" + accessCode;
    }
}