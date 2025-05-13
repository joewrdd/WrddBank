package com.example.server.Service;

import com.example.server.Model.CheckingAccount;
import com.example.server.Model.SavingsAccount;
import com.example.server.Model.Transaction;
import com.example.server.Repository.CheckingAccountRepository;
import com.example.server.Repository.SavingsAccountRepository;
import com.example.server.Repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

//----- Transaction Service -----
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CheckingAccountRepository checkingAccountRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountService accountService;

    //----- Get Transactions By Payee Address -----
    public List<Transaction> getTransactionsByPayeeAddress(String payeeAddress) {
        return transactionRepository.findBySenderOrReceiverOrderByDateDesc(payeeAddress);
    }

    //----- Get Latest Transactions -----
    public List<Transaction> getLatestTransactions(String payeeAddress, int limit) {
        return transactionRepository.findBySenderOrReceiverOrderByDateDesc(payeeAddress, PageRequest.of(0, limit));
    }

    //----- Calculate Income -----
    public double calculateIncome(String payeeAddress) {
        List<Transaction> transactions = getTransactionsByPayeeAddress(payeeAddress);
        double income = 0.0;
        
        for (Transaction transaction : transactions) {
            if (transaction.getReceiver().equals(payeeAddress)) {
                income += transaction.getAmount();
            }
        }
        
        return income;
    }

    //----- Calculate Expense -----
    public double calculateExpense(String payeeAddress) {
        List<Transaction> transactions = getTransactionsByPayeeAddress(payeeAddress);
        double expense = 0.0;
        
        for (Transaction transaction : transactions) {
            if (transaction.getSender().equals(payeeAddress)) {
                expense += transaction.getAmount();
            }
        }
        
        return expense;
    }

    //----- Create Transaction -----
    @Transactional
    public Transaction createTransaction(String sender, String receiver, double amount, String message, String sourceAccountType) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        
        if (sourceAccountType.equalsIgnoreCase("checking")) {
            return processCheckingTransaction(sender, receiver, amount, message);
        } else if (sourceAccountType.equalsIgnoreCase("savings")) {
            return processSavingsTransaction(sender, receiver, amount, message);
        } else {
            throw new IllegalArgumentException("Invalid account type: " + sourceAccountType);
        }
    }

    //----- Process Checking Transaction -----
    @Transactional
    private Transaction processCheckingTransaction(String sender, String receiver, double amount, String message) {
        // Find sender's checking account
        List<CheckingAccount> senderAccounts = checkingAccountRepository.findByOwnerPayeeAddress(sender);
        if (senderAccounts.isEmpty()) {
            throw new IllegalArgumentException("Sender has no checking account");
        }
        
        //----- Get The First Checking Account (Could Be Enhanced To Specify Which Account) -----
        CheckingAccount senderAccount = senderAccounts.get(0);
        
        //----- Check If Sender Has Enough Balance -----
        if (senderAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        //----- Check Transaction Limit -----
        if (amount > senderAccount.getTransactionLimit()) {
            throw new IllegalArgumentException("Transaction exceeds limit");
        }
        
        //----- Update Sender's Balance -----
        double newSenderBalance = senderAccount.getBalance() - amount;
        accountService.updateAccountBalance(senderAccount.getAccountNumber(), "checking", newSenderBalance);
        
        //----- Check If This Is A Transfer Between Accounts Of The Same Client -----
        boolean isSameClient = sender.equals(receiver);
        
        if (isSameClient) {
            //----- For Same Client, Transfer To Savings -----
            List<SavingsAccount> receiverSavingsAccounts = savingsAccountRepository.findByOwnerPayeeAddress(receiver);
            if (!receiverSavingsAccounts.isEmpty()) {
                //----- Update Receiver's Savings Account Balance -----
                SavingsAccount receiverSavingsAccount = receiverSavingsAccounts.get(0);
                double newReceiverBalance = receiverSavingsAccount.getBalance() + amount;
                accountService.updateAccountBalance(receiverSavingsAccount.getAccountNumber(), "savings", newReceiverBalance);
            }
        } else {
            //----- For Different Clients, Transfer To Checking (Standard Behavior) -----
            List<CheckingAccount> receiverAccounts = checkingAccountRepository.findByOwnerPayeeAddress(receiver);
            if (!receiverAccounts.isEmpty()) {
                //----- Update Receiver's Balance -----
                CheckingAccount receiverAccount = receiverAccounts.get(0);
                double newReceiverBalance = receiverAccount.getBalance() + amount;
                accountService.updateAccountBalance(receiverAccount.getAccountNumber(), "checking", newReceiverBalance);
            }
        }
        
        //----- Create And Save Transaction -----
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());
        transaction.setMessage(message);
        
        return transactionRepository.save(transaction);
    }

    //----- Process Savings Transaction -----
    @Transactional
    private Transaction processSavingsTransaction(String sender, String receiver, double amount, String message) {
        //----- Find Sender's Savings Account -----
        List<SavingsAccount> senderAccounts = savingsAccountRepository.findByOwnerPayeeAddress(sender);
        if (senderAccounts.isEmpty()) {
            throw new IllegalArgumentException("Sender has no savings account");
        }
        
        //----- Get The First Savings Account (Could Be Enhanced To Specify Which Account) -----
        SavingsAccount senderAccount = senderAccounts.get(0);
        
        //----- Check If Sender Has Enough Balance -----
        if (senderAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        //----- Check Withdrawal Limit -----
        if (amount > senderAccount.getWithdrawalLimit()) {
            throw new IllegalArgumentException("Transaction exceeds withdrawal limit");
        }
        
        //----- Update Sender's Balance -----
        double newSenderBalance = senderAccount.getBalance() - amount;
        accountService.updateAccountBalance(senderAccount.getAccountNumber(), "savings", newSenderBalance);
        
        //----- Check If This Is A Transfer Between Accounts Of The Same Client -----
        boolean isSameClient = sender.equals(receiver);
        
        if (isSameClient) {
            //----- For Same Client, Transfer To Checking -----
            List<CheckingAccount> receiverCheckingAccounts = checkingAccountRepository.findByOwnerPayeeAddress(receiver);
            if (!receiverCheckingAccounts.isEmpty()) {
                //----- Update Receiver's Checking Account Balance -----
                CheckingAccount receiverCheckingAccount = receiverCheckingAccounts.get(0);
                double newReceiverBalance = receiverCheckingAccount.getBalance() + amount;
                accountService.updateAccountBalance(receiverCheckingAccount.getAccountNumber(), "checking", newReceiverBalance);
            }
        } else {
            //----- For Different Clients, Transfer To Checking (Standard Behavior) -----
            List<CheckingAccount> receiverAccounts = checkingAccountRepository.findByOwnerPayeeAddress(receiver);
            if (!receiverAccounts.isEmpty()) {
                //----- Update Receiver's Balance -----
                CheckingAccount receiverAccount = receiverAccounts.get(0);
                double newReceiverBalance = receiverAccount.getBalance() + amount;
                accountService.updateAccountBalance(receiverAccount.getAccountNumber(), "checking", newReceiverBalance);
            }
        }
        
        //----- Create And Save Transaction -----
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());
        transaction.setMessage(message);
        
        return transactionRepository.save(transaction);
    }
    
    //----- Save Transaction -----
    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
} 