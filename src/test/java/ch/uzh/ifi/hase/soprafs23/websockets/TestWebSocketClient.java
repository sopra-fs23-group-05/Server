package ch.uzh.ifi.hase.soprafs23.websockets;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;

// Source: https://stackoverflow.com/questions/37899268/test-websocket-in-spring-boot-application

@ClientEndpoint
public class TestWebSocketClient {
    Session session;

    @OnOpen
    public void onOpen(final Session session){
        this.session = session;
    }
}
