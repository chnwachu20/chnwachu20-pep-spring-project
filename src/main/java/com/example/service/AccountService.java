package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Check if a user exists by username
    public boolean doesUserExistByUsername(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }

    // User login: Check if the credentials are valid
    public Account userLogin(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
    }

    // User registration: Create a new user account
    public Account userRegistration(Account account) {
        // Validate username
        if (account.getUsername().isEmpty() || account.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is invalid");
        }

        // Check if user already exists
        if (doesUserExistByUsername(account.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        // Save and return the newly created account
        return accountRepository.save(account);
    }
}