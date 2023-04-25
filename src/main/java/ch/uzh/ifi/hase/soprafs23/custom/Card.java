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

    private String definition;

    public Card(String word, String taboo1, String taboo2, String taboo3, String taboo4, String taboo5, String definition){
        this.word = word;
        this.taboo1 = taboo1;
        this.taboo2 = taboo2;
        this.taboo3 = taboo3;
        this.taboo4 = taboo4;
        this.taboo5 = taboo5;
        this.definition = definition;
    }

    public Card() {
    }

    public boolean isCorrectGuess(String guess){
        return word.equalsIgnoreCase(guess);
    }

    public boolean isTaboo(String taboo){
        return Objects.equals(taboo, taboo1) || Objects.equals(taboo, taboo2) || Objects.equals(taboo, taboo3) || Objects.equals(taboo, taboo4) || Objects.equals(taboo, taboo5);
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
