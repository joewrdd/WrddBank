package com.example.server.Controller;

import com.example.server.Model.Admin;
import com.example.server.Model.CheckingAccount;
import com.example.server.Model.SavingsAccount;
import com.example.server.Model.Transaction;
import com.example.server.Service.AccountService;
import com.example.server.Service.AdminService;
import com.example.server.Service.TransactionService;
import com.example.server.Repository.CheckingAccountRepository;
import com.example.server.Repository.ClientRepository;
import com.example.server.Repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//----- Admin Controller -----
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ClientRepository clientRepository;
    private final CheckingAccountRepository checkingAccountRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    //----- Authenticate Admin -----
    @PostMapping("/auth")
    public ResponseEntity<Admin> authenticateAdmin(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Optional<Admin> admin = adminService.authenticateAdmin(username, password);
        return admin.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    //----- Create Deposit -----
    @PostMapping("/deposits")
    public ResponseEntity<?> createDeposit(@RequestBody Map<String, Object> depositData) {
        try {
            String payeeAddress = (String) depositData.get("payeeAddress");
            String accountNumber = (String) depositData.get("accountNumber");
            String accountType = (String) depositData.get("accountType");
            double amount = Double.parseDouble(depositData.get("amount").toString());
            String description = (String) depositData.get("description");
            
            //----- Ensure Consistent Format For Payee Address -----
            if (payeeAddress != null && !payeeAddress.startsWith("@")) {
                payeeAddress = "@" + payeeAddress;
            }
            
            //----- Validate Deposit Data -----
            if (payeeAddress == null || accountNumber == null || accountType == null || amount <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid deposit data. All fields are required and amount must be positive."));
            }
            
            //----- Verify The Account Exists -----
            boolean accountExists = false;
            if ("checking".equalsIgnoreCase(accountType)) {
                Optional<CheckingAccount> account = accountService.getCheckingAccountByNumber(accountNumber);
                accountExists = account.isPresent();
                
                if (accountExists) {
                    //----- Update Account Balance -----
                    CheckingAccount checkingAccount = account.get();
                    double newBalance = checkingAccount.getBalance() + amount;
                    accountService.updateAccountBalance(accountNumber, "checking", newBalance);
                    
                    //----- Create A Transaction Record -----
                    Transaction transaction = new Transaction();
                    transaction.setSender("@BANK-ADMIN");
                    transaction.setReceiver(payeeAddress);
                    transaction.setAmount(amount);
                    transaction.setDate(LocalDate.now());
                    transaction.setMessage(description);
                    transactionService.saveTransaction(transaction);
                }
            } else if ("savings".equalsIgnoreCase(accountType)) {
                Optional<SavingsAccount> account = accountService.getSavingsAccountByNumber(accountNumber);
                accountExists = account.isPresent();
                
                if (accountExists) {
                    //----- Update Account Balance -----
                    SavingsAccount savingsAccount = account.get();
                    double newBalance = savingsAccount.getBalance() + amount;
                    accountService.updateAccountBalance(accountNumber, "savings", newBalance);
                    
                    //----- Create A Transaction Record -----
                    Transaction transaction = new Transaction();
                    transaction.setSender("@BANK-ADMIN");
                    transaction.setReceiver(payeeAddress);
                    transaction.setAmount(amount);
                    transaction.setDate(LocalDate.now());
                    transaction.setMessage(description);
                    
                    //----- Save The Transaction -----
                    transactionService.saveTransaction(transaction);
                }
            }
            
            if (!accountExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Account not found with number: " + accountNumber));
            }
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Deposit processed successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to process deposit: " + e.getMessage()));
        }
    }

    //----- Get System Statistics -----
    @GetMapping("/statistics")
    public ResponseEntity<?> getSystemStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            //----- Get Client Count -----
            long clientCount = clientRepository.count();
            statistics.put("clientCount", clientCount);
            
            //----- Get Account Counts -----
            long checkingAccountCount = checkingAccountRepository.count();
            long savingsAccountCount = savingsAccountRepository.count();
            statistics.put("checkingAccountCount", checkingAccountCount);
            statistics.put("savingsAccountCount", savingsAccountCount);
            
            //----- Calculate Total Money Inflow -----
            double checkingBalance = checkingAccountRepository.sumBalance().orElse(0.0);
            double savingsBalance = savingsAccountRepository.sumBalance().orElse(0.0);
            double totalInflow = checkingBalance + savingsBalance;
            statistics.put("totalInflow", totalInflow);
            
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve system statistics"));
        }
    }
} 