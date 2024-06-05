package com.cody.backend.full.cache.batch.parameter;

import java.time.LocalDateTime;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

@Component
public class DateJobParametersIncrementer implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters parameters) {
        return new JobParametersBuilder().addString("run.id", LocalDateTime.now().toString()).toJobParameters();
    }
}
