package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamWebSocketHandler extends TextWebSocketHandler {

    private final HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = new HashMap<>();

    // Inject dependency to GameService here
    private final LobbyService lobbyService;
    private final UserService userService;

    public TeamWebSocketHandler(LobbyService lobbyService, UserService userService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        if(!webSocketSessions.containsKey(1)){
            webSocketSessions.put(1, new ArrayList<>());
        }
        webSocketSessions.get(1).add(session);
    }

    /**
     * @param message is of the form "\"accessCode\":123456,\"teamNr\":1,\"userId\":1,\"type\":\"addition\""
     *                the type can be either "addition" or "removal"
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // System.out.println("Sending message: " + message.getPayload());
        String messagePayload = message.getPayload();
        String[] messageParts = messagePayload.split(",");
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

        User aUser = userService.getUser(userId);
        messagePayload = messagePayload.substring(0, messagePayload.length() - 1);
        messagePayload += ",\"username\":\"" + aUser.getUsername() + "\"}";
        System.out.println("Sending message: " + messagePayload);
        TextMessage outMessage = new TextMessage(messagePayload);

        for (WebSocketSession webSocketSession : webSocketSessions.get(1)) {
            webSocketSession.sendMessage(outMessage);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessions.get(1).remove(session);
    }
}
