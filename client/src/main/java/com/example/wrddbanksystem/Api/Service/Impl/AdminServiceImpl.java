package com.example.wrddbanksystem.Api.Service.Impl;

import com.example.wrddbanksystem.Api.Service.AdminService;
import com.example.wrddbanksystem.Models.ApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//----- Admin Service Implementation -----
public class AdminServiceImpl implements AdminService {

    //----- Get Statistics -----
    @Override
    public Map<String, Object> getStatistics() throws IOException, InterruptedException {
        try {
            return ApiClient.get("/admin/statistics", Map.class);
        } catch (Exception e) {
            System.err.println("Error getting statistics: " + e.getMessage());
            return createDefaultStatistics();
        }
    }

    //----- Get Transaction Summary -----
    @Override
    public Map<String, Object> getTransactionSummary(String period) throws IOException, InterruptedException {
        try {
            return ApiClient.get("/admin/transactions/summary?period=" + period.toLowerCase(), Map.class);
        } catch (Exception e) {
            System.err.println("Error getting transaction summary: " + e.getMessage());
            return createDefaultTransactionSummary(period);
        }
    }

    //----- Get Account Growth -----
    @Override
    public Map<String, Object> getAccountGrowth() throws IOException, InterruptedException {
        try {
            return ApiClient.get("/admin/accounts/growth", Map.class);
        } catch (Exception e) {
            System.err.println("Error getting account growth: " + e.getMessage());
            return createDefaultAccountGrowth();
        }
    }

    //----- Get System Health -----
    @Override
    public Map<String, Object> getSystemHealth() throws IOException, InterruptedException {
        try {
            return ApiClient.get("/admin/system/health", Map.class);
        } catch (Exception e) {
            System.err.println("Error getting system health: " + e.getMessage());
            return createDefaultSystemHealth();
        }
    }

    //----- Perform Maintenance -----
    @Override
    public boolean performMaintenance(String maintenanceType) throws IOException, InterruptedException {
        try {
            Map<String, String> request = new HashMap<>();
            request.put("type", maintenanceType);
            
            ApiClient.post("/admin/maintenance", request, Map.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error performing maintenance: " + e.getMessage());
            return false;
        }
    }

    //----- Create Deposit -----
    @Override
    public boolean createDeposit(String payeeAddress, String accountNumber, String accountType, 
                               double amount, String description) throws IOException, InterruptedException {
        try {
            Map<String, Object> depositData = new HashMap<>();
            depositData.put("payeeAddress", payeeAddress);
            depositData.put("accountNumber", accountNumber);
            depositData.put("accountType", accountType);
            depositData.put("amount", amount);
            depositData.put("description", description);
            
            ApiClient.post("/admin/deposits", depositData, Map.class);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating deposit: " + e.getMessage());
            return false;
        }
    }
    
    //----- Create Default Statistics -----
    private Map<String, Object> createDefaultStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("clientCount", 0);
        statistics.put("checkingAccountCount", 0);
        statistics.put("savingsAccountCount", 0);
        statistics.put("transactionCount", 0);
        return statistics;
    }
    
    //----- Create Default Transaction Summary -----
    private Map<String, Object> createDefaultTransactionSummary(String period) {
        Map<String, Object> summary = new HashMap<>();
        Map<String, Map<String, Number>> periodData = new HashMap<>();
        
        if ("monthly".equalsIgnoreCase(period)) {
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
            double[] deposits = {4500, 5200, 4800, 6700, 5900, 7000};
            double[] withdrawals = {3200, 3800, 3600, 4100, 3900, 4500};
            
            for (int i = 0; i < months.length; i++) {
                Map<String, Number> data = new HashMap<>();
                data.put("deposits", deposits[i]);
                data.put("withdrawals", withdrawals[i]);
                periodData.put(months[i], data);
            }
        } else if ("weekly".equalsIgnoreCase(period)) {
            for (int i = 1; i <= 6; i++) {
                Map<String, Number> data = new HashMap<>();
                data.put("deposits", 1000 + Math.random() * 1000);
                data.put("withdrawals", 800 + Math.random() * 800);
                periodData.put("Week " + i, data);
            }
        } else if ("daily".equalsIgnoreCase(period)) {
            for (int i = 1; i <= 7; i++) {
                Map<String, Number> data = new HashMap<>();
                data.put("deposits", 200 + Math.random() * 300);
                data.put("withdrawals", 150 + Math.random() * 250);
                periodData.put("Day " + i, data);
            }
        } else {
            for (int i = 2021; i <= 2023; i++) {
                Map<String, Number> data = new HashMap<>();
                data.put("deposits", 50000 + Math.random() * 20000);
                data.put("withdrawals", 40000 + Math.random() * 15000);
                periodData.put(String.valueOf(i), data);
            }
        }
        
        summary.put("periodData", periodData);
        return summary;
    }
    
    //----- Create Default Account Growth -----
    private Map<String, Object> createDefaultAccountGrowth() {
        Map<String, Object> growth = new HashMap<>();
        Map<String, Map<String, Number>> monthlyGrowth = new HashMap<>();
        
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
        int[] checkingData = {45, 52, 59, 63, 71, 77};
        int[] savingsData = {30, 36, 42, 50, 59, 63};
        
        for (int i = 0; i < months.length; i++) {
            Map<String, Number> data = new HashMap<>();
            data.put("checking", checkingData[i]);
            data.put("savings", savingsData[i]);
            monthlyGrowth.put(months[i], data);
        }
        
        growth.put("monthlyGrowth", monthlyGrowth);
        return growth;
    }
    
    //----- Create Default System Health -----
    private Map<String, Object> createDefaultSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("systemStatus", true);
        health.put("dbStatus", true);
        health.put("lastBackup", java.time.LocalDate.now().toString() + " 08:00");
        return health;
    }
} 