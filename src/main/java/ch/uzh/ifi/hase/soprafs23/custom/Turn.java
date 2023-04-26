package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.ArrayList;

import static java.lang.String.valueOf;

@Embeddable
public class Turn {
    @Embedded
    private final Deck deck;

    // private Timer timer;    do we need a timer class in the backend? like it is in our class diagram

    private int turnPoints;

    private Card drawnCard;

    private int buzzCounter = 0;

    private int mockCounter = 0;

    public Turn(Deck deck) {
        this.deck = deck;
        this.turnPoints = 0;
        this.drawnCard=deck.draw();
    }

    public Turn() {
        this.deck = new Deck();
    }

    public Card drawCard(){
        if(deck == null){
            String s = "mock" + valueOf(mockCounter);
            drawnCard = new Card(s, s, s, s, s, s);
            mockCounter++;
            return drawnCard;
        }
        drawnCard = deck.draw();
        return drawnCard;
    }

    public Card skip() {
        turnPoints--;
        return drawCard();
    }

    public Card buzz(){
        buzzCounter++;
        if(buzzCounter == 2){
            buzzCounter = 0;
            turnPoints--;
            return drawCard();
        }
        return drawnCard;
    }

    public boolean guess(String guess){
        if(drawnCard.isCorrectGuess(guess)){
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
}
