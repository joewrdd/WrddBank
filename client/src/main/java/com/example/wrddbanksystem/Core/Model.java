package com.example.wrddbanksystem.Core;

import com.example.wrddbanksystem.Api.Service.AccountService;
import com.example.wrddbanksystem.Api.Service.AdminService;
import com.example.wrddbanksystem.Api.Service.AuthService;
import com.example.wrddbanksystem.Api.Service.ClientService;
import com.example.wrddbanksystem.Api.Service.ServiceFactory;
import com.example.wrddbanksystem.Api.Service.TransactionService;
import com.example.wrddbanksystem.Models.*;
import com.example.wrddbanksystem.Views.ViewFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

//----- Model Class -----
public class Model {
    private static Model model;
    private final ViewFactory viewFactory;

    //----- Service Instances -----
    private final AuthService authService;
    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AdminService adminService;

    //----- Client Data Fields Section -----
    private final Client client;
    private boolean clientLoginSuccessFlag;
    private final ObservableList<Transaction> latestTransactions;
    private final ObservableList<Transaction> allTransactions;

    //----- Admin Data Fields Section -----
    private boolean adminLoginSuccessFlag;
    private final ObservableList<Client> clients;

    private Model() {
        this.viewFactory = new ViewFactory();

        //----- Get Service Instances From ServiceFactory -----
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.authService = serviceFactory.getAuthService();
        this.clientService = serviceFactory.getClientService();
        this.accountService = serviceFactory.getAccountService();
        this.transactionService = serviceFactory.getTransactionService();
        this.adminService = serviceFactory.getAdminService();

        //----- Client Section -----
        this.clientLoginSuccessFlag = false;
        this.client = new Client("", "", "", null, null, null);
        this.latestTransactions = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();

        //----- Admin Section -----
        this.adminLoginSuccessFlag = false;
        this.clients = FXCollections.observableArrayList();
    }

    //----- Method To Get Instance Of Model -----
    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    //----- Method To Get View Factory -----
    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    /*
     * Client Method Section
     */

    //----- Method To Get Client Login Success Flag -----
    public boolean isClientLoginSuccessFlag() {
        return clientLoginSuccessFlag;
    }

    //----- Method To Set Client Login Success Flag -----
    public void setClientLoginSuccessFlag(boolean clientLoginSuccessFlag) {
        this.clientLoginSuccessFlag = clientLoginSuccessFlag;

        //----- Clear Client Data When Logging Out -----
        if (!clientLoginSuccessFlag) {
            clearClientData();
        }
    }

    //----- Method To Clear All Client Data On Logout -----
    public void clearClientData() {
        //----- Reset Client Properties -----
        this.client.firstNameProperty().set("");
        this.client.lastNameProperty().set("");
        this.client.payeeAddressProperty().set("");
        this.client.dateCreatedProperty().set(null);
        this.client.checkingAccountProperty().set(null);
        this.client.savingsAccountProperty().set(null);

        //----- Clear Transaction Lists -----
        this.latestTransactions.clear();
        this.allTransactions.clear();

        //----- Clear Token If Using API Authentication -----
        ApiClient.clearAuthToken();
    }

    //----- Method To Get Client -----
    public Client getClient() {
        return client;
    }

    //----- Method To Evaluate Client Credentials -----
    public void evaluateClientCredentials(String pAddress, String password) {
        try {
            //----- Clear Any Existing Client Data Before Login Attempt -----
            clearClientData();

            Optional<Client> clientOpt = authService.authenticateClient(pAddress, password);

            if (clientOpt.isPresent()) {
                //----- Copy Client Data From Authentication Response -----
                Client authenticatedClient = clientOpt.get();
                this.client.firstNameProperty().set(authenticatedClient.getFirstName());
                this.client.lastNameProperty().set(authenticatedClient.getLastName());
                this.client.payeeAddressProperty().set(authenticatedClient.getPayeeAddress());
                this.client.dateCreatedProperty().set(authenticatedClient.getDateCreated());

                //----- Load Accounts -----
                loadCheckingAccount(pAddress);
                loadSavingsAccount(pAddress);

                this.clientLoginSuccessFlag = true;
            } else {
                this.clientLoginSuccessFlag = false;
            }
        } catch (Exception e) {
            System.err.println("Error in evaluateClientCredentials: " + e.getMessage());
            e.printStackTrace();
            this.clientLoginSuccessFlag = false;
        }
    }

