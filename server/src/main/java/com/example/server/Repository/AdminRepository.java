package com.example.server.Repository;

import com.example.server.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//----- Admin Repository -----
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    //----- Find Admin By Username And Password -----
    Optional<Admin> findByUsernameAndPassword(String username, String password);
} 