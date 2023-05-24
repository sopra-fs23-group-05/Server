package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamWebSocketHandler extends TextWebSocketHandler {

    /**
     * HashMap that stores a list of sessions for each access code.
     */
    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    // Inject dependency to GameService here
    private final LobbyService lobbyService;
    private final UserService userService;

    public TeamWebSocketHandler(LobbyService lobbyService, UserService userService) {
        this.lobbyService = lobbyService;
        lobbyService.initializeTeamWebSocketHandler(this);
        this.userService = userService;
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
        String messagePayload = message.getPayload();
        String[] messageParts = messagePayload.split(",");
        int accessCode = Integer.parseInt(messageParts[0].substring(messageParts[0].indexOf(':') + 1));
        int teamNr = Integer.parseInt(messageParts[1].substring(messageParts[1].indexOf(':') + 1));
        int userId = Integer.parseInt(messageParts[2].substring(messageParts[2].indexOf(':') + 1));
        String type = "";
        if (messageParts[3].contains("addition")) {
            type = "addition";
        }
        else if (messageParts[3].contains("removal")) {
            type = "removal";
        }
        else if (messageParts[3].contains("UserLeftLobby")) {
            type = "UserLeftLobby";
        }
        else if (messageParts[3].contains("LeaderLeftLobby")) {
            type = "LeaderLeftLobby";
        }
        //System.out.println("accessCode: " + accessCode + ", teamNr: " + teamNr + ", userId: " + userId + ", type: " + type);

        User aUser = userService.getUser(userId);
        TextMessage outMessage;

        try {
            switch (type) {
                case "addition" -> lobbyService.joinLobbyTeam(accessCode, teamNr, userId);
                case "UserLeftLobby" -> lobbyService.leaveLobby(accessCode, userId);
                case "LeaderLeftLobby" -> lobbyService.deleteLobbyAndUsers(accessCode);
                case "removal" -> lobbyService.leaveLobbyTeam(accessCode, teamNr, userId);
            }
            messagePayload = messagePayload.substring(0, messagePayload.length() - 1);
            messagePayload += ",\"username\":\"" + aUser.getUsername() + "\"}";
            System.out.println("Sending message: " + messagePayload);
            outMessage = new TextMessage(messagePayload);

        }
        catch (Exception e) { //exception is thrown if the join would not be fair
            messagePayload = "{\"accessCode\":" + accessCode + ",\"teamNr\":" + teamNr + ",\"userId\":" + userId + ",\"type\":\"" + "error" + "\",\"username\":\"" + aUser.getUsername() + "\"}";
            System.out.println("Sending message: " + messagePayload);
            outMessage = new TextMessage(messagePayload);
        }

        for (WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(outMessage);
        }
    }

    public void callBack(int accessCode, int teamNr, int userId) {
        User aUser = userService.getUser(userId);
        String messagePayload = "{\"accessCode\":" + accessCode + ",\"teamNr\":" + teamNr + ",\"userId\":" + userId + ",\"type\":\"removal\",\"username\":\"" + aUser.getUsername() + "\"}";
        System.out.println("Sending message: " + messagePayload);
        TextMessage outMessage = new TextMessage(messagePayload);

        try {
            for (WebSocketSession webSocketSession : webSocketSessions) {
                webSocketSession.sendMessage(outMessage);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessions.remove(session);
    }

    public List<WebSocketSession> getWebSocketSessions() {
        return webSocketSessions;
    }
}
