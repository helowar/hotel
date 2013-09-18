
package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.constant.HraOrderType;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.CooperateChannel;
import com.mangocity.hotel.order.constant.EmergencyLevel;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.util.DateUtil;
import com.mangocity.util.Entity;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.hotel.constant.BedType;
import com.mangocity.util.hotel.constant.PayMethod;


/**
 * 
 *  订单
 *  
 *  @author chenkeming
 */
public class OrOrder implements Entity {

    private static final long serialVersionUID = -695186284798499403L;

    /**
	 * 订单ID <pk>
	 */
    private Long ID;
    
    /**
	 * v2.6 无用字段(prepayType)用来表示历史单编号
	 * @author chenkeming Feb 26, 2009 5:31:48 PM
	 */
    private int hisNo = 0;
        
    /**
	 * v2.6 无用字段(SURETYCURRENCY)用来表示当为恢复单时,其恢复目标单的历史序号<br>
	 * 默认值为-1 
	 * @author chenkeming Mar 5, 2009 3:25:25 PM
	 */
    private int resumeNo = -1;
    
    /**
	 * 订单CD
	 */
    private String orderCD;

    /**
	 * 订单CD -- 给酒店发传真用
	 */
    private String orderCDHotel;
    
    /**
	 * 源订单CD
	 */
    private String originCD;
    
    /**
	 * 酒店ID
	 */
    private Long hotelId;

    /**
	 * 酒店名称
	 */
    private String hotelName;

    /**
	 * 会员ID
	 */
    private Long memberId;
 
    /**
	 * 会员名字(包括中英文)
	 */
    private String memberName;    

    /**
	 * 订单类型 :(1-mango, 2-114,3-TMC,4-B2B代理)
	 * 
	 * @see OrderType
	 */
    private int type;
    
    /**
	 * 订单来源: 网站，CCmango，香港组
	 * 
	 * @see OrderSource
	 */
    private String source;

    /**
	 * 合作公司渠道（如德比等）
	 * hotel 2.5 合并
	 * @see ChannelType
	 */
    private int channel;
    
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
	 * 付款状态
	 */
    private int payState;
    
    /**
	 * 日审状态：noshow，提前退房，正常退房，延住
	 */
    private int auditState;
    
    /**
	 * 房型ID
	 */
    private Long roomTypeId;
    
    /**
	 * 房型名称
	 */
    private String roomTypeName;

    /**
	 * 子房型
	 */
    private Long childRoomTypeId;
        
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
	 * 紧急程度
	 * 
	 * @see EmergencyLevel
	 */
    private int emergencyLevel;
    
    /**
	 * 已经支付金额
	 */
    private double payAmount;
    
    /**
	 * 担保金额
	 */
    private double suretyPrice;
    
    /**
	 * 最晚取消时间
	 */
    private Date   allowCancelTime;
    
    /**
	 * 从V2.9.3开始存放优惠立减总金额 v2.9.3后该字段已无用 modify by chenjiajie 2009-10-15)
	 */
    private double penalty;
    
    /**
	 * 退款金额
	 */
    private double refund;
    
    /**
	 * 配送单号
	 */
    private String fulfillmentCD;
    
    /**
	 * 付款类型 网上支付、积分、现金、POS机等
	 */
    private int prepayType;
    
    /**
	 * 币种类型
	 */
    private String paymentCurrency;
    
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
	 * 预付时限
	 */
    private Date prepayLimitDate;
    
    /**
	 * 撤单原因 
	 */
    private int cancelReason;
    
    /**
	 * 担保币种
	 */
    private int suretyCurrency;
    
    /**
	 * 订单创建时间
	 */
    private Date createDate;
    
    /**
	 * 创建人登陆工号
	 */
    private String creator;
    
    /**
	 * 创建人中英文姓名
	 */
    private String creatorName;
    
    /**
	 * 修改人登陆工号
	 */
    private String modifier;
    
    /**
	 * 最后修改人中英文姓名
	 */
    private String modifierName;
    
    /**
	 * 该属性已经没用 OrOrder.hbm.xml 取消这个属性的映射 
	 * modify by chenjiajie@2009.02.19 v2.7.1
	 */
    private String modifierRole;
    
    /**
	 * 分配给中台操作人员
	 */
    private String assignTo;
    
    /**
	 * 中英文，如 (丁争 Terry)便于查询，既能查中文又能查英文）
	 */
    private String assignToName;
    
    /**
	 * 订单最后修改时间
	 */
    private Date modifiedTime;
    
    /**
	 * 酒店确认标志
	 */
    private boolean hotelConfirm;
    
    /**
	 * 客户确认标志
	 */
    private boolean customerConfirm;    
    
    /**
	 * 客人确认方式
	 * 
	 * @see ConfirmType
	 */
    private int confirmType = ConfirmType.SMS;
    
    /**
	 * 总金额
	 */
    private double sum;
    
    /**
	 * 是否虚假订单（现在没用，保留）
	 */
    private boolean illusive = false;
    
    /**
	 * （现在没用，保留，用于unicall一个电话呼入）
	 */
    private String taskId;
    
    /**
	 * 撤单原因为其他时，为手工输入
	 */
    private String cancelMessage;
    
    /**
	 * 撤单原因为客人原因时
	 */
    private String guestCancelMessage;    
    
    /**
	 * VIP级别。<br>
	 * 从会员里面来 0表示非VIP
	 */
    private int vipLevel;        
    
    /**
	 * 代理商号，如果为N则表示不是代理商定单<br>　
	 * member表中有代理商编号的都认为是代理商
	 * 暂时改成优惠代码
	 */
    private String agentCode;
    
    /**
	 * 是否手工订单
	 */
    private boolean isManualOrder;
    
    /**
	 * 订单中台分类类型（预付单、担保单、114单、散客单，香港，疑难）
	 * 
	 * @see HraOrderType
	 */
    private int hraOrderType;
    
    /**
	 * 对换比率
	 */
    private double rateId = 1;
    
