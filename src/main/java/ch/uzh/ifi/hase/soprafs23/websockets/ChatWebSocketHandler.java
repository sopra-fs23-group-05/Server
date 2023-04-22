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
        for(WebSocketSession webSocketSession : webSocketSessions){
            System.out.println("Sending message: " + message.getPayload());
            Message msg = convertTextMessageToMessage(message);
            // Call game service to guess the word
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessions.remove(session);
    }

    private Message convertTextMessageToMessage(TextMessage message){
        String textMessageString = message.getPayload();

        // Convert user string to user
        String userString = textMessageString.substring(textMessageString.indexOf('{')+1, textMessageString.indexOf('}')+1);
        String userIdString = userString.substring(userString.indexOf("id")+4, userString.indexOf("username")-2);
        long userId = -1;
        if(!userIdString.equals("null")){
            userId = Long.parseLong(userIdString);
        }

        // Extract content and type of message
        String messageAndTypeString = textMessageString.substring(textMessageString.indexOf('}') + 2, textMessageString.length() - 1);
        String contentString = messageAndTypeString.substring(0, messageAndTypeString.indexOf(','));
        String typeString = messageAndTypeString.substring(messageAndTypeString.indexOf(',') + 1);

        // Extract content from content string
        contentString = contentString.substring(contentString.indexOf(':') + 2, contentString.length() - 1);

        // Convert type string to MessageType
        typeString = typeString.substring(typeString.indexOf(':') + 2, typeString.length() - 1);
        MessageType msgType;
        if(typeString.equals("description")){
            msgType = MessageType.DESCRIPTION;
        }else{
            msgType = MessageType.GUESS;
        }

        // Create new Message object
        return new Message(userId, contentString, msgType);
    }
}