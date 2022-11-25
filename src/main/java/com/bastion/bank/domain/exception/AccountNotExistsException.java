package com.bastion.bank.domain.exception;

public class AccountNotExistsException extends Exception {

    public AccountNotExistsException(String message) {
        super(message);
    }

}
