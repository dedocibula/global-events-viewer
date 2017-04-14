package edu.vt.dlrl.service;

import edu.vt.dlrl.dao.GlobalEventsDAO;
import edu.vt.dlrl.domain.DateRange;
import edu.vt.dlrl.domain.Event;
import edu.vt.dlrl.domain.TermFrequency;
import edu.vt.dlrl.domain.TermSelection;
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
        List<TermFrequency> termFrequencies = dao.getTermFrequencies(dateRange, topK);
        NavigableSet<TermFrequency> termSet = mergeAndOrder(termFrequencies);
        List<TermFrequency> topKTerms = getTopK(termSet, topK);
        TermSelection selection = new TermSelection(dateRange.getFrom(), dateRange.getTo(), topKTerms);
        List<Event> events = dao.getEvents(dateRange);
        for (Event event : events)
            event.setSelected(eventIds.isEmpty() || eventIds.contains(event.getId()));
        selection.setEvents(events);
        return selection;
    }

    private NavigableSet<TermFrequency> mergeAndOrder(List<TermFrequency> termFrequencies) {
        TreeSet<TermFrequency> set = new TreeSet<>(comparator);
        Map<String, TermFrequency> visited = new HashMap<>();
        for (TermFrequency termFrequency : termFrequencies) {
            if (visited.containsKey(termFrequency.getTerm())) {
                TermFrequency current = visited.remove(termFrequency.getTerm());
                set.remove(current);
                termFrequency.setFrequency(termFrequency.getFrequency() + current.getFrequency());
            }
            set.add(termFrequency);
            visited.put(termFrequency.getTerm(), termFrequency);
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
