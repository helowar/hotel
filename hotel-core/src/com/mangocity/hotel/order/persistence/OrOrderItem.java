
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.util.Entity;
import com.mangocity.util.hotel.constant.QuotaType;

/** 
 *  or_orderitem
 *  
 *  @author chenkeming
 */
public class OrOrderItem implements Entity {

	private static final long serialVersionUID = 4456235021667062869L;

	/**
	 * ID <pk>
	 */
	private Long ID;

	/**
	 * 订单ID <fk> 和OrOrder关联
	 */
	private OrOrder order;	

	/**
	 * Night (短日期)
	 */
	private Date night;

	/**
	 * 房态
	 */
	private String roomState;
	
	/**
	 * 房间数（都为1）
	 */
	private int quantity;
	
	/**
	 * 订单状态：已撤单,noshow,提前退房
	 */
	private int orderState;

	/**
	 * 是否为订单第一天：方便计算订单是否noshow
	 */
	private boolean firstNight;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 如果是红单、或附加单，则该单应与原订单关联。<br>
	 * 即是哪条记录的红单，哪条记录的附加<br>
	 * itemsIdLink字段目前用于hisNo
	 */
	private Long hisNo = 0L;
	
	// private Long itemsIdLink;
	
	/**
	 * 备注
	 */
	private String notes;
	
	/**
	 * 明细数据类型：1order（正常订单明细）、2red（红单）、3add（冲后附加单）
	 * 
	 * @see OrderItemType
	 */
	private int orderItemsType;
	
	/**
	 * 是否已日结  
	 */
	private boolean settlement;
	
	/**
	 * 结算日期
	 */
	private Date settlementDate;
	
	/**
	 * 是否已确认
	 */
	private boolean isConfirm;
	
	/**
	 * 配额ID
	 */
	private Long quotaId;
	
	/**
	 * 配额类型
	 * 
	 * @see QuotaType
	 */
	private String quotaType;
	
	/**
	 * 配额批次id
	 */
	private Long quotaBatchId;
	
	/**
	 * cutoffday
	 */
	private long cutoffDay;
	
	/**
	 * 单价
	 */
	private double price;
	
	/**
	 * 销售价
	 */
	private double salePrice;
	
	/**
	 * 底价
	 */
	private double basePrice;
	
	
	/**
	 * add by zengyong 
	 * 商旅价
	 */

	private double tmcPrice;

	public double getTmcPrice() {
		return tmcPrice;
	}

	public void setTmcPrice(double tmcPrice) {
		this.tmcPrice = tmcPrice;
	}

	/**
	 * 加幅
	 */
	private double amplitude;
	
	/**
	 * 门市价
	 */
	private double marketPrice;
	
	/**
	 * 建议门市价
	 */
	private double adviceMarketPrice;
	
	/**
	 * 服务费
	 */
	private double serviceFee;
	
	/**
	 * 房价是否含服务费
	 */
	private boolean includeService;
	
	/**
	 * 佣金
	 */
	private double commission;
		
	/**
	 * 含早类型
	 */
	private int breakfast;
	
	/**
	 * 含早数量
	 */
	private int breakfastNum;
	
	/**
	 * 含早形式
	 */
	private String breakfastWay;
	
	/**
	 * 含早价
	 */
	private double breakfastPrice;
	
	/**
	 * 含早价是否返佣
	 */
	private boolean includeBreakfast;	

	/**
	 * 日审ID <fk> 和OrDailyAudit关联
	 */
	private OrDailyAudit audit;	
	
	/**
	 * 日审状态：完成/操作中
	 */
	private int auditState;
	
	/**
	 * 房间号
	 */
	private String roomNo;
	
	/**
	 * 特别说明
	 */
	private String specialNote;
	
	/**
	 * 分配
	 */
	private Long assignTo;
	
	/**
	 * 记录日审的人
	 */
	private String notesMan;
	
	/**
	 * 日审记录时间
	 */
	private Date noteTime;
	
	/**
	 * 已入住/未入住   --是否入住是用orderstate来表示,orderstate==1已入住，2未入住，0未审核，add by luoguangming
	 */
	private int noteResult;
	
	/**
	 * 是订单的第几天(zero based)
	 */
	private int dayIndex;
	
