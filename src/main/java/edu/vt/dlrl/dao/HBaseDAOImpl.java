package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.DateRange;
import edu.vt.dlrl.domain.Event;
import edu.vt.dlrl.domain.TermFrequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
@Repository("hbase-dao")
public class HBaseDAOImpl implements GlobalEventsDAO {

    private final HbaseTemplate template;

    @Autowired
    public HBaseDAOImpl(HbaseTemplate template) {
        this.template = template;
    }

    @Override
    public DateRange getMaxDateRange() {
        return null;
    }

    @Override
    public LinkedHashMap<Event, List<TermFrequency>> getEventTermFrequencies(DateRange dateRange, int kForEvent) {
        return new LinkedHashMap<>();
    }

    @Override
    public LinkedHashMap<Event, List<String>> getEventTermToURLs(String term, DateRange dateRange) {
        return new LinkedHashMap<>();
    }
}
