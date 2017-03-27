package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.WordFrequency;

import java.util.List;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public interface GlobalEventsDAO {
    List<WordFrequency> getWordFrequencies();
}
