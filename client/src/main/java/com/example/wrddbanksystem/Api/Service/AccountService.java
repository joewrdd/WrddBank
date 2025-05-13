package com.example.wrddbanksystem.Api.Service;

import com.example.wrddbanksystem.Models.CheckingAccounts;
import com.example.wrddbanksystem.Models.SavingsAccounts;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//----- Account Service Interface -----
public interface AccountService {
    
    //----- Get Checking Accounts -----
    List<CheckingAccounts> getCheckingAccounts(String payeeAddress) throws IOException, InterruptedException;
    
    //----- Get Savings Accounts -----
    List<SavingsAccounts> getSavingsAccounts(String payeeAddress) throws IOException, InterruptedException;
    
    //----- Get Checking Account -----
    Optional<CheckingAccounts> getCheckingAccount(String accountNumber) throws IOException, InterruptedException;
    
    //----- Get Savings Account -----
    Optional<SavingsAccounts> getSavingsAccount(String accountNumber) throws IOException, InterruptedException;
    
    //----- Create Checking Account -----
    CheckingAccounts createCheckingAccount(String payeeAddress, double initialBalance, double transactionLimit) 
            throws IOException, InterruptedException;
    
    //----- Create Savings Account -----
    SavingsAccounts createSavingsAccount(String payeeAddress, double initialBalance, double withdrawalLimit) 
            throws IOException, InterruptedException;
    
    //----- Update Checking Account -----
    boolean updateCheckingAccount(String accountNumber, double transactionLimit, double monthlyFee, boolean hasSalaryDomiciliation) 
            throws IOException, InterruptedException;
    
    //----- Update Savings Account -----
    boolean updateSavingsAccount(String accountNumber, double withdrawalLimit, double interestRate, 
                               String compoundingFrequency, double goalAmount) 
            throws IOException, InterruptedException;
    
    //----- Close Account -----
    boolean closeAccount(String accountNumber, String accountType) throws IOException, InterruptedException;
} 