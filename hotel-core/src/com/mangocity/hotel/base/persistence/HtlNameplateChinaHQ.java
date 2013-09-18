package com.mangocity.hotel.base.persistence;

/**
 * 集团酒店品牌中国总部联系信息
 * 
 * @author xiaowumi
 * 
 */
public class HtlNameplateChinaHQ extends HtlContactInfo {

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
