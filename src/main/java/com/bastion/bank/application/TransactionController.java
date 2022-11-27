package com.bastion.bank.application;

import com.bastion.bank.domain.transaction.ManageTransactionImpl;
import com.bastion.bank.domain.account.exception.AccountNotExistsException;
import com.bastion.bank.domain.transaction.model.TransactionData;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TransactionController {
    private final ManageTransactionImpl transactionServer;

    @PostMapping(value = "/accounts/transactions/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTransaction(@RequestBody TransactionDto transactionDto) throws Exception {
        transactionServer.addTransaction(mapToTransactionData(transactionDto));
    }

    @GetMapping(value = "/accounts/{accountId}/transactions/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionDto> getTransactionsForAccount(@PathVariable("accountId") String accountId) throws AccountNotExistsException {
        return transactionServer.getTransactionsForAccount(Long.parseLong(accountId)).stream()
                .map(this::mapToTransactionDto)
                .collect(Collectors.toList());
    }

    //TODO: better place - mapper interface
    private TransactionData mapToTransactionData(TransactionDto transactionDto) {
        return TransactionData.builder()
                .fromAccountId(transactionDto.fromAccountId)
                .toAccountId(transactionDto.toAccountId)
                .amount(transactionDto.amount)
                .currencyCode(transactionDto.currencyCode)
                .date(transactionDto.date)
                .message(transactionDto.message)
                .build();
    }

    private TransactionDto mapToTransactionDto(TransactionData transactionData) {
        return TransactionDto.builder()
                .fromAccountId(transactionData.fromAccountId())
                .toAccountId(transactionData.toAccountId())
                .amount(transactionData.amount())
                .currencyCode(transactionData.currencyCode())
                .date(transactionData.date())
                .message(transactionData.message())
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class TransactionDto {

        private long transactionId;

        @JsonProperty(required = true)
        private long fromAccountId;

        @JsonProperty(required = true)
        private long toAccountId;

        @JsonProperty(required = true)
        private BigDecimal amount;

        @JsonProperty(required = true)
        private String currencyCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
        private Date date;

        private String status;

        private String message;
    }
}
