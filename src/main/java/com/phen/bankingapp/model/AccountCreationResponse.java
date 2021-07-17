package com.phen.bankingapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationResponse {
    private int responseCode;
    private boolean success;
    private String message;
    private String accountNumber;
}
