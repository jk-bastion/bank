package com.bastion.bank.domain.transaction;


import com.bastion.bank.domain.account.model.AccountData;
import com.bastion.bank.domain.transaction.exception.NotEnoughBalanceException;
import com.bastion.bank.domain.transaction.model.TransactionData;

import java.util.List;

public interface TransactionRepository {

    TransactionData addTransaction(AccountData accountFrom, AccountData accountTo, TransactionData transactionData) throws NotEnoughBalanceException;

    void addTransaction(TransactionData transactionData);

    List<TransactionData> getTransactionsForAccount(Long accountId);

}
