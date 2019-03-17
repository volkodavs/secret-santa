package com.sergeyvolkodav.secretsanta.exceptions;

public class SecretSantaException extends RuntimeException {

    private static final long serialVersionUID = 2252236144194015241L;

    private final ErrorCode errorCode;

    public SecretSantaException(String message) {
        super(message);
        this.errorCode = ErrorCode.INVALID_OPERATION;
    }

    public SecretSantaException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
