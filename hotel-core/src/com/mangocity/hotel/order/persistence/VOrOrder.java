
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.util.Entity;


/**
 * 
 *  订单视图，供日审使用，同时支持TMC订单日审
 *  TMC订单得ID重复
 *  @author chenkeming
 */
public class VOrOrder implements Entity {

    private static final long serialVersionUID = 7961304951515956365L;

    /**
	 * 订单ID <pk>
	 */
    private Long ID;
    
    private Integer version;
    
    /**
	 * 订单CD
	 */
    private String orderCD;


    /**
	 * 酒店ID
	 */
    private Long hotelId;

    /**
	 * 酒店名称
	 */
    private String hotelName;
    
    /**
	 * 订单状态（前台订单未提交，已提交订单，已提交中台，中台处理完毕，已开始日审，日审完毕，已日结
	 * 
	 * @see OrderState
	 */
    private int orderState;

    /**
	 * 信用卡担保状态
	 * 
	 * @see GuaranteeState
	 */
    private int suretyState;
    
    /**
	 * 日审状态：noshow，提前退房，正常退房，延住
	 */
    private int auditState;
    
    /**
	 * 房间数量
	 */
    private int roomQuantity;
        
    /**
	 * 入住日期
	 */
    private Date checkinDate;

    /**
	 * 退房时间
	 */
    private Date checkoutDate;
    
    /**
	 * 最后修改人中英文姓名
	 */
    private String modifierName;

    /**
	 * 订单最后修改时间
	 */
    private Date modifiedTime;
    
    /**
	 * noshow原因
	 */
    private String noshowReason;
    
    /**
	 * 特别说明
	 */
    private String specialNote;
    
    /**
	 *  订单来源
	 */
    private int orderType;

    /**
	 * 联系人
	 */
    private String linkMan;    
    
    /**
	 * 手机
	 */
    private String mobile;    
    
    /**
	 * 房型ID
	 */
    private Long roomTypeId;
    
    /**
	 * 房型名称
	 */
    private String roomTypeName;    
    
    /**
	 * 是否需要信用卡担保
	 */
    private boolean isCreditAssured;    
    
    /**
	 * 入住人列表
	 */
    private String fellowNames;    
    
    /**
	 * 面付/预付<br>
	 * 为了和酒店本部一致，这里采用字符串:<br>
	 * 1. "pay" : 面付<br>
	 * 2. "pre_pay" : 预付<br>
	 * 
	 * @see PayMethod类
	 */
    private String payMethod;
    
    /**
	 * 会员ID
	 */
    private Long memberId;
    
    /**
	 * 会员姓名
	 */
    private String memberName;
    
    /**
	 * 酒店确认号
	 */
    private String confirmNo;        
    
    /**
	 * 新增noshow原因代码 add by chenkeming@2008.12.04 v2.4.1 
	 */
    private int noshowCode;
    
    /**
	 * add by chenjiajie@2008.12.31 v2.4.2 订单的创建时间
	 */
    private Date createDate;
    
    /**
	 * 订单渠道
	 */
    private int channel;
    
    /**
	 * QC444日审操作后最后修改人中文名和工号对不上 
	 * add by chenjiajie@2009-05-05
	 */
    private String modifier;
    
    private Long channelID;
    
    private String channelName;
    
    private int genAudit;
    
    private int orderAuditState;
    /**
     * 房间号
     */
    private String roomNum;
    /**
     * 日审备注
     */
    private String auditRemark;
    
    private String memberCd;//增加会员cd 2012-07-30

    public String getMemberCd() {
		return memberCd;
	}

	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}

	public String getAuditRemark() {
		if (auditRemark != null){
			return auditRemark.replaceAll("&&", "\n");
		}else{
			return auditRemark;
		}
	}

	public void setAuditRemark(String auditRemark) {
		if (auditRemark != null){
			this.auditRemark = auditRemark.replaceAll("\n", "&&");
		}else{
			this.auditRemark = auditRemark;
		}
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getFellowNames() {
        return fellowNames;
    }

    public void setFellowNames(String fellowNames) {
        this.fellowNames = fellowNames;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }
    
    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(int roomQuantity) {
        this.roomQuantity = roomQuantity;
    }    

    public int getSuretyState() {
        return suretyState;
    }

    public void setSuretyState(int suretyState) {
        this.suretyState = suretyState;
    }

    public String getNoshowReason() {
        return noshowReason;
    }

    public void setNoshowReason(String noshowReason) {
        this.noshowReason = noshowReason;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
    
    public boolean isCreditAssured() {
        return isCreditAssured;
    }

    public boolean getIsCreditAssured() {
        return isCreditAssured;
    }
    
    public void setCreditAssured(boolean isCreditAssured) {
        this.isCreditAssured = isCreditAssured;
    }
    
    public void setIsCreditAssured(boolean isCreditAssured) {
        this.isCreditAssured = isCreditAssured;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getNoshowCode() {
        return noshowCode;
    }

    public void setNoshowCode(int noshowCode) {
        this.noshowCode = noshowCode;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        if(null != this.version) {
            return;
        } else {
            this.version = version;
        }        
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

	public Long getChannelID() {
		return channelID;
	}

	public void setChannelID(Long channelID) {
		this.channelID = channelID;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getGenAudit() {
		return genAudit;
	}

	public void setGenAudit(int genAudit) {
		this.genAudit = genAudit;
	}

	public int getOrderAuditState() {
		return orderAuditState;
	}

	public void setOrderAuditState(int orderAuditState) {
		this.orderAuditState = orderAuditState;
	}

}
