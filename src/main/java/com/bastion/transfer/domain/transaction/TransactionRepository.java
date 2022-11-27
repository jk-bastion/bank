package com.bastion.transfer.domain.transaction;


import com.bastion.transfer.domain.transaction.model.TransactionData;

import java.util.List;

public interface TransactionRepository {

    void addTransaction(TransactionData transactionData);

    List<TransactionData> getTransactionsForAccount(Long accountId);

}
