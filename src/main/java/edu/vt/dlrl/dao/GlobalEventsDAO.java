package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.DateRange;
import edu.vt.dlrl.domain.Event;
import edu.vt.dlrl.domain.TermFrequency;

import java.util.List;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public interface GlobalEventsDAO {
    DateRange getMaxDateRange();

    List<TermFrequency> getTermFrequencies(DateRange dateRange, int kForEvent);

    List<Event> getEvents(DateRange dateRange);
}
