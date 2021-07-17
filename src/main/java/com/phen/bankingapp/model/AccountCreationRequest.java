package com.phen.bankingapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreationRequest {
    private String accountName;
    private String accountPassword;
    private Double initialDeposit;
}
