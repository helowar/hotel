package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 集团酒店基本信息
 * 
 * @author xiaowumi
 * 
 */

public class HtlHotelGroup extends CEntity implements Entity {

    /**
     * 酒店集团id hotel_group_id
     */
    private Long ID;

    /**
     * 集团酒店名称
     */
    private String groupName;

    /**
     * 集团酒店的性质是国内还是国际酒店
     */
    private String groupKind;

    /**
     * 集团酒店简介
     */
    private String groupIntroduce;

    /**
     * 集团酒店总部所在地
     */
    private String hqAddress;

    /**
     * 集团酒店中国总部所在地
     */
    private String chinaHqAddress;

    /**
     * 集团酒店编码
     */
    private String groupCode;

    /**
     * 集团酒店英文名称
     */
    private String engName;

    // 分支机构信息列表
    private List<HtlGroupOffshootOrgan> lisHtlGroupOffshootOrgan = 
        new ArrayList<HtlGroupOffshootOrgan>();

    // 集团酒店中国总部联系信息
    private List<HtlGroupChinaHq> lisHtlGroupChinaHq = new ArrayList<HtlGroupChinaHq>();

    // 集团酒店总部联系信息
    private List<HtlGroupHQ> lisHtlGroupHQ = new ArrayList<HtlGroupHQ>();

    /**
     * 酒店集团信息首字母
     */
    private String firstLetter;

    /**
     * 集团品牌列表
     */
    private List lstNameplate = new ArrayList();

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getGroupIntroduce() {
        return groupIntroduce;
    }

    public void setGroupIntroduce(String groupIntroduce) {
        this.groupIntroduce = groupIntroduce;
    }

    public String getGroupKind() {
        return groupKind;
    }

    public void setGroupKind(String groupKind) {
        this.groupKind = groupKind;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHqAddress() {
        return hqAddress;
    }

    public void setHqAddress(String hqAddress) {
        this.hqAddress = hqAddress;
    }

    public String getChinaHqAddress() {
        return chinaHqAddress;
    }

    public void setChinaHqAddress(String chinaHqAddress) {
        this.chinaHqAddress = chinaHqAddress;
    }

    public List getLstNameplate() {
        return lstNameplate;
    }

    public void setLstNameplate(List lstNameplate) {
        this.lstNameplate = lstNameplate;
    }

    public List<HtlGroupOffshootOrgan> getLisHtlGroupOffshootOrgan() {
        return lisHtlGroupOffshootOrgan;
    }

    public void setLisHtlGroupOffshootOrgan(List<HtlGroupOffshootOrgan> lisHtlGroupOffshootOrgan) {
        this.lisHtlGroupOffshootOrgan = lisHtlGroupOffshootOrgan;
    }

    public List<HtlGroupChinaHq> getLisHtlGroupChinaHq() {
        return lisHtlGroupChinaHq;
    }

    public void setLisHtlGroupChinaHq(List<HtlGroupChinaHq> lisHtlGroupChinaHq) {
        this.lisHtlGroupChinaHq = lisHtlGroupChinaHq;
    }

    public List<HtlGroupHQ> getLisHtlGroupHQ() {
        return lisHtlGroupHQ;
    }

    public void setLisHtlGroupHQ(List<HtlGroupHQ> lisHtlGroupHQ) {
        this.lisHtlGroupHQ = lisHtlGroupHQ;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

}
