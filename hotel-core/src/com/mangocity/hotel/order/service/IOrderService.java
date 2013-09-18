package com.mangocity.hotel.order.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mangocity.hotel.base.persistence.HtlB2bComminfo;
import com.mangocity.hotel.base.persistence.HtlProjectCode;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.base.service.assistant.ReservationInfo;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.exception.MemberException;
import com.mangocity.hotel.order.manager.MidOrderTransfer;
import com.mangocity.hotel.order.persistence.OrFaxLog;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderOfSMS;
import com.mangocity.hotel.order.persistence.OrOrderStatistics;
import com.mangocity.hotel.order.persistence.OrToContractgroup;
import com.mangocity.hotel.order.persistence.OrUserPower;
import com.mangocity.hotel.order.persistence.view.OrOrderVO;
import com.mangocity.hotel.user.UserWrapper;

/**
 */
public interface IOrderService {
	
    public  OrOrder getOrder(Serializable orderID);
    
    public  OrOrder findOrOrder(Serializable orderID);
    
    public OrOrder getOrderItems(Serializable orderID);

    public void newOrder(OrOrder order);

    public void updateOrder(OrOrder order);

    /**
     * 定制获取order对象
     * 
     * @param fetchList
     *            - 需要抓取的一对多的list
     */
    public OrOrder getCustomOrderByOrderCD(String orderCD, String[] fetchList);

    /**
     * 定制获取order对象
     * 
     * @param fetchList
     *            - 需要抓取的一对多的list
     */
    public OrOrder getCustomOrder(long orderId, String[] fetchList);

    /**
     * 获取发邮件订单对象
     * 
     * @param orderID
     * @return
     */
    public OrOrder getCustomOrderForMail(Long orderID);

    /**
     * 预付收款
     * 
     * @param orderId
     * @param paymentId
     * @param userName
     * @param member
     * @param sb
     * @return
     * 
     */
    public OrOrder prepayCheck(long orderId, long paymentId, String userName, MemberDTO member,
        StringBuffer sb);

    /**
     * 预付退款
     * 
     * @param orderId
     * @param refundId
     * @return
     * @throws MemberException 
     * 
     */
    public OrOrder refundCheck(long orderId, long refundId, MemberDTO member) throws MemberException;

    /**
     * 预付单退款审批
     * 
     * @param orderId
     * @param roleUser
     * @return
     * 
     */
    public OrOrder refundBillAuditPass(long orderId, UserWrapper roleUser);

    /**
     * 酒店传真确认
     * 
     * @param orderId
     * @param faxItemId
     * @param isConfirm
     * @param confirmNo
     * @param confirmNotes
     * @return
     * 
     */
    public OrOrder confirmFaxItem(long orderId, long faxItemId, boolean isConfirm,
        String confirmNo, String confirmNotes, UserWrapper roleUser);

    /**
     * 呼出配额
     * 
     * @param orderId
     * @param roomItemId
     * @param basePrice
     * @param salePrice
     * @param roleUser
     * @return
     */
    public OrOrder callRoom(long orderId, long roomItemId, double basePrice, double salePrice,
        UserWrapper roleUser);

    /**
     * 变价
     * 
     * @param orderId
     * @param roomItemId
     * @param basePrice
     * @param salePrice
     * @param roleUser
     * @return
     */
    public OrOrder changeRoomPrice(long orderId, long roomItemId, double basePrice,
        double salePrice, UserWrapper roleUser);

    /**
     * 判断是否系统配额
     * 
     * @param order
     * @return
     */
    public boolean getIsSystemQuota(OrOrder order);

    /**
     * 计算订单总金额<br>
     * 
     * TODO: 考虑币种
     * 
     * @param order
     */
    public void calculateTotalAmount(OrOrder order);

    /**
     * 预订前验证
     * 
     * @param order
     * @return 是否验证成功的字符串信息, 空串表示成功
     */
    public String checkBeforeOrder(OrOrder order);

    /**
     * process订单
     * 
     * @param order
     * @return process订单时需显示的内容
     */
    // public ProcessOrderShowType processBookRoom(OrOrder order);
    /**
     * 获取订单配额, 如果获取成功则同时填充orderItems
     * 
     * @param order
     * @return true:获取配额成功, false:获取失败
     */
    public boolean deductOrderQuota(OrOrder order, int[] breakfast, int[] breakfastNum,
        String quotaType);

    /**
     * 手工单生成orderItems
     * 
     * @param order
     */
    public void getManualOrderQuota(OrOrder order, int breakfast, int breakfastNum);

    /**
     * 复制订单
     * 
     * @param newOrder
     * @param sourceOrderId
     * @param isCopyPayInfo
     *            是否要COPY支付和信用卡担保信息
     */
    public void copyOrder(OrOrder newOrder, String sourceOrderId, boolean isCopyPayInfo);

