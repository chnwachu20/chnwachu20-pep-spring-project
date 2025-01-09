package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import javax.transaction.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    // Method to find all messages by the postedBy (accountId)
    List<Message> findAllByPostedBy(Integer accountId);

// Query to update message text by messageId

@Transactional
@Modifying
@Query("UPDATE Message m SET m.messageText = :messageText WHERE m.messageId = :messageId")
int updateMessageTextById(@Param("messageId") Integer messageId, @Param("messageText") String messageText);

    // Method to delete a message by its ID (the delete operation is handled via the JpaRepository's built-in deleteById method)
    void deleteById(Integer messageId);

    // Optional method to find a message by its ID
    Optional<Message> findById(Integer messageId);
}