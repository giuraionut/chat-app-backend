package com.chatapp.message.exceptions;

public class CustomException extends Exception {
    public static ExceptionResource RESOURCE;

    public CustomException(ExceptionResource exceptionResource) {
        super(exceptionResource.toString());
        RESOURCE = exceptionResource;
    }

}
