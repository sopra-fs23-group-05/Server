
package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.ElementCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    @ElementCollection
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {

        if (cards.isEmpty()) {
            String s = "mock";
            return new Card(s, s, s, s, s, s);
        }

        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }
}

