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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ChatWebSocketHandlerTest {
    @Mock
    private WebSocketSession session;
    @Mock
    private GameService gameService;

    private ChatWebSocketHandler chatWebSocketHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        chatWebSocketHandler = new ChatWebSocketHandler(gameService);
    }

    @Test
    void testAfterConnectionEstablished() {
        int accessCode = 123456;
        when(session.getUri()).thenReturn(URI.create(getUriWithAccessCode(accessCode)));
        Game game = new Game();
        game.setAccessCode(accessCode);
        game.setSettings(new Settings());
        Mockito.when(gameService.getGame(accessCode)).thenReturn(game);
        chatWebSocketHandler.afterConnectionEstablished(session);

        HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = chatWebSocketHandler.getWebSocketSessions();
        assertTrue(webSocketSessions.containsKey(accessCode));
        assertTrue( webSocketSessions.get(accessCode).contains(session));
    }

    @Test
    void testAfterConnectionClosed_GameNotDeleted() {
        int accessCode = 123456;
        Mockito.when(session.getUri()).thenReturn(URI.create(getUriWithAccessCode(accessCode)));
        chatWebSocketHandler.afterConnectionEstablished(session);
        chatWebSocketHandler.afterConnectionClosed(session, CloseStatus.NORMAL);


        when(gameService.getGame(123456)).thenReturn(mock(ch.uzh.ifi.hase.soprafs23.entity.Game.class));

        assertTrue(chatWebSocketHandler.getWebSocketSessions().containsKey(123456));
    }

    private String getUriWithAccessCode(int accessCode) {
        return "/websocket/" + accessCode;
    }
}
