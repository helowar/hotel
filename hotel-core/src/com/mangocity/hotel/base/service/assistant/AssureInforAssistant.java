package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 担保信息辅助类 字段顺序不要乱调，因为这个java类只用了一次，所以就拼接成字符串。
 * 
 * @author lixiaoyong
 * 
 */
public class AssureInforAssistant implements Serializable {

    private static final long serialVersionUID = -6763440284987473670L;

    /**
     * 担保的具体日期
     */
    private Date assureDate;

    /**
     * 担保规则"1"check in day,"2"全额,"3"累加
     */
    private String assureRule;

    /**
     * 担保类型 2首日 4全额
     */
    private String assureType;

    /**
     * 是否是无条件担保
     */
    private boolean unconditionAssure = false;

    /**
     * 无条件担保金额
     */
    private double unconditionAssureAmount;

    /**
     * 是否超房担保
     */
    private boolean overRoomsAssure = false;

    /**
     * 担保的房间数
     */
    private int overNightsNum;

    /**
     * 是否超房担保
     */
    private boolean overNightsAssure = false;

    /**
     * 担保的房间数
     */
    private int overRoomsNum;

    /**
     * 超房数担保金额
     */
    private double overRoomsAssureAmount;

    /**
     * 超间夜担保金额
     */
    private double overNightsAssureAmount;

    /**
     * 是否超时担保
     */
    private boolean overTimeAssure;

    /**
     * 是否超时担保
     */
    private String overTimeStr;

    /**
     * 超时担保金额
     */
    private double overTimeAssureAmount;

    /**
     * 首日金额
     */
    private double fristDayAssureAmount;

    public Date getAssureDate() {
        return assureDate;
    }

    public void setAssureDate(Date assureDate) {
        this.assureDate = assureDate;
    }

    public boolean isUnconditionAssure() {
        return unconditionAssure;
    }

    public void setUnconditionAssure(boolean unconditionAssure) {
        this.unconditionAssure = unconditionAssure;
    }

    public double getUnconditionAssureAmount() {
        return unconditionAssureAmount;
    }

    public void setUnconditionAssureAmount(double unconditionAssureAmount) {
        this.unconditionAssureAmount = unconditionAssureAmount;
    }

    public boolean isOverRoomsAssure() {
        return overRoomsAssure;
    }

    public void setOverRoomsAssure(boolean overRoomsAssure) {
        this.overRoomsAssure = overRoomsAssure;
    }

    public double getOverRoomsAssureAmount() {
        return overRoomsAssureAmount;
    }

    public void setOverRoomsAssureAmount(double overRoomsAssureAmount) {
        this.overRoomsAssureAmount = overRoomsAssureAmount;
    }

    public boolean isOverTimeAssure() {
        return overTimeAssure;
    }

    public void setOverTimeAssure(boolean overTimeAssure) {
        this.overTimeAssure = overTimeAssure;
    }

    public String getAssureRule() {
        return assureRule;
    }

    public void setAssureRule(String assureRule) {
        this.assureRule = assureRule;
    }

    public double getOverTimeAssureAmount() {
        return overTimeAssureAmount;
    }

    public void setOverTimeAssureAmount(double overTimeAssureAmount) {
        this.overTimeAssureAmount = overTimeAssureAmount;
    }

    public int getOverRoomsNum() {
        return overRoomsNum;
    }

    public void setOverRoomsNum(int overRoomsNum) {
        this.overRoomsNum = overRoomsNum;
    }

    public String getAssureType() {
        return assureType;
    }

    public void setAssureType(String assureType) {
        this.assureType = assureType;
    }

    public String getOverTimeStr() {
        return overTimeStr;
    }

    public void setOverTimeStr(String overTimeStr) {
        this.overTimeStr = overTimeStr;
    }

    public double getFristDayAssureAmount() {
        return fristDayAssureAmount;
    }

    public void setFristDayAssureAmount(double fristDayAssureAmount) {
        this.fristDayAssureAmount = fristDayAssureAmount;
    }

    public int getOverNightsNum() {
        return overNightsNum;
    }

    public void setOverNightsNum(int overNightsNum) {
        this.overNightsNum = overNightsNum;
    }

    public boolean isOverNightsAssure() {
        return overNightsAssure;
    }

    public void setOverNightsAssure(boolean overNightsAssure) {
        this.overNightsAssure = overNightsAssure;
    }

    public double getOverNightsAssureAmount() {
        return overNightsAssureAmount;
    }

    public void setOverNightsAssureAmount(double overNightsAssureAmount) {
        this.overNightsAssureAmount = overNightsAssureAmount;
    }

}
