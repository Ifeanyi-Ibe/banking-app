package com.phen.bankingapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TransactionRecord {
    private Date transactionDate;
    private String transactionType;
    private String narration;
    private Double amount;
    private Double accountBalance;
}
