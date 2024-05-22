package com.ems.emsdevice.exception;

public class ClientStatusException extends RuntimeException{
    public ClientStatusException(String message) {
        super(message);
    }

    public ClientStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