    /**
     * 将预订结果信息填充到订单当中
     * 
     * @param order
     * @param reserv
     */
    public void populateOrder(OrOrder order, ReservationInfo reserv);

    /**
     * 预订之前，查找?是否预订同一卡号，同一城市，相同的入住日期的酒店<br>
     * 把"重复"订单列出来
     * 
     * @param order
     * @return
     */
    public List findSimilarOrder(OrOrder order);

    /**
     * 插入或更新订单, 插入订单时自动设置createDate属性
     * 
     * @param order
     */
    public Serializable saveOrUpdate(OrOrder order);

    /**
     * 取消订单
     * 
     * @param order
     */
    public void cancelOrder(OrOrder order, int cancelReason, String cancelMessage,
        String guestCancelMessage, UserWrapper roleUser);

    /**
     * 取消订单
     */
    public void cancelOrderByOrderCD(String orderCD, int cancelReason, String cancelMessage,
        String guestCancelMessage, UserWrapper roleUser);
    
    /**
     * 处理异常订单（直联和中旅订单提交时出现异常，不直接撤单，而是保存订单，记录日志，然后由人工跟进订单）
     */
    public void handleExceptionOrder(OrOrder order,int exceptionType,String message,UserWrapper roleUser);

    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @return
     */
    public List hQueryOrderItemByFaxGroup(Long orderId);

    /**
     * 定时更新OrOrderStatistics统计信息表
     * 
     */
    public void updateOrderStatistics();
    /**
     * 根据会籍编号获取其订单统计信息
     * @param memberCd
     * @return
     */
    public OrOrderStatistics getOrderStatByMemberCd(String memberCd);

    /**
     * 根据OrOrderFax主键获取OrOrderFax
     * 
     * @param barCode
     * @return
     */
    public OrOrderFax getOrOrderFax(String barCode);

    /**
     * 获取酒店传真邦定ID
     */
    public Long getparmsId();

    /**
     * 插入回传日志
     */
    public boolean saveFaxLogAndOrderFax(OrOrderFax orOrderFax, OrFaxLog orFaxLog);

    public boolean saveFaxLog(OrFaxLog orFaxLog);

    /**
     * 更新订单的付款状态
     * 
     * @param orderCD
     * @param bHasPay
     *            true:已付款, false:未付款
     * @param confirmUser
     *            操作人
     * @return true:成功 false:失败
     */
    public boolean updateOrderPrepayStatus(final String orderCD, final boolean bHasPay,
        String confirmUser);

    /**
     * 给配送提供获取订单对象
     * 
     * @param orderCD
     * @return
     */
    public OrOrderVO getHotelOrderByOrderCD(final String orderCD);

    /**
     * 根据订单ID获取订单CD
     * 
     * @param sID
     * @return
     */
    public String getOrderCDByID(Serializable sID);

    public List queryHotelOrder114(Date subdate, Date subdate1);

    public List queryHotelDailyAult114(Date subdate, Date subdate1);

    /**
     * 获取系统内酒店币种信息
     * 
     * @param hotelId
     */
    public String getHotelSysCurrency(Serializable hotelId);

    /**
     * 获取114用户所在省份的114会员列表
     * 
     * @param sState
     *            114用户的省份(3字编码)
     * @return
     */
    public List get114Member(String sState);

    /**
     * 查询酒店订单的所有订单号绑定的回传
     * 
     * @param orderCD
     * @return
     */
    public List getOrderFaxLot(String orderCD);

    /**
     * 读FaxLog
     * 
     * @param objID
     *            FaxLog的主键ID
     * @param bCanChangeState
     *            能否改faxlog的state
     * @return
     */
    public OrFaxLog updateFaxLog(long objID, boolean bCanChangeState);

    /**
     * 得到guestCancelMessage
     * 
     * @param guestCancelMessage
     * @return guestCancelMessage
     */
    public String getGuestCancelMessage(int guestCancelMessage);

    /**
     * 修改OrderItem的入住人信息 v2.4.2 2008-12-31
     * 
     * @param order
     * @return
     */
    public boolean modifyOrderItem(OrOrder order);

    /**
     * 根据当天的日期,查询当天的订单(CC短信需求,自动发送入住提醒短信) add by shizhongwen 时间:Jan 13, 2009 3:04:34 PM
     * 
     * @param triggerdate
     * @return
     */
    public List<OrOrderOfSMS> getOrderforTrigger(String triggerdate);

    /**
     * 删除 add by shizhongwen 时间:Jan 14, 2009 12:01:23 PM
     * 
     * @param hql
     */
    public void delete(String hql);

