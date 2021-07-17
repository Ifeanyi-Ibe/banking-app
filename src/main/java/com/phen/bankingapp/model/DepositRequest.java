package com.phen.bankingapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest {
    private String accountNumber;
    private double amount;
}
