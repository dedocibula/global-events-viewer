package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.TermFrequency;

import java.util.List;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public interface GlobalEventsDAO {
    List<TermFrequency> getWordFrequencies();
}
