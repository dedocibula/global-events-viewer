package edu.vt.dlrl.domain;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public class TermFrequency {
    private String term;
    private int size;

    public TermFrequency(String term, int size) {
        this.term = term;
        this.size = size;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