    /**
     * 保存已发送短信的订单 add by shizhongwen 时间:Jan 15, 2009 1:46:06 PM
     * 
     * @param ordersms
     */
    public void saveOrUpdateOrderSMS(OrOrderOfSMS ordersms);

    /**
     * 查询该类(表)中的所有数据 add by shizhongwen 时间:Jan 15, 2009 3:00:47 PM
     * 
     * @param clazz
     * @return
     */
    public List listAll(Class clazz);

    /**
     * 更新合作方酒店订单编号到数据库
     * 
     * @param orderId
     * @param orderCD
     * @param hotelOrderForChannel
     *            合作方的定单编号
     * @return true:成功 false:失败
     */
    public boolean updateOrdercdForChannel(String orderId, String orderCD,
        String hotelOrderForChannel);

    /**
     * 根据酒店ID,起始日期获取税费设定信息
     * 
     * @return
     */
    public List getTaxCharges(Long contractId, Date beginDate, Date endDate);

    /**
     * 获取芒果网促销信息
     * 
     * @param
     * @return
     */
    // public List getPreOrderPresaleList(Long hotelID,Date beginDate,Date endDate);
    /**
     * 获取酒店促销信息
     * 
     * @param
     * @return
     */
    // public List getPreOrderSalePromos(Long hotelID,Date beginDate,Date endDate);

    /**
     * hotel2.6 网站：取出预订条款，并计算订单担保或者预付金额 add by zhineng.zhuang 2009-02-12
     * 
     * @param order
     */
    public ReservationAssist loadBookClauseForWeb(OrOrder order);

    /**
     * v2.6 获取酒店或芒果促销信息,并保存到订单
     * 
     * @author chenkeming Feb 18, 2009 3:26:48 PM
     * @param order
     */
    public void getPreSale(OrOrder order);

    /**
     * v2.6 获取房费另缴税信息
     * 
     * @author chenkeming Feb 18, 2009 4:47:23 PM
     * @param order
     */
    public void getTaxCharge(OrOrder order);

    /**
     * hotel2.6 保存权限
     * 
     * @author zhuangzhineng
     * @param orUserPower
     * @return
     */
    public String saveOrUpdateUserPower(OrUserPower orUserPower);

    /**
     * hotel2.6 删除权限
     * 
     * @author zhuangzhineng
     * @param id
     * @return
     */
    public String delUserPower(Long orUserPowerId);

    /**
     * hotel2.6 查询权限
     * 
     * @author zhuangzhineng
     * @param memberId
     * @return
     */
    public boolean queryUserPower(String memberCD);

    /**
     * Hotel 2.5.0 判断是否满足担保条款 add by guojun
     * 
     * @return
     */
    public boolean isAssureTerm();

    /**
     * Hotel 2.5.0 查询直联酒店订单是否可以修改 add by guojun
     */
    public String queryHotelDirectionModify(String hotelid);

    /**
     * Hotel2.5 重新下单时，返回原订单的Item详细情况
     * 
     * @param orderCd
     *            当前订单的cd
     * @param oriOrderCd
     *            旧订单的详细信息
     * @param add
     *            by guojun 2009-02-11 14:53
     */
    public OrOrder getOriOrOrder(String orderCd, String oriOrderCd);

    /**
     * Hotel2.5 重新下单时，通过orderCd查找旧订单的详细信息 add by guojun 2009-02-11 14:53
     */
    public OrOrder getOrOrderByOrderCd(String orderCd);

    /**
     * Hotel2.5 根据订单编号查找该订单所对应的酒店的渠道 原因(在hotelOrderEdit.jsp 页面中check
     * 从网站下的德比的订单的渠道也为0,故改为从htl_hotel_ext表中取channel ) add by shizhongwen 时间:Feb 24, 2009 2:08:15 PM
     * 
     * @param orderid
     * @return
     */
    public String getChannelByHotelId(String hotelid);

    /**
     * 订单前台转中台处理
     * 
     * @author chenkeming Mar 18, 2009 11:34:13 AM
     * @param orderId
     * @return
     */
    public boolean confirmToMid(long orderId, UserWrapper roleUser);

    /**
     * 更改支付状态
     */
    public void updateOrder(OrOrder order, int payType, boolean isSuccess, boolean isConfirm);

    /**
     * CC下中旅单,填充配额明细
     * 
     * @author chenkeming Apr 21, 2009 3:29:36 PM
     * @param order
     * @param breakfast
     * @param breakfastNum
     * @param params
     */
    public void getCtsOrderQuota(OrOrder order, int[] breakfast, int[] breakfastNum, Map params);

