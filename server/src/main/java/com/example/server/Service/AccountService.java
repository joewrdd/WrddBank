package com.example.server.Service;

import com.example.server.Model.CheckingAccount;
import com.example.server.Model.Client;
import com.example.server.Model.SavingsAccount;
import com.example.server.Repository.CheckingAccountRepository;
import com.example.server.Repository.ClientRepository;
import com.example.server.Repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//----- Account Service -----
@Service
@RequiredArgsConstructor
public class AccountService {

    private final CheckingAccountRepository checkingAccountRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final ClientRepository clientRepository;

    //----- Get Checking Accounts -----
    public List<CheckingAccount> getCheckingAccounts(String payeeAddress) {
        return checkingAccountRepository.findByOwnerPayeeAddress(payeeAddress);
    }

    //----- Get Savings Accounts -----
    public List<SavingsAccount> getSavingsAccounts(String payeeAddress) {
        return savingsAccountRepository.findByOwnerPayeeAddress(payeeAddress);
    }

    //----- Get Checking Account By Number -----
    public Optional<CheckingAccount> getCheckingAccountByNumber(String accountNumber) {
        return checkingAccountRepository.findByAccountNumber(accountNumber);
    }

    //----- Get Savings Account By Number -----
    public Optional<SavingsAccount> getSavingsAccountByNumber(String accountNumber) {
        return savingsAccountRepository.findByAccountNumber(accountNumber);
    }

    //----- Create Checking Account -----
    @Transactional
    public CheckingAccount createCheckingAccount(String ownerPayeeAddress, double transactionLimit, double initialBalance) {
        Optional<Client> clientOpt = clientRepository.findByPayeeAddress(ownerPayeeAddress);
        
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            
            CheckingAccount account = new CheckingAccount();
            account.setOwner(client);
            account.setAccountNumber(generateAccountNumber());
            account.setTransactionLimit(transactionLimit);
            account.setBalance(initialBalance);
            account.setLastTransactionDate(LocalDate.now());
            account.setMonthlyFee(0.0);
            account.setSalaryDomiciliation(false);
            account.setAccountStatus("active");
            
            return checkingAccountRepository.save(account);
        }
        
        throw new IllegalArgumentException("Client not found with payee address: " + ownerPayeeAddress);
    }

    //----- Create Savings Account -----
    @Transactional
    public SavingsAccount createSavingsAccount(String ownerPayeeAddress, double withdrawalLimit, double initialBalance) {
        Optional<Client> clientOpt = clientRepository.findByPayeeAddress(ownerPayeeAddress);
        
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            
            SavingsAccount account = new SavingsAccount();
            account.setOwner(client);
            account.setAccountNumber(generateAccountNumber());
            account.setWithdrawalLimit(withdrawalLimit);
            account.setBalance(initialBalance);
            account.setInterestRate(0.01);
            account.setCompoundingFrequency("monthly");
            account.setGoalAmount(0.0);
            account.setAccountStatus("active");
            
            return savingsAccountRepository.save(account);
        }
        
        throw new IllegalArgumentException("Client not found with payee address: " + ownerPayeeAddress);
    }

    //----- Update Checking Account Properties -----
    @Transactional
    public boolean updateCheckingAccountProperties(String accountNumber, double monthlyFee, boolean salaryDomiciliation, String status) {
        Optional<CheckingAccount> accountOpt = checkingAccountRepository.findByAccountNumber(accountNumber);
        
        if (accountOpt.isPresent()) {
            CheckingAccount account = accountOpt.get();
            account.setMonthlyFee(monthlyFee);
            account.setSalaryDomiciliation(salaryDomiciliation);
            account.setAccountStatus(status);
            
            checkingAccountRepository.save(account);
            return true;
        }
        
        return false;
    }

    //----- Update Savings Account Properties -----
    @Transactional
    public boolean updateSavingsAccountProperties(String accountNumber, double interestRate, String compoundingFrequency, double goalAmount, String status) {
        Optional<SavingsAccount> accountOpt = savingsAccountRepository.findByAccountNumber(accountNumber);
        
        if (accountOpt.isPresent()) {
            SavingsAccount account = accountOpt.get();
            account.setInterestRate(interestRate);
            account.setCompoundingFrequency(compoundingFrequency);
            account.setGoalAmount(goalAmount);
            account.setAccountStatus(status);
            
            savingsAccountRepository.save(account);
            return true;
        }
        
        return false;
    }

    //----- Update Account Balance -----
    @Transactional
    public boolean updateAccountBalance(String accountNumber, String accountType, double newBalance) {
        if ("checking".equalsIgnoreCase(accountType)) {
            Optional<CheckingAccount> accountOpt = checkingAccountRepository.findByAccountNumber(accountNumber);
            
            if (accountOpt.isPresent()) {
                CheckingAccount account = accountOpt.get();
                account.setBalance(newBalance);
                account.setLastTransactionDate(LocalDate.now());
                checkingAccountRepository.save(account);
                return true;
            }
        } else if ("savings".equalsIgnoreCase(accountType)) {
            Optional<SavingsAccount> accountOpt = savingsAccountRepository.findByAccountNumber(accountNumber);
            
            if (accountOpt.isPresent()) {
                SavingsAccount account = accountOpt.get();
                account.setBalance(newBalance);
                savingsAccountRepository.save(account);
                return true;
            }
        }
        
        return false;
    }

    //----- Generate Account Number -----
    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
} 