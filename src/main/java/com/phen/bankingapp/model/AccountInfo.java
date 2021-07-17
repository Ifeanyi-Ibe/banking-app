package com.phen.bankingapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountInfo {
    private String accountName;
    private String accountNumber;
    private Double balance;
}
