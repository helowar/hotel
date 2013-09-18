package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.base.manage.RoomStateManage;
import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlQuotaJudge;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.OrLockedRecords;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.ILockedRecordService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class RoomStateProcessByCalendarAction extends PersistenceAction {

	/**
	 * 开始日期
	 */
	private Date beginDate;

	/**
	 * 结束日期
	 */
	private Date endDate;

	/**
	 * 将要处理的星期
	 */
	private String[] week;

	/**
	 * 房型列表
	 */
	private String[] roomType;

	private String roomTypeArray;

	/**
	 * 酒店id
	 */
	private long hotelID;

	private HtlHotel hotel;

	private HotelManage hotelManage;

	private HotelRoomTypeService hotelRoomTypeService;

	private RoomStateManage roomStateManage;

	private ContractManage contractManage;

	private HtlTempQuota tempQuota;

	private int iCount;

	/**
	 * 合同起始日期，只做显示用，
	 */
	private String bD;

	private String isRoomStatusReport;

	// 房态负责人
	private String roomStateManagerStr;

	private String isRoomStatus;

	private String roomState;

	/**
	 * 合同结束日期
	 */
	private String eD;

	/**
	 * 是否青芒果标志 1为是、0为不是 add by guzhijie
	 */
	private String isGreenMangoHotel;

	private String isGreenMangoHotelReport;

	// 酒店，合同，房态，配额等加解锁操作接口
	private ILockedRecordService lockedRecordService;

	/**
	 * 分页 add by shengwei.zuo 2009-10-22 begin
	 */
	private int pageSize = 10;

	private int totalPage;

	private Long totalNum;

	private int pageNo = 1;

	/**
	 * 分页 add by shengwei.zuo 2009-10-22 end
	 */

	//配额manage add by shengwei.zuo 2009-10-21
	private IQuotaManage quotaManage;

	/**
	 * 行数 add by shengwei.zuo hotel 2.9.3
	 */
	private int cutoffDayNum;

	//有配额预警的日期集合  add by shengwei.zuo hotel 2.9.3
	private List quotaDateList;

	// 判断上一页或下一页的按钮是否需要灰选;
	private String isDisablePage;

	static final int lockedType = 02;
	
	static final String WEEKS = "1,2,3,4,5,6,7";

	public List getQuotaDateList() {
		return quotaDateList;
	}

	public void setQuotaDateList(List quotaDateList) {
		this.quotaDateList = quotaDateList;
	}

	public String getBD() {
		return bD;
	}

	public void setBD(String bd) {
		bD = bd;
	}

	public String getED() {
		return eD;
	}

	public void setED(String ed) {
		eD = ed;
	}

	/*
	 * 昨天到今天所有房态更改记录
	 */
	private List dateRoomStatusProcess = new ArrayList();

	/**
	 * 交接事项
	 */
	private String processRemark;

	// 装酒店房型的List
	private List<HtlRoomtype> hRTList = new ArrayList<HtlRoomtype>();

	private RoomControlManage roomControlManage;

	static final String quotaHolderCC = "CC";

	/**
	 * 按天调整房态
	 * 
	 * @return
	 */
	public String processByCalendar() {
		UserWrapper userw = super.getOnlineRoleUser();
		dateRoomStatusProcess = hotelManage.findRoomStatusDateProcess(hotelID);
		int i = dateRoomStatusProcess.size();

		if (0 != i) {
			// HtlRoomStatusProcess htlRoomStatusProcess = new HtlRoomStatusProcess(); 不必要的创建对象
			HtlRoomStatusProcess htlRoomStatusProcess = (HtlRoomStatusProcess) dateRoomStatusProcess.get(i - 1);
			isRoomStatusReport = htlRoomStatusProcess.getIsRoomStatusReport().toString();
			processRemark = htlRoomStatusProcess.getProcessRemark();
		}
		hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
		List<HtlHotelExt> list = hotel.getHtelHotelExt();
		for (HtlHotelExt htlHotelExt :list) { //该for循环不合理
			String roomStateManager = htlHotelExt.getRoomStateManager();
			isGreenMangoHotel = htlHotelExt.getIsGreenMangoHotel(); //不明白为何在循环里面对其赋值,每一次赋值都把之前的覆盖掉了
			request.setAttribute("roomStateManagerStr", roomStateManager);
		}

		setBeginDate(DateUtil.getSystemDate());
		setEndDate(DateUtil.getDate(DateUtil.getSystemDate(), 10));
		super.getParams().put("init", "Y");

		/** 检查酒店房态是否锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
		if (0 < hotelID) {
			OrLockedRecords orLockedRecord = new OrLockedRecords();
			orLockedRecord.setRecordCD(String.valueOf(hotelID));
			orLockedRecord.setLockType(02);
			OrLockedRecords lockedRecords = lockedRecordService.loadLockedRecord(orLockedRecord);
			if (null != lockedRecords) { // 已锁
				String lockerName = lockedRecords.getLockerName();
				String lockerLoginName = lockedRecords.getLockerLoginName();

				if (null != userw && null != userw.getLoginName() && !userw.getLoginName().equals(lockerLoginName)) { // 不是锁定人进入
					String lockedMSG = "此酒店已被锁定，在被解锁之前，只有锁定人才能进入（锁定人：" + lockerName + "[" + lockerLoginName + "]）";
					request.setAttribute("lockedMSG", lockedMSG);
					return "lockedHint1";
				}

			} else {
				if (null != userw) {
					orLockedRecord.setRemark(hotel.getChnName());
					orLockedRecord.setLockerName(userw.getName());
					orLockedRecord.setLockerLoginName(userw.getLoginName());
					orLockedRecord.setLockTime(DateUtil.getSystemDate());
					lockedRecordService.insertLockedRecord(orLockedRecord);
				}
			}
		}
		/** 检查酒店房态是否锁定 add by chenjiajie V2.4 2008-8-26 End* */

		return "processByCalendar";
	}

	/**
	 * 查询有配额预警的房态，临时配额，预警数，预警状态，可用配额总数 add by shengwei.zuo 2009-10-20
	 */
	public String processByRoomControl() {

		//房态
		dateRoomStatusProcess = hotelManage.findRoomStatusDateProcess(hotelID);
		int i = dateRoomStatusProcess.size(); //没必要多定义变量
		if (0 != i) {
			HtlRoomStatusProcess htlRoomStatusProcess = new HtlRoomStatusProcess();
			htlRoomStatusProcess = (HtlRoomStatusProcess) dateRoomStatusProcess.get(i - 1);
			isRoomStatusReport = htlRoomStatusProcess.getIsRoomStatusReport().toString();
			processRemark = htlRoomStatusProcess.getProcessRemark();
		}

		//有配额预警的房型列表
		hRTList = roomControlManage.getRoomTypeHavaForewarn(hotelID);

		if (hRTList == null || hRTList.isEmpty()) {
			return super.forwardMsgAndColse("该酒店的房型没有对应的预警信息！");
		}

		//查询有配额预警的日期列表的总的记录数。
		Map<String, ?> map = quotaManage.queryNewQuotaDateList(hotelID, hRTList, pageNo, pageSize);
		if (null != map.get("query_newquota_detail_list")) {
			quotaDateList = (List) map.get("query_newquota_detail_list");
			cutoffDayNum = quotaDateList.size();
		}
		if (null != map.get("query_newquota_detail_totalnum")) {
			totalNum = (Long) map.get("query_newquota_detail_totalnum");
			totalPage = (int) ((totalNum - 1) / pageSize + 1);
		}

		if (quotaDateList == null || quotaDateList.isEmpty()) {
			return super.forwardMsgAndColse("该酒店的房型没有对应的预警信息");
		}

		//房态，临时配额，以及相关预警信息的list;
		List lstDisplayRoomStates = hotelManage.qryRoomForQuota(hotelID, week, hRTList, quotaDateList, null);

		if (lstDisplayRoomStates == null || lstDisplayRoomStates.isEmpty()) {
			return super.forwardMsgAndColse("该酒店的房型没有对应的预警信息！");
		}

		// 判断页面上 "上一页"和"下一页"的按钮是否需要灰选 add by shengwei.zuo 2009-10-15 begin

		// 如果总的页数等于当前的页数
		if (pageNo == totalPage) {

			// 如果是第一页或者为0，那么页面上"上一页"和"下一页"的按钮都要灰选 
			if (pageNo == 1 || pageNo == 0) {
				isDisablePage = "3";
				// 如果是不是第一页，则是最后一页，那么页面上"下一页"的按钮要灰选
			} else {
				isDisablePage = "2";
			}

		} else {// 如果当前的页数 不等于 总的页数
			// 如果是第一页，那么页面上"上一页"的按钮要灰选
			if (pageNo == 1) {
				isDisablePage = "1";
			}
		}

		// 判断页面上 "上一页"和"下一页"的按钮是否需要灰选 add by shengwei.zuo 2009-10-15 end
		super.getParams().put("init", "N");
		super.getParams().put("roomTypeArray", roomTypeArray);
		super.getParams().put("lstDisplayRoomStates", lstDisplayRoomStates);

		return "processByRoomControl";
	}


	/**
	 * 查询
	 * 
	 * @return
	 */
	public String query() {
		dateRoomStatusProcess = hotelManage.findRoomStatusDateProcess(hotelID);
		int i = dateRoomStatusProcess.size();
		if (0 != i) {
			HtlRoomStatusProcess htlRoomStatusProcess = new HtlRoomStatusProcess();
			htlRoomStatusProcess = (HtlRoomStatusProcess) dateRoomStatusProcess.get(i - 1);
			isRoomStatusReport = htlRoomStatusProcess.getIsRoomStatusReport().toString();
			processRemark = htlRoomStatusProcess.getProcessRemark();
		}
		if (null == roomTypeArray) roomTypeArray = "";
		if (null == roomType || 0 == roomType.length) roomType = roomTypeArray.split(",");
		
		hRTList = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
		List lstDisplayRoomStates = hotelManage.qryRoom(hotelID, beginDate, endDate, week, roomType);
		hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
		List<HtlHotelExt> list = hotel.getHtelHotelExt();
		for (HtlHotelExt htlHotelExt: list) {
			roomStateManagerStr = htlHotelExt.getRoomStateManager();
			isGreenMangoHotel = htlHotelExt.getIsGreenMangoHotel();
		}
		if (StringUtil.isValidStr(isGreenMangoHotelReport)) isGreenMangoHotel = isGreenMangoHotelReport;
		if (StringUtil.isValidStr(roomState)) roomStateManagerStr = roomState;
		if (StringUtil.isValidStr(isRoomStatus)) isRoomStatusReport = isRoomStatus;

		super.getParams().put("init", "N");
		super.getParams().put("roomTypeArray", roomTypeArray);
		super.getParams().put("lstDisplayRoomStates", lstDisplayRoomStates);
		return "query";
	}

	/**
	 * 更新房态
	 * 
	 * @return
	 */
	public String update() {
		HashMap tqMap = new HashMap();
		List lstActiveTempQuota = new ArrayList();
		List lstTempQuotaNew = new ArrayList();
		Map params = super.getParams();
		List<HtlTempQuota> lstTempQuota = MyBeanUtil.getBatchObjectFromParam(params, HtlTempQuota.class, iCount);

		//查询出合同，得到合同中的配额模式，赋值到调整表中  add by shengwei.zuo 2009-10-26
		HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelID);

		if (contractEntity == null) {
			return super.forwardError("该酒店在当前日期没有对应的合同，请先添加合同！");
		}

		String name = super.getOnlineRoleUser().getName();

		String loginName = super.getOnlineRoleUser().getLoginName();
		
		//改用 for each
		for(HtlTempQuota htlTempQuota:lstTempQuota){
			String bedSttu = (String) tqMap.get("" + htlTempQuota.getRoomId());
			if (null != bedSttu) {
				bedSttu += "/" + htlTempQuota.getBedId() + ":" + htlTempQuota.getBedStatus();
			} else {
				bedSttu = htlTempQuota.getBedId() + ":" + htlTempQuota.getBedStatus();
			}
			tqMap.put("" + htlTempQuota.getRoomId(), bedSttu);
			if (null != super.getOnlineRoleUser()) {
				htlTempQuota = (HtlTempQuota) CEntityEvent.setCEntity(htlTempQuota, name, loginName);
			}
			
			//这里将一个模块抽取出来使代码简介
			lstTempQuotaNew.add(assembleHtlQuotaJudge(htlTempQuota,contractEntity));

			lstActiveTempQuota.add(htlTempQuota);
		}
		
		// 更新房态负责人 haibo.li by 2008.12.01 begin
		hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
		List lsHtlHotelExt = hotel.getHtelHotelExt();
		if (0 < lsHtlHotelExt.size()) {
			if (StringUtil.isValidStr(roomStateManagerStr) || StringUtil.isValidStr(isGreenMangoHotel)) {
				HtlHotelExt he = (HtlHotelExt) lsHtlHotelExt.get(0);
				he.setHtlHotel(hotel);
				he.setRoomStateManager(roomStateManagerStr);
				he.setIsGreenMangoHotel(isGreenMangoHotel);
				hotelManage.saveOrUpdateExt(he);
			}
		} else if (0 == lsHtlHotelExt.size()
				&& ((null != roomStateManagerStr && !roomStateManagerStr.trim().equals("")) || (null != isGreenMangoHotel && !("")
						.equals(isGreenMangoHotel.trim())))) {
			if (StringUtil.isValidStr(roomStateManagerStr) || StringUtil.isValidStr(isGreenMangoHotel)) {
				HtlHotelExt he = new HtlHotelExt();
				he.setHtlHotel(hotel);
				he.setRoomStateManager(roomStateManagerStr);
				he.setIsGreenMangoHotel(isGreenMangoHotel);
				hotelManage.saveHtlHotelExt(he);
			}
		}

		// end
		roomStateManage.batchUpdateRoomStatus(assembleRoomStateBean(), tqMap, lstActiveTempQuota);
		//调用方法更新临时配额 add by haibo.li 2.9.3配额改造
		try {
			if (lstTempQuotaNew.size() > 0) {
				String idStr = roomStateManage.batchUpdateTempQuota(lstTempQuotaNew);
				roomStateManage.UpdateTempQuota(idStr); 
			}
			//修改对应的配额信息并记录日志 hotel2.9.3 add by shengwei.zuo 2009-10-19
			if (lstActiveTempQuota != null && !lstActiveTempQuota.isEmpty()) {
				roomStateManage.modifyQuotaNewInfo(lstActiveTempQuota);
			}
		} catch (Exception e) {
			log.error("保存临时配额出现异常", e);
		}

		super.getParams().put("roomTypeArray", roomTypeArray);
		return "update";
	}
	
	/**
	 * 装配 RoomStateBean对象
	 * @return
	 */
	private RoomStateBean assembleRoomStateBean(){
		RoomStateBean roomStateBean = new RoomStateBean();
		roomStateBean.setHotelID(hotelID);
		if (null != super.getOnlineRoleUser()) {
			roomStateBean.setUserName(super.getOnlineRoleUser().getName());
			roomStateBean.setUserId(super.getOnlineRoleUser().getLoginName());
			if (super.getOnlineRoleUser().getLoginName().equals("") || super.getOnlineRoleUser().getName().equals("")) {
				roomStateBean.setUserName(super.getBackUserName());
				roomStateBean.setUserId(super.getBackLoginName());
			}
		}
		roomStateBean.setIsRoomStatusReport(isRoomStatusReport);

		roomStateBean.setProcessRemark(processRemark);
		
		return roomStateBean;
	}
	
	/**
	 * 装配 HtlQuotaJudge对象
	 * @param htlTempQuota
	 * @param contractEntity
	 * @return
	 */
	private HtlQuotaJudge assembleHtlQuotaJudge(HtlTempQuota htlTempQuota,HtlContract contractEntity){
		HtlQuotaJudge htlQuotaJudge = new HtlQuotaJudge();
		htlQuotaJudge.setBedId(new Long(htlTempQuota.getBedId()));//床型
		htlQuotaJudge.setCutofftime(htlTempQuota.getCutofftime());//cutofftime
		htlQuotaJudge.setCutoffday(new Long(0));//临时配额直接写入0
		htlQuotaJudge.setQuotaPattern(contractEntity.getQuotaPattern() == null ? HotelBaseConstantBean.QUOTAPATTERNSI
				: contractEntity.getQuotaPattern());//配额模式
		htlQuotaJudge.setHotelId(hotelID);//酒店
		htlQuotaJudge.setQuotaHolder(quotaHolderCC);//
		htlQuotaJudge.setRoomtypeId(htlTempQuota.getRoomTypeId());//房型ID
		htlQuotaJudge.setStartDate(htlTempQuota.getStartDate());//开始时间
		htlQuotaJudge.setEndDate(htlTempQuota.getStartDate());//结束时间
		htlQuotaJudge.setQuotaType(HotelBaseConstantBean.TEMPQUOTA);//配额类型
		htlQuotaJudge.setQuotaChannel(HotelBaseConstantBean.CC);//配额渠道
		htlQuotaJudge.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPE);
		//addQuotaQty 是页面中增加配额 ,delQuotaQty是页面中的减少配额
		htlQuotaJudge.setQuotaCutoffDayNewId(htlTempQuota.getQuotaCutoffDayNewId());//配额明细ID 有则加 没有则无
		htlQuotaJudge.setBlnBack(1L);
		htlQuotaJudge.setJudgeWeeks(WEEKS);
		if (htlTempQuota.getAddQuotaQty() != 0 && htlTempQuota.getDelQuotaQty() != 0) {
			if (htlTempQuota.getAddQuotaQty() > htlTempQuota.getDelQuotaQty()) {
				long qty = htlTempQuota.getAddQuotaQty() - htlTempQuota.getDelQuotaQty();
				htlQuotaJudge.setQuotaNum(qty);
				htlQuotaJudge.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_ADD);
			} else if (htlTempQuota.getAddQuotaQty() < htlTempQuota.getDelQuotaQty()) {
				long qty = htlTempQuota.getDelQuotaQty() - htlTempQuota.getAddQuotaQty();
				htlQuotaJudge.setQuotaNum(qty);
				htlQuotaJudge.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_DEC);
			} else if (htlTempQuota.getAddQuotaQty() == htlTempQuota.getDelQuotaQty()) {
				htlQuotaJudge.setQuotaNum(new Long(htlTempQuota.getQuotaQty()));//配额数
			}

		}

		if (htlTempQuota.getAddQuotaQty() != 0 && htlTempQuota.getDelQuotaQty() == 0) {//判断是增加配额还是减少配额
			htlQuotaJudge.setQuotaNum(htlTempQuota.getAddQuotaQty());
			htlQuotaJudge.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_ADD);
		} else if (htlTempQuota.getAddQuotaQty() == 0 && htlTempQuota.getDelQuotaQty() != 0) {
			htlQuotaJudge.setQuotaNum(htlTempQuota.getDelQuotaQty());
			htlQuotaJudge.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_DEC);
		} else if (htlTempQuota.getAddQuotaQty() == 0 && htlTempQuota.getDelQuotaQty() == 0) {
			htlQuotaJudge.setQuotaNum(new Long(htlTempQuota.getQuotaQty()));//配额数
			htlQuotaJudge.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_UTP);
		}

		if (null != super.getOnlineRoleUser()) { //登陆人员
			htlQuotaJudge.setOperatorId(super.getOnlineRoleUser().getName());
			htlQuotaJudge.setOperatorName(super.getOnlineRoleUser().getLoginName());
			htlQuotaJudge.setOperatorDept("HBIZ");
			htlQuotaJudge.setOperatorTime(new Date());

			htlTempQuota.setOperatorId(super.getOnlineRoleUser().getName());
			htlTempQuota.setOperatorName(super.getOnlineRoleUser().getLoginName());
			htlTempQuota.setOperatorDept("HBIZ");
			htlTempQuota.setOperatorTime(new Date());

		}
		return htlQuotaJudge;
	}

	/**
	 * 返回查询，并解锁
	 * 
	 * @return
	 */
	public String backToList() {
		Map params = super.getParams();
		String hotelId = (String) params.get("hotelId");
		/** 解除酒店房态锁定 **/
		lockedRecordService.deleteLockedRecordTwo(hotelId, lockedType, super.getOnlineRoleUser().getLoginName());
		return "query";
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getHotelID() {
		return hotelID;
	}

	public void setHotelID(long hotelID) {
		this.hotelID = hotelID;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public String[] getRoomType() {
		return roomType;
	}

	public void setRoomType(String[] roomType) {
		this.roomType = roomType;
	}

	public String[] getWeek() {
		return week;
	}

	public void setWeek(String[] week) {
		this.week = week;
	}

	public HtlHotel getHotel() {
		return hotel;
	}

	public void setHotel(HtlHotel hotel) {
		this.hotel = hotel;
	}

	public HtlTempQuota getTempQuota() {
		return tempQuota;
	}

	public void setTempQuota(HtlTempQuota tempQuota) {
		this.tempQuota = tempQuota;
	}

	public int getICount() {
		return iCount;
	}

	public void setICount(int count) {
		iCount = count;
	}

	public RoomStateManage getRoomStateManage() {
		return roomStateManage;
	}

	public void setRoomStateManage(RoomStateManage roomStateManage) {
		this.roomStateManage = roomStateManage;
	}

	public List getDateRoomStatusProcess() {
		return dateRoomStatusProcess;
	}

	public void setDateRoomStatusProcess(List dateRoomStatusProcess) {
		this.dateRoomStatusProcess = dateRoomStatusProcess;
	}

	public String getProcessRemark() {
		return processRemark;
	}

	public void setProcessRemark(String processRemark) {
		this.processRemark = processRemark;
	}

	public String getRoomTypeArray() {
		return roomTypeArray;
	}

	public void setRoomTypeArray(String roomTypeArray) {
		this.roomTypeArray = roomTypeArray;
	}

	public ILockedRecordService getLockedRecordService() {
		return lockedRecordService;
	}

	public void setLockedRecordService(ILockedRecordService lockedRecordService) {
		this.lockedRecordService = lockedRecordService;
	}

	public List<HtlRoomtype> getHRTList() {
		return hRTList;
	}

	public void setHRTList(List<HtlRoomtype> list) {
		hRTList = list;
	}

	public RoomControlManage getRoomControlManage() {
		return roomControlManage;
	}

	public void setRoomControlManage(RoomControlManage roomControlManage) {
		this.roomControlManage = roomControlManage;
	}

	public String getIsRoomStatusReport() {
		return isRoomStatusReport;
	}

	public void setIsRoomStatusReport(String isRoomStatusReport) {
		this.isRoomStatusReport = isRoomStatusReport;
	}

	public String getRoomStateManagerStr() {
		return roomStateManagerStr;
	}

	public void setRoomStateManagerStr(String roomStateManagerStr) {
		this.roomStateManagerStr = roomStateManagerStr;
	}

	public String getIsRoomStatus() {
		return isRoomStatus;
	}

	public void setIsRoomStatus(String isRoomStatus) {
		this.isRoomStatus = isRoomStatus;
	}

	public String getRoomState() {
		return roomState;
	}

	public void setRoomState(String roomState) {
		this.roomState = roomState;
	}

	public String getIsGreenMangoHotel() {
		return isGreenMangoHotel;
	}

	public void setIsGreenMangoHotel(String isGreenMangoHotel) {
		this.isGreenMangoHotel = isGreenMangoHotel;
	}

	public String getIsGreenMangoHotelReport() {
		return isGreenMangoHotelReport;
	}

	public void setIsGreenMangoHotelReport(String isGreenMangoHotelReport) {
		this.isGreenMangoHotelReport = isGreenMangoHotelReport;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public IQuotaManage getQuotaManage() {
		return quotaManage;
	}

	public void setQuotaManage(IQuotaManage quotaManage) {
		this.quotaManage = quotaManage;
	}

	public int getCutoffDayNum() {
		return cutoffDayNum;
	}

	public void setCutoffDayNum(int cutoffDayNum) {
		this.cutoffDayNum = cutoffDayNum;
	}

	public String getIsDisablePage() {
		return isDisablePage;
	}

	public void setIsDisablePage(String isDisablePage) {
		this.isDisablePage = isDisablePage;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
