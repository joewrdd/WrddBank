package com.example.wrddbanksystem.Api.Service;

import com.example.wrddbanksystem.Models.Client;

import java.io.IOException;
import java.util.Optional;

//----- Auth Service Interface -----
public interface AuthService {
    
    //----- Authenticate Client -----
        Optional<Client> authenticateClient(String payeeAddress, String password)  
            throws IOException, InterruptedException;
    
    //----- Authenticate Admin -----
    boolean authenticateAdmin(String username, String password) 
            throws IOException, InterruptedException;
    
    //----- Logout -----
    void logout();
    
    //----- Validate Session -----
    boolean validateSession() throws IOException, InterruptedException;
} 