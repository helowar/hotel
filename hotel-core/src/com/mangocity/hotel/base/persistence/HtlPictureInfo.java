package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

/**
 * @author yuexiaofeng
 * 
 */
public class HtlPictureInfo implements Serializable {

    // 主键ID
    private long id = -1;

    // 酒店ID
    private long hotelId = -1;

    // 显示标题
    private String title = "";

    // 小图片
    private String smollPic = "";

    // 大图片
    private String bigPic = "";

    // 是否房型
    private String isType = "";

    // 排序数值
    private long sortNo = -1;

    public HtlPictureInfo() {
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public String getSmollPic() {
        return smollPic;
    }

    public void setSmollPic(String smollPic) {
        this.smollPic = smollPic;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public long getSortNo() {
        return sortNo;
    }

    public void setSortNo(long sortNo) {
        this.sortNo = sortNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
