package com.example.server.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//----- Savings Account Model -----
@Entity
@Table(name = "SavingsAccounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "AccountNumber", unique = true)
    private String accountNumber;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "Owner", referencedColumnName = "PayeeAddress")
    private Client owner;

    @Column(name = "WithdrawalLimit")
    private double withdrawalLimit;

    @Column(name = "Balance")
    private double balance;

    @Column(name = "InterestRate")
    private double interestRate;

    @Column(name = "CompoundingFrequency")
    private String compoundingFrequency;

    @Column(name = "GoalAmount")
    private double goalAmount;

    @Column(name = "AccountStatus")
    private String accountStatus;
} 