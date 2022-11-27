package com.bastion.transfer.infrustructure.mapper;

import com.bastion.transfer.domain.account.model.AccountData;
import com.bastion.transfer.infrustructure.repository.model.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper implements Mapper<AccountData, AccountEntity> {

    @Override
    public AccountEntity mapToEntity(AccountData accountData) {
        if (accountData == null)
            return null;

        return AccountEntity.builder()
                .accountId(accountData.accountId())
                .balance(accountData.balance())
                .currencyCode(accountData.currencyCode())
                .email(accountData.email())
                .username(accountData.username())
                .build();
    }

    @Override
    public AccountData mapToData(AccountEntity accountEntity) {
        if (accountEntity == null)
            return null;
        return AccountData.builder()
                .accountId(accountEntity.getAccountId())
                .email(accountEntity.getEmail())
                .username(accountEntity.getUsername())
                .currencyCode(accountEntity.getCurrencyCode())
                .balance(accountEntity.getBalance())
                .build();
    }
}
