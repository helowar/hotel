package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.ep.service.EpOrderManagerService;
import com.mangocity.hotel.base.service.assistant.URLClient;
import com.mangocity.hotel.order.constant.DailyAuditConstant;
import com.mangocity.hotel.order.persistence.DaDailyauditItem;
import com.mangocity.hotel.order.persistence.DaPersonalWorkload;
import com.mangocity.hotel.order.persistence.DaReturnvisit;
import com.mangocity.hotel.order.service.IDailyAuditService;
import com.mangocity.hotel.order.service.assistant.AuditItemDetailInfo;
import com.mangocity.hotel.order.service.assistant.DaReturnRoomDetail;
import com.mangocity.hotel.order.service.assistant.ReturnInfo;
import com.mangocity.util.bean.MyBeanUtil;

public class AuditDistributionAction extends GenericCCAction {
	
	private static final long serialVersionUID = 1L;
	
	public IDailyAuditService dailyAuditService;
	/** 需回访的数据 */
	public List<ReturnInfo> returnDateList;
	/** 回访ID */
	private String returnVisitID;
	/** 个人工作量情况 */
	private DaPersonalWorkload myWorkload;
	/** 回访数据 */
	private DaReturnvisit returnvisit;
	/** 获取人ID */
	private String loginID;
	/** 房间总数 */
	private int roomNumCount;
	/** 订单总数 */
	private int orderNumCount;
	/** 订单量 */
	private String orderMount;
	/** 房间量 */
	private String roomMount;
	
	private EpOrderManagerService epOrderManagerService;
	
	private String isEpHotel;
	
	private Map epHotelList = new HashMap<String,String>();;
	/**
	 * 查看TMC订单的
	 */
	private String HOP_TMC_ORDER;
	
	public String getHOP_TMC_ORDER() {
		return HOP_TMC_ORDER;
	}

	public void setHOP_TMC_ORDER(String hop_tmc_order) {
		HOP_TMC_ORDER = hop_tmc_order;
	}
	/**
	 * 回访自动分配
	 * @return
	 */
	public String returnDistribution_Auto(){
		//当前登录信息
		user = super.getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		loginID = user.getLogonId();
//		判断此用户是否日审人员
        if(!checkAuditUser()){
    		return SUCCESS;
        }
		//自动分配一条回访记录给用户
		boolean isUserhadAutoAuditRecord =dailyAuditService.
						autoAllotReturnForUser(user.getLogonId(),user.getName());
		
		// 把用户的工作量找出
		myWorkload = dailyAuditService.getPersonWorkloadByAuditId(user.getLogonId(),
				DailyAuditConstant.RETURNVISIT_TYPE);
		// 如果为空,则新建一个对象
		if (null == myWorkload) {
			myWorkload = new DaPersonalWorkload();
		}
		request.setAttribute("isUserhadAutoAuditRecord",
				isUserhadAutoAuditRecord);
	
		return SUCCESS;
	}

	/**
	 * 得到需要回访的数据
	 * @return
	 */
	public String getRetrunData(){
		getReturnInfo();
		return "returnItem";
	}
	/**
	 * 根据回访ID得到所有回访信息
	 */
	public void getReturnInfo(){
		//回访信息
		returnvisit = dailyAuditService.getReturnvisit(Long.valueOf(returnVisitID).longValue());
		//回访日审明细
		returnDateList = dailyAuditService.getReturnData(Long.valueOf(returnVisitID).longValue());
//		 TMC订单记录查询
        HOP_TMC_ORDER = URLClient.HOP_TMC_ORDER;
        
         
        if(returnDateList!=null && returnDateList.size()>0){
        	for(ReturnInfo returnInfo : returnDateList){
            	for(DaDailyauditItem item : returnInfo.getReturnDateList()){
            		if(item.getHotelid()!=null){
                		isEpHotel = epOrderManagerService.validateEpOrderByHotelId(String.valueOf(item.getHotelid()));               		
                		epHotelList.put(String.valueOf(item.getHotelid()), isEpHotel);
            		}            		
            	}
            	
            }
        }
        
        
		String memberCd = null;
		for(ReturnInfo returnInfo : returnDateList){
			List<DaDailyauditItem> list = returnInfo.getReturnDateList();
			for(DaDailyauditItem item : list ){
				memberCd = item.getMemberCd();
				if(memberCd !=null && !"".equals(memberCd)){
					break; 
				}
			}
		}
		member = getMemberSimpleInfoByMemberCd(memberCd, false);
		
	}
	
	/**
	 * 保存回访操作
	 * @return
	 */
	public String saveReturnRecord() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		Map params = super.getParams();
		// 取得页面上更新的房间数据
		List roomInfosList = getRoomInforFromPage(params);
		
		List orderInfoList = getOrderInforFromPage(params);
		// 如果无更新,则直接返回
		if (!roomInfosList.isEmpty()) {
			 //把更新数据交给service进行更新操作
			dailyAuditService.updateReturnData(null,null,null,null,returnVisitID,user.getLogonId(),user.getName(),roomInfosList,
					DailyAuditConstant.SAVE_OPERATION,checkAuditUser());
		}
		//更新订单数据
		if(!orderInfoList.isEmpty())
			dailyAuditService.updateAuditItemDataRemark(orderInfoList,roleUser.getLoginName(),roleUser.getName());
		//回访信息
		getReturnInfo();
		
		request.setAttribute("operaMsg", "操作成功!");
		
