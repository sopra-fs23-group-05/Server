package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CardWebSocketHandlerTest {
    @Mock
    private GameService gameService;

    @Mock
    private WebSocketSession session;

    private CardWebSocketHandler cardWebSocketHandler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cardWebSocketHandler = new CardWebSocketHandler(gameService);
    }

    @Test
    void testAfterConnectionEstablished() throws IOException, URISyntaxException {

        int accessCode = 123456;
        Card card = new Card("Word", "Taboo1", "Taboo2", "Taboo3", "Taboo4", "Taboo5");
        Mockito.when(session.getUri()).thenReturn(new URI("/game/" + accessCode));
        Mockito.when(gameService.getDrawnCard(accessCode)).thenReturn(card);

        cardWebSocketHandler.afterConnectionEstablished(session);
        HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = cardWebSocketHandler.getWebSocketSessions();
        assertTrue(webSocketSessions.containsKey(accessCode));
        assertTrue(webSocketSessions.get(accessCode).contains(session));

        Mockito.verify(session, times(1)).sendMessage(any(TextMessage.class));
    }



}
