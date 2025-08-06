package com.job.lab.batchprocessor.batch;

import com.job.lab.batchprocessor.model.Transaction;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    @Bean
    public FlatFileItemReader<Transaction> transactionReader() {
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionItemReader")
                .resource(new ClassPathResource("data/input/Assessment_Data_Source.txt"))
                .linesToSkip(1)
                .delimited()
                .delimiter("|")
                .names("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId")
                .fieldSetMapper(new TransactionFieldSetMapper()) // <-- use custom mapper
                .build();
    }

    @Bean
    public JpaItemWriter<Transaction> transactionWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Transaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     FlatFileItemReader<Transaction> reader,
                     JpaItemWriter<Transaction> writer) {

        // Create StepBuilder instance
        StepBuilder stepBuilder = new StepBuilder("step-load-transactions", jobRepository);

        // Create chunk-oriented step
        SimpleStepBuilder<Transaction, Transaction> chunkStep = stepBuilder
                .<Transaction, Transaction>chunk(10, transactionManager)
                .reader(reader)
                .writer(writer);

        return chunkStep.build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        JobBuilder jobBuilder = new JobBuilder("job-load-transactions", jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
