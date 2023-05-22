package ch.uzh.ifi.hase.soprafs23.custom;


import ch.uzh.ifi.hase.soprafs23.websockets.TimerWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

public class Timer extends Thread {
    private final List<WebSocketSession> webSocketSessions;
    private int timerValue;
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
            Thread.sleep(100);  // Wait until all sessions are connected (I cannot iterate over webSocketSessions while it is being modified --> ConcurrentModificationException)
            for (int tick = timerValue; tick >= 0; tick--) {
                String time = String.valueOf(timerValue);
                for (WebSocketSession webSocketSession : webSocketSessions) {
                    webSocketSession.sendMessage(new TextMessage(time));
                }
                System.out.println("Sending message: " + time);
                timerValue--;
                Thread.sleep(1000);
            }
            timerWebSocketHandler.callBack(accessCode);
        }
        catch(IOException e){
            System.out.println("IOException");
        }
        catch(InterruptedException e){
            System.out.println("InterruptedException");
            Thread.currentThread().interrupt();
        }

    }
}