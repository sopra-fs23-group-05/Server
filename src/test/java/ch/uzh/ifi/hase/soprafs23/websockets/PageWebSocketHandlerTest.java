package ch.uzh.ifi.hase.soprafs23.websockets;


import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PageWebSocketHandlerTest {
    @Mock
    private GameService gameService;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private WebSocketSession session;

    private PageWebSocketHandler pageWebSocketHandler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        pageWebSocketHandler = new PageWebSocketHandler(lobbyService);
    }

    @Test
    void testAfterConnectionEstablished() throws IOException, URISyntaxException {
        int accessCode = 123456;
        Mockito.when(session.getUri()).thenReturn(new URI("/game/" + accessCode));

        pageWebSocketHandler.afterConnectionEstablished(session);
        HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = pageWebSocketHandler.getWebSocketSessions();
        assertTrue(webSocketSessions.containsKey(accessCode));
        assertTrue(webSocketSessions.get(accessCode).contains(session));

        Mockito.verify(session, times(0)).sendMessage(any(TextMessage.class));
    }

    @Test
    void testHandleTextMessage() throws Exception {
        TextMessage message = new TextMessage("testMessage");
        int accessCode = 123456;
        Mockito.when(session.getUri()).thenReturn(URI.create(getUriWithAccessCode(accessCode)));
        pageWebSocketHandler.afterConnectionEstablished(session);
        pageWebSocketHandler.handleTextMessage(session, message);

        Mockito.verify(session, times(1)).sendMessage(any(TextMessage.class));
    }
    @Test
    void testAfterConnectionClosed_GameNotDeleted() {
        int accessCode = 123456;
        Mockito.when(session.getUri()).thenReturn(URI.create(getUriWithAccessCode(accessCode)));
        pageWebSocketHandler.afterConnectionEstablished(session);
        pageWebSocketHandler.afterConnectionClosed(session, CloseStatus.NORMAL);


        when(gameService.getGame(123456)).thenReturn(mock(ch.uzh.ifi.hase.soprafs23.entity.Game.class));

        assertTrue(pageWebSocketHandler.getWebSocketSessions().containsKey(123456));
    }

    private String getUriWithAccessCode(int accessCode) {
        return "/websocket/" + accessCode;
    }
}
