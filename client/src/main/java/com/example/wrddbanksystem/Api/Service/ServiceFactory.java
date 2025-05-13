package com.example.wrddbanksystem.Api.Service;

import com.example.wrddbanksystem.Api.Service.Impl.AccountServiceImpl;
import com.example.wrddbanksystem.Api.Service.Impl.AdminServiceImpl;
import com.example.wrddbanksystem.Api.Service.Impl.AuthServiceImpl;
import com.example.wrddbanksystem.Api.Service.Impl.ClientServiceImpl;
import com.example.wrddbanksystem.Api.Service.Impl.TransactionServiceImpl;

//----- Service Factory -----
public class ServiceFactory {
    
    //----- Singleton Instance -----
    private static ServiceFactory instance;
    
    //----- Service Instances -----
    private AuthService authService;
    private ClientService clientService;
    private AccountService accountService;
    private TransactionService transactionService;
    private AdminService adminService;
    
    //----- Private Constructor -----
    private ServiceFactory() {
        //----- Initialize Default Service Implementations -----
        this.authService = new AuthServiceImpl();
        this.clientService = new ClientServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.adminService = new AdminServiceImpl();
    }
    
    //----- Get Singleton Instance -----
    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }
    
    //----- Get Authentication Service -----
    public AuthService getAuthService() {
        return authService;
    }
    
    //----- Set Authentication Service -----
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
    
    //----- Get Client Service -----
    public ClientService getClientService() {
        return clientService;
    }
    
    //----- Set Client Service -----
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
    
    //----- Get Account Service -----
    public AccountService getAccountService() {
        return accountService;
    }
    
    //----- Set Account Service -----
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
    
    //----- Get Transaction Service -----
    public TransactionService getTransactionService() {
        return transactionService;
    }
    
    //----- Set Transaction Service -----
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    //----- Get Admin Service -----
    public AdminService getAdminService() {
        return adminService;
    }
    
    //----- Set Admin Service -----
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }
    
    //----- Reset All Services to Default Implementations -----
    public void resetToDefaults() {
        this.authService = new AuthServiceImpl();
        this.clientService = new ClientServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.adminService = new AdminServiceImpl();
    }
} 