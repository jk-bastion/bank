package com.bastion.bank.domain;

import com.bastion.bank.application.dto.TransactionDto;
import com.bastion.bank.domain.exception.AccountNotExistsException;
import com.bastion.bank.domain.exception.InvalidCurrencyException;
import com.bastion.bank.domain.exception.NotEnoughBalanceException;
import com.bastion.bank.domain.model.ErrorsCode;
import com.bastion.bank.domain.model.TransactionStatus;
import com.bastion.bank.infrustructure.repository.AccountRepository;
import com.bastion.bank.infrustructure.repository.TransactionRepository;
import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import com.bastion.bank.infrustructure.repository.model.TransactionEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.bastion.bank.domain.model.ErrorsCode.*;

@Service
@AllArgsConstructor
public class TransactionServer {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void addTransaction(final TransactionDto transactionDto) throws Exception {

        AccountEntity accountFrom = getAndValidateAccount(transactionDto.getFromAccountId(), SRC_ACCOUNT_NOT_EXISTS);
        AccountEntity accountTo = getAndValidateAccount(transactionDto.getToAccountId(), DES_ACCOUNT_NOT_EXISTS);

        if (!(accountFrom.getCurrencyCode().equals(transactionDto.getCurrencyCode()) && accountTo.getCurrencyCode().equals(accountFrom.getCurrencyCode()))) {
            transactionRepository.addTransaction(getTransactionEntity(transactionDto, INVALID_CURRENCY.getMessage(), TransactionStatus.FAILED));
            throw new InvalidCurrencyException(INVALID_CURRENCY.getMessage());
        }

        if (!(accountFrom.getBalance().compareTo(transactionDto.getAmount()) >= 0)) {
            transactionRepository.addTransaction(accountFrom, accountTo, getTransactionEntity(transactionDto, NOT_ENOUGH_BALANCE.getMessage(), TransactionStatus.FAILED));
            throw new NotEnoughBalanceException(NOT_ENOUGH_BALANCE.getMessage());
        }
        TransactionEntity TransactionEntity = transactionRepository.addTransaction(accountFrom, accountTo, getTransactionEntity(transactionDto, "", TransactionStatus.SUCCESS));

        if (TransactionEntity.getTransactionId() == null) {
            if (NOT_ENOUGH_BALANCE.getMessage().equals(TransactionEntity.getMessage())) {
                TransactionEntity.setMessage(UNEXPECTED_ERROR.getMessage());
                TransactionEntity.setStatus(TransactionStatus.FAILED);
            }
            transactionRepository.addTransaction(TransactionEntity);
        }
    }

    private TransactionEntity getTransactionEntity(TransactionDto transactionDto,  String message, TransactionStatus status) {
        return TransactionEntity.builder()
                    .fromAccountId(transactionDto.getFromAccountId())
                    .toAccountId(transactionDto.getToAccountId())
                    .amount(transactionDto.getAmount())
                    .currencyCode(transactionDto.getCurrencyCode())
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

    public List<TransactionDto> getTransactionsForAccount(final Long accountId) throws AccountNotExistsException {
        if (accountRepository.findAccountById(accountId).getAccountId() == 0) {
            throw new AccountNotExistsException(ACCOUNT_NOT_EXISTS.getMessage());
        }

        return transactionRepository.getTransactionsForAccount(accountId)
                .stream()
                .map(TransactionEntity -> TransactionDto.builder()
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
