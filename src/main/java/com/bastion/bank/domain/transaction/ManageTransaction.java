package com.bastion.bank.domain.transaction;

import com.bastion.bank.domain.account.exception.AccountNotExistsException;
import com.bastion.bank.domain.transaction.model.TransactionData;

import java.util.List;

public interface ManageTransaction {
    void addTransaction(TransactionData transactionData) throws Exception;

    List<TransactionData> getTransactionsForAccount(Long accountId) throws AccountNotExistsException;
}
