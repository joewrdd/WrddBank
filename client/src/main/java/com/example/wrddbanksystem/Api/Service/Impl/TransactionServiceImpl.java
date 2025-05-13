package com.example.wrddbanksystem.Api.Service.Impl;

import com.example.wrddbanksystem.Api.Service.TransactionService;
import com.example.wrddbanksystem.Models.ApiClient;
import com.example.wrddbanksystem.Models.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//----- Transaction Service Implementation -----
public class TransactionServiceImpl implements TransactionService {

    //----- Get Transactions -----
    @Override
    public List<Transaction> getTransactions(String payeeAddress, int limit) throws IOException, InterruptedException {
        String endpoint = limit > 0 
            ? "/transactions/" + payeeAddress + "/latest?limit=" + limit 
            : "/transactions/" + payeeAddress;
        
        List<Map<String, Object>> apiTransactions = ApiClient.get(endpoint, List.class);
        List<Transaction> transactions = new ArrayList<>();
        
        if (apiTransactions != null) {
            for (Map<String, Object> apiTx : apiTransactions) {
                transactions.add(mapToTransaction(apiTx));
            }
        }
        
        return transactions;
    }

    //----- Get All Transactions -----
    @Override
    public List<Transaction> getAllTransactions(String payeeAddress) throws IOException, InterruptedException {
        return getTransactions(payeeAddress, -1);
    }

    //----- Create Transaction -----
    @Override
    public boolean createTransaction(String sender, String receiver, double amount, String message) 
            throws IOException, InterruptedException {
        return createTransaction(sender, receiver, amount, message, null);
    }

    //----- Create Transaction -----
    @Override
    public boolean createTransaction(String sender, String receiver, double amount, String message, String sourceAccountType) 
            throws IOException, InterruptedException {
        Map<String, String> transactionData = new HashMap<>();
        transactionData.put("sender", sender);
        transactionData.put("receiver", receiver);
        transactionData.put("amount", String.valueOf(amount));
        transactionData.put("message", message);
        
        if (sourceAccountType != null && !sourceAccountType.isEmpty()) {
            transactionData.put("sourceAccountType", sourceAccountType);
        }
        
        try {
            ApiClient.post("/transactions", transactionData, Map.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //----- Get Sum of Income -----
    @Override
    public double getSumOfIncome(String payeeAddress) throws IOException, InterruptedException {
        try {
            Map<String, Object> summary = ApiClient.get("/transactions/" + payeeAddress + "/summary", Map.class);
            
            if (summary != null && summary.containsKey("income")) {
                return ((Number) summary.get("income")).doubleValue();
            }
            
            //----- If API Doesn't Provide Summary, Calculate Locally -----
            return calculateSumOfIncome(payeeAddress);
        } catch (Exception e) {
            System.err.println("Error getting sum of income: " + e.getMessage());
            //----- Fall Back to Local Calculation -----
            return calculateSumOfIncome(payeeAddress);
        }
    }

    //----- Get Sum of Expense -----
    @Override
    public double getSumOfExpense(String payeeAddress) throws IOException, InterruptedException {
        try {
            Map<String, Object> summary = ApiClient.get("/transactions/" + payeeAddress + "/summary", Map.class);
            
            if (summary != null && summary.containsKey("expense")) {
                return ((Number) summary.get("expense")).doubleValue();
            }
            
            //----- If API Doesn't Provide Summary, Calculate Locally -----
            return calculateSumOfExpense(payeeAddress);
        } catch (Exception e) {
            System.err.println("Error getting sum of expense: " + e.getMessage());
            //----- Fall Back to Local Calculation -----
            return calculateSumOfExpense(payeeAddress);
        }
    }
    
    //----- Map API Transaction Data to Transaction Object -----
    private Transaction mapToTransaction(Map<String, Object> apiTransaction) {
        String sender = (String) apiTransaction.get("sender");
        String receiver = (String) apiTransaction.get("receiver");
        double amount = ((Number) apiTransaction.get("amount")).doubleValue();
        String message = (String) apiTransaction.get("message");
        
        //----- Parse Date -----
        LocalDate date;
        try {
            date = LocalDate.parse((String) apiTransaction.get("date"));
        } catch (Exception e) {
            date = LocalDate.now();
            System.err.println("Error parsing transaction date: " + e.getMessage());
        }
        
        return new Transaction(sender, receiver, amount, date, message);
    }
    
    //----- Calculate Sum of Income -----
    private double calculateSumOfIncome(String payeeAddress) throws IOException, InterruptedException {
        List<Transaction> transactions = getAllTransactions(payeeAddress);
        double sum = 0;
        
        for (Transaction transaction : transactions) {
            if (transaction.getReceiver().equals(payeeAddress)) {
                sum += transaction.getAmount();
            }
        }
        
        return sum;
    }
    
    //----- Calculate Sum of Expense -----
    private double calculateSumOfExpense(String payeeAddress) throws IOException, InterruptedException {
        List<Transaction> transactions = getAllTransactions(payeeAddress);
        double sum = 0;
        
        for (Transaction transaction : transactions) {
            if (transaction.getSender().equals(payeeAddress)) {
                sum += transaction.getAmount();
            }
        }
        
        return sum;
    }
} 