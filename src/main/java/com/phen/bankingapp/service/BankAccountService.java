package com.phen.bankingapp.service;

import com.phen.bankingapp.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BankAccountService {

    private List<Account> accounts = new ArrayList<>();

    public ResponseEntity<AccountCreationResponse> createAccount(AccountCreationRequest request) {
        Account account = getAccount(request.getAccountName(), request.getAccountPassword());
        AccountCreationResponse response;
        if(account != null) {
            response = new AccountCreationResponse(HttpStatus.BAD_REQUEST.value(), false, "Account already exists.", "");
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(request.getInitialDeposit() < 500) {
            response = new AccountCreationResponse(HttpStatus.BAD_REQUEST.value(), false, "Insufficient funds. A minimum of 500 naira is required to open an account.", "");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Generate account number
        String accountNumber = generateAccountNumber();

        // Create new account and add it to the repository
        Account account1 = addAccount(request.getAccountName(), accountNumber, request.getAccountPassword(), request.getInitialDeposit());

        response = new AccountCreationResponse(HttpStatus.OK.value(), true, "Account created successfully.", account1.getAccountNumber());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> withdraw(WithdrawalRequest request) {
        Account account = getAccount(request.getAccountName(), request.getAccountPassword());
        ResponseData response;
        if(account != null) {
            if(request.getWithdrawnAmount() < 1) {
                response = new ResponseData(HttpStatus.BAD_REQUEST.value(), false, "Invalid withdrawal amount. Amount must be at least 1 naira");
                TransactionRecord record = getWithrawalError(request, response);
                account.getTransactionRecords().add(record);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if(account.getBalance() - request.getWithdrawnAmount() < 500) {
                response = new ResponseData(HttpStatus.BAD_REQUEST.value(), false, "Insufficient balance. Balance cannot be less than 500 naira.");
                TransactionRecord record = getWithrawalError(request, response);
                account.getTransactionRecords().add(record);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            debitAccount(account, request.getWithdrawnAmount());
            response = new ResponseData(HttpStatus.OK.value(), true, "Transaction successful.");
            TransactionRecord record = getWithdrawalTransactionRecord(account, request, response);
            account.getTransactionRecords().add(record);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ResponseData(HttpStatus.BAD_REQUEST.value(), false, "Incorrect account details. Please try again.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ResponseData> deposit(DepositRequest request) {
        boolean accountExists = accountExists(request.getAccountNumber());
        Account account = getByAccountNumber(request.getAccountNumber());
        ResponseData response;
        if(accountExists) {
            if(request.getAmount() > 1000000) {
                response = new ResponseData(HttpStatus.BAD_REQUEST.value(), false, "Deposit limit exceeded. Amount must be less than 1,000,000 (one million) naira.");
                TransactionRecord record = getDepositError(request, response);
                account.getTransactionRecords().add(record);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if(request.getAmount() < 1) {
                response = new ResponseData(HttpStatus.BAD_REQUEST.value(), false, "Invalid deposit amount. Deposit amount cannot be less than one (1) naira");
                TransactionRecord record = getDepositError(request, response);
                account.getTransactionRecords().add(record);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            creditAccount(account, request.getAmount());
            response = new ResponseData(HttpStatus.OK.value(), true, "Transaction successful.");
            TransactionRecord record = getDepositTransactionRecord(account, request, response);
            account.getTransactionRecords().add(record);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response = new ResponseData(HttpStatus.BAD_REQUEST.value(), false, "Account number does not exist.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getTransactionRecords(String accountNumber) {
        Account account = getByAccountNumber(accountNumber);

        if(account == null) {
           ResponseData errorResponse = new ResponseData(HttpStatus.BAD_REQUEST.value(), false, "Account does not exist.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<TransactionRecord> response;
        response = account.getTransactionRecords();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getAccountInfo(LoginRequest request) {
        Account account = findByNumberAndPassword(request.getAccountNumber(), request.getAccountPassword());
        AccountInfoResponse response;
        if(account != null) {
            AccountInfo info = initAccountInfo(account);
            response = initAccountInfoResponse(info);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ResponseData errorResponse = new ResponseData(HttpStatus.BAD_REQUEST.value(), false, "Account does not exist.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String generateAccountNumber() {
        Random random = new Random();
        char[] digits = new char[10];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < 10; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return new String(digits);
    }

    private Account addAccount(String name, String number, String password, double initialDeposit) {
        Account account = new Account();

        account.setAccountName(name);
        account.setAccountNumber(number);
        account.setAccountPassword(password);
        account.setBalance(initialDeposit);

        this.accounts.add(account);
        return account;
    }

    private Account  getAccount(String accountName, String password) {
        Optional<Account> account = this.accounts.stream().filter(acc -> acc.getAccountName().equals(accountName) && acc.getAccountPassword().equals(password)).findFirst();
        return account.orElse(null);
    }

    public Account findByNumberAndPassword(String accountNumber, String password) {
        Optional<Account> account = this.accounts.stream().filter(acc -> acc.getAccountNumber().equals(accountNumber) && acc.getAccountPassword().equals(password)).findFirst();
        return account.orElse(null);
    }

    private Account getByAccountNumber(String accountNumber) {
        Optional<Account> account = this.accounts.stream().filter(acc -> acc.getAccountNumber().equals(accountNumber)).findFirst();
        return account.orElse(null);
    }

    private boolean accountExists(String accountNumber) {
        return this.accounts.stream().anyMatch(account -> account.getAccountNumber().equals(accountNumber));
    }

    public Account findByUsername(String name) {
        Optional<Account> account = this.accounts.stream().filter(acc -> acc.getAccountName().equals(name)).findFirst();
        return account.orElse(null);
    }

    private void debitAccount(Account account, double amount) {
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
    }

    private void creditAccount(Account account, double amount) {
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
    }

    private AccountInfo initAccountInfo(Account account) {
        AccountInfo newAccount = new AccountInfo();
        newAccount.setAccountName(account.getAccountName());
        newAccount.setAccountNumber(account.getAccountNumber());
        newAccount.setBalance(account.getBalance());

        return newAccount;
    }

    private AccountInfoResponse initAccountInfoResponse(AccountInfo info) {
        AccountInfoResponse response = new AccountInfoResponse();
        response.setResponseCode(200);
        response.setSuccess(true);
        response.setMessage("Account information retrieved successfully.");
        response.setAccount(info);

        return response;
    }

    private TransactionRecord getWithdrawalTransactionRecord(Account account, WithdrawalRequest request, ResponseData response) {
        TransactionRecord transaction = new TransactionRecord();
        Date date = new Date();

        transaction.setTransactionDate(date);
        transaction.setTransactionType("Withdrawal");
        transaction.setNarration(response.getMessage());
        transaction.setAmount(request.getWithdrawnAmount());
        transaction.setAccountBalance(account.getBalance());

        return transaction;
    }

    private TransactionRecord getWithrawalError(WithdrawalRequest request, ResponseData response) {
        TransactionRecord transaction = new TransactionRecord();
        Date date = new Date();

        transaction.setTransactionDate(date);
        transaction.setTransactionType("Withdrawal");
        transaction.setNarration(response.getMessage());
        transaction.setAmount(request.getWithdrawnAmount());
        transaction.setAccountBalance(0.0);

        return transaction;
    }


    private TransactionRecord getDepositTransactionRecord(Account account, DepositRequest request, ResponseData response) {
        TransactionRecord transaction = new TransactionRecord();
        Date date = new Date();

        transaction.setTransactionDate(date);
        transaction.setTransactionType("Deposit");
        transaction.setNarration(response.getMessage());
        transaction.setAmount(request.getAmount());
        transaction.setAccountBalance(account.getBalance());

        return transaction;
    }

    private TransactionRecord getDepositError(DepositRequest request, ResponseData response) {
        TransactionRecord transaction = new TransactionRecord();
        Date date = new Date();

        transaction.setTransactionDate(date);
        transaction.setTransactionType("Withdrawal");
        transaction.setNarration(response.getMessage());
        transaction.setAmount(request.getAmount());
        transaction.setAccountBalance(0.0);

        return transaction;
    }
}
