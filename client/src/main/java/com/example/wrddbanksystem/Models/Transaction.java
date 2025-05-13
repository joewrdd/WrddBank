package com.example.wrddbanksystem.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Transaction {
    //----- Observable Properties -----
    private final StringProperty sender;
    private final StringProperty receiver;
    private final DoubleProperty amount;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty message;


    //----- Constructor -----
    public Transaction(String sender, String receiver, double amount,
                       LocalDate date, String message) {
        this.sender = new SimpleStringProperty(this, "Sender", sender);
        this.receiver = new SimpleStringProperty(this, "Receiver", receiver);
        this.amount = new SimpleDoubleProperty(this, "Amount", amount);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
        this.message = new SimpleStringProperty(this, "Message", message);
    }

    public String getSender() {
        return sender.get();
    }

    public StringProperty senderProperty() {
        return this.sender;
    }

    public String getReceiver() {
        return receiver.get();
    }

    public StringProperty receiverProperty() {
        return this.receiver;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return this.date;
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return this.amount;
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return this.message;
    }
}
