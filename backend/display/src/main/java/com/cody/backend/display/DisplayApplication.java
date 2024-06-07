package com.cody.backend.display;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cody.domain", "com.cody.resource", "com.cody.common.core", "com.cody.backend.storage", "com.cody.backend.display"})
public class DisplayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisplayApplication.class, args);
    }
}
