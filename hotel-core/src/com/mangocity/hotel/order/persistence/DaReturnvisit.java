package com.mangocity.hotel.order.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.Entity;

/**
 * DaReturnvisit entity.
 * 回访总表
 * @author MyEclipse Persistence Tools
 */

public class DaReturnvisit implements Entity, Serializable {

    // Fields

    private Long ID;
    /**
	 * 联系人号码
	 */
    private String linkmannumber;
    /**
	 * 回访完成日期
	 */
    private Date returnvisitidate;
    /**
	 * 联系人姓名
	 */
    private String linkmanname;
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
    private String delaytime;
    /**
	 * 再分配时间
	 */
    private Date reassigntime;
    /**
     * 回访状态
     */
    private Integer returnstate;
    /**
     * 获取方式
     */
    private Integer aquireway;
    /**
     * 获取状态
     */
    private Integer aquirestate;
    /**
     * 释放原因
     */
    private Integer releasereason;
    /**
     * 审核时间
     */
    private Date auditordate;

    // Constructors

    /** default constructor */
    public DaReturnvisit() {
    }

    /** minimal constructor */
    public DaReturnvisit(Long returnvisitId) {
        this.ID = returnvisitId;
    }

    /** full constructor */
    public DaReturnvisit(Long returnvisitId, String linkmannumber,
            Date returnvisitidate, String linkmanname, String acquireid,
            String acquirename, Date acquiretime, String deliverid,
            String delivername, Date delivertime, String delaytime,
            Date reassigntime,Integer returnstate,Integer aquireway,Integer aquirestate) {
        this.ID = returnvisitId;
        this.linkmannumber = linkmannumber;
        this.returnvisitidate = returnvisitidate;
        this.linkmanname = linkmanname;
        this.acquireid = acquireid;
        this.acquirename = acquirename;
        this.acquiretime = acquiretime;
        this.deliverid = deliverid;
        this.delivername = delivername;
        this.delivertime = delivertime;
        this.delaytime = delaytime;
        this.reassigntime = reassigntime;
        this.returnstate = returnstate;
        this.aquireway = aquireway;
        this.aquirestate = aquirestate;
    }

    // Property accessors

    public String getLinkmannumber() {
        return this.linkmannumber;
    }

    public void setLinkmannumber(String linkmannumber) {
        this.linkmannumber = linkmannumber;
    }

    public Date getReturnvisitidate() {
        return this.returnvisitidate;
    }

    public void setReturnvisitidate(Date returnvisitidate) {
        this.returnvisitidate = returnvisitidate;
    }

    public String getLinkmanname() {
        return this.linkmanname;
    }

    public void setLinkmanname(String linkmanname) {
        this.linkmanname = linkmanname;
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

    public String getDelaytime() {
        return this.delaytime;
    }

    public void setDelaytime(String delaytime) {
        this.delaytime = delaytime;
    }

    public Date getReassigntime() {
        return this.reassigntime;
    }

    public void setReassigntime(Date reassigntime) {
        this.reassigntime = reassigntime;
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long id) {
        ID = id;
    }
	public Integer getReturnstate() {
		return returnstate;
	}
	public void setReturnstate(Integer returnstate) {
		this.returnstate = returnstate;
	}
	public Integer getAquireway() {
		return aquireway;
	}
	public void setAquireway(Integer aquireway) {
		this.aquireway = aquireway;
	}
	public Integer getAquirestate() {
		return aquirestate;
	}

	public void setAquirestate(Integer aquirestate) {
		this.aquirestate = aquirestate;
	}
	public Integer getReleasereason() {
		return releasereason;
	}
	public void setReleasereason(Integer releasereason) {
		this.releasereason = releasereason;
	}
	public Date getAuditordate() {
		return auditordate;
	}
	public void setAuditordate(Date auditordate) {
		this.auditordate = auditordate;
	}

}