    /**
	 * 转换为人民币后的支付总金额
	 */
    private double sumRmb;
    
    /**
	 * 预留日期,目前用来保存最早到店日期
	 */
    private String reservedDate;
    
    /**
	 * 联系人
	 */
    private String linkMan;
    
    /**
	 * 联系人称呼--目前用作表示联系人性别<br>
	 * 'M' : 男<br>
	 * 'F' : 女
	 */
    private String title;
    
    /**
	 * 手机
	 */
    private String mobile;    
    
    /**
	 * 附加手机号码
	 */
    private String appendMobile;    
    
    /**
	 * 电话号码
	 */
    private String telephone;
    
    
    /**
	 * 传真
	 */
    private String customerFax;
    
    /**
	 * 电子邮箱
	 */
    private String email;
    
    /**
	 * 去酒店坐什么交通工具（如飞机）
	 */
    private String arrivalTraffic;
    
    /**
	 * 车次（航班）
	 */
    private String flight;
    
    /**
	 * 最早到店时间
	 */
    private String arrivalTime;
    
    /**
	 * 最晚到店时间
	 */
    private String latestArrivalTime;
    
    /**
	 * 中英文，包括所有入住人。便于查询
	 */
    private String fellowNames;
    
    /**
	 * 已发送酒店确认传真（包括修改传真）确认和修改其实是一个概念，<br>
	 * 都是要发送传真等酒店确认。修改是重新确认
	 */
    private boolean sendedHotelFax;
    
    /**
	 * 已满足配额
	 */
    private boolean quotaOk;
    
    /**
	 * 会员所在的省份（用于114区分不同省份）
	 */
    private String memberState;
    
    /**
	 * 114序列号
	 */
    private String sequence114;
    
    /**
	 * 114订单前台取消标识
	 */
    private boolean frontCancel;
    
    /**
	 * 是否需要信用卡担保
	 */
    private boolean isCreditAssured;

    /**
	 * 会员要求使用的卡IDS可多选，现在只有114有设置该值
	 */
    private String creditCardIdsSelect;
    
    /**
	 * 确认金额
	 */
    private double confirmTotal;
    
    /**
	 * 结算金额
	 */
    private double balanceTotal;
    
    /**
	 * 面付转预付标识
	 */
    private boolean payToPrepay;
    
    /**
	 * 预付金额
	 */
    private double prepayTotalRmb;
    
    /**
	 * 是否需要退款
	 */
    private boolean needRefund;
    
    /**
	 * 退款原因
	 */
    private String refundMessage;
    
    /**
	 * 是否已退款
	 */
    private boolean hasRefund;
    
    /**
	 * 退款时限
	 */
    private Date refundLimitTime;    
    
    /**
	 * 是否需要付款给供应商
	 * 注：目前用于表示该订单的酒店是否400酒店(因为暂时不新增字段,故借用该字段)
	 */
    private boolean payToHotel;    
    
    /**
	 * 退款单领导审批已通过
	 */
    private boolean refundBillAuditPass;
    
    /**
	 * 退款单财务执行完毕
	 */
    private boolean refundDoneForFinance;
    
    /**
	 * 酒店给我们退款状态
	 */
    private int refundStateForHotel;
    
    /**
	 * fax字段没用，用于记录转合约组信息
	 */
    private String contractlog;
    
    /**
	 * 支付比率　一个月只出现几张这种单，有面付也有预付
	 */
    private String paymentRate;
    
    /**
	 * 预付完成
	 */
    private boolean hasPrepayed;
    
    /**
	 * 已口头确认酒店
	 */
    private boolean hotelConfirmTel;
    
    /**
	 * 已书面确认酒店
	 */
    private boolean hotelConfirmFax;
    
    /**
	 * 已收回传
	 */
    private boolean hotelConfirmFaxReturn;
    
    /**
	 * 已发送客户确认(包括修改)
	 */
    private boolean sendedMemberFax;
    
    /**
	 * 客人特殊要求
	 */
    private String specialRequest;        
    
    /**
	 * 是否属于被交接班订单
	 */
    private boolean inNextTeam;
    
    /**
	 * 会员CD
	 */
    private String memberCd;
    
    /**
	 * noshow原因
	 */
    private String noshowReason;
    
    /**
	 * 特别说明
	 */
    private String specialNote;
    
    /**
	 * 日审操作状态(未操作/已保存/待审核/完成) (v2.9.3后该字段已无用 modify by chenjiajie 2009-10-15)
	 */
    private int auditOpState;
    
    /**
	 * 是否继续保持在中台
	 */
    private boolean isStayInMid;
    
    /**
	 * 床型
	 * 
	 * @see BedType
	 */
    private int bedType;
    
    /**
	 * 子房型名称
	 */
    private String childRoomTypeName;
    
    /**
	 * 酒店星级
	 */
    private float hotelStar;
    
    /**
	 * 城市
	 */
    private String city;
    
    /**
	 * 订单提交到中台的时间
	 */
    private Date toMidTime;
    
    /**
	 * 对于面付转预付单，当给酒店发传真时，该字段的值决定
	 * 显示底价还是销售价
	 */
    private boolean showBasePrice = true;
    
    /**
	 * 配额是否可退
	 */
    private boolean quotaCanReturn = true;
    
    /**
	 * 订单的要扣配额类型(包房配额2，普通配额1)
	 */
    private String quotaTypeOld = "";
    
    /**
	 * 入住总人数
	 */
    private String num;
    
    /**
	 * 订单前台最后修改时间
	 */
    private Date modifiedFrontTime;
    
    /**
	 * 订单中台最后修改时间
	 */
    private Date modifiedMidTime;
    
    /**
	 * 客人要求预付时限
	 */
    private Date guestPrepayLimitDate;
    
    /**
	 * 新增noshow原因代码 add by chenkeming@2008.12.04 v2.4.1 
	 */
    private int noshowCode;
    
    /**
	 * 生成日审记录标记 
	 */
    private int genAudit;
    
