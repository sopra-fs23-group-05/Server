package ch.uzh.ifi.hase.soprafs23.custom;

import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck(List<Card> cards){
        this.cards = cards;
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card draw(){
        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }
    public boolean isEmpty(){
        return cards.isEmpty();
    }
}
