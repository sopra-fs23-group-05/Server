package ch.uzh.ifi.hase.soprafs23.websockets;

import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final static String CHAT_ENDPOINT = "/chats/{accessCode}";
    private final static String CARD_ENDPOINT = "/cards/{accessCode}";
    private final static String TEAM_ENDPOINT = "/teams";
    private final static String PAGE_ENDPOINT = "/pages/{accessCode}";
    private final static String TIMER_ENDPOINT = "/timers/{accessCode}";
    private final static String PREGAME_ENDPOINT = "/pregameTimers/{accessCode}";

    private final GameService gameService;

    private final LobbyService lobbyService;

    private final UserService userService;

    public WebSocketConfig(GameService gameService, LobbyService lobbyService, UserService userService) {
        this.gameService = gameService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(getChatWebSocketHandler(), CHAT_ENDPOINT).setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(getCardWebSocketHandler(), CARD_ENDPOINT).setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(getTeamWebSocketHandler(), TEAM_ENDPOINT).setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(getPageWebSocketHandler(), PAGE_ENDPOINT).setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(getTimerWebSocketHandler(), TIMER_ENDPOINT).setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(getPreGameTimeWebSocketHandler(), PREGAME_ENDPOINT).setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler getPageWebSocketHandler() {
        return new PageWebSocketHandler(gameService);
    }

    @Bean
    public WebSocketHandler getCardWebSocketHandler() {
        return new CardWebSocketHandler(gameService);
    }

    @Bean
    public WebSocketHandler getChatWebSocketHandler() {
        return new ChatWebSocketHandler(gameService);
    }

    @Bean
    public WebSocketHandler getTeamWebSocketHandler() {
        return new TeamWebSocketHandler(lobbyService, userService);
    }

    @Bean
    public WebSocketHandler getTimerWebSocketHandler() {
        return new TimerWebSocketHandler(gameService);
    }

    @Bean
    public WebSocketHandler getPreGameTimeWebSocketHandler() {
        return new TimerWebSocketHandler();
    }
}
