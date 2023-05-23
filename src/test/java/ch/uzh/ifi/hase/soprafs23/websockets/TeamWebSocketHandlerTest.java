package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TeamWebSocketHandlerTest {
    @Mock
    private WebSocketSession session;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private UserService userService;


    private TeamWebSocketHandler teamWebSocketHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teamWebSocketHandler = new TeamWebSocketHandler(lobbyService, userService);
    }

    @Test
    void testAfterConnectionEstablished() {
        WebSocketSession session = mock(WebSocketSession.class);

        teamWebSocketHandler.afterConnectionEstablished(session);

        List<WebSocketSession> webSocketSessions = teamWebSocketHandler.getWebSocketSessions();
        assertEquals(session, webSocketSessions.get(0));
    }

    @Test
    void testHandleTextMessage_Addition() throws Exception {
        String messagePayload = "\"accessCode\":123456,\"teamNr\":1,\"userId\":1,\"type\":\"addition\"";
        TextMessage message = new TextMessage(messagePayload);
        User user = new User();
        user.setUsername("testName");

        Mockito.when(userService.getUser(1)).thenReturn(user);

        teamWebSocketHandler.afterConnectionEstablished(session);
        teamWebSocketHandler.handleTextMessage(session, message);

        Mockito.verify(lobbyService, times(1)).joinLobbyTeam(123456, 1, 1);
        assertEquals(1, teamWebSocketHandler.getWebSocketSessions().size());
        Mockito.verify(session, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    void testHandleTextMessage_Removal() throws Exception {
        String messagePayload = "\"accessCode\":123456,\"teamNr\":1,\"userId\":1,\"type\":\"removal\"";
        TextMessage message = new TextMessage(messagePayload);
        User user = new User();
        user.setUsername("testUser");
        Mockito.when(userService.getUser(1)).thenReturn(user);
        teamWebSocketHandler.afterConnectionEstablished(session);
        teamWebSocketHandler.handleTextMessage(session, message);


        Mockito.verify(lobbyService, times(1)).leaveLobbyTeam(123456, 1, 1);
        Mockito.verify(session, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    void testCallBack() throws IOException {
        // Arrange
        int accessCode = 123456;
        int teamNr = 1;
        int userId = 1;
        User user = new User();
        user.setUsername("testUser");

        Mockito.when(userService.getUser(userId)).thenReturn(user);
        teamWebSocketHandler.afterConnectionEstablished(session);
        teamWebSocketHandler.callBack(accessCode, teamNr, userId);

        // Assert
        Mockito.verify(userService, times(1)).getUser(userId);
        Mockito.verify(session, times(1)).sendMessage(any(TextMessage.class));
    }
    @Test
    void testAfterConnectionClosed_GameNotDeleted() {
        WebSocketSession session = mock(WebSocketSession.class);

        teamWebSocketHandler.afterConnectionEstablished(session);
        teamWebSocketHandler.afterConnectionClosed(session, CloseStatus.NORMAL);
        List<WebSocketSession> webSocketSessions = teamWebSocketHandler.getWebSocketSessions();
        assertFalse(webSocketSessions.contains(session));
    }


}
