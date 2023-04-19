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



    public Turn(Deck deck) {
        this.deck = deck;
        this.turnPoints = 0;
        this.drawnCard=deck.draw();

    }

    public Turn() {
        this.deck = null;
    }


    public void describe(String s){

    }
    public Card skipCard(){
        Card card =deck.draw();
        drawnCard = card;
        return card;
    }
    public void buzz(){

    }
}
