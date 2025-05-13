package com.example.wrddbanksystem.Api.Service.Impl;

import com.example.wrddbanksystem.Api.Service.AccountService;
import com.example.wrddbanksystem.Models.Account;
import com.example.wrddbanksystem.Models.ApiClient;
import com.example.wrddbanksystem.Models.CheckingAccounts;
import com.example.wrddbanksystem.Models.SavingsAccounts;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//----- Account Service Implementation -----
public class AccountServiceImpl implements AccountService {

    //----- Get Checking Accounts -----
    @Override
    public List<CheckingAccounts> getCheckingAccounts(String payeeAddress) throws IOException, InterruptedException {
        List<Map<String, Object>> accounts = ApiClient.get("/accounts/checking/" + payeeAddress, List.class);
        List<CheckingAccounts> checkingAccounts = new ArrayList<>();
        
        if (accounts != null) {
            for (Map<String, Object> account : accounts) {
                checkingAccounts.add(mapToCheckingAccount(account));
            }
        }
        
        return checkingAccounts;
    }

    //----- Get Savings Accounts -----
    @Override
    public List<SavingsAccounts> getSavingsAccounts(String payeeAddress) throws IOException, InterruptedException {
        List<Map<String, Object>> accounts = ApiClient.get("/accounts/savings/" + payeeAddress, List.class);
        List<SavingsAccounts> savingsAccounts = new ArrayList<>();
        
        if (accounts != null) {
            for (Map<String, Object> account : accounts) {
                savingsAccounts.add(mapToSavingsAccount(account));
            }
        }
        
        return savingsAccounts;
    }

