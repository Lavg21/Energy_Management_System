package com.ems.emsdevice.exception;

public class UserAvailableNotFoundException extends RuntimeException {

    public UserAvailableNotFoundException(String message) {
        super(message);
    }

    public UserAvailableNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
