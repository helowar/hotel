package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

/**
 * 集团酒店品牌分支机构联系信息
 * 
 * @author xiaowumi
 * 
 */
public class HtlNameplateOffshootOrgan extends HtlContactInfo implements Serializable {

    /**
     * 集团酒店品牌
     */
    private HtlNameplate nameplate;

    public HtlNameplate getNameplate() {
        return nameplate;
    }

    public void setNameplate(HtlNameplate nameplate) {
        this.nameplate = nameplate;
    }

}
