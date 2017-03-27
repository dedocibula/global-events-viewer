package edu.vt.dlrl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

/**
 * Author: dedocibula
 * Created on: 26.3.2017.
 */
@SpringBootApplication
public class GlobalEventsApplication {

    @Value("hbase.zk.host")
    private String hbaseZkQuorum;
    @Value("hbase.zk.port")
    private String hbaseZkPort;

    @Autowired
    private Configuration configuration;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Configuration getHbaseConfiguration() {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", hbaseZkQuorum);
        config.set("hbase.zookeeper.property.clientPort", hbaseZkPort);
        return config;
    }

    @Bean
    public HbaseTemplate getConnection() {
        return new HbaseTemplate(configuration);
    }

    public static void main(String[] args) {
        SpringApplication.run(GlobalEventsApplication.class, args);
    }
}
