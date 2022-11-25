package com.bastion.bank.infrustructure.repository;


import com.bastion.bank.domain.exception.NotEnoughBalanceException;
import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import com.bastion.bank.infrustructure.repository.model.TransactionEntity;

import java.util.List;

public interface TransactionRepository {

    TransactionEntity addTransaction(AccountEntity accountFrom, AccountEntity accountTo, TransactionEntity TransactionEntity) throws NotEnoughBalanceException;

    void addTransaction(TransactionEntity TransactionEntity);

    List<TransactionEntity> getTransactionsForAccount(Long accountId);

}
