package edu.vt.dlrl.domain;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public class WordFrequency {
    private String word;
    private int size;

    public WordFrequency(String word, int size) {
        this.word = word;
        this.size = size;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
