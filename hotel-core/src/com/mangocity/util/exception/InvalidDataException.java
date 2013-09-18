package com.mangocity.util.exception;

import java.io.Serializable;

/**
 */
public class InvalidDataException extends RuntimeException implements Serializable {

    public InvalidDataException(Exception e) {
        super(e);
    }

    public InvalidDataException(String message) {
        super(message);
    }

}
