package com.phen.bankingapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String accountNumber;
    private String accountPassword;
}
