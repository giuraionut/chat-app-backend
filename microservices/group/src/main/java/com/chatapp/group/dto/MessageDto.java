package com.chatapp.group.dto;

import lombok.Data;

import java.util.UUID;

public class MessageDto {
    private MessageDto() {
    }

    @Data
    public static class Base {
        private UUID messageId;
    }

    @Data
    public static class Display {
        private UUID messageId;
        private UUID roomId;
    }
}
