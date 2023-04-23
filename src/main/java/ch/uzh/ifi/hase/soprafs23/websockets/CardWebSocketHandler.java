package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public class CardWebSocketHandler extends TextWebSocketHandler{

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    // Inject dependency to GameService
    private final GameService gameService;

    public CardWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessions.add(session);
    }

    // Handle the client requesting a card
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // I expect a message of the form "{"action":"draw", "accessCode":123456}" or "{"action":"buzz", "accessCode":123456}"
        String messageString = message.getPayload();

        // Extract the access code
        int accessCode = Integer.parseInt(messageString.substring(messageString.indexOf("accessCode")+12, messageString.indexOf("}")));

        // Execute buzz or draw
        Card outCard;
        if(messageString.contains("{draw}")){
            outCard = gameService.drawCard(accessCode);
        } else if(messageString.contains("{buzz}")){
            outCard = gameService.buzz(accessCode);
        } else {
            throw new Exception("Invalid action");
        }

        // Convert it to a TextMessage object
        // I expect the outMessage to look like this: {"word":"Bic Mac","taboo1":"McDonalds","taboo2":"hamburger","taboo3":"pattie","taboo4":"salad","taboo5":"null"}
        TextMessage outMessage = new TextMessage(outCard.toString());

        for(WebSocketSession webSocketSession : webSocketSessions){
            webSocketSession.sendMessage(outMessage);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessions.remove(session);
    }
}