    /**
     * 担保提示信息
     */
    private String orderHint;
    
    /**
	 * 和OrOrderItem关联
	 */
    private List<OrOrderItem> orderItems;
        
    /**
	 * 和OrPayment关联
	 */
    private List<OrPayment> paymentList;
    
    /**
	 * 和OrRefund关联
	 */
    private List<OrRefund> refundList;
    
    /**
	 * 和OrCreditCard关联
	 */
    private List<OrCreditCard> creditCardList;
    
    /**
	 * 和OrHandleLog关联
	 */
    private List<OrHandleLog> logList;
    
    /**
	 * 和OrFellowInfo关联
	 */
    private List<OrFellowInfo> fellowList;
    
    /**
	 * 和OrOrderFax关联
	 */
    private List<OrOrderFax> faxList;
    
    /**
	 * 和OrMemberConfirm关联
	 */
    private List<OrMemberConfirm> memberConfirmList;
    
    /**
	 * 和OrCreditCardTemp关联
	 */
    private List<OrCreditCardTemp> cardTempList;
    
    /**
	 * 和OrPriceDetail关联
	 */
    private List<OrPriceDetail> priceList;
    
    /**
	 * 和OrPreSale关联
	 * @author chenkeming Feb 18, 2009 4:34:22 PM
	 */
    private List<OrPreSale> preSales;
    
    /**
	 * 和OrTaxCharge关联
	 * @author chenkeming Feb 18, 2009 4:34:22 PM
	 */
    private List<OrTaxCharge> taxCharges;
    
    /**
	 * 和OrOrderMoney关联
	 */
    private List<OrOrderMoney> moneyList;
    /**
	 * 和OrChannelNo关联
	 */
    private List<OrChannelNo> channelList;
    
    /**
	 * 配送ID <fk> 和OrFulfillment关联
	 */
    private OrFulfillment fulfill;
    
    /**
	 * 备注ID <fk> 和OrRemark关联
	 */
    private OrRemark remark;
     
    /**
	 * 预订规则ID <fk> 和OrReservation关联
	 */
    private OrReservation reservation;
    
    /**
	 * 退款信息里的备注
	 */
    private String refundNote;
    /**
	 * 渠道号
	 */
    private String agentid;
        
    /**
	 * 即时确认 1:是,0:否
	 */
    private boolean instantConfirm = false;
    
    /**
	 * 订单的有效时间，供报表等用途
	 */
    private Date validTime;
    
    /**
	 * 面付单:日审完成时间，预付单:付款成功时间，供报表等用途
	 */
    private Date completeTime;
    
    /**
	 * 合作方定单编号
	 */
    private String orderCdForChannel;

    /**
	 * 中台订单转房控疑难的时间
	 * add by chenjiajie@2008.12.26 v2.4.2 新增中台订单转房控疑难的时间
	 */
    private Date difficultyTime;
    
    /**
	 * 会员的联名商家项目号 如:0000000099
	 * add by chenjiajie@2009.02.19 v2.7.1
	 */
    private String memberAliasId;
    
    /**
	 * TMC订单是否积分
	 * add by haibo.li
	 */
    private String tmcOrderPoints;
    /**
	 * 会员的VIP级别
	 * add by chenjiajie@2009.02.19 v2.7.1
	 */
    private int memberVipLevel;
    
    /**
     * 是否是虚假订单
     * add by haibo.li 2009-9-15
     */
    private long falseOrderFalg ;
    /**
	 * 订单使用的代金券记录 
	 * hotel 2.9.3 add by chenjiajie 2009-08-24
	 */
    private List<OrCouponRecords> couponRecords;
    
    /**
     * 订单使用的积分记录 add by diandian.hou 2011-11-21
     */
	private List<OrPointRecords> pointRecordsList;
    
    /**
     * 日审备注
     */
    private String auditRemark;
    /**
     * 日审完成状态
     */
    private int payToHotelOk;
    
    /**
     * 标志是否优惠立减订单 映射表的AUDITOPSTATE字段 V2.9.3.1 add by chenjiajie 2009-10-15
     * 1:是优惠立减 0:不是,default为0
     */
    private int favourableFlag;
    
    /**
     * 优惠立减的总金额 映射表的PENALTY字段 V2.9.3.1 add by chenjiajie 2009-10-15
     */
    private float favourableAmount;	
	
    
    /**
     * 映射数据库paymentRate字段，原字段已无用 现在用于记录订单的实际币种 香港组紧急需求 add by chenjiajie 2009-11-23
     */
    private String actualPayCurrency;
    
    
    /**
	 * 在HAGTB2B 中 用来表示 佣金类型  add by shengwei.zuo 2010-1-9 
	 */
    private int  footWay;
    
    /**
	 * 在HAGTB2B 中 用来表示 佣金类型对应的值   add by shengwei.zuo 2010-1-9 
	 */
    private String footFee;
    
	/**
     * 返回现金 映射表的confirmtotal 字段 TMC-V2.0 add by shengwei.zuo 2010-3-17
     */
    private double cashBackTotal;
	
    /**
	 * 代理打电话到cc，用来存放代理操作人id   add by zhijie.gu 2010-3-10 
	 */
    private String b2bOperaterId;
    
    private String orderCDConvert;
    
    /**
	 * 供应商别名add by xieyanhui 2011-09-20 
	 */
    private String supplierAlias;

    /**
	 * 供应商IDadd by xieyanhui 2011-09-20 
	 */
    private Long supplierID;
    
    /**
	 * 订单辅助类（魅影项目用到）
	 */
    private OrOrderRMP orOrderRMP;
    
    /**
	 * 是否是魅影订单
	 */
    private Boolean rmpOrder;
    
    /**
     * 订单附近信息 add by alfred
     */
    private List<OrOrderExtInfo> OrOrderExtInfoList;
    

	public String getB2bOperaterId() {
		return b2bOperaterId;
	}
	/**
     * 出行性质 映射表的refundDoneForFinance 字段 TMC-V2.0 add by shengwei.zuo 2010-3-17
     *  1 :因公 2：私人
     */
    private int tripNature;	
    
