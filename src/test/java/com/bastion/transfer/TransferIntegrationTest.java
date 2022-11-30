package com.bastion.transfer;

import com.bastion.transfer.domain.account.ManageAccount;
import com.bastion.transfer.domain.account.model.AccountData;
import com.bastion.transfer.domain.transaction.ManageTransaction;
import com.bastion.transfer.domain.transaction.exception.NotEnoughBalanceException;
import com.bastion.transfer.domain.transaction.model.ErrorsCode;
import com.bastion.transfer.domain.transaction.model.TransactionData;
import com.bastion.transfer.domain.transaction.model.TransactionStatus;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransferIntegrationTest {

    @Autowired
    private ManageAccount manageAccount;
    @Autowired
    private ManageTransaction manageTransaction;

    private static final String EMAIL = "testemail@mail.com";

    private static final String USERNAME = "testusername";
    private static final BigDecimal BALANCE = new BigDecimal("20.00");
    private static final String CURRENCY_CODE = "usd";

    @Test
    void shouldTransferMoneyFromOneAccountToAnother() throws Exception {
        // given
        var accountFrom = manageAccount.createAccount(getAccountData());
        var accountTo = manageAccount.createAccount(AccountData.builder()
                .email("email2")
                .balance(BALANCE)
                .username("username2")
                .currencyCode(CURRENCY_CODE)
                .build());
        var amount = new BigDecimal("10.00");

        // when
        manageTransaction.addTransaction(TransactionData.builder()
                .fromAccountId(accountFrom.accountId())
                .toAccountId(accountTo.accountId())
                .message("message")
                .currencyCode(CURRENCY_CODE)
                .amount(amount)
                .build());

        // then
        var transactionsForAccount = manageTransaction.getTransactionsForAccount(accountFrom.accountId());
        assertThat(transactionsForAccount).hasSize(1);
        assertThat(transactionsForAccount.get(0).transactionId()).isNotNull();
        assertThat(transactionsForAccount.get(0).fromAccountId()).isEqualTo(accountFrom.accountId());
        assertThat(transactionsForAccount.get(0).toAccountId()).isEqualTo(accountTo.accountId());
        assertThat(transactionsForAccount.get(0).message()).isEqualTo("message");
        assertThat(transactionsForAccount.get(0).status()).isEqualTo(TransactionStatus.SUCCESS);
        assertThat(transactionsForAccount.get(0).currencyCode()).isEqualTo(CURRENCY_CODE);
        assertThat(transactionsForAccount.get(0).date()).isNotNull();

        assertThat(manageAccount.findAccountById(accountFrom.accountId()).balance()).isEqualTo(BALANCE.subtract(amount));
        assertThat(manageAccount.findAccountById(accountTo.accountId()).balance()).isEqualTo(BALANCE.add(amount));
    }

    @Test
    void transactionShouldFailedWhenNotEnoughMoneyToTransfer() throws Exception {
        // given
        var accountFrom = manageAccount.createAccount(getAccountData());
        var accountTo = manageAccount.createAccount(AccountData.builder()
                .email("email2")
                .balance(BALANCE)
                .username("username2")
                .currencyCode(CURRENCY_CODE)
                .build());
        var amount = BALANCE.add(BigDecimal.ONE);

        // when &&  then
        var statusRuntimeException = assertThrows(NotEnoughBalanceException.class, () ->
                manageTransaction.addTransaction(TransactionData.builder()
                        .fromAccountId(accountFrom.accountId())
                        .toAccountId(accountTo.accountId())
                        .message("message")
                        .currencyCode(CURRENCY_CODE)
                        .amount(amount)
                        .build()));
        Assertions.assertThat(statusRuntimeException.getMessage()).isEqualTo(ErrorsCode.NOT_ENOUGH_BALANCE.getMessage());

        assertThat(manageAccount.findAccountById(accountFrom.accountId()).balance()).isEqualTo(BALANCE);
        assertThat(manageAccount.findAccountById(accountTo.accountId()).balance()).isEqualTo(BALANCE);
    }

    @Test
    void transactionShouldFailedWhenTwoTransactionHapendedAtTheSameTime() throws Exception {
        // given
        var accountFrom = manageAccount.createAccount(getAccountData());
        var accountTo = manageAccount.createAccount(AccountData.builder()
                .email("email2")
                .balance(BALANCE)
                .username("username2")
                .currencyCode(CURRENCY_CODE)
                .build());
        var amount = BALANCE;

        // when &&  then
        var executorService = Executors.newFixedThreadPool(2);
        Callable<Void> callable1 = () -> {
            manageTransaction.addTransaction(TransactionData.builder()
                    .fromAccountId(accountFrom.accountId())
                    .toAccountId(accountTo.accountId())
                    .message("message 1")
                    .currencyCode(CURRENCY_CODE)
                    .amount(amount)
                    .build());
            return null;
        };

        Callable<Void> callable2 = () -> {
            manageTransaction.addTransaction(TransactionData.builder()
                    .fromAccountId(accountFrom.accountId())
                    .toAccountId(accountTo.accountId())
                    .message("message 2")
                    .currencyCode(CURRENCY_CODE)
                    .amount(amount)
                    .build());
            return null;
        };

        try {

            List<Future<Void>> futures = executorService.invokeAll(List.of(callable1, callable2));
                 futures.get(0);
                 futures.get(1);

        }   catch (Exception e) {
            Assert.fail();
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        assertThat(manageAccount.findAccountById(accountFrom.accountId()).balance()).isEqualTo(BALANCE.subtract(amount));
        assertThat(manageAccount.findAccountById(accountTo.accountId()).balance()).isEqualTo(BALANCE.add(amount));
    }

    private AccountData getAccountData() {
        return AccountData.builder()
                .email(EMAIL)
                .balance(BALANCE)
                .username(USERNAME)
                .currencyCode(CURRENCY_CODE)
                .build();
    }
}
