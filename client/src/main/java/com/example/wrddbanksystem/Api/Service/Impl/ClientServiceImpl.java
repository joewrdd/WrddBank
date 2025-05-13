package com.example.wrddbanksystem.Api.Service.Impl;

import com.example.wrddbanksystem.Api.Service.ClientService;
import com.example.wrddbanksystem.Models.ApiClient;
import com.example.wrddbanksystem.Models.Client;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//----- Client Service Implementation -----
public class ClientServiceImpl implements ClientService {

    //----- Get All Clients -----
    @Override
    public List<Client> getAllClients() throws IOException, InterruptedException {
        List<Map<String, Object>> apiClients = ApiClient.get("/clients", List.class);
        List<Client> clients = new ArrayList<>();
        
        if (apiClients != null) {
            for (Map<String, Object> apiClient : apiClients) {
                clients.add(mapToClient(apiClient));
            }
        }
        
        return clients;
    }

    //----- Get Client by Payee Address -----
    @Override
    public Optional<Client> getClientByPayeeAddress(String payeeAddress) throws IOException, InterruptedException {
        try {
            Map<String, Object> apiClient = ApiClient.get("/clients/" + payeeAddress, Map.class);
            
            if (apiClient != null) {
                Client client = mapToClient(apiClient);
                return Optional.of(client);
            }
        } catch (Exception e) {
            System.err.println("Error fetching client: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    //----- Create Client -----
    @Override
    public Client createClient(String firstName, String lastName, String payeeAddress, String password) 
            throws IOException, InterruptedException {
        Map<String, String> clientData = new HashMap<>();
        clientData.put("firstName", firstName);
        clientData.put("lastName", lastName);
        clientData.put("payeeAddress", payeeAddress);
        clientData.put("password", password);
        
        Map<String, Object> newClient = ApiClient.post("/clients", clientData, Map.class);
        
        if (newClient != null) {
            return mapToClient(newClient);
        }
        
        throw new IOException("Failed to create client");
    }

    //----- Create Client with Accounts -----
    @Override
    public boolean createClientWithAccounts(String firstName, String lastName, String payeeAddress, 
                                           String password, double checkingBalance, double savingsBalance) 
            throws IOException, InterruptedException {
        //----- First Create the Client -----
        Client client = createClient(firstName, lastName, payeeAddress, password);
        
        if (client != null) {
            //----- Create Checking Account if Balance > 0 -----
            if (checkingBalance > 0) {
                Map<String, Object> checkingData = new HashMap<>();
                checkingData.put("ownerPayeeAddress", payeeAddress);
                checkingData.put("transactionLimit", 1000.0); // Default transaction limit
                checkingData.put("initialBalance", checkingBalance);
                
                ApiClient.post("/accounts/checking", checkingData, Map.class);
            }
            
            //----- Create Savings Account if Balance > 0 -----
            if (savingsBalance > 0) {
                Map<String, Object> savingsData = new HashMap<>();
                savingsData.put("ownerPayeeAddress", payeeAddress);
                savingsData.put("withdrawalLimit", 500.0); // Default withdrawal limit
                savingsData.put("initialBalance", savingsBalance);
                
                ApiClient.post("/accounts/savings", savingsData, Map.class);
            }
            
            return true;
        }
        
        return false;
    }

    //----- Update Client -----
    @Override
    public boolean updateClient(String payeeAddress, String firstName, String lastName) 
            throws IOException, InterruptedException {
        Map<String, String> clientData = new HashMap<>();
        clientData.put("firstName", firstName);
        clientData.put("lastName", lastName);
        
        try {
            ApiClient.put("/clients/" + payeeAddress, clientData, Map.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating client: " + e.getMessage());
            return false;
        }
    }

    //----- Update Password -----
    @Override
    public boolean updatePassword(String payeeAddress, String currentPassword, String newPassword) 
            throws IOException, InterruptedException {
        Map<String, String> passwordData = new HashMap<>();
        passwordData.put("currentPassword", currentPassword);
        passwordData.put("newPassword", newPassword);
        
        try {
            ApiClient.put("/clients/" + payeeAddress + "/password", passwordData, Map.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    //----- Delete Client -----
    @Override
    public boolean deleteClient(String payeeAddress) throws IOException, InterruptedException {
        try {
            ApiClient.delete("/clients/" + payeeAddress, Void.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting client: " + e.getMessage());
            return false;
        }
    }

    //----- Search Clients -----
    @Override
    public List<Client> searchClients(String searchTerm) throws IOException, InterruptedException {
        List<Map<String, Object>> apiClients = ApiClient.get("/clients/search?term=" + searchTerm, List.class);
        List<Client> clients = new ArrayList<>();
        
        if (apiClients != null) {
            for (Map<String, Object> apiClient : apiClients) {
                clients.add(mapToClient(apiClient));
            }
        }
        
        return clients;
    }

    //----- Map Client to Client Object -----
    private Client mapToClient(Map<String, Object> apiClient) {
        String firstName = (String) apiClient.get("firstName");
        String lastName = (String) apiClient.get("lastName");
        String payeeAddress = (String) apiClient.get("payeeAddress");
        
        //----- Parse Date -----
        LocalDate dateCreated;
        try {
            dateCreated = LocalDate.parse((String) apiClient.get("dateCreated"));
        } catch (Exception e) {
            dateCreated = LocalDate.now();
            System.err.println("Error parsing client creation date: " + e.getMessage());
        }
        
        return new Client(firstName, lastName, payeeAddress, null, null, dateCreated);
    }
} 