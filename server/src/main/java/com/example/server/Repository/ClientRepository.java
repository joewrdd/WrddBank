package com.example.server.Repository;

import com.example.server.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//----- Client Repository -----
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    //----- Find Client By Payee Address -----
    Optional<Client> findByPayeeAddress(String payeeAddress);

    //----- Find Client By Payee Address And Password -----
    Optional<Client> findByPayeeAddressAndPassword(String payeeAddress, String password);
} 