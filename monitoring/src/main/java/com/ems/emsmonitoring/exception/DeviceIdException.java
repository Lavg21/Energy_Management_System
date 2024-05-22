package com.ems.emsmonitoring.exception;

public class DeviceIdException extends RuntimeException{
    public DeviceIdException(String message) {
        super(message);
    }

    public DeviceIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
