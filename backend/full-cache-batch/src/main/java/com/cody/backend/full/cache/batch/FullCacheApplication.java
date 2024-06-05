package com.cody.backend.full.cache.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cody.domain.store.cache", "com.cody.resource.redis", "com.cody.resource.db",
    "com.cody.common.core", "com.cody.backend.full.cache.batch"})
public class FullCacheApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(FullCacheApplication.class, args)));
    }
}
