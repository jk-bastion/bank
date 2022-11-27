package com.bastion.transfer.domain.transaction;

import com.bastion.transfer.domain.account.exception.AccountNotExistsException;
import com.bastion.transfer.domain.transaction.model.TransactionData;

import java.util.List;

public interface ManageTransaction {
    void addTransaction(TransactionData transactionData) throws Exception;

    List<TransactionData> getTransactionsForAccount(Long accountId) throws AccountNotExistsException;
}
