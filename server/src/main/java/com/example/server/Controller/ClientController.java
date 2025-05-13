package com.example.server.Controller;

import com.example.server.Model.Client;
import com.example.server.Service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//----- Client Controller -----
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    //----- Get All Clients -----
    @GetMapping
    public ResponseEntity<?> getAllClients() {
        try {
            List<Client> clients = clientService.getAllClients();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve clients: " + e.getMessage()));
        }
    }

    //----- Get Client By Payee Address -----
    @GetMapping("/{payeeAddress}")
    public ResponseEntity<?> getClientByPayeeAddress(@PathVariable String payeeAddress) {
        try {
            Optional<Client> client = clientService.getClientByPayeeAddress(payeeAddress);
            if (client.isPresent()) {
                return ResponseEntity.ok(client.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Client not found with payee address: " + payeeAddress));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve client: " + e.getMessage()));
        }
    }

    //----- Search Clients -----
    @GetMapping("/search")
    public ResponseEntity<?> searchClients(@RequestParam("term") String searchTerm) {
        try {
            List<Client> allClients = clientService.getAllClients();
            
            //----- Search Clients By Payee Address (Exact Or Containing) -----
            // Also Search By First Name Or Last Name 
            List<Client> matchingClients = allClients.stream()
                .filter(client -> 
                    (client.getPayeeAddress() != null && 
                     (client.getPayeeAddress().equals(searchTerm) || 
                      client.getPayeeAddress().contains(searchTerm) ||
                      searchTerm.equals("@" + client.getPayeeAddress().substring(1)))) || 
                    (client.getFirstName() != null && 
                     client.getFirstName().toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (client.getLastName() != null && 
                     client.getLastName().toLowerCase().contains(searchTerm.toLowerCase()))
                )
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(matchingClients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to search clients: " + e.getMessage()));
        }
    }

    //----- Authenticate Client -----
    @PostMapping("/auth")
    public ResponseEntity<?> authenticateClient(@RequestBody Map<String, String> credentials) {
        try {
            String payeeAddress = credentials.get("payeeAddress");
            String password = credentials.get("password");
            
            if (payeeAddress == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Payee address and password are required"));
            }
            
            Optional<Client> client = clientService.authenticateClient(payeeAddress, password);
            if (client.isPresent()) {
                return ResponseEntity.ok(client.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Authentication failed: " + e.getMessage()));
        }
    }

    //----- Create Client -----
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody Map<String, String> clientData) {
        try {
            String firstName = clientData.get("firstName");
            String lastName = clientData.get("lastName");
            String payeeAddress = clientData.get("payeeAddress");
            String password = clientData.get("password");
            
            if (firstName == null || lastName == null || payeeAddress == null || password == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "First name, last name, payee address and password are required"));
            }
            
            Client client = clientService.createClient(firstName, lastName, payeeAddress, password);
            return ResponseEntity.status(HttpStatus.CREATED).body(client);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create client: " + e.getMessage()));
        }
    }

    //----- Update Client Info -----
    @PutMapping("/{payeeAddress}/info")
    public ResponseEntity<?> updateClientInfo(
            @PathVariable String payeeAddress,
            @RequestBody Map<String, String> clientData) {
        
        try {
            String firstName = clientData.get("firstName");
            String lastName = clientData.get("lastName");
            
            if (firstName == null || lastName == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "First name and last name are required"));
            }
            
            boolean updated = clientService.updateClientInfo(payeeAddress, firstName, lastName);
            if (updated) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Client not found with payee address: " + payeeAddress));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update client info: " + e.getMessage()));
        }
    }

    //----- Update Client Password -----
    @PutMapping("/{payeeAddress}/password")
    public ResponseEntity<?> updateClientPassword(
            @PathVariable String payeeAddress,
            @RequestBody Map<String, String> passwordData) {
        
        try {
            String newPassword = passwordData.get("newPassword");
            
            if (newPassword == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "New password is required"));
            }
            
            boolean updated = clientService.updateClientPassword(payeeAddress, newPassword);
            if (updated) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Client not found with payee address: " + payeeAddress));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update password: " + e.getMessage()));
        }
    }

    //----- Delete Client -----
    @DeleteMapping("/{payeeAddress}")
    public ResponseEntity<?> deleteClient(@PathVariable String payeeAddress) {
        try {
            boolean deleted = clientService.deleteClient(payeeAddress);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Client not found with payee address: " + payeeAddress));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to delete client: " + e.getMessage()));
        }
    }
} 