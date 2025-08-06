package com.job.lab.batchprocessor.batch;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchRunner {

    private final JobLauncher jobLauncher;
    private final Job job;

    @PostConstruct
    public void runJob() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job, parameters);
    }
}