    //----- Get Checking Account -----
    @Override
    public Optional<CheckingAccounts> getCheckingAccount(String accountNumber) throws IOException, InterruptedException {
        try {
            Map<String, Object> account = ApiClient.get("/accounts/checking/number/" + accountNumber, Map.class);
            
            if (account != null) {
                return Optional.of(mapToCheckingAccount(account));
            }
        } catch (Exception e) {
            System.err.println("Error fetching checking account: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    //----- Get Savings Account -----
    @Override
    public Optional<SavingsAccounts> getSavingsAccount(String accountNumber) throws IOException, InterruptedException {
        try {
            Map<String, Object> account = ApiClient.get("/accounts/savings/number/" + accountNumber, Map.class);
            
            if (account != null) {
                return Optional.of(mapToSavingsAccount(account));
            }
        } catch (Exception e) {
            System.err.println("Error fetching savings account: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    //----- Create Checking Account -----
    @Override
    public CheckingAccounts createCheckingAccount(String payeeAddress, double initialBalance, double transactionLimit) 
            throws IOException, InterruptedException {
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("ownerPayeeAddress", payeeAddress);
        accountData.put("initialBalance", initialBalance);
        accountData.put("transactionLimit", transactionLimit);
        
        Map<String, Object> newAccount = ApiClient.post("/accounts/checking", accountData, Map.class);
        
        if (newAccount != null) {
            return mapToCheckingAccount(newAccount);
        }
        
        throw new IOException("Failed to create checking account");
    }

    //----- Create Savings Account -----
    @Override
    public SavingsAccounts createSavingsAccount(String payeeAddress, double initialBalance, double withdrawalLimit) 
            throws IOException, InterruptedException {
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("ownerPayeeAddress", payeeAddress);
        accountData.put("initialBalance", initialBalance);
        accountData.put("withdrawalLimit", withdrawalLimit);
        
        Map<String, Object> newAccount = ApiClient.post("/accounts/savings", accountData, Map.class);
        
        if (newAccount != null) {
            return mapToSavingsAccount(newAccount);
        }
        
        throw new IOException("Failed to create savings account");
    }

    //----- Update Checking Account -----
    @Override
    public boolean updateCheckingAccount(String accountNumber, double transactionLimit, double monthlyFee, boolean hasSalaryDomiciliation) 
            throws IOException, InterruptedException {
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("transactionLimit", transactionLimit);
        accountData.put("monthlyFee", monthlyFee);
        accountData.put("salaryDomiciliation", hasSalaryDomiciliation);
        
        try {
            ApiClient.put("/accounts/checking/" + accountNumber, accountData, Map.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating checking account: " + e.getMessage());
            return false;
        }
    }

    //----- Update Savings Account -----
    @Override
    public boolean updateSavingsAccount(String accountNumber, double withdrawalLimit, double interestRate, 
                                      String compoundingFrequency, double goalAmount) 
            throws IOException, InterruptedException {
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("withdrawalLimit", withdrawalLimit);
        accountData.put("interestRate", interestRate);
        accountData.put("compoundingFrequency", compoundingFrequency);
        accountData.put("goalAmount", goalAmount);
        
        try {
            ApiClient.put("/accounts/savings/" + accountNumber, accountData, Map.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating savings account: " + e.getMessage());
            return false;
        }
    }

    //----- Close Account -----
    @Override
    public boolean closeAccount(String accountNumber, String accountType) throws IOException, InterruptedException {
        try {
            String endpoint = "/accounts/" + accountType + "/" + accountNumber + "/close";
            ApiClient.put(endpoint, null, Map.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error closing account: " + e.getMessage());
            return false;
        }
    }
    
    //----- Map Checking Account -----
    private CheckingAccounts mapToCheckingAccount(Map<String, Object> apiAccount) {
        String accountNumber = (String) apiAccount.get("accountNumber");
        String payeeAddress = (String) apiAccount.get("ownerPayeeAddress");
        double transactionLimit = ((Number) apiAccount.get("transactionLimit")).doubleValue();
        double balance = ((Number) apiAccount.get("balance")).doubleValue();
        double monthlyFee = ((Number) apiAccount.get("monthlyFee")).doubleValue();
        boolean salaryDomiciliation = (Boolean) apiAccount.get("salaryDomiciliation");
        
        Account.AccountStatus status = Account.AccountStatus.ACTIVE;
        String statusStr = (String) apiAccount.get("accountStatus");
        if (statusStr != null) {
            if (statusStr.equalsIgnoreCase("suspended")) {
                status = Account.AccountStatus.SUSPENDED;
            } else if (statusStr.equalsIgnoreCase("closed")) {
                status = Account.AccountStatus.CLOSED;
            }
        }
        
        LocalDateTime lastTransaction = LocalDateTime.now();
        
        return new CheckingAccounts(
            accountNumber,
            payeeAddress,
            transactionLimit,
            balance,
            lastTransaction,
            monthlyFee,
            salaryDomiciliation,
            status
        );
    }
    
    //----- Map Savings Account -----
    private SavingsAccounts mapToSavingsAccount(Map<String, Object> apiAccount) {
        String payeeAddress = (String) apiAccount.get("ownerPayeeAddress");
        String accountNumber = (String) apiAccount.get("accountNumber");
        double balance = ((Number) apiAccount.get("balance")).doubleValue();
        double withdrawalLimit = ((Number) apiAccount.get("withdrawalLimit")).doubleValue();
        double interestRate = ((Number) apiAccount.get("interestRate")).doubleValue();
        double goalAmount = ((Number) apiAccount.get("goalAmount")).doubleValue();
        
        String compoundingFrequencyStr = (String) apiAccount.get("compoundingFrequency");
        SavingsAccounts.CompoundingFrequency compoundingFrequency = SavingsAccounts.CompoundingFrequency.MONTHLY;
        if (compoundingFrequencyStr != null) {
            if (compoundingFrequencyStr.equalsIgnoreCase("daily")) {
                compoundingFrequency = SavingsAccounts.CompoundingFrequency.DAILY;
            } else if (compoundingFrequencyStr.equalsIgnoreCase("annually")) {
                compoundingFrequency = SavingsAccounts.CompoundingFrequency.ANNUALLY;
            }
        }
        
        Account.AccountStatus status = Account.AccountStatus.ACTIVE;
        String statusStr = (String) apiAccount.get("accountStatus");
        if (statusStr != null) {
            if (statusStr.equalsIgnoreCase("suspended")) {
                status = Account.AccountStatus.SUSPENDED;
            } else if (statusStr.equalsIgnoreCase("closed")) {
                status = Account.AccountStatus.CLOSED;
            }
        }
        
        return new SavingsAccounts(
            payeeAddress,
            accountNumber,
            balance,
            withdrawalLimit,
            interestRate,
            compoundingFrequency,
            goalAmount,
            status
        );
    }
} 