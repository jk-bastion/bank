package com.bastion.transfer.domain.transaction.model;

public enum ErrorsCode {

    ACCOUNT_NOT_EXISTS("account does not exist"),
    INVALID_CURRENCY("not compatible currency"),
    ACCOUNT_CREATION_FAILED("account creation failed"),
    NOT_ENOUGH_BALANCE("not enough balance"),
    ACCOUNT_UPDATE_FAILED("account update failed");

    private final String message;

    ErrorsCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
