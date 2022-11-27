package com.bastion.transfer.infrustructure.repository;


import com.bastion.transfer.domain.transaction.TransactionRepository;
import com.bastion.transfer.domain.transaction.model.TransactionData;
import com.bastion.transfer.infrustructure.mapper.Mapper;
import com.bastion.transfer.infrustructure.repository.model.TransactionEntity;
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