	/**
	 * 酒店ID
	 */
	private Long hotelId;
	
	/**
	 * 是订单的第几间房(zero based)
	 */
	private int roomIndex;
	
	/**
	 * 分配ID
	 */
	private long assignCustomId;
	
	/**
	 * 扣的配额对会员属于共享还是独占,独占为1，共享为0
	 */
	private int memberQuotaType;
	
	/**
	 * 是否显示
	 */
	private boolean show;
	
	/**
	 * 订单来源: 1:mango 2:tmc
	 */
	private int auditType;

	/**
	 * 是否为订单最后一天
	 */
	private boolean lastNight;
	
	/**
	 * 配额模式
	 */
	private String quotaPattern;
	
	/**
	 * 入住人姓名
	 */
	private String fellowName;	
	
	/**
	 * includeService用于quotaCantReturn
	 */
	private boolean quotaCantReturn = false;
    
    /**
     * 政府基金
     */
    private double otherTax;

    /**
     * 是否有政府基金
     */
    private boolean includeOtherTax;
    
    private double faxfee;
    private boolean has_faxfee;
    /**
     * quotaholder 配额所有者,CC/TMP/TP, CC的配额可供TMP,TP公用
     */
    private String quotaholder;
    /**
     * quotashare 是否为共享模式,1为是,0为否
     */
    private Long quotashare;
	
    /**
     * 每间房的优惠立减金额 V2.9.3.1 add by chenjiajie 2009-10-15
     */
    private float favourableAmount;	
    
    /*用于三方协议酒店---begin*/
    /**
     * 是否含宽带
     */
    private boolean includeNetFee;
    
    /**
     * 宽带费
     */
    private double netFee;
    
    /**
     * 是否含税
     */
    private boolean hasTaxFee;
    
    /**
     * 税费
     */
    private double taxFee;
    
    /*用于三方协议酒店---end*/
    
    /**
     * B2B 代理 add by shengwei.zuo 2010-1-13 begin
     */
    
	    //代理 佣金
	    private  double agentComission;
	    
	    //代理佣金价
	    private  double agentComissionPrice;
	    
	    //代理佣金率
	    private  double agentComissionRate;
	    
	    //佣金税率
	    private  double commTax ;
	    
	    //无用字段，从B2B代理开始，用来表示 佣金类型  2010-1-13   1 为 佣金率 ，2 为 现金
	    private  int  comissionType  ;
	    
	    //无用字段，从B2B代理开始，用来表示 佣金类型对应的值  2010-1-13
	    private  String comissionTypeValue;
	    
	    //显示给在代理系统的代理 佣金 add by zhijie.gu 2010-3-17
	    private  double agentReadComission;
	    
	    //显示给在代理系统的代理佣金价
	    private  double agentReadComissionPrice;
	    
	    //显示给在代理系统的代理佣金率
	    private  double agentReadComissionRate;
    
    /**
     * B2B 代理 add by shengwei.zuo 2010-1-13 end
     */
	    
    /**
	 * 现金返还 add by linpeng.fang 2010-10-12
	 */
	private double cashReturnAmount = 0.0;   
    
	public boolean isIncludeOtherTax() {
        return includeOtherTax;
    }

    public void setIncludeOtherTax(boolean includeOtherTax) {
        this.includeOtherTax = includeOtherTax;
    }

    public double getOtherTax() {
        return otherTax;
    }

    public void setOtherTax(double otherTax) {
        this.otherTax = otherTax;
    }

    public int getMemberQuotaType() {
		return memberQuotaType;
	}

	public void setMemberQuotaType(int memberQuotaType) {
		this.memberQuotaType = memberQuotaType;
	}

	public boolean isLastNight() {
		return lastNight;
	}

	public void setLastNight(boolean lastNight) {
		this.lastNight = lastNight;
	}

	public int getAuditType() {
		return auditType;
	}

	public void setAuditType(int auditType) {
		this.auditType = auditType;
	}

	public long getAssignCustomId() {
		return assignCustomId;
	}

