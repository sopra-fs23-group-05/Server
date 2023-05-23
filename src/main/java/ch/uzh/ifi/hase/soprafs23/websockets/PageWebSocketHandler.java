package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class PageWebSocketHandler extends TextWebSocketHandler {

    /** HashMap that stores a list of sessions for each access code. */
    private final HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = new HashMap<>();

    // Inject dependency to GameService here
    private final GameService gameService;

    public PageWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        int accessCode = getAccessCode(session);
        // If the access code is not in the HashMap, add it
        if (!webSocketSessions.containsKey(accessCode)) {
            webSocketSessions.put(accessCode, new ArrayList<>());
        }
        webSocketSessions.get(accessCode).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        int accessCode = getAccessCode(session);
        System.out.println("Sending message: " + message.getPayload());

        for (WebSocketSession webSocketSession : webSocketSessions.get(accessCode)) {
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        int accessCode = getAccessCode(session);
        webSocketSessions.get(accessCode).remove(session);

        // If the game was deleted, delete the mapping.
        try{
            gameService.getGame(accessCode);
        }catch (ResponseStatusException e){
            webSocketSessions.remove(accessCode);
        }
    }

    /** Extracts the access code from a WebSocketSession object. */
    private static int getAccessCode(WebSocketSession session) {
        return Integer.parseInt(session.getUri().toString().substring(session.getUri().toString().lastIndexOf('/') + 1));
    }
    public HashMap<Integer, ArrayList<WebSocketSession>> getWebSocketSessions() {
        return webSocketSessions;
    }
}
