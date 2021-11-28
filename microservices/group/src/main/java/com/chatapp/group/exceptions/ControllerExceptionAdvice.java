package com.chatapp.group.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(CustomException.class)
    private ResponseEntity<Object> handleException(ServletWebRequest request) {
        return new ResponseEntity<>(
                new CustomResponse(CustomException.RESOURCE, request.getRequest().getRequestURI()),
                CustomException.RESOURCE.getHttpStatus()
        );
    }
}
