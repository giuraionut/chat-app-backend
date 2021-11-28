package com.chatapp.message.exceptions;

import org.springframework.http.HttpStatus;

public enum ExceptionResource {
    MESSAGE_NOT_FOUND("Requested message does not exists", HttpStatus.NOT_FOUND),
    CHAT_HISTORY_NOT_FOUND("Requested chat history does not exists", HttpStatus.NOT_FOUND);

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