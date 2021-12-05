package com.chatapp.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


public class MemberDto {

    private MemberDto(){}

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Display {
        private UUID id;
        private List<RoleDto> roles;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Add {
        private UUID userId;
    }
}
