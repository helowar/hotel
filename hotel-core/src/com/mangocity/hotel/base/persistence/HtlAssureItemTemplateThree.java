package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * HtlAssureItemTemplate generated by MyEclipse Persistence Tools 担保取消修改条款模板
 */

public class HtlAssureItemTemplateThree extends CEntity implements Entity ,AssureClauseTemplate{

    // Fields

    private Long id;

    /*
     * 担保条款ID
     */
    private Long assureClauseId;

    /*
     * 取消修改条款种类
     */
    private String typeThree;

    /*
     * 第一日期或天数
     */

    private String firstDateOrDaysThree;

    /*
     * 第一时间点
     */
    private String firstTimeThree;

    /*
     * 第二日期或天数
     */
    private String secondDateOrDaysThree;

    /*
     * 第二时间点
     */

    private String secondTimeThree;

    /*
     * 取消修改范围
     */

    private String scopeThree;

    /*
     * 
     * 扣款类型
     */

    private String deductTypeThree;

    /*
     * 扣款百分比
     */
    private String percentageThree;

    /*
     * 之前还是之后
     */
    private String beforeOrAfterThree;

    private HtlAssureTemplate htlAssureTemplateThree;

    private HtlAssure htlAssureThree;

    public HtlAssure getHtlAssureThree() {
        return htlAssureThree;
    }

    public void setHtlAssureThree(HtlAssure htlAssureThree) {
        this.htlAssureThree = htlAssureThree;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return id;
    }

    public Long getAssureClauseId() {
        return assureClauseId;
    }

    public void setAssureClauseId(Long assureClauseId) {
        this.assureClauseId = assureClauseId;
    }

    public String getDeductTypeThree() {
        return deductTypeThree;
    }

    public void setDeductTypeThree(String deductTypeThree) {
        this.deductTypeThree = deductTypeThree;
    }

    public String getFirstDateOrDaysThree() {
        return firstDateOrDaysThree;
    }

    public void setFirstDateOrDaysThree(String firstDateOrDaysThree) {
        this.firstDateOrDaysThree = firstDateOrDaysThree;
    }

    public String getFirstTimeThree() {
        return firstTimeThree;
    }

    public void setFirstTimeThree(String firstTimeThree) {
        this.firstTimeThree = firstTimeThree;
    }

    public HtlAssureTemplate getHtlAssureTemplateThree() {
        return htlAssureTemplateThree;
    }

    public void setHtlAssureTemplateThree(HtlAssureTemplate htlAssureTemplateThree) {
        this.htlAssureTemplateThree = htlAssureTemplateThree;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPercentageThree() {
        return percentageThree;
    }

    public void setPercentageThree(String percentageThree) {
        this.percentageThree = percentageThree;
    }

    public String getScopeThree() {
        return scopeThree;
    }

    public void setScopeThree(String scopeThree) {
        this.scopeThree = scopeThree;
    }

    public String getSecondDateOrDaysThree() {
        return secondDateOrDaysThree;
    }

    public void setSecondDateOrDaysThree(String secondDateOrDaysThree) {
        this.secondDateOrDaysThree = secondDateOrDaysThree;
    }

    public String getSecondTimeThree() {
        return secondTimeThree;
    }

    public void setSecondTimeThree(String secondTimeThree) {
        this.secondTimeThree = secondTimeThree;
    }

    public String getTypeThree() {
        return typeThree;
    }

    public void setTypeThree(String typeThree) {
        this.typeThree = typeThree;
    }

    public String getBeforeOrAfterThree() {
        return beforeOrAfterThree;
    }

    public void setBeforeOrAfterThree(String beforeOrAfterThree) {
        this.beforeOrAfterThree = beforeOrAfterThree;
    }
    
    public void setParams(HtlAssureItemTemplate htlAssureItemTemplate) {
        setFirstDateOrDaysThree(htlAssureItemTemplate.getFirstDateOrDays());
	    setFirstTimeThree(htlAssureItemTemplate.getFirstTime());
	    setSecondDateOrDaysThree(htlAssureItemTemplate.getSecondDateOrDays());
	    setSecondTimeThree(htlAssureItemTemplate.getSecondTime());
	    setScopeThree(htlAssureItemTemplate.getScope());
	    setDeductTypeThree(htlAssureItemTemplate.getDeductType());
	    setPercentageThree(htlAssureItemTemplate.getPercentage());
    }
    // Constructors

}