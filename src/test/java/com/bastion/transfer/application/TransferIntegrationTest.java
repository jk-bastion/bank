package com.bastion.transfer.application;

import com.bastion.transfer.domain.account.ManageAccount;
import com.bastion.transfer.domain.account.model.AccountData;
import com.bastion.transfer.domain.transaction.ManageTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

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
    private static final long ACCOUNT_ID = 1l;


    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        var accountFrom = manageAccount.createAccount(getAccountData());
        var accountTo = manageAccount.createAccount(AccountData.builder()
                .email("email2")
                .balance(BALANCE)
                .username("username2")
                .currencyCode(CURRENCY_CODE)
                .build());


    }

    private AccountData getAccountData() {
        return AccountData.builder()
//                .accountId(ACCOUNT_ID)
                .email(EMAIL)
                .balance(BALANCE)
                .username(USERNAME)
                .currencyCode(CURRENCY_CODE)
                .build();
    }
}
