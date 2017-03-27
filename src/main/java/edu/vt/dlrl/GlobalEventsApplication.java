package edu.vt.dlrl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Author: dedocibula
 * Created on: 26.3.2017.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class GlobalEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalEventsApplication.class, args);
    }
}
