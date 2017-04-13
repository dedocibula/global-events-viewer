package edu.vt.dlrl.service;

import edu.vt.dlrl.domain.TermSelection;

import java.util.Date;
import java.util.Set;

/**
 * Author: dedocibula
 * Created on: 13.4.2017.
 */
public interface GlobalEventsService {
    TermSelection getTermSelection(Date from, Date to, int topK, Set<String> eventIds);
}
