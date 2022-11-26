package com.bastion.bank.infrustructure.repository;

import com.bastion.bank.infrustructure.repository.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepositoryJpa extends JpaRepository<AccountEntity, Long> {

}
