package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.DateRange;
import edu.vt.dlrl.domain.Event;
import edu.vt.dlrl.domain.TermFrequency;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public interface GlobalEventsDAO {
    DateRange getMaxDateRange();

    LinkedHashMap<Event, List<TermFrequency>> getEventTermFrequencies(DateRange dateRange, int kForEvent);

    LinkedHashMap<Event, List<String>> getEventTermToURLs(String term, DateRange dateRange);
}
