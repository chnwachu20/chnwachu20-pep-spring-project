package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // User Story 1: Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data");
        }

        if (accountService.doesUserExistByUsername(account.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        Account registeredAccount = accountService.userRegistration(account);
        return ResponseEntity.ok(registeredAccount);
    }

    // User Story 2: Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.userLogin(account.getUsername(), account.getPassword());
        if (loggedInAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        return ResponseEntity.ok(loggedInAccount);
    }

    // User Story 3: Create Message
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid message text");
        }
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);
    }

  // User Story 4: Retrieve All Messages
  @GetMapping("/messages")
  public ResponseEntity<List<Message>> getAllMessages() {
      List<Message> messages = messageService.retrieveAllMessages();
      return ResponseEntity.ok(messages);
  }

// User Story 5: Retrieve Message by ID
@GetMapping("/messages/{messageId}")
public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
    Optional<Message> message = messageService.retrieveMessageById(messageId);
    if (message.isPresent()) {
        return ResponseEntity.ok(message.get());  // Return the message with 200 OK status
    } else {
        return ResponseEntity.ok().build();  // Return 200 OK with an empty body when no message found
    }
}

  // User Story 6: Delete Message by ID
@DeleteMapping("/messages/{messageId}")
public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
    int rowsDeleted = messageService.deleteMessageById(messageId);
    if (rowsDeleted > 0) {
        return ResponseEntity.ok(rowsDeleted);  // Message deleted, returning the number of rows deleted.
    } else {
        return ResponseEntity.ok().build();  // No message found, but response is 200 OK with empty body.
    }
}
// User Story 7: Update Message by ID
@PatchMapping("/messages/{messageId}")
public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Message updatedMessage) {
    // Validate message text
    if (updatedMessage.getMessageText() == null || updatedMessage.getMessageText().isBlank() || 
        updatedMessage.getMessageText().length() > 255) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid message text");
    }

    try {
        // Perform update
        int rowsUpdated = messageService.updateMessage(messageId, updatedMessage.getMessageText());
        if (rowsUpdated > 0) {
            return ResponseEntity.ok(rowsUpdated);  // Successful update, return 200 OK with rows updated
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message update failed");  // Return 400 if update failed
        }
    } catch (ResponseStatusException e) {
        // Handle known exceptions like BAD_REQUEST or any custom exception
        return ResponseEntity.status(e.getStatus()).body(e.getReason());
    } catch (Exception e) {
        // Handle unexpected errors
    
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}

  // User Story 8: Retrieve All Messages by Account
  @GetMapping("/accounts/{accountId}/messages")
  public ResponseEntity<List<Message>> getMessagesByAccount(@PathVariable Integer accountId) {
      List<Message> messages = messageService.retrieveAllMessagesForUser(accountId);
      return ResponseEntity.ok(messages);
  }
}