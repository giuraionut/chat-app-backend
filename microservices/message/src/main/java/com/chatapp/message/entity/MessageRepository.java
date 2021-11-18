package com.chatapp.message.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("select m from Message m where m.senderId = ?1 and m.recipientId = ?2 or m.senderId = ?2 and m.recipientId = ?1 ORDER BY m.timestamp ASC")
    List<Message> findByRecipientIdAndSenderId(UUID senderId, UUID recipientId);

}
