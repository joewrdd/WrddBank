package com.example.server.Repository;

import com.example.server.Model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//----- Transaction Repository -----
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //----- Find Transactions By Sender Or Receiver -----
    @Query("SELECT t FROM Transaction t WHERE t.sender = ?1 OR t.receiver = ?1 ORDER BY t.date DESC")
    List<Transaction> findBySenderOrReceiverOrderByDateDesc(String payeeAddress);

    //----- Find Transactions By Sender Or Receiver And Pageable -----
    @Query("SELECT t FROM Transaction t WHERE t.sender = ?1 OR t.receiver = ?1 ORDER BY t.date DESC")
    List<Transaction> findBySenderOrReceiverOrderByDateDesc(String payeeAddress, Pageable pageable);
} 