    /**
     * 担保转支付时候,支付的金额,扣款金额
     * add by haibo.li 2010-6-28
     */
    private double payCharge;
    
	public void setB2bOperaterId(String operaterId) {
		b2bOperaterId = operaterId;
	}

	public String getRefundNote() {
        return refundNote;
    }

    public void setRefundNote(String refundNote) {
        this.refundNote = refundNote;
    }

    public List<OrPriceDetail> getPriceList() {
    	if(null == priceList) {
    		priceList = new ArrayList<OrPriceDetail>();
    	}
        return priceList;
    }

    public void setPriceList(List<OrPriceDetail> priceList) {
        this.priceList = priceList;
    }

    /** getter and setter begin */

    public boolean isStayInMid() {
        return this.isStayInMid;
        /*return (orderState >= OrderState.SUBMIT_TO_MID &&
				!(hotelConfirmTel && hotelConfirmFax && sendedMemberFax
						&& quotaOk));*/
    }    
    /**
	 * 是否3G订单
	 * @return
	 */
    public boolean isFrom3G() {        
        return null != source && source.equals(OrderSource.FROM_MTL);
    }
    public boolean getIsStayInMid() {
        return this.isStayInMid;
    }
    
    public void setIsStayInMid(boolean isStayInMid) {
        this.isStayInMid = isStayInMid;
    }    
    
    public void setStayInMid(boolean isStayInMid) {
        this.isStayInMid = isStayInMid;
    }    
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public Date getGuestPrepayLimitDate() {
        return guestPrepayLimitDate;
    }

    public void setGuestPrepayLimitDate(Date guestPrepayLimitDate) {
        this.guestPrepayLimitDate = guestPrepayLimitDate;
    }

    public String getQuotaTypeOld() {
        return quotaTypeOld;
    }

    public void setQuotaTypeOld(String quotaTypeOld) {
        this.quotaTypeOld = quotaTypeOld;
    }

    public List<OrCreditCardTemp> getCardTempList() {
    	if(null == cardTempList) {
    		cardTempList = new ArrayList<OrCreditCardTemp>();
    	}
        return cardTempList;
    }

    public void setCardTempList(List<OrCreditCardTemp> cardTempList) {
        this.cardTempList = cardTempList;
    }

    public boolean isShowBasePrice() {
        return showBasePrice;
    }

    public void setShowBasePrice(boolean showBasePrice) {
        this.showBasePrice = showBasePrice;
    }

    public Date getToMidTime() {
        return toMidTime;
    }

    public void setToMidTime(Date toMidTime) {
        this.toMidTime = toMidTime;
    }

