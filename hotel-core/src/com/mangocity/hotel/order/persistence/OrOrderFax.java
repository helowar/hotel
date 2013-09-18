
package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.util.Entity;

/** 
 *  订单传真/邮件发送表
 *  
 *  @author chenkeming
 */
public class OrOrderFax implements Entity {

    private static final long serialVersionUID = -2131568809938674701L;

    /**
	 * ID <pk>
	 */
    private Long ID;
    
    /**
	 * 历史单编号
	 */
    private int hisNo = 0;
    
    /**
	 * 酒店ID
	 */
    private Long hotelId;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private OrOrder order;    

    /**
	 * 发送方式：传真/电邮/Ebooking
	 * 
	 * @see ConfirmType
	 */
    private int channel;

    /**
	 * 传真类型：确认/修改/取消
	 * 
	 * @see HotelConfirmType
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
	 * 发送目标：传真号码/email地址
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
	 * Unicall返回ID
	 */
    private String unicallRetId;
    
    /**
	 * 是否酒店已回传
	 */
    private boolean hotelReturn;
    
    /**
	 * 回传结果：已确认/其它结果
	 */
    private int returnResult;
    
    /**
	 * 是否已确认
	 */
    private boolean isConfirm;
    
    /**
	 * 确认号
	 */
    private String confirmNo;
    
    /**
	 * 备注
	 */
    private String notes;
    
    /**
	 * 条形码<br>
	 * 和OrFaxLog的barCode关联
	 */
    private String barCode;
    
    /**
	 * 该传真/邮件回传的确认号是否有效
	 */
    private boolean validConfirm = false;
    
    /**
	 * unicall回传list
	 */
    private List<OrFaxLog> logList = new ArrayList<OrFaxLog>();

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public boolean isValidConfirm() {
        return validConfirm;
    }

    public void setValidConfirm(boolean validConfirm) {
        this.validConfirm = validConfirm;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHotelReturn() {
        return hotelReturn;
    }

    public void setHotelReturn(boolean hotelReturn) {
        this.hotelReturn = hotelReturn;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public boolean getIsConfirm() {
        return isConfirm;
    }
    
    public void setConfirm(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }
    
    public void setIsConfirm(boolean isConfirm) {
        this.isConfirm = isConfirm;
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

    public int getReturnResult() {
        return returnResult;
    }

    public void setReturnResult(int returnResult) {
        this.returnResult = returnResult;
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

    public String getUnicallRetId() {
        return unicallRetId;
    }

    public void setUnicallRetId(String unicallRetId) {
        this.unicallRetId = unicallRetId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public List<OrFaxLog> getLogList() {
        return logList;
    }

    public void setLogList(List<OrFaxLog> logList) {
        this.logList = logList;
    }

    public int getHisNo() {
        return hisNo;
    }

    public void setHisNo(int hisNo) {
        this.hisNo = hisNo;
    }

}
