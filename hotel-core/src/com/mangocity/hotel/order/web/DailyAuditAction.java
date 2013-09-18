package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mangocity.ep.service.EpOrderManagerService;
import com.mangocity.hdl.hotel.dto.DailyAuditExRequest;
import com.mangocity.hdl.hotel.dto.DailyAuditExResponse;
import com.mangocity.hdl.hotel.dto.MGExAuditOrderItem;
import com.mangocity.hdl.hotel.dto.MGExDailyAudit;
import com.mangocity.hdl.service.IExMappingService;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.service.assistant.URLClient;
import com.mangocity.hotel.order.constant.DailyAuditConstant;
import com.mangocity.hotel.order.persistence.DaAuditingWorkload;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaDailyauditItem;
import com.mangocity.hotel.order.persistence.DaDailyauditItemSubtable;
import com.mangocity.hotel.order.persistence.DaPersonalWorkload;
import com.mangocity.hotel.order.persistence.HtlAuditInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.service.IDailyAuditService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.assistant.AuditItemDetailInfo;
import com.mangocity.hotel.order.service.assistant.ChannelInfo;
import com.mangocity.hotel.order.service.assistant.DaAuditDetailInfo;
import com.mangocity.hotel.order.service.assistant.DaAuditRoomDetailInfo;
import com.mangocity.hotel.order.service.assistant.HotelAuditDetail;
import com.mangocity.hotel.order.service.assistant.HtlChannelDetailInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 日审操作Action
 * 
 * @author chenjuesu
 * 
 */
public class DailyAuditAction extends GenericCCAction {

	/**
	 * 日审服务接口
	 */
	private IDailyAuditService dailyAuditService;
	
	/**
	 * 直连服务接口
	 */
	private IHDLService hdlService;
	
	
	private IOrderService orderService;
	
	private IExMappingService exService;

	/**
	 * 定义跳转字符串
	 */
	private String forward;

	/**
	 * 日审总表ID
	 */
	private Long auditId;
	/**
	 * 订单ID,日审补登时
	 */
	private Long orderID;
	/**
	 * 分配的日审记录ID集合
	 */
	private String selIDs;
	/**
	 * 订单体,日审补登时
	 */
	private VOrOrder vOrOrder;
	/**
	 * 订单Item集合,日审补登时
	 */
	private List orderItems;

	/**
	 * 日审总表
	 */
	private DaDailyaudit audit;

	/**
	 * 日审记录详情
	 */
	private List<DaAuditDetailInfo> auditDetails;

	/**
	 * 酒店渠道详情
	 */
	private HtlChannelDetailInfo channelDetail;

	/**
	 * 个人工作量情况
	 */
	private DaPersonalWorkload myWorkload;

	/**
	 * 总的工作量情况(包括:今天需审核/历史未审核/今天已完成)
	 */
	private List<DaAuditingWorkload> auditWorkload;

	/**
	 * 渠道信息
	 */
	private List channelsInfo;

	/**
	 * 存放交接人信息
	 */
	private List<OrWorkStates> userList;

	/**
	 * 房间总数
	 */
	private int roomNumCount;
	/**
	 * 订单数
	 */
	private int orderCount;

	/**
	 * 酒店审核信息
	 */
	private List<HotelAuditDetail> hotelAudits;

	private String theHotelName;

	private String theAuditDate;

	private String theOrderCd;

	/**
	 * 延长分钟数
	 */
	private String delayTime;
	/**
	 * 查看TMC订单的
	 */
	private String HOP_TMC_ORDER;
	
	//********用于审核记录明细页面查询艺龙订单所需参数
	
	private Long hotelId;
	//最早入住日期
	private String 	earlyCheckInDate;  //日期格式都为yyyy-MM-dd
	//最晚入住日期
	private String lateCheckInDate;
	//最早离店日期
	private String earlyCheckOutDate;
	//最晚离店日期
	private String lateCheckOutDate;
	//艺龙渠道为9
	private long channelType = 0;
	
