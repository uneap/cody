package com.cody.backend.display;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.cody.domain"})
@EntityScan("com.cody.domain")
@SpringBootApplication(scanBasePackages = {"com.cody.domain", "com.cody.resource", "com.cody.common.core", "com.cody.backend.storage"})
public class DisplayApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageApplication.class, args);
    }
}
