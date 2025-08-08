package com.job.lab.batchprocessor;

import com.job.lab.batchprocessor.batch.BatchRunner;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProcessorApplication {
    private static final Logger logger = LoggerFactory.getLogger(BatchProcessorApplication.class);

    // Wrapped the JobLauncher to BatchRunner, otherwise the @WebMvcTest in JUnit would fail as they
    // don't load batch infrastructure beans
    private BatchRunner batchRunner;

    public static void main(String[] args) {
        SpringApplication.run(BatchProcessorApplication.class, args);
    }

    // Run batch job automatically on startup
    @PostConstruct
    public void runJobOnStartup() {
        try {
            logger.debug("Starting application...");
            batchRunner.runJob();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
