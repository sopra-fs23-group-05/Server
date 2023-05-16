package ch.uzh.ifi.hase.soprafs23.websockets;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class TimerWebSocketHandler extends TextWebSocketHandler {

    // HashMap that stores a list of sessions for each access code
    private final HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = new HashMap<>();

    // The current value of the timer
    private int timerValue;

    private final GameService gameService;
    private boolean isRunning = false;

    public TimerWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }
    public TimerWebSocketHandler() {
        this.gameService = null;
        timerValue =10;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws InterruptedException, IOException {
        int accessCode = getAccessCode(session);
        // If the access code is not in the HashMap, add it
        if (!webSocketSessions.containsKey(accessCode)) {
            webSocketSessions.put(accessCode, new ArrayList<>());
        }
        webSocketSessions.get(accessCode).add(session);
        if (!isRunning) {
            if (gameService != null) {
                timerValue = gameService.getGame(accessCode).getSettings().getRoundTime();
            }
            isRunning = true;
            startTimer(accessCode);

        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        int accessCode = getAccessCode(session);
        String time = String.valueOf(timerValue);
        for (WebSocketSession webSocketSession : webSocketSessions.get(accessCode)) {
            webSocketSession.sendMessage(new TextMessage(time));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        int accessCode = getAccessCode(session);
        webSocketSessions.get(accessCode).remove(session);
        if (webSocketSessions.get(accessCode).isEmpty()) {
            stopTimer(accessCode);
        }
    }
    public void stopTimer(int accessCode) {
        if (isRunning) {
            isRunning = false;
            if (gameService != null) {
                timerValue = gameService.getGame(accessCode).getSettings().getRoundTime();
            } else {
                timerValue = 10;
            }
        }
    }

    public void startTimer(int accessCode) throws InterruptedException, IOException {
        for (int tick = timerValue; tick >= 0; tick --){
            String time = String.valueOf(timerValue);
            for (WebSocketSession webSocketSession : webSocketSessions.get(accessCode)) {
                webSocketSession.sendMessage(new TextMessage(time));
            }
            System.out.println("Sending message: " + time);
            timerValue--;
            Thread.sleep(1000);
        }
    }

    /** Extracts the access code from a WebSocketSession object. */
    private static int getAccessCode(WebSocketSession session) {
        return Integer.parseInt(session.getUri().toString().substring(session.getUri().toString().lastIndexOf('/') + 1));
    }
}