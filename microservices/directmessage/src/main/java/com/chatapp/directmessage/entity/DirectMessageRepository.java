package com.chatapp.directmessage.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, UUID> {
}
