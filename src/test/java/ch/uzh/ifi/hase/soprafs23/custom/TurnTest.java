package ch.uzh.ifi.hase.soprafs23.custom;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {
    @Test
    void addCard() {
        Turn turn = new Turn();
        turn.addCard(new Card());
        assertEquals(1, turn.getDeck().getCards().size());
    }

    @Test
    void correctGuess() {
        Turn turn = new Turn();
        Card card = new Card("apple", "fruit", "red", "green", "yellow", "blue");
        turn.addCard(card);
        turn.drawCard();
        turn.guess("apple");
        assertEquals(1, turn.getTurnPoints());
    }

    @Test
    void wrongGuess() {
        Turn turn = new Turn();
        Card card = new Card("apple", "fruit", "red", "green", "yellow", "blue");
        turn.addCard(card);
        turn.drawCard();
        turn.guess("banana");
        assertEquals(0, turn.getTurnPoints());
    }

    @Test
    void buzz() {
        Turn turn = new Turn();
        Card card = new Card("apple", "fruit", "red", "green", "yellow", "blue");
        turn.addCard(card);
        turn.drawCard();
        turn.buzz();
        assertEquals(0, turn.getTurnPoints());
    }

    @Test
    void twoBuzz() {
        Turn turn = new Turn();
        Card card = new Card("apple", "fruit", "red", "green", "yellow", "blue");
        Card card2 = new Card("banana", "fruit", "red", "green", "yellow", "blue");
        turn.addCard(card);
        turn.addCard(card2);
        turn.drawCard();
        turn.buzz();
        turn.buzz();
        assertEquals(-1, turn.getTurnPoints());
    }

    @Test
    void skip() {
        Turn turn = new Turn();
        Card card = new Card("apple", "fruit", "red", "green", "yellow", "blue");
        Card card2 = new Card("banana", "fruit", "red", "green", "yellow", "blue");
        turn.addCard(card);
        turn.addCard(card2);
        turn.drawCard();
        turn.skip();
        assertEquals(-1, turn.getTurnPoints());
    }

}