	private EpOrderManagerService epOrderManagerService;
	
	private String isEpHotel;
	
	//*****审核记录明细页面查询艺龙订单所需参数
	
	public String getHOP_TMC_ORDER() {
		return HOP_TMC_ORDER;
	}

	public void setHOP_TMC_ORDER(String hop_tmc_order) {
		HOP_TMC_ORDER = hop_tmc_order;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	/**
	 * 进入查询审核记录时 addby juesuchen
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String forwadToQueryAuditRecords() {
//		 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
		auditWorkload = (List<DaAuditingWorkload>) dailyAuditService
				.getWorkloadByType(DailyAuditConstant.AUDIT_TYPE);
		channelsInfo = dailyAuditService.getAllChannelInfo();
		forward = "toQueryAuditRecords";
		return forward;
	}

	/**
	 * 进入审核工作档案时,如无自动的则给用户自动分配一个记录 addby juesuchen
	 * 
	 * @return
	 */
	public String forwardToWorkingAudit() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
//		 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        forward = "toWorkingAudit";
        //判断此用户是否日审人员,如果是则分配
        if(checkAuditUser()){
//        	 如果用户已登陆，则为用户自动分配一条审核记录
    		boolean isUserhadAutoAuditRecord = dailyAuditService
    				.autoAllotAuditForUser(user.getLogonId(), user.getName());
//    		 把用户的工作量找出
    		//channelsInfo = dailyAuditService.getAllChannelInfo();
    		myWorkload = dailyAuditService.getPersonWorkloadByAuditId(user
    				.getLogonId(),DailyAuditConstant.AUDIT_TYPE);
    		request.setAttribute("isUserhadAutoAuditRecord",
    				isUserhadAutoAuditRecord);
        }
		// 如果为空,则新建一个对象
		if (null == myWorkload) {
			myWorkload = new DaPersonalWorkload();
		}
		return forward;
	}
	/**
	 * 在工作档案点击记录操作 addby juesuchen
	 * 
	 * @return
	 */
	public String getAuditDetail() {
		audit = dailyAuditService.findDaDailyauditById(auditId);
		Object htlId = super.getParams().get("hotelId");
		
		// 根据日审记录返回日审明细对象
		auditDetails = dailyAuditService.getAuditDetailsByDaDailyaudit(audit,htlId);
		isEpHotel = epOrderManagerService.validateEpOrderByHotelId(String.valueOf(auditDetails.get(0).getHotelId()));
		//add by yong.zeng
		if(audit.getChannelid()==5){//格林豪泰
			setDailyAuditResultToDetails(auditDetails);
		}
		HtlAuditInfo htlAudit = dailyAuditService.findHtlAuditInfoById(audit
				.getChannelid());
		if(null != htlAudit){
			// 根据渠道记录返回当天渠道明细对象
			channelDetail = dailyAuditService.getChannelDetailsByHtlAuditInfo(
					htlAudit, audit.getAuditdate(), audit.getID());
			channelDetail.setChannelId(htlAudit.getID());
			channelDetail.setChannelName(htlAudit.getChannelName());
			channelDetail.setReturnId(audit.getReturnid());
			// 日审时间
			channelDetail.setAuditDate(audit.getAuditdate());
		}else{
			//渠道为空,说明已被删除
			channelDetail = null;
		}
		 // TMC订单记录查询
        HOP_TMC_ORDER = URLClient.HOP_TMC_ORDER;
		// 如果去查询明细页面,把此单的情况结果带出
		if (null != forward) {
			if (1 == audit.getState() || 1 == audit.getAquireState())
				request.setAttribute("auditState", 1);
			else
				request.setAttribute("auditState", 0);
		}
		
		//如果为艺龙渠道 设置查询艺龙订单的参数
		if(null != auditDetails && auditDetails.size() == 1){ //艺龙一个酒店一个渠道
			DaAuditDetailInfo auditDetail = auditDetails.get(0);
			hotelId = auditDetail.getHotelId();
			//根据酒店ID 查询channelType 
			List<Long> hotelIds = new ArrayList<Long>();
			hotelIds.add(hotelId);
			List<ExMapping> exMapping = exService.getMapping(9, hotelIds);
			if(null != exMapping && exMapping.size() == 1){
				channelType = 9;
				assembleQueryParam(auditDetail);
				queryChannel();
			}
		}
		
		/*
		 * else{ //需要设置交接人信息 --注意,暂时未开放此功能 user = getOnlineWorkStates();
		 * userList = workStatesManager.lstWorkStatesByType(WorkType.AUDIT); for
		 * (OrWorkStates theUser : userList) { if(theUser == user){
		 * userList.remove(theUser); break; } } }
		 */
		// 默认是去工作档案的明细页面,除非有Forward值带入
		return null != forward ? forward : "toAuditDetail";

	}
	
