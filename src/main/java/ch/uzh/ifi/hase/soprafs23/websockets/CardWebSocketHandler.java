package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    // Inject dependency to GameService
    private final GameService gameService;

    // Register the handler as an observer of the GameService
    public CardWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
        gameService.initializeCardWebSocketHandler(this);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessions.add(session);
    }

    // Handle the client requesting a card
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // I expect a message of the form "{"accessCode":"123456", "action":"draw"}", "{"accessCode":"123456", "action":"skip"}" or "{"accessCode":"123456", "action":"buzz"}"
        String messageString = message.getPayload();

        // Extract the access code (it is at index 15 to 21)
        int accessCode = Integer.parseInt(messageString.substring(15, 21));

        // Execute buzz or draw
        Card outCard;
        try {
            if (messageString.contains("draw")) {
                outCard = gameService.drawCard(accessCode);
            }
            else if (messageString.contains("skip")) {
                outCard = gameService.skip(accessCode);
            }
            else if (messageString.contains("buzz")) {
                outCard = gameService.buzz(accessCode);
            }
            else {
                throw new Exception("Invalid action");
            }
        }
        catch (NullPointerException e) {
            outCard = new Card("NA", "NA", "NA", "NA", "NA", "NA");
        }

        // Convert it to a TextMessage object
        // I expect the outMessage to look like this: {"word":"Bic Mac","taboo1":"McDonalds","taboo2":"hamburger","taboo3":"pattie","taboo4":"salad","taboo5":"null"}
        TextMessage outMessage = new TextMessage(outCard.toString());

        for (WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(outMessage);
        }
    }

    /* Send new card to all clients after a correct guess.
     * I could have a method that sends stuff to clients without first receiving a message to trigger it.
     * I would inject this CardWebSocketHandler into the gameService and call this method from there. */

    /* Make a method that observes the Turn and realizes, when a new card is drawn. */
    public void callBack(Card outCard) {
        // Convert the card to a TextMessage object
        // I expect the outMessage to look like this: {"word":"Bic Mac","taboo1":"McDonalds","taboo2":"hamburger","taboo3":"pattie","taboo4":"salad","taboo5":"null"}
        TextMessage outMessage = new TextMessage(outCard.toString());
        try {
            for (WebSocketSession webSocketSession : webSocketSessions) {
                webSocketSession.sendMessage(outMessage);
            }
        }
        catch (IOException e) {
            System.out.println("IOException in CardWebSocketHandler: Sending TextMessage objects failed.");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessions.remove(session);
    }
}
