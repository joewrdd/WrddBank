package com.example.server.Service;

import com.example.server.Model.Client;
import com.example.server.Repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//----- Client Service -----
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    //----- Get All Clients -----
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    //----- Get Client By Payee Address -----
    public Optional<Client> getClientByPayeeAddress(String payeeAddress) {
        return clientRepository.findByPayeeAddress(payeeAddress);
    }

    //----- Authenticate Client -----
    public Optional<Client> authenticateClient(String payeeAddress, String password) {
        return clientRepository.findByPayeeAddressAndPassword(payeeAddress, password);
    }

    //----- Create Client -----
    @Transactional
    public Client createClient(String firstName, String lastName, String payeeAddress, String password) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setPayeeAddress(payeeAddress);
        client.setPassword(password);
        client.setDateCreated(LocalDate.now());
        
        return clientRepository.save(client);
    }

    //----- Update Client Info -----
    @Transactional
    public boolean updateClientInfo(String payeeAddress, String firstName, String lastName) {
        Optional<Client> clientOpt = clientRepository.findByPayeeAddress(payeeAddress);
        
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setFirstName(firstName);
            client.setLastName(lastName);
            clientRepository.save(client);
            return true;
        }
        
        return false;
    }

    //----- Update Client Password -----
    @Transactional
    public boolean updateClientPassword(String payeeAddress, String newPassword) {
        Optional<Client> clientOpt = clientRepository.findByPayeeAddress(payeeAddress);
        
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setPassword(newPassword);
            clientRepository.save(client);
            return true;
        }
        
        return false;
    }

    //----- Delete Client -----
    @Transactional
    public boolean deleteClient(String payeeAddress) {
        Optional<Client> clientOpt = clientRepository.findByPayeeAddress(payeeAddress);
        
        if (clientOpt.isPresent()) {
            clientRepository.delete(clientOpt.get());
            return true;
        }
        
        return false;
    }
} 