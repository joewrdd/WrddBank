package com.example.wrddbanksystem.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Account {
    //----- Enum For Account Status -----
    public enum AccountStatus {
        ACTIVE,
        SUSPENDED,
        CLOSED
    }

    private final StringProperty owner;
    private final StringProperty accountNumber;
    private final DoubleProperty balance;
    private final ObjectProperty<AccountStatus> status;

    //----- Constructor -----
    public Account(String owner, String accountNumber, double balance) {
        this.owner = new SimpleStringProperty(this, "Owner", owner);
        this.accountNumber = new SimpleStringProperty(this, "Account Number", accountNumber);
        this.balance = new SimpleDoubleProperty(this, "Balance", balance);
        this.status = new SimpleObjectProperty<>(this, "Account Status", AccountStatus.ACTIVE);
    }

    //----- Constructor With Account Status -----
    public Account(String owner, String accountNumber, double balance, AccountStatus status) {
        this.owner = new SimpleStringProperty(this, "Owner", owner);
        this.accountNumber = new SimpleStringProperty(this, "Account Number", accountNumber);
        this.balance = new SimpleDoubleProperty(this, "Balance", balance);
        this.status = new SimpleObjectProperty<>(this, "Account Status", status);
    }

    //----- Get The Owner -----
    public String getOwner() {
        return owner.get();
    }

    //----- Get The Owner Property -----
    public StringProperty ownerProperty() {
        return owner;
    }

    //----- Get The Account Number -----
    public String getAccountNumber() {
        return accountNumber.get();
    }

    //----- Get The Account Number Property -----
    public StringProperty accountNumberProperty() {
        return accountNumber;
    }

    //----- Get The Balance -----
    public double getBalance() {
        return balance.get();
    }

    //----- Get The Balance Property -----
    public DoubleProperty balanceProperty() {
        return balance;
    }

    //----- Set The Balance -----
    public void setBalance(double balance) {
        this.balance.set(balance);
    }

    //----- Get The Status -----
    public AccountStatus getStatus() {
        return status.get();
    }

    //----- Get The Status Property -----
    public ObjectProperty<AccountStatus> statusProperty() {
        return status;
    }

    //----- Set The Status -----
    public void setStatus(AccountStatus status) {
        this.status.set(status);
    }

    //----- Check If The Account Is Active -----
    public boolean isActive() {
        return status.get() == AccountStatus.ACTIVE;
    }
}
