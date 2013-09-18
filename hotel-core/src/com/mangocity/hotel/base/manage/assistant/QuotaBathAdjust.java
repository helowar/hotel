package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 * hotel 2.9.3 配额改造 add by shengwei.zuo 2009-09-12  批量调整配额辅助类
 */
public class QuotaBathAdjust implements Serializable {

    /**
     * id
     */
    private Long cutoffDayID;


    /**
     * 开始日期
     */
    private Date cutoffBeginDate;

    /**
     * 结束日期
     */
    private Date cutoffEndDate;
   
   
       /**
     * 房型ID
     */
    private String roomTypeId;
    
    /**
     * 床型ID
     */
    private String bedTypeId;
   
   
    /**
     * 提前天数
     */
    private Long cutoffDay;

    /**
     * 星期
     */
    private String weeks;

    /**
     * 提前时间
     */
    private String cutoffTime;

    /**
     * 面付或共享配额数
     */
    private Long quotaQty;
    
    /**
     * hotel 2.9.3 配额改造 add by shengwei.zuo  2009-09-11 begin
     * @return
     */
    
    //床型ID
    private Long bedId;
    
    
    // 配额调整方式
    private String judgeType;
    
    /**
     * 是否可退
     */
    private Long blnBack;


	/**
     * hotel 2.9.3 配额改造 add by shengwei.zuo  2009-09-11 end
     * @return
     */
    
    //配额是否共享
    private Long  quotaBedShare;
    
    
    public Date getCutoffBeginDate() {
        return cutoffBeginDate;
    }

    public void setCutoffBeginDate(Date cutoffBeginDate) {
        this.cutoffBeginDate = cutoffBeginDate;
    }

    public Long getCutoffDay() {
        return cutoffDay;
    }

    public void setCutoffDay(Long cutoffDay) {
        this.cutoffDay = cutoffDay;
    }

    public Long getCutoffDayID() {
        return cutoffDayID;
    }

    public void setCutoffDayID(Long cutoffDayID) {
        this.cutoffDayID = cutoffDayID;
    }

    public Date getCutoffEndDate() {
        return cutoffEndDate;
    }

    public void setCutoffEndDate(Date cutoffEndDate) {
        this.cutoffEndDate = cutoffEndDate;
    }

    public String getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(String cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

    public Long getQuotaQty() {
        return quotaQty;
    }

    public void setQuotaQty(Long quotaQty) {
        this.quotaQty = quotaQty;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

	public Long getBedId() {
		return bedId;
	}

	public void setBedId(Long bedId) {
		this.bedId = bedId;
	}

	public String getJudgeType() {
		return judgeType;
	}

	public void setJudgeType(String judgeType) {
		this.judgeType = judgeType;
	}

	public Long getBlnBack() {
		return blnBack;
	}

	public void setBlnBack(Long blnBack) {
		this.blnBack = blnBack;
	}

	public String getBedTypeId() {
		return bedTypeId;
	}

	public void setBedTypeId(String bedTypeId) {
		this.bedTypeId = bedTypeId;
	}

	public Long getQuotaBedShare() {
		return quotaBedShare;
	}

	public void setQuotaBedShare(Long quotaBedShare) {
		this.quotaBedShare = quotaBedShare;
	}

}
