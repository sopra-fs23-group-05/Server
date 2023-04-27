package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    // Inject dependency to GameService here
    private final GameService gameService;

    public ChatWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Sending message: " + message.getPayload());
        Message msg = convertTextMessageToMessage(message);
        // Call game service to guess the word
        if (msg.getType() == MessageType.GUESS) {
            gameService.guessWord(msg);
        }
        for (WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessions.remove(session);
    }

    private Message convertTextMessageToMessage(TextMessage message) {
        String textMessageString = message.getPayload();
        textMessageString = textMessageString.substring(1, textMessageString.length() - 1);   // Remove curly braces
        String[] messageParts = textMessageString.split(",");

        for(int i = 0; i < messageParts.length; i++) {
            System.out.println(messageParts[i]);
        }

        // Extract access code, userId, content
        int accessCode = Integer.parseInt(messageParts[0].substring(messageParts[0].indexOf(':') + 2, messageParts[0].length() - 1));
        long userId = Long.parseLong(messageParts[1].substring(messageParts[1].indexOf(':') + 1));
        String content = messageParts[2].substring(messageParts[2].indexOf(':') + 2, messageParts[2].length() - 1);

        // Extract type and convert to MessageType
        String typeString = messageParts[3].substring(messageParts[3].indexOf(':') + 2, messageParts[3].length());
        System.out.println(typeString);
        MessageType msgType;
        if (typeString.equals("description")) {
            msgType = MessageType.DESCRIPTION;
        }
        else {
            msgType = MessageType.GUESS;
        }

        // Create new Message object
        return new Message(accessCode, userId, content, msgType);
    }
}