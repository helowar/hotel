package com.mangocity.hotel.order.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mangocity.hotel.base.web.TranslateUtil;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.DailyAuditConstant;
import com.mangocity.hotel.order.constant.MemberConfirmSmsStutas;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.dao.IOrDailyAuditDao;
import com.mangocity.hotel.order.persistence.DaAuditingWorkload;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaDailyauditItem;
import com.mangocity.hotel.order.persistence.DaDailyauditItemSubtable;
import com.mangocity.hotel.order.persistence.DaPersonalWorkload;
import com.mangocity.hotel.order.persistence.DaReturnvisit;
import com.mangocity.hotel.order.persistence.HtlAuditInfo;
import com.mangocity.hotel.order.persistence.HtlAuditInfoSetup;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.persistence.view.AuditResult;
import com.mangocity.hotel.order.service.IAuditOrderService;
import com.mangocity.hotel.order.service.IDailyAuditService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.assistant.AuditItemDetailInfo;
import com.mangocity.hotel.order.service.assistant.DaAuditDetailInfo;
import com.mangocity.hotel.order.service.assistant.DaAuditRoomDetailInfo;
import com.mangocity.hotel.order.service.assistant.DaOrderAudit;
import com.mangocity.hotel.order.service.assistant.DaReturnRoomDetail;
import com.mangocity.hotel.order.service.assistant.DaSubItem;
import com.mangocity.hotel.order.service.assistant.HotelAuditDetail;
import com.mangocity.hotel.order.service.assistant.HtlChannelDetailInfo;
import com.mangocity.hotel.order.service.assistant.ReturnInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Sms;

/**
 * 日审重构 service addby juesuchen
 */
public class DailyAuditServiceImpl implements IDailyAuditService {
	 private static final MyLog log = MyLog.getLogger(DailyAuditServiceImpl.class);
	/**
	 * 日审操作DAO
	 */
	private IOrDailyAuditDao orDailyAuditDao;
	/**
     * message接口
     */
    private CommunicaterService communicaterService;
    /**
     * 订单服务接口
     */
    private IOrderService orderService;
	/**
	 * 日审service
	 */
	private IAuditOrderService auditOrderService;
    
    /**
     * 会员转换辅助
     */
    protected TranslateUtil translateUtil;

    private DAOIbatisImpl queryDao;
    
	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	public IOrDailyAuditDao getOrDailyAuditDao() {
		return orDailyAuditDao;
	}

    public void setOrDailyAuditDao(IOrDailyAuditDao orDailyAuditDao) {
        this.orDailyAuditDao = orDailyAuditDao;
    }
    //==========================================回访=======begin===============================================
    
    /**
	 * 获得回访数据
	 * @param returnVisitID
	 * @return
	 */
	public DaReturnvisit getReturnvisit(long  returnVisitID) {
		DaReturnvisit returnvisit = orDailyAuditDao.queryDaReturnvisitById(DaReturnvisit.class, returnVisitID);
		return returnvisit;
	}
	
	/**
	 * 通过回访ID得到订单对象
	 * @param orderID
	 * @param orderType
	 * @return
	 */
	private VOrOrder getViewOrder(Long orderID, int orderType) {
        List<VOrOrder> res = orDailyAuditDao.queryByNamedQuery("hQueryOrder_audit", new Object[] { orderID,
            orderType });
        return null != res && 0 < res.size()?(VOrOrder) res.get(0):null;
    }
    /**
	 * 封装所要查看的回访记录
	 * @param returnVisitID 回访ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReturnInfo> getReturnData(long returnVisitID) {
		//查出回访ID相同的所有日审明细
		List<DaDailyauditItem> itemList = orDailyAuditDao.queryDaDailyauditItemById(returnVisitID);
		List<ReturnInfo> auditList = new ArrayList<ReturnInfo>();
		ReturnInfo returnInfo;
		for(DaDailyauditItem dailyaudit : itemList){
			returnInfo = null;
			//通过回访ID得到订单对象
			VOrOrder order = this.getViewOrder(dailyaudit.getOrderid(), dailyaudit.getOrderType());
			if (null == order){
				continue;
			}
			for(ReturnInfo rInfo : auditList){
				if(rInfo.hotelID.equals(order.getHotelId())){
					returnInfo = rInfo;
					break;
				}
			}
			//如果是CC订单，则读取订单日审备注
			if(1 == dailyaudit.getOrderType()){
				dailyaudit.setRemark(order.getAuditRemark());
			}
			if(null != returnInfo){
				returnInfo.getReturnDateList().add(dailyaudit);//日审明细
			}else{
				returnInfo = new ReturnInfo();
				returnInfo.setHotelID(order.getHotelId());//酒店ID
				returnInfo.setHotelName(order.getHotelName());//酒店名称
				returnInfo.setOrderCD(order.getOrderCD());//订单号码
				returnInfo.getReturnDateList().add(dailyaudit);//日审明细
				auditList.add(returnInfo);
			}
		}
		return auditList;
	}
	
	/**
	 * 手动获取一条回访记录到我的工作档案中
	 * @param loginID 登录号
	 * @param userName 登录人姓名
	 * @param returnVisitID
	 * @return
	 */
	public boolean achieveRetrunForUser(String loginID, String userName,String returnVisitID) {
		DaReturnvisit returnVisit = (DaReturnvisit) orDailyAuditDao.queryDaReturnvisitById(DaReturnvisit.class, Long.valueOf(returnVisitID));
		//获取状态为 未获取
		if(returnVisit.getAquirestate() != DailyAuditConstant.NOTYETGOTAUDIT){
			return false;
		}else{
			returnVisit.setAcquireid(loginID); //获取人ID
			returnVisit.setAcquirename(userName); //获取人姓名
			returnVisit.setAcquiretime(new Date()); //获取时间
			returnVisit.setAquireway(DailyAuditConstant.GETAUDIT_BYHAND);//获取方式
			returnVisit.setAquirestate(DailyAuditConstant.HADGOTAUDIT);//获取状态
			orDailyAuditDao.saveOrUpdateDaReturnvisit(returnVisit); //更新数据
			return true;
		}
		
	}
	
	
	
    /**
	 * 为用户自动分配一条回访记录,如所有记录已分完毕则返回 false
	 * @param userName 登录人姓名
	 * @param loginId 登录人ID
	 * @return
	 */
	public boolean autoAllotReturnForUser(String loginId, String userName) {
		// 查询用户是否已有自动分配的记录,无则分配 
        if (!orDailyAuditDao.checkUserhasAutoReturnRecord(loginId)) {
        	// 先分配有预约的
            if (!allotReturnRecordForUserByType(loginId, userName,
                   DailyAuditConstant.DELAY_RETURN_TYPE)) {
            	// 如无则分配随机的
                if (!allotReturnRecordForUserByType(loginId, userName,
                        DailyAuditConstant.RADOM_RETURN_TYPE)) {
                    // 如无则说明日审记录已全部分配完
                    return false;
                }
            }
        }
		return true;
	}
	
    /**
	 * 为用户自动分配一条回访记录
	 * @param loginId
	 * @param userName
	 * @param delayAuditType
	 * @return 如未成功分配则返回false
	 */
    private boolean allotReturnRecordForUserByType(String loginId, String userName,
            int allotAuditType) {
    	DaReturnvisit returnvisit = orDailyAuditDao.allotReturnRecordForUserByType(
                loginId, userName, allotAuditType);
        if (null != returnvisit) {
        	// 设置获取人姓名,Id,时间,获取状态,获取方式
    		setUpAccquirerInfo_Return(returnvisit, loginId, userName,
    				DailyAuditConstant.GETAUDIT_AUTO, false);
        	orDailyAuditDao.sameContactData(returnvisit);

            return true;
        }
        return false;
    }
    /**
	 * 置获取人,交接人,姓名,Id,时间,获取状态,获取方式,

	 * @param audit
	 * @param loginId
	 *            获取ID
	 * @param name
	 *            获取姓名
	 * @param aquireWay
	 *            获取方式
	 * @param isNeedToDeliver
	 *            是否需要交接
	 */
    private void setUpAccquirerInfo_Return(DaReturnvisit returnvisit, String loginId,
            String name, int aquireWay, boolean isNeedToDeliver) {
        if (isNeedToDeliver) {// 需要设置交接人信息,即现在的获取人信息
        	returnvisit.setDeliverid(returnvisit.getAcquireid());//交接人ID
        	returnvisit.setDelivername(returnvisit.getAcquirename());//交接人姓名
        	returnvisit.setDelivertime(returnvisit.getAcquiretime());//交接时间
        }
        // 设置获取人信息
        returnvisit.setAcquireid(loginId);//获取人ID
        returnvisit.setAcquirename(name);//获取人姓名
        returnvisit.setAcquiretime(new Date());//获取人时间
        returnvisit.setAquirestate(DailyAuditConstant.HADGOTAUDIT);//获取状态
        returnvisit.setAquireway(aquireWay);//获取方式
    }
    
    
    
