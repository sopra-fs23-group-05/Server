package ch.uzh.ifi.hase.soprafs23.custom;


import ch.uzh.ifi.hase.soprafs23.websockets.TimerWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

public class Timer extends Thread {
    private final List<WebSocketSession> webSocketSessions;
    private final int timerValue;
    private final int accessCode;
    private TimerWebSocketHandler timerWebSocketHandler;

    public Timer(List<WebSocketSession> webSocketSessions, int timerValue, int accessCode) {
        this.webSocketSessions = webSocketSessions;
        this.timerValue = timerValue;
        this.accessCode = accessCode;
    }

    public void initializeTimerWebSocketHandler(TimerWebSocketHandler timerWebSocketHandler) {
        this.timerWebSocketHandler = timerWebSocketHandler;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(250);  // Wait until all sessions are connected (I cannot iterate over webSocketSessions while it is being modified --> ConcurrentModificationException)

            String timeString = String.valueOf(timerValue);
            for (WebSocketSession webSocketSession : webSocketSessions) {
                webSocketSession.sendMessage(new TextMessage(timeString));
            }
            // Having the Thread.sleep before sending the messages ensures, that immediately after sending the last message, the callBack method is called.
            for (int tick = timerValue - 1; tick >= 0; tick--) {
                Thread.sleep(1000);

                timeString = String.valueOf(tick);
                System.out.println("Sending message: " + timeString);
                /* Sending messages takes time. When the fist client got the message and
                 * built his connection to the new websocket, this thread might still be
                 * sending messages. The following if clause paves the way to enable that. */
                if (tick == 0) {
                    timerWebSocketHandler.callBack(accessCode);
                }
                for (WebSocketSession webSocketSession : webSocketSessions) {
                    webSocketSession.sendMessage(new TextMessage(timeString));
                }
            }
        }
        catch (IOException e) {
            System.out.println("IOException");
        }
        catch (InterruptedException e) {
            System.out.println("InterruptedException");
            Thread.currentThread().interrupt();
        }

    }
}