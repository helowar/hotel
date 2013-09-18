/**
 * DaDailyaudit entity.
 * 日审总表
 * @author MyEclipse Persistence Tools
 */
package com.mangocity.hotel.order.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mangocity.util.Entity;

/**
 */
public class DaDailyaudit implements Entity, Serializable {

    // Fields

    private Long ID;
    /**
	 * 渠道ID
	 */
    private Long channelid;
    /**
	 * 渠道名称
	 */
    private String channelname;
    /**
	 * 审核日期
	 */
    private Date auditdate;
    /**
	 * 审核方式 :1:传真，2：邮件，3:电话，4：ebooking，5:直连
	 */
    private Integer auditway;
    /**
	 * 是否回传 :0: 未回传 1: 已回传
	 */
    private Integer isreturn;
    /**
	 * 状态:0: 未完成 1: 已完成
	 */
    private Integer state;
    /**
	 * 订单量
	 */
    private Long orderamount;
    /**
	 * 酒店数
	 */
    private Long hotelamount;
    /**
	 * 房间数
	 */
    private Long roomamount;
    /**
	 * 发送成功数
	 */
    private Long sendsucceed;
    /**
	 * 发送失败数
	 */
    private Long sendfailure;
    /**
	 * 获取人ID
	 */
    private String acquireid;
    /**
	 * 获取人姓名
	 */
    private String acquirename;
    /**
	 * 获取时间
	 */
    private Date acquiretime;
    /**
	 * 交接人ID
	 */
    private String deliverid;
    /**
	 * 交接人姓名
	 */
    private String delivername;
    /**
	 * 交接时间
	 */
    private Date delivertime;
    /**
	 * 延长时间
	 */
    private Integer delaytime;
    /**
	 * 回传ID
	 */
    private String returnid;
    /**
	 * 完成时间
	 */
    private Date achievetime;
    /**
	 * 再分配时间
	 */
    private Date reassigntime;
    
    /**
     * 获取方式 1:自动分配 2:手工分配
     */
    private Integer aquireWay;
    /**
     * 获取状态 1:已获取 2:未获取
     */
    private Integer aquireState;
    /**
     * 释放原因: 1,酒店忙 2.无法联系 3.中台管理员分配
     */
    private Integer releaseReason;
    
    private Set<DaDailyauditItem> daDailyauditItems = new HashSet<DaDailyauditItem>(0);
    
    private List<DaPaperFaxItem> faxList = new ArrayList<DaPaperFaxItem>();
    
    // Constructors

    /** default constructor */
    public DaDailyaudit() {
    }

    /** minimal constructor */
    public DaDailyaudit(Long dailyauditId) {
        this.ID = dailyauditId;
    }

    /** full constructor */
    public DaDailyaudit(Long dailyauditId, Long channelid, String channelname,
            Date auditdate, Integer auditway, Integer isreturn, Integer state,
            Long orderamount, Long hotelamount, Long roomamount,
            Long sendsucceed, Long sendfailure, String acquireid,
            String acquirename, Date acquiretime, String deliverid,
            String delivername, Date delivertime, Integer delaytime,
            String returnid, Date achievetime, Date reassigntime) {
        this.ID = dailyauditId;
        this.channelid = channelid;
        this.channelname = channelname;
        this.auditdate = auditdate;
        this.auditway = auditway;
        this.isreturn = isreturn;
        this.state = state;
        this.orderamount = orderamount;
        this.hotelamount = hotelamount;
        this.roomamount = roomamount;
        this.sendsucceed = sendsucceed;
        this.sendfailure = sendfailure;
        this.acquireid = acquireid;
        this.acquirename = acquirename;
        this.acquiretime = acquiretime;
        this.deliverid = deliverid;
        this.delivername = delivername;
        this.delivertime = delivertime;
        this.delaytime = delaytime;
        this.returnid = returnid;
        this.achievetime = achievetime;
        this.reassigntime = reassigntime;
    }

    // Property accessors

