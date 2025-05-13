package com.example.server.Controller;

import com.example.server.Model.Transaction;
import com.example.server.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//----- Transaction Controller -----
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    //----- Get Transactions By Payee Address -----
    @GetMapping("/{payeeAddress}")
    public ResponseEntity<?> getTransactionsByPayeeAddress(@PathVariable String payeeAddress) {
        try {
            //----- Ensure Consistent Format With @ Symbol -----
            if (!payeeAddress.startsWith("@")) {
                payeeAddress = "@" + payeeAddress;
            }
            
            List<Transaction> transactions = transactionService.getTransactionsByPayeeAddress(payeeAddress);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve transactions: " + e.getMessage()));
        }
    }

    //----- Get Latest Transactions -----
    @GetMapping("/{payeeAddress}/latest")
    public ResponseEntity<?> getLatestTransactions(
            @PathVariable String payeeAddress,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            //----- Ensure Consistent Format With @ Symbol -----
            if (!payeeAddress.startsWith("@")) {
                payeeAddress = "@" + payeeAddress;
            }
            
            List<Transaction> transactions = transactionService.getLatestTransactions(payeeAddress, limit);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve latest transactions: " + e.getMessage()));
        }
    }

    //----- Get Transaction Summary -----
    @GetMapping("/{payeeAddress}/summary")
    public ResponseEntity<?> getTransactionSummary(@PathVariable String payeeAddress) {
        try {
            //----- Ensure Consistent Format With @ Symbol -----
            if (!payeeAddress.startsWith("@")) {
                payeeAddress = "@" + payeeAddress;
            }
            
            double income = transactionService.calculateIncome(payeeAddress);
            double expense = transactionService.calculateExpense(payeeAddress);
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("income", income);
            summary.put("expense", expense);
            summary.put("balance", income - expense);
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve transaction summary: " + e.getMessage()));
        }
    }

    //----- Create Transaction -----
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Map<String, String> transactionData) {
        try {
            String sender = transactionData.get("sender");
            String receiver = transactionData.get("receiver");
            double amount = Double.parseDouble(transactionData.get("amount"));
            String message = transactionData.get("message");
            String sourceAccountType = transactionData.getOrDefault("sourceAccountType", "checking");
            
            //----- Ensure Consistent Format With @ Symbol For Both Sender And Receiver -----
            if (sender != null && !sender.startsWith("@")) {
                sender = "@" + sender;
            }
            
            if (receiver != null && !receiver.startsWith("@")) {
                receiver = "@" + receiver;
            }
            
            //----- Create The Transaction -----
            Transaction transaction = transactionService.createTransaction(sender, receiver, amount, message, sourceAccountType);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create transaction: " + e.getMessage()));
        }
    }
} 