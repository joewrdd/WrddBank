package com.example.server.Repository;

import com.example.server.Model.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//----- Checking Account Repository -----
@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {
    //----- Find Checking Accounts By Owner Payee Address -----
    List<CheckingAccount> findByOwnerPayeeAddress(String payeeAddress);

    //----- Find Checking Account By Account Number -----
    Optional<CheckingAccount> findByAccountNumber(String accountNumber);
    
    //----- Sum The Balance Of All Checking Accounts In The System -----
    @Query("SELECT SUM(c.balance) FROM CheckingAccount c")
    Optional<Double> sumBalance();
} 