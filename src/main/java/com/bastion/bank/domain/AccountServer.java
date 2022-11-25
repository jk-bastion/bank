package com.bastion.bank.domain;

import com.bastion.bank.application.dto.AccountDto;
import com.bastion.bank.domain.exception.AccountBalanceUpdateException;
import com.bastion.bank.domain.exception.AccountCreationException;
import com.bastion.bank.domain.exception.AccountNotExistsException;
import com.bastion.bank.domain.model.ErrorsCode;
import com.bastion.bank.infrustructure.repository.AccountRepository;
import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bastion.bank.domain.model.ErrorsCode.ACCOUNT_NOT_EXISTS;
import static com.bastion.bank.domain.model.ErrorsCode.ACCOUNT_UPDATE_BALANCE_FAILED;


@Service
@AllArgsConstructor
public class AccountServer {

    private final AccountRepository accountRepository;


    public AccountDto createAccount(AccountDto accountDto) throws AccountCreationException {
        try {
            return mapAccountEntityToAccountDto(accountRepository.createAccount(createAccountEntity(accountDto)));
        } catch (Exception ex) {
            throw new AccountCreationException(ErrorsCode.ACCOUNT_CREATION_FAILED.getMessage());
        }
    }

    public List<AccountDto> getAllAccounts() {
        return accountRepository.getAllAccounts()
                .stream()
                .map(AccountEntity -> mapAccountEntityToAccountDto(AccountEntity))
                .collect(Collectors.toList());
    }

    public AccountDto findAccountById(Long accountId) throws AccountNotExistsException {
        AccountEntity AccountEntity = accountRepository.findAccountById(accountId);
        validateIfAccountExists(AccountEntity);
        return mapAccountEntityToAccountDto(AccountEntity);
    }

    public void updateAccountBalance(AccountDto accountDto) throws AccountNotExistsException, AccountBalanceUpdateException {
        AccountEntity AccountEntity = accountRepository.findAccountById(accountDto.getAccountId());
        validateIfAccountExists(AccountEntity);
        AccountEntity.setBalance(accountDto.getBalance());
        try {
            accountRepository.updateAccountBalance(AccountEntity);
        } catch (Exception exception) {
            throw new AccountBalanceUpdateException(ACCOUNT_UPDATE_BALANCE_FAILED.getMessage());
        }
    }

    public void deleteAccount(long accountId) throws AccountNotExistsException {
        AccountEntity AccountEntity = accountRepository.findAccountById(accountId);
        validateIfAccountExists(AccountEntity);
        accountRepository.deleteAccount(accountId);
    }

    private void validateIfAccountExists(AccountEntity AccountEntity) throws AccountNotExistsException {
        if (AccountEntity.getAccountId() == 0) {
            throw new AccountNotExistsException(ACCOUNT_NOT_EXISTS.getMessage());
        }
    }

    private AccountEntity createAccountEntity(AccountDto accountDto) {
        return AccountEntity.builder()
                .email(accountDto.getEmail())
                .username(accountDto.getUsername())
                .currencyCode(accountDto.getCurrencyCode())
                .balance(accountDto.getBalance())
                .build();
    }

    private AccountDto mapAccountEntityToAccountDto(AccountEntity AccountEntity) {
        return AccountDto.builder()
                .accountId(AccountEntity.getAccountId())
                .username(AccountEntity.getUsername())
                .email(AccountEntity.getEmail())
                .balance(AccountEntity.getBalance())
                .currencyCode(AccountEntity.getCurrencyCode())
                .build();
    }
}
