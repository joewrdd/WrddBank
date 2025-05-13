package com.example.server.Controller;

import com.example.server.Model.CheckingAccount;
import com.example.server.Model.SavingsAccount;
import com.example.server.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//----- Account Controller -----
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    //----- Get Checking Accounts -----
    @GetMapping("/checking/{payeeAddress}")
    public ResponseEntity<?> getCheckingAccounts(@PathVariable String payeeAddress) {
        try {
            List<CheckingAccount> accounts = accountService.getCheckingAccounts(payeeAddress);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve checking accounts: " + e.getMessage()));
        }
    }

    //----- Get Savings Accounts -----
    @GetMapping("/savings/{payeeAddress}")
    public ResponseEntity<?> getSavingsAccounts(@PathVariable String payeeAddress) {
        try {
            List<SavingsAccount> accounts = accountService.getSavingsAccounts(payeeAddress);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve savings accounts: " + e.getMessage()));
        }
    }

    //----- Get Checking Account By Number -----
    @GetMapping("/checking/number/{accountNumber}")
    public ResponseEntity<?> getCheckingAccountByNumber(@PathVariable String accountNumber) {
        try {
            Optional<CheckingAccount> account = accountService.getCheckingAccountByNumber(accountNumber);
            if (account.isPresent()) {
                return ResponseEntity.ok(account.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve checking account: " + e.getMessage()));
        }
    }

    //----- Get Savings Account By Number -----
    @GetMapping("/savings/number/{accountNumber}")
    public ResponseEntity<?> getSavingsAccountByNumber(@PathVariable String accountNumber) {
        try {
            Optional<SavingsAccount> account = accountService.getSavingsAccountByNumber(accountNumber);
            if (account.isPresent()) {
                return ResponseEntity.ok(account.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve savings account: " + e.getMessage()));
        }
    }

    //----- Create Checking Account -----
    @PostMapping("/checking")
    public ResponseEntity<?> createCheckingAccount(@RequestBody Map<String, Object> accountData) {
        String ownerPayeeAddress = (String) accountData.get("ownerPayeeAddress");
        double transactionLimit = Double.parseDouble(accountData.get("transactionLimit").toString());
        double initialBalance = Double.parseDouble(accountData.get("initialBalance").toString());
        
        try {
            CheckingAccount account = accountService.createCheckingAccount(ownerPayeeAddress, transactionLimit, initialBalance);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create checking account: " + e.getMessage()));
        }
    }

    //----- Create Savings Account -----
    @PostMapping("/savings")
    public ResponseEntity<?> createSavingsAccount(@RequestBody Map<String, Object> accountData) {
        String ownerPayeeAddress = (String) accountData.get("ownerPayeeAddress");
        double withdrawalLimit = Double.parseDouble(accountData.get("withdrawalLimit").toString());
        double initialBalance = Double.parseDouble(accountData.get("initialBalance").toString());
        
        try {
            SavingsAccount account = accountService.createSavingsAccount(ownerPayeeAddress, withdrawalLimit, initialBalance);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create savings account: " + e.getMessage()));
        }
    }

    //----- Update Checking Account Properties -----
    @PutMapping("/checking/{accountNumber}/properties")
    public ResponseEntity<?> updateCheckingAccountProperties(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Object> propertyData) {
        
        try {
            double monthlyFee = Double.parseDouble(propertyData.get("monthlyFee").toString());
            boolean salaryDomiciliation = (boolean) propertyData.get("salaryDomiciliation");
            String status = (String) propertyData.get("status");
            
            boolean updated = accountService.updateCheckingAccountProperties(accountNumber, monthlyFee, salaryDomiciliation, status);
            return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update checking account properties: " + e.getMessage()));
        }
    }

    //----- Update Savings Account Properties -----
    @PutMapping("/savings/{accountNumber}/properties")
    public ResponseEntity<?> updateSavingsAccountProperties(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Object> propertyData) {
        
        try {
            double interestRate = Double.parseDouble(propertyData.get("interestRate").toString());
            String compoundingFrequency = (String) propertyData.get("compoundingFrequency");
            double goalAmount = Double.parseDouble(propertyData.get("goalAmount").toString());
            String status = (String) propertyData.get("status");
            
            boolean updated = accountService.updateSavingsAccountProperties(accountNumber, interestRate, compoundingFrequency, goalAmount, status);
            return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update savings account properties: " + e.getMessage()));
        }
    }

    //----- Update Account Balance -----
    @PutMapping("/{accountType}/{accountNumber}/balance")
    public ResponseEntity<?> updateAccountBalance(
            @PathVariable String accountType,
            @PathVariable String accountNumber,
            @RequestBody Map<String, Object> balanceData) {
        
        try {
            double newBalance = Double.parseDouble(balanceData.get("newBalance").toString());
            
            boolean updated = accountService.updateAccountBalance(accountNumber, accountType, newBalance);
            return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update account balance: " + e.getMessage()));
        }
    }
} 