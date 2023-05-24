package ch.uzh.ifi.hase.soprafs23.custom;

import javax.persistence.Embeddable;

@Embeddable
public class Card {

    private String word;

    private String taboo1;

    private String taboo2;

    private String taboo3;

    private String taboo4;

    private String taboo5;

    public Card(String word, String taboo1, String taboo2, String taboo3, String taboo4, String taboo5) {
        this.word = word;
        this.taboo1 = taboo1;
        this.taboo2 = taboo2;
        this.taboo3 = taboo3;
        this.taboo4 = taboo4;
        this.taboo5 = taboo5;
    }

    public Card() {
    }

    public boolean isCorrectGuess(String guess) {
        return word.equalsIgnoreCase(guess);
    }

    @Override
    public String toString() {
        return "{" + "\"word\":\"" + word + '\"' + ", \"taboo1\":\"" + taboo1 + '\"' + ", \"taboo2\":\"" + taboo2 + '\"' + ", \"taboo3\":\"" + taboo3 + '\"' + ", \"taboo4\":\"" + taboo4 + '\"' + ", \"taboo5\":\"" + taboo5 + '\"' + '}';
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTaboo1() {
        return taboo1;
    }

    public void setTaboo1(String taboo1) {
        this.taboo1 = taboo1;
    }

    public String getTaboo2() {
        return taboo2;
    }

    public void setTaboo2(String taboo2) {
        this.taboo2 = taboo2;
    }

    public String getTaboo3() {
        return taboo3;
    }

    public void setTaboo3(String taboo3) {
        this.taboo3 = taboo3;
    }

    public String getTaboo4() {
        return taboo4;
    }

    public void setTaboo4(String taboo4) {
        this.taboo4 = taboo4;
    }

    public String getTaboo5() {
        return taboo5;
    }

    public void setTaboo5(String taboo5) {
        this.taboo5 = taboo5;
    }
}
