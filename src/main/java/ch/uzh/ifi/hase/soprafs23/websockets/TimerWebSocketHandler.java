package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.custom.Timer;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimerWebSocketHandler extends TextWebSocketHandler {

    // HashMap that stores a list of sessions for each access code
    private final HashMap<Integer, ArrayList<WebSocketSession>> webSocketSessions = new HashMap<>();
    private final GameService gameService;
    private final List<Integer> isRunning = new ArrayList<>();
    // The current value of the timer
    private int timerValue;

    public TimerWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public TimerWebSocketHandler() {
        this.gameService = null;
        timerValue = 10;
    }

    /**
     * Extracts the access code from a WebSocketSession object.
     */
    private static int getAccessCode(WebSocketSession session) {
        return Integer.parseInt(session.getUri().toString().substring(session.getUri().toString().lastIndexOf('/') + 1));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        int accessCode = getAccessCode(session);
        // If the access code is not in the HashMap, add it
        if (!webSocketSessions.containsKey(accessCode)) {
            webSocketSessions.put(accessCode, new ArrayList<>());
        }
        webSocketSessions.get(accessCode).add(session);

        startTimer(session);
    }

    private void startTimer(WebSocketSession session) {
        int accessCode = getAccessCode(session);
        if (!isRunning.contains(accessCode)) {
            if (gameService != null) {
                timerValue = gameService.getGame(accessCode).getSettings().getRoundTime();
            }
            isRunning.add(accessCode);
            Timer timer = new Timer(webSocketSessions.get(accessCode), timerValue, accessCode);
            timer.initializeTimerWebSocketHandler(this);
            timer.start();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        int accessCode = getAccessCode(session);
        webSocketSessions.get(accessCode).remove(session);

        try {
            if (gameService != null) {
                gameService.getGame(accessCode);
            }
        }
        catch (ResponseStatusException e) {
            webSocketSessions.remove(accessCode);
        }
    }

    public void callBack(int accessCode) {
        isRunning.remove(Integer.valueOf(accessCode));
    }

    public HashMap<Integer, ArrayList<WebSocketSession>> getWebSocketSessions() {
        return webSocketSessions;
    }
}