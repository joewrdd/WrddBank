package com.example.wrddbanksystem.Models;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;

public class CheckingAccounts extends Account {
    private final DoubleProperty transactionLimit; 
    private final ObjectProperty<LocalDateTime> lastTransactionDate;
    private final DoubleProperty monthlyFee; 
    private final BooleanProperty salaryDomiciliation;

    //----- Basic Constructor For Backward Compatibility -----
    public CheckingAccounts(String accountNumber, String owner, double transactionLimit, double balance) {
        super(owner, accountNumber, balance);
        this.transactionLimit = new SimpleDoubleProperty(this, "Transaction Limit", transactionLimit);
        this.lastTransactionDate = new SimpleObjectProperty<>(this, "Last Transaction Date", LocalDateTime.now());
        this.monthlyFee = new SimpleDoubleProperty(this, "Monthly Fee", 0.0);
        this.salaryDomiciliation = new SimpleBooleanProperty(this, "Salary Domiciliation", false);
    }
    
    //----- Full Constructor With All New Properties -----
    public CheckingAccounts(String accountNumber, String owner, double transactionLimit, double balance,
                           LocalDateTime lastTransactionDate, double monthlyFee, boolean salaryDomiciliation) {
        super(owner, accountNumber, balance);
        this.transactionLimit = new SimpleDoubleProperty(this, "Transaction Limit", transactionLimit);
        this.lastTransactionDate = new SimpleObjectProperty<>(this, "Last Transaction Date", 
                                  lastTransactionDate != null ? lastTransactionDate : LocalDateTime.now());
        this.monthlyFee = new SimpleDoubleProperty(this, "Monthly Fee", monthlyFee);
        this.salaryDomiciliation = new SimpleBooleanProperty(this, "Salary Domiciliation", salaryDomiciliation);
    }
    
    //----- Full Constructor With Account Status -----
    public CheckingAccounts(String accountNumber, String owner, double transactionLimit, double balance,
                           LocalDateTime lastTransactionDate, double monthlyFee, boolean salaryDomiciliation, 
                           AccountStatus status) {
        super(owner, accountNumber, balance, status);
        this.transactionLimit = new SimpleDoubleProperty(this, "Transaction Limit", transactionLimit);
        this.lastTransactionDate = new SimpleObjectProperty<>(this, "Last Transaction Date", 
                                  lastTransactionDate != null ? lastTransactionDate : LocalDateTime.now());
        this.monthlyFee = new SimpleDoubleProperty(this, "Monthly Fee", monthlyFee);
        this.salaryDomiciliation = new SimpleBooleanProperty(this, "Salary Domiciliation", salaryDomiciliation);
    }

   
    public DoubleProperty transactionLimitProperty() {
        return transactionLimit;
    }
    
    public double getTransactionLimit() {
        return transactionLimit.get();
    }
    
    public void setTransactionLimit(double limit) {
        this.transactionLimit.set(limit);
    }
    
    public ObjectProperty<LocalDateTime> lastTransactionDateProperty() {
        return lastTransactionDate;
    }
    
    public LocalDateTime getLastTransactionDate() {
        return lastTransactionDate.get();
    }
    
    public void setLastTransactionDate(LocalDateTime date) {
        this.lastTransactionDate.set(date);
    }
    
    @SuppressWarnings("exports")
    public DoubleProperty monthlyFeeProperty() {
        return monthlyFee;
    }
    
    public double getMonthlyFee() {
        return monthlyFee.get();
    }
    
    public void setMonthlyFee(double fee) {
        this.monthlyFee.set(fee);
    }
    
    @SuppressWarnings("exports")
    public BooleanProperty salaryDomiciliationProperty() {
        return salaryDomiciliation;
    }
    
    public boolean hasSalaryDomiciliation() {
        return salaryDomiciliation.get();
    }
    
    public void setSalaryDomiciliation(boolean hasSalaryDomiciliation) {
        this.salaryDomiciliation.set(hasSalaryDomiciliation);
    }
    
    //----- Method To Update Last Transaction Date -----
    public void updateLastTransactionDate() {
        this.lastTransactionDate.set(LocalDateTime.now());
    }
    
    //----- Method To Calculate If The Monthly Fee Should Be Waived -----
    public boolean shouldWaiveMonthlyFee() {
        if (hasSalaryDomiciliation()) {
            return true;
        }
        
        if (getBalance() >= 1000.0) {
            return true;
        }
        
        return false;
    }

    //----- Method To Return The Account Number -----
    @Override
    public String toString() {
        return accountNumberProperty().get();
    }

}
