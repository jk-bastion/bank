package com.bastion.transfer.domain.transaction;

import com.bastion.transfer.domain.account.AccountRepository;
import com.bastion.transfer.domain.account.exception.AccountNotExistsException;
import com.bastion.transfer.domain.account.model.AccountData;
import com.bastion.transfer.domain.transaction.exception.InvalidCurrencyException;
import com.bastion.transfer.domain.transaction.exception.NotEnoughBalanceException;
import com.bastion.transfer.domain.transaction.model.TransactionData;
import com.bastion.transfer.domain.transaction.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static com.bastion.transfer.domain.transaction.model.ErrorsCode.*;

@Slf4j
@Service
@AllArgsConstructor
public class ManageTransactionImpl implements ManageTransaction {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void addTransaction(final TransactionData transactionData) throws Exception {
        var accountFrom = getAccountData(transactionData.fromAccountId(), transactionData);
        var accountTo = getAccountData(transactionData.toAccountId(), transactionData);

        validateCurrencyCode(transactionData, accountFrom, accountTo);
        validateBalance(transactionData, accountFrom);

        accountRepository.updateAccount(updateAccountData(accountFrom, accountFrom.balance().subtract(transactionData.amount())));
        accountRepository.updateAccount(updateAccountData(accountTo, accountTo.balance().add(transactionData.amount())));
        Thread.sleep(10000);
        transactionRepository.addTransaction(updateTransactionData(transactionData, TransactionStatus.SUCCESS));
    }

    private static void validateBalance(TransactionData transactionData, AccountData accountFrom) throws NotEnoughBalanceException {
        if (!(accountFrom.balance().compareTo(transactionData.amount()) >= 0)) {
            log.warn("Transaction={} rejected, reason={}", transactionData, NOT_ENOUGH_BALANCE.getMessage());
            throw new NotEnoughBalanceException(NOT_ENOUGH_BALANCE.getMessage());
        }
    }

    private static void validateCurrencyCode(TransactionData transactionData, AccountData accountFrom, AccountData accountTo) throws InvalidCurrencyException {
        if (!(accountFrom.currencyCode().equals(transactionData.currencyCode())
                && accountTo.currencyCode().equals(transactionData.currencyCode()))) {
            log.warn("Transaction={} rejected, reason={}", transactionData, INVALID_CURRENCY.getMessage());
            throw new InvalidCurrencyException(INVALID_CURRENCY.getMessage());
        }
    }

    private AccountData getAccountData(long accountId, TransactionData transactionData) throws AccountNotExistsException {
        var accountFrom = accountRepository.findAccountById(accountId);
        if (accountFrom.accountId() == 0) {
            log.warn("Transaction={} rejected, reason={}", transactionData, ACCOUNT_NOT_EXISTS.getMessage());
            throw new AccountNotExistsException(ACCOUNT_NOT_EXISTS.getMessage());
        }
        return accountFrom;
    }

    private AccountData updateAccountData(AccountData accountData, BigDecimal balance) {
        return AccountData.builder()
                .accountId(accountData.accountId())
                .email(accountData.email())
                .username(accountData.username())
                .balance(balance)
                .currencyCode(accountData.currencyCode())
                .build();
    }

    private TransactionData updateTransactionData(TransactionData transactionData, TransactionStatus status) {
        return TransactionData.builder()
                .fromAccountId(transactionData.fromAccountId())
                .toAccountId(transactionData.toAccountId())
                .amount(transactionData.amount())
                .currencyCode(transactionData.currencyCode())
                .message(transactionData.message())
                .date(new Date(Instant.now().toEpochMilli()))
                .status(status)
                .build();
    }

    @Override
    public List<TransactionData> getTransactionsForAccount(final Long accountId) throws AccountNotExistsException {

        if (accountRepository.findAccountById(accountId).accountId() == 0) {
            throw new AccountNotExistsException(ACCOUNT_NOT_EXISTS.getMessage());
        }

        return transactionRepository.getTransactionsForAccount(accountId);
    }
}
