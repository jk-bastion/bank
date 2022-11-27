package com.bastion.transfer.domain.transaction.exception;

public class NotEnoughBalanceException extends Exception {

    public NotEnoughBalanceException(String message) {
        super(message);
    }

}