	/**
	 * 查询出订单的渠道
	 */
	public void queryChannel(){
		//入住审核订单号
		String checkInOrderIds="";
		//退房审核订单号
		String checkOutOrderIds="";
		
		//获取订单渠道号
		for(DaAuditDetailInfo info:auditDetails){
			
			//获取入住审核订单渠道号
			for(DaDailyauditItem item:info.getCheckInItems()){
				checkInOrderIds+=","+item.getOrderid();
			}
			if(checkInOrderIds!=""){
				List<OrOrder> orList=orderService.getOrOrderList(checkInOrderIds.substring(1));
				for(DaDailyauditItem item:info.getCheckInItems()){
					for(OrOrder or:orList){
						if(item.getOrderid().equals(or.getID())){
							item.setChannel(or.getChannel());
						}
					}
				}
			}
			
			//获取退房审核订单渠道号
			for(DaDailyauditItem item:info.getCheckOutItems()){
				checkOutOrderIds+=","+item.getOrderid();
			}
			if(checkOutOrderIds!=""){
				List<OrOrder> orList=orderService.getOrOrderList(checkOutOrderIds.substring(1));
				for(DaDailyauditItem item:info.getCheckOutItems()){
					for(OrOrder or:orList){
						if(item.getOrderid().equals(or.getID())){
							item.setChannel(or.getChannel());
						}
					}
				}
			}
		}
	}
	/**
	 * 组装艺龙订单查询参数
	 * @param auditDetails
	 */
	private void assembleQueryParam(DaAuditDetailInfo auditDetail) {
		Date earlyCheckIn = null;
		Date lateCheckIn = null;
		Date earlyCheckOut = null;
		Date lateCheckOut = null;
		
		List<DaDailyauditItem> dailyauditItems = auditDetail.getCheckInItems();
		dailyauditItems.addAll(auditDetail.getCheckOutItems());
		
		for(DaDailyauditItem dailyauditItem : dailyauditItems){
			Date checkInTime = dailyauditItem.getCheckintime();
			Date checkOutTime = dailyauditItem.getCheckouttime();
			
			if(null == earlyCheckIn){
				earlyCheckIn = checkInTime;
				lateCheckIn = checkInTime;
			}else{
				if(DateUtil.compare(earlyCheckIn, checkInTime)<0) earlyCheckIn = checkInTime; //最早入住时间 大于该订单的入住时间 
				if(DateUtil.compare(lateCheckIn, checkInTime)>0) lateCheckIn = checkInTime;  //最晚入住时间 小于该订单的入住时间
			}
			
			if(null == earlyCheckOut){
				earlyCheckOut = checkOutTime;
				lateCheckOut = checkOutTime;
			}else{
				if(DateUtil.compare(earlyCheckOut, checkOutTime)<0) earlyCheckOut = checkOutTime; //最早退房时间 大于该订单的退房时间 
				if(DateUtil.compare(lateCheckOut, checkOutTime)>0) lateCheckOut = checkOutTime; //最晚退房时间 小于该订单的退房时间
			}
		}
		
		if(null != earlyCheckIn){
			earlyCheckInDate = DateUtil.toStringByFormat(earlyCheckIn, "yyyy-MM-dd");
			lateCheckInDate = DateUtil.toStringByFormat(lateCheckIn, "yyyy-MM-dd");
			earlyCheckOutDate = DateUtil.toStringByFormat(earlyCheckOut, "yyyy-MM-dd");
			lateCheckOutDate = DateUtil.toStringByFormat(lateCheckOut, "yyyy-MM-dd");
		}
	}

