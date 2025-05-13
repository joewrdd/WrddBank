package com.example.server.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//----- Transaction Model ----- 
@Entity
@Table(name = "Transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Sender")
    private String sender;

    @Column(name = "Receiver")
    private String receiver;

    @Column(name = "Amount")
    private double amount;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "Message")
    private String message;
} 