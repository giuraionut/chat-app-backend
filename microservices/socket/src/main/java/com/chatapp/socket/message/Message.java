package com.chatapp.socket.message;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Message {
    private UUID senderId;
    private UUID recipientId;
    private String content;
    private Instant timestamp;
}
