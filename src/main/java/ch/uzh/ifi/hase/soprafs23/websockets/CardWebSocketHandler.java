package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CardWebSocketHandler extends TextWebSocketHandler {

    private final HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = new HashMap<>();

    // Inject dependency to GameService
    private final GameService gameService;

    // Register the handler as an observer of the GameService
    public CardWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
        gameService.initializeCardWebSocketHandler(this);
    }

    /**
     * Extracts the access code from a WebSocketSession object.
     */
    private static int getAccessCode(WebSocketSession session) {
        return Integer.parseInt(session.getUri().toString().substring(session.getUri().toString().lastIndexOf('/') + 1));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        int accessCode = getAccessCode(session);
        if (!webSocketSessions.containsKey(accessCode)) {
            webSocketSessions.put(accessCode, new ArrayList<>());
        }
        webSocketSessions.get(accessCode).add(session);

        // Send the starting card to each connecting client
        Card outCard = gameService.getDrawnCard(accessCode);
        String outCardString = outCard.toString();
        String outCardStringWithTurnPoints = outCardString.substring(0, outCardString.length() - 1) + ", \"turnPoints\":\"" + 0 + '\"' + "}";
        session.sendMessage(new TextMessage(outCardStringWithTurnPoints));
    }

    /* Send new card to all clients after a correct guess.
     * I could have a method that sends stuff to clients without first receiving a message to trigger it.
     * I would inject this CardWebSocketHandler into the gameService and call this method from there. */

    // Handle the client requesting a card
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        int accessCode = getAccessCode(session);
        // I expect a message of the form "{"accessCode":"123456", "action":"draw"}", "{"accessCode":"123456", "action":"skip"}" or "{"accessCode":"123456", "action":"buzz"}"
        String messageString = message.getPayload();

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

        //add the turnPoints to the card to send it to the client
        Integer turnPoints = gameService.getTurnPoints(accessCode);
        String outCardString = outCard.toString();
        String outCardStringWithTurnPoints = outCardString.substring(0, outCardString.length() - 1) + ", \"turnPoints\":\"" + turnPoints + '\"' + "}";

        // Convert it to a TextMessage object
        // I expect the outMessage to look like this: {"word":"Bic Mac","taboo1":"McDonalds","taboo2":"hamburger","taboo3":"pattie","taboo4":"salad","taboo5":"null", "turnPoints":"0"
        TextMessage outMessage = new TextMessage(outCardStringWithTurnPoints);

        for (WebSocketSession webSocketSession : webSocketSessions.get(accessCode)) {
            webSocketSession.sendMessage(outMessage);
        }
    }

    /* Make a method that observes the Turn and realizes, when a new card is drawn. */
    public void callBack(int accessCode, Card outCard, int turnPoints) {
        // Convert the card to a TextMessage object
        // I expect the outMessage to look like this: {"word":"Bic Mac", "taboo1":"McDonalds", "taboo2":"hamburger", "taboo3":"pattie", "taboo4":"salad", "taboo5":"null", "turnPoints":"0"}
        String outCardString = outCard.toString();
        String outCardStringWithTurnPoints = outCardString.substring(0, outCardString.length() - 1) + ", \"turnPoints\":\"" + turnPoints + '\"' + "}";
        TextMessage outMessage = new TextMessage(outCardStringWithTurnPoints);

        try {   // Send the message to all clients
            for (WebSocketSession webSocketSession : webSocketSessions.get(accessCode)) {
                webSocketSession.sendMessage(outMessage);
            }
        }
        catch (IOException e) {
            System.out.println("IOException in CardWebSocketHandler: Sending TextMessage objects failed.");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        int accessCode = getAccessCode(session);
        webSocketSessions.get(accessCode).remove(session);

        // If the game was deleted, delete the mapping.
        try {
            gameService.getGame(accessCode);
        }
        catch (ResponseStatusException e) {
            webSocketSessions.remove(accessCode);
        }
    }

    public HashMap<Integer, ArrayList<WebSocketSession>> getWebSocketSessions() {
        return webSocketSessions;
    }

}
