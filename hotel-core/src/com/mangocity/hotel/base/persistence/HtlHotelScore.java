package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 */
public class HtlHotelScore extends CEntity implements Entity {

    private Long ID;

    private String type;

    private String score;

    private HtlMainCommend mainCommend;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public HtlMainCommend getMainCommend() {
        return mainCommend;
    }

    public void setMainCommend(HtlMainCommend mainCommend) {
        this.mainCommend = mainCommend;
    }

}
