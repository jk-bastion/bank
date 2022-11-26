package com.bastion.bank.infrustructure.mapper;

import com.bastion.bank.domain.transaction.model.TransactionData;
import com.bastion.bank.infrustructure.repository.model.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper implements Mapper<TransactionData, TransactionEntity> {

    @Override
    public TransactionEntity mapToEntity(TransactionData transactionData) {
        if (transactionData == null)
            return null;

        return TransactionEntity.builder()
                .fromAccountId(transactionData.fromAccountId())
                .toAccountId(transactionData.toAccountId())
                .amount(transactionData.amount())
                .currencyCode(transactionData.currencyCode())
                .date(new java.sql.Date(transactionData.date().getTime()))
                .status(transactionData.status())
                .message(transactionData.message())
                .build();
    }

    @Override
    public TransactionData mapToData(TransactionEntity transactionEntity) {
        if (transactionEntity == null)
            return null;
        return TransactionData.builder()
                .fromAccountId(transactionEntity.getFromAccountId())
                .toAccountId(transactionEntity.getToAccountId())
                .amount(transactionEntity.getAmount())
                .currencyCode(transactionEntity.getCurrencyCode())
                .date(transactionEntity.getDate())
                .status((transactionEntity.getStatus()))
                .message(transactionEntity.getMessage())
                .build();
    }
}
