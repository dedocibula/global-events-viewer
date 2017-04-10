package edu.vt.dlrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

/**
 * Author: dedocibula
 * Created on: 10.4.2017.
 */
@Configuration
public class HBaseConfiguration {

    private final org.apache.hadoop.conf.Configuration configuration;

    @Autowired
    public HBaseConfiguration(org.apache.hadoop.conf.Configuration configuration) {
        this.configuration = configuration;
    }

    @Bean
    public HbaseTemplate getConnection() {
        return new HbaseTemplate(configuration);
    }
}
