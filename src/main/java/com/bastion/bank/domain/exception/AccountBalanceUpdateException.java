package com.bastion.bank.domain.exception;

public class AccountBalanceUpdateException extends Exception {

    private static final long serialVersionUID = 1L;

    public AccountBalanceUpdateException(String message) {
        super(message);
    }
}
