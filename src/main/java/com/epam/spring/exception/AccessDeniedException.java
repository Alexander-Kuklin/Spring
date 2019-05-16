package com.epam.spring.exception;

public class AccessDeniedException extends IllegalArgumentException {
    private static final long serialVersionUID = 2849341587872815591L;


    public AccessDeniedException(String s) {
        super(s);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }
}
