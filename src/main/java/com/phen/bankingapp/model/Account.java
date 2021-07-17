package com.phen.bankingapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String accountName;
    private String accountNumber;
    private String accountPassword;
    private double balance;
    private List<TransactionRecord> transactionRecords = new ArrayList<>();
}
