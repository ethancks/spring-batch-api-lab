package com.job.lab.batchprocessor.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DummyJobConfig {
    @Bean
    public Job dummyJob() {
        return new Job() {
            @Override public String getName() { return "dummy"; }
            @Override public boolean isRestartable() { return false; }
            @Override
            public void execute(JobExecution execution) {}
        };
    }
}

