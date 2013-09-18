
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 *  操作日志
 *  
 *  @author chenkeming
 */
public class OrHandleLog implements Entity {

    private static final long serialVersionUID = 4286359093758591584L;

    /**
	 * ID <pk>
	 */
    private Long ID;
    
    /**
	 * 历史单编号
	 */
    private int hisNo = 0;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private OrOrder order;    

    /**
	 * 订单修改人工号
	 */
    private Long modifier;

    /**
	 * 修改人姓名
	 */
    private String modifierName;
    
    /**
	 * 修改人角色(目前用于保存修改人工号)
	 */
    private String modifierRole;
    
    /**
	 * 修改时间
	 */
    private Date modifiedTime;

    /**
	 * 修改内容
	 */
    private String content;

    /**
	 * 修改前订单状态
	 */
    private int beforeState;

    /**
	 * 修改后订单状态
	 */
    private int afterState;

    /**
	 * 修改用时
	 */
    private String timeConsume;
    
    /**
	 * 状态是否改变
	 */
    private boolean stateChange;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long logId) {
        this.ID = logId;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getModifierRole() {
        return modifierRole;
    }

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public void setModifierRole(String modifierRole) {
        this.modifierRole = modifierRole;
    }

    public String getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(String timeConsume) {
        this.timeConsume = timeConsume;
    }

    public int getAfterState() {
        return afterState;
    }

    public void setAfterState(int afterState) {
        this.afterState = afterState;
    }

    public int getBeforeState() {
        return beforeState;
    }

    public void setBeforeState(int beforeState) {
        this.beforeState = beforeState;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public boolean isStateChange() {
        return stateChange;
    }

    public void setStateChange(boolean stateChange) {
        this.stateChange = stateChange;
    }

    public int getHisNo() {
        return hisNo;
    }

    public void setHisNo(int hisNo) {
        this.hisNo = hisNo;
    }


}
