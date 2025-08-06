package com.job.lab.batchprocessor.controller;

import com.job.lab.batchprocessor.dto.TransactionDTO;
import com.job.lab.batchprocessor.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping
    public ResponseEntity<Page<TransactionDTO>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String description) {

        Page<TransactionDTO> results = service.search(customerId, accountNumber, description, page, size);
        return ResponseEntity.ok(results);
    }

    @PutMapping("/{id}")
    public TransactionDTO updateDescription(
            @PathVariable Long id,
            @RequestParam String description,
            @RequestParam Integer version
    ) {
        return service.updateDescription(id, description, version);
    }
}
