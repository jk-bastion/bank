package com.bastion.transfer.domain.account;

import com.bastion.transfer.domain.account.exception.AccountUpdateException;
import com.bastion.transfer.domain.account.model.AccountData;

import java.util.List;

public interface AccountRepository {

    AccountData createAccount(AccountData accountData);
    List<AccountData> getAllAccounts();
    AccountData findAccountById(Long accountId);
    void deleteAccount(Long accountId);
    void updateAccount(AccountData accountData) throws AccountUpdateException;
}
