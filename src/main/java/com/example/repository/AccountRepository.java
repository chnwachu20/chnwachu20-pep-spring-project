package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    // Method to find an account by its username
    Optional<Account> findByUsername(String username);

    // Method to find an account by both username and password
    Optional<Account> findByUsernameAndPassword(String username, String password);

    // Optional method to find an account by its ID
    Optional<Account> findById(Integer accountId);
}