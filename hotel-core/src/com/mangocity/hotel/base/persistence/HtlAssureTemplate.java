package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * HtlAssureTemplate generated by MyEclipse Persistence Tools create by Shengwei.Zuo 2009-02-05
 * 担保条款模板
 */

public class HtlAssureTemplate extends CEntity implements Entity {

    // Fields

    private Long id;

    /*
     * 是否无条件担保
     */
    private String isNotConditional;

    /*
     * 最晚担保时间
     */
    private String latestAssureTime;

    /*
     * 超房数量
     */
    private Long overRoomNumber;

    /*
     * 超过间夜数
     */
    private Long overNightsNumber;

    /*
     * 担保类型
     */
    private String assureType;

    /*
     * 是否需要担保函
     */
    private String assureLetter;

    // 预定条款模板总表
    private HtlPreconcertItemTemplet htlPreconcertItemTempletZ;

    private List<HtlAssureItemTemplate> lisHtlAssureItemTemplate = new ArrayList<HtlAssureItemTemplate>();

    private List<AssureClauseTemplate> lisHtlAssureItemTemplateOne = 
        new ArrayList<AssureClauseTemplate>();

    private List<AssureClauseTemplate> lisHtlAssureItemTemplateTwo = 
        new ArrayList<AssureClauseTemplate>();

    private List<AssureClauseTemplate> lisHtlAssureItemTemplateThree =
        new ArrayList<AssureClauseTemplate>();

    private List<AssureClauseTemplate> lisHtlAssureItemTemplateFour =
        new ArrayList<AssureClauseTemplate>();

    private List<AssureClauseTemplate> lisHtlAssureItemTemplateFive = 
        new ArrayList<AssureClauseTemplate>();

    // Constructors

    /** default constructor */
    public HtlAssureTemplate() {
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsNotConditional() {
        return this.isNotConditional;
    }

    public void setIsNotConditional(String isNotConditional) {
        this.isNotConditional = isNotConditional;
    }

    public String getLatestAssureTime() {
        return this.latestAssureTime;
    }

    public void setLatestAssureTime(String latestAssureTime) {
        this.latestAssureTime = latestAssureTime;
    }

    public Long getOverRoomNumber() {
        return this.overRoomNumber;
    }

    public void setOverRoomNumber(Long overRoomNumber) {
        this.overRoomNumber = overRoomNumber;
    }

    public String getAssureType() {
        return this.assureType;
    }

    public void setAssureType(String assureType) {
        this.assureType = assureType;
    }

    public String getAssureLetter() {
        return this.assureLetter;
    }

    public void setAssureLetter(String assureLetter) {
        this.assureLetter = assureLetter;
    }

    public List<HtlAssureItemTemplate> getLisHtlAssureItemTemplate() {
        return lisHtlAssureItemTemplate;
    }

    public void setLisHtlAssureItemTemplate(List<HtlAssureItemTemplate> lisHtlAssureItemTemplate) {
        this.lisHtlAssureItemTemplate = lisHtlAssureItemTemplate;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return id;
    }

    public HtlPreconcertItemTemplet getHtlPreconcertItemTempletZ() {
        return htlPreconcertItemTempletZ;
    }

    public void setHtlPreconcertItemTempletZ(HtlPreconcertItemTemplet htlPreconcertItemTempletZ) {
        this.htlPreconcertItemTempletZ = htlPreconcertItemTempletZ;
    }

    public List<AssureClauseTemplate> getLisHtlAssureItemTemplateFive() {
        return lisHtlAssureItemTemplateFive;
    }

    public void setLisHtlAssureItemTemplateFive(
        List<AssureClauseTemplate> lisHtlAssureItemTemplateFive) {
        this.lisHtlAssureItemTemplateFive = lisHtlAssureItemTemplateFive;
    }

    public List<AssureClauseTemplate> getLisHtlAssureItemTemplateFour() {
        return lisHtlAssureItemTemplateFour;
    }

    public void setLisHtlAssureItemTemplateFour(
        List<AssureClauseTemplate> lisHtlAssureItemTemplateFour) {
        this.lisHtlAssureItemTemplateFour = lisHtlAssureItemTemplateFour;
    }

    public List<AssureClauseTemplate> getLisHtlAssureItemTemplateOne() {
        return lisHtlAssureItemTemplateOne;
    }

    public void setLisHtlAssureItemTemplateOne(
        List<AssureClauseTemplate> lisHtlAssureItemTemplateOne) {
        this.lisHtlAssureItemTemplateOne = lisHtlAssureItemTemplateOne;
    }

    public List<AssureClauseTemplate> getLisHtlAssureItemTemplateThree() {
        return lisHtlAssureItemTemplateThree;
    }

    public void setLisHtlAssureItemTemplateThree(
        List<AssureClauseTemplate> lisHtlAssureItemTemplateThree) {
        this.lisHtlAssureItemTemplateThree = lisHtlAssureItemTemplateThree;
    }

    public List<AssureClauseTemplate> getLisHtlAssureItemTemplateTwo() {
        return lisHtlAssureItemTemplateTwo;
    }

    public void setLisHtlAssureItemTemplateTwo(
        List<AssureClauseTemplate> lisHtlAssureItemTemplateTwo) {
        this.lisHtlAssureItemTemplateTwo = lisHtlAssureItemTemplateTwo;
    }

    public Long getOverNightsNumber() {
        return overNightsNumber;
    }

    public void setOverNightsNumber(Long overNightsNumber) {
        this.overNightsNumber = overNightsNumber;
    }

}