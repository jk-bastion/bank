package com.bastion.bank.infrustructure.repository;

import com.bastion.bank.domain.account.AccountRepository;
import com.bastion.bank.domain.account.model.AccountData;
import com.bastion.bank.infrustructure.mapper.Mapper;
import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaRepository<AccountEntity, Long> jpaRepository;
    private final Mapper<AccountData, AccountEntity> mapper;

    @Override
    public AccountData createAccount(AccountData accountData) {
        return mapper.mapToData(jpaRepository.save(mapper.mapToEntity(accountData)));
    }

    @Override
    public void updateAccountBalance(AccountData accountData) {
        jpaRepository.save(mapper.mapToEntity(accountData));
    }

    @Override
    public List<AccountData> getAllAccounts() {
        return jpaRepository.findAll().stream()
                .map(mapper::mapToData)
                .collect(Collectors.toList());
    }

    @Override
    public AccountData findAccountById(Long accountId) {
        return mapper.mapToData(jpaRepository.findById(accountId).orElseGet(() -> AccountEntity.builder().build()));
    }

    @Override
    public void deleteAccount(Long accountId) {
        jpaRepository.deleteById(accountId);
    }
}
