package com.job.lab.batchprocessor;

import com.job.lab.batchprocessor.batch.BatchRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BatchProcessorApplication {
    private static final Logger logger = LoggerFactory.getLogger(BatchProcessorApplication.class);

//    @Autowired
    private BatchRunner jobLauncher;

    @Autowired
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(BatchProcessorApplication.class, args);
    }

    // Run batch job automatically on startup
    @PostConstruct
    public void runJobOnStartup() {
        try {
            logger.debug("🌱 application.properties IS LOADED");
            JobParameters params = new JobParametersBuilder()
                    .addLong("run.id", System.currentTimeMillis()) // to ensure uniqueness
                    .toJobParameters();

            jobLauncher.runJob();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
