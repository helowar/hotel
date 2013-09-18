package com.mangocity.hotel.order.service.impl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.order.constant.DailyAuditConstant;
import com.mangocity.hotel.order.constant.OrderAuditState;
import com.mangocity.hotel.order.constant.OrderItemAuditState;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.dao.IAuditDao;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaPaperFaxItem;
import com.mangocity.hotel.order.persistence.HtlAuditInfo;
import com.mangocity.hotel.order.persistence.HtlAuditInfoSetup;
import com.mangocity.hotel.order.persistence.OrAuditFaxLog;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.persistence.VOrOrderItem;
import com.mangocity.hotel.order.persistence.view.AuditResult;
import com.mangocity.hotel.order.service.IAuditOrderService;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.order.service.assistant.OrderFax;
import com.mangocity.hotel.order.service.assistant.OrderItemFax;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Fax;

/**
 * 日审service
 * 
 * @author chenkeming
 * 
 */
public class AuditOrderService implements IAuditOrderService {

	private static final MyLog log = MyLog.getLogger(AuditOrderService.class);

	private DAOIbatisImpl queryDao;

	/**
	 * 传真邮件辅助类
	 */
	private MsgAssist msgAssist;

	/**
	 * message接口
	 */
	private CommunicaterService communicaterService;

	// 日审记录的DAO
	private IAuditDao auditDao;

	/**
	 * 生成日审记录
	 * 
	 * @param auditDate
	 * @param channelId
	 * @param newL
	 * @param editL
	 */
	public void genAuditRecords(Date auditDate, Long channelId, List<Object[]> newL, List<Object[]> editL) {
		long bathId = auditDao.getBathIdByAutoAudit(DateUtil.dateToString(auditDate), channelId);
		List<Object[]> lstAuditInfo = auditDao.getAuditRecordsByTempAuditinfo(bathId);
		for (Object[] res : lstAuditInfo) {
			if ("1".equals(res[3].toString())) {
				newL.add(res);
			} else {
				editL.add(res);
			}
		}
	}

	/**
	 * 根据参数获取日审传真list
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getChannels(Map params) {
		return queryDao.queryForList("querySendFaxChannels", params);
	}

	/**
	 * 获取日审记录
	 * 
	 * @param auditID
	 * @return
	 */
	public DaDailyaudit getDaDailyAudit(Serializable auditID) {
		return (DaDailyaudit) auditDao.get(DaDailyaudit.class, auditID);
	}

