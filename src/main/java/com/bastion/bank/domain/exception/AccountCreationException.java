package com.bastion.bank.domain.exception;

public class AccountCreationException extends Exception {

    private static final long serialVersionUID = 1L;

    public AccountCreationException(String message) {
        super(message);
    }
}