	/**
	 * 保存操作 addby juesuchen
	 * 
	 * @return
	 */
	public String saveAuditRecordOperation() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		Map params = super.getParams();
		// 取得页面上更新的房间数据
		Map roomInfosMap = getRoomInforFromPage(params,true);
		// 如果无更新,则直接返回
		if (!roomInfosMap.isEmpty()) {
			// 把日审ID和更新数据交给service进行更新操作
			dailyAuditService.updateDaDailyaudit(auditId, roomInfosMap,
					DailyAuditConstant.SAVE_OPERATION,false);
		}
		//取得订单的备注信息
		List remarks = getOrderInforFromPage(params);
		if(!remarks.isEmpty())
			dailyAuditService.updateAuditItemDataRemark(remarks,roleUser.getLoginName(),roleUser.getName());
		request.setAttribute("operaMsg", "操作成功!");
		forward = "saveAuditRecord";
		return forward;
	}

	/**
	 * 把页面上的房间数据封闭到一个MAP里 addby juesuchen
	 * 
	 * @param params
	 * @return
	 */
	private Map getRoomInforFromPage(Map params,boolean isNeedFilter) {
		List roomInfos = MyBeanUtil.getBatchObjectFromParam(params,
				DaAuditRoomDetailInfo.class, roomNumCount);
		Map roomInfosMap = new HashMap();
		for (int i = 0; i < roomInfos.size(); i++) {
			DaAuditRoomDetailInfo roomInfo = (DaAuditRoomDetailInfo) roomInfos
					.get(i);
			if(isNeedFilter){
				// 只有输入了结果,才把它放到Map,以便更新到数据库
				if (null != roomInfo.getAuditresults()
						&& !("0".equals(roomInfo.getAuditresults()))) {
					roomInfosMap.put(roomInfo.getID(), roomInfo);
				}
			}else{
				//这里是取有页面数据
				roomInfosMap.put(roomInfo.getID(), roomInfo);
			}
		}
		return roomInfosMap;
	}

	/**
	 * 完成操作 addby juesuchen
	 * 
	 * @return
	 */
	public String completeAuditRecordOperation() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		Map params = super.getParams();
		//Boolean iaAutoAllot = (Boolean)params.get("iaAutoAllot");
		// 取得页面上更新的房间数据
		Map roomInfosMap = getRoomInforFromPage(params,true);
		// 把日审ID和更新数据交给service进行更新操作
		dailyAuditService.updateDaDailyaudit(auditId, roomInfosMap,
				DailyAuditConstant.COMPLETE_OPERATION,checkAuditUser());
