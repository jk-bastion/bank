package com.bastion.bank.infrustructure.repository;

import com.bastion.bank.domain.exception.AccountBalanceUpdateException;
import com.bastion.bank.infrustructure.repository.model.AccountEntity;

import java.util.List;

public interface AccountRepository {

    AccountEntity createAccount(AccountEntity accountDao);
    List<AccountEntity> getAllAccounts();
    AccountEntity findAccountById(Long accountId);
    void deleteAccount(Long accountId);
    void updateAccountBalance(AccountEntity accountDao) throws AccountBalanceUpdateException;
}