    /**
     * 中旅单支付成功后的处理
     * 
     * @author chenkeming Apr 21, 2009 7:14:04 PM
     * @param order
     * @param handleLog
     * @param bPoint
     * @return
     */
    public boolean getPayFinishCts(OrOrder order, OrHandleLog handleLog, boolean bPoint);

    /**
     * 网站在线支付同步支付状态
     * 
     * @author wuyun 2009-05-09
     * @return
     */
    public List<OrOrder> queryOrdersForSynchronous();

    /**
     * 网站在线支付同步支付状态时需要加载订单对象，由于订单采取了延迟加载，因此需要立即加载出关联的香港订单信息及支付信息
     * 
     * @author wuyun 2009-05-12
     * @return
     */
    public OrOrder queryOrderForSyschronous(Long orderId);

    /**
     * 转合约组
     * 
     * @author guojun 2009-05-19 16:30
     */
    public boolean toContractGoup(OrToContractgroup orToContractgroup);

    /**
     * 根据订单编号获取OrToContractgroup对象
     * 
     * @author chenkeming Jul 2, 2009 10:09:02 AM
     * @param orderId
     * @return
     */
    public OrToContractgroup getContractgroup(Long orderId);
    
    /**
     * 根据订单编号获取代理修改对象
     * @author lihaibo 2010-1-10
     */
    public List getB2bHagtOrder(String orderCD);
    
    
    /**
     * 代理查询芒果网应得多少佣金
     * add by haibo.li 2.9.3代理系统 2010-1-15
     * @return
     */
    public HtlB2bComminfo queryB2bComminfo();
    
    
    /**
     * 保存B2B订单的订单状态
     * 
     * @param orderCD
     * @param isCancelOrder
     */
    public void saveB2BOrderWithOrderState(String orderCD, boolean isCancelOrder);
    
    /**
     * 获取中台订单流转类
     * chenjuesu editAt 2010-3-30 下午03:54:54
     * @return
     */
    public MidOrderTransfer getMidOrderTransfer();
	

	    /**
     * 判断这个membercd是否为代理卡号
     * add by haibo.li 2010-3-23
     */
    public boolean isb2bMember(String memberCd);
    
    /**
     * 查找该订单现在所在的组别
     * @param id
     * @return
     */
	public Long getOrderGroup(Long id);
    
    
    
    
    
    /**
     * 增加订单操作日至 add by haibo.li
     * 2010-5-10
     */
    public void addHandleLog(OrHandleLog log);
    
    /**
     * 同步配额明细中的总价格到订单表，防止变价，导致总金额和配额明细中的金额不相等 add by shengwei.zuo 2010-6-4
     * @param order
     * @return
     */
    public void synchroSumPriceToOrder(OrOrder order);
    
	/**
	 * 根据代理商code得到享受政策范围
	 * add by yong.zeng
	 * @param agentcode
	 * @return
	 */
	public int getPolicyScope(String agentcode);
	
	/**
	 * 二次确认改变order的一些属性，返回二次确认的log
	 * @param order
	 * @return strLog
	 */
	public String changeOrderForSencondConfirm(OrOrder order);
	
	/**
	 * 根据会员会籍获取会员的项目号
	 * @param memberCd
	 * @return
	 */
	public String getMemberAliasId(String memberCd);

	/**
	 * 处理订单的附加信息，如提示信息
	 * @param order
	 * @param params
	 */
	public void updateExtInfo(OrOrder order, Map params);
	
	/**
	 * 记录订单的来源信息，醒狮计划促销活动
	 */
	public void updateExtInfoForWakeUp(OrOrder order);
	
	/**
	 * 记录订单来源，百分点推荐栏
	 */
	public void updateExtInfoforBFD(OrOrder order);
	/**
	 * 获取订单附近信息，如提示信息
	 * @param request
	 * @param order
	 */
	public void getOrOrderExtInfo(HttpServletRequest request, OrOrder order);
	
    /**
     * 根据订单号和信息类型获取订单附加信息
     * @param order
     * @param type
     * @return
     */
    public String getOrderExtInfoByType(OrOrder order, String type);
	
	/**
	 * 根据多个订单号查询订单 
	 * @param orderIds 订单ID字符串
	 * @return
	 */
	List<OrOrder> getOrOrderList(String orderIds);
	
	
	/* 查询是否有这个sid
	 * 信用卡支付sid
	 * return boolean
	 */
	
	boolean validateSid(String sid);
	/**
	 * 获取酒店确认号
	 * @param id
	 * @return
	 */
	public String getConfirmNum(Long orderId);
	
	/**
	 * 查询projectcode
	 * @param orderCD 
	 */
	public String queryOrderProject(String orderCD);
	
	/**
	 * 据订单编号查询HtlProjectCode
	 * @param orderCD
	 * @return
	 */
	public HtlProjectCode queryHtlOrderProject(String orderCD);
}