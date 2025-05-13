package com.example.wrddbanksystem.Api.Service;

import java.io.IOException;
import java.util.Map;

//----- Admin Service Interface -----
public interface AdminService {
    
    //----- Get Statistics -----
    Map<String, Object> getStatistics() throws IOException, InterruptedException;
    
    //----- Get Transaction Summary -----
    Map<String, Object> getTransactionSummary(String period) throws IOException, InterruptedException;
    
    //----- Get Account Growth -----
    Map<String, Object> getAccountGrowth() throws IOException, InterruptedException;
    
    //----- Get System Health -----
    Map<String, Object> getSystemHealth() throws IOException, InterruptedException;
    
    //----- Perform Maintenance -----
    boolean performMaintenance(String maintenanceType) throws IOException, InterruptedException;
    
    //----- Create Deposit -----
    boolean createDeposit(String payeeAddress, String accountNumber, String accountType, 
                        double amount, String description) throws IOException, InterruptedException;
} 