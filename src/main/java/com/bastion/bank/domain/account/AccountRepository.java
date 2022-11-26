package com.bastion.bank.domain.account;

import com.bastion.bank.domain.account.exception.AccountBalanceUpdateException;
import com.bastion.bank.domain.account.model.AccountData;

import java.util.List;

public interface AccountRepository {

    AccountData createAccount(AccountData accountData);
    List<AccountData> getAllAccounts();
    AccountData findAccountById(Long accountId);
    void deleteAccount(Long accountId);
    void updateAccountBalance(AccountData accountDao) throws AccountBalanceUpdateException;
}