    public float getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(float hotelStar) {
        this.hotelStar = hotelStar;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public boolean isQuotaCanReturn() {
        return quotaCanReturn;
    }

    public void setQuotaCanReturn(boolean quotaCanReturn) {
        this.quotaCanReturn = quotaCanReturn;
    }

    public Date getAllowCancelTime() {
        return allowCancelTime;
    }

    public void setAllowCancelTime(Date allowCancelTime) {
        this.allowCancelTime = allowCancelTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalTraffic() {
        return arrivalTraffic;
    }

    public void setArrivalTraffic(String arrivalTraffic) {
        this.arrivalTraffic = arrivalTraffic;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getAssignToName() {
        return assignToName;
    }

    public void setAssignToName(String assignToName) {
        this.assignToName = assignToName;
    }

    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public double getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(double balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    public int getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(int cancelReason) {
        this.cancelReason = cancelReason;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
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

    public Long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(Long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public double getConfirmTotal() {
        return confirmTotal;
    }

    public void setConfirmTotal(double confirmTotal) {
        this.confirmTotal = confirmTotal;
    }

    public int getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(int confirmType) {
        this.confirmType = confirmType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreditCardIdsSelect() {
        return creditCardIdsSelect;
    }

    public void setCreditCardIdsSelect(String creditCardIdsSelect) {
        this.creditCardIdsSelect = creditCardIdsSelect;
    }

    public List<OrCreditCard> getCreditCardList() {
    	if(null == creditCardList) {
    		creditCardList = new ArrayList<OrCreditCard>();
    	}
        return creditCardList;
    }

    public void setCreditCardList(List<OrCreditCard> creditCardList) {
        this.creditCardList = creditCardList;
    }

    public boolean isCustomerConfirm() {
        return customerConfirm;
    }

    public void setCustomerConfirm(boolean customerConfirm) {
        this.customerConfirm = customerConfirm;
    }

    public String getCustomerFax() {
        return customerFax;
    }

    public void setCustomerFax(String customerFax) {
        this.customerFax = customerFax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(int emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public List<OrOrderFax> getFaxList() {
    	if(null == faxList) {
    		faxList = new ArrayList<OrOrderFax>();
    	}
        return faxList;
    }

    public void setFaxList(List<OrOrderFax> faxList) {
        this.faxList = faxList;
    }

    public List<OrFellowInfo> getFellowList() {
    	if(null == fellowList) {
    		fellowList = new ArrayList<OrFellowInfo>();
    	}
        return fellowList;
    }

    public void setFellowList(List<OrFellowInfo> fellowList) {
        this.fellowList = fellowList;
    }

    public String getFellowNames() {
        return fellowNames;
    }

    public void setFellowNames(String fellowNames) {
        if (fellowNames != null){
        	this.fellowNames = fellowNames.replace("<", "");
        }else{
        	this.fellowNames = "";
        }
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public boolean isFrontCancel() {
        return frontCancel;
    }

    public void setFrontCancel(boolean frontCancel) {
        this.frontCancel = frontCancel;
    }

    public OrFulfillment getFulfill() {
        return fulfill;
    }

    public void setFulfill(OrFulfillment fulfill) {
        this.fulfill = fulfill;
    }

    public String getFulfillmentCD() {
        return fulfillmentCD;
    }

    public void setFulfillmentCD(String fulfillmentCD) {
        this.fulfillmentCD = fulfillmentCD;
    }

    public boolean isHasRefund() {
        return hasRefund;
    }

    public void setHasRefund(boolean hasRefund) {
        this.hasRefund = hasRefund;
    }


    public boolean isHotelConfirm() {
        return hotelConfirm;
    }

    public void setHotelConfirm(boolean hotelConfirm) {
        this.hotelConfirm = hotelConfirm;
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

    public int getHraOrderType() {
        return hraOrderType;
    }

    public void setHraOrderType(int hraOrderType) {
        this.hraOrderType = hraOrderType;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public boolean isIllusive() {
        return illusive;
    }

    public boolean getIsIllusive() {
        return illusive;
    }
    
    public void setIllusive(boolean illusive) {
        this.illusive = illusive;
    }
    
    public void setIsIllusive(boolean illusive) {
        this.illusive = illusive;
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

    public boolean isManualOrder() {
        return isManualOrder;
    }
    
    public boolean getIsManualOrder() {
        return isManualOrder;
    }

    public void setManualOrder(boolean isManualOrder) {
        this.isManualOrder = isManualOrder;
    }
    
    public void setIsManualOrder(boolean isManualOrder) {
        this.isManualOrder = isManualOrder;
    }

    public String getLatestArrivalTime() {
        return latestArrivalTime;
    }

    public void setLatestArrivalTime(String latestArrivalTime) {
        this.latestArrivalTime = latestArrivalTime;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        if (linkMan != null){
        	this.linkMan = linkMan.replace("<", "");
        }else{
        	this.linkMan = "";
        }
    }

    public List<OrHandleLog> getLogList() {
    	if(null == logList) {
    		logList = new ArrayList<OrHandleLog>();
    	}
        return logList;
    }

    public void setLogList(List<OrHandleLog> logList) {
        this.logList = logList;
    }

    public List<OrMemberConfirm> getMemberConfirmList() {
    	if(null == memberConfirmList) {
    		memberConfirmList = new ArrayList<OrMemberConfirm>();
    	}
        return memberConfirmList;
    }

    public void setMemberConfirmList(List<OrMemberConfirm> memberConfirmList) {
        this.memberConfirmList = memberConfirmList;
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


    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getModifierRole() {
        return modifierRole;
    }

    public void setModifierRole(String modifierRole) {
        this.modifierRole = modifierRole;
    }

    public boolean isNeedRefund() {
        return needRefund;
    }

    public void setNeedRefund(boolean needRefund) {
        this.needRefund = needRefund;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public List<OrOrderItem> getOrderItems() {
    	if(null == orderItems) {
    		orderItems = new ArrayList<OrOrderItem>();
    	}
        return orderItems;
    }

    public void setOrderItems(List<OrOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getOriginCD() {
        return originCD;
    }

    public void setOriginCD(String originCD) {
        this.originCD = originCD;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public List<OrPayment> getPaymentList() {
    	if(null == paymentList) {
    		paymentList = new ArrayList<OrPayment>();
    	}
        return paymentList;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public void setPaymentList(List<OrPayment> paymentList) {
        this.paymentList = paymentList;
    }

    public String getPaymentRate() {
        return paymentRate;
    }

    public void setPaymentRate(String paymentRate) {
        this.paymentRate = paymentRate;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public boolean isPayToHotel() {
        return payToHotel;
    }

    public void setPayToHotel(boolean payToHotel) {
        this.payToHotel = payToHotel;
    }

    public boolean isPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(boolean payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public Date getPrepayLimitDate() {
        return prepayLimitDate;
    }

    public void setPrepayLimitDate(Date prepayLimitDate) {
        this.prepayLimitDate = prepayLimitDate;
    }

    public double getPrepayTotalRmb() {
        return prepayTotalRmb;
    }

    public void setPrepayTotalRmb(double prepayTotalRmb) {
        this.prepayTotalRmb = prepayTotalRmb;
    }

    public int getPrepayType() {
        return prepayType;
    }

    public void setPrepayType(int prepayType) {
        this.prepayType = prepayType;
    }

    public boolean isQuotaOk() {
        return quotaOk;
    }

    public void setQuotaOk(boolean quotaOk) {
        this.quotaOk = quotaOk;
    }

    public double getRateId() {
        return rateId;
    }

    public void setRateId(double rateId) {
        this.rateId = rateId;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public boolean isRefundBillAuditPass() {
        return refundBillAuditPass;
    }

    public void setRefundBillAuditPass(boolean refundBillAuditPass) {
        this.refundBillAuditPass = refundBillAuditPass;
    }

    public boolean isRefundDoneForFinance() {
        return refundDoneForFinance;
    }

    public void setRefundDoneForFinance(boolean refundDoneForFinance) {
        this.refundDoneForFinance = refundDoneForFinance;
    }

    public Date getRefundLimitTime() {
        return refundLimitTime;
    }

    public void setRefundLimitTime(Date refundLimitTime) {
        this.refundLimitTime = refundLimitTime;
    }

    public List<OrRefund> getRefundList() {
    	if(null == refundList) {
    		refundList = new ArrayList<OrRefund>();
    	}
        return refundList;
    }

    public void setRefundList(List<OrRefund> refundList) {
        this.refundList = refundList;
    }

    public String getRefundMessage() {
        return refundMessage;
    }

    public void setRefundMessage(String refundMessage) {
        this.refundMessage = refundMessage;
    }

    public int getRefundStateForHotel() {
        return refundStateForHotel;
    }

    public void setRefundStateForHotel(int refundStateForHotel) {
        this.refundStateForHotel = refundStateForHotel;
    }

    public OrRemark getRemark() {
    	if(null == remark) {
    		remark = new OrRemark();
    	}    	
        return remark;
    }

    public void setRemark(OrRemark remark) {
        this.remark = remark;
    }

    public OrReservation getReservation() {
        return reservation;
    }

    public void setReservation(OrReservation reservation) {
        this.reservation = reservation;
    }



    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }
	public int getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(int roomQuantity) {
        this.roomQuantity = roomQuantity;
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

    public boolean isSendedHotelFax() {
        return sendedHotelFax;
    }

    public void setSendedHotelFax(boolean sendedHotelFax) {
        this.sendedHotelFax = sendedHotelFax;
    }

    public String getSequence114() {
        return sequence114;
    }

    public void setSequence114(String sequence114) {
        this.sequence114 = sequence114;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSumRmb() {
        return sumRmb;
    }

    public void setSumRmb(double sumRmb) {
        this.sumRmb = sumRmb;
    }

    public int getSuretyCurrency() {
        return suretyCurrency;
    }

    public void setSuretyCurrency(int suretyCurrency) {
        this.suretyCurrency = suretyCurrency;
    }

    public double getSuretyPrice() {
        return suretyPrice;
    }

    public void setSuretyPrice(double suretyPrice) {
        this.suretyPrice = suretyPrice;
    }

    public int getSuretyState() {
        return suretyState;
    }

    public void setSuretyState(int suretyState) {
        this.suretyState = suretyState;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
    	if (telephone != null){
    		this.telephone = telephone.replace("<", "");
    	}else{
    		this.telephone = "";
    	}
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }
    
    public String getOrderCDHotel() {
        return orderCDHotel;
    }

    public void setOrderCDHotel(String orderCDHotel) {
        this.orderCDHotel = orderCDHotel;
    }
    
    public boolean isHasPrepayed() {
        return hasPrepayed;
    }

    public void setHasPrepayed(boolean hasPrepayed) {
        this.hasPrepayed = hasPrepayed;
    }
    
    public boolean isHotelConfirmFax() {
        return hotelConfirmFax;
    }

    public void setHotelConfirmFax(boolean hotelConfirmFax) {
        this.hotelConfirmFax = hotelConfirmFax;
    }

    public boolean isHotelConfirmTel() {
        return hotelConfirmTel;
    }

    public void setHotelConfirmTel(boolean hotelConfirmTel) {
        this.hotelConfirmTel = hotelConfirmTel;
    }

    public boolean isHotelConfirmFaxReturn() {
        return hotelConfirmFaxReturn;
    }

    public void setHotelConfirmFaxReturn(boolean hotelConfirmFaxReturn) {
        this.hotelConfirmFaxReturn = hotelConfirmFaxReturn;
    }

    public boolean isSendedMemberFax() {
        return sendedMemberFax;
    }

    public void setSendedMemberFax(boolean sendedMemberFax) {
        this.sendedMemberFax = sendedMemberFax;
    }
    
    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }
    
    public boolean isInNextTeam() {
        return inNextTeam;
    }

    public void setInNextTeam(boolean inNextTeam) {
        this.inNextTeam = inNextTeam;
    }
    
    public String getMemberCd() {
        return memberCd;
    }

    public void setMemberCd(String memberCd) {
        this.memberCd = memberCd;
    }
    
    public int getAuditOpState() {
        return auditOpState;
    }

    public void setAuditOpState(int auditOpState) {
        this.auditOpState = auditOpState;
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
    
    public int getBedType() {
        return bedType;
    }

    public void setBedType(int bedType) {
        this.bedType = bedType;
    }
    
    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }

    public void setChildRoomTypeName(String childRoomTypeName) {
        this.childRoomTypeName = childRoomTypeName;
    }
    
    public String getGuestCancelMessage() {
        return guestCancelMessage;
    }

    public void setGuestCancelMessage(String guestCancelMessage) {
        this.guestCancelMessage = guestCancelMessage;
    }
    
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
    
    public Date getModifiedFrontTime() {
        return modifiedFrontTime;
    }

    public void setModifiedFrontTime(Date modifiedFrontTime) {
        this.modifiedFrontTime = modifiedFrontTime;
    }

    public Date getModifiedMidTime() {
        return modifiedMidTime;
    }

    public void setModifiedMidTime(Date modifiedMidTime) {
        this.modifiedMidTime = modifiedMidTime;
    }
    
    public String getAppendMobile() {
        return appendMobile;
    }

    public void setAppendMobile(String appendMobile) {
        this.appendMobile = appendMobile;
    }

    public boolean isInstantConfirm() {
        return instantConfirm;
    }

    public void setInstantConfirm(boolean instantConfirm) {
        this.instantConfirm = instantConfirm;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }
    
    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }
    
    public int getNoshowCode() {
        return noshowCode;
    }

    public void setNoshowCode(int noshowCode) {
        this.noshowCode = noshowCode;
    }
    
    /** end of getter and setter */
    
    

    /**
     * 是否预付订单
     * @return
     */
    public boolean isPrepayOrder() {
        return PayMethod.PRE_PAY.equals(this.payMethod);
    }    
    
    /**
	 * 是否需要配送
	 * @return
	 */
    public boolean isNeedFulfill(){
        if (!isPrepayOrder())
            return false;        
        
        // 如果包含现金和POS刷卡方式，则需要配送	
        return OrderUtil.isNeedFulfill(getPaymentList());        
    }
    
    /**
	 * 判断订单入住日期是否今天
	 * @return
	 */
    public boolean isTodayCheckin() {
        return 0 == DateUtil.compare(this.checkinDate,new Date());
    }
    
    /**
	 * 是否新建订单
	 * @return
	 */
    public boolean isNewOrder() {
        return null == this.getID() || 0 == this.getID().longValue() 
        || orderState == OrderState.NOT_SUBMIT;
    }

    /**
	 * 综合考虑预付付款类型和面付是否担保 决定是否在页面显示信用卡信息
	 * @return
	 */
    public boolean isNeedCreditCard() {
        if(isCreditAssured) {
            return true;
        }        
        if(isPrepayOrder()) {
            return OrderUtil.isNeedCreditCard(getPaymentList(), getRefundList()); 
        }
        return false;
    }
    
    /**
	 * 是否取消单
	 * @return
	 */
    public boolean isCancel() {
        return orderState == OrderState.CANCEL;
    }
    
    /**
	 * 在提交新建或修改订单后的页面是否能显示"提交中台"按钮
	 * @return
	 */
    public boolean isCanSubmitToMid() {
        return orderState >= OrderState.HAS_SUBMIT && 
                orderState < OrderState.SUBMIT_TO_MID &&  
                !isStayInMid;
    }
    
    /**
	 * 是否芒果订单
	 * @return
	 */
    public boolean isMango() {
    	return type == OrderType.TYPE_MANGO || type == OrderType.TYPE_B2BAGENT || type == OrderType.TYPE_NORMANDY;
    }
    
    /**
	 * 需要处理退款信息
	 * @return
	 */
    public boolean isNeedHandleRefund() {
        return isPrepayOrder() && isHasPrepayed() && 
                isNeedRefund() && !hasRefund;        
    }        
    
    /**
	 * 是否包含积分的预付方式
	 * @return
	 */
    public boolean isIncludePtPrepay() {
        return OrderUtil.includePrepay(PrepayType.Points, getPaymentList());
    }        
    
    /**
	 * 是否能创建预授权工单
	 */
    public boolean canPreAuth() {
        return suretyState < GuaranteeState.PREAUTH;
    }

    /**
	 * 是否已创建预授权工单
	 */
    public boolean isHasCreatePreAuth() {
        return suretyState >= GuaranteeState.PREAUTH;
    }
    
    /**
	 * 是否网站订单
	 * @return
	 */
    public boolean isFromWeb() {        
        return null != source && (source.equals(OrderSource.FROM_WEB) || source.equals(OrderSource.FAN_TI_NET));
    }
    
    /**
	 * 是否B2B代理订单  add by shengwei.zuo  2010-1-19
	 * @return
	 */
    public boolean isFromB2B() {        
        return null != source && source.equals(OrderSource.FROM_B2B);
    }
    
    /**
	 * 是否允许修改基本信息
	 * @author chenkeming Feb 11, 2009 2:09:00 PM
	 * @return
	 */
    public boolean isCanEditBaseInfo() {
        return !isManualOrder && !isCancel() && 
                ((isPrepayOrder() && !OrderUtil.partlyPay(getPaymentList())) || 
                (!isPrepayOrder() && !OrderUtil.checkAuditFirstNight(this))); 
    }
    
    /**
	 * 订单 非直连酒店订单 或者 走传统渠道 
	 * @return
	 */
    public boolean isOriChannel() {
        return 0 == channel || channel > ChannelType.ORI_ADD;
    }
    
    /**
	 * 是否中旅订单
	 * @author chenkeming Mar 18, 2009 9:44:43 AM
	 * @return
	 */
    public boolean isCtsHK() {
        return channel == ChannelType.CHANNEL_CTS;
    }
    
    /**
     * 判断中旅芒果单的中旅单是否提交成功
     * @author chenkeming Apr 17, 2009 12:30:00 PM
     * @return
     */
    public boolean isCtsOK() {
        return OrderUtil.isCtsOK(this);
    }

    /**
	 * TMC 2.3
	 * @return
	 */
    
    /**
     * TMC-V2.0 公司会员CD   add by shengwei.zuo 2010-3-22
     */
    public String companyMemCd;
    
    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }
    
    public Date getDifficultyTime() {
        return difficultyTime;
    }

    public void setDifficultyTime(Date difficultyTime) {
        this.difficultyTime = difficultyTime;
    }

    public String getMemberAliasId() {
        return memberAliasId;
    }

    public void setMemberAliasId(String memberAliasId) {
        this.memberAliasId = memberAliasId;
    }

    public String getOrderCdForChannel() {
        return orderCdForChannel;
    }

    public void setOrderCdForChannel(String orderCdForChannel) {
        this.orderCdForChannel = orderCdForChannel;
    }

    public List<OrPreSale> getPreSales() {
    	if(null == preSales) {
    		preSales = new ArrayList<OrPreSale>();
    	}
        return preSales;
    }

    public void setPreSales(List<OrPreSale> preSales) {
        this.preSales = preSales;
    }

    public List<OrTaxCharge> getTaxCharges() {
    	if(null == taxCharges) {
    		taxCharges = new ArrayList<OrTaxCharge>();
    	}
        return taxCharges;
    }

    public void setTaxCharges(List<OrTaxCharge> taxCharges) {
        this.taxCharges = taxCharges;
    }

    public int getHisNo() {
        return hisNo;
    }

    public void setHisNo(int hisNo) {
        this.hisNo = hisNo;
    }

    public int getResumeNo() {
        return resumeNo;
    }

    public void setResumeNo(int resumeNo) {
        this.resumeNo = resumeNo;
    }
    public int getMemberVipLevel() {
        return memberVipLevel;
    }

    public void setMemberVipLevel(int memberVipLevel) {
        this.memberVipLevel = memberVipLevel;
    }

    public List<OrOrderMoney> getMoneyList() {
    	if(null == moneyList) {
    		moneyList = new ArrayList<OrOrderMoney>();
    	}
        return moneyList;
    }

    public void setMoneyList(List<OrOrderMoney> moneyList) {
        this.moneyList = moneyList;
    }

    public String getTmcOrderPoints() {
        return tmcOrderPoints;
    }

    public void setTmcOrderPoints(String tmcOrderPoints) {
        this.tmcOrderPoints = tmcOrderPoints;
    }

    public List<OrChannelNo> getChannelList() {
    	if(null == channelList) {
    		channelList = new ArrayList<OrChannelNo>();
    	}
        return channelList;
    }

    public void setChannelList(List<OrChannelNo> channelList) {
        this.channelList = channelList;
    }

    public String getContractlog() {
        return contractlog;
    }

    public void setContractlog(String contractlog) {
        this.contractlog = contractlog;
    }
    
    /**
	 * v2.8 判断是否可能需要担保
	 * @author chenkeming Jun 15, 2009 6:15:51 PM
	 * @return
	 */
    public boolean isCanAssure() {
        return OrderUtil.isCanAssure(this);
    }

    public List<OrCouponRecords> getCouponRecords() {
    	if(null == couponRecords) {
    		couponRecords = new ArrayList<OrCouponRecords>();
    	}
        return couponRecords;
    }

    public void setCouponRecords(List<OrCouponRecords> couponRecords) {
        this.couponRecords = couponRecords;
    }
    
    public void addCouponRecords(OrCouponRecords couponRecords) {
    	getCouponRecords().add(couponRecords);        
        couponRecords.setOrder(this);
    }
    
    /**
	 * 是否包含代金券的预付方式
	 * hotel2.9.3 代金券对接 add by chenjiajie 2009-08-31
	 * @return
	 */
    public boolean isIncludeCouponPrepay() {
        return OrderUtil.includePrepay(PrepayType.Coupon, getPaymentList());
    }

	public long getFalseOrderFalg() {
		return falseOrderFalg;
	}

	public void setFalseOrderFalg(long falseOrderFalg) {
		this.falseOrderFalg = falseOrderFalg;
	}

	public int getGenAudit() {
		return genAudit;
	}

	public void setGenAudit(int genAudit) {
		this.genAudit = genAudit;
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

	public int getPayToHotelOk() {
		return payToHotelOk;
	}

	public void setPayToHotelOk(int payToHotelOk) {
		this.payToHotelOk = payToHotelOk;
	}

	public int getFavourableFlag() {
		return favourableFlag;
	}

	public void setFavourableFlag(int favourableFlag) {
		this.favourableFlag = favourableFlag;
	}

	public float getFavourableAmount() {
		return favourableAmount;
	}

	public void setFavourableAmount(float favourableAmount) {
		this.favourableAmount = favourableAmount;
	}
	
	/**
	 * 计算RMB的立减总金额
	 * @return
	 */
	public double getRmbFavourableAmount(){
		return Math.floor(this.favourableAmount / this.roomQuantity * this.rateId) * this.roomQuantity;
	}

	public String getOrderHint() {
		return orderHint;
	}

	public void setOrderHint(String orderHint) {
		this.orderHint = orderHint;
	}
	    public String getActualPayCurrency() {
        if(StringUtil.isValidStr(actualPayCurrency)){
            return actualPayCurrency;
        }else{
            return CurrencyBean.RMB;
        }
    }

    public void setActualPayCurrency(String actualPayCurrency) {
        this.actualPayCurrency = actualPayCurrency;
    }
	public int getFootWay() {
		return footWay;
	}

	public void setFootWay(int footWay) {
		this.footWay = footWay;
	}

	public String getFootFee() {
		return footFee;
	}

	public void setFootFee(String footFee) {
		this.footFee = footFee;
	}

	public double getCashBackTotal() {
		return cashBackTotal;
	}

	public void setCashBackTotal(double cashBackTotal) {
		this.cashBackTotal = cashBackTotal;
	}

	public int getTripNature() {
		return tripNature;
	}

	public void setTripNature(int tripNature) {
		this.tripNature = tripNature;
	}

	public String getCompanyMemCd() {
		return companyMemCd;
	}

	public void setCompanyMemCd(String companyMemCd) {
		this.companyMemCd = companyMemCd;
	}

	public double getPayCharge() {
		return payCharge;
	}

	public void setPayCharge(double payCharge) {
		if(!Double.isNaN(payCharge) && payCharge >= 0){
			this.payCharge = payCharge;
		}else{
			this.payCharge = 0;
		}
	}

	public String getOrderCDConvert() {
		return orderCDConvert;
	}

	public void setOrderCDConvert(String orderCDConvert) {
		this.orderCDConvert = orderCDConvert;
	}
	
	/**
	 * 
	 * 是否交行全卡商旅等渠道下的订单 add by chenkeming
	 * 
	 */
	public boolean isCooperateOrder() {
		// agentid是交行等渠道号的 /*或者 非网站的并且会员合作项目号是交行等渠道的*/ 订单
		return (CooperateChannel.JIAOHANG_CHANNEL_CODE.equals(agentid)/* || (!OrderSource.FROM_WEB
				.equals(source) && MemberAliasConstants.JIAOHANG
				.equals(memberAliasId))*/);
	}

	public String getSupplierAlias() {
		return supplierAlias;
	}

	public void setSupplierAlias(String supplierAlias) {
		this.supplierAlias = supplierAlias;
	}

	public Long getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(Long supplierID) {
		this.supplierID = supplierID;
	}

	public List<OrOrderExtInfo> getOrOrderExtInfoList() {
		if(OrOrderExtInfoList == null) {
			OrOrderExtInfoList = new ArrayList<OrOrderExtInfo>();
		}
		return OrOrderExtInfoList;
	}

	public void setOrOrderExtInfoList(List<OrOrderExtInfo> orOrderExtInfoList) {
		OrOrderExtInfoList = orOrderExtInfoList;
	}

	public List<OrPointRecords> getPointRecordsList() {
		if(pointRecordsList == null){
			return new ArrayList<OrPointRecords>();
		}
		return pointRecordsList;
	}
	
	public void addPointRecordsList(OrPointRecords orPointRecords) {
		getPointRecordsList().add(orPointRecords);        
		orPointRecords.setOrder(this);
    } 

	public void setPointRecordsList(List<OrPointRecords> pointRecordsList) {
		this.pointRecordsList = pointRecordsList;
	}

	public OrOrderRMP getOrOrderRMP() {
		return orOrderRMP;
	}

	public void setOrOrderRMP(OrOrderRMP orOrderRMP) {
		this.orOrderRMP = orOrderRMP;
	}

	public Boolean getRmpOrder() {
		if(null==rmpOrder){
			return false;
		}
		return rmpOrder;
	}

	public void setRmpOrder(Boolean rmpOrder) {
		this.rmpOrder = rmpOrder;
	}

}
