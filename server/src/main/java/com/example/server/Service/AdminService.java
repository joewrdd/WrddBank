package com.example.server.Service;

import com.example.server.Model.Admin;
import com.example.server.Repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

//----- Admin Service -----
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    //----- Authenticate Admin -----
    public Optional<Admin> authenticateAdmin(String username, String password) {
        return adminRepository.findByUsernameAndPassword(username, password);
    }
} 