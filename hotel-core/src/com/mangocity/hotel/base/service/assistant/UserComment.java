package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;

/**
 */
public class UserComment implements Serializable {

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private String context;

    /**
     * 房间评分
     */
    private double roomPoint;

    /**
     * 环境评分
     */
    private double environmentPoint;

    /**
     * 服务评分
     */
    private double servicePoint;

    /**
     * 星级评分
     */
    private double starPoint;

    /**
     * 总体评分
     */
    private double generalPoint;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public double getEnvironmentPoint() {
        return environmentPoint;
    }

    public void setEnvironmentPoint(double environmentPoint) {
        this.environmentPoint = environmentPoint;
    }

    public double getGeneralPoint() {
        return generalPoint;
    }

    public void setGeneralPoint(double generalPoint) {
        this.generalPoint = generalPoint;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public double getRoomPoint() {
        return roomPoint;
    }

    public void setRoomPoint(double roomPoint) {
        this.roomPoint = roomPoint;
    }

    public double getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(double servicePoint) {
        this.servicePoint = servicePoint;
    }

    public double getStarPoint() {
        return starPoint;
    }

    public void setStarPoint(double starPoint) {
        this.starPoint = starPoint;
    }

}
