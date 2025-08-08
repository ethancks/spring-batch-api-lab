package com.job.lab.batchprocessor.service;

import com.job.lab.batchprocessor.dto.TransactionDTO;
import com.job.lab.batchprocessor.model.Transaction;
import com.job.lab.batchprocessor.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public Page<TransactionDTO> search(String customerId, String accountNumber, String description, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("trxDate").descending());

        Page<Transaction> results = repository.search(customerId,accountNumber,description,pageable);

        return results.map(this::toDTO);
    }

    public TransactionDTO updateDescription(Long id, String description, Integer version) {
        Transaction entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        if (!entity.getVersion().equals(version)) {
            throw new OptimisticLockException("Record was modified by another transaction");
        }

        entity.setDescription(description);
        return toDTO(repository.save(entity));
    }

    private TransactionDTO toDTO(Transaction t) {
        return TransactionDTO.builder()
                .id(t.getId())
                .accountNumber(t.getAccountNumber())
                .trxAmount(t.getTrxAmount())
                .description(t.getDescription())
                .trxDate(t.getTrxDate())
                .trxTime(t.getTrxTime())
                .customerId(t.getCustomerId())
                .version(t.getVersion())
                .build();
    }
}
