package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final static String CHAT_ENDPOINT = "/chat";
    private final static String CARD_ENDPOINT = "/cards";

    private final static String TEAM_ENDPOINT = "/teams";

    private final GameService gameService;

    private final LobbyService lobbyService;

    public WebSocketConfig(GameService gameService, LobbyService lobbyService){
        this.gameService = gameService;
        this.lobbyService = lobbyService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(getChatWebSocketHandler(), CHAT_ENDPOINT)
                .setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(getCardWebSocketHandler(), CARD_ENDPOINT)
                .setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(getTeamWebSocketHandler(), TEAM_ENDPOINT)
                .setAllowedOrigins("*");
    }

    private WebSocketHandler getCardWebSocketHandler() {
        return new CardWebSocketHandler(gameService);
    }

    @Bean
    public WebSocketHandler getChatWebSocketHandler() {
        return new ChatWebSocketHandler(gameService);
    }

    @Bean
    public WebSocketHandler getTeamWebSocketHandler() {
        return new TeamWebSocketHandler(lobbyService);
    }
}
