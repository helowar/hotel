
package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.constant.HraOrderType;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.EmergencyLevel;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.util.HEntity;
import com.mangocity.util.hotel.constant.BedType;
import com.mangocity.util.hotel.constant.PayMethod;


/**
 * 
 *  历史订单
 *  
 *  @author chenkeming
 */
public class HOrder implements HEntity {

    private static final long serialVersionUID = -778399720443938341L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;
    
    /**
	 * 历史单创建人工号
	 */
    private String hisCreator;
    
    /**
	 * 历史单创建时间
	 */
    private Date hisCreateDate;
    
    /**
	 * 历史订单序号,zero based
	 */
    private int hisNo;
    
    /**
	 * v2.6 用来表示当为恢复单时,其恢复目标单的历史序号 
	 * @author chenkeming Mar 5, 2009 3:25:25 PM
	 */
    private int resumeNo = 0;
    
    /**
	 * 历史订单是否有效
	 */
    private boolean hisValid;
    
    /**
	 * 历史订单是否酒店确认
	 */
    private boolean hisHotelConfirm;
    
    /**
	 * 是否能恢复
	 * @author chenkeming Feb 13, 2009 3:17:01 PM
	 */
    private boolean hisCanResume = true;
        
    /**
	 * 订单ID
	 */
    private Long orderId;
    
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
	 * 订单类型 :(1-mango, 2-114)
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
	 * 
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
	 * 担保金额
	 */
    private double suretyPrice;
    
    /**
	 * 最晚取消时间
	 */
    private Date   allowCancelTime;    
    
    /**
	 * 退款金额
	 */
    private double refund;
    
    /**
	 * 配送单号
	 */
    private String fulfillmentCD;    
    
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
    private int confirmType;
    
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
    private double rateId;
    
    /**
	 * 转换为人民币后的支付总金额
	 */
    private double sumRmb;    
    
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
    private boolean showBasePrice;
    
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
	 * 和HOrderItem关联
	 */
    private List<HOrderItem> orderItemsH = new ArrayList<HOrderItem>();
        
    /**
	 * 和HPayment关联
	 */
    private List<HPayment> paymentListH = new ArrayList<HPayment>();
    
    /**
	 * 和HRefund关联
	 */
    private List<HRefund> refundListH = new ArrayList<HRefund>();
    
    /**
	 * 和HCreditCard关联
	 */
    // private List creditCardListH = new ArrayList();
    
    /**
	 * 和HHandleLog关联
	 */
    // private List<HHandleLog> logListH = new ArrayList<HHandleLog>();
    
    /**
	 * 和HFellowInfo关联
	 */
    private List<HFellowInfo> fellowListH = new ArrayList<HFellowInfo>();
    
    /**
	 * 和HOrderFax关联
	 */
    // private List<HOrderFax> faxListH = new ArrayList<HOrderFax>();
    
    /**
	 * 和HMemberConfirm关联
	 */
    // private List<HMemberConfirm> memberConfirmListH = new ArrayList<HMemberConfirm>();
    
    /**
	 * 和HCreditCardTemp关联
	 */
    private List<HCreditCardTemp> cardTempListH = new ArrayList<HCreditCardTemp>();
    
    /**
	 * 和HPriceDetail关联
	 */
    private List<HPriceDetail> priceListH = new ArrayList<HPriceDetail>();
    
    /**
	 * 和HPreSale关联
	 * @author chenkeming Feb 18, 2009 4:34:22 PM
	 */
    private List<HPreSale> preSalesH = new ArrayList<HPreSale>();
    
    /**
	 * 和HTaxCharge关联
	 * @author chenkeming Feb 18, 2009 4:34:22 PM
	 */
    private List<HTaxCharge> taxChargesH = new ArrayList<HTaxCharge>();
    
    /**
	 * 历史配送ID <fk> 和HFulfillment关联
	 */
    private HFulfillment fulfillH;
    
    /**
	 * 历史备注ID <fk> 和HRemark关联
	 */
    private HRemark remarkH;
    
