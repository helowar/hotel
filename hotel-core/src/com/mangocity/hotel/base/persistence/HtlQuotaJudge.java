package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * HtlQuotaJudge generated by MyEclipse Persistence Tools 配额调整操作表
 */

public class HtlQuotaJudge implements Entity {

    // Fields
    /**
	 * 
	 */
    private static final long serialVersionUID = -8172615503346104299L;

    /**
     * 配额调整ID
     */
    private Long ID;
    
    /**
     * 配额明细ID
     */
    private long quotaCutoffDayNewId;
    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 酒店ID
     */
    private Long hotelId;

    /**
     * 房型ID
     */
    private Long roomtypeId;

    /**
     * 床型ID
     */
    private Long bedId;

    /**
     * 配额类型（包房、普通、临时、呼出）
     */
    private String quotaType;

    /**
     * 配额模式（进店--表示入住的第一天扣配额、在店--表示入住的每天都要扣配额）
     */
    private String quotaPattern;

    /**
     * 配额共享模式（预付2、面付1、面预付共享3）
     */
    private String quotaShare;

    /**
     * 配额
     */
    private String quotaHolder;

    /**
     *配额过期日期
     */
    private Long cutoffday;

    /**
     * 配额过期时间
     */
    private String cutofftime;

    /**
     * 配额调整方式
     */
    private String judgeType;

    /**
     * 配额数
     */
    private Long quotaNum;

    /**
     * 配额渠道来源
     */
    private String quotaChannel;

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
     * 是否可退
     */
    private Long blnBack;
    
    
    //合同ID  add by shengwei.zuo 
    private Long contractId;
    
    //合同开始日期
    private Date contractBd;
    
    //合同结束日期
    private Date contractEd;
    
    //调整星期
    private String judgeWeeks; 
    
    private int pageNo;
    
    private int pageSize;
    
    private String []  weeks;
   
    // Constructors

    public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	/** default constructor */
    public HtlQuotaJudge() {
    }

    /** minimal constructor */
    public HtlQuotaJudge(Long htlQuotaJudgeId) {
        this.ID = htlQuotaJudgeId;
    }



    // Property accessors

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getHotelId() {
        return this.hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getRoomtypeId() {
        return this.roomtypeId;
    }

    public void setRoomtypeId(Long roomtypeId) {
        this.roomtypeId = roomtypeId;
    }

    public Long getBedId() {
        return this.bedId;
    }

    public void setBedId(Long bedId) {
        this.bedId = bedId;
    }

    public String getQuotaType() {
        return this.quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getQuotaPattern() {
        return this.quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public String getQuotaShare() {
        return this.quotaShare;
    }

    public void setQuotaShare(String quotaShare) {
        this.quotaShare = quotaShare;
    }

    public String getQuotaHolder() {
        return this.quotaHolder;
    }

    public void setQuotaHolder(String quotaHolder) {
        this.quotaHolder = quotaHolder;
    }

    public Long getCutoffday() {
        return this.cutoffday;
    }

    public void setCutoffday(Long cutoffday) {
        this.cutoffday = cutoffday;
    }

 

    public String getCutofftime() {
		return cutofftime;
	}

	public void setCutofftime(String cutofftime) {
		this.cutofftime = cutofftime;
	}

	public String getJudgeType() {
        return this.judgeType;
    }

    public void setJudgeType(String judgeType) {
        this.judgeType = judgeType;
    }

    public Long getQuotaNum() {
        return this.quotaNum;
    }

    public void setQuotaNum(Long quotaNum) {
        this.quotaNum = quotaNum;
    }

    public String getQuotaChannel() {
        return this.quotaChannel;
    }

    public void setQuotaChannel(String quotaChannel) {
        this.quotaChannel = quotaChannel;
    }

    public String getOperatorDept() {
        return this.operatorDept;
    }

    public void setOperatorDept(String operatorDept) {
        this.operatorDept = operatorDept;
    }

    public String getOperatorId() {
        return this.operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return this.operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getOperatorTime() {
        return this.operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

	public long getQuotaCutoffDayNewId() {
		return quotaCutoffDayNewId;
	}

	public void setQuotaCutoffDayNewId(long quotaCutoffDayNewId) {
		this.quotaCutoffDayNewId = quotaCutoffDayNewId;
	}

	public Long getBlnBack() {
		return blnBack;
	}

	public void setBlnBack(Long blnBack) {
		this.blnBack = blnBack;
	}

	public Date getContractBd() {
		return contractBd;
	}

	public void setContractBd(Date contractBd) {
		this.contractBd = contractBd;
	}

	public Date getContractEd() {
		return contractEd;
	}

	public void setContractEd(Date contractEd) {
		this.contractEd = contractEd;
	}

	public String getJudgeWeeks() {
		return judgeWeeks;
	}

	public void setJudgeWeeks(String judgeWeeks) {
		this.judgeWeeks = judgeWeeks;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String[] getWeeks() {
		return weeks;
	}

	public void setWeeks(String[] weeks) {
		this.weeks = weeks;
	}

}