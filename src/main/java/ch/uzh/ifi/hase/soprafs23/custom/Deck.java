
package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    @ElementCollection
    private final List<Card> cards;

    public Deck(List<Card> cards){
        this.cards = cards;
    }

    public Deck(){
        this.cards = new ArrayList<>();
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

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }
}