		return "returnItem";
	}
	
	/**
	 * 完成回访操作 
	 * @return
	 */
	public String completeAuditRecord() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		Map params = super.getParams();
		// 取得页面上更新的房间数据
		List roomInfosList = getRoomInforFromPage(params);
		List orderInfoList = getOrderInforFromPage(params);
		// 把日审ID和更新数据交给service进行更新操作
		dailyAuditService.updateReturnData(orderMount,roomMount,null,null,returnVisitID,user.getLogonId(),user.getName(), roomInfosList,
				DailyAuditConstant.COMPLETE_OPERATION,checkAuditUser());
		//更新订单数据
		if(!orderInfoList.isEmpty())
			dailyAuditService.updateAuditItemDataRemark(orderInfoList,roleUser.getLoginName(),roleUser.getName());
		request.setAttribute("operaMsg", "操作成功!");
		request.setAttribute("isComplete", true);
		return "completeReturn";
	}
	
	/**
	 * 释放回访操作 
	 * @return
	 */
	public String releaseReturnRecord() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		Map params = super.getParams();
		// 取得页面上更新的房间数据
		List roomInfosList = getRoomInforFromPage(params);
		//延长时间
		String delayTime = (String) params.get("delayTime");
		//释放原因
		String releaseReason = (String) params.get("relaxReason");
		// 把日审ID和更新数据交给service进行更新操作
		dailyAuditService.updateReturnData(null,null,delayTime,releaseReason,returnVisitID,user.getLogonId(),user.getName(), roomInfosList,
				DailyAuditConstant.RELEASE_OPERATION,checkAuditUser());
		List orderInfoList = getOrderInforFromPage(params);
		if(!orderInfoList.isEmpty())
			dailyAuditService.updateAuditItemDataRemark(orderInfoList,roleUser.getLoginName(),roleUser.getName());
		request.setAttribute("operaMsg", "操作成功!");
		request.setAttribute("isRelease", true);
		return "completeReturn";
	}
	
	
	/**
	 * 把页面上的房间数据封闭到一个List里 addby juesuchen
	 * 
	 * @param params
	 * @return
	 */
	private List getRoomInforFromPage(Map params) {
		List roomInfos = MyBeanUtil.getBatchObjectFromParam(params,
				DaReturnRoomDetail.class, roomNumCount);
		List<DaReturnRoomDetail> roomInfoList = new ArrayList();
		for (int i = 0; i < roomInfos.size(); i++) {
			DaReturnRoomDetail roomInfo = (DaReturnRoomDetail) roomInfos
					.get(i);
			// 只有输入了结果,才把它放到Map,以便更新到数据库
			if (null != roomInfo.getReturnresults()
					&& !("0".equals(roomInfo.getReturnresults()))) {
				roomInfoList.add(roomInfo);
			}
		}
		return roomInfoList;
	}
	/**
	 * 把订单信息封装到List里
	 * @param params
	 * @return
	 */
	private List getOrderInforFromPage(Map params){
		List<AuditItemDetailInfo> orderInfos = MyBeanUtil.getBatchObjectFromParam(params,
				AuditItemDetailInfo.class, orderNumCount);
		List<AuditItemDetailInfo> orderInfoList = new ArrayList();
		for(AuditItemDetailInfo item : orderInfos){
			if(null != item.getRemark()&& !("".equals(item.getRemark()))){
				orderInfoList.add(item);
			}
		}
		return orderInfoList;
	}
	
	
	
	
	
	
	
	
	
	
	/*============get set=====================*/
	public IDailyAuditService getDailyAuditService() {
		return dailyAuditService;
	}
	public void setDailyAuditService(IDailyAuditService dailyAuditService) {
		this.dailyAuditService = dailyAuditService;
	}
	public DaPersonalWorkload getMyWorkload() {
		return myWorkload;
	}
	public void setMyWorkload(DaPersonalWorkload myWorkload) {
		this.myWorkload = myWorkload;
	}
	public String getReturnVisitID() {
		return returnVisitID;
	}
	public void setReturnVisitID(String returnVisitID) {
		this.returnVisitID = returnVisitID;
	}
	public List<ReturnInfo> getReturnDateList() {
		return returnDateList;
	}
	public void setReturnDateList(List<ReturnInfo> returnDateList) {
		this.returnDateList = returnDateList;
	}
	public DaReturnvisit getReturnvisit() {
		return returnvisit;
	}
	public void setReturnvisit(DaReturnvisit returnvisit) {
		this.returnvisit = returnvisit;
	}
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public int getRoomNumCount() {
		return roomNumCount;
	}
	public void setRoomNumCount(int roomNumCount) {
		this.roomNumCount = roomNumCount;
	}
	public String getOrderMount() {
		return orderMount;
	}
	public void setOrderMount(String orderMount) {
		this.orderMount = orderMount;
	}
	public String getRoomMount() {
		return roomMount;
	}
	public void setRoomMount(String roomMount) {
		this.roomMount = roomMount;
	}

	public int getOrderNumCount() {
		return orderNumCount;
	}

	public void setOrderNumCount(int orderNumCount) {
		this.orderNumCount = orderNumCount;
	}

	public EpOrderManagerService getEpOrderManagerService() {
		return epOrderManagerService;
	}

	public void setEpOrderManagerService(EpOrderManagerService epOrderManagerService) {
		this.epOrderManagerService = epOrderManagerService;
	}

	public String getIsEpHotel() {
		return isEpHotel;
	}

	public void setIsEpHotel(String isEpHotel) {
		this.isEpHotel = isEpHotel;
	}

	public Map getEpHotelList() {
		return epHotelList;
	}

	public void setEpHotelList(Map epHotelList) {
		this.epHotelList = epHotelList;
	}
	
	
}
