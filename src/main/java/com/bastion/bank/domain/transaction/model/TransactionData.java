package com.bastion.bank.domain.transaction.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record TransactionData(
        long transactionId,
        long fromAccountId,
        long toAccountId,
        BigDecimal amount,
        String currencyCode,
        Date date,
        TransactionStatus status,
        String message) {
}