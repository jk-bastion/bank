package com.bastion.bank.infrustructure.exception.handlers;

import com.bastion.bank.domain.account.exception.AccountBalanceUpdateException;
import com.bastion.bank.domain.account.exception.AccountCreationException;
import com.bastion.bank.domain.account.exception.AccountNotExistsException;
import com.bastion.bank.domain.transaction.exception.InvalidCurrencyException;
import com.bastion.bank.domain.transaction.exception.NotEnoughBalanceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Slf4j
@Order(HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AccountTransferExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String NOT_ENOUGH_BALANCE = "1010";
    private static final String INVALID_CURRENCY = "1020";
    private static final String ACCOUNT_BALANCE_UPDATE = "1030";
    private static final String ACCOUNT_CREATION = "1040";
    private static final String ACCOUNT_NOT_EXISTS = "1020";

    //    todo: REVIEW CODES
    @ExceptionHandler(AccountCreationException.class)
    public final ResponseEntity<ApiErrorsResponse> handleAccountCreationException(AccountCreationException ex) {
        return new ResponseEntity<>(new ApiErrorsResponse(List.of(new ApiError(ACCOUNT_CREATION, ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotExistsException.class)
    public final ResponseEntity<ApiErrorsResponse> handleIAccountNotExistsException(AccountNotExistsException ex) {
        return new ResponseEntity<>(new ApiErrorsResponse(List.of(new ApiError(ACCOUNT_NOT_EXISTS, ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public final ResponseEntity<ApiErrorsResponse> handleNotEnoughBalanceException(NotEnoughBalanceException ex) {
        return new ResponseEntity<>(new ApiErrorsResponse(List.of(new ApiError(NOT_ENOUGH_BALANCE, ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    public final ResponseEntity<ApiErrorsResponse> handleInvalidCurrencyException(NotEnoughBalanceException ex) {
        return new ResponseEntity<>(new ApiErrorsResponse(List.of(new ApiError(INVALID_CURRENCY, ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountBalanceUpdateException.class)
    public final ResponseEntity<ApiErrorsResponse> handleAccountBalanceUpdateException(AccountBalanceUpdateException ex) {
        return new ResponseEntity<>(new ApiErrorsResponse(List.of(new ApiError(ACCOUNT_BALANCE_UPDATE, ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }


    @Data
    @AllArgsConstructor
    private static class ApiErrorsResponse {
        private List<ApiError> errors;
    }

    @Data
    @AllArgsConstructor
    private static class ApiError {
        private String code;
        private String message;
    }
}



