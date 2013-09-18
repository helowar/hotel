package com.mangocity.util.exception;

import java.io.Serializable;

/**
 */
public class DAOException extends RuntimeException implements Serializable {
    public DAOException() {
        super();
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
