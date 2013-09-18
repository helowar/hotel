package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HtlUsersComment extends CEntity implements Entity {

    // 评论id
    private Long ID;

    // 酒店id
    private Long hotelID;

    // 作者
    private String author;

    // 内容
    private String context;

    // 房间评分
    private Double roomPoint;

    // 环境评分
    private Double environmentPoint;

    // 服务评分
    private Double servicePoint;

    // 星级评分
    private Double starPoint;

    // 总体评分
    private Double generalPoint;

    // 评论状态
    private String commentStatus;

    // 评论创建时间
    private Date createTime;

    // 评论审核时间
    private Date verifyDate;

    // 会员ID
    private Long memberId;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String comment_status) {
        this.commentStatus = comment_status;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Double getEnvironmentPoint() {
        return environmentPoint;
    }

    public void setEnvironmentPoint(Double environment_point) {
        this.environmentPoint = environment_point;
    }

    public Double getGeneralPoint() {
        return generalPoint;
    }

    public void setGeneralPoint(Double general_point) {
        this.generalPoint = general_point;
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotel_id) {
        this.hotelID = hotel_id;
    }

    public Double getRoomPoint() {
        return roomPoint;
    }

    public void setRoomPoint(Double room_point) {
        this.roomPoint = room_point;
    }

    public Double getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(Double service_point) {
        this.servicePoint = service_point;
    }

    public Double getStarPoint() {
        return starPoint;
    }

    public void setStarPoint(Double star_point) {
        this.starPoint = star_point;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
