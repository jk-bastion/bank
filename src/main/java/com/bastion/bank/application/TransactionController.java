package com.bastion.bank.application;


import com.bastion.bank.application.dto.TransactionDto;
import com.bastion.bank.domain.TransactionServer;
import com.bastion.bank.domain.exception.AccountNotExistsException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TransactionController {
    private final TransactionServer transactionServer;

    @PostMapping(value = "/account/transaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTransaction(TransactionDto transactionDto) throws Exception {
        transactionServer.addTransaction(transactionDto);
    }

    @GetMapping(value = "/account/{accountId}/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionDto> getTransactionsForAccount(@PathParam("accountId") String accountId) throws AccountNotExistsException {
        return transactionServer.getTransactionsForAccount(Long.parseLong(accountId));
    }
}