//		取得订单的备注信息
		List remarks = getOrderInforFromPage(params);
		if(!remarks.isEmpty())
			dailyAuditService.updateAuditItemDataRemark(remarks,roleUser.getLoginName(),roleUser.getName());
		request.setAttribute("operaMsg", "操作成功!");
		request.setAttribute("isComplete", true);
		forward = "completeAuditRecord";
		return forward;
	}

	/**
	 * 释放操作 addby juesuchen
	 * 
	 * @return
	 */
	public String releaseAuditRecordOperation() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		Map params = super.getParams();
		// 取得页面上更新的房间数据
		Map roomInfosMap = getRoomInforFromPage(params,true);
		if (StringUtil.isValidStr(delayTime)) {// 如果是延时操作,则设置延时时间
			roomInfosMap.put("delayTime", delayTime);
		} else {
			// 设置交接人信息
			roomInfosMap.put("beDeliverId", params.get("beDeliverId"));
			roomInfosMap.put("beDeliverName", params.get("beDeliverName"));
		}
		roomInfosMap.put("releaseReason", params.get("relaxReason"));
		// 把日审ID和更新数据交给service进行更新操作
		dailyAuditService.updateDaDailyaudit(auditId, roomInfosMap,
				DailyAuditConstant.RELEASE_OPERATION,false);
