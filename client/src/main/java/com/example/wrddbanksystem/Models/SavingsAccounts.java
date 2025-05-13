package com.example.wrddbanksystem.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SavingsAccounts extends Account {
    //----- Enum For Compounding Frequency -----
    public enum CompoundingFrequency {
        DAILY,
        MONTHLY,
        ANNUALLY
    }

    private final DoubleProperty withdrawalLimit;
    private final DoubleProperty interestRate; 
    private final ObjectProperty<CompoundingFrequency> compoundingFrequency; 
    private final DoubleProperty goalAmount; 

    //----- Constructor For Savings Accounts -----
    public SavingsAccounts(String owner, String accountNumber, double balance, double withdrawalLimit) {
        super(owner, accountNumber, balance);
        this.withdrawalLimit = new SimpleDoubleProperty(this, "Withdrawal Limit", withdrawalLimit);
        this.interestRate = new SimpleDoubleProperty(this, "Interest Rate", 0.0);
        this.compoundingFrequency = new SimpleObjectProperty<>(this, "Compounding Frequency", CompoundingFrequency.MONTHLY);
        this.goalAmount = new SimpleDoubleProperty(this, "Goal Amount", 0.0);
    }

    //----- Constructor For Savings Accounts With All Parameters -----
    public SavingsAccounts(String owner, String accountNumber, double balance, double withdrawalLimit, 
                          double interestRate, CompoundingFrequency frequency, double goalAmount) {
        super(owner, accountNumber, balance);
        this.withdrawalLimit = new SimpleDoubleProperty(this, "Withdrawal Limit", withdrawalLimit);
        this.interestRate = new SimpleDoubleProperty(this, "Interest Rate", interestRate);
        this.compoundingFrequency = new SimpleObjectProperty<>(this, "Compounding Frequency", frequency);
        this.goalAmount = new SimpleDoubleProperty(this, "Goal Amount", goalAmount);
    }
    
    //----- Constructor For Savings Accounts With All Parameters And Account Status -----
    public SavingsAccounts(String owner, String accountNumber, double balance, double withdrawalLimit, 
                          double interestRate, CompoundingFrequency frequency, double goalAmount, AccountStatus status) {
        super(owner, accountNumber, balance, status);
        this.withdrawalLimit = new SimpleDoubleProperty(this, "Withdrawal Limit", withdrawalLimit);
        this.interestRate = new SimpleDoubleProperty(this, "Interest Rate", interestRate);
        this.compoundingFrequency = new SimpleObjectProperty<>(this, "Compounding Frequency", frequency);
        this.goalAmount = new SimpleDoubleProperty(this, "Goal Amount", goalAmount);
    }

    //----- Method To Get Withdrawal Limit Property -----
    public DoubleProperty withdrawalLimitProperty() {
        return withdrawalLimit;
    }
    
    //----- Method To Get Withdrawal Limit -----
    public double getWithdrawalLimit() {
        return withdrawalLimit.get();
    }

    //----- Method To Set Withdrawal Limit -----
    public void setWithdrawalLimit(double limit) {
        this.withdrawalLimit.set(limit);
    }
    
    //----- Method To Get Interest Rate Property -----
    public DoubleProperty interestRateProperty() {
        return interestRate;
    }

    //----- Method To Get Interest Rate -----
    public double getInterestRate() {
        return interestRate.get();
    }
    
    //----- Method To Set Interest Rate -----
    public void setInterestRate(double rate) {
        this.interestRate.set(rate);
    }
    
    //----- Method To Get Compounding Frequency Property -----
    public ObjectProperty<CompoundingFrequency> compoundingFrequencyProperty() {
        return compoundingFrequency;
    }

    //----- Method To Get Compounding Frequency -----
    public CompoundingFrequency getCompoundingFrequency() {
        return compoundingFrequency.get();
    }
    
    //----- Method To Set Compounding Frequency -----
    public void setCompoundingFrequency(CompoundingFrequency frequency) {
        this.compoundingFrequency.set(frequency);
    }
    
    //----- Method To Get Goal Amount Property -----
    public DoubleProperty goalAmountProperty() {
        return goalAmount;
    }
    
    //----- Method To Get Goal Amount -----
    public double getGoalAmount() {
        return goalAmount.get();
    }
    
    //----- Method To Set Goal Amount -----
    public void setGoalAmount(double amount) {
        this.goalAmount.set(amount);
    }
    
    //----- Method To Calculate Next Interest Payment -----
    public double calculateNextInterestPayment() {
        double balance = getBalance();
        double rate = getInterestRate() / 100.0; 
        
        switch (getCompoundingFrequency()) {
            case DAILY:
                return balance * (Math.pow(1 + rate/365, 1) - 1);
            case MONTHLY:
                return balance * (Math.pow(1 + rate/12, 1) - 1);
            case ANNUALLY:
                return balance * rate / 12;
            default:
                return 0.0;
        }
    }
    
    //----- Method To Project Balance After A Specified Number Of Months -----
    public double projectBalance(int months) {
        double projectedBalance = getBalance();
        double rate = getInterestRate() / 100.0; 
        
        switch (getCompoundingFrequency()) {
            case DAILY:
                projectedBalance *= Math.pow(1 + rate/365, 30 * months);
                break;
            case MONTHLY:
                projectedBalance *= Math.pow(1 + rate/12, months);
                break;
            case ANNUALLY:
                projectedBalance *= Math.pow(1 + rate/12, months);
                break;
        }
        
        return projectedBalance;
    }

    //----- Method To Return The Account Number -----
    @Override
    public String toString() {
        return accountNumberProperty().get();
    }
}
