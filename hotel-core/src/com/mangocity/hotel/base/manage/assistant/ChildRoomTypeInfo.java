package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class ChildRoomTypeInfo implements Serializable {

    private String childRoomId;

    private String childRoomName;

    private Map<Long, String> priceType = new HashMap<Long, String>();

    public String getChildRoomId() {
        return childRoomId;
    }

    public void setChildRoomId(String childRoomId) {
        this.childRoomId = childRoomId;
    }

    public String getChildRoomName() {
        return childRoomName;
    }

    public void setChildRoomName(String childRoomName) {
        this.childRoomName = childRoomName;
    }

    public Map<Long, String> getPriceType() {
        return priceType;
    }

    public void setPriceType(Map<Long, String> priceType) {
        this.priceType = priceType;
    }

}
