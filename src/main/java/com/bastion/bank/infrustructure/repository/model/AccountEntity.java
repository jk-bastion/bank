package com.bastion.bank.infrustructure.repository.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "account")
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long accountId;

    @NotNull(message = "username is required")
    @Column(name="username")
    private String username;

    @Email(message = "invalid email")
    @Column(name="email", unique = true)
    private String email;

    @Column(name= "balance")
    private BigDecimal balance;

    @Column(name= "currency_code")
    private String currencyCode;

}
