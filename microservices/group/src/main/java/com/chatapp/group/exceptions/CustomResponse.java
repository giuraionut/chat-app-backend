package com.chatapp.group.exceptions;

import lombok.Data;

import java.time.Instant;

@Data
public class CustomResponse {
    private Instant timestamp = Instant.now();
    private ExceptionResource resource;
    private String error;
    private Integer status;
    private String message;
    private String path;

    CustomResponse(ExceptionResource resource, String path) {
        this.resource = resource;
        this.path = path;
        this.error = resource.getReason();
        this.message = resource.getMessage();
        this.status = resource.getStatus();
    }

    @Override
    public String toString() {
        return "CustomResponse{" +
                "timestamp=" + timestamp +
                ", error='" + error + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
