package com.bastion.transfer.application;

import com.bastion.transfer.domain.transaction.ManageTransaction;
import com.bastion.transfer.domain.account.exception.AccountNotExistsException;
import com.bastion.transfer.domain.transaction.model.TransactionData;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TransactionController {
    private final ManageTransaction manageTransaction;

    @PostMapping(value = "/accounts/{accountId}/transactions/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addTransaction(@PathVariable("accountId") Long accountId, @RequestBody TransactionDto transactionDto) throws Exception {
        log.info("Create new transaction {} for accountId=", transactionDto, accountId);
        manageTransaction.addTransaction(mapToTransactionData(transactionDto, accountId));
    }

    @GetMapping(value = "/accounts/{accountId}/transactions/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionDto> getTransactionsForAccount(@PathVariable("accountId") Long accountId) throws AccountNotExistsException {
        log.info("List all transactions");
        return manageTransaction.getTransactionsForAccount(accountId).stream()
                .map(this::mapToTransactionDto)
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class TransactionDto {

        private long transactionId;

        private long fromAccountId;

        @JsonProperty(required = true)
        private long toAccountId;

        @JsonProperty(required = true)
        private BigDecimal amount;

        @JsonProperty(required = true)
        private String currencyCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private Date date;

        private String status;

        private String message;
    }

    private TransactionData mapToTransactionData(TransactionDto transactionDto, long fromAccountId) {
        return TransactionData.builder()
                .fromAccountId(fromAccountId)
                .toAccountId(transactionDto.toAccountId)
                .amount(transactionDto.amount)
                .currencyCode(transactionDto.currencyCode)
                .date(transactionDto.date)
                .message(transactionDto.message)
                .build();
    }

    private TransactionDto mapToTransactionDto(TransactionData transactionData) {
        return TransactionDto.builder()
                .transactionId(transactionData.transactionId())
                .fromAccountId(transactionData.fromAccountId())
                .toAccountId(transactionData.toAccountId())
                .amount(transactionData.amount())
                .currencyCode(transactionData.currencyCode())
                .date(transactionData.date())
                .status(transactionData.status().name())
                .message(transactionData.message())
                .build();
    }
}
