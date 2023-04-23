package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Turn {
    @Embedded
    private final Deck deck;

    // private Timer timer;    do we need a timer class in the backend? like it is in our class diagram

    private int turnPoints;

    private Card drawnCard;

    private int buzzCounter = 0;

    public Turn(Deck deck) {
        this.deck = deck;
        this.turnPoints = 0;
        this.drawnCard=deck.draw();
    }

    public Turn() {
        this.deck = null;
    }

    public Card drawCard(){
        drawnCard = deck.draw();
        return drawnCard;
    }
    public Card buzz(){
        buzzCounter++;
        if(buzzCounter == 2){
            buzzCounter = 0;
            return drawCard();
        }
        return drawnCard;
    }
}
