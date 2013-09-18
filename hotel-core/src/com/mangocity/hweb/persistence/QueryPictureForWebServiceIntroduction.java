package com.mangocity.hweb.persistence;

import java.io.Serializable;

/**
 */
public class QueryPictureForWebServiceIntroduction implements Serializable {

    private String pictureType;

    private String pictureName;

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

}
