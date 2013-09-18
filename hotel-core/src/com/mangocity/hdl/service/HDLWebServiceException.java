package com.mangocity.hdl.service;

import java.io.Serializable;

import javax.xml.ws.WebServiceException;

/**
 */
public class HDLWebServiceException extends WebServiceException implements Serializable {
    private static final long serialVersionUID = -743306003199694554L;

    public HDLWebServiceException() {
        super();
    }

    public HDLWebServiceException(String message) {
        super(message);
    }

    public HDLWebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public HDLWebServiceException(Throwable cause) {
        super(cause);
    }
}
