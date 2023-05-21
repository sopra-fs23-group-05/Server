package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = new HashMap<>();

    // Inject dependency to GameService here
    private final GameService gameService;

    public ChatWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
        gameService.initializeChatWebSocketHandler(this);
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
        Message msg = convertTextMessageToMessage(message);
        // Call game service to guess the word
        if (msg.getType() == MessageType.GUESS) {
            gameService.guessWord(msg);
        }
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

    private Message convertTextMessageToMessage(TextMessage message) {
        String textMessageString = message.getPayload();
        textMessageString = textMessageString.substring(1, textMessageString.length() - 1);   // Remove curly braces
        String[] messageParts = textMessageString.split(",");

        // Extract access code, userId, content
        int accessCode = Integer.parseInt(messageParts[0].substring(messageParts[0].indexOf(':') + 2, messageParts[0].length() - 1));
        long userId = Long.parseLong(messageParts[1].substring(messageParts[1].indexOf(':') + 1));
        String content = messageParts[2].substring(messageParts[2].indexOf(':') + 2, messageParts[2].length() - 1);

        // Extract type and convert to MessageType
        String typeString = messageParts[3].substring(messageParts[3].indexOf(':') + 2, messageParts[3].length() - 1);
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

    /** Extracts the access code from a WebSocketSession object. */
    private static int getAccessCode(WebSocketSession session) {
        return Integer.parseInt(session.getUri().toString().substring(session.getUri().toString().lastIndexOf('/') + 1));
    }

    /** Inform the clients that a new card was drawn. */
    public void sendInformationCallBack(int accessCode){
        String messageString = "{\"accessCode\":\"" + accessCode + "\", \"userId\":" + -1 + ", \"message\":\"A new card was drawn.\", \"type\":\"information\"}";
        TextMessage message = new TextMessage(messageString);
        try {
            for (WebSocketSession webSocketSession : webSocketSessions.get(accessCode)) {
                webSocketSession.sendMessage(message);
            }
        }
        catch (IOException e) {
            System.out.println("IOException in CardWebSocketHandler: Sending TextMessage objects failed.");
        }
    }
}