package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.ElementCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    @ElementCollection
    private final List<Card> cards;

    private int cardIdx = 0;

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

        cardIdx = cardIdx % cards.size();
        return cards.get(cardIdx++);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }
}

