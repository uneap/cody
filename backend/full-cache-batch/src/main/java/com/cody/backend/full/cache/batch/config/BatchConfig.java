package com.cody.backend.full.cache.batch.config;

import static com.cody.backend.full.cache.batch.constant.constants.DISPLAY_PRODUCT_JOB;
import static com.cody.backend.full.cache.batch.constant.constants.DISPLAY_PRODUCT_STEP;
import static com.cody.backend.full.cache.batch.constant.constants.FULL_BRAND_STEP;
import static com.cody.backend.full.cache.batch.constant.constants.FULL_USER_STEP;

import com.cody.backend.full.cache.batch.listener.ChunkLoggingListener;
import com.cody.backend.full.cache.batch.parameter.DateJobParametersIncrementer;
import com.cody.backend.full.cache.batch.steps.AllUserReader;
import com.cody.backend.full.cache.batch.steps.AllUserWriter;
import com.cody.backend.full.cache.batch.steps.DisplayProductReader;
import com.cody.backend.full.cache.batch.steps.DisplayProductWriter;
import com.cody.backend.full.cache.batch.steps.FullBrandReader;
import com.cody.backend.full.cache.batch.steps.FullBrandWriter;
import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.FullBrand;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(BatchProperties.class)
@EnableTransactionManagement
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;
    private final ChunkLoggingListener chunkLoggingListener;

    private final DateJobParametersIncrementer dateJobParametersIncrementer;

    private final DisplayProductReader productReader;
    private final DisplayProductWriter productWriter;
    private final AllUserReader allUserReader;
    private final AllUserWriter allUserWriter;
    private final FullBrandReader fullBrandReader;
    private final FullBrandWriter fullBrandWriter;
    private static final int chunkSize = 1000;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository, BatchProperties properties) {
        JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
        String jobNames = properties.getJob().getName();
        if (StringUtils.hasText(jobNames)) {
            runner.setJobName(jobNames);
        }
        return runner;
    }

    @Bean(name = DISPLAY_PRODUCT_JOB)
    public Job displayProductJob() {
        return new JobBuilder(DISPLAY_PRODUCT_JOB, jobRepository)
            .incrementer(dateJobParametersIncrementer)
            .start(fullBrandStep())
            .next(displayProductStep())
            .next(allUserStep())
            .build();
    }

    @Bean(name = DISPLAY_PRODUCT_STEP)
    public Step displayProductStep() {
        return new StepBuilder(DISPLAY_PRODUCT_STEP, jobRepository)
            .allowStartIfComplete(true)
            .<DisplayProduct,DisplayProduct>chunk(chunkSize, transactionManager)
            .reader(productReader)
            .writer(productWriter)
            .listener(chunkLoggingListener)
            .build();
    }

    @Bean(name =FULL_USER_STEP)
    public Step allUserStep() {
        return new StepBuilder(FULL_USER_STEP, jobRepository)
            .<AllUser,AllUser>chunk(chunkSize, transactionManager)
            .allowStartIfComplete(true)
            .reader(allUserReader)
            .writer(allUserWriter)
            .listener(chunkLoggingListener)
            .build();
    }

    @Bean(name =FULL_BRAND_STEP)
    public Step fullBrandStep() {
        return new StepBuilder(FULL_BRAND_STEP, jobRepository)
            .<FullBrand,FullBrand>chunk(chunkSize, transactionManager)
            .allowStartIfComplete(true)
            .reader(fullBrandReader)
            .writer(fullBrandWriter)
            .listener(chunkLoggingListener)
            .build();
    }

}
