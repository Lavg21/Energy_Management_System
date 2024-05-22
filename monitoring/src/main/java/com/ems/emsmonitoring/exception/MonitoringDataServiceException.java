package com.ems.emsmonitoring.exception;

public class MonitoringDataServiceException extends RuntimeException {

    public MonitoringDataServiceException(String message) {
        super(message);
    }

    public MonitoringDataServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
