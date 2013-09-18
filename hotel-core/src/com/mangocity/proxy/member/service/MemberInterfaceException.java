package com.mangocity.proxy.member.service;

import java.io.Serializable;

/**
 */
public class MemberInterfaceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -3388564369026626666L;

    public MemberInterfaceException() {
        super();
    }

    public MemberInterfaceException(String message) {
        super(message);
    }

    public MemberInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberInterfaceException(Throwable cause) {
        super(cause);
    }
}
