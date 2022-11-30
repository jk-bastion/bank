package com.bastion.transfer.domain.account;

import com.bastion.transfer.domain.account.exception.AccountUpdateException;
import com.bastion.transfer.domain.account.exception.AccountCreationException;
import com.bastion.transfer.domain.account.exception.AccountNotExistsException;
import com.bastion.transfer.domain.account.model.AccountData;
import com.bastion.transfer.domain.transaction.model.ErrorsCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bastion.transfer.domain.transaction.model.ErrorsCode.ACCOUNT_NOT_EXISTS;
import static com.bastion.transfer.domain.transaction.model.ErrorsCode.ACCOUNT_UPDATE_FAILED;

@Slf4j
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
    public void updateAccountBalance(AccountData accountData) throws AccountUpdateException {
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
