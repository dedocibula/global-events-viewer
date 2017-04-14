package edu.vt.dlrl.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: dedocibula
 * Created on: 14.4.2017.
 */
public class TermMentions {
    private String term;
    private Date from;
    private Date to;
    private Map<Event, List<String>> eventsToURLs;

    public TermMentions(String term, Date from, Date to) {
        this.term = term;
        this.from = from;
        this.to = to;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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

    public Map<Event, List<String>> getEventsToURLs() {
        return eventsToURLs;
    }

    public void setEventsToURLs(Map<Event, List<String>> eventsToURLs) {
        this.eventsToURLs = eventsToURLs;
    }
}
