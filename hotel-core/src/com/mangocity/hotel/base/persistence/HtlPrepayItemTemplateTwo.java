package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * HtlPrepayItemTemplate generated by MyEclipse Persistence Tools 预付取消及修改条款模板
 */

public class HtlPrepayItemTemplateTwo extends CEntity implements Entity , PrepayClauseTemplate{

    // Fields

    private Long id;

    private Long prepayClauseId;

    private String typePPTwo;

    private String firstDateOrDaysPPTwo;

    private String firstTimePPTwo;

    private String secondDateOrDaysPPTwo;

    private String secondTimePPTwo;

    private String scopePPTwo;

    private String deductTypePPTwo;

    private String percentagePPTwo;

    /*
     * 之前还是之后
     */
    private String beforeOrAfterPPTwo;

    private HtlPrepayTemplate htlPrepayTemplatePPTwo;

    private HtlPrepayEveryday htlPrepayEverydayPPTwo;

    public Long getID() {
        // TODO Auto-generated method stub
        return id;
    }

    public String getDeductTypePPTwo() {
        return deductTypePPTwo;
    }

    public void setDeductTypePPTwo(String deductTypePPTwo) {
        this.deductTypePPTwo = deductTypePPTwo;
    }

    public String getFirstDateOrDaysPPTwo() {
        return firstDateOrDaysPPTwo;
    }

    public void setFirstDateOrDaysPPTwo(String firstDateOrDaysPPTwo) {
        this.firstDateOrDaysPPTwo = firstDateOrDaysPPTwo;
    }

    public String getFirstTimePPTwo() {
        return firstTimePPTwo;
    }

    public void setFirstTimePPTwo(String firstTimePPTwo) {
        this.firstTimePPTwo = firstTimePPTwo;
    }

    public HtlPrepayTemplate getHtlPrepayTemplatePPTwo() {
        return htlPrepayTemplatePPTwo;
    }

    public void setHtlPrepayTemplatePPTwo(HtlPrepayTemplate htlPrepayTemplatePPTwo) {
        this.htlPrepayTemplatePPTwo = htlPrepayTemplatePPTwo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPercentagePPTwo() {
        return percentagePPTwo;
    }

    public void setPercentagePPTwo(String percentagePPTwo) {
        this.percentagePPTwo = percentagePPTwo;
    }

    public Long getPrepayClauseId() {
        return prepayClauseId;
    }

    public void setPrepayClauseId(Long prepayClauseId) {
        this.prepayClauseId = prepayClauseId;
    }

    public String getScopePPTwo() {
        return scopePPTwo;
    }

    public void setScopePPTwo(String scopePPTwo) {
        this.scopePPTwo = scopePPTwo;
    }

    public String getSecondDateOrDaysPPTwo() {
        return secondDateOrDaysPPTwo;
    }

    public void setSecondDateOrDaysPPTwo(String secondDateOrDaysPPTwo) {
        this.secondDateOrDaysPPTwo = secondDateOrDaysPPTwo;
    }

    public String getSecondTimePPTwo() {
        return secondTimePPTwo;
    }

    public void setSecondTimePPTwo(String secondTimePPTwo) {
        this.secondTimePPTwo = secondTimePPTwo;
    }

    public String getTypePPTwo() {
        return typePPTwo;
    }

    public void setTypePPTwo(String typePPTwo) {
        this.typePPTwo = typePPTwo;
    }

    public String getBeforeOrAfterPPTwo() {
        return beforeOrAfterPPTwo;
    }

    public void setBeforeOrAfterPPTwo(String beforeOrAfterPPTwo) {
        this.beforeOrAfterPPTwo = beforeOrAfterPPTwo;
    }

    public HtlPrepayEveryday getHtlPrepayEverydayPPTwo() {
        return htlPrepayEverydayPPTwo;
    }

    public void setHtlPrepayEverydayPPTwo(HtlPrepayEveryday htlPrepayEverydayPPTwo) {
        this.htlPrepayEverydayPPTwo = htlPrepayEverydayPPTwo;
    }
    //策略模式
    public void setParams(HtlPrepayItemTemplate htlPrepayItemTemplate){
    	if(htlPrepayItemTemplate == null) {return;}
    	setFirstDateOrDaysPPTwo(htlPrepayItemTemplate.getFirstDateOrDays());
		setFirstTimePPTwo(htlPrepayItemTemplate.getFirstTime());
		setSecondDateOrDaysPPTwo(htlPrepayItemTemplate.getSecondDateOrDays());
		setSecondTimePPTwo(htlPrepayItemTemplate.getSecondTime());
		setScopePPTwo(htlPrepayItemTemplate.getScope());
		setDeductTypePPTwo(htlPrepayItemTemplate.getDeductType());
		setPercentagePPTwo(htlPrepayItemTemplate.getPercentage());
    }

    // Constructors

}