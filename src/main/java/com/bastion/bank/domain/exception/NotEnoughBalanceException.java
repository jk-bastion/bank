package com.bastion.bank.domain.exception;

public class NotEnoughBalanceException extends Exception {

    public NotEnoughBalanceException(String message) {
        super(message);
    }

}
