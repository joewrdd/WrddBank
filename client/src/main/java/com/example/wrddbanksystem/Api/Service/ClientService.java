package com.example.wrddbanksystem.Api.Service;

import com.example.wrddbanksystem.Models.Client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//----- Client Service Interface -----
public interface ClientService {
    
    //----- Get All Clients -----
    List<Client> getAllClients() throws IOException, InterruptedException;
    
    //----- Get Client by Payee Address -----
    Optional<Client> getClientByPayeeAddress(String payeeAddress) throws IOException, InterruptedException;
    
    //----- Create Client -----
    Client createClient(String firstName, String lastName, String payeeAddress, String password) 
            throws IOException, InterruptedException;
    
    //----- Create Client with Accounts -----
    boolean createClientWithAccounts(String firstName, String lastName, String payeeAddress, 
                                    String password, double checkingBalance, double savingsBalance) 
            throws IOException, InterruptedException;
    
    //----- Update Client -----
    boolean updateClient(String payeeAddress, String firstName, String lastName) 
            throws IOException, InterruptedException;
    
    //----- Update Password -----
    boolean updatePassword(String payeeAddress, String currentPassword, String newPassword) 
            throws IOException, InterruptedException;
    
    //----- Delete Client -----
    boolean deleteClient(String payeeAddress) throws IOException, InterruptedException;
    
    //----- Search Clients -----
    List<Client> searchClients(String searchTerm) throws IOException, InterruptedException;
} 