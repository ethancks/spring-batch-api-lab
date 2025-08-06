package com.job.lab.batchprocessor.batch;

import com.job.lab.batchprocessor.model.Transaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionFieldSetMapper implements FieldSetMapper<Transaction> {

    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
        return Transaction.builder()
                .accountNumber(fieldSet.readString("accountNumber"))
                .trxAmount(fieldSet.readBigDecimal("trxAmount"))
                .description(fieldSet.readString("description"))
                .trxDate(LocalDate.parse(fieldSet.readString("trxDate")))     // yyyy-MM-dd
                .trxTime(LocalTime.parse(fieldSet.readString("trxTime")))     // HH:mm:ss
                .customerId(fieldSet.readString("customerId"))
                .build();
    }
}
