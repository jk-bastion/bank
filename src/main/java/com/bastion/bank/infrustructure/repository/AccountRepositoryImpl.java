package com.bastion.bank.infrustructure.repository;

import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private static final String ACCOUNT_TABLE = "account";
    private final EntityManager entityManager;

    @Override
    public AccountEntity createAccount(AccountEntity accountEntity) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(accountEntity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            entityManager.getTransaction().rollback();
            throw e;
        }
        return accountEntity;
    }

    @Override
    public void updateAccountBalance(AccountEntity accountEntity) {
        try {
            entityManager.getTransaction().begin();
            log.info("start updating account id {} balance to {}", accountEntity.getAccountId(), accountEntity.getBalance());
            Query query = entityManager.createQuery("update " + ACCOUNT_TABLE + " set balance=?1 where accountId=?2");
            query.setParameter(1, accountEntity.getBalance());
            query.setParameter(2, accountEntity.getAccountId());
            query.executeUpdate();
            entityManager.getTransaction().commit();
            log.info("finish updating account id {} balance to {}", accountEntity.getAccountId(), accountEntity.getBalance());
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public List<AccountEntity> getAllAccounts() {
        try {
            Query query = entityManager.createQuery("from " + ACCOUNT_TABLE);
            return (List<AccountEntity>) query.getResultList();
        } catch (Exception ex) {
            log.info("{}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public AccountEntity findAccountById(Long accountId) {
        try {
            Query query = entityManager.createQuery("from " + ACCOUNT_TABLE + " where accountId = ?1");
            query.setParameter(1, accountId);
            return (AccountEntity) query.getSingleResult();
        } catch (Exception ex) {
            log.info("{}", ex.getMessage());
            return new AccountEntity();
        }
    }

    @Override
    public void deleteAccount(Long accountId) {
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("delete from " + ACCOUNT_TABLE + " where accountId = ?1");
            query.setParameter(1, accountId);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            entityManager.getTransaction().rollback();
        }
    }
}
