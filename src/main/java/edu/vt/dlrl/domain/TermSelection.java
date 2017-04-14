package edu.vt.dlrl.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Author: dedocibula
 * Created on: 13.4.2017.
 */
public class TermSelection {
    @JsonFormat(pattern = "yyyy")
    private Date from;
    @JsonFormat(pattern = "yyyy")
    private Date to;
    private List<TermFrequency> termFrequencies;
    private Set<Event> events;

    public TermSelection(Date from, Date to, List<TermFrequency> termFrequencies) {
        this.from = from;
        this.to = to;
        this.termFrequencies = termFrequencies;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public List<TermFrequency> getTermFrequencies() {
        return termFrequencies;
    }

    public void setTermFrequencies(List<TermFrequency> termFrequencies) {
        this.termFrequencies = termFrequencies;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }
}
