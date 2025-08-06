package com.job.lab.batchprocessor.batch;

import com.job.lab.batchprocessor.model.Transaction;
import com.job.lab.batchprocessor.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
public class BatchJobIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private Job job;

    @Test
    public void testBatchJobLoadsTransactionsFromFile() throws Exception {
        // When: Run the batch job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then: Job should complete successfully
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        // And: The DB should contain the expected number of rows
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).isNotEmpty();

        // Optional: Check one field from first record
        Transaction first = transactions.get(0);
        assertThat(first.getCustomerId()).isNotBlank();
    }
}
