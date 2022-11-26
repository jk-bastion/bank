package com.bastion.bank.domain.transaction.exception;

public class InvalidCurrencyException extends Exception {

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
