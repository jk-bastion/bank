package com.bastion.transfer.application;

import com.bastion.transfer.domain.account.ManageAccount;
import com.bastion.transfer.domain.account.model.AccountData;
import com.bastion.transfer.domain.transaction.ManageTransaction;
import com.bastion.transfer.domain.transaction.model.TransactionData;
import com.bastion.transfer.domain.transaction.model.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferIntegrationTest {

    @Autowired
    private ManageAccount manageAccount;
    @Autowired
    private ManageTransaction manageTransaction;

    private static final String EMAIL = "testemail@mail.com";

    private static final String USERNAME = "testusername";
    private static final BigDecimal BALANCE = new BigDecimal("20");
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
        var amount = new BigDecimal("10");

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

    private AccountData getAccountData() {
        return AccountData.builder()
                .email(EMAIL)
                .balance(BALANCE)
                .username(USERNAME)
                .currencyCode(CURRENCY_CODE)
                .build();
    }
}
