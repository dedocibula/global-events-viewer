package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.TermFrequency;

import java.util.Date;
import java.util.List;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public interface GlobalEventsDAO {
    List<TermFrequency> getTermFrequencies(Date from, Date to, int kForEvent);

    List<String> getEventNames(Date date);
}
