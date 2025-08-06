package com.job.lab.batchprocessor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.lab.batchprocessor.dto.TransactionDTO;
import com.job.lab.batchprocessor.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private Job job;

    @MockBean
    private JobLauncher jobLauncher;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionDTO mockDTO;

    @BeforeEach
    void setup() {
        mockDTO = TransactionDTO.builder()
                .id(1L)
                .accountNumber("1234567890")
                .trxAmount(new BigDecimal("99.99"))
                .description("Mock Desc")
                .trxDate(LocalDate.of(2025, 8, 6))
                .trxTime(LocalTime.of(10, 0))
                .customerId("CUST001")
                .version(0)
                .build();
    }

    @Test
    void testSearchTransactions() throws Exception {
        Page<TransactionDTO> page = new PageImpl<>(List.of(mockDTO));
        Mockito.when(transactionService.search(isNull(), isNull(),isNull(), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/transactions")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].accountNumber", is("1234567890")))
                .andExpect(jsonPath("$.content.length()", is(1)));
    }

    @Test
    void testUpdateDescription() throws Exception {
        mockDTO.setDescription("Updated Desc");
        Mockito.when(transactionService.updateDescription(eq(1L), eq("Updated Desc"), eq(0)))
                .thenReturn(mockDTO);

        mockMvc.perform(put("/transactions/1")
                        .param("description", "Updated Desc")
                        .param("version", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Updated Desc")));
    }
}
