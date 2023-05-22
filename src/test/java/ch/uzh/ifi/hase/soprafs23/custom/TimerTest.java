package ch.uzh.ifi.hase.soprafs23.custom;

import ch.uzh.ifi.hase.soprafs23.websockets.TimerWebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class TimerTest {

    @Mock
    private TimerWebSocketHandler mockWebSocketHandler;

    @Mock
    private WebSocketSession mockWebSocketSession;

    private List<WebSocketSession> webSocketSessions;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        webSocketSessions = new ArrayList<>();
        webSocketSessions.add(mockWebSocketSession);
    }

    @Test
    void testTimerSendsCorrectMessages() throws IOException {
        Timer timer = new Timer(webSocketSessions, 3, 123456);
        timer.initializeTimerWebSocketHandler(mockWebSocketHandler);

        timer.run();
        Mockito.verify(mockWebSocketSession, times(4)).sendMessage(any(TextMessage.class));
        Mockito.verify(mockWebSocketSession, times(1)).sendMessage(new TextMessage("3"));
        Mockito.verify(mockWebSocketSession, times(1)).sendMessage(new TextMessage("2"));
        Mockito.verify(mockWebSocketSession, times(1)).sendMessage(new TextMessage("1"));
        Mockito.verify(mockWebSocketSession, times(1)).sendMessage(new TextMessage("0"));
    }

    @Test
    void testTimerCallbackCalled() {
        Timer timer = new Timer(webSocketSessions, 1, 123456);
        timer.initializeTimerWebSocketHandler(mockWebSocketHandler);
        timer.run();
        Mockito.verify(mockWebSocketHandler, times(1)).callBack(123456);
    }
}