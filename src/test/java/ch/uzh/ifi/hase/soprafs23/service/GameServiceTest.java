package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // #50, Test if the nextTurn method throws an exception when the game is not found
    @Test
    void nextTurn_invalidInputs_gameNotFound() {
        Mockito.when(gameRepository.findByAccessCode(123456)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.nextTurn(123456, 3));
    }
}