    //==========================================回访==============end========================================    
    
    
    /**
	 * 为用户自动分配一条审核记录,如所有记录已分完毕则返回 false
	 * addby juesuchen
	 * @param Name
	 * @param loginId
	 * @return
	 */
	public boolean autoAllotAuditForUser(String loginId, String name) {
		// 查询用户是否已有自动分配的记录,无则分配
		if (!orDailyAuditDao.checkUserhasAutoAuditRecord(loginId)) {
			// 先分配有预约的
			if (!allotAuditRecordForUserByType(loginId, name,
					DailyAuditConstant.DELAY_AUDIT_TYPE)) {
				// 如无则分配回传的
				if (!allotAuditRecordForUserByType(loginId, name,
						DailyAuditConstant.RETURN_AUDIT_TYPE)) {
					// 如无则分配随机的
					if (!allotAuditRecordForUserByType(loginId, name,
							DailyAuditConstant.RADOM_AUDIT_TYPE)) {
						// 如无则说明日审记录已全部分配完
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 为用户自动分配一条审核记录 addby juesuchen
	 * 
	 * @param loginId
	 * @param name
	 * @param delayAuditType
	 * @return 如未成功分配则返回false
	 */
	private boolean allotAuditRecordForUserByType(String loginId, String name,
			int allotAuditType) {
		DaDailyaudit audit = orDailyAuditDao.allotAuditRecordForUserByType(
				loginId, name, allotAuditType);
		if (null != audit) {
			// 设置获取人姓名,Id,时间,获取状态,获取方式
			setUpAccquirerInfo(audit, loginId, name,
					DailyAuditConstant.GETAUDIT_AUTO, false);
			// 更新到数据库
			updateDaDailyauditBatchInSameChannel(audit);
			return true;
		}
		return false;
	}
	/**
	 * 把同一个渠道的所有未完成的都分给同一个人
	 * @param audit
	 */
	private void updateDaDailyauditBatchInSameChannel(DaDailyaudit audit) {
		orDailyAuditDao.updateDaDailyauditBatchInSameChannel(audit);
	}

	/**
	 * 置获取人,交接人,姓名,Id,时间,获取状态,获取方式, addby juesuchen addby juesuchen
	 * 
	 * @param audit
	 * @param loginId
	 *            获取ID
	 * @param name
	 *            获取姓名
	 * @param aquireWay
	 *            获取方式
	 * @param isNeedToDeliver
	 *            是否需要交接
	 */
	private void setUpAccquirerInfo(DaDailyaudit audit, String loginId,
			String name, int aquireWay, boolean isNeedToDeliver) {
		if (isNeedToDeliver && StringUtil.isValidStr(audit.getAcquireid())) {// 需要设置交接人信息,即现在的获取人信息
			audit.setDeliverid(audit.getAcquireid());
			audit.setDelivername(audit.getAcquirename());
			audit.setDelivertime(new Date());
		}
		// 设置获取人信息
		audit.setAcquireid(loginId);
		audit.setAcquirename(name);
		audit.setAcquiretime(new Date());
		audit.setAquireState(DailyAuditConstant.HADGOTAUDIT);
		audit.setAquireWay(aquireWay);
	}

	/**
	 * 清除用户信息 完成和延时的操作
	 * 
	 * @param audit
	 */
	private void removeUserInfo(DaDailyaudit audit) {
		audit.setAcquireid(null);
		audit.setAcquirename(null);
		audit.setAcquiretime(null);
		// 设置未获取
		audit.setAquireState(DailyAuditConstant.NOTYETGOTAUDIT);
		audit.setAquireWay(DailyAuditConstant.GETAUDIT_AUTO);
	}

	/**
	 * 根据Id 查找一个实体对象 addby juesuchen
	 * 
	 * @param id
	 * @return
	 */
	public DaDailyaudit findDaDailyauditById(Serializable id) {
		return orDailyAuditDao.queryDaDailyauditById(DaDailyaudit.class, id);
	}

	/**
	 * 查找一个渠道实体
	 * 
	 * @param sid
	 * @return
	 */
	public HtlAuditInfo findHtlAuditInfoById(Serializable htlAuditInfoSid) {
		return orDailyAuditDao.queryHtlAuditInfoById(HtlAuditInfo.class, htlAuditInfoSid);
	}

	/**
	 * 保存一个实体对象 addby juesuchen
	 * 
	 * @param audit
	 */
	public void updateDaDailyaudit(DaDailyaudit audit) {
		orDailyAuditDao.saveOrUpdateDaDailyaudit(audit);
		//orDailyAuditDao.getHibernateTemplate().flush();
	}

	/**
	 * 根据日审总表ID取得明细数据 addby juesuchen
	 */
	public List<DaAuditDetailInfo> getAuditDetailsByDaDailyaudit(
			DaDailyaudit audit,Object theHotelId) {
		Map<Long, DaAuditDetailInfo> hotelAuditInfoMap = new HashMap<Long, DaAuditDetailInfo>();
		List<DaAuditDetailInfo> auditDetails = new ArrayList<DaAuditDetailInfo>();
		if(null == audit.getDaDailyauditItems())
			return auditDetails;
		for (DaDailyauditItem item : audit.getDaDailyauditItems()) {
			if(null != theHotelId && (Long.valueOf((String)theHotelId).longValue() != item.getHotelid().longValue())){
				//不为空，说明查看指定酒店的审核记录，所以不是这个酒店的就会过滤掉
				continue;
			}
			DaAuditDetailInfo auditInfo = hotelAuditInfoMap.get(item.getHotelid());
			if (null == auditInfo) {// 如果MAP不存在这个元素,则加入
				auditInfo = new DaAuditDetailInfo();
				// 设置把酒店信息
				auditInfo.setHotelId(item.getHotelid());
				String htlName = orDailyAuditDao.getHotelNameById(item.getHotelid());
				auditInfo.setHotelName(htlName);
				hotelAuditInfoMap.put(item.getHotelid(), auditInfo);
			}
			int auditType = item.getAudittype();
			//把订单的确认号带出
			Object[] results= orDailyAuditDao.getHotelComfirmByOrderId(item.getOrderid(),item.getOrderType());
			if(results!=null && results.length > 0){
				String confirNos = (String)results[0];
				String roomNos = (String)results[1];
				if(StringUtil.isValidStr(confirNos) || StringUtil.isValidStr(roomNos))
					initComfirNosAndRoomNos(item,confirNos,roomNos);
				if(1 == item.getOrderType())//目前只取CC的订单中的备注
				{	
					item.setRemark((String)results[2]);
				}
				
			}
			
			if (auditType == DailyAuditConstant.CHECKINTYPE) {// 如果是入住审核
				auditInfo.getCheckInItems().add(item);
			} else {// 否则是退房审核
				auditInfo.getCheckOutItems().add(item);
			}
		}
		// 些时hotelAuditInfoMap 里面装有所有已封装好的 DaAuditDetailInfo
		for (Iterator<Long> htlIdsIter = hotelAuditInfoMap.keySet().iterator(); htlIdsIter
				.hasNext();) {
			Long htlId = htlIdsIter.next();
			DaAuditDetailInfo tempInfor = hotelAuditInfoMap.get(htlId);
			tempInfor.sort();
			auditDetails.add(tempInfor);
		}
		return auditDetails;
	}
	/**
	 * 初始化房间号和确认号，以/分开
	 * @param item
	 * @param confirNos
	 * @param roomNos
	 */
	private void initComfirNosAndRoomNos(DaDailyauditItem item, String confirNos, String roomNos) {
		String[] comfirs = new String[0];
		String[] rooms = new String[0];;
		if(StringUtil.isValidStr(confirNos)){
			if(confirNos.contains(","))
				comfirs = confirNos.split(",");
			else
				comfirs = confirNos.split("/");
		}
		if(StringUtil.isValidStr(roomNos)){
			if(roomNos.contains(","))
				rooms = roomNos.split(",");
			else
				rooms = roomNos.split("/");
		}
		Set<DaDailyauditItemSubtable> subs = item.getDaDailyauditItemSubtables();
		int subIndex = 0;
		for (DaDailyauditItemSubtable sub : subs) {
			 if(subIndex < comfirs.length){
				 sub.setAffirmnumber(comfirs[subIndex]);
			 }
			 if(subIndex < rooms.length){
				 sub.setRoomnumber(rooms[subIndex]);
			 }
			 subIndex ++;
		}
	}
	
	 /**
	  * 根据渠道记录返回当天渠道明细对象
	  * @param htlAudit
	  * @param auditDate
	  * @param auditId
	  */
	public HtlChannelDetailInfo getChannelDetailsByHtlAuditInfo(
			HtlAuditInfo htlAudit, Date auditDate,Long auditId) {
		auditDate = DateUtil.getDate(auditDate, 1);//与酒店审核信息设置页面同步时间
		HtlChannelDetailInfo channelDetail = new HtlChannelDetailInfo();
		// 得到渠道的一些配置信息
		List<HtlAuditInfoSetup> lstSetup = htlAudit.getLstSetup();
		for (HtlAuditInfoSetup htlSetup : lstSetup) {
			// 如果日期符合
			if (DateUtil.between(auditDate, htlSetup.getAuditBeginDate(),
					htlSetup.getAuditEndDate())) {
				// 而且星期也符合,则说明当天是这条配置信息
				String setupWeeks = htlSetup.getWeeks();
				if(null == setupWeeks)
					setupWeeks = "1,2,3,4,5,6,7";
				String[] weeks = setupWeeks.split(",");
				if (DateUtil.isMatchWeek(auditDate, weeks)) {
					// 设置联系号码,备注,传真号码,部门,审核方式
					channelDetail.setAuditApartM(htlSetup.getAuditApartM());
					channelDetail.setAuditCtPhone(htlSetup.getAuditCtPhone());
					channelDetail.setAuditNo(htlSetup.getAuditNo());
					channelDetail.setAuditRemark(htlSetup.getAuditRemark());
					channelDetail.setAuditType(htlSetup.getAuditType());
					//根据日审ID找得酒店回复URL
					String url = orDailyAuditDao.getHotelReturnURL(auditId);
					channelDetail.setReturnURL(url);
					break;
				}
			}
		}
		return channelDetail;
	}
	
	/**
	 * 清除回访表获取人ID
	 * @param returnID 回访ID
	 */
	public DaReturnvisit clearAcquireInfo(String returnID){
		DaReturnvisit returnvisit = (DaReturnvisit) orDailyAuditDao.queryDaReturnvisitById(DaReturnvisit.class, Long.valueOf(returnID));
		returnvisit.setAcquireid("");//获取人ID
	    //获取人姓名放到交接人姓名里
		returnvisit.setDelivername(returnvisit.getAcquirename());
		returnvisit.setDelivertime(new Date());//设置释放时间
		returnvisit.setAcquirename("");//获取人姓名
		// 设置未获取
		returnvisit.setAquirestate(DailyAuditConstant.NOTYETGOTAUDIT);
		returnvisit.setAquireway(DailyAuditConstant.GETAUDIT_AUTO);
		
		return returnvisit;
	}
	
	/**
     * 更新回访数据
     * @param orderMount 订单量
     * @param roomMount 房间量
     * @param delayTime 延长时间
     * @param relaxReason 延时理由
     * @param returnID 回访ID
     * @param loginID 登录人ID
     * @param userName 登录人姓名
     * @param roomInfos  
     * @param operateType 操作类型
     */
    @SuppressWarnings("unchecked")
	public void updateReturnData(String orderMount,String roomMount,String delayTime,String relaxReason,String returnID,String loginID,String userName,List roomInfos, int operateType,boolean userState){
    	//数据表里的回访完成时间
    	//完成
    	if (DailyAuditConstant.COMPLETE_OPERATION == operateType) {
    		//更新子表信息
    		updateSubItemInReturn(roomInfos,loginID,userName);
    		
    		//清除获取人信息
    		DaReturnvisit returnvisit = clearAcquireInfo(returnID);
    		returnvisit.setReturnstate(DailyAuditConstant.RETURNSTATE_ACHIEVE);//回访状态（完成）
    		returnvisit.setReturnvisitidate(new Date());//回访完成时间
    		//更新回访数据
    		orDailyAuditDao.saveOrUpdateDaReturnvisit(returnvisit);
    		updateOrderItemAndWorkingLoad(returnvisit,loginID,userName,operateType);
    		//更新以回访的订单为"以统计"

    		//回访完成后检查是否有可自动分配的回访数据
        	if(userState)
        		autoAllotReturnForUser(loginID,userName);
    		
    	}
    	//保存
    	if(DailyAuditConstant.SAVE_OPERATION == operateType){
//    		更新子表信息
    		updateSubItemInReturn(roomInfos,loginID,userName);
    		DaReturnvisit returnvisit = (DaReturnvisit) orDailyAuditDao.queryDaReturnvisitById(DaReturnvisit.class, Long.valueOf(returnID));
    		updateOrderItemAndWorkingLoad(returnvisit,loginID,userName,operateType);
    	}
    	//释放
    	if (DailyAuditConstant.RELEASE_OPERATION == operateType){
    		updateSubItemInReturn(roomInfos,loginID,userName);
    		DaReturnvisit returnvisit = (DaReturnvisit) orDailyAuditDao.queryDaReturnvisitById(DaReturnvisit.class, Long.valueOf(returnID));
    		updateOrderItemAndWorkingLoad(returnvisit,loginID,userName,operateType);
    		int delayT = Integer.parseInt(delayTime);
			Calendar can = Calendar.getInstance();
			can.add(Calendar.MINUTE, delayT);// 加上分钟数
			Date reSignTime = can.getTime();
			//清除获取人信息
			returnvisit = clearAcquireInfo(returnID);
			returnvisit.setReassigntime(reSignTime);//再分配时间
			returnvisit.setDelaytime(delayTime);//延长时间
			//延时原因
			int releaseReason = Integer.parseInt(relaxReason);
			returnvisit.setReleasereason(releaseReason);
    		orDailyAuditDao.saveOrUpdateDaReturnvisit(returnvisit);
    	}
    }
    private void updateSubItemInReturn(List roomInfos, String loginID, String userName) {
    	for (int i=0;i<roomInfos.size();i++) {
			DaReturnRoomDetail roomDetail = (DaReturnRoomDetail)roomInfos.get(i);
			DaDailyauditItemSubtable itemSub = orDailyAuditDao.queryDaDailyauditItemSubtableById(DaDailyauditItemSubtable.class, roomDetail.getID());
			itemSub.setActualcheckinname(roomDetail.getActualcheckinname());//实际入住人
			itemSub.setRoomnumber(roomDetail.getRoomnumber());//房间号
			itemSub.setReciprocal(roomDetail.getReturnresults());//回访结果
			itemSub.setReturnvisitid(loginID);//回访人ID
			itemSub.setReturnvisitname(userName);//回访人姓名
			itemSub.setReturnvisittime(new Date());//回访时间
			if("2".equals(roomDetail.getReturnresults())){//回房结果为noshow时才写原因
				itemSub.setNoshowCode(roomDetail.getNoshowCode());//noshow一级目录
				itemSub.setNoshow(roomDetail.getNoshow());//noshow原因
				itemSub.setNoshEdit(roomDetail.getNoshEdit());//noshow其它原因
			}else{//否则清空
				itemSub.setNoshowCode(null);//noshow一级目录
				itemSub.setNoshow(null);//noshow原因
			}
			itemSub.setRtAdvancecheckouttime(roomDetail.getAdvancecheckouttime());//提前退房时间(回访)
			//itemSub.setAdvancecheckouttime(roomDetail.getAdvancecheckouttime());//提前退房时间
			itemSub.setAffirmnumber(roomDetail.getAffirmnumber());//确认号
			//理新日审明细数据
			orDailyAuditDao.saveOrUpdateDaDailyauditItemSubtable(itemSub);
			
			// add by diandian.hou 2011-09-06
			DaDailyauditItemSubtable itemSubForCheckOut = orDailyAuditDao.queryAuditItemSubForCheckOut(itemSub.getID());
			if(itemSubForCheckOut != null){
				itemSubForCheckOut.setRoomnumber(itemSub.getRoomnumber());
				itemSubForCheckOut.setActualcheckinname(itemSub.getActualcheckinname());
				itemSubForCheckOut.setAffirmnumber(itemSub.getAffirmnumber());
				orDailyAuditDao.saveOrUpdateDaDailyauditItemSubtable(itemSubForCheckOut);
			}
		}
	}

	/**
     * 回访更新orderItem和工作量
     * @param  returnvisit
     * @param userName
     * @param userName 
     * @param operateType
     */
    private void updateOrderItemAndWorkingLoad(DaReturnvisit  returnvisit, String loginID, String userName, int operateType) {
    	// 回访完成时更新OrderItem表
    	String returnID = String.valueOf(returnvisit.getID());
		updateOrderItemInReturn( returnID,userName);//回访时更新orderitem表
		//取得完成的工作量
		Map<String,Long> datas = getCompleteDatas(null, returnID,operateType);
		Long htlCount = datas.get("hotelCount");
		Long orderCount = datas.get("orderCount");
		Long roomCount = datas.get("roomCount");
		if(htlCount + orderCount + roomCount == 0)
			return;//表明无更新
		long costTime = new Date().getTime()- returnvisit.getAcquiretime().getTime();
		//更新个人工作量表
		updatePersonel(loginID,htlCount, orderCount, roomCount,costTime,DailyAuditConstant.RETURNVISIT_TYPE);
		//更新工作量总表
		updateAllWorkingLoad(returnvisit.getAuditordate(),htlCount, orderCount,roomCount,DailyAuditConstant.RETURNVISIT_TYPE);
	}

	/**
     * 回访时更新orderitem表
     * @param returnID
     * @param userName 
     */
    private void updateOrderItemInReturn(String returnID, String userName) {
    	
		List<DaDailyauditItem> itemList = orDailyAuditDao.queryDaDailyauditItemByreturnID(Long.valueOf(returnID));
    	for(DaDailyauditItem dailyanuditem : itemList){
			if(!checkTheOrderDone(dailyanuditem.getDaDailyauditItemSubtables(),DailyAuditConstant.RETURNVISIT_TYPE,dailyanuditem.getAudittype())){
				//如果这张单不是所有结果已有,则不更新先
				continue;
			}
			Long orderId = dailyanuditem.getOrderid();//订单ID
			int orderType = dailyanuditem.getOrderType();//订单类型
			int auditType = dailyanuditem.getAudittype();//回访类型
			
			Date night = null;
			AuditResult[] results = null;
			int index = 0;
			if(auditType == DailyAuditConstant.CHECKINTYPE){//审入住
				// 在更新之前，如果此时已生成退房审核，且状态是已完成，则不应该再去更新orderitem数据了,因为退房审核已经有结果了
				if(isCheckOutAuditDone(dailyanuditem.getOrderid(),DailyAuditConstant.RETURNVISIT_TYPE))
					continue;
				night = dailyanuditem.getCheckintime();//入住日期
			}else{//审退房
				night = DateUtil.getDate(dailyanuditem.getCheckouttime(),-1);//退房日期-1天
			}
			Set<DaDailyauditItemSubtable> auditItemSub = dailyanuditem.getDaDailyauditItemSubtables();
			//如果无房间或者已经标记了,则不用再更新了
			if(auditItemSub.isEmpty() || (dailyanuditem.getHasReturnMark() != null && dailyanuditem.getHasReturnMark() == 1))
				continue;
			results = new AuditResult[auditItemSub.size()];
			boolean isAudit = true;
			int noshowCode = 0;
			String noshowReason = null;
			for (DaDailyauditItemSubtable sub : auditItemSub) {
				if (sub.getAuditresults() == null || "".equals(sub.getAuditresults())){
					continue;
				}
				AuditResult result = new AuditResult();
				result.setRoomIndex(sub.getRoomIndex());//房间索引号
				result.setRoomNo(sub.getRoomnumber());//房号
    			result.setFellowName(sub.getActualcheckinname());//实际入住人
    			result.setOldName(sub.getCheckinname());//原订单日入住人
    			int reciprocal = 9999;
				if (sub.getReciprocal() != null) {
					reciprocal = Integer.parseInt(sub.getReciprocal());// 回访结果
				}
				result.setReciprocal(reciprocal);
    			//回访类型为入住
    			if(auditType == DailyAuditConstant.CHECKINTYPE){
    				//入住 或 延住
    				if(reciprocal == DailyAuditConstant.NORMAL_CHECKIN || reciprocal == DailyAuditConstant.EXTENDED_STAY){
    					result.setNoteResult(DailyAuditConstant.NORMAL_CHECKIN);//入住
    				}else if(reciprocal == DailyAuditConstant.NOSHOW){
    					//noshow
    					result.setNoteResult(DailyAuditConstant.NOSHOW);//未入住
    					noshowCode = sub.getNoshowCode();
    					noshowReason = sub.getNoshow();
    				}else {
    					//无需回访的间夜，即审核结果不为Noshow
    					result.setNoteResult(DailyAuditConstant.NORMAL_CHECKIN);
    				}
    				//间夜数 如果入住天数大于1，而且回访结果为已入住，则订单审核未结束，需继续退房审核，回访结果为Noshow或延住均审核已经结束 modify by wupingxiang at 2012-9-19
    				//如果间夜数等于1，则无论什么回访结果，订单的日审都将结束
    				//多间时，当有房间的审核结果或回访结果为正常入住时，需进行退房审核
    				int day = DateUtil.getDay(sub.getCheckintime(), sub.getCheckouttime());
    				int auditResult = Integer.parseInt(sub.getAuditresults());
    				if(day > 1 && (reciprocal == DailyAuditConstant.NORMAL_CHECKIN || auditResult == DailyAuditConstant.NORMAL_CHECKIN)){
    					isAudit = false;
    				}    				
    			}else{
    				//正常退房 或 延住
    				if(reciprocal == DailyAuditConstant.NORMAL_CHECKOUT || reciprocal == DailyAuditConstant.EXTENDED_STAY){
    					result.setNoteResult(DailyAuditConstant.NORMAL_CHECKIN);//入住
    				//提前退房	
    				}else if(reciprocal == DailyAuditConstant.BEFORE_CHECKOUT){
    					result.setNoteResult(DailyAuditConstant.NOSHOW);//未入住
    					result.setCheckoutDate(sub.getRtAdvancecheckouttime());//提前退房时间
    				}else{
    					result.setNoteResult(Integer.parseInt(sub.getAuditresults()));
    				}
    			}
    			results[index] = result;
    			index++;
			}
		   //更新noshow原因，如果有的话
			if(0 !=noshowCode){
				orDailyAuditDao.updateNoshowReason(dailyanuditem.getOrderid(),noshowCode,dailyanuditem.getOrderType(),noshowReason);
			}
			//更新orderItem表
			auditOrderService.noteAuditResult(userName,orderId,orderType, auditType, 
					night, results, isAudit,getOrderState(auditItemSub,DailyAuditConstant.RETURNVISIT_TYPE));
		}
	}

	/**
     * 更新日审明细中的备注
     */
	public void updateAuditItemDataRemark(List<AuditItemDetailInfo> list, String loginId, String loginName) {
		if(null != list){
			for(AuditItemDetailInfo auditItem : list){
				//取得备注
				Object[] results= orDailyAuditDao.getHotelComfirmByOrderId(auditItem.getOrderId(),auditItem.getOrderType());
				if(results != null){
					String auditRemark = (String)results[2];
					if(2 == auditItem.getOrderType()){
						auditRemark = orDailyAuditDao.getRemark(auditItem.getAitemid());
					}
					String append = loginName + "(" + loginId + ") "
			            + DateUtil.datetimeToString(new Date());
			        if(null != auditRemark){
			        	auditRemark = auditRemark + "\n" + append + "\n   "
			            + auditItem.getRemark();
			        }else{
			        	auditRemark = append + "\n   " + auditItem.getRemark();
			        }
			        //保存备注
			        orDailyAuditDao.updateAuditItemDataRemark(auditItem.getOrderId(),auditItem.getOrderType(),auditRemark);
				}
			}
		}
		
	}
	/**
	 * 根据渠道ID,页面数据和操作方式保存到数据库
	 * 
	 * @param operateType
	 * @param htlAudit
	 * @param date
	 */
	public void updateDaDailyaudit(Long auditId, Map roomInfosMap,
			int operateType,boolean workState) {
		DaDailyaudit audit = findDaDailyauditById(auditId);
		boolean needUpdatItem = true;
		//如果只是是释放或获取到我的工作档案
		if(roomInfosMap.size() == 2 && DailyAuditConstant.RELEASE_OPERATION == operateType){
			//只是释放或获取到我的工作档案,没有数据更新子表
			needUpdatItem = false;
		}else{
			List needVisitItems = new ArrayList();
			updateAuditSubRecord(audit, roomInfosMap,needVisitItems);
			if(!needVisitItems.isEmpty()){
				sendSMSProc(audit.getAcquireid(),audit.getAcquirename(),needVisitItems);
			}
		}
		//更新订单明细和更新工作量
		if (DailyAuditConstant.COMPLETE_OPERATION == operateType){
			audit.setState(DailyAuditConstant.AUDIT_COMPLETE_STATE);
		}
		if(needUpdatItem){
			updateOrderItemAndWorkingLoad(audit,operateType);
		}
		if (DailyAuditConstant.COMPLETE_OPERATION == operateType) {
			completeOperation(audit);
		}
		// 如果是释放操作,则更新主表的延时,再分配时间 交接人,交接时间
		if (DailyAuditConstant.RELEASE_OPERATION == operateType) {
			releaseOperation(audit,roomInfosMap);
		}
		updateDaDailyaudit(audit);
		
		//完成一条记录后,记得给用记再自动分配一条记录,在用户状态打开情况下
		if(workState && DailyAuditConstant.COMPLETE_OPERATION == operateType){
			autoAllotAuditForUser(audit.getAcquireid(),audit.getAcquirename());
		}
	}
	
	private void updateOrderItemAndWorkingLoad(DaDailyaudit audit, int operateType) {
		//此时需要更新oreritem表数据
		updateToOrderItem(audit);
		//更新个人和总表数据
		Map<String,Long> datas = getCompleteDatas(audit,null,operateType);
		Long htlCount = datas.get("hotelCount");
		Long orderCount = datas.get("orderCount");
		Long roomCount = datas.get("roomCount");
		if(0 == htlCount + orderCount + roomCount) return;//表明无更新
		long costTime = new Date().getTime()- audit.getAcquiretime().getTime();
		updatePersonel(audit.getAcquireid(), htlCount, orderCount, roomCount, costTime,	DailyAuditConstant.AUDIT_TYPE);
		updateAllWorkingLoad(audit.getAuditdate(), htlCount, orderCount, roomCount,DailyAuditConstant.AUDIT_TYPE);
	}

	/**
	 * 完成操作
	 * @param audit
	 */
	private void completeOperation(DaDailyaudit audit){
		audit.setAchievetime(new Date());
		// 清除用户信息
		removeUserInfo(audit);
	}
	/**
	 * 取得酒店,订单,房间量数据
	 * @param audit
	 * @param returnID 
	 * @param operateType 
	 * @return
	 */
	private Map<String,Long> getCompleteDatas(DaDailyaudit audit, String returnID, int operateType){
		
		Set<DaDailyauditItem> dailyItems = null;
		boolean isReturnType = false;
		Long hotelMember = 0L;
		if(null != audit){
			dailyItems = audit.getDaDailyauditItems();//审核
			hotelMember = audit.getHotelamount();
		}else{
			//回访
    		List<DaDailyauditItem> item_list = orDailyAuditDao.queryDaDailyauditItemByReturnIDAndHasReturnMark(Long.valueOf(returnID));
    		hotelMember = 1L;
    		isReturnType = true;
    		dailyItems = new HashSet();
    		for (DaDailyauditItem daDailyauditItem : item_list) {//转成set类型以统一计算
    			dailyItems.add(daDailyauditItem);
			}
		}
		Long orderCount = 0L;
		Long roomCount = 0L;
		for (DaDailyauditItem item : dailyItems) {
			if(isReturnType){//回访统计
				if(!checkTheOrderDone(item.getDaDailyauditItemSubtables(),DailyAuditConstant.RETURNVISIT_TYPE,item.getAudittype()))//如果这张单不是所有结果已有,则不更新先
					continue;
				if(item.getHasReturnMark() == 0){
					orderCount ++;
					roomCount += Long.parseLong(item.getRoomamount());
					item.setHasReturnMark(1);
				}
			}else{//审核统计
				if(!checkTheOrderDone(item.getDaDailyauditItemSubtables(),DailyAuditConstant.AUDIT_TYPE,item.getAudittype()))//如果这张单不是所有结果已有,则不更新先
					continue;
				if(item.getHasAuditMark() == 0){
					orderCount ++;
					roomCount += Long.parseLong(item.getRoomamount());
					item.setHasAuditMark(1);//设为已标记
				}
			}
		}
		if(isReturnType && !dailyItems.isEmpty()){//如果是回访,则保存更改的
			orDailyAuditDao.saveOrUpdateDaDailyauditItemCollection(dailyItems);
		}
		Map<String,Long> datas = new HashMap<String, Long>();
		if(DailyAuditConstant.COMPLETE_OPERATION ==operateType){
			datas.put("hotelCount", hotelMember);//只要完成,才算酒店量(会员量),否则只算订单量
		}else{
			datas.put("hotelCount", 0L);
		}
		datas.put("orderCount", orderCount);
		datas.put("roomCount", roomCount);
		return datas;
	}
	/**
	 * 完成时，需要把数据更新到oderitem
	 * @param audit
	 */
	private void updateToOrderItem(DaDailyaudit audit) {
		Set<DaDailyauditItem> items = audit.getDaDailyauditItems();
		for (DaDailyauditItem item : items) {
			Set<DaDailyauditItemSubtable> itemSubs = item.getDaDailyauditItemSubtables();
			//如果无房间或者已经标记了,则不用再更新了
			if((itemSubs.isEmpty() || (item.getHasAuditMark() != null && item.getHasAuditMark() == 1))
					|| !checkTheOrderDone(itemSubs,DailyAuditConstant.AUDIT_TYPE,item.getAudittype()))//如果这张单不是所有结果已有,则不更新先
				continue;
			
			boolean isNeedToSetOrderState = false;
			Date night = null;
			int audittype = item.getAudittype();
			if(audittype == DailyAuditConstant.CHECKINTYPE){//审入住
				//入住审核
				//在更新之前，如果此时已生成退房审核，且状态是已完成，则不应该再去更新orderitem数据了,因为退房审核已经有结果了
				if(isCheckOutAuditDone(item.getOrderid(),DailyAuditConstant.AUDIT_TYPE))
					continue;
				night = item.getCheckintime();
				//看是否需要更改审核状态,条件为:只有一个间夜,且无未入住
				//找出有noshow的订单
				if(1 == DateUtil.getDay(item.getCheckintime(), item.getCheckouttime())){
					DaDailyauditItemSubtable noShowetcSub = null;
					noShowetcSub = checkIsNeedToUpdateOrderitem(itemSubs,false);
					if(null == noShowetcSub)//需要改成:已审核
						isNeedToSetOrderState = true;
				}
			}else{//审退房
				night = DateUtil.getDate(item.getCheckouttime(),-1);
				//看是否需要更改审核状态,条件为:多个间夜,且无提前退房
				if(1 < DateUtil.getDay(item.getCheckintime(), item.getCheckouttime())){
					DaDailyauditItemSubtable checkBeforSub = null;
					checkBeforSub = checkIsNeedToUpdateOrderitem(itemSubs,true);
					if(null == checkBeforSub)//没有提前退房的,需要改成:已审核
						isNeedToSetOrderState = true;
				}
				//添加无入住审核，只有退房审核的day=1的补单 add by diandian.hou 2011-08-20
				if(1 == DateUtil.getDay(item.getCheckintime(), item.getCheckouttime())){
					DaDailyauditItemSubtable checkBeforSub = null;
					checkBeforSub = checkIsNeedToUpdateOrderitem(itemSubs,true);
					if(null == checkBeforSub)//没有提前退房的,需要改成:已审核
						isNeedToSetOrderState = true;
				}
				
			}
			int index = 0;
			AuditResult[] results = new AuditResult[itemSubs.size()];
			for (DaDailyauditItemSubtable sub : itemSubs) {
				getAuditResults(results,sub,index++,audittype);
			}
			//如果已审核,则更改订单状态
			int orderState = 0;
			orderState = getOrderState(itemSubs,DailyAuditConstant.AUDIT_TYPE);
			auditOrderService.noteAuditResult(audit.getAcquirename(),item.getOrderid(),
					item.getOrderType(),audittype,night,results, isNeedToSetOrderState,orderState);
		}
	}
	/**
	 * 检查此单是否都已完成,完成返回true
	 * @param itemSubs
	 * @param type
	 * param auditType 审核类型
	 * @return
	 */
	private boolean checkTheOrderDone(Set<DaDailyauditItemSubtable> itemSubs, int type,int auditType) {
		String result = null;
		for (DaDailyauditItemSubtable sub : itemSubs) {
			if(DailyAuditConstant.AUDIT_TYPE == type){
				result = sub.getAuditresults();//审核结果
				if(!StringUtil.isValidStr(result) || null != sub.getIsSaveFront())
					return false;
			}else{
				result = sub.getReciprocal();//回访结果
				if (sub.getAuditresults() == null || "".equals(sub.getAuditresults())){
					continue;
				}
				int auditResult = Integer.parseInt(sub.getAuditresults());
				if (DailyAuditConstant.CHECKINTYPE == auditType){
					if (auditResult == DailyAuditConstant.NOSHOW 
							&& (!StringUtil.isValidStr(result) || null != sub.getIsSaveFront())){
							return false;
					}
				}else{
					if (auditResult == DailyAuditConstant.BEFORE_CHECKOUT 
							&& (!StringUtil.isValidStr(result) || null != sub.getIsSaveFront())){
							return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 在更新之前，如果此时已生成退房审核（回访），且状态是已完成，则返回true
	 * @param item
	 * @param audit_type
	 * @return
	 */
	private boolean isCheckOutAuditDone(Long orderId, int audit_type) {
		return orDailyAuditDao.isCheckOutAuditDone(orderId,audit_type);
	}

	/**
	 * 根据审核结果,更改订单状态
	 * @param itemSubs
	 * @return
	 */
	private int getOrderState(Set<DaDailyauditItemSubtable> itemSubs,int type) {
		
		int extendQuit = 0;
		int normalQuit = 0;
		int earlyQuit = 0;
		int nomalCheckin = 0;
		int noshow = 0;
		int auditResult=0;
		for (DaDailyauditItemSubtable sub : itemSubs) {
			if(null == sub.getAuditresults()){
				continue;
			}
			if(DailyAuditConstant.AUDIT_TYPE == type){
				auditResult = Integer.parseInt(sub.getAuditresults());
			}else{
				if (sub.getReciprocal() == null){
					auditResult = Integer.parseInt(sub.getAuditresults());
				}else{
					auditResult = Integer.parseInt(sub.getReciprocal());
				}
			}
			if(auditResult == DailyAuditConstant.EXTENDED_STAY)
				extendQuit = OrderState.EXTEND;
			else if(auditResult == DailyAuditConstant.NORMAL_CHECKOUT)
				normalQuit = OrderState.NORMAL_QUIT;
			else if(auditResult == DailyAuditConstant.BEFORE_CHECKOUT)
				earlyQuit = OrderState.EARLY_QUIT;
			else if(auditResult == DailyAuditConstant.NORMAL_CHECKIN){
				//如果是多个间夜的，则把订单状态改为已入住,否则为正常退房
				if(DateUtil.getDay(sub.getCheckintime(), sub.getCheckouttime()) > 1)
					nomalCheckin = OrderState.CHECKIN;
				else
					normalQuit = OrderState.NORMAL_QUIT;
			}else
				noshow = OrderState.NOSHOW;
		}
		int orderState = 0;
		if(extendQuit != 0)
			orderState = extendQuit;
		else if(normalQuit != 0)
			orderState = normalQuit;
		else if(earlyQuit != 0)
			orderState = earlyQuit;
		else if(nomalCheckin != 0)
			orderState = nomalCheckin;
		else
			orderState = noshow;
		return orderState;
	}

	private DaDailyauditItemSubtable checkIsNeedToUpdateOrderitem(Set<DaDailyauditItemSubtable> itemSubs, boolean inNeedCheckBefore) {
		for (DaDailyauditItemSubtable sub : itemSubs) {
			String temp = sub.getAuditresults();
			if(null == temp)
				continue;
			int auditResult = Integer.parseInt(temp);
			if(((auditResult == DailyAuditConstant.NOSHOW && (StringUtil.isValidStr(sub.getAuditorid()))) || auditResult == DailyAuditConstant.BEFORE_CHECKOUT)){
				if(inNeedCheckBefore)
					return sub;
				else{
					if(auditResult == DailyAuditConstant.NOSHOW)
						return sub;
				}
			}
		}
		return null;
	}

	/**
	 * 释放操作
	 * @param roomInfosMap 
	 * @param audit 
	 * 
	 */
	private void releaseOperation(DaDailyaudit audit, Map roomInfosMap){
		//无延长时间,说明是交接处理,设置交接人信息
		if (null != roomInfosMap.get("delayTime")) {
			// 延长时间不为空,则说明是延时处理,设置再分配时间,和清除获取人信息
			int delayTime = Integer.parseInt((String) roomInfosMap
					.get("delayTime"));
			Calendar can = Calendar.getInstance();
			can.add(Calendar.MINUTE, delayTime);// 加上分钟数
			Date reSignTime = can.getTime();
			audit.setReassigntime(reSignTime);
			audit.setDelaytime(delayTime);
			//延时原因
			audit.setReleaseReason(Integer.parseInt((String) roomInfosMap
					.get("releaseReason")));
			//设置释放人到交接人保存
			setUpAccquirerInfo(audit, null, null,
					DailyAuditConstant.GETAUDIT_AUTO, true);
			//把状态设置为未获取
			audit.setAquireState(DailyAuditConstant.NOTYETGOTAUDIT);
			audit.setAcquiretime(null);
			roomInfosMap.remove("delayTime");// 把时间去掉
			roomInfosMap.remove("releaseReason");
		} else {
			//交接原因,如果有的话,另外,获取到我的工作档案也是调用此里,这时就没有释放原因了
			if(null != roomInfosMap.get("releaseReason")){
				audit.setReleaseReason(Integer.parseInt((String) roomInfosMap
						.get("releaseReason")));
			}
			setUpAccquirerInfo(audit, (String) roomInfosMap.get("beDeliverId"), (String) roomInfosMap
					.get("beDeliverName"),DailyAuditConstant.GETAUDIT_BYHAND, true);
			roomInfosMap.remove("beDeliverId");
			roomInfosMap.remove("beDeliverName");
			roomInfosMap.remove("releaseReason");
		}
	}
	/**
	 * 完成工作后,更新工作量总表
	 * 
	 * @param auditdate
	 *            日审日期
	 * @param hotelamount
	 *            酒店量
	 * @param orderamount
	 *            订单量
	 * @param roomamount
	 *            房间数
	 * @param Re_type 说明是日审(1)还是回访(2)
	 */
	@SuppressWarnings("unchecked")
	private void updateAllWorkingLoad(Date auditdate, Long hotelamount,
			Long orderamount, Long roomamount, int Re_type) {
		//审核日期与当天日期相差1
		int type = (DateUtil.compare(auditdate,new Date()) == 1) ? 1:2;
		if(Re_type == DailyAuditConstant.RETURNVISIT_TYPE){
			type = (DateUtil.compare(auditdate,new Date()) == 0) ? 1:2;
		}
		List<DaAuditingWorkload> auditWorkload = getWorkloadByType(Re_type);
		DaAuditingWorkload workload = null;
		for (DaAuditingWorkload daAuditingWorkload : auditWorkload) {
			if(daAuditingWorkload.getContenttype() == type){
				workload = daAuditingWorkload;
				break;
			}
		}
		workload.setHotelmemberamount(workload.getHotelmemberamount() - hotelamount);
		workload.setOrderamount(workload.getOrderamount() - orderamount);
		workload.setRoomamount(workload.getRoomamount() - roomamount);
		//为了数据准确性,现在每少10条就更新下记录,只更新今天需完成和历史需完成两项
		if((hotelamount != 0) && (workload.getHotelmemberamount()%10 == 0))
			updateAllWorkingLoads(workload,type,Re_type);
		//更新今天完成情况记录
		for (DaAuditingWorkload daAuditingWorkload : auditWorkload) {
			if(daAuditingWorkload.getContenttype() == 3){
				workload = daAuditingWorkload;
				break;
			}
		}
		workload.setHotelmemberamount(workload.getHotelmemberamount() + hotelamount);
		workload.setOrderamount(workload.getOrderamount() + orderamount);
		workload.setRoomamount(workload.getRoomamount() + roomamount);
		//保存入库
		orDailyAuditDao.saveOrUpdateDaAuditingWorkloadCollection(auditWorkload);
	}

	private void updateAllWorkingLoads(DaAuditingWorkload workload, int type, int re_type) {
		Object[] obj = orDailyAuditDao.queryDataForDaAuditingWorkload(type,re_type);
		workload.setHotelmemberamount(obj[0] == null ? 0 : (Long)obj[0]);
		workload.setOrderamount(obj[1] == null ? 0 : (Long)obj[1]);
		if(obj[2] != null && obj[2] instanceof String) {
			obj[2] = Long.valueOf((String)obj[2]);
		}
		workload.setRoomamount(obj[2] == null ? 0 : (Long)obj[2]);
	}

	/**
	 * 完成工作后,更新工作量表
	 * 
	 * @param acquireid
	 *            用户ID
	 * @param hotelamount
	 *            酒店量
	 * @param orderamount
	 *            订单量
	 * @param roomamount
	 *            房间数
	 * @param costTime
	 *            此花费时间
	 * @param updatePersonelAudit
	 *            更新类型:审核组人 或 回访组人
	 */
	private void updatePersonel(String acquireid, Long hotelamount,
			Long orderamount, Long roomamount, long costTime,
			int updatePersonelAudit) {
		DaPersonalWorkload thePerson = getPersonWorkloadByAuditId(acquireid,updatePersonelAudit);
		// 完成的渠道量
		long channelStep = 1;
		if (null == thePerson) {
			// 此用户今天无记录
			thePerson = new DaPersonalWorkload();
			thePerson.setAuditortime(DateUtil.getDate(new Date()));
			thePerson.setAuditorid(acquireid);
			thePerson.setType(updatePersonelAudit);
		}
		costTime = thePerson.getCostTime().longValue() + costTime;
		// 下面更新酒店量,订单量,房间量和花费时间等
		thePerson.setCompletedChannel((thePerson.getCompletedChannel() + channelStep));
		thePerson.setCompletedhotel(thePerson.getCompletedhotel() + hotelamount);
		thePerson.setCompletedorder(thePerson.getCompletedorder() + orderamount);
		thePerson.setCompletedroom(thePerson.getCompletedroom() + roomamount);
		//如果没有订单量,则不去更新时长之类的
		if(orderamount > 0){
			thePerson.setCostTime(costTime);
			// 下面计算平均处理时长
			/*long workSpeed = costTime
					/ (PERSON_MINUTE * thePerson.getCompletedorder());
			String averagetime = workSpeed + " 分钟/订单";*/
			long workSpeed = costTime/(1000 * thePerson.getCompletedorder());
			String averagetime = workSpeed + " 秒/订单";
			thePerson.setAveragetime(averagetime);
			if(DailyAuditConstant.RETURNVISIT_TYPE == updatePersonelAudit){
				int noshowBackCount = orDailyAuditDao.getNoshowBackCountOrAll(acquireid,false);
				int noshowCountAll = orDailyAuditDao.getNoshowBackCountOrAll(acquireid,true);
				thePerson.setNoshowBackCount(noshowBackCount);
				thePerson.setNoshowCountAll(noshowCountAll);
				float noshowRate = ((noshowBackCount/(float)noshowCountAll));
				//String noshowRateSt = noshowRate+"%";
				thePerson.setNoshowRate(String.valueOf(noshowRate));
			}
		}
		// 保存或更新数据
		orDailyAuditDao.saveOrUpdateDaPersonalWorkload(thePerson);
	}

	/**
	 * 根据用户ID获取当天工作量 by juesuchen
	 * 
	 * @param auditorid
	 * @param updatePersonelAudit 
	 * @return
	 */
	public DaPersonalWorkload getPersonWorkloadByAuditId(String auditorid, int updatePersonelAudit) {
		return orDailyAuditDao.getPersonWorkloadByAuditId(auditorid,updatePersonelAudit);

	}
	
	/**
	 * 当页面上有需要的信息更新到子表
	 * 
	 * @param audit
	 * @param roomInfosMap
	 * @param needVisitItems 
	 */
	private void updateAuditSubRecord(DaDailyaudit audit, Map roomInfosMap, List needVisitItems) {
		Set<DaDailyauditItem> dailyItems = audit.getDaDailyauditItems();
		for (DaDailyauditItem item : dailyItems) {
			boolean needToVisit = false;
			Set<DaDailyauditItemSubtable> subItems = item.getDaDailyauditItemSubtables();
			for (DaDailyauditItemSubtable daisub : subItems) {
				// 如果页面上有对应的更新数据,则进行更新操作,审核结果,房间号,确认号,提前退房时间,延时,再分配时间等等
				DaAuditRoomDetailInfo temp = (DaAuditRoomDetailInfo) roomInfosMap
						.get(daisub.getID());
				if (null != temp) {
					daisub.setAuditresults(temp.getAuditresults());
					daisub.setIsSaveFront(temp.getIsSaveFront());
					daisub.setRoomnumber(temp.getRoomnumber());
					daisub.setAffirmnumber(temp.getAffirmnumber());
					daisub.setActualcheckinname(temp.getActualcheckinname());
					daisub.setAuditorid(audit.getAcquireid());
					daisub.setAuditorname(audit.getAcquirename());
					daisub.setAuditortime(new Date());
					Integer saveFont = temp.getIsSaveFront();
					// noshow 2,所以还要看下提前日期是否为空
					if (DailyAuditConstant.NOSHOW == Integer.parseInt(temp
							.getAuditresults()) && saveFont == null) {
						// 如果没有入住,则设置此信息需要回访
						needToVisit = true;
					} else if (DailyAuditConstant.BEFORE_CHECKOUT == Integer
							.parseInt(temp.getAuditresults())) {
						// 如果有提前退房 4,则保存退房时间
						daisub.setAdvancecheckouttime(temp
								.getAdvancecheckouttime());
						// 设置此信息需要回访
						if(saveFont == null)
							needToVisit = true;
					}
				}
			}
			if(needToVisit){
				setAuditRecordToBeReturnVisit(item,audit.getAuditdate());
				//增加元素以发送回访短信
				if(1 == item.getOrderType())
					needVisitItems.add(item);
			}
		}
	}
	/**
	 * 发送回访短信
	 * @param needVisitItems 
	 * @param loginName 
	 * @param loginId 
	 * @return
	 */
	public void sendSMSProc(String loginId, String loginName, List<DaDailyauditItem> needVisitItems) {
		for (DaDailyauditItem item : needVisitItems) {
			if(!checkTheOrderDone(item.getDaDailyauditItemSubtables(),DailyAuditConstant.AUDIT_TYPE,item.getAudittype()))//如果这张单不是所有结果已有,则不更新先
				continue;
	        OrOrder order = orderService.findOrOrder(item.getOrderid());
	        //order.setMobile("13580450193");
	        if(ConfirmType.SMS != order.getConfirmType() || !StringUtil.isValidStr(order.getMobile()))
	        	continue;
	        String smsText = getMsgText(order,getMsgType(item));
	        
	        // 调用发送短信接口和创建订单客户确认信息发送表信息
	        Sms sms = new Sms();
	        sms.setApplicationName("hotel");
	        sms.setTo(new String[] {order.getMobile()});
	        sms.setMessage(smsText);
	        sms.setFrom(loginName);
	        Long res = communicaterService.sendSms(sms);
	        //创建订单客户确认信息发送表信息
	        OrMemberConfirm memberConfirm = getOrMemberConfirm(order.getMobile(), getMsgType(item), smsText, res,loginId,loginName,order);
	        order.getMemberConfirmList().add(memberConfirm);
	        if (!order.isSendedMemberFax()) {
	            order.setSendedMemberFax(true);
	        }
	        // 已发送客户确认
	        boolean bChange = false;
	        if (!order.isCustomerConfirm() && getMsgType(item) == MemberConfirmType.CONFIRM) {
	            order.setCustomerConfirm(true);
	            bChange = true;
	        }
	        if (bChange) {
	            addMemberConfirmLog(order,loginId,loginName);
	        }
	        order.setModifiedMidTime(new Date());
	        // v2.8.1 增加操作人 add by chenkeming
	        order.setModifier(loginId);
	        order.setModifierName(loginName);
	
	        orderService.saveOrUpdate(order);
		}
    }
	/**
	 * 获取确认信息的类型
	 * @param item
	 * @return
	 */
	private int getMsgType(DaDailyauditItem item) {
		int beforeCheckOut = 0;
		int partIn = 0;
		Set<DaDailyauditItemSubtable> subItems = item.getDaDailyauditItemSubtables();
		for (DaDailyauditItemSubtable daisub : subItems) {
			String rs = daisub.getAuditresults();
			if(null == rs)
				continue;
			int auditResult = Integer.parseInt(rs);
			if(auditResult == DailyAuditConstant.BEFORE_CHECKOUT){
				beforeCheckOut = DailyAuditConstant.BEFORE_CHECKOUT;
			}
			if(auditResult == DailyAuditConstant.NORMAL_CHECKIN || auditResult == DailyAuditConstant.EXTENDED_STAY){
				partIn = DailyAuditConstant.NORMAL_CHECKIN;
			}
		}
		if(0 != beforeCheckOut)
			return OrderState.EARLY_QUIT;
		else if(0 != partIn)
			return 12;
		else
			return OrderState.NOSHOW;
	}
	/**
	 * 获取发送信息的文本信息
	 * @param order
	 * @param smsType
	 * @return
	 */
	private String getMsgText(OrOrder order, int smsType) {
		//您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）,酒店反馈您已提前退房,如不符请致电4008876698
		Date choutDate = new Date(order.getCheckoutDate().getTime() + 24 * 60 * 60 * 1000);
        String dateStr = "";
        long checkInLong = order.getCheckinDate().getTime() + 24 * 60 * 60 * 1000;
        long checkOutLong = choutDate.getTime();
        if (checkOutLong > checkInLong) {
            dateStr = DateUtil.toStringByFormat(order.getCheckinDate(), "MM") + "月"
                + DateUtil.toStringByFormat(order.getCheckinDate(), "dd") + "-"
                + DateUtil.toStringByFormat(order.getCheckoutDate(), "dd") + "日";
        } else {
            dateStr = DateUtil.toStringByFormat(order.getCheckinDate(), "MM月dd日");
        }
        String memberAliasIds = "9400100001,9200600002,8500200002,8500200001";
        if(OrderState.EARLY_QUIT == smsType){
        	StringBuilder msgTextBuild = new StringBuilder();
        	msgTextBuild.append("");
        	if(order.isMango()){
        		msgTextBuild.append("您预订的");
        		msgTextBuild.append(dateStr);
        		msgTextBuild.append(order.getHotelName());
        		if(order.getMemberAliasId() != null && memberAliasIds.indexOf(order.getMemberAliasId())>=0){  
        		msgTextBuild.append(",酒店反馈您已提前退房,为确保积分准确,如不符请致电4008876698");       		
        		}else{
        			msgTextBuild.append(",酒店反馈您已提前退房,如不符请直接回复退房日期，以确保您的积分准确。");
        		}
        	}else{
        		msgTextBuild.append("您预订的");
        		msgTextBuild.append(dateStr);
        		msgTextBuild.append(order.getHotelName());
        		msgTextBuild.append(",酒店反馈您已提前退房,如不符请致电4008876698");
        	}
        	return msgTextBuild.toString();
        }
        String roomType = (order.getRoomQuantity() + "").trim() + "间" + order.getRoomTypeName();
        if(OrderState.NOSHOW == smsType){
        	// 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未入住,如不符请致电4008876698
        	StringBuilder noShowBuilder = new StringBuilder();
        	if(order.isMango()){
        		noShowBuilder.append("您预订的");
        		noShowBuilder.append(dateStr);
        		noShowBuilder.append(order.getHotelName());
        		noShowBuilder.append(roomType);
        		if(order.getMemberAliasId() != null && memberAliasIds.indexOf(order.getMemberAliasId())>=0){       		
        		 noShowBuilder.append(",酒店反馈您未入住,为确保积分准确,如不符请致电4008876698");
        		}else{       			
        			 noShowBuilder.append(",酒店反馈您未入住,若有不符请直接回复“入住人姓名+房号”，以确保您的积分准确。");
        		}
        	}else{
        		noShowBuilder.append("您预订的");
        		noShowBuilder.append(dateStr);
        		noShowBuilder.append(order.getHotelName());
        		noShowBuilder.append(roomType);
        		noShowBuilder.append(",酒店反馈您未入住,如不符请致电4008876698");
        	}
            return noShowBuilder.toString();
        }else{
        	//您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未全部入住,如不符请致电4008876698
        	StringBuilder partCheckinBuilder = new StringBuilder();
            if(order.isMango()){
            	partCheckinBuilder.append("您预订的");
            	partCheckinBuilder.append(dateStr);
            	partCheckinBuilder.append(order.getHotelName());
            	partCheckinBuilder.append(roomType);
            	if(order.getMemberAliasId() != null && memberAliasIds.indexOf(order.getMemberAliasId())>=0){  
            	partCheckinBuilder.append(",酒店反馈您未全部入住,为确保积分准确,如不符请致电4008876698");           	
            	}else{
            		partCheckinBuilder.append(",酒店反馈您未全部入住,若有不符请直接回复“入住人姓名+房号”，以确保您的积分准确。");
            	}
            }else{
            	partCheckinBuilder.append("您预订的");
            	partCheckinBuilder.append(dateStr);
            	partCheckinBuilder.append(order.getHotelName());
            	partCheckinBuilder.append(roomType);
            	partCheckinBuilder.append(",酒店反馈您未全部入住,如不符请致电4008876698");
            }
            return partCheckinBuilder.toString();
        }
	}

	/**
     * 发送客人确认日志
	 * @param loginName 
	 * @param loginId 
	 * @param order 
     * 
     */
    private void addMemberConfirmLog(OrOrder order, String loginId, String loginName) {
        StringBuffer strCmp = new StringBuffer();
        strCmp.append("订单改为:<font color='red'>" + "已发送客户确认" + "</font>");
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(loginName);
        handleLog.setModifierRole(loginId);
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent(strCmp.toString());
        handleLog.setModifiedTime(new Date());
        handleLog.setHisNo(order.getHisNo());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);

        OrderUtil.updateStayInMid(order);
    }
	/**
     * 创建订单客户确认信息发送表信息
	 * @param order 
	 * @param loginName 
	 * @param loginId 
     */
    private OrMemberConfirm getOrMemberConfirm(String phoneNo, 
        int smsType, String smsText, Long res, String loginId, String loginName, OrOrder order) {
        OrMemberConfirm memberConfirm = new OrMemberConfirm();
        memberConfirm.setChannel(ConfirmType.SMS);
        memberConfirm.setModelType(order.getType());// 114还是芒果
        memberConfirm.setType(smsType);
        memberConfirm.setSendTarget(phoneNo);

        memberConfirm.setSendMan(loginName);
        memberConfirm.setSendManId(loginId); // 发送人的工号
        memberConfirm.setSendTime(new Date());
        memberConfirm.setSendSucceed(true);
        memberConfirm.setContent(smsText);

        memberConfirm.setSendStatus(MemberConfirmSmsStutas.SENDING);
        memberConfirm.setUnicallRetId(res);

        memberConfirm.setOrder(order);
        return memberConfirm;
    }
	/**
	 * 设置此订单需要回访
	 * 
	 */
	private void setAuditRecordToBeReturnVisit(DaDailyauditItem item, Date auditdate) {
		DaReturnvisit visit = (DaReturnvisit)orDailyAuditDao.findDaReturnvisitToday(item.getLinkmannumber());
		//更新当天回访量总表
		DaAuditingWorkload toLoad = null;
		List<DaAuditingWorkload> retLoad= getWorkloadByType(DailyAuditConstant.RETURNVISIT_TYPE);
		for (DaAuditingWorkload obj : retLoad) {
			if(obj.getContenttype() == 1L){
				toLoad = obj;
				break;
			}
		}
		if(null != visit){
			if(null != item.getReturnvisitid())//如果已经为此订单设置了回访，则直接返回
				return;
			//说明此会员当天已有回访数据,根据ID设置需要回访
			if(visit.getReturnstate() == DailyAuditConstant.AUDIT_COMPLETE_STATE)
				toLoad.setHotelmemberamount(toLoad.getHotelmemberamount() + 1);
			visit.setReturnstate(DailyAuditConstant.AUDIT_NOTCOMPLETE_STATE);
			log.info("null != visit :为此明细设置回访："+item.getID()+"---"+visit.getID());
			item.setReturnvisitid(visit.getID());
			orDailyAuditDao.saveOrUpdateDaReturnvisit(visit);
		}else{
			//需要为此会员生成一条回访记录
			visit = new DaReturnvisit();
			visit.setLinkmanname(item.getLinkmanname());
			visit.setReturnstate(DailyAuditConstant.AUDIT_NOTCOMPLETE_STATE);
			visit.setLinkmannumber(item.getLinkmannumber());
			if(2 == item.getOrderType()) {//如果是TMC订单,则把号码写成一个标志数字
				visit.setLinkmannumber("NOMOBILE");
			}
			visit.setAuditordate(DateUtil.getDate(new Date()));
			visit.setAquirestate(DailyAuditConstant.NOTYETGOTAUDIT);
			//DaReturnvisit
			orDailyAuditDao.saveOrUpdateDaReturnvisit(visit);
			log.info("null == visit :为此明细设置回访："+item.getID()+"---"+visit.getID());
			item.setReturnvisitid(visit.getID());
			toLoad.setHotelmemberamount(toLoad.getHotelmemberamount() + 1);
		}
		Long roomNum = Long.parseLong(item.getRoomamount());
		toLoad.setOrderamount(toLoad.getOrderamount()+1);
		toLoad.setRoomamount(toLoad.getRoomamount()+roomNum);
		orDailyAuditDao.saveOrUpdateDaAuditingWorkload(toLoad);
	}

	/**
	 * 根据类型获取当天工作量 by juesuchen
	 * 
	 * @param type
	 *            是回访组还是会员组
	 * @return
	 */
	public List getWorkloadByType(int type) {
		return orDailyAuditDao.getWorkloadByType(type);
	}

	public List getAllChannelInfo() {
		return orDailyAuditDao.getAllChannelInfo();
	}

	public List<HotelAuditDetail> queryAuditInfo(String theHotelName, String theAuditDate, String theOrderCd, int type) {
		
		Map<Long,HotelAuditDetail> resultMap = new HashMap<Long, HotelAuditDetail>();
		if(type == 1){
			//根据酒店和日期查
			List<String> orderCds = orDailyAuditDao.getOrderCdsByHotelNameAndDate(theHotelName, theAuditDate);
			for (String orderCd : orderCds) {
				getAuditInfoByOrderCd(orderCd,resultMap);
			}
		}else if(type == 2){
			//根据订单号查
			getAuditInfoByOrderCd(theOrderCd,resultMap);
		}
		//些时resultMap 里面装有所有已封装好的 HotelAuditDetail
		List<HotelAuditDetail> results = new ArrayList<HotelAuditDetail>();
		if(!resultMap.isEmpty()){
			for (Iterator<Long> htlIdsIter = resultMap.keySet().iterator(); htlIdsIter
					.hasNext();) {
				Long htlId = htlIdsIter.next();
				HotelAuditDetail tempInfor = resultMap.get(htlId);
				//把酒店内部的订单转成LIST
				tempInfor.converMapToList();
				results.add(tempInfor);
			}
		}
		return results;
	}
	/**
	 * 根据订单号取得日审信息
	 * @param theOrderCd
	 * @param resultMap 
	 */
	private void getAuditInfoByOrderCd(String theOrderCd, Map<Long, HotelAuditDetail> resultMap) {
		//这里处理的是一张订单
		List<DaDailyauditItem> items = orDailyAuditDao.getDaDailyauditItemByOrder(theOrderCd);
		if(!items.isEmpty()){
			for (DaDailyauditItem item : items) {
				if(item.getAudittype() == DailyAuditConstant.CHECKINTYPE){
					//入住审核
					fillDetailRoomInfos(item,resultMap,DailyAuditConstant.CHECKINTYPE);
				}else{
					//退房审核
					fillDetailRoomInfos(item,resultMap,DailyAuditConstant.CHECKOUTTYPE);
				}
			}
			DaOrderAudit theOrderInfo = resultMap.get(items.get(0).getHotelid())
							.getOrderAuditsMap().get(theOrderCd);
			theOrderInfo.converMapToList();
		}
	}
	/**
	 * 填充数据
	 * @param item
	 * @param resultMap
	 * @param checkType
	 */
	private void fillDetailRoomInfos(DaDailyauditItem item, Map<Long, HotelAuditDetail> resultMap, int checkType) {
		//拿酒店数据
		HotelAuditDetail detail = resultMap.get(item.getHotelid());
		if(detail == null){
			//没有记录
			detail = new HotelAuditDetail();
			detail.setHotelId(item.getHotelid());
			String htlName = orDailyAuditDao.getHotelNameById(item.getHotelid());
			Object[] chnNameId = orDailyAuditDao.getChaNameByAuditId(item.getDailyauditid());
			detail.setHotelName(htlName) ;
			detail.setChannelId((Long)chnNameId[1]);
			detail.setChannelName((String)chnNameId[0]);
			resultMap.put(item.getHotelid(), detail);
		}
		//看是是否有订单审核信息
		DaOrderAudit orderAudit = detail.getOrderAuditsMap().get(item.getOrderCd());
		if(orderAudit == null){
			orderAudit = new DaOrderAudit();
			orderAudit.setAuditItem(item);
			detail.getOrderAuditsMap().put(item.getOrderCd(), orderAudit);
		}
		//设置订单审核信息下面的房间信息
		for (DaDailyauditItemSubtable room : item.getDaDailyauditItemSubtables()) {
			DaSubItem subItem = orderAudit.getRoomInfosMap().get(room.getRoomIndex());
			if(subItem == null){
				subItem = new DaSubItem();
				orderAudit.getRoomInfosMap().put(room.getRoomIndex(), subItem);
			}
			if(checkType == DailyAuditConstant.CHECKINTYPE)
				subItem.setCheckInSub(room);
			else
				subItem.setCheckOutSub(room);
		}
		
	}
	/**
	 * 在查询审核信息页面时,保存数据,并更新到Item表
	 * 0: 成功, 1:失败, 2:财务已获取 
	 */
	public int saveAuditInfo(String noteMan,Map<Long,DaAuditRoomDetailInfo> roomInfosMap) {
		//先保存数据到库,并且收集好订单编号,用于更新ITEM表
		Set<String> orderCds = new HashSet<String>();
		Iterator<Long> ids = roomInfosMap.keySet().iterator();
		//遍历取得订单编号值
		for (; ids.hasNext();) {
			Long id = ids.next();
			DaAuditRoomDetailInfo element = (DaAuditRoomDetailInfo) roomInfosMap.get(id);
			orderCds.add(element.getOrderCd());
		}
		//根据订单编号读取数据并更新
		DaDailyauditItem item = null;
		int ret = 1;
		for (String orderCd : orderCds) {
			List<DaDailyauditItem> items = orDailyAuditDao.getDaDailyauditItemByOrder(orderCd);
			if(items.size() > 1){
				//说明已生成了入住与退房记录,遍历更新数据,以退房审核数据为准
				item = items.get(0);
				//需要更新入住审核的房间数据，只更新审核回访结果，其它会以退房数据为准
				Set<DaDailyauditItemSubtable> itemSubs = item.getDaDailyauditItemSubtables();
				if(!itemSubs.isEmpty()){//如果不为空，则保存数据
					for (DaDailyauditItemSubtable sub : itemSubs) {
						DaAuditRoomDetailInfo roomInfo = roomInfosMap.get(sub.getID());
						updatePageDataToDB(roomInfo,sub);
					}
				}
				orDailyAuditDao.saveorupdatedadailyaudititem(item);
				item = items.get(1);
			}else if(items.size() == 1){
				//说明只生成了入住或退房记录
				item = items.get(0);
			}
			if(null != item)
				ret = updateToDBAndOrderItem(noteMan,item,roomInfosMap);
		}
		return ret;
	}
	/**
	 * 更新到数据库和orderitem表
	 * @param noteMan
	 * @param item
	 * @param roomInfosMap
	 */
	private int updateToDBAndOrderItem(String noteMan, DaDailyauditItem item,Map<Long,DaAuditRoomDetailInfo> roomInfosMap) {
	
		//组装入住结果
		Set<DaDailyauditItemSubtable> itemSubs = item.getDaDailyauditItemSubtables();
		if(itemSubs.isEmpty())
			return 1;
		int audittype = item.getAudittype();
		Date night;
		if(audittype == DailyAuditConstant.CHECKINTYPE){
			//入住审核,在这里调用,一般是已经确定了入住结果,但未生成退房结果,
			//否则就无效了,所以应该要传调用当天日期
			night = item.getCheckintime();
		}else{
			night = DateUtil.getDate(item.getCheckouttime(),-1);
		}
		AuditResult[] results  = new AuditResult[itemSubs.size()];
		int index = 0;
		for (DaDailyauditItemSubtable sub : itemSubs) {
			//先进行保存页面上的数据
			DaAuditRoomDetailInfo roomInfo = roomInfosMap.get(sub.getID());
			if(roomInfo == null)
				continue;
			updatePageDataToDB(roomInfo,sub);
			//再进行组装参数，更新orderitem表
			getAuditResults(results,sub,index++,audittype);
		}
		orDailyAuditDao.saveorupdatedadailyaudititem(item);
		int ret = 1;
		if(null != results[0])
			ret = auditOrderService.noteAuditResult(noteMan,item.getOrderid(),
				item.getOrderType(),audittype,night,results, false,OrderState.NOSHOW);
		//更新noshow原因,如果有的话
		//找出有noshow的订单
		DaDailyauditItemSubtable noShowetcSub = checkIsNeedToUpdateOrderitem(itemSubs,false);
		if(noShowetcSub != null){
			orDailyAuditDao.updateNoshowReason(item.getOrderid(),noShowetcSub.getNoshowCode(),item.getOrderType(),noShowetcSub.getNoshow());
		}
		return ret;
	}
	
	public void assignAuditByMid(String assignToId, String assignToName, String auditIds,
			String loginId,String loginName,int assignType) {
		if(auditIds.endsWith(","))//如果以,后结尾，则去掉它
			auditIds = auditIds.substring(0, auditIds.length()-1);
		orDailyAuditDao.assignAuditByMid(assignToId,  assignToName,  auditIds,
					loginId, loginName, assignType);

	}

	private void getAuditResults(AuditResult[] results, DaDailyauditItemSubtable sub, int index,int audittype) {
		AuditResult result = new AuditResult();
		result.setRoomIndex(sub.getRoomIndex());
		result.setRoomNo(sub.getRoomnumber());
		result.setFellowName(sub.getActualcheckinname());
		result.setOldName(sub.getCheckinname());
		int auditResult = Integer.parseInt(sub.getAuditresults());
		if(audittype == DailyAuditConstant.CHECKINTYPE){
			result.setNoteResult(auditResult);
			if(auditResult == DailyAuditConstant.EXTENDED_STAY)
				result.setNoteResult(DailyAuditConstant.NORMAL_CHECKIN);
		}else{
			//退房时，对不同的结果需要传不同的东西
			if(auditResult == DailyAuditConstant.NORMAL_CHECKOUT 
					|| auditResult == DailyAuditConstant.EXTENDED_STAY){
				//如果是正常退房或延住
				result.setNoteResult(DailyAuditConstant.NORMAL_CHECKIN);
			}else{
				if(auditResult == DailyAuditConstant.BEFORE_CHECKOUT){
					//如果是正常的提前退房，则把提前退房时间传入
					result.setCheckoutDate(sub.getAdvancecheckouttime());
				}else if(auditResult == DailyAuditConstant.NOSHOW){
//					如果是未入住，则应该是在回访中,不应该改变item值
					result.setCheckoutDate(null);
				}
				result.setNoteResult(DailyAuditConstant.NOSHOW);
			}
		}
		results[index] = result;
	}

	private void updatePageDataToDB(DaAuditRoomDetailInfo roomInfo, DaDailyauditItemSubtable sub) {
		sub.setAuditresults(roomInfo.getAuditresults());
		sub.setReciprocal(roomInfo.getReciprocal());
		if(2 == roomInfo.getUpdateDataType()){
			//不止只更新一小部分数据，还需要更新其它数据
			sub.setAffirmnumber(roomInfo.getAffirmnumber());
			sub.setActualcheckinname(roomInfo.getActualcheckinname());
			sub.setRoomnumber(roomInfo.getRoomnumber());
			sub.setNoshow(roomInfo.getNoshow());
			sub.setRemark(roomInfo.getRemark());
			sub.setAdvancecheckouttime(roomInfo.getAdvancecheckouttime());
			int auditResult = Integer.parseInt(roomInfo.getAuditresults());
			if(DailyAuditConstant.NOSHOW != auditResult){
				//如果不是noshow,则应该把noshow原因信息请空
				sub.setNoshowCode(null);
				sub.setNoshow(null);
				sub.setNoshEdit(null);
			}else{
				sub.setNoshowCode(roomInfo.getNoshowCode());
				sub.setNoshow(roomInfo.getNoshow());
				sub.setNoshEdit(roomInfo.getNoshEdit());
			}
		}
	}
	
	public IAuditOrderService getAuditOrderService() {
		return auditOrderService;
	}

	public void setAuditOrderService(IAuditOrderService auditOrderService) {
		this.auditOrderService = auditOrderService;
	}

	public CommunicaterService getCommunicaterService() {
		return communicaterService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public TranslateUtil getTranslateUtil() {
		return translateUtil;
	}

	public void setTranslateUtil(TranslateUtil translateUtil) {
		this.translateUtil = translateUtil;
	}

}
