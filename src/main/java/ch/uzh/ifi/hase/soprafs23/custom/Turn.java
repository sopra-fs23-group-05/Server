package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Turn {
    @Embedded
    private final Deck deck;

    private int turnPoints;

    private Card drawnCard;

    private int turnCounter;

    private int buzzCounter = 0;

    public Turn() {
        this.deck = new Deck();
        this.turnPoints = 0;
        this.turnCounter = 0;
    }

    public Card drawCard() {
        drawnCard = deck.draw();
        return drawnCard;
    }

    public Card skip() {
        turnPoints--;
        return drawCard();
    }

    public Card buzz() {
        buzzCounter++;
        if (buzzCounter == 2) {
            buzzCounter = 0;
            turnPoints--;
            return drawCard();
        }
        return null;
    }

    public boolean guess(String guess) {
        if (drawnCard.isCorrectGuess(guess)) {
            turnPoints++;
            return true;
        }
        return false;
    }

    public void addCard(Card card) {
        deck.addCard(card);
    }

    public Deck getDeck() {
        return deck;
    }

    public int getTurnPoints() {
        return turnPoints;
    }

    public void setTurnPoints(int turnPoints) {
        this.turnPoints = turnPoints;
    }

    public Card getDrawnCard() {
        return drawnCard;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void incrementTurnCounter() {
        this.turnCounter += 1;
    }

    public void resetTurnCounter() {
        this.turnCounter = 0;
    }
}
