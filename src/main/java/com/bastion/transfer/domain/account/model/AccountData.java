package com.bastion.transfer.domain.account.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountData(
        long accountId,
        String username,
        String email,
        BigDecimal balance,
        String currencyCode) {
}
