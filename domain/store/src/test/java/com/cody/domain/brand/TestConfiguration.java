package com.cody.domain.brand;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.cody.domain"})
@EntityScan("com.cody.domain")
@SpringBootApplication(scanBasePackages = {"com.cody.domain", "com.cody.resource.db", "com.cody.common.core"})
public class TestConfiguration {

}
