package edu.vt.dlrl.service;

import edu.vt.dlrl.dao.GlobalEventsDAO;
import edu.vt.dlrl.domain.TermFrequency;
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
    public List<TermFrequency> loadTermFrequencies(Date from, Date to, int topK) {
        List<TermFrequency> termFrequencies = dao.getTermFrequencies(from, to, topK);
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
        termFrequencies.clear();
        int current = 0;
        for (TermFrequency termFrequency : set) {
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
