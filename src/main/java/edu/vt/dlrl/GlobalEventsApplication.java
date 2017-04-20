package edu.vt.dlrl;

import edu.vt.dlrl.dao.GlobalEventsDAO;
import edu.vt.dlrl.dao.HBaseDAOImpl;
import edu.vt.dlrl.dao.InMemoryDAOImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

/**
 * Author: dedocibula
 * Created on: 26.3.2017.
 */
@SpringBootApplication
@PropertySource("classpath:hbase.properties")
public class GlobalEventsApplication {

    @Bean
    public GlobalEventsDAO getGlobalEventDAO(@Value("${dao.type}") String daoType, HbaseTemplate template) {
        if ("hbase".equalsIgnoreCase(daoType))
            return new HBaseDAOImpl(template);
        else
            return new InMemoryDAOImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public org.apache.hadoop.conf.Configuration getHbaseConfiguration(@Value("${hbase.zk.host}") String hbaseZkQuorum,
                                                                      @Value("${hbase.zk.port}") String hbaseZkPort) {
        org.apache.hadoop.conf.Configuration config = org.apache.hadoop.hbase.HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", hbaseZkQuorum);
        config.set("hbase.zookeeper.property.clientPort", hbaseZkPort);
        return config;
    }

    public static void main(String[] args) {
        SpringApplication.run(GlobalEventsApplication.class, args);
    }
}
