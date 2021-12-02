package com.chatapp.group.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private String name;
    private List<String> permissions;
}