    public Long getChannelid() {
        return this.channelid;
    }

    public void setChannelid(Long channelid) {
        this.channelid = channelid;
    }

    public String getChannelname() {
        return this.channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    public Date getAuditdate() {
        return this.auditdate;
    }

    public void setAuditdate(Date auditdate) {
        this.auditdate = auditdate;
    }

    public Long getOrderamount() {
        return this.orderamount;
    }

    public void setOrderamount(Long orderamount) {
        this.orderamount = orderamount;
    }

    public Long getHotelamount() {
        return this.hotelamount;
    }

    public void setHotelamount(Long hotelamount) {
        this.hotelamount = hotelamount;
    }

    public Long getRoomamount() {
        return this.roomamount;
    }

    public void setRoomamount(Long roomamount) {
        this.roomamount = roomamount;
    }

    public Long getSendsucceed() {
        return this.sendsucceed;
    }

    public void setSendsucceed(Long sendsucceed) {
        this.sendsucceed = sendsucceed;
    }

    public Long getSendfailure() {
        return this.sendfailure;
    }

    public void setSendfailure(Long sendfailure) {
        this.sendfailure = sendfailure;
    }

    public String getAcquireid() {
        return this.acquireid;
    }

    public void setAcquireid(String acquireid) {
        this.acquireid = acquireid;
    }

    public String getAcquirename() {
        return this.acquirename;
    }

    public void setAcquirename(String acquirename) {
        this.acquirename = acquirename;
    }

    public Date getAcquiretime() {
        return this.acquiretime;
    }

    public void setAcquiretime(Date acquiretime) {
        this.acquiretime = acquiretime;
    }

    public String getDeliverid() {
        return this.deliverid;
    }

    public void setDeliverid(String deliverid) {
        this.deliverid = deliverid;
    }

    public String getDelivername() {
        return this.delivername;
    }

    public void setDelivername(String delivername) {
        this.delivername = delivername;
    }

    public Date getDelivertime() {
        return this.delivertime;
    }

    public void setDelivertime(Date delivertime) {
        this.delivertime = delivertime;
    }

    public Integer getDelaytime() {
        return this.delaytime;
    }

    public void setDelaytime(Integer delaytime) {
        this.delaytime = delaytime;
    }

    public String getReturnid() {
        return this.returnid;
    }

    public void setReturnid(String returnid) {
        this.returnid = returnid;
    }

    public Date getAchievetime() {
        return this.achievetime;
    }

    public void setAchievetime(Date achievetime) {
        this.achievetime = achievetime;
    }

    public Date getReassigntime() {
        return this.reassigntime;
    }

    public void setReassigntime(Date reassigntime) {
        this.reassigntime = reassigntime;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return this.ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Integer getAuditway() {
        return this.auditway;
    }

    public void setAuditway(Integer auditway) {
        this.auditway = auditway;
    }
    
    public Integer getAquireWay() {
        return aquireWay;
    }

    public void setAquireWay(Integer aquireWay) {
        this.aquireWay = aquireWay;
    }

    public Integer getAquireState() {
        return aquireState;
    }

    public void setAquireState(Integer aquireState) {
        this.aquireState = aquireState;
    }
    
    public Integer getIsreturn() {
        return this.isreturn;
    }

    public void setIsreturn(Integer isreturn) {
        this.isreturn = isreturn;
    }
    
    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

	public Set<DaDailyauditItem> getDaDailyauditItems() {
		return daDailyauditItems;
	}

	public void setDaDailyauditItems(Set<DaDailyauditItem> daDailyauditItems) {
		this.daDailyauditItems = daDailyauditItems;
	}

	public Integer getReleaseReason() {
		return releaseReason;
	}

	public void setReleaseReason(Integer releaseReason) {
		this.releaseReason = releaseReason;
	}

	public List<DaPaperFaxItem> getFaxList() {
		return faxList;
	}

	public void setFaxList(List<DaPaperFaxItem> faxList) {
		this.faxList = faxList;
	}

}