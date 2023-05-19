package ch.uzh.ifi.hase.soprafs23.websockets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

// Source: https://stackoverflow.com/questions/37899268/test-websocket-in-spring-boot-application

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class WebSocketConfigTest {

    private WebSocketContainer container;
    private TestWebSocketClient client;

    @BeforeEach
    void setUp() {
        container = ContainerProvider.getWebSocketContainer();
        client = new TestWebSocketClient();
    }

    @Test
    void createSession() throws DeploymentException, IOException {
        container.connectToServer(client, URI.create("ws://localhost:8080/timers/123456"));
        assertNotNull(client.session);
    }
}