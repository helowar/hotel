package com.mangocity.hotelweb.persistence;

import java.io.Serializable;

/**
 */
public class AlertMessageBean implements Serializable {

    private String title = "";

    private String message = "";

    private String returnURL = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

}
