package com.bastion.bank.infrustructure.repository;


import com.bastion.bank.domain.transaction.TransactionRepository;
import com.bastion.bank.domain.transaction.model.TransactionData;
import com.bastion.bank.infrustructure.mapper.Mapper;
import com.bastion.bank.infrustructure.repository.model.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionRepositoryJpa jpaRepository;
    private final Mapper<TransactionData, TransactionEntity> mapper;


//    @Transactional
    public void addTransaction(TransactionData transactionData) {
        jpaRepository.save(mapper.mapToEntity(transactionData));
    }

    @Override
    public List<TransactionData> getTransactionsForAccount(final Long accountId) {
        return jpaRepository.findByFromAccountId(accountId).stream()
                .map(mapper::mapToData)
                .collect(Collectors.toList());
    }
}
