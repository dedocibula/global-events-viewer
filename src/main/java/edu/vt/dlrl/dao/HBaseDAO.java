package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.WordFrequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
@Repository("hbase-dao")
public class HBaseDAO implements GlobalEventsDAO {

    private HbaseTemplate template;

    @Autowired
    public HBaseDAO(HbaseTemplate template) {
        this.template = template;
    }

    @Override
    public List<WordFrequency> getWordFrequencies() {
        return Collections.emptyList();
    }
}
