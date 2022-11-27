package com.bastion.bank.domain.account;

import com.bastion.bank.domain.account.exception.AccountUpdateException;
import com.bastion.bank.domain.account.exception.AccountCreationException;
import com.bastion.bank.domain.account.exception.AccountNotExistsException;
import com.bastion.bank.domain.account.model.AccountData;
import com.bastion.bank.domain.transaction.model.ErrorsCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bastion.bank.domain.transaction.model.ErrorsCode.ACCOUNT_NOT_EXISTS;
import static com.bastion.bank.domain.transaction.model.ErrorsCode.ACCOUNT_UPDATE_FAILED;


@Service
@AllArgsConstructor
public class ManageAccountImpl implements ManageAccount {
    private final AccountRepository accountRepository;

    @Override
    public AccountData createAccount(AccountData accountData) throws AccountCreationException {
        try {
            return accountRepository.createAccount(accountData);
        } catch (Exception ex) {
            throw new AccountCreationException(ErrorsCode.ACCOUNT_CREATION_FAILED.getMessage());
        }
    }

    @Override
    public List<AccountData> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    @Override
    public AccountData findAccountById(Long accountId) throws AccountNotExistsException {
        var accountData = accountRepository.findAccountById(accountId);
        validateIfAccountExists(accountData);
        return accountData;
    }

    @Override
    public void updateAccountBalance(AccountData accountData) throws AccountNotExistsException, AccountUpdateException {
        try {
            accountRepository.updateAccount(accountData);
        } catch (Exception exception) {
            throw new AccountUpdateException(ACCOUNT_UPDATE_FAILED.getMessage());
        }
    }

    @Override
    public void deleteAccount(long accountId) throws AccountNotExistsException {
        var accountData = accountRepository.findAccountById(accountId);
        validateIfAccountExists(accountData);
        accountRepository.deleteAccount(accountId);
    }

    private void validateIfAccountExists(AccountData accountData) throws AccountNotExistsException {
        if (accountData.accountId() == 0) {
            throw new AccountNotExistsException(ACCOUNT_NOT_EXISTS.getMessage());
        }
    }
}
