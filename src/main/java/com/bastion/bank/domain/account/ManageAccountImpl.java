package com.bastion.bank.domain.account;

import com.bastion.bank.domain.account.exception.AccountBalanceUpdateException;
import com.bastion.bank.domain.account.exception.AccountCreationException;
import com.bastion.bank.domain.account.exception.AccountNotExistsException;
import com.bastion.bank.domain.account.model.AccountData;
import com.bastion.bank.domain.transaction.model.ErrorsCode;
import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bastion.bank.domain.transaction.model.ErrorsCode.ACCOUNT_NOT_EXISTS;
import static com.bastion.bank.domain.transaction.model.ErrorsCode.ACCOUNT_UPDATE_BALANCE_FAILED;


@Service
@AllArgsConstructor
public class ManageAccountImpl implements ManageAccount {
    private final AccountRepository accountRepository;

    @Override
    public AccountData createAccount(AccountData accountData) throws AccountCreationException {
        try {
            return mapAccountEntityToAccount(accountRepository.createAccount(createAccountEntity(accountData)));
        } catch (Exception ex) {
            throw new AccountCreationException(ErrorsCode.ACCOUNT_CREATION_FAILED.getMessage());
        }
    }

    @Override
    public List<AccountData> getAllAccounts() {
        return accountRepository.getAllAccounts()
                .stream()
                .map(AccountEntity -> mapAccountEntityToAccount(AccountEntity))
                .collect(Collectors.toList());
    }

    @Override
    public AccountData findAccountById(Long accountId) throws AccountNotExistsException {
        var AccountEntity = accountRepository.findAccountById(accountId);
        validateIfAccountExists(AccountEntity);
        return mapAccountEntityToAccount(AccountEntity);
    }

    @Override
    public void updateAccountBalance(AccountData accountData) throws AccountNotExistsException, AccountBalanceUpdateException {
        var accountEntity = accountRepository.findAccountById(accountData.accountId());
        validateIfAccountExists(accountEntity);
        accountEntity.setBalance(accountData.balance());
        try {
            accountRepository.updateAccountBalance(accountEntity);
        } catch (Exception exception) {
            throw new AccountBalanceUpdateException(ACCOUNT_UPDATE_BALANCE_FAILED.getMessage());
        }
    }

    @Override
    public void deleteAccount(long accountId) throws AccountNotExistsException {
        var accountEntity = accountRepository.findAccountById(accountId);
        validateIfAccountExists(accountEntity);
        accountRepository.deleteAccount(accountId);
    }

    private void validateIfAccountExists(AccountEntity accountEntity) throws AccountNotExistsException {
        if (accountEntity.getAccountId() == 0) {
            throw new AccountNotExistsException(ACCOUNT_NOT_EXISTS.getMessage());
        }
    }

    private AccountEntity createAccountEntity(AccountData accountData) {
        return AccountEntity.builder()
                .email(accountData.email())
                .username(accountData.username())
                .currencyCode(accountData.currencyCode())
                .balance(accountData.balance())
                .build();
    }

    private AccountData mapAccountEntityToAccount(AccountEntity accountEntity) {
        return AccountData.builder()
                .accountId(accountEntity.getAccountId())
                .username(accountEntity.getUsername())
                .email(accountEntity.getEmail())
                .balance(accountEntity.getBalance())
                .currencyCode(accountEntity.getCurrencyCode())
                .build();
    }
}
