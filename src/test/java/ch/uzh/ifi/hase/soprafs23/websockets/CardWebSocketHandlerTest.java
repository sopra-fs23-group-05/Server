package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.custom.Card;
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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

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


    @Test
    void testAfterConnectionClosed_GameNotDeleted() throws IOException {
        int accessCode = 123456;
        Mockito.when(session.getUri()).thenReturn(URI.create(getUriWithAccessCode(accessCode)));
        Game game = new Game();
        game.setAccessCode(accessCode);
        game.setSettings(new Settings());
        Card card = new Card("Word", "Taboo1", "Taboo2", "Taboo3", "Taboo4", "Taboo5");
        Mockito.when(gameService.getDrawnCard(accessCode)).thenReturn(card);
        Mockito.when(gameService.getGame(accessCode)).thenReturn(game);
        cardWebSocketHandler.afterConnectionEstablished(session);
        cardWebSocketHandler.afterConnectionClosed(session, CloseStatus.NORMAL);

        when(gameService.getGame(123456)).thenReturn(mock(ch.uzh.ifi.hase.soprafs23.entity.Game.class));

        assertTrue(cardWebSocketHandler.getWebSocketSessions().containsKey(123456));
    }

    @Test
    void testCallBack() throws IOException {
        int accessCode = 123456;
        Mockito.when(session.getUri()).thenReturn(URI.create(getUriWithAccessCode(accessCode)));

        Card card = new Card("word", "taboo1", "taboo2", "taboo3", "taboo4", "taboo5");
        int turnPoints = 0;
        Mockito.when(gameService.getDrawnCard(accessCode)).thenReturn(card);
        HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = new HashMap<>();
        ArrayList<WebSocketSession> sessions = new ArrayList<>();
        sessions.add(session);
        webSocketSessions.put(accessCode, sessions);
        cardWebSocketHandler.afterConnectionEstablished(session);
        cardWebSocketHandler.callBack(accessCode, card, turnPoints);

        verify(session, times(2)).sendMessage(any(TextMessage.class));
    }

    private String getUriWithAccessCode(int accessCode) {
        return "/websocket/" + accessCode;
    }

}
