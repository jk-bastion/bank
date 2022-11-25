package com.bastion.bank.domain.exception;

public class InvalidCurrencyException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
