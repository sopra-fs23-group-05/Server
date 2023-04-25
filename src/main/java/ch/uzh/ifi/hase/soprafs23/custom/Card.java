package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Card {

    private  String word;

    private String taboo1;

    private String taboo2;

    private String taboo3;

    private String taboo4;

    private String taboo5;

    public Card(String word, String taboo1, String taboo2, String taboo3, String taboo4, String taboo5){
        this.word = word;
        this.taboo1 = taboo1;
        this.taboo2 = taboo2;
        this.taboo3 = taboo3;
        this.taboo4 = taboo4;
        this.taboo5 = taboo5;
    }

    public Card() {
    }

    public boolean isCorrectGuess(String guess){
        return Objects.equals(guess, word);
    }

    @Override
    public String toString() {
        return "{" +
                "\"word\":\"" + word + '\"' +
                ", \"taboo1\":\"" + taboo1 + '\"' +
                ", \"taboo2\":\"" + taboo2 + '\"' +
                ", \"taboo3\":\"" + taboo3 + '\"' +
                ", \"taboo4\":\"" + taboo4 + '\"' +
                ", \"taboo5\":\"" + taboo5 + '\"' +
                '}';
    }
}
