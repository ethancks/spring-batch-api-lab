package com.job.lab.batchprocessor.service;

import com.job.lab.batchprocessor.dto.TransactionDTO;
import com.job.lab.batchprocessor.model.Transaction;
import com.job.lab.batchprocessor.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Transaction mockTransaction(Long id, int version) {
        return Transaction.builder()
                .id(id)
                .accountNumber("123456789")
                .trxAmount(new BigDecimal("99.99"))
                .description("Test")
                .trxDate(LocalDate.now())
                .trxTime(LocalTime.of(12, 0))
                .customerId("CUST001")
                .version(version)
                .build();
    }

    @Test
    void testSearchWithFilters() {
        Transaction trx = mockTransaction(1L, 0);
        Page<Transaction> page = new PageImpl<>(List.of(trx));

        when(transactionRepository.search(anyString(), isNull(), isNull(), any(Pageable.class))).thenReturn(page);

        Page<TransactionDTO> result = transactionService.search("CUST", null, null, 0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals("CUST001", result.getContent().get(0).getCustomerId());
    }

    @Test
    void testSearch_withAllParams_sortedByTrxDateDesc() {
        List<Transaction> data = List.of(new Transaction());
        Page<Transaction> page = new PageImpl<>(data);

        Mockito.when(transactionRepository.search(
                        eq("C1"), eq("A1"), eq("Desc"), any(Pageable.class)))
                .thenReturn(page);

        Page<TransactionDTO> result = transactionService.search("C1", "A1", "Desc", 0, 10);

        assertEquals(1, result.getTotalElements());
        verify(transactionRepository).search("C1", "A1", "Desc", PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,"trxDate")));
    }

    @Test
    void testUpdateDescription_Success() {
        Transaction trx = mockTransaction(1L, 0);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(trx));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        TransactionDTO updated = transactionService.updateDescription(1L, "New Desc", 0);

        assertEquals("New Desc", updated.getDescription());
    }

    @Test
    void testUpdateTransaction_notFound() {
        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> transactionService.updateDescription(1L, "Should fail", 1));
    }

    @Test
    void testUpdateDescription_OptimisticLockException() {
        Transaction trx = mockTransaction(1L, 2); // current version is 2
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(trx));

        assertThrows(OptimisticLockException.class, () ->
                transactionService.updateDescription(1L, "New Desc", 1) // stale version
        );
    }
}
