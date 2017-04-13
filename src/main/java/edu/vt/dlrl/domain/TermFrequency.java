package edu.vt.dlrl.domain;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public class TermFrequency {
    private String term;
    private int frequency;

    public TermFrequency(String term, int frequency) {
        this.term = term;
        this.frequency = frequency;
    }

    public TermFrequency(TermFrequency copy) {
        this(copy.term, copy.frequency);
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
