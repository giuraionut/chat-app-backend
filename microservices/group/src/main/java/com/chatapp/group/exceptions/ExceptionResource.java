package com.chatapp.group.exceptions;

import org.springframework.http.HttpStatus;

public enum ExceptionResource {
    GROUP_NOT_FOUND("Requested group does not exists", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("Requested category does not exists", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND("Requested room does not exists", HttpStatus.NOT_FOUND),

    ACCESS_DENIED("You don't have enough permissions for this action", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;

    ExceptionResource(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Integer getStatus() {
        return this.httpStatus.value();
    }

    public String getReason() {
        return this.httpStatus.getReasonPhrase();
    }

    public String getMessage() {
        return this.message;
    }
}