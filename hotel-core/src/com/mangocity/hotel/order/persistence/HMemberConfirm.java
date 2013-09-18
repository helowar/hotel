
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.util.HEntity;

/** 
 *  历史订单客户确认信息发送表
 *  
 *  @author chenkeming
 */
public class HMemberConfirm implements HEntity {

    private static final long serialVersionUID = 4990248290299789177L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;    
    
    /**
	 * 历史单编号
	 */
    private int hisNo;

    /**
	 * 历史订单ID <fk> 和HOrder关联
	 */
    private HOrder orderH;    

    /**
	 * 短信/传真/email/口头
	 * 
	 * @see ConfirmType
	 */
    private int channel;

    /**
	 * 确认/取消
	 * 
	 * @see MemberConfirmType
	 */
    private int type;
    
    /**
	 * 模板类型：芒果散客/114
	 * 
	 * @see ModelType
	 */
    private int modelType;
    
    /**
	 * 发送内容
	 */
    private String content;

    /**
	 * 发送目标：短信号/传真号/EMAIL
	 */
    private String sendTarget;

    /**
	 * 发送人
	 */
    private String sendMan;

    /**
	 * 发送时间
	 */
    private Date sendTime;
    
    /**
	 * 是否发送成功
	 */
    private boolean sendSucceed;
    
    /**
	 * 备注
	 */
    private String notes;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSendMan() {
        return sendMan;
    }

    public void setSendMan(String sendMan) {
        this.sendMan = sendMan;
    }

    public boolean isSendSucceed() {
        return sendSucceed;
    }

    public void setSendSucceed(boolean sendSucceed) {
        this.sendSucceed = sendSucceed;
    }

    public String getSendTarget() {
        return sendTarget;
    }

    public void setSendTarget(String sendTarget) {
        this.sendTarget = sendTarget;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HOrder getOrderH() {
        return orderH;
    }

    public void setOrderH(HOrder orderH) {
        this.orderH = orderH;
    }

    public int getHisNo() {
        return hisNo;
    }

    public void setHisNo(int hisNo) {
        this.hisNo = hisNo;
    }

}
