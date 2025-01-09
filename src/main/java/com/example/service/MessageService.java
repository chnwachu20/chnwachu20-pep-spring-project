package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Create a new message
    public Message createMessage(Message message) {
        // Check if the message text is valid and postedBy refers to a valid user
        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text is invalid");
        }

        if (message.getPostedBy() == null || !messageRepository.existsById(message.getPostedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PostedBy user does not exist");
        }

        // Save the message and return the saved message
        return messageRepository.save(message);
    }

    // Retrieve all messages
    public List<Message> retrieveAllMessages() {
        return messageRepository.findAll();
    }

// Retrieve a message by its ID
public Optional<Message> retrieveMessageById(Integer messageId) {
    return messageRepository.findById(messageId);  // Find message by ID
}

   // Delete a message by its ID
public int deleteMessageById(Integer messageId) {
    if (messageRepository.existsById(messageId)) {
        messageRepository.deleteById(messageId);  // Perform the deletion.
        return 1;  // Message deleted, returning 1 as the number of rows affected.
    }
    return 0;  // No message found, returning 0 to indicate no deletion occurred.
}
public int updateMessage(Integer messageId, String messageText) {
    if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text is invalid");
    }

    // Check if the message exists
    Optional<Message> message = messageRepository.findById(messageId);
    if (message.isPresent()) {
        try {
            // Perform the update
            return messageRepository.updateMessageTextById(messageId, messageText);
        } catch (Exception e) {
            // Log the exception to capture the root cause of the error
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error during update");
        }
    } else {
        return 0;  // Return 0 if no message with the given ID is found
    }
}
    // Retrieve all messages posted by a particular user (accountId)
    public List<Message> retrieveAllMessagesForUser(Integer accountId) {
        return messageRepository.findAllByPostedBy(accountId);
    }
}