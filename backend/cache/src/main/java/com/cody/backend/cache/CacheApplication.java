package com.cody.backend.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.cody.domain"})
@EntityScan("com.cody.domain")
@SpringBootApplication(scanBasePackages = {"com.cody.resource", "com.cody.backend.cache", "com.cody.domain"}, exclude = {KafkaAutoConfiguration.class})
public class CacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }
}