//		取得订单的备注信息
		List remarks = getOrderInforFromPage(params);
		if(!remarks.isEmpty())
			dailyAuditService.updateAuditItemDataRemark(remarks,roleUser.getLoginName(),roleUser.getName());
		request.setAttribute("operaMsg", "操作成功!");
		request.setAttribute("isRelease", true);
		forward = "releaseAuditRecord";
		return forward;
	}

	/**
	 * 获取到我的工作档案 addby juesuchen
	 * 
	 * @return
	 */
	public String getAuditRecordToMyList() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		// 设置本人信息,这里面有个注意的是,因为自已获取与交接,其实质是一样的,所以就共用同样逻辑处理
		forward = "getAuditRecordToMyList";
		//判断此用户是否日审人员
        if(!checkAuditUser()){
        	request.setAttribute("operaMsg", "你状态为关或非日审人员,不能获取");
    		return forward;
        }
		Map roomInfosMap = new HashMap();
		roomInfosMap.put("beDeliverId", user.getLogonId());
		roomInfosMap.put("beDeliverName", user.getName());
		// 把日审ID和更新数据交给service进行更新操作
		dailyAuditService.updateDaDailyaudit(auditId, roomInfosMap,
				DailyAuditConstant.RELEASE_OPERATION,false);
		request.setAttribute("operaMsg", "操作成功!");
		return forward;
	}
	/**
	 * 把订单备注封装到List里
	 * @param params
	 * @return
	 */
	private List getOrderInforFromPage(Map params){
		List<AuditItemDetailInfo> orderInfos = MyBeanUtil.getBatchObjectFromParam(params,
				AuditItemDetailInfo.class, orderCount);
		List<AuditItemDetailInfo> orderInfoList = new ArrayList();
		for(AuditItemDetailInfo item : orderInfos){
			if(null != item.getRemark()&& !("".equals(item.getRemark()))){
				orderInfoList.add(item);
			}
		}
		return orderInfoList;
	}
	
	public String queryAuditInfo() {
		String action = (String)super.getParams().get("action");
//		 TMC订单记录查询
        HOP_TMC_ORDER = URLClient.HOP_TMC_ORDER;
		if(!StringUtil.isValidStr(theAuditDate))//对日期减一操作
			theAuditDate = DateUtil.dateToString(new Date());
		if("1".equals(action)){
			//根据酒店和日期查
			hotelAudits = dailyAuditService.queryAuditInfo(theHotelName.trim(),DateUtil.addStringDateALL(theAuditDate, -1),theOrderCd.trim(),1);
		}else{
			//根据订单号查
			hotelAudits = dailyAuditService.queryAuditInfo(theHotelName.trim(),DateUtil.addStringDateALL(theAuditDate, -1),theOrderCd.trim(),2);
		}
		if(hotelAudits.isEmpty())
			request.setAttribute("result", "查询无记录,请检查酒店名称和日期或订单编号是否有误!");
		forward = "toQueryAuditInfo";
		return forward;
	}
	/**
	 * 保存审核信息
	 * @return
	 */
	public String saveAuditInfo(){
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		//取得页面上更新的房间数据
		Map params = super.getParams();
		Map<Long,DaAuditRoomDetailInfo> roomInfosMap = getRoomInforFromPage(params,true);
		int ret = 1;
		String operaMsg = null;
		if (!roomInfosMap.isEmpty()) {
			ret = dailyAuditService.saveAuditInfo(user.getName(),roomInfosMap);
		}
		if(0 == ret)
			operaMsg = "操作成功";
		else if(1 == ret)
			operaMsg = "操作失败,无可更新数据或订单无效!";
		else
			operaMsg = "财务已获取,无法更新!";
		request.setAttribute("operaMsg", operaMsg);
		forward = "toSaveAuditInfo";
		return forward;
	}

	/**
	 * 批量交接日审 addby juesuchen
	 * 
	 * @return
	 */
	public String batchAssignAudit() {
		int assignType = Integer.parseInt((String)super.getParams().get("assignType"));
		request.setAttribute("assignType", assignType);
		//审核组的 : 1 回访组的 : 2
		userList = workStatesManager.lstAuditWorkersByGroup(assignType, true);
		forward = "batchAssignAudit";
		return forward;
	}

	public String batchAssignAuditOK() {
		user = getOnlineWorkStates();
		if (null == user)
			forwardMsg("您未登陆，不能进行操作！");
		//取得页面上更新的房间数据
		String assignToId = (String)super.getParams().get("assignToId");
		String assignToName = (String)super.getParams().get("assignToName");
		int assignType = Integer.parseInt((String)super.getParams().get("assignType"));
		dailyAuditService.assignAuditByMid(assignToId,assignToName,selIDs,
				user.getLogonId(),user.getName(),assignType);
		request.setAttribute("operaMsg", "分配成功！");
		forward = "batchAssignAudit";
		return forward;
	}
	
	/**
	 * 查看日审操作日志 addby juesuchen
	 * @return
	 */
	public String queryAuditLog() {
		request.setAttribute("orderType", super.getParams().get("orderType"));
		forward = "queryAuditLog";
		return forward;
	}
	
	/**
	 * 将格林豪泰的结果覆盖当前日审记录
	 * add by zengyong 2009-12-21
	 * @param detailInfos
	 */
	public void setDailyAuditResultToDetails(List<DaAuditDetailInfo> detailInfos){
		if(null!=detailInfos&&detailInfos.size()>0){
			Iterator<DaAuditDetailInfo> infoLst = detailInfos.iterator();
			 DailyAuditExResponse dailyAuditExRes = null;//response
		     DailyAuditExRequest dailyAuditExReq = new DailyAuditExRequest();//request

		     
			while(infoLst.hasNext()){
				
				DaAuditDetailInfo detailInfo = (DaAuditDetailInfo)infoLst.next();
				
				/*****************************入住日审开始*****************************************************************/
				List<DaDailyauditItem> daItemLst =  detailInfo.getCheckInItems();//入住日审记录
				if(null!=daItemLst && daItemLst.size()>0){//审入住
					Iterator<DaDailyauditItem> daItemIters = daItemLst.iterator();
					while(daItemIters.hasNext()){
						List<MGExDailyAudit> daList = null;
						DaDailyauditItem daItem = daItemIters.next();
						dailyAuditExReq.setChainCode("COL");//供应商
						dailyAuditExReq.setChannelCode(5);//格林豪泰
						dailyAuditExReq.getOrderCode().add(daItem.getOrderCd());
						dailyAuditExReq.setAuditDate("IN");//IN或OUT
				        try {
				        	dailyAuditExRes = hdlService.dailyAudit(dailyAuditExReq);
				        } catch (Exception e) {
				            // TODO Auto-generated catch block
				        	log.info("格林豪泰日审错误:"+e.getMessage());
				        }
				        if (null == dailyAuditExRes) {
				            // 如果日审为空,
				            daList = null;

				        } else {
				            if (null == dailyAuditExRes.getDailyAudits()) {
				                daList = null;
				            } else {
				                daList = dailyAuditExRes.getDailyAudits();
				               
				            }
				        }//end if
				        if(null!=daList && daList.size()>0){
				        	try{
				        	convertDailyAuditFromGLHT(daItem,daList.get(0));
				        	}catch(Exception ex){
				        		log.info("格林豪泰日审结果转换成日审记录错误:"+ex.getMessage());
				        	}
				        }

					}//end while
				}
			    /*******************入住日审结束******************************/

	
				/*****************************退房日审开始*****************************************************************/
				List<DaDailyauditItem> daItemLst_out =  detailInfo.getCheckOutItems();//退房日审记录
				if(null!=daItemLst_out && daItemLst_out.size()>0){//审退房
					Iterator<DaDailyauditItem> daItemIters = daItemLst_out.iterator();
					while(daItemIters.hasNext()){
						List<MGExDailyAudit> daList = null;
						DaDailyauditItem daItem = daItemIters.next();
						dailyAuditExReq.setChainCode("COL");//供应商
						dailyAuditExReq.setChannelCode(5);//格林豪泰
						dailyAuditExReq.getOrderCode().add(daItem.getOrderCd());
						dailyAuditExReq.setAuditDate("OUT");//IN或OUT
				        try {
				        	dailyAuditExRes = hdlService.dailyAudit(dailyAuditExReq);
				        } catch (Exception e) {
				            // TODO Auto-generated catch block
				        	log.info("格林豪泰日审错误:"+e.getMessage());
				        }
				        if (null == dailyAuditExRes) {
				            // 如果日审为空,
				            daList = null;

				        } else {
				            if (null == dailyAuditExRes.getDailyAudits()) {
				                daList = null;
				            } else {
				                daList = dailyAuditExRes.getDailyAudits();
				               
				            }
				        }//end if
				        if(null!=daList && daList.size()>0){
				        	try{
				        	convertDailyAuditFromGLHT(daItem,daList.get(0));
				        	}catch(Exception ex){
				        		log.info("格林豪泰日审结果转换成日审记录错误:"+ex.getMessage());
				        	}
				        }
					}//end while
				}
			    /*******************退房日审结束******************************/
				
			}
		}
	}
	
	
	/**
	 * 将GLHT的日审结果赋值给DailyAudit
	 * @param daItem
	 * @param da
	 */
	public void convertDailyAuditFromGLHT(DaDailyauditItem daItem ,MGExDailyAudit da)throws Exception{
		if(null==daItem||null==daItem.getDaDailyauditItemSubtables()){
			return;
		}
		if(null==da||null==da.getOrderItem()){
			return;
		}
		
		//日审记录明细
		Set<DaDailyauditItemSubtable> itemSubSet = daItem.getDaDailyauditItemSubtables();
		List<DaDailyauditItemSubtable> listItemSub = new ArrayList(itemSubSet);
		
		//GLHT返回的查询结果
		List<MGExAuditOrderItem> mgExItemList = da.getOrderItem();
		
		for(int k=0;k<listItemSub.size();k++){
			MGExAuditOrderItem mgExItem = mgExItemList.get(k);
			DaDailyauditItemSubtable itemSub = listItemSub.get(k);
			itemSub.setActualcheckinname(mgExItem.getGuests());//入住人
			itemSub.setRoomnumber(mgExItem.getRoomNo());//房间号
			itemSub.setAuditresults(String.valueOf(mgExItem.getNoteResult()));//审核结果
		}
	}
	
	public IDailyAuditService getDailyAuditService() {
		return dailyAuditService;
	}

	public void setDailyAuditService(IDailyAuditService dailyAuditService) {
		this.dailyAuditService = dailyAuditService;
	}

	public List<DaAuditDetailInfo> getAuditDetails() {
		return auditDetails;
	}

	public HtlChannelDetailInfo getChannelDetail() {
		return channelDetail;
	}

	public void setChannelDetail(HtlChannelDetailInfo channelDetail) {
		this.channelDetail = channelDetail;
	}

	public int getRoomNumCount() {
		return roomNumCount;
	}

	public void setRoomNumCount(int roomNumCount) {
		this.roomNumCount = roomNumCount;
	}

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(String delayTime) {
		this.delayTime = delayTime;
	}

	public void setAuditDetails(List<DaAuditDetailInfo> auditDetails) {
		this.auditDetails = auditDetails;
	}

	public DaPersonalWorkload getMyWorkload() {
		return myWorkload;
	}

	public void setMyWorkload(DaPersonalWorkload myWorkload) {
		this.myWorkload = myWorkload;
	}

	public List getAuditWorkload() {
		return auditWorkload;
	}

	public void setAuditWorkload(List auditWorkload) {
		this.auditWorkload = auditWorkload;
	}

	public List getUserList() {
		return userList;
	}

	public void setUserList(List userList) {
		this.userList = userList;
	}

	public DaDailyaudit getAudit() {
		return audit;
	}

	public void setAudit(DaDailyaudit audit) {
		this.audit = audit;
	}

	public List<ChannelInfo> getChannelsInfo() {
		return channelsInfo;
	}

	public void setChannelsInfo(List<ChannelInfo> channelsInfo) {
		this.channelsInfo = channelsInfo;
	}

	public List<HotelAuditDetail> getHotelAudits() {
		return hotelAudits;
	}

	public void setHotelAudits(List<HotelAuditDetail> hotelAudits) {
		this.hotelAudits = hotelAudits;
	}

	public String getTheAuditDate() {
		return theAuditDate;
	}

	public void setTheAuditDate(String theAuditDate) {
		this.theAuditDate = theAuditDate;
	}

	public String getTheHotelName() {
		return theHotelName;
	}

	public void setTheHotelName(String theHotelName) {
		this.theHotelName = theHotelName;
	}

	public String getTheOrderCd() {
		return theOrderCd;
	}

	public void setTheOrderCd(String theOrderCd) {
		this.theOrderCd = theOrderCd;
	}

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}

	public List getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List orderItems) {
		this.orderItems = orderItems;
	}

	public VOrOrder getVOrOrder() {
		return vOrOrder;
	}

	public void setVOrOrder(VOrOrder orOrder) {
		vOrOrder = orOrder;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public String getSelIDs() {
		return selIDs;
	}

	public void setSelIDs(String selIDs) {
		this.selIDs = selIDs;
	}

	public IHDLService getHdlService() {
		return hdlService;
	}

	public void setHdlService(IHDLService hdlService) {
		this.hdlService = hdlService;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public String getEarlyCheckInDate() {
		return earlyCheckInDate;
	}

	public void setEarlyCheckInDate(String earlyCheckInDate) {
		this.earlyCheckInDate = earlyCheckInDate;
	}

	public String getLateCheckInDate() {
		return lateCheckInDate;
	}

	public void setLateCheckInDate(String lateCheckInDate) {
		this.lateCheckInDate = lateCheckInDate;
	}

	public String getEarlyCheckOutDate() {
		return earlyCheckOutDate;
	}

	public void setEarlyCheckOutDate(String earlyCheckOutDate) {
		this.earlyCheckOutDate = earlyCheckOutDate;
	}

	public String getLateCheckOutDate() {
		return lateCheckOutDate;
	}

	public void setLateCheckOutDate(String lateCheckOutDate) {
		this.lateCheckOutDate = lateCheckOutDate;
	}

	public IExMappingService getExService() {
		return exService;
	}

	public void setExService(IExMappingService exService) {
		this.exService = exService;
	}

	public long getChannelType() {
		return channelType;
	}

	public void setChannelType(long channelType) {
		this.channelType = channelType;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
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
	
}