    //----- Method To Load Checking Account(s) -----
    private void loadCheckingAccount(String pAddress) {
        try {
            List<CheckingAccounts> accounts = accountService.getCheckingAccounts(pAddress);

            if (accounts != null && !accounts.isEmpty()) {
                //----- For Simplicity, We'll Use The First Checking Account -----
                this.client.checkingAccountProperty().set(accounts.get(0));
            }
        } catch (Exception e) {
            System.err.println("Error loading checking accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Load Savings Account(s) -----
    private void loadSavingsAccount(String pAddress) {
        try {
            List<SavingsAccounts> accounts = accountService.getSavingsAccounts(pAddress);

            if (accounts != null && !accounts.isEmpty()) {
                //----- For Simplicity, We'll Use The First Savings Account -----
                this.client.savingsAccountProperty().set(accounts.get(0));
            }
        } catch (Exception e) {
            System.err.println("Error loading savings accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Set Latest Transactions -----
    public void setLatestTransactions() {
        try {
            List<Transaction> transactions = transactionService.getTransactions(
                    this.client.getPayeeAddress(), 10);

            latestTransactions.clear();
            latestTransactions.addAll(transactions);
        } catch (Exception e) {
            System.err.println("Error setting latest transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Get Latest Transactions -----
    public ObservableList<Transaction> getLatestTransactions() {
        return latestTransactions;
    }

    //----- Method To Set All Transactions -----
    public void setAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions(
                    this.client.getPayeeAddress());

            allTransactions.clear();
            allTransactions.addAll(transactions);
        } catch (Exception e) {
            System.err.println("Error setting all transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Get All Transactions -----
    public ObservableList<Transaction> getAllTransactions() {
        return allTransactions;
    }

    //----- Method To Get Account Service -----
    public AccountService getAccountService() {
        return accountService;
    }

    //----- Method To Create Transaction -----
    public boolean createTransaction(String sender, String receiver, double amount, String message, String sourceAccountType) {
        try {
            boolean success = transactionService.createTransaction(sender, receiver, amount, message, sourceAccountType);

            if (success) {
                //----- Update Account Balances After Transaction -----
                refreshAccounts();
            }

            return success;
        } catch (Exception e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //----- Method To Refresh Account Data After Transactions -----
    public void refreshAccounts() {
        String pAddress = this.client.getPayeeAddress();
        loadCheckingAccount(pAddress);
        loadSavingsAccount(pAddress);
    }

    /*
     * Admin Method Section
     */

    //----- Method To Get Admin Login Success Flag -----
    public boolean getAdminLoginSuccessFlag() {
        return adminLoginSuccessFlag;
    }

    //----- Method To Set Admin Login Success Flag -----
    public void setAdminLoginSuccessFlag(boolean adminLoginSuccessFlag) {
        this.adminLoginSuccessFlag = adminLoginSuccessFlag;
    }

    //----- Method To Evaluate Admin Credentials -----
    public void evaluateAdminCredentials(String username, String password) {
        try {
            boolean success = authService.authenticateAdmin(username, password);
            this.adminLoginSuccessFlag = success;
        } catch (Exception e) {
            System.err.println("Error in evaluateAdminCredentials: " + e.getMessage());
            e.printStackTrace();
            this.adminLoginSuccessFlag = false;
        }
    }

    //----- Method To Get Clients -----
    public ObservableList<Client> getClients() {
        return clients;
    }

    //----- Method To Set Clients -----
    public void setClients() {
        try {
            List<Client> clientList = clientService.getAllClients();

            clients.clear();
            clients.addAll(clientList);
        } catch (Exception e) {
            System.err.println("Error setting clients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Search Clients -----
    public ObservableList<Client> searchClient(String searchTerm) {
        ObservableList<Client> searchResults = FXCollections.observableArrayList();

        try {
            List<Client> results = clientService.searchClients(searchTerm);

            //----- For Each Client Found, Make Sure To Load Their Accounts -----
            for (Client client : results) {
                //----- Load Checking Account For This Client -----
                try {
                    List<CheckingAccounts> checkingAccounts = accountService.getCheckingAccounts(client.getPayeeAddress());
                    if (checkingAccounts != null && !checkingAccounts.isEmpty()) {
                        client.checkingAccountProperty().set(checkingAccounts.get(0));
                    }
                } catch (Exception e) {
                    System.err.println("Error loading checking account: " + e.getMessage());
                }

                //----- Load Savings Account For This Client -----
                try {
                    List<SavingsAccounts> savingsAccounts = accountService.getSavingsAccounts(client.getPayeeAddress());
                    if (savingsAccounts != null && !savingsAccounts.isEmpty()) {
                        client.savingsAccountProperty().set(savingsAccounts.get(0));
                    }
                } catch (Exception e) {
                    System.err.println("Error loading savings account: " + e.getMessage());
                }

                searchResults.add(client);
            }
        } catch (Exception e) {
            System.err.println("Error searching clients: " + e.getMessage());
            e.printStackTrace();
        }

        return searchResults;
    }

    //----- Method To Create New Client -----
    public boolean createClient(String firstName, String lastName, String payeeAddress, String password,
                                double checkingBalance, double savingsBalance) {
        try {
            return clientService.createClientWithAccounts(
                    firstName, lastName, payeeAddress, password, checkingBalance, savingsBalance);
        } catch (Exception e) {
            System.err.println("Error creating client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //----- Method To Delete Client -----
    public boolean deleteClient(String payeeAddress) {
        try {
            return clientService.deleteClient(payeeAddress);
        } catch (Exception e) {
            System.err.println("Error deleting client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //----- Helper Method For Sum Of Income -----
    public double getSumOfIncome() {
        try {
            return transactionService.getSumOfIncome(client.getPayeeAddress());
        } catch (Exception e) {
            System.err.println("Error getting sum of income: " + e.getMessage());
            e.printStackTrace();

            //----- Fallback To Calculating From Loaded Transactions -----
            double sum = 0;
            for (Transaction transaction : allTransactions) {
                if (transaction.getReceiver().equals(client.getPayeeAddress())) {
                    sum += transaction.getAmount();
                }
            }
            return sum;
        }
    }

    //----- Helper Method For Sum Of Expense -----
    public double getSumOfExpense() {
        try {
            return transactionService.getSumOfExpense(client.getPayeeAddress());
        } catch (Exception e) {
            System.err.println("Error getting sum of expense: " + e.getMessage());
            e.printStackTrace();

            //----- Fallback To Calculating From Loaded Transactions -----
            double sum = 0;
            for (Transaction transaction : allTransactions) {
                if (transaction.getSender().equals(client.getPayeeAddress())) {
                    sum += transaction.getAmount();
                }
            }
            return sum;
        }
    }

    //----- Admin Statistics Methods -----

    public Object getAdminStatistics() {
        try {
            return adminService.getStatistics();
        } catch (Exception e) {
            System.err.println("Error getting admin statistics: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //----- Method To Get Transaction Summary -----
    public Object getTransactionSummary(String period) {
        try {
            return adminService.getTransactionSummary(period);
        } catch (Exception e) {
            System.err.println("Error getting transaction summary: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //----- Method To Get Account Growth -----
    public Object getAccountGrowth() {
        try {
            return adminService.getAccountGrowth();
        } catch (Exception e) {
            System.err.println("Error getting account growth: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //----- Method To Get System Health -----
    public Object getSystemHealth() {
        try {
            return adminService.getSystemHealth();
        } catch (Exception e) {
            System.err.println("Error getting system health: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //----- Method To Create Deposit -----
    public boolean createDeposit(String payeeAddress, String accountNumber, String accountType,
                                 double amount, String description) {
        try {
            return adminService.createDeposit(payeeAddress, accountNumber, accountType, amount, description);
        } catch (Exception e) {
            System.err.println("Error creating deposit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
