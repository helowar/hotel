package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HtlTempQuota extends CEntity implements Entity {

    private Long ID;

    /**
     * 房间Id
     */
    private long roomId;
    
    /**
     * QuotaCutoffDayNewID
     */
    private long quotaCutoffDayNewId;
    /**
     * 床型
     */
    private String bedId;

    /**
     * 床型状态
     */
    private String bedStatus;
    
    /**
     * 原床型状态
     */
    private String oldBedStatus;
   

    /**
     * 临时配额模式
     */
    private String tempQuotaMode;

    /**
     * 临时配额数量
     */
    private int quotaQty;
    
    /**
     * 可用临时配额数量
     */
    private int ableQuotaQty;
    /**
     * 已用临时配额数量
     */
    private Long usedQuotaQty;
    /**
     * cutofftime
     */
    private String cutofftime;
    
    /**
     * 配额过期日期
     */
    private Long cutoffday;
    
    /**
     * 房型ID
     */
    private long roomTypeId;
    
    /**
     * 房型名称
     */
    private String roomTypeStr;
    
    /**
     * 当前日期
     */
    private Date startDate;
    
    /**
     * 配额原数目
     */
    private int quotaQtyCo;
    
    /**
     * 增加配额
     */
    private long addQuotaQty;
    
    /**
     * 减少配额
     */
    private long delQuotaQty;
    
    
    //房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
    
    /**
     * 配额预警数
     */
    private Long forewarnQuotaNumRoSta;

    /**
     * 配额预警标识
     */
    private Long forewarnFlagRoSta;
    
    /**
     * 可用配额总数(合约)
     */
    private Long quotaAvailableSumRoSta;
    /**
     * 已用配额总数（合约）
     */
    private Long quotaUsedSumRosta;
    /**
     * 配额总数（合约）
     */
    private Long quotaSumRosta;
    
    /**
     * quotaNewId  新配额总表ID
     */
    private Long quotaNewId;
    
    //房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  end
    
    
    //房态调整日志  hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
    
    /**
     * 操作部门
     */
    private String operatorDept;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private Date operatorTime;
    /**
     * 配额进店模式
     */
    private String quotaPattern;
    
    private String comQuotaCot;

    
    //房态调整日志  hotel2.9.3 add by shengwei.zuo 2009-10-16  end
    
    public int getQuotaQtyCo() {
		return quotaQtyCo;
	}

	public void setQuotaQtyCo(int quotaQtyCo) {
		this.quotaQtyCo = quotaQtyCo;
	}

	public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public int getQuotaQty() {
        return quotaQty;
    }

    public void setQuotaQty(int quotaQty) {
        this.quotaQty = quotaQty;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getTempQuotaMode() {
        return tempQuotaMode;
    }

    public void setTempQuotaMode(String tempQuotaMode) {
        this.tempQuotaMode = tempQuotaMode;
    }

    public String getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(String bedStatus) {
        this.bedStatus = bedStatus;
    }

    public String getCutofftime() {
        return cutofftime;
    }

    public void setCutofftime(String cutofftime) {
        this.cutofftime = cutofftime;
    }

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public long getAddQuotaQty() {
		return addQuotaQty;
	}

	public void setAddQuotaQty(long addQuotaQty) {
		this.addQuotaQty = addQuotaQty;
	}

	public long getDelQuotaQty() {
		return delQuotaQty;
	}

	public void setDelQuotaQty(long delQuotaQty) {
		this.delQuotaQty = delQuotaQty;
	}

	public long getQuotaCutoffDayNewId() {
		return quotaCutoffDayNewId;
	}

	public void setQuotaCutoffDayNewId(long quotaCutoffDayNewId) {
		this.quotaCutoffDayNewId = quotaCutoffDayNewId;
	}

	public Long getForewarnQuotaNumRoSta() {
		return forewarnQuotaNumRoSta;
	}

	public void setForewarnQuotaNumRoSta(Long forewarnQuotaNumRoSta) {
		this.forewarnQuotaNumRoSta = forewarnQuotaNumRoSta;
	}

	public Long getForewarnFlagRoSta() {
		return forewarnFlagRoSta;
	}

	public void setForewarnFlagRoSta(Long forewarnFlagRoSta) {
		this.forewarnFlagRoSta = forewarnFlagRoSta;
	}

	public Long getQuotaAvailableSumRoSta() {
		return quotaAvailableSumRoSta;
	}

	public void setQuotaAvailableSumRoSta(Long quotaAvailableSumRoSta) {
		this.quotaAvailableSumRoSta = quotaAvailableSumRoSta;
	}

	public Long getQuotaNewId() {
		return quotaNewId;
	}

	public void setQuotaNewId(Long quotaNewId) {
		this.quotaNewId = quotaNewId;
	}

	public String getOperatorDept() {
		return operatorDept;
	}

	public void setOperatorDept(String operatorDept) {
		this.operatorDept = operatorDept;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Date getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}

	public int getAbleQuotaQty() {
		return ableQuotaQty;
	}

	public void setAbleQuotaQty(int ableQuotaQty) {
		this.ableQuotaQty = ableQuotaQty;
	}

	public Long getCutoffday() {
		return cutoffday;
	}

	public void setCutoffday(Long cutoffday) {
		this.cutoffday = cutoffday;
	}

	public String getQuotaPattern() {
		return quotaPattern;
	}

	public void setQuotaPattern(String quotaPattern) {
		this.quotaPattern = quotaPattern;
	}

	public String getOldBedStatus() {
		return oldBedStatus;
	}

	public void setOldBedStatus(String oldBedStatus) {
		this.oldBedStatus = oldBedStatus;
	}

	public String getRoomTypeStr() {
		return roomTypeStr;
	}

	public void setRoomTypeStr(String roomTypeStr) {
		this.roomTypeStr = roomTypeStr;
	}

	public Long getQuotaSumRosta() {
		return quotaSumRosta;
	}

	public void setQuotaSumRosta(Long quotaSumRosta) {
		this.quotaSumRosta = quotaSumRosta;
	}

	public Long getQuotaUsedSumRosta() {
		return quotaUsedSumRosta;
	}

	public void setQuotaUsedSumRosta(Long quotaUsedSumRosta) {
		this.quotaUsedSumRosta = quotaUsedSumRosta;
	}

	public Long getUsedQuotaQty() {
		return usedQuotaQty;
	}

	public void setUsedQuotaQty(Long usedQuotaQty) {
		this.usedQuotaQty = usedQuotaQty;
	}

	public String getComQuotaCot() {
		return comQuotaCot;
	}

	public void setComQuotaCot(String comQuotaCot) {
		this.comQuotaCot = comQuotaCot;
	}

	



}
