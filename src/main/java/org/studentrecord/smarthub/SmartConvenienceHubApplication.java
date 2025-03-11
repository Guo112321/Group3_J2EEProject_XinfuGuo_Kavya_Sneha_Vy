package org.studentrecord.smarthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SmartConvenienceHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartConvenienceHubApplication.class, args);
    }

}
