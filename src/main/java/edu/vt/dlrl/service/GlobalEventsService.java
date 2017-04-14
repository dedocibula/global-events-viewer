package edu.vt.dlrl.service;

import edu.vt.dlrl.domain.DateRange;
import edu.vt.dlrl.domain.TermMentions;
import edu.vt.dlrl.domain.TermSelection;

import java.util.Date;
import java.util.Set;

/**
 * Author: dedocibula
 * Created on: 13.4.2017.
 */
public interface GlobalEventsService {
    DateRange getMaxDateRange();

    TermSelection getTermSelection(DateRange dateRange, int topK, Set<String> eventIds);

    TermMentions getTermMentions(String term, DateRange dateRange, Set<String> eventIds);
}
