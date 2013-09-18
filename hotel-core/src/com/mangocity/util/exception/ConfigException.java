package com.mangocity.util.exception;

import java.io.Serializable;

/**
 */
public class ConfigException extends RuntimeException implements Serializable {

    public ConfigException(Exception e) {
        super(e);
    }

    public ConfigException(String message) {
        super(message);
    }
}
