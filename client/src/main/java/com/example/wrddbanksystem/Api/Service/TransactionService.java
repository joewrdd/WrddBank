package com.example.wrddbanksystem.Api.Service;

import com.example.wrddbanksystem.Models.Transaction;

import java.io.IOException;
import java.util.List;

//----- Transaction Service Interface -----
public interface TransactionService {
    
    //----- Get Latest Transactions for a Client -----
    List<Transaction> getTransactions(String payeeAddress, int limit) throws IOException, InterruptedException;
    
    //----- Get All Transactions for a Client -----
    List<Transaction> getAllTransactions(String payeeAddress) throws IOException, InterruptedException;
    
    //----- Create a New Transaction -----
    boolean createTransaction(String sender, String receiver, double amount, String message) 
            throws IOException, InterruptedException;
    
    //----- Create a New Transaction with Source Account Type Specified -----
    boolean createTransaction(String sender, String receiver, double amount, String message, String sourceAccountType) 
            throws IOException, InterruptedException;
    
    //----- Get the Sum of Income for a Client -----
    double getSumOfIncome(String payeeAddress) throws IOException, InterruptedException;
    
    //----- Get the Sum of Expense for a Client -----
    double getSumOfExpense(String payeeAddress) throws IOException, InterruptedException;
} 