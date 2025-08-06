package com.job.lab.jpaconcurrencytest;

import com.job.lab.batchprocessor.model.Transaction;
import com.job.lab.batchprocessor.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ContextConfiguration(classes = TestJpaConfig.class)
public class TransactionConcurrencyTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testOptimisticLockingConflict() {
        // 1. Create and save entity
        Transaction tx = new Transaction();
        tx.setCustomerId("C1");
        tx.setAccountNumber("A1");
        tx.setDescription("Initial");
        transactionRepository.saveAndFlush(tx);

        // 2. Load in two detached objects (simulate 2 users)
        Transaction t1 = entityManager.find(Transaction.class, tx.getId());
        entityManager.detach(t1);

        Transaction t2 = entityManager.find(Transaction.class, tx.getId());
        entityManager.detach(t2);

        // 3. First user updates
        t1.setDescription("Updated by User 1");
        transactionRepository.saveAndFlush(t1); // version++

        // 4. Second user tries to update stale version
        t2.setDescription("Updated by User 2");

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            transactionRepository.saveAndFlush(t2);
        });
    }

}