    /**
	 * 预订规则ID <fk> 和HReservation关联
	 */
    private HReservation reservationH;         
    
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
	 * 中台订单转房控疑难的时间
	 * add by chenjiajie@2008.12.26 v2.4.2 新增中台订单转房控疑难的时间
	 */
    private Date difficultyTime;    
    
    /**
	 * 合作方定单编号
	 */
    private String orderCdForChannel;

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public Date getAllowCancelTime() {
        return allowCancelTime;
    }

    public void setAllowCancelTime(Date allowCancelTime) {
        this.allowCancelTime = allowCancelTime;
    }

    public String getAppendMobile() {
        return appendMobile;
    }

    public void setAppendMobile(String appendMobile) {
        this.appendMobile = appendMobile;
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

    public int getBedType() {
        return bedType;
    }

    public void setBedType(int bedType) {
        this.bedType = bedType;
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

    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }

    public void setChildRoomTypeName(String childRoomTypeName) {
        this.childRoomTypeName = childRoomTypeName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
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

    public Date getDifficultyTime() {
        return difficultyTime;
    }

    public void setDifficultyTime(Date difficultyTime) {
        this.difficultyTime = difficultyTime;
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

    public String getFellowNames() {
        return fellowNames;
    }

    public void setFellowNames(String fellowNames) {
        this.fellowNames = fellowNames;
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

    public String getFulfillmentCD() {
        return fulfillmentCD;
    }

    public void setFulfillmentCD(String fulfillmentCD) {
        this.fulfillmentCD = fulfillmentCD;
    }

    public String getGuestCancelMessage() {
        return guestCancelMessage;
    }

    public void setGuestCancelMessage(String guestCancelMessage) {
        this.guestCancelMessage = guestCancelMessage;
    }

    public Date getGuestPrepayLimitDate() {
        return guestPrepayLimitDate;
    }

    public void setGuestPrepayLimitDate(Date guestPrepayLimitDate) {
        this.guestPrepayLimitDate = guestPrepayLimitDate;
    }

    public boolean isHasPrepayed() {
        return hasPrepayed;
    }

    public void setHasPrepayed(boolean hasPrepayed) {
        this.hasPrepayed = hasPrepayed;
    }

    public boolean isHasRefund() {
        return hasRefund;
    }

    public void setHasRefund(boolean hasRefund) {
        this.hasRefund = hasRefund;
    }

    public String getHisCreator() {
        return hisCreator;
    }

    public void setHisCreator(String hisCreator) {
        this.hisCreator = hisCreator;
    }

    public boolean isHisHotelConfirm() {
        return hisHotelConfirm;
    }

    public void setHisHotelConfirm(boolean hisHotelConfirm) {
        this.hisHotelConfirm = hisHotelConfirm;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public int getHisNo() {
        return hisNo;
    }

    public void setHisNo(int hisNo) {
        this.hisNo = hisNo;
    }

    public boolean isHisValid() {
        return hisValid;
    }

    public void setHisValid(boolean hisValid) {
        this.hisValid = hisValid;
    }

    public boolean isHotelConfirm() {
        return hotelConfirm;
    }

    public void setHotelConfirm(boolean hotelConfirm) {
        this.hotelConfirm = hotelConfirm;
    }

    public boolean isHotelConfirmFax() {
        return hotelConfirmFax;
    }

    public void setHotelConfirmFax(boolean hotelConfirmFax) {
        this.hotelConfirmFax = hotelConfirmFax;
    }

    public boolean isHotelConfirmFaxReturn() {
        return hotelConfirmFaxReturn;
    }

    public void setHotelConfirmFaxReturn(boolean hotelConfirmFaxReturn) {
        this.hotelConfirmFaxReturn = hotelConfirmFaxReturn;
    }

    public boolean isHotelConfirmTel() {
        return hotelConfirmTel;
    }

    public void setHotelConfirmTel(boolean hotelConfirmTel) {
        this.hotelConfirmTel = hotelConfirmTel;
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

    public float getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(float hotelStar) {
        this.hotelStar = hotelStar;
    }

    public int getHraOrderType() {
        return hraOrderType;
    }

    public void setHraOrderType(int hraOrderType) {
        this.hraOrderType = hraOrderType;
    }

    public boolean isIllusive() {
        return illusive;
    }

    public void setIllusive(boolean illusive) {
        this.illusive = illusive;
    }

    public boolean isInNextTeam() {
        return inNextTeam;
    }

    public void setInNextTeam(boolean inNextTeam) {
        this.inNextTeam = inNextTeam;
    }

    public boolean isInstantConfirm() {
        return instantConfirm;
    }

    public void setInstantConfirm(boolean instantConfirm) {
        this.instantConfirm = instantConfirm;
    }

    public boolean isCreditAssured() {
        return isCreditAssured;
    }
    
    public boolean getIsCreditAssured() {
        return isCreditAssured;
    }
    
    public void setIsCreditAssured(boolean isCreditAssured) {
        this.isCreditAssured = isCreditAssured;
    }

    public boolean isManualOrder() {
        return isManualOrder;
    }

    public void setManualOrder(boolean isManualOrder) {
        this.isManualOrder = isManualOrder;
    }
    
    public boolean getIsManualOrder() {
        return isManualOrder;
    }
    
    public void setIsManualOrder(boolean isManualOrder) {
        this.isManualOrder = isManualOrder;
    }

    public boolean isStayInMid() {
        return isStayInMid;
    }
    
    public boolean getIsStayInMid() {
        return this.isStayInMid;
    }
    
    public void setIsStayInMid(boolean isStayInMid) {
        this.isStayInMid = isStayInMid;
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
        this.linkMan = linkMan;
    }

    public String getMemberCd() {
        return memberCd;
    }

    public void setMemberCd(String memberCd) {
        this.memberCd = memberCd;
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

    public boolean isNeedRefund() {
        return needRefund;
    }

    public void setNeedRefund(boolean needRefund) {
        this.needRefund = needRefund;
    }

    public int getNoshowCode() {
        return noshowCode;
    }

    public void setNoshowCode(int noshowCode) {
        this.noshowCode = noshowCode;
    }

    public String getNoshowReason() {
        return noshowReason;
    }

    public void setNoshowReason(String noshowReason) {
        this.noshowReason = noshowReason;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public String getOrderCdForChannel() {
        return orderCdForChannel;
    }

    public void setOrderCdForChannel(String orderCdForChannel) {
        this.orderCdForChannel = orderCdForChannel;
    }

    public String getOrderCDHotel() {
        return orderCDHotel;
    }

    public void setOrderCDHotel(String orderCDHotel) {
        this.orderCDHotel = orderCDHotel;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
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

    public boolean isQuotaCanReturn() {
        return quotaCanReturn;
    }

    public void setQuotaCanReturn(boolean quotaCanReturn) {
        this.quotaCanReturn = quotaCanReturn;
    }

    public boolean isQuotaOk() {
        return quotaOk;
    }

    public void setQuotaOk(boolean quotaOk) {
        this.quotaOk = quotaOk;
    }

    public String getQuotaTypeOld() {
        return quotaTypeOld;
    }

    public void setQuotaTypeOld(String quotaTypeOld) {
        this.quotaTypeOld = quotaTypeOld;
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

    public Date getRefundLimitTime() {
        return refundLimitTime;
    }

    public void setRefundLimitTime(Date refundLimitTime) {
        this.refundLimitTime = refundLimitTime;
    }

    public String getRefundMessage() {
        return refundMessage;
    }

    public void setRefundMessage(String refundMessage) {
        this.refundMessage = refundMessage;
    }

    public String getRefundNote() {
        return refundNote;
    }

    public void setRefundNote(String refundNote) {
        this.refundNote = refundNote;
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

    public boolean isSendedMemberFax() {
        return sendedMemberFax;
    }

    public void setSendedMemberFax(boolean sendedMemberFax) {
        this.sendedMemberFax = sendedMemberFax;
    }

    public String getSequence114() {
        return sequence114;
    }

    public void setSequence114(String sequence114) {
        this.sequence114 = sequence114;
    }

    public boolean isShowBasePrice() {
        return showBasePrice;
    }

    public void setShowBasePrice(boolean showBasePrice) {
        this.showBasePrice = showBasePrice;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
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
        this.telephone = telephone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getToMidTime() {
        return toMidTime;
    }

    public void setToMidTime(Date toMidTime) {
        this.toMidTime = toMidTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public List<HCreditCardTemp> getCardTempListH() {
        return cardTempListH;
    }

    public void setCardTempListH(List<HCreditCardTemp> cardTempListH) {
        this.cardTempListH = cardTempListH;
    }

    /*public List<HOrderFax> getFaxListH() {
		return faxListH;
	}

	public void setFaxListH(List<HOrderFax> faxListH) {
		this.faxListH = faxListH;
	}*/

    public List<HFellowInfo> getFellowListH() {
        return fellowListH;
    }

    public void setFellowListH(List<HFellowInfo> fellowListH) {
        this.fellowListH = fellowListH;
    }

    public HFulfillment getFulfillH() {
        return fulfillH;
    }

    public void setFulfillH(HFulfillment fulfillH) {
        this.fulfillH = fulfillH;
    }

    /*public List<HHandleLog> getLogListH() {
		return logListH;
	}

	public void setLogListH(List<HHandleLog> logListH) {
		this.logListH = logListH;
	}

	public List<HMemberConfirm> getMemberConfirmListH() {
		return memberConfirmListH;
	}

	public void setMemberConfirmListH(List<HMemberConfirm> memberConfirmListH) {
		this.memberConfirmListH = memberConfirmListH;
	}*/

    public List<HOrderItem> getOrderItemsH() {
        return orderItemsH;
    }

    public void setOrderItemsH(List<HOrderItem> orderItemsH) {
        this.orderItemsH = orderItemsH;
    }

    public List<HPayment> getPaymentListH() {
        return paymentListH;
    }

    public void setPaymentListH(List<HPayment> paymentListH) {
        this.paymentListH = paymentListH;
    }

    public List<HPriceDetail> getPriceListH() {
        return priceListH;
    }

    public void setPriceListH(List<HPriceDetail> priceListH) {
        this.priceListH = priceListH;
    }

    public List<HRefund> getRefundListH() {
        return refundListH;
    }

    public void setRefundListH(List<HRefund> refundListH) {
        this.refundListH = refundListH;
    }

    public HRemark getRemarkH() {
        return remarkH;
    }

    public void setRemarkH(HRemark remarkH) {
        this.remarkH = remarkH;
    }

    public Date getHisCreateDate() {
        return hisCreateDate;
    }

    public void setHisCreateDate(Date hisCreateDate) {
        this.hisCreateDate = hisCreateDate;
    }

    public boolean isHisCanResume() {
        return hisCanResume;
    }

    public void setHisCanResume(boolean hisCanResume) {
        this.hisCanResume = hisCanResume;
    }

    public boolean isPrepayOrder() {
        return PayMethod.PRE_PAY.equals(payMethod);
    }

    /**
	 * 是否已创建预授权工单
	 */
    public boolean isHasCreatePreAuth() {
        return suretyState >= GuaranteeState.PREAUTH;
    }    
    
    /**
	 * 是否取消单
	 * @return
	 */
    public boolean isCancel() {
        return orderState == OrderState.CANCEL;
    }
    
    public boolean isNeedCreditCard() {
        return true;
    }

    public HReservation getReservationH() {
        return reservationH;
    }

    public void setReservationH(HReservation reservationH) {
        this.reservationH = reservationH;
    }

    public List<HPreSale> getPreSalesH() {
        return preSalesH;
    }

    public void setPreSalesH(List<HPreSale> preSalesH) {
        this.preSalesH = preSalesH;
    }

    public List<HTaxCharge> getTaxChargesH() {
        return taxChargesH;
    }

    public void setTaxChargesH(List<HTaxCharge> taxChargesH) {
        this.taxChargesH = taxChargesH;
    }

    public int getResumeNo() {
        return resumeNo;
    }

    public void setResumeNo(int resumeNo) {
        this.resumeNo = resumeNo;
    }
}
