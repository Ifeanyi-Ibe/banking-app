package com.phen.bankingapp.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountInfoResponse {
    private int responseCode;
    private boolean success;
    private String message;
    private AccountInfo account;
}
