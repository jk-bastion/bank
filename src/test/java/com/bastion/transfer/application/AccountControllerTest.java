package com.bastion.transfer.application;

import com.bastion.transfer.domain.account.ManageAccount;
import com.bastion.transfer.domain.account.model.AccountData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    private static final String EMAIL = "testemail@mail.com";
    private static final String USERNAME = "testusername";
    private static final BigDecimal BALANCE = new BigDecimal("20");
    private static final String CURRENCY_CODE = "usd";
    private static final long ACCOUNT_ID = 1l;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ManageAccount manageAccount;

    @Test
    void shouldCreateAccount() throws Exception {

        given(manageAccount.createAccount(AccountData.builder()
                .email(EMAIL)
                .balance(BALANCE)
                .username(USERNAME)
                .currencyCode(CURRENCY_CODE)
                .build())).willReturn(getAccountData());

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/accounts/")
                        .content(
                                """
                                          {
                                            "username": "testusername",
                                            "email": "testemail@mail.com",
                                            "balance": 20,
                                            "currencyCode": "usd"
                                          }
                                        """
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(ACCOUNT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(USERNAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(BALANCE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyCode").value(CURRENCY_CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(EMAIL));
    }

    @Test
    void shouldGetAccount() throws Exception {

        given(manageAccount.findAccountById(1l)).willReturn(getAccountData());

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/{accountId}/", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(ACCOUNT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(USERNAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(BALANCE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyCode").value(CURRENCY_CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(EMAIL));
    }

    @Test
    void shouldGetAllAccounts() throws Exception {

        given(manageAccount.getAllAccounts()).willReturn(
                List.of(AccountData.builder()
                                .accountId(1l)
                                .email("testemail@mail.com")
                                .balance(new BigDecimal("20"))
                                .username("testusername")
                                .currencyCode("usd")
                                .build(),

                        AccountData.builder()
                                .accountId(2l)
                                .email("testemail2@mail.com")
                                .balance(new BigDecimal("20"))
                                .username("testusername2")
                                .currencyCode("usd")
                                .build()

                ));

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(ACCOUNT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(USERNAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(BALANCE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyCode").value(CURRENCY_CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(EMAIL))

                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].accountId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].username").value("testusername2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].balance").value("20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].currencyCode").value("usd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].email").value("testemail2@mail.com")

                );
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/accounts/{accountId}/", 1l))
                .andExpect(status().isNoContent());
    }

    private static AccountData getAccountData() {
        AccountData accountData = AccountData.builder()
                .accountId(ACCOUNT_ID)
                .email(EMAIL)
                .balance(BALANCE)
                .username(USERNAME)
                .currencyCode(CURRENCY_CODE)
                .build();
        return accountData;
    }
}