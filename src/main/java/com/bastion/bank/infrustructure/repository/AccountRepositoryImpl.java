package com.bastion.bank.infrustructure.repository;

import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaRepository<AccountEntity, Long> jpaRepository;

    @Override
    public AccountEntity createAccount(AccountEntity accountEntity) {
       return jpaRepository.save(accountEntity);
    }

    @Override
    public void updateAccountBalance(AccountEntity accountEntity) {
        jpaRepository.save(accountEntity);
    }

    @Override
    public List<AccountEntity> getAllAccounts() {
        return jpaRepository.findAll();
    }

    @Override
    public AccountEntity findAccountById(Long accountId) {
        return jpaRepository.findById(accountId).orElseGet(() -> AccountEntity.builder().build());
    }

    @Override
    public void deleteAccount(Long accountId) {
      jpaRepository.deleteById(accountId);
    }
}
