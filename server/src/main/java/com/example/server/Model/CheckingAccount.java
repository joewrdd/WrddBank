package com.example.server.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//----- Checking Account Model -----
@Entity
@Table(name = "CheckingAccounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "AccountNumber", unique = true)
    private String accountNumber;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "Owner", referencedColumnName = "PayeeAddress")
    private Client owner;

    @Column(name = "TransactionLimit")
    private double transactionLimit;

    @Column(name = "Balance")
    private double balance;

    @Column(name = "LastTransactionDate")
    private LocalDate lastTransactionDate;

    @Column(name = "MonthlyFee")
    private double monthlyFee;

    @Column(name = "SalaryDomiciliation")
    private boolean salaryDomiciliation;

    @Column(name = "AccountStatus")
    private String accountStatus;
} 