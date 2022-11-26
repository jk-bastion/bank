package com.bastion.bank.domain.account;

import com.bastion.bank.domain.account.exception.AccountUpdateException;
import com.bastion.bank.domain.account.exception.AccountCreationException;
import com.bastion.bank.domain.account.exception.AccountNotExistsException;
import com.bastion.bank.domain.account.model.AccountData;

import java.util.List;

public interface ManageAccount {
    AccountData createAccount(AccountData accountData) throws AccountCreationException;

    List<AccountData> getAllAccounts();

    AccountData findAccountById(Long accountId) throws AccountNotExistsException;

    void updateAccountBalance(AccountData accountData) throws AccountNotExistsException, AccountUpdateException;

    void deleteAccount(long accountId) throws AccountNotExistsException;
}
