package com.bastion.bank.domain.transaction;

import com.bastion.bank.domain.account.AccountRepository;
import com.bastion.bank.domain.account.exception.AccountNotExistsException;
import com.bastion.bank.domain.transaction.exception.InvalidCurrencyException;
import com.bastion.bank.domain.transaction.exception.NotEnoughBalanceException;
import com.bastion.bank.domain.transaction.model.ErrorsCode;
import com.bastion.bank.domain.transaction.model.TransactionData;
import com.bastion.bank.domain.transaction.model.TransactionStatus;
import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import com.bastion.bank.infrustructure.repository.model.TransactionEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.bastion.bank.domain.transaction.model.ErrorsCode.*;

@Service
@AllArgsConstructor
public class ManageTransactionImpl implements ManageTransaction {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public void addTransaction(final TransactionData transactionData) throws Exception {

        AccountEntity accountFrom = getAndValidateAccount(transactionData.fromAccountId(), SRC_ACCOUNT_NOT_EXISTS);
        AccountEntity accountTo = getAndValidateAccount(transactionData.toAccountId(), DES_ACCOUNT_NOT_EXISTS);

        if (!(accountFrom.getCurrencyCode().equals(transactionData.currencyCode()) && accountTo.getCurrencyCode().equals(accountFrom.getCurrencyCode()))) {
            transactionRepository.addTransaction(getTransactionEntity(transactionData, INVALID_CURRENCY.getMessage(), TransactionStatus.FAILED));
            throw new InvalidCurrencyException(INVALID_CURRENCY.getMessage());
        }

        if (!(accountFrom.getBalance().compareTo(transactionData.amount()) >= 0)) {
            transactionRepository.addTransaction(accountFrom, accountTo, getTransactionEntity(transactionData, NOT_ENOUGH_BALANCE.getMessage(), TransactionStatus.FAILED));
            throw new NotEnoughBalanceException(NOT_ENOUGH_BALANCE.getMessage());
        }
        TransactionEntity TransactionEntity = transactionRepository.addTransaction(accountFrom, accountTo, getTransactionEntity(transactionData, "", TransactionStatus.SUCCESS));

        if (TransactionEntity.getTransactionId() == null) {
            if (NOT_ENOUGH_BALANCE.getMessage().equals(TransactionEntity.getMessage())) {
                TransactionEntity.setMessage(UNEXPECTED_ERROR.getMessage());
                TransactionEntity.setStatus(TransactionStatus.FAILED);
            }
            transactionRepository.addTransaction(TransactionEntity);
        }
    }

    private TransactionEntity getTransactionEntity(TransactionData transactionDto,  String message, TransactionStatus status) {
        return TransactionEntity.builder()
                    .fromAccountId(transactionDto.fromAccountId())
                    .toAccountId(transactionDto.toAccountId())
                    .amount(transactionDto.amount())
                    .currencyCode(transactionDto.currencyCode())
                    .message(message)
                    .date(new Date(Instant.now().toEpochMilli()))
                    .status(status)
                    .build();
    }

    private AccountEntity getAndValidateAccount(final Long accountId, final ErrorsCode errorsCode) throws AccountNotExistsException {
        AccountEntity accountFrom = accountRepository.findAccountById(accountId);
        if (accountFrom.getAccountId() == 0) {
            throw new AccountNotExistsException(errorsCode.getMessage());
        }
        return accountFrom;
    }

    @Override
    public List<TransactionData> getTransactionsForAccount(final Long accountId) throws AccountNotExistsException {
        if (accountRepository.findAccountById(accountId).getAccountId() == 0) {
            throw new AccountNotExistsException(ACCOUNT_NOT_EXISTS.getMessage());
        }

        return transactionRepository.getTransactionsForAccount(accountId)
                .stream()
                .map(TransactionEntity -> TransactionData.builder()
                                                    .transactionId(TransactionEntity.getTransactionId())
                                                    .fromAccountId(TransactionEntity.getFromAccountId())
                                                    .toAccountId(TransactionEntity.getToAccountId())
                                                    .amount(TransactionEntity.getAmount())
                                                    .currencyCode(TransactionEntity.getCurrencyCode())
                                                    .status(TransactionEntity.getStatus().name())
                                                    .date(TransactionEntity.getDate())
                                                    .message(TransactionEntity.getMessage())
                                                    .build())
                .collect(Collectors.toList());
    }
}
