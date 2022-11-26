package com.bastion.bank.infrustructure.repository;


import com.bastion.bank.domain.exception.NotEnoughBalanceException;
import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import com.bastion.bank.infrustructure.repository.model.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionRepositoryJpa jpaRepository;

    @Override
    public TransactionEntity addTransaction(AccountEntity accountFrom, AccountEntity accountTo, TransactionEntity transactionDao) throws NotEnoughBalanceException {
        var transactionEntity = TransactionEntity.builder()
                .fromAccountId(accountFrom.getAccountId())
                .toAccountId(accountTo.getAccountId())
                .amount(transactionDao.getAmount())
                .currencyCode(transactionDao.getCurrencyCode())
                .message(transactionDao.getMessage());
//       return jpaRepository.save(transactionEntity);
        return null;
    }

    @Transactional
    public void addTransaction(TransactionEntity TransactionEntity) {
        jpaRepository.save(TransactionEntity);
    }

    @Override
    public List<TransactionEntity> getTransactionsForAccount(final Long accountId) {
       return jpaRepository.findByFromAccountId(accountId);
    }
}
