package com.bastion.transfer.application;

import com.bastion.transfer.domain.transaction.ManageTransaction;
import com.bastion.transfer.domain.transaction.model.TransactionData;
import com.bastion.transfer.domain.transaction.model.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManageTransaction manageTransaction;

    private static final String CURRENCY_CODE = "usd";
    private static final long ACCOUNT_ID = 1l;

    @Test
    void shouldCreateTransaction() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/accounts/1/transactions/")
                        .content(
                                """
                                    {
                                    "toAccountId": 2,
                                    "amount": "2",
                                    "currencyCode": "usd",
                                    "message" : "transfer title 1"
                                    }
                                """
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetTransactionsForAccount() throws Exception {

        var date = new Date();
        var transfer_title = "transfer title";
        var amount = new BigDecimal("10");
        given(manageTransaction.getTransactionsForAccount(ACCOUNT_ID)).willReturn(List.of(TransactionData.builder()
                        .transactionId(1l)
                        .toAccountId(2l)
                        .fromAccountId(ACCOUNT_ID)
                        .amount(amount)
                        .message(transfer_title)
                        .currencyCode(CURRENCY_CODE)
                        .status(TransactionStatus.SUCCESS)
                        .date(date)
                .build()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/{accountId}/transactions/", ACCOUNT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].transactionId").value(1l))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fromAccountId").value(ACCOUNT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].toAccountId").value(2l))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].currencyCode").value(CURRENCY_CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].message").value(transfer_title))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].status").value(TransactionStatus.SUCCESS.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].date").isNotEmpty());
    }
}