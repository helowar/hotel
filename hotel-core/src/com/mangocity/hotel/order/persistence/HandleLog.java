/**
 * 
 *  操作日志
 */
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HandleLog implements Entity {

    // ID <pk>
    private Long ID;

    // 订单ID <fk> 和Order关联
    private Order order;    

    // 订单修改人ID
    private String modifier;

    // 最后修改人姓名
    private String modifierName;
    
    // 最后修改人角色
    private String modifierRole;
    
    // 最后修改时间
    private Date modifiedTime;

    // 修改内容
    private String content;

    // 修改前订单状态
    private String beforeState;

    // 修改后订单状态
    private String afterState;

    // 修改用时
    private String timeConsume;

    public String getAfterState() {
        return afterState;
    }

    public void setAfterState(String afterState) {
        this.afterState = afterState;
    }

    public String getBeforeState() {
        return beforeState;
    }

    public void setBeforeState(String beforeState) {
        this.beforeState = beforeState;
    }

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

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
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


}
