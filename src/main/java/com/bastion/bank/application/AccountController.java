package com.bastion.bank.application;


import com.bastion.bank.application.dto.AccountDto;
import com.bastion.bank.domain.AccountServer;
import com.bastion.bank.domain.exception.AccountBalanceUpdateException;
import com.bastion.bank.domain.exception.AccountCreationException;
import com.bastion.bank.domain.exception.AccountNotExistsException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AccountController {

//    TODO: review REST API
    private final AccountServer accountServer;

    @PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(AccountDto accountDto) throws AccountCreationException {
        accountServer.createAccount(accountDto);
    }

    @PutMapping(value = "/accounts/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccount(AccountDto accountDto) throws AccountNotExistsException, AccountBalanceUpdateException {
        accountServer.updateAccountBalance(accountDto);
    }

    @GetMapping(value = "/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDto getAccount(@PathParam("accountId") String accountId) throws AccountNotExistsException {
        return accountServer.findAccountById(Long.parseLong(accountId));
    }

    @GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountDto> getAllAccounts() {
        return accountServer.getAllAccounts();
    }

    @DeleteMapping("/accounts/{accountId}")
    public void deleteAccount(@PathParam("accountId") String accountId) throws AccountNotExistsException {
        accountServer.deleteAccount(Long.parseLong(accountId));
    }

}
