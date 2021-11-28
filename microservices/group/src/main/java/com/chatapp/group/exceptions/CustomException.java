package com.chatapp.group.exceptions;

public class CustomException extends Exception {
    public static ExceptionResource RESOURCE;

    public CustomException(ExceptionResource exceptionResource) {
        super(exceptionResource.toString());
        RESOURCE = exceptionResource;
    }

}
