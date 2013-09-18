package com.mangocity.proxy.member.service;

import java.io.Serializable;

/**
 */
public class MemberPassengerInterfaceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -3388564369026626666L;

    public MemberPassengerInterfaceException() {
        super();
    }

    public MemberPassengerInterfaceException(String message) {
        super(message);
    }

    public MemberPassengerInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberPassengerInterfaceException(Throwable cause) {
        super(cause);
    }
}
