package com.bastion.bank.infrustructure.repository;


import com.bastion.bank.domain.account.model.AccountData;
import com.bastion.bank.domain.transaction.TransactionRepository;
import com.bastion.bank.domain.transaction.exception.NotEnoughBalanceException;
import com.bastion.bank.domain.transaction.model.TransactionData;
import com.bastion.bank.infrustructure.mapper.Mapper;
import com.bastion.bank.infrustructure.repository.model.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionRepositoryJpa jpaRepository;
    private final Mapper<TransactionData, TransactionEntity> mapper;

    @Override
    public TransactionData addTransaction(AccountData accountFrom, AccountData accountTo, TransactionData transactionData) throws NotEnoughBalanceException {
//        var transactionEntity = TransactionEntity.builder()
//                .fromAccountId(accountFrom.getAccountId())
//                .toAccountId(accountTo.getAccountId())
//                .amount(transactionData.getAmount())
//                .currencyCode(transactionData.getCurrencyCode())
//                .message(transactionData.getMessage());
//       return jpaRepository.save(transactionEntity);
        return null;
    }

    @Transactional
    public void addTransaction(TransactionData transactionData) {
        jpaRepository.save(mapper.mapToEntity(transactionData));
    }

    @Override
    public List<TransactionData> getTransactionsForAccount(final Long accountId) {
        return jpaRepository.findByFromAccountId(accountId).stream()
                .map(mapper::mapToData)
                .collect(Collectors.toList());
    }
}
