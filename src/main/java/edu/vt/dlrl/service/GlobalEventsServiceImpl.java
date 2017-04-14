package edu.vt.dlrl.service;

import edu.vt.dlrl.dao.GlobalEventsDAO;
import edu.vt.dlrl.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author: dedocibula
 * Created on: 13.4.2017.
 */
@Service
public class GlobalEventsServiceImpl implements GlobalEventsService {

    private final GlobalEventsDAO dao;
    private final Comparator<TermFrequency> comparator;

    @Autowired
    public GlobalEventsServiceImpl(@Qualifier("in-memory-dao") GlobalEventsDAO dao) {
        this.dao = dao;
        this.comparator = new TermComparator();
    }

    @Override
    public DateRange getMaxDateRange() {
        return dao.getMaxDateRange();
    }

    @Override
    public TermSelection getTermSelection(DateRange dateRange, int topK, Set<String> eventIds) {
        Map<Event, List<TermFrequency>> eventTermFrequencies = dao.getEventTermFrequencies(dateRange, topK);
        NavigableSet<TermFrequency> termSet = mergeAndOrder(eventTermFrequencies, eventIds);
        List<TermFrequency> topKTerms = getTopK(termSet, topK);
        TermSelection selection = new TermSelection(dateRange.getFrom(), dateRange.getTo(), topKTerms);
        Set<Event> events = eventTermFrequencies.keySet();
        for (Event event : events)
            event.setSelected(eventIds.isEmpty() || eventIds.contains(event.getId()));
        selection.setEvents(events);
        return selection;
    }

    @Override
    public TermMentions getTermMentions(String term, DateRange dateRange, Set<String> eventIds) {
        Map<Event, List<String>> eventTermToURLs = dao.getEventTermToURLs(term, dateRange);
        for (Iterator<Event> it = eventTermToURLs.keySet().iterator(); it.hasNext();) {
            Event event = it.next();
            if (!eventIds.isEmpty() && !eventIds.contains(event.getId()))
                it.remove();
        }
        TermMentions mentions = new TermMentions(term, dateRange.getFrom(), dateRange.getTo());
        mentions.setEventsToURLs(eventTermToURLs);
        return mentions;
    }

    private NavigableSet<TermFrequency> mergeAndOrder(Map<Event, List<TermFrequency>> eventTermFrequencies,
                                                      Set<String> eventIds) {
        TreeSet<TermFrequency> set = new TreeSet<>(comparator);
        Map<String, TermFrequency> visited = new HashMap<>();
        for (Event event : eventTermFrequencies.keySet()) {
            if (!eventIds.isEmpty() && !eventIds.contains(event.getId()))
                continue;
            for (TermFrequency termFrequency : eventTermFrequencies.get(event)) {
                if (visited.containsKey(termFrequency.getTerm())) {
                    TermFrequency current = visited.remove(termFrequency.getTerm());
                    set.remove(current);
                    termFrequency.setFrequency(termFrequency.getFrequency() + current.getFrequency());
                }
                set.add(termFrequency);
                visited.put(termFrequency.getTerm(), termFrequency);
            }
        }
        return set;
    }

    private List<TermFrequency> getTopK(NavigableSet<TermFrequency> termSet, int topK) {
        List<TermFrequency> termFrequencies = new ArrayList<>();
        int current = 0;
        for (TermFrequency termFrequency : termSet) {
            if (current >= topK)
                break;
            termFrequencies.add(termFrequency);
            current++;
        }
        return termFrequencies;
    }

    private static final class TermComparator implements Comparator<TermFrequency> {
        @Override
        public int compare(TermFrequency o1, TermFrequency o2) {
            int result = Integer.compare(o2.getFrequency(), o1.getFrequency());
            return result != 0 ? result : o1.getTerm().compareTo(o2.getTerm());
        }
    }
}
