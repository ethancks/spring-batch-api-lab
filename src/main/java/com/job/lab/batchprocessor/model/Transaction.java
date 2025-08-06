package com.job.lab.batchprocessor.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // These fields must match the column names in the input file (and reader tokenizer)
    private String accountNumber;

    private BigDecimal trxAmount;

    private String description;

    private LocalDate trxDate;

    private LocalTime trxTime;

    private String customerId;

    // Enables optimistic locking for the update API
    @Version
    private Integer version;
}
