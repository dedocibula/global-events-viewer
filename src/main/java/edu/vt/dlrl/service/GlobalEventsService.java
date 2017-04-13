package edu.vt.dlrl.service;

import edu.vt.dlrl.domain.TermFrequency;

import java.util.Date;
import java.util.List;

/**
 * Author: dedocibula
 * Created on: 13.4.2017.
 */
public interface GlobalEventsService {
    List<TermFrequency> loadTermFrequencies(Date from, Date to, int topK);
}
