package com.job.lab.batchprocessor.repository;

import com.job.lab.batchprocessor.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
    SELECT t FROM Transaction t
    WHERE (:customerId IS NULL OR LOWER(t.customerId) LIKE LOWER(CONCAT('%', :customerId, '%')))
      AND (:accountNumber IS NULL OR LOWER(t.accountNumber) LIKE LOWER(CONCAT('%', :accountNumber, '%')))
      AND (:description IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%')))
    """)
    Page<Transaction> search(
            @Param("customerId") String customerId,
            @Param("accountNumber") String accountNumber,
            @Param("description") String description,
            Pageable pageable
    );
}
