package com.mangocity.proxy.delivery.service;

import java.io.Serializable;

/**
 */
public class DeliveryInterfaceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -3619031807060755197L;

    public DeliveryInterfaceException() {
        super();
    }

    public DeliveryInterfaceException(String message) {
        super(message);
    }

    public DeliveryInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeliveryInterfaceException(Throwable cause) {
        super(cause);
    }
}
