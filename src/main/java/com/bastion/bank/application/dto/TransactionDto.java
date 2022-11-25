package com.bastion.bank.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    private long transactionId;

    @JsonProperty(required = true)
    private long fromAccountId;

    @JsonProperty(required = true)
    private long toAccountId;

    @JsonProperty(required = true)
    private BigDecimal amount;

    @JsonProperty(required = true)
    private String currencyCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date date;

    private String status;

    private String message;

}
