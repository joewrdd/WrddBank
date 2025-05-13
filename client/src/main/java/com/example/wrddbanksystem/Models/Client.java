package com.example.wrddbanksystem.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty payeeAddress;
    private final ObjectProperty<Account> checkingAccount;
    private final ObjectProperty<Account> savingsAccount;
    private final ObjectProperty<LocalDate> dateCreated;

    //----- Constructor -----
    public Client(String fName, String lName, String payeeAddress, Account checkingAccount, Account savingsAccount, LocalDate createdDate) {
        this.firstName = new SimpleStringProperty(this, "FirstName", fName);
        this.lastName = new SimpleStringProperty(this, "LastName", lName);
        this.payeeAddress = new SimpleStringProperty(this, "Payee Address", payeeAddress);
        this.checkingAccount = new SimpleObjectProperty<>(this, "Checking Account", checkingAccount);
        this.savingsAccount = new SimpleObjectProperty<>(this, "Savings Account", savingsAccount);
        this.dateCreated = new SimpleObjectProperty<>(this, "Date Created", createdDate);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return this.firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return this.lastName;
    }

    public String getPayeeAddress() {
        return payeeAddress.get();
    }

    public StringProperty payeeAddressProperty() {
        return this.payeeAddress;
    }

    public Account getCheckingAccount() {
        return checkingAccount.get();
    }

    public ObjectProperty<Account> checkingAccountProperty() {
        return this.checkingAccount;
    }

    public Account getSavingsAccount() {
        return savingsAccount.get();
    }

    public ObjectProperty<Account> savingsAccountProperty() {
        return this.savingsAccount;
    }

    public LocalDate getDateCreated() {
        return dateCreated.get();
    }

    public ObjectProperty<LocalDate> dateCreatedProperty() {
        return this.dateCreated;
    }
}
