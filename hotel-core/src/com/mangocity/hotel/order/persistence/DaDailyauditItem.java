package com.mangocity.hotel.order.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.mangocity.util.Entity;

/**
 * DaDailyauditItem entity.
 * 日审明细表
 * @author MyEclipse Persistence Tools
 */

public class DaDailyauditItem implements Entity, Serializable {

    // Fields

    private Long ID;
    /**
	 * 日审ID
	 */
    private Long dailyauditid;
    /**
	 * 酒店ID
	 */
    private Long hotelid;
    /**
     * 酒店名称
     */
    private String hotelName;
    /**
	 * 订单ID
	 */
    private Long orderid;
    /**
	 * 订单编号
	 */
    private String orderCd;
    /**
	 * 审核类型 1: 入住审核 2: 退房审核
	 */
    private Integer audittype;
    /**
	 * 回访ID
	 */
    private Long returnvisitid;
    /**
	 * 会员ID
	 */
    private Long memberid;
    /**
	 * 联系人姓名
	 */
    private String linkmanname;
    /**
	 * 联系人号码
	 */
    private String linkmannumber;
    /**
	 * 入住时间
	 */
    private Date checkintime;
    /**
	 * 退房时间
	 */
    private Date checkouttime;
    /**
	 * 房型ID
	 */
    private Long roomid;
    /**
	 * 房型名称
	 */
    private String roomname;
    /**
	 * 价格类型ID
	 */
    private Long pricetypeid;
    /**
	 * 价格类型名称
	 */
    private String pricetypename;
    /**
	 * 房间数
	 */
    private String roomamount;
    /**
	 * 是否担保
	 */
    private String isavouch;
    
    /**
	 * 订单来源: 1:mango 2:tmc 
	 */
    private Integer orderType;
    /**
	 * 审核,是否统计标记: 0:未被统计 1:已被统计
	 */
    private Integer hasAuditMark;   
    /**
	 * 回访,是否统计标记: 0:未被统计 1:已被统计
	 */
    private Integer hasReturnMark;   
    /**
	 * 酒店确认号
	 */
    private String confirmNo; 
    /**
     * 备注
     */
    private String remark;
    
    //订单渠道
    private int channel;
    private String memberCd;
    
    private Set<DaDailyauditItemSubtable> daDailyauditItemSubtables = new HashSet(0);

    // Constructors

    /** default constructor */
    public DaDailyauditItem() {
    }

	public String getMemberCd() {
		return memberCd;
	}

	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}

	/** minimal constructor */
    public DaDailyauditItem(Long dailyauditItemId) {
        this.ID = dailyauditItemId;
    }

    /** full constructor */
    public DaDailyauditItem(Long dailyauditItemId, Long dailyauditid,
            Long hotelid,String hotelName, Long orderid, Integer audittype, Long returnvisitid,
            Long memberid, String linkmanname, String linkmannumber,
            Date checkintime, Date checkouttime, Long roomid, String roomname,
            Long pricetypeid, String pricetypename, String roomamount,
            String isavouch, Set daDailyauditItemSubtables) {
        this.ID = dailyauditItemId;
        this.dailyauditid = dailyauditid;
        this.hotelid = hotelid;
        this.hotelName = hotelName;
        this.orderid = orderid;
        this.audittype = audittype;
        this.returnvisitid = returnvisitid;
        this.memberid = memberid;
        this.linkmanname = linkmanname;
        this.linkmannumber = linkmannumber;
        this.checkintime = checkintime;
        this.checkouttime = checkouttime;
        this.roomid = roomid;
        this.roomname = roomname;
        this.pricetypeid = pricetypeid;
        this.pricetypename = pricetypename;
        this.roomamount = roomamount;
        this.isavouch = isavouch;
        this.daDailyauditItemSubtables = daDailyauditItemSubtables;
    }

    // Property accessors

    public Long getDailyauditid() {
        return this.dailyauditid;
    }

    public void setDailyauditid(Long dailyauditid) {
        this.dailyauditid = dailyauditid;
    }

    public Long getHotelid() {
        return this.hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public Long getOrderid() {
        return this.orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public Integer getAudittype() {
        return this.audittype;
    }

    public void setAudittype(Integer ype) {
        this.audittype = ype;
    }

    public Long getReturnvisitid() {
        return this.returnvisitid;
    }

    public void setReturnvisitid(Long returnvisitid) {
        this.returnvisitid = returnvisitid;
    }

    public Long getMemberid() {
        return this.memberid;
    }

    public void setMemberid(Long memberid) {
        this.memberid = memberid;
    }

    public String getLinkmanname() {
        return this.linkmanname;
    }

    public void setLinkmanname(String linkmanname) {
        this.linkmanname = linkmanname;
    }

    public String getLinkmannumber() {
        return this.linkmannumber;
    }

    public void setLinkmannumber(String linkmannumber) {
        this.linkmannumber = linkmannumber;
    }

    public Date getCheckintime() {
        return this.checkintime;
    }

    public void setCheckintime(Date checkintime) {
        this.checkintime = checkintime;
    }

    public Date getCheckouttime() {
        return this.checkouttime;
    }

    public void setCheckouttime(Date checkouttime) {
        this.checkouttime = checkouttime;
    }

    public Long getRoomid() {
        return this.roomid;
    }

    public void setRoomid(Long roomid) {
        this.roomid = roomid;
    }

    public String getRoomname() {
        return this.roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public Long getPricetypeid() {
        return this.pricetypeid;
    }

    public void setPricetypeid(Long pricetypeid) {
        this.pricetypeid = pricetypeid;
    }

    public String getPricetypename() {
        return this.pricetypename;
    }

    public void setPricetypename(String pricetypename) {
        this.pricetypename = pricetypename;
    }

    public String getRoomamount() {
        return this.roomamount;
    }

    public void setRoomamount(String roomamount) {
        this.roomamount = roomamount;
    }

    public String getIsavouch() {
        return this.isavouch;
    }

    public void setIsavouch(String isavouch) {
        this.isavouch = isavouch;
    }

    public Set<DaDailyauditItemSubtable> getDaDailyauditItemSubtables() {
        return this.daDailyauditItemSubtables;
    }

    public void setDaDailyauditItemSubtables(Set daDailyauditItemSubtables) {
        this.daDailyauditItemSubtables = daDailyauditItemSubtables;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return this.ID;
    }

    public void setID(Long id) {
        ID = id;
    }

	public String getOrderCd() {
		return orderCd;
	}

	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}    

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public Integer getHasAuditMark() {
		return hasAuditMark;
	}

	public void setHasAuditMark(Integer hasAuditMark) {
		this.hasAuditMark = hasAuditMark;
	}

	public Integer getHasReturnMark() {
		return hasReturnMark;
	}

	public void setHasReturnMark(Integer hasReturnMark) {
		this.hasReturnMark = hasReturnMark;
	}

	public String getConfirmNo() {
		return confirmNo;
	}

	public void setConfirmNo(String confirmNo) {
		this.confirmNo = confirmNo;
	}

	public String getRemark() {
		if (null != remark){
			return remark.replaceAll("&&", "\n");
		}else{
			return remark;
		}
	}

	public void setRemark(String remark) {
		if (remark != null) {
			this.remark = remark.replaceAll("\n", "&&");
		} else {
			this.remark = remark;
		}
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}
}