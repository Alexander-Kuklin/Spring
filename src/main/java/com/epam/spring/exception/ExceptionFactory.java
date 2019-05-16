package com.epam.spring.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.FailedLoginException;
import java.lang.reflect.Constructor;
import java.sql.SQLException;

public class ExceptionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionFactory.class);

    public static SQLException getSqlException(String message, Throwable cause) {
        try {
            Constructor<SQLException> exceptionConstructor = SQLException.class
                    .getConstructor(String.class, Throwable.class);
            return exceptionConstructor.newInstance(message, cause);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static SQLException getSqlException(String message) {
        try {
            Constructor<SQLException> exceptionConstructor = SQLException.class
                    .getConstructor(String.class);
            return exceptionConstructor.newInstance(message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }


    public static AccessDeniedException getAccessDeniedException(String message) {
        try {
            Constructor<AccessDeniedException> exceptionConstructor = AccessDeniedException.class
                    .getConstructor(String.class);
            return exceptionConstructor.newInstance(message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static FailedLoginException getFailedLoginException(String message) {
        try {
            Constructor<FailedLoginException> exceptionConstructor = FailedLoginException.class
                    .getConstructor(String.class);
            return exceptionConstructor.newInstance(message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }


    public static IllegalArgumentException getIllegalArgumentException(String message) {
        try {
            Constructor<IllegalArgumentException> exceptionConstructor = IllegalArgumentException.class
                    .getConstructor(String.class);
            return exceptionConstructor.newInstance(message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static InternalServerErrorException getInternalServerErrorException(String message, Throwable cause) {
        try {
            Constructor<InternalServerErrorException> exceptionConstructor = InternalServerErrorException.class
                    .getConstructor(String.class, Throwable.class);
            return exceptionConstructor.newInstance(message, cause);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
