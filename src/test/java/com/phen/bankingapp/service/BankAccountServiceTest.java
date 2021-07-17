package com.phen.bankingapp.service;

import com.phen.bankingapp.config.UserDetailsImplService;
import com.phen.bankingapp.model.*;
import com.phen.bankingapp.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsImplService userDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    BankAccountService bankAccountService = new BankAccountService(authenticationManager, userDetailsService, jwtTokenProvider, passwordEncoder);

    @Test
    void createAccount() {

        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountName("Kurt Hamilton");
        request.setAccountPassword("kurt");
        request.setInitialDeposit(5000.0);

        ResponseEntity<AccountCreationResponse> response = bankAccountService.createAccount(request);
        assertEquals("Account created successfully.", response.getBody().getMessage());
    }

    @Test
    void withdraw() {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountName("Motunrayo Adeleke");
        request.setAccountPassword("motun");
        request.setInitialDeposit(6200.0);

        bankAccountService.createAccount(request);

        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setAccountName("Motunrayo Adeleke");
        withdrawalRequest.setAccountPassword("motun");
        withdrawalRequest.setWithdrawnAmount(5900.0);

        ResponseEntity<ResponseData> response = bankAccountService.withdraw(withdrawalRequest);
        assertEquals("Insufficient balance. Balance cannot be less than 500 naira.", response.getBody().getMessage());

    }

    @Test
    void deposit() {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountName("Abdullahi Mustapha");
        request.setAccountPassword("abdul");
        request.setInitialDeposit(2900.0);

        bankAccountService.createAccount(request);

        Account account = bankAccountService.findByUsername("Abdullahi Mustapha");

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountNumber(account.getAccountNumber());
        depositRequest.setAmount(1200000);

        ResponseEntity<ResponseData> response = bankAccountService.deposit(depositRequest);
        assertEquals("Deposit limit exceeded. Amount must be less than 1,000,000 (one million) naira.", response.getBody().getMessage());
    }
}