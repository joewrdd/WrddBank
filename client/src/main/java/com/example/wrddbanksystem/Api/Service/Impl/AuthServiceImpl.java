package com.example.wrddbanksystem.Api.Service.Impl;

import com.example.wrddbanksystem.Api.Service.AuthService;
import com.example.wrddbanksystem.Models.ApiClient;
import com.example.wrddbanksystem.Models.Client;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//----- Auth Service Implementation -----
public class AuthServiceImpl implements AuthService {

    //----- Store Session Token -----
    private String sessionToken;

    //----- Authenticate Client -----
    @Override
    public Optional<Client> authenticateClient(String payeeAddress, String password) 
            throws IOException, InterruptedException {
        try {
            //----- Create Credentials Map -----
            Map<String, String> credentials = new HashMap<>();
            credentials.put("payeeAddress", payeeAddress);
            credentials.put("password", password);

            //----- Call Authentication API -----
            Map<String, Object> result = ApiClient.post("/clients/auth", credentials, Map.class);
            
            //----- If Successful, Create a Client from the Result -----
            if (result != null) {
                //----- Extract Client Data from Response -----
                String firstName = (String) result.get("firstName");
                String lastName = (String) result.get("lastName");
                
                //----- Convert Date String to LocalDate -----
                LocalDate dateCreated = null;
                String dateCreatedStr = (String) result.get("dateCreated");
                if (dateCreatedStr != null && !dateCreatedStr.isEmpty()) {
                    try {
                        dateCreated = LocalDate.parse(dateCreatedStr);
                    } catch (Exception e) {
                        dateCreated = LocalDate.now();
                        System.err.println("Error parsing client creation date: " + e.getMessage());
                    }
                }
                
                //----- If the Backend Provides a Session Token, Store it -----
                if (result.containsKey("token")) {
                    sessionToken = (String) result.get("token");
                    //----- Set the Token in ApiClient if it Supports Token-Based Auth -----
                }
                
                //----- Create Client Object from Response Data -----
                Client client = new Client(firstName, lastName, payeeAddress, null, null, dateCreated);
                return Optional.of(client);
            }
        } catch (Exception e) {
            System.err.println("Error in client authentication: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    //----- Authenticate Admin -----
    @Override
    public boolean authenticateAdmin(String username, String password) 
            throws IOException, InterruptedException {
        try {
            //----- Create Credentials Map for API Request -----
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);

            //----- Call Admin Authentication API -----
            Map<String, Object> result = ApiClient.post("/admin/auth", credentials, Map.class);
            
            //----- If the Backend Provides a Session Token, Store it -----
            if (result != null && result.containsKey("token")) {
                sessionToken = (String) result.get("token");
                //----- Set the Token in ApiClient if it Supports Token-Based Auth -----
                // ApiClient.setAuthToken(sessionToken);
            }
            
            return result != null;
        } catch (Exception e) {
            System.err.println("Error in admin authentication: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //----- Logout -----
    @Override
    public void logout() {
        try {
            //----- Call Logout API Endpoint if Available -----
            if (sessionToken != null && !sessionToken.isEmpty()) {
                ApiClient.post("/auth/logout", null, Void.class);
            }
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
        } finally {
            //----- Clear Session Token -----
            sessionToken = null;
            //----- Clear Token from ApiClient if it Supports Token-Based Auth -----
            // ApiClient.setAuthToken(null);
        }
    }

    //----- Validate Session -----
    @Override
    public boolean validateSession() throws IOException, InterruptedException {
        //----- If No Token, Session is Invalid -----
        if (sessionToken == null || sessionToken.isEmpty()) {
            return false;
        }
        
        try {
            //----- Call Session Validation API -----
            Map<String, Object> result = ApiClient.get("/auth/validate", Map.class);
            return result != null && Boolean.TRUE.equals(result.get("valid"));
        } catch (Exception e) {
            System.err.println("Error validating session: " + e.getMessage());
            return false;
        }
    }
} 