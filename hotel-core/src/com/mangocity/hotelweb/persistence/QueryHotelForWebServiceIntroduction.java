package com.mangocity.hotelweb.persistence;

import java.io.Serializable;
import java.util.List;

/**
 */
public class QueryHotelForWebServiceIntroduction implements Serializable {

    /**
     * 餐饮设施
     */
    private String mealFixtrue;

    /**
     * 客房设施
     */
    private String roomFixtrue;

    /**
     * 免费服务
     */
    private String freeService;

    /**
     * 酒店详细介绍
     */
    private String chnHotelIntroduce;

    /**
     * 图片信息
     */
    private List pictureInfos;

    public String getChnHotelIntroduce() {
        return chnHotelIntroduce;
    }

    public void setChnHotelIntroduce(String chnHotelIntroduce) {
        this.chnHotelIntroduce = chnHotelIntroduce;
    }

    public String getMealFixtrue() {
        return mealFixtrue;
    }

    public void setMealFixtrue(String mealFixtrue) {
        this.mealFixtrue = mealFixtrue;
    }

    public String getRoomFixtrue() {
        return roomFixtrue;
    }

    public void setRoomFixtrue(String roomFixtrue) {
        this.roomFixtrue = roomFixtrue;
    }

    public List getPictureInfos() {
        return pictureInfos;
    }

    public void setPictureInfos(List pictureInfos) {
        this.pictureInfos = pictureInfos;
    }

    public String getFreeService() {
        return freeService;
    }

    public void setFreeService(String freeService) {
        this.freeService = freeService;
    }

}
