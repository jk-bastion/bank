package com.bastion.transfer.infrustructure.repository;

import com.bastion.transfer.infrustructure.repository.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepositoryJpa extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findById(Long accountId);
}
