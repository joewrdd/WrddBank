package com.example.server.Repository;

import com.example.server.Model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//----- Savings Account Repository -----
@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    //----- Find Savings Accounts By Owner Payee Address -----
    List<SavingsAccount> findByOwnerPayeeAddress(String payeeAddress);

    //----- Find Savings Account By Account Number -----
    Optional<SavingsAccount> findByAccountNumber(String accountNumber);
    
    //----- Sum The Balance Of All Savings Accounts In The System -----
    @Query("SELECT SUM(s.balance) FROM SavingsAccount s")
    Optional<Double> sumBalance();
} 