	public void setAssignCustomId(long assignCustomId) {
		this.assignCustomId = assignCustomId;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	public int getRoomIndex() {
		return roomIndex;
	}

	public void setRoomIndex(int roomIndex) {
		this.roomIndex = roomIndex;
	}

	public double getAdviceMarketPrice() {
		return adviceMarketPrice;
	}

	public void setAdviceMarketPrice(double adviceMarketPrice) {
		this.adviceMarketPrice = adviceMarketPrice;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	public Long getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Long assignTo) {
		this.assignTo = assignTo;
	}

	public OrDailyAudit getAudit() {
		return audit;
	}

	public void setAudit(OrDailyAudit audit) {
		this.audit = audit;
	}

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public int getBreakfast() {
		return breakfast;
	}

	public void setBreakfast(int breakfast) {
		this.breakfast = breakfast;
	}

	public int getBreakfastNum() {
		return breakfastNum;
	}

	public void setBreakfastNum(int breakfastNum) {
		this.breakfastNum = breakfastNum;
	}

	public double getBreakfastPrice() {
		return breakfastPrice;
	}

	public void setBreakfastPrice(double breakfastPrice) {
		this.breakfastPrice = breakfastPrice;
	}

	public String getBreakfastWay() {
		return breakfastWay;
	}

	public void setBreakfastWay(String breakfastWay) {
		this.breakfastWay = breakfastWay;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCutoffDay() {
		return cutoffDay;
	}

	public void setCutoffDay(long cutoffDay) {
		this.cutoffDay = cutoffDay;
	}

	public boolean isFirstNight() {
		return firstNight;
	}

	public void setFirstNight(boolean firstNight) {
		this.firstNight = firstNight;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public boolean isIncludeBreakfast() {
		return includeBreakfast;
	}

	public void setIncludeBreakfast(boolean includeBreakfast) {
		this.includeBreakfast = includeBreakfast;
	}

	public boolean isIncludeService() {
		return includeService;
	}

	public void setIncludeService(boolean includeService) {
		this.includeService = includeService;
	}

	public boolean isConfirm() {
		return isConfirm;
	}

	public void setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public boolean getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}
	
	/*public Long getItemsIdLink() {
		return itemsIdLink;
	}

	public void setItemsIdLink(Long itemsIdLink) {
		this.itemsIdLink = itemsIdLink;
	}*/

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Date getNight() {
		return night;
	}

	public void setNight(Date night) {
		this.night = night;
	}

	public int getNoteResult() {
		return noteResult;
	}

	public void setNoteResult(int noteResult) {
		this.noteResult = noteResult;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotesMan() {
		return notesMan;
	}

	public void setNotesMan(String notesMan) {
		this.notesMan = notesMan;
	}

	public Date getNoteTime() {
		return noteTime;
	}

	public void setNoteTime(Date noteTime) {
		this.noteTime = noteTime;
	}

	public OrOrder getOrder() {
		return order;
	}

	public void setOrder(OrOrder order) {
		this.order = order;
	}

	public int getOrderItemsType() {
		return orderItemsType;
	}

	public void setOrderItemsType(int orderItemsType) {
		this.orderItemsType = orderItemsType;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Long getQuotaBatchId() {
		return quotaBatchId;
	}

	public void setQuotaBatchId(Long quotaBatchId) {
		this.quotaBatchId = quotaBatchId;
	}

	public Long getQuotaId() {
		return quotaId;
	}

	public void setQuotaId(Long quotaId) {
		this.quotaId = quotaId;
	}

	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public String getRoomState() {
		return roomState;
	}

	public void setRoomState(String roomState) {
		this.roomState = roomState;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(double serviceFee) {
		this.serviceFee = serviceFee;
	}

	public boolean isSettlement() {
		return settlement;
	}

	public void setSettlement(boolean settlement) {
		this.settlement = settlement;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getSpecialNote() {
		return specialNote;
	}

	public void setSpecialNote(String specialNote) {
		this.specialNote = specialNote;
	} 
	
	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	
	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}
	
	public String getQuotaPattern() {
		return quotaPattern;
	}

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }
    
    public boolean isAudited() {
        if(null == this.audit) {
            return false;
        }
        return true;
    }
    
    /**
	 * 是否呼出配额
	 * @return
	 */
	public boolean isCallQuota() {
		return quotaType.equals(QuotaType.CALLQUOTA);
	}

	public String getFellowName() {
		return fellowName;
	}

	public void setFellowName(String fellowName) {
		this.fellowName = fellowName;
	}

	public Long getHisNo() {
		return hisNo;
	}

	public void setHisNo(Long hisNo) {
		this.hisNo = hisNo;
	}

	public boolean isQuotaCantReturn() {
		return quotaCantReturn;
	}

	public void setQuotaCantReturn(boolean quotaCantReturn) {
		this.quotaCantReturn = quotaCantReturn;
	}

	public double getFaxfee() {
		return faxfee;
	}

	public void setFaxfee(double faxfee) {
		this.faxfee = faxfee;
	}

	public boolean isHas_faxfee() {
		return has_faxfee;
	}

	public void setHas_faxfee(boolean has_faxfee) {
		this.has_faxfee = has_faxfee;
	}

    public String getQuotaholder() {
        return quotaholder;
    }

    public void setQuotaholder(String quotaholder) {
        this.quotaholder = quotaholder;
    }

    public Long getQuotashare() {
        return quotashare;
    }

    public void setQuotashare(Long quotashare) {
        this.quotashare = quotashare;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }
	
	public float getFavourableAmount() {
		return favourableAmount;
	}

	public void setFavourableAmount(float favourableAmount) {
		this.favourableAmount = favourableAmount;
	}	

    public boolean isIncludeNetFee() {
        return includeNetFee;
    }

    public void setIncludeNetFee(boolean includeNetFee) {
        this.includeNetFee = includeNetFee;
    }

    public double getNetFee() {
        return netFee;
    }

    public void setNetFee(double netFee) {
        this.netFee = netFee;
    }

    public boolean isHasTaxFee() {
        return hasTaxFee;
    }

    public void setHasTaxFee(boolean hasTaxFee) {
        this.hasTaxFee = hasTaxFee;
    }

    public double getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(double taxFee) {
        this.taxFee = taxFee;
    }
    
    public int compareTo(OrOrderItem o) {
        
        if(null == o){
            return 1;
        }
        
        if(null == this.night){
            return -1;
        }
        
        if(null == o.getNight()){
            return 1;
        }
        
        return this.night.compareTo(o.getNight());
    }

	public double getAgentComission() {
		return agentComission;
	}

	public void setAgentComission(double agentComission) {
		this.agentComission = agentComission;
	}

	public double getAgentComissionPrice() {
		return agentComissionPrice;
	}

	public void setAgentComissionPrice(double agentComissionPrice) {
		this.agentComissionPrice = agentComissionPrice;
	}

	public double getAgentComissionRate() {
		return agentComissionRate;
	}

	public void setAgentComissionRate(double agentComissionRate) {
		this.agentComissionRate = agentComissionRate;
	}

	public double getCommTax() {
		return commTax;
	}

	public void setCommTax(double commTax) {
		this.commTax = commTax;
	}

	public int getComissionType() {
		return comissionType;
	}

	public void setComissionType(int comissionType) {
		this.comissionType = comissionType;
	}

	public String getComissionTypeValue() {
		return comissionTypeValue;
	}

	public void setComissionTypeValue(String comissionTypeValue) {
		this.comissionTypeValue = comissionTypeValue;
	}

	public double getAgentReadComission() {
		return agentReadComission;
	}

	public void setAgentReadComission(double agentReadComission) {
		this.agentReadComission = agentReadComission;
	}

	public double getAgentReadComissionPrice() {
		return agentReadComissionPrice;
	}

	public void setAgentReadComissionPrice(double agentReadComissionPrice) {
		this.agentReadComissionPrice = agentReadComissionPrice;
	}

	public double getAgentReadComissionRate() {
		return agentReadComissionRate;
	}

	public void setAgentReadComissionRate(double agentReadComissionRate) {
		this.agentReadComissionRate = agentReadComissionRate;
	}
	
	public double getCashReturnAmount() {
		return cashReturnAmount;
	}

	public void setCashReturnAmount(double cashReturnAmount) {
		if(!Double.isNaN(cashReturnAmount) && cashReturnAmount >= 0){
			this.cashReturnAmount = cashReturnAmount;
		}else{
			this.cashReturnAmount = 0;
		}
	}

}
