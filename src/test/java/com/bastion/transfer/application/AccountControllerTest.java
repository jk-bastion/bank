package com.bastion.transfer.application;

import com.bastion.transfer.domain.account.ManageAccount;
import com.bastion.transfer.domain.account.exception.AccountNotExistsException;
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

import static com.bastion.transfer.domain.transaction.model.ErrorsCode.ACCOUNT_NOT_EXISTS;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManageAccount manageAccount;

    private static final String EMAIL = "testemail@mail.com";

    private static final String USERNAME = "testusername";
    private static final BigDecimal BALANCE = new BigDecimal("20");
    private static final String CURRENCY_CODE = "usd";
    private static final long ACCOUNT_ID = 1l;

    @Test
    void shouldCreateAccount() throws Exception {

        given(manageAccount.createAccount(AccountData.builder()
                .email(EMAIL)
                .balance(BALANCE)
                .username(USERNAME)
                .currencyCode(CURRENCY_CODE)
                .build())).willReturn(getAccountData());

        mockMvc.perform(MockMvcRequestBuilders
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
    void shouldUpdateAccount() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/accounts/")
                        .content(
                                """
                                        {
                                            "accountId" : 1,
                                            "username": "testusername",
                                            "email": "testemail@mail.com",
                                            "balance": 20,
                                            "currencyCode": "usd"
                                          }
                                        """
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAccount() throws Exception {

        given(manageAccount.findAccountById(ACCOUNT_ID)).willReturn(getAccountData());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/{accountId}/", ACCOUNT_ID))
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
                List.of(getAccountData(),
                        AccountData.builder()
                                .accountId(2l)
                                .email("testemail2@mail.com")
                                .balance(new BigDecimal("20"))
                                .username("testusername2")
                                .currencyCode("usd")
                                .build()

                ));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].accountId").value(ACCOUNT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].username").value(USERNAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].balance").value(BALANCE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].currencyCode").value(CURRENCY_CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value(EMAIL))

                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].accountId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].username").value("testusername2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].balance").value("20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].currencyCode").value("usd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].email").value("testemail2@mail.com")
                );
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/accounts/{accountId}/", ACCOUNT_ID))
                .andExpect(status().isNoContent());
    }

    private AccountData getAccountData() {
        return AccountData.builder()
                .accountId(ACCOUNT_ID)
                .email(EMAIL)
                .balance(BALANCE)
                .username(USERNAME)
                .currencyCode(CURRENCY_CODE)
                .build();
    }
}