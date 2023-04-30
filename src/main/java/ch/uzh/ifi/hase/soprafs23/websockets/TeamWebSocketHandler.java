package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class TeamWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    // Inject dependency to GameService here
    private final LobbyService lobbyService;

    public TeamWebSocketHandler(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessions.add(session);
    }

    /**
     * @param message is of the form "\"accessCode\":123456,\"teamNr\":1,\"userId\":1,\"type\":\"addition\""
     *                the type can be either "addition" or "removal"
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // System.out.println("Sending message: " + message.getPayload());
        String[] messageParts = message.getPayload().split(",");
        int accessCode = Integer.parseInt(messageParts[0].substring(messageParts[0].indexOf(':') + 1));
        int teamNr = Integer.parseInt(messageParts[1].substring(messageParts[1].indexOf(':') + 1));
        int userId = Integer.parseInt(messageParts[2].substring(messageParts[2].indexOf(':') + 1));
        String type = messageParts[3].contains("addition") ? "addition" : "removal";
        // System.out.println("accessCode: " + accessCode + ", teamNr: " + teamNr + ", userId: " + userId + ", type: " + type);

        if(type.equals("addition")) {
            lobbyService.joinLobbyTeam(accessCode, teamNr, userId);
        } else {
            lobbyService.leaveLobbyTeam(accessCode, teamNr, userId);
        }

        for (WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessions.remove(session);
    }
}
