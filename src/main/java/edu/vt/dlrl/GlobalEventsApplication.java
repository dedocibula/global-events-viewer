package edu.vt.dlrl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

/**
 * Author: dedocibula
 * Created on: 26.3.2017.
 */
@SpringBootApplication
@PropertySource("classpath:hbase.properties")
public class GlobalEventsApplication {

    @Value("${hbase.zk.host}")
    private String hbaseZkQuorum;
    @Value("${hbase.zk.port}")
    private String hbaseZkPort;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public org.apache.hadoop.conf.Configuration getHbaseConfiguration() {
        org.apache.hadoop.conf.Configuration config = org.apache.hadoop.hbase.HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", hbaseZkQuorum);
        config.set("hbase.zookeeper.property.clientPort", hbaseZkPort);
        return config;
    }

    public static void main(String[] args) {
        SpringApplication.run(GlobalEventsApplication.class, args);
    }
}
