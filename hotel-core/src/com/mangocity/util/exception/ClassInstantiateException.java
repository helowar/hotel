package com.mangocity.util.exception;

import java.io.Serializable;

/**
 */
public class ClassInstantiateException extends RuntimeException implements Serializable {

    public ClassInstantiateException(Throwable e) {
        super(e);
    }

    public ClassInstantiateException(String e) {
        super(e);
    }

}
