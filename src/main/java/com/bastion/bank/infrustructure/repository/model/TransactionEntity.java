package com.bastion.bank.infrustructure.repository.model;

import com.bastion.bank.domain.transaction.model.TransactionStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "transaction")
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long transactionId;

    @NotNull(message = "Source account id is required")
    @Column(name= "fromAccountId")
    private Long fromAccountId;

    @NotNull(message = "Destination account id is required")
    @Column(name = "toAccountId")
    private Long toAccountId;

    @NotNull(message = "Transaction amount is required.")
    @Column(name ="amount")
    private BigDecimal amount;

    @NotNull(message = "Currency code is required")
    @Column(name = "currencyCode")
    private String currencyCode;

    @NotNull(message = "Transaction status is required")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name ="date")
    private java.sql.Date date;

    @Column(name ="message")
    private String message;
}