	/**
	 * 根据渠道ID和给的日期获取传真号码
	 * 
	 * @param channelId
	 * @param now
	 * @return
	 */
	public String getFaxNoByChannelId(Long channelId, Date now) {
		List<HtlAuditInfoSetup> getAuditInfoSetup = auditDao.getAuditInfoSetup(channelId);
		for (HtlAuditInfoSetup setup : getAuditInfoSetup) {
			//开始日期
			Date tmpDate = setup.getAuditBeginDate();
			if (null != tmpDate && 0 <= now.compareTo(tmpDate) || null == tmpDate) {
				//结束日期
				tmpDate = setup.getAuditEndDate();
				if (null != tmpDate && 0 >= now.compareTo(tmpDate) || null == tmpDate) {
					String weeks = setup.getWeeks();
					if (!StringUtil.isValidStr(weeks) || 0 <= weeks.indexOf("" + DateUtil.getWeekOfDate(now))) {
						String timeStr = DateUtil.formatHourTimeHToString(now);
						String tmpTimeStr = setup.getAuditBeginTime();
						if (!StringUtil.isValidStr(tmpTimeStr)
								|| Integer.parseInt(tmpTimeStr) <= Integer.parseInt(timeStr)) {
							tmpTimeStr = setup.getAuditEndTime();
							if (!StringUtil.isValidStr(tmpTimeStr)
									|| Integer.parseInt(tmpTimeStr) >= Integer.parseInt(timeStr)) {
								return setup.getAuditNo();
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取退房日审传真信息
	 * 
	 * @param dailyAudit
	 * @return
	 */
	public List<Object[]> getItemsForAuditFaxCheckout(DaDailyaudit dailyAudit) {
		return auditDao.getItemsForAuditFaxCheckout(dailyAudit);
	}

	/**
	 * 获取入住日审传真信息
	 * 
	 * @param dailyAudit
	 * @return
	 */
	public List<Object[]> getItemsForAuditFaxCheckin(DaDailyaudit dailyAudit) {
		return auditDao.getItemsForAuditFaxCheckin(dailyAudit);
	}

	/**
	 * 根据日期获取OrAuditFaxLog
	 * @param nigth
	 * @return
	 */
	public List<OrAuditFaxLog> getAuditLogList(Date nigth) {
		return  auditDao.queryByNamedQuery("hQueryAudit_Log", new Object[] { nigth });
		
	}

	/**
	 * 根据渠道名称获取渠道
	 * 
	 * @param data
	 * @return
	 */
	public List<HtlAuditInfo> getAuditInfoByChannelName(String channelName) {
		return  auditDao.queryByNamedQuery("hQueryAudit_Info", new Object[] { channelName });
		
	}

	/**
	 * 发送日审传真
	 * 
	 * @param dailyAudit
	 * @param faxNo
	 * @param user
	 * @return
	 */
	public Long sendAuditFax(DaDailyaudit dailyAudit, String faxNo, OrWorkStates user, String remarkToHotel) {
		//获取退房日审传真信息
		List<Object[]> itemList = this.getItemsForAuditFaxCheckout(dailyAudit);		
		
		if(itemList.isEmpty()){
			//因为返回的itemList是final的Collections.EMPTY_LIST，不能对它进行addAll操作
			itemList = new ArrayList<Object[]>();
		}
		//获取入住日审传真信息
		itemList.addAll(this.getItemsForAuditFaxCheckin(dailyAudit));
		if (itemList.isEmpty()) {
			return null;
		}
		//封装发送传真的信息
		String xmlString = this.genOrderFaxXml(itemList,remarkToHotel,dailyAudit, faxNo);
		try {
			Fax fax = new Fax();
			fax.setXml(xmlString);
			fax.setApplicationName("hotel");
			fax.setTemplateFileName(FaxEmailModel.DAY_CHECK_FORM);
			fax.setTo(new String[] { faxNo });
			fax.setFrom(user.getLogonId());
			Long ret = communicaterService.sendFax(fax);
			return ret;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 *  * 封装发送传真的信息
	 * @param itemList
	 * @param remarkToHotel
	 * @param dailyAudit
	 * @param faxNo
	 * @return
	 */
	public  String  genOrderFaxXml(List<Object[]> itemList,String remarkToHotel,DaDailyaudit dailyAudit, String faxNo){
		OrderFax orderFax = new OrderFax();
		for (Object[] itemVal : itemList) {
			//传真模板
			OrderItemFax item = new OrderItemFax();
			item.setItemOrderCD((String) itemVal[0]);
			item.setItemName((String) itemVal[1]);
			String itemFellowName = (String) itemVal[6];
			if (!StringUtil.isValidStr(itemFellowName)) {
				itemFellowName = (String) itemVal[2];
			}
			item.setItemFellowNames(itemFellowName);
			item.setItemConfirmNo((String) itemVal[3]);
			Date checkInDate = (Date) itemVal[4];
			item.setItemCheckInDate(DateUtil.dateToString(checkInDate));
			item.setRoomNo((String) itemVal[5]);
			item.setItemType(itemVal[7].toString());
			orderFax.getOrderItemList().add(item);
		}
		orderFax.setRemarkToHotel(remarkToHotel);// 加上酒店备注
		orderFax.setHotelName(dailyAudit.getChannelname());
		orderFax.setHotelFax(faxNo);//传真号码
		Date toNight = dailyAudit.getAuditdate();
		orderFax.setArrivalTimeStart(DateUtil.dateToString(toNight));
		Date retTime = DateUtil.getDate(toNight, 1);
		orderFax.setCheckOutDate(DateUtil.dateToString(retTime));
		retTime = DateUtil.getDateByHour(retTime, 22);
		orderFax.setArrivalTimeEnd(DateUtil.datetimeToString(retTime));//最晚到店时间
		orderFax.setBarCode("HB" + dailyAudit.getID());
		
		//将发送传真的对象转换为xmL格式的传真模板
		return  msgAssist.genOrderFaxXml(orderFax);
	}
	
	
	/**
	 * 记录日志
	 * 
	 * @param seanState
	 * @param channelId
	 * @param night
	 * @param fax
	 * @param user
	 * @param channelName
	 */
	public void noteLog(boolean seanState, Long channelId, Date night, String fax, OrWorkStates user,
			String channelName, String remarkToHotel) {
		Date now = new Date();
		OrAuditFaxLog orAuditFaxLog = new OrAuditFaxLog();
		orAuditFaxLog.setNight(DateUtil.getDate(night, 1));
		orAuditFaxLog.setFax(fax);
		//增加酒店备注 addby juesu.chen
		orAuditFaxLog.setRemarkToHotel(remarkToHotel);
		orAuditFaxLog.setWorkName(user.getName() + "(" + user.getLogonId() + ")");
		orAuditFaxLog.setWorkTime(now);
		orAuditFaxLog.setChannelId(channelId);
		orAuditFaxLog.setChannelName(channelName);
		if (seanState) {
			orAuditFaxLog.setSendState(1);
		} else {
			orAuditFaxLog.setSendState(2);
		}
		try {
			if (null == orAuditFaxLog.getID()) {
				auditDao.save(orAuditFaxLog);
			} else
				auditDao.saveOrUpdate(orAuditFaxLog);
		} catch (Exception e) {
			log.error("记录日志发生错误 !",e);
		}
	}

	/**
	 * 获取日审传真发送次数
	 * 
	 * @param auditId
	 * @return
	 */
	public List<DaPaperFaxItem> getPaperFaxItem(Long auditId) {
		return  auditDao.queryByNamedQuery("hQueryPaperFaxItem", new Object[] { auditId });
	}

	/**
	 * 提供给审核的接口方法，用于更新item表数据
	 * 
	 * @param notesMan
	 *            操作人名字
	 * @param orderId
	 *            订单Id
	 * @param orderType
	 *            1:散客, 2:tmc订单
	 * @param auditType
	 *            1: 入住审核, 2: 退房审核
	 * @param results
	 *            审核结果数组
	 * @param bFinish
	 *            改订单是否审核已完成
	 * @param orderState
	 *            看OrderState类
	 * @return 0: 成功, 1:失败, 2:财务已获取
	 */
	public int noteAuditResult(String notesMan, Long orderId, int orderType, int auditType, Date night,
			AuditResult[] results, boolean bFinish, int orderState) {
		VOrOrder order = this.getViewOrder(orderId, orderType);
		if (null == order) {
			log.error("取不到VOrOrder, id=" + orderId);
			return 1;
		}
		if (null == results || 0 >= results.length) {
			return 1;
		}
		String hsql = "";
		Object[] obj = null;
		if (1 == auditType) { // 入住审核
			int intAudit = this.checkInAudit(results,orderId,night,notesMan,order);
			//为2说明是tmc订单的，直接返回
			if(intAudit==2){
				return  2;
			}
		} else { // 退房审核
			int intChOut = checkOutAudit(results, orderId, night, notesMan, order,orderType);
			//为2说明是tmc订单的，直接返回
			if(intChOut==2){
				return  2;
			}
		}
		// 更新订单的入住人姓名
		String fellowNames = order.getFellowNames();
		for (AuditResult result : results) {
			String fellow = result.getFellowName();
			String oldName = result.getOldName();
			if (StringUtil.isValidStr(fellow) && StringUtil.isValidStr(oldName)) {
				fellowNames = fellowNames.replace(oldName, fellow);
			}
		}
		boolean bSave = false;
		if (!fellowNames.equals(order.getFellowNames())) {
			order.setFellowNames(fellowNames);
			bSave = true;
		}
		if (bFinish) {
			order.setOrderAuditState(OrderAuditState.FINISH);
			bSave = true;
		}
		order.setOrderState(orderState);
		// 设置日审状态
		if (OrderState.CHECKIN == orderState){
			order.setAuditState(OrderState.EARLY_QUIT);
		}else{
			order.setAuditState(orderState);
		}
		auditDao.saveOrUpdate(order);
		return 0;
	}
	
	/**
	 * 入住审核
	 * @param results
	 * @param orderId
	 * @param night
	 * @param notesMan
	 * @param order
	 * @return
	 */
	public  int   checkInAudit(AuditResult[] results,Long orderId,Date night,String notesMan,VOrOrder order){
		for (AuditResult auditResult :results) {
			if (auditResult == null){
				continue;
			}
			int roomIndex = auditResult.getRoomIndex();
			VOrOrderItem orderItem = this.getOrOrderItem(orderId, night, roomIndex);
			if (orderItem.isSettlement()) {
				return 2;
			}
			if (null != orderItem) {
				String fellowName = auditResult.getFellowName();
				String roomNo = auditResult.getRoomNo();
				// 取得之前入住情况
				if (StringUtil.isValidStr(fellowName) || StringUtil.isValidStr(roomNo)) { // 入住人是否修改,房间号是否设置
					updateNameAndRoomNum(order, orderItem, fellowName, roomNo, notesMan);
				}
				int noteResult = auditResult.getNoteResult();
				if (!orderItem.isSettlement()) {
					orderItem.setOrderState(noteResult);
					orderItem.setAuditState(OrderItemAuditState.ACHIEVE);
					orderItem.setNotesMan(notesMan);
					orderItem.setShow(1 == noteResult ? false : true);
					auditDao.saveOrUpdate(orderItem);
				}
				Object[] obj = new Object[7];
				obj[0] = notesMan;
				if (2 == noteResult) { // noshow
					obj[1] = 2;
					obj[2] = true;
					obj[3] = OrderItemAuditState.ACHIEVE;
				} else if (auditResult.getReciprocal() == DailyAuditConstant.EXTENDED_STAY){
					//如果回访结果为延住，日审完成，其它间夜订单明细状态全部改为已入住 modify by wupingxiang at 2012-9-19
					obj[1] = 1;
					obj[2] = true;
					obj[3] = OrderItemAuditState.ACHIEVE;
				}else {
					obj[1] = 0;
					obj[2] = false;
					obj[3] = OrderItemAuditState.NOT_WORK;
				}
				obj[4] = orderId;
				obj[5] = roomIndex;
				obj[6] = order.getCheckinDate();
				auditDao.updateVOrOrderItem(obj);
			}
		}
		return 0;
	}
	
	/**
	 * 退房审核
	 * @param results
	 * @param orderId
	 * @param night
	 * @param notesMan
	 * @param order
	 * @param orderType
	 * @return
	 */
	public int  checkOutAudit(AuditResult[] results,Long orderId,Date night,String notesMan,VOrOrder order,int orderType){
		for (AuditResult auditResult: results) {
			// 退房审核时,如果有未入住,这时是不应该产生的,因为还在做入住回访,所以不应该更新到item表里
			if (auditResult == null || (auditResult.getNoteResult() == 2 && auditResult.getCheckoutDate() == null))
				continue;
			int roomIndex = auditResult.getRoomIndex();
			VOrOrderItem orderItem = this.getOrOrderItem(orderId, night, roomIndex);
			if (orderItem.isSettlement()) {
				return 2;
			}
			if (null != orderItem) {
				String fellowName = auditResult.getFellowName();
				String roomNo = auditResult.getRoomNo();
				if (StringUtil.isValidStr(fellowName) || StringUtil.isValidStr(roomNo)) { // 入住人是否修改,房间号是否设置
					updateNameAndRoomNum(order, orderItem, fellowName, roomNo, notesMan);
				}
				int noteResult = auditResult.getNoteResult();
				if (!orderItem.isSettlement()) {
					orderItem.setOrderState(noteResult);
					orderItem.setShow(1 == noteResult ? false : true);
					orderItem.setAuditState(OrderItemAuditState.ACHIEVE);
					orderItem.setNotesMan(notesMan);
					auditDao.saveOrUpdate(orderItem);  
				}
				Date checkoutDate = auditResult.getCheckoutDate();
				Date checkinDate = order.getCheckinDate();
				// 把>1 改成>=１,主要是处理：住２天，而且只生成退房审核时，要更新第一天的入住情况
				if (1 == noteResult && DateUtil.getDay(checkinDate, night) >= 1 || 2 == noteResult
						&& DateUtil.getDay(checkinDate, checkoutDate) >= 1) {
					Object [] obj = new Object[5];
					obj[0] = notesMan;
					obj[1] = orderId;
					obj[2] = Integer.valueOf(orderType);// TMC订单编号重复,修改日审的问题
					obj[3] = roomIndex;
					Date argDate = (1 == noteResult) ? night : checkoutDate;
					obj[4] = argDate;
					auditDao.updateVOrOrderItemTwoDays(obj);
				}
				if (2 == noteResult && night.after(checkoutDate)) {
					Object [] obj = new Object[4];
					obj[0] = notesMan;
					obj[1] = orderId;
					obj[2] = roomIndex;
					obj[3] = checkoutDate;
					auditDao.updateVorderItemShow(obj);
				}
			}
		}
		return 0;
	}
	
	/**
	 * 更新入住人或房间号
	 * 
	 * @param order
	 * @param orderItem
	 * @param fellowName
	 * @param roomNo
	 * @param notesMan
	 */
	public void updateNameAndRoomNum(VOrOrder order, VOrOrderItem orderItem, String fellowName, String roomNo,
			String notesMan) {
		if (!orderItem.isSettlement()) {
			if (StringUtil.isValidStr(fellowName)) {
				orderItem.setFellowName(fellowName);
			}
			if (StringUtil.isValidStr(roomNo)) {
				orderItem.setRoomNo(roomNo);
			}
		}
		auditDao.updateNameAndRoomNum(order,  orderItem, fellowName, roomNo,notesMan);
	}
	
	/**
	 * 查询订单视图，供日审使用，同时支持TMC订单日审
	 * @param orderID
	 * @param orderType
	 * @return
	 */
	public VOrOrder getViewOrder(Long orderID, int orderType) {
		List<VOrOrder> vorderLst = auditDao.queryByNamedQuery("hQueryOrder_audit", new Object[] { orderID, orderType });
		if (null != vorderLst && !vorderLst.isEmpty()) {
			return vorderLst.get(0);
		}
		return null;
	}

	/**
	 * 根据参数获得VOrOrderItem对象
	 * 
	 * @param orderId
	 * @param date
	 * @param roomIndex
	 * @return
	 */
	public VOrOrderItem getOrOrderItem(Long orderId, Date date, int roomIndex) {
		List<VOrOrderItem> vOrderItemLst = auditDao.queryByNamedQuery("hQueryVOrderItem3", new Object[] { orderId, date, roomIndex });
		if (null != vOrderItemLst && !vOrderItemLst.isEmpty()) {
			return  vOrderItemLst.get(0);
		}
		return null;
	}

	/**
	 * 更新日审信息
	 * @param dailyAudit
	 */
	public void saveOrUpdateDailyAudit(DaDailyaudit dailyAudit) {
		auditDao.saveOrUpdate(dailyAudit);
	}

	/**
	 * 更新 纸质日审明细表
	 * 
	 *  @param faxItem
	 */
	public void saveOrUpdateFaxItem(DaPaperFaxItem faxItem) {
		if (null == faxItem.getID()) {
			auditDao.save(faxItem);
		} else {
			auditDao.saveOrUpdate(faxItem);
		}
	}

	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	public void setMsgAssist(MsgAssist msgAssist) {
		this.msgAssist = msgAssist;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public void setAuditDao(IAuditDao auditDao) {
		this.auditDao = auditDao;
	}
}