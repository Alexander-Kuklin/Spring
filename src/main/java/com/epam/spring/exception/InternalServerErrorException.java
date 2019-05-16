package com.epam.spring.exception;

public class InternalServerErrorException extends IllegalArgumentException {

    private static final long serialVersionUID = -6253562259209287186L;

    public InternalServerErrorException(String s) {
        super(s);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }
}
