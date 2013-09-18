
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.util.Entity;

/** 
 *  订单客户确认信息发送表
 *  
 *  @author chenkeming
 */
public class OrMemberConfirm implements Entity {

    private static final long serialVersionUID = -649206236812465591L;

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
    
    /**
	 * 发送状态
	 * 1：待发送 2：成功   3：失败  4：已确认  5：失败已确认 6：其它
	 * add by chenjiajie 2009-02-17 V2.7.1
	 */
    private Integer sendStatus;
    
    /**
	 * Unicall返回ID
	 * add by chenjiajie 2009-02-17 V2.7.1
	 */
    private Long unicallRetId;
    
    /**
	 * 发送人ID
	 * add by chenjiajie 2009-02-17 V2.7.1
	 */
    private String sendManId;
    
    /**
     *调用短信平台发送短信，返回的SP ID 号  add by shengwei.zuo  2010-7-29
     */
    private Long  msSpID;
    
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

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
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

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
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

    public int getHisNo() {
        return hisNo;
    }

    public void setHisNo(int hisNo) {
        this.hisNo = hisNo;
    }

    public Long getUnicallRetId() {
        return unicallRetId;
    }

    public void setUnicallRetId(Long unicallRetId) {
        this.unicallRetId = unicallRetId;
    }

    public String getSendManId() {
        return sendManId;
    }

    public void setSendManId(String sendManId) {
        this.sendManId = sendManId;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

	public Long getMsSpID() {
		return msSpID;
	}

	public void setMsSpID(Long msSpID) {
		this.msSpID = msSpID;
	}


}
