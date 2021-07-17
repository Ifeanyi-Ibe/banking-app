package com.phen.bankingapp;

import com.phen.bankingapp.model.*;
import com.phen.bankingapp.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class controller {


    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/account_info/{accountNumber}")
    public ResponseEntity<?> getAccountInfo(@PathVariable("accountNumber") String accountNumber, @RequestBody LoginRequest request) {
       return bankAccountService.getAccountInfo(request);
    }

    @GetMapping("/account_statement/{accountNumber}")
    public ResponseEntity<?> getAccountStatement(@PathVariable("accountNumber") String accountNumber) {
        return bankAccountService.getTransactionRecords(accountNumber);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ResponseData> creditAccount(@RequestBody DepositRequest request) {
       return bankAccountService.deposit(request);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<ResponseData> debitAccount(@RequestBody WithdrawalRequest request) {
        return bankAccountService.withdraw(request);
    }

    @PostMapping("/create_account")
    public ResponseEntity<AccountCreationResponse> addAccount(@RequestBody AccountCreationRequest request) {
        return bankAccountService.createAccount(request);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) throws Exception {
        return bankAccountService.login(request);
    }
}
