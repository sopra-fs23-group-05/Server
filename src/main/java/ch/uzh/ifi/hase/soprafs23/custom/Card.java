package ch.uzh.ifi.hase.soprafs23.custom;

import java.util.Objects;

public class Card {

    private final  String word;

    private final String taboo1;

    private final String taboo2;

    private final String taboo3;

    private final String taboo4;

    private final String taboo5;

    private final String definition;

    public Card(String word, String taboo1, String taboo2, String taboo3, String taboo4, String taboo5, String definition){
        this.word = word;
        this.taboo1 = taboo1;
        this.taboo2 = taboo2;
        this.taboo3 = taboo3;
        this.taboo4 = taboo4;
        this.taboo5 = taboo5;
        this.definition = definition;
    }

    public boolean isCorrectGuess(String guess){
        return Objects.equals(guess, word);
    }

    public boolean isTaboo(String taboo){
        return Objects.equals(taboo, taboo1) || Objects.equals(taboo, taboo2) || Objects.equals(taboo, taboo3) || Objects.equals(taboo, taboo4) || Objects.equals(taboo, taboo5);
    }
    public String getDefinition(){
        return definition;
    }

}
