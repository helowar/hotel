package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.manage.assistant.QuotaBathAdjust;
import com.mangocity.hotel.base.manage.assistant.QuotaJudgeCalendar;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlQuotaJudge;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 
 * @author yangshaojun
 */
public class QuotaJudgeByCalendarAction extends PersistenceAction {

	private static final long serialVersionUID = -3810758154624566257L;

	private Long hotelId;

	private Long hotelNo;

	private List quotaList;

	private int pageSize = 10;

	private int totalPage;

	private Long totalNum;

	private int pageNo = 1;

	private Long quotaId;

	private Long quotaType;

	private Long roomTypeId;

	private String quotaHolder;

	private Long shareType;

	private Long bedType;

	private Long forewarn;

	/**
	 * 配额批次开始日期
	 */
	private Date beginDate;

	/**
	 * 配额批次结束日期
	 */
	private Date endDate;

	// add by shengwei.zuo hotel 2.9.3 配额调整操作实体类的List;
	private List lstInputCDay;

	private int rowIndex;
	/**
	 * cutoff行数 add by shengwei.zuo hotel 2.9.3
	 */
	private int cutoffDayNum;

	// add by shengwei.zuo hotel 2.9.3 房型实体;
	private HtlRoomtype htlRoomtype;

	// 房型ID add by shengwei.zuo hotel 2.9.3 房型ID;
	private long roomType;

	// 配额是否可以回收 add by shengwei.zuo hotel 2.9.3
	private long blnBack;

	// 返回的标记 add by shengwei.zuo hotel 2.9.3
	private String backToFlag;

	// 按日历查询是不是第一次查询
	private String isFirsQry;

	/**
	 * 该房型下面配额是否存在共享
	 */
	private HotelManage hotelManage;

	private ContractManage contractManage;

	/**
	 * 配额模式（进店--表示入住的第一天扣配额、在店--表示入住的每天都要扣配额）
	 */
	private String quotaPattern;

	private List roomTypePriceTypeLis;

	// 合同的付款方式
	private String contractPay;

	private String backQuota;

	private HtlHotel hotel;

	private String takebackQuota;

	private int rowCount;

	private IQuotaManage quotaManage;

	private HotelRoomTypeService hotelRoomTypeService;

	// 床型list
	private List lstBedEntity;

	// 选择的房型类型ID;
	private Long selectRoomTypeId;

	// 是否是查询的配额报表
	private String isQueryReport;

	// 判断上一页或下一页的按钮是否需要灰选;
	private String isDisablePage;

	private String hotelName;

	// 从合同页面带过来的合同开始日期
	private Date bD;

	// 从合同页面带过来的合同结束日期
	private Date eD;

	// 是否从合同页面直接跳转过来的。
	private String isFromContract;

	// 合同ID;
	private Long contractId;

	private String[] week;

	private String weeks;

	// 索引号 add by shengwei.zuo 2010-1-26
	private Long rowsIndex;

	// 合同页面的付款方法
	private String payment_Method;

	private String returnFlag;

	/**
	 * 配额实时报表查询页面，根据酒店ID查询酒店信息
	 * 
	 * @return
	 */
	public String quotaRealTimeReport() {
		hotel = hotelManage.findHotel(hotelId);
		/** qc 632 修改了return  add by  xiaowei.wang */
		return returnFlag;
		//return "toRealTimeReport";
	}

	/**
	 * 按日历调整配额查询
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryQuota() {
		// 获取指定酒店的房型信息
		List<HtlRoomtype> list = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
		Map<Long, Long> roomTypeMap = new HashMap<Long, Long>(list.size());
		// 根据房型ID，设置配额床型共享
		for (HtlRoomtype htlRoomType : list) {
			roomTypeMap.put(htlRoomType.getID(), htlRoomType.getQuotaBedShare());
		}
		request.setAttribute("roomTypeMap", roomTypeMap);

		// 查询配额明细
		HtlQuotaJudge  quotaJudgeVO = this.packageQuotaJudgeVO();
		Map map = quotaManage.queryNewQuotaDetail(quotaJudgeVO);
		showPageNum(map);
		isDisablePage = quotaManage.showPagination( map, quotaList, cutoffDayNum, totalNum, totalPage, pageSize, pageNo);
		hotel = hotelManage.findHotel(hotelId);
		weeks = BizRuleCheck.ArrayToString(week);

		// 判断页面上 "上一页"和"下一页"的按钮是否需要灰选 add by shengwei.zuo 2009-10-15 end
		if (null != isFirsQry) {
			// 如果不为空，就说明是从合同页面直接跳转过来的。add by shengwei.zuo 2009-09-16 hotel 2.9.3			
			HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelId);
			if (null == contractEntity) {
				return super.forwardError("该酒店在当前日期没有对应的合同，请先添加合同！");
			}			
			
			request.setAttribute("contractId", contractEntity.getID());
			HtlQuotaJudge quotaJudg = new HtlQuotaJudge();
			quotaJudg.setContractId(contractEntity.getID());
			
			Map params = super.getParams();
			quotaJudg.setHotelId(Long.parseLong((String) params.get("hotelId")));
			quotaJudg.setContractBd(contractEntity.getBeginDate());
			quotaJudg.setContractEd(contractEntity.getEndDate());
			quotaJudg.setQuotaPattern(contractEntity.getQuotaPattern());
			super.setEntity(quotaJudg);
		}
		
		request.setAttribute("changePriceHint", null != hotel ? hotel.getChangePriceHint() :"");
		
		return "toCalendar";
	}

	/**
	 * 计算酒店多少家酒店预警
	 * 
	 * @return
	 */
	public String countHotelQuotaForwarnNum() {
		forewarn = quotaManage.countHotelQuotaForwarnNum();
		return "toHotelQuotaForwarnNum";
	}

	/**
	 * 预警条件查询
	 * 
	 * @return
	 */
	public String queryNewQuotaByForwarn() {
		Map map = quotaManage.queryNewQuotaByForwarn(hotelNo, beginDate, endDate, roomType, quotaHolder, shareType,
				bedType, forewarn, pageNo, pageSize);
		showPageNum(map);
		isDisablePage=quotaManage.showPagination( map, quotaList, cutoffDayNum, totalNum, totalPage, pageSize, pageNo);
		hotel = hotelManage.findHotel(hotelNo);
		return "toQuotaForewarnByCalendar";
	}

	/**
	 * 根据条件调整配额整
	 * 
	 * @return
	 */
	public String setNewQuotaByForwarn() {
		Map params = super.getParams();
		quotaList = MyBeanUtil.getBatchObjectFromParam(params, HtlQuotaNew.class, cutoffDayNum);
		quotaManage.setQuotaForwarn(quotaList);
		return queryNewQuotaByForwarn();
	}

	/**
	 * 按日历调整预览
	 * 
	 * @return
	 */
	public String viewJudgeQuota() {
		Map params = super.getParams();
		hotel = hotelManage.findHotel(hotelId);
		try {
			quotaList = MyBeanUtil.getBatchObjectFromParam(params, QuotaJudgeCalendar.class, cutoffDayNum);
		} catch (Exception e) {
			log.error("QuotaJudgeByCalendarAction.judgeQuota error : ", e);
		}

		return "toJudgeQuota";
	}

	/**
	 * 按日历调整配额
	 * 
	 * @return
	 */
	public String judgeQuota() {
		Map params = super.getParams();
		try {
			quotaList = MyBeanUtil.getBatchObjectFromParam(params,
					QuotaJudgeCalendar.class, cutoffDayNum);
			Long hotelId = Long.parseLong((String) params.get("hotelId"));
			List<HtlQuotaJudge> list = new ArrayList<HtlQuotaJudge>();
			UserWrapper user = super.getOnlineRoleUser();
			for (Iterator i = quotaList.iterator(); i.hasNext();) {
				QuotaJudgeCalendar quota = (QuotaJudgeCalendar) i.next();
				HtlQuotaJudge quotaJudge = new HtlQuotaJudge();
				quotaJudge.setHotelId(hotelId);
				quotaJudge.setBedId(quota.getBedId());
				quotaJudge.setCutoffday(quota.getCutoffday());
				quotaJudge.setCutofftime(quota.getCutofftime());
				quotaJudge.setEndDate(quota.getAbleDate());
				quotaJudge.setStartDate(quota.getAbleDate());
				Date curDate = DateUtil.getDate(new Date());
				if(quotaJudge.getStartDate().getTime()<=curDate.getTime()){
					quotaJudge.setStartDate(curDate);
				}
				if(user!=null&&user.getId()!=null){
				    quotaJudge.setOperatorId(String.valueOf(user.getId()));
				}
				if(user!=null&&user.getName()!=null){
				    quotaJudge.setOperatorName(user.getName());
				}
				quotaJudge.setOperatorTime(new Date());
				quotaJudge.setQuotaChannel("HBIZ");
				quotaJudge.setQuotaCutoffDayNewId(quota.getQuotaDetailId());
				quotaJudge.setQuotaHolder(quota.getQuotaHolder());
				quotaJudge.setJudgeType(quota.getJudgeTypeStr());
				quotaJudge.setQuotaPattern(quota.getQuotaPattern());
				quotaJudge.setQuotaShare(quota.getQuotaShare());
				quotaJudge.setQuotaType(quota.getQuotaType());
				quotaJudge.setRoomtypeId(quota.getRoomTypeIds());
				quotaJudge.setBlnBack(quota.getBlnBack());
				//添加星期
				quotaJudge.setJudgeWeeks("1,2,3,4,5,6,7");
				if (null != quota.getQuotaNum() && quota.getQuotaNum() > 0) {
					quotaJudge.setQuotaNum(quota.getQuotaNum());
					list.add(quotaJudge);
				} else {
					if ((!quota.getOldCutoffday().equals(quota.getCutoffday()))
							|| (!quota.getCutofftime().equals(quota
									.getOldCutoffTime()))) {
						quotaJudge.setQuotaNum(quota.getOldQuotaNum());
						quotaJudge
								.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_UTP);
						list.add(quotaJudge);
					}
				}
			}
			quotaManage.judgeQuota(list);
		} catch (Exception e) {
			log.error("QuotaJudgeByCalendarAction.judgeQuota error : ", e);
		}
		
		//保存并跳转到合同页面 add by shengwei.zuo 2009-10-18
		if ("toContract".equals(backToFlag)){
			
			return backToFlag; 
			
		}
		
		return queryQuota();
	}

	/**
	 * 批量配额预警查询
	 */
	public String queryQuotaForewarn() {
		// 如果不为空，就说明是从合同页面直接跳转过来的。add by shengwei.zuo 2009-09-16 hotel 2.9.3
		if (null != isFromContract && !"".equals(isFromContract)) {
			if (contractId == null || "".equals(contractId.toString())) {
				return super.forwardError("未获取到合同ID！");
			}
		} else {			
			HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelId);
			if (null == contractEntity) {
				return super.forwardError("该酒店在当前日期没有对应的合同，请先添加合同！");
			}
			contractId = contractEntity.getID();
			Map params = super.getParams();
			hotelId = Long.parseLong((String) params.get("hotelId"));
			bD = contractEntity.getEndDate();
			eD = contractEntity.getEndDate();
			quotaPattern = contractEntity.getQuotaPattern();
		}
		hotel = hotelManage.findHotel(hotelId);
		HtlQuotaJudge quotaJudg = new HtlQuotaJudge();
		quotaJudg.setContractId(contractId);
		quotaJudg.setHotelId(hotelId);
		quotaJudg.setContractBd(bD);
		quotaJudg.setContractEd(eD);
		quotaJudg.setQuotaPattern(quotaPattern);
		super.setEntity(quotaJudg);
		return "toQuotaForwarn";
	}

	/**
	 * 批量调整配额预警
	 * 
	 * @return
	 */
	public String setQuotaForwarn() {		
		UserWrapper user = super.getOnlineRoleUser();
		if (null == user) {
			return super.forwardError("获取登陆用户信息失效,请重新登陆");
		}
		Map params = super.getParams();
		quotaManage.setBathQuotaForwarn(params, user, cutoffDayNum);
		return queryQuotaForewarn();
	}

	/**
	 * 本部强制解除预警
	 * 
	 * @return
	 */
	public String setQuotaFreeForwarnFromBenbu() {		
		UserWrapper user = super.getOnlineRoleUser();
		if (null == user) {
			return super.forwardError("获取登陆用户信息失效,请重新登陆");
		}
		Map params = super.getParams();
		quotaManage.setFreeQuotaForwarn(params, user, rowCount);
		return queryQuotaForewarn();
	}

	/**
	 * 房控强制解除预警
	 * 
	 * @return
	 */
	public String setQuotaFreeForwarn() {		
		UserWrapper user = super.getOnlineRoleUser();
		if (null == user) {
			return super.forwardError("获取登陆用户信息失效,请重新登陆");
		}
		
		Map params = super.getParams();
		quotaManage.setFreeQuotaForwarn(params, user, cutoffDayNum);
		return toQuotaForwarnFromRoom();
	}

	// 批量调整配额的查询页面 add by shengwei.zuo 2009-09-11
	public String bathAdjustQuotaQuery() {
		// 如果不为空，就说明是从合同页面直接跳转过来的。add by shengwei.zuo 2009-09-16 hotel 2.9.3
		if (null != isFromContract && !"".equals(isFromContract)) {
			if (contractId == null || "".equals(contractId.toString())) {
				return super.forwardError("非法的合同ID！");
			}
		} else {
			HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelId);
			if (contractEntity == null) {
				return super.forwardError("该酒店在当前日期没有对应的合同，请先添加合同！");
			}
			payment_Method = contractEntity.getPaymentMethod();
			// 根据合同中的付款方法,判断 配额中默认的共享方式 add by shengwei.zuo 2009-11-10 end
			contractId = contractEntity.getID();
			Map params = super.getParams();
			hotelId = Long.parseLong((String) params.get("hotelId"));
			bD = contractEntity.getBeginDate();
			eD = contractEntity.getEndDate();
			quotaPattern = contractEntity.getQuotaPattern();
		}
		HtlQuotaJudge quotaJudg = new HtlQuotaJudge();
		quotaJudg.setContractId(contractId);
		quotaJudg.setHotelId(hotelId);
		quotaJudg.setContractBd(bD);
		quotaJudg.setContractEd(eD);
		quotaJudg.setQuotaPattern(quotaPattern);

		// 根据合同中的付款方法,判断 配额中默认的共享方式 
		shareType = this.validateShareType(payment_Method);
		
		hotel = hotelManage.findHotel(hotelId);
		super.setEntity(quotaJudg);
		request.setAttribute("changePriceHint", null != hotel ? hotel.getChangePriceHint() :"");
		return "toBathAdjustQuota";
	}

	/**
	 * 从房控转入批量调整临时配额
	 * 
	 * @return
	 */
	public String tempJudge() {		
		HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelId);
		if (contractEntity == null) {
			return super.forwardError("该酒店在当前日期没有对应的合同，请先添加合同！");
		}
		
		HtlQuotaJudge quotaJudg = new HtlQuotaJudge();
		quotaJudg.setContractId(contractEntity.getID());
		Map params = super.getParams();
		quotaJudg.setHotelId(Long.parseLong((String) params.get("hotelId")));
		quotaJudg.setContractBd(contractEntity.getBeginDate());
		quotaJudg.setContractEd(contractEntity.getEndDate());
		quotaJudg.setQuotaPattern(contractEntity.getQuotaPattern());
		hotel = hotelManage.findHotel(hotelId);
		super.setEntity(quotaJudg);
		return "toTempAdjustQuota";
	}

	/**
	 * 从房控转入到批量配额预警界面
	 * 
	 * @return
	 */
	public String toQuotaForwarnFromRoom() {
		HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelId);
		if (contractEntity == null) {
			return super.forwardError("该酒店在当前日期没有对应的合同，请先添加合同！");
		}
		
		HtlQuotaJudge quotaJudg = new HtlQuotaJudge();
		Map params = super.getParams();
		quotaJudg.setContractId(contractEntity.getID());
		quotaJudg.setHotelId(Long.parseLong((String) params.get("hotelId")));
		quotaJudg.setContractBd(contractEntity.getBeginDate());
		quotaJudg.setContractEd(contractEntity.getEndDate());
		quotaJudg.setQuotaPattern(contractEntity.getQuotaPattern());

		hotel = hotelManage.findHotel(hotelId);
		super.setEntity(quotaJudg);

		return "toQuotaForwarnFromRoom";
	}

	// 批量调整配额时的预览 add by shengwei.zuo hotel 2.9.3
	public String review() {
		Map params = super.getParams();
		lstInputCDay = MyBeanUtil.getBatchObjectFromParam(params, QuotaBathAdjust.class, cutoffDayNum);
		rowCount = lstInputCDay.size();
		htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(roomType);
		hotel = hotelManage.findHotel(hotelId);
		return "reviewBathAdjustQuota";
	}

	/**
	 * 房态批量临时配额调整
	 */
	public String reviewTemp() {
		Map params = super.getParams();
		lstInputCDay = MyBeanUtil.getBatchObjectFromParam(params, QuotaBathAdjust.class, cutoffDayNum);
		rowCount = lstInputCDay.size();
		// 通过房型ID获取房型信息
		htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(roomType);
		hotel = hotelManage.findHotel(hotelId);
		return "reviewBathAdjustTempQuota";
	}

	// 批量调整配额时的保存 add by shengwei.zuo hotel 2.9.3
	public String bathJudgeQuota() {
		Map params = super.getParams();
		List lstInputCutoffDay = MyBeanUtil.getBatchObjectFromParam(params,
				QuotaBathAdjust.class, cutoffDayNum);

		for (int i = 0; i < lstInputCutoffDay.size(); i++) {

			QuotaBathAdjust quotaBathAdjustObj = (QuotaBathAdjust) lstInputCutoffDay
					.get(i);

			HtlQuotaJudge quotaJudgeObj = new HtlQuotaJudge();
			Date curDate = DateUtil.getDate(new Date());
			if(quotaBathAdjustObj.getCutoffBeginDate().getTime()>=curDate.getTime()){
				quotaJudgeObj.setStartDate(quotaBathAdjustObj.getCutoffBeginDate());
			}else{
				quotaJudgeObj.setStartDate(curDate);
			}
			quotaJudgeObj.setEndDate(quotaBathAdjustObj.getCutoffEndDate());

			quotaJudgeObj.setHotelId(hotelId);
			quotaJudgeObj.setRoomtypeId(Long.parseLong(quotaBathAdjustObj
					.getRoomTypeId()));

			// 床型ID
			quotaJudgeObj.setBedId(quotaBathAdjustObj.getBedId());
			// 配额类型
			quotaJudgeObj.setQuotaType(quotaType.toString());
			// 配额共享模式
			quotaJudgeObj.setQuotaShare(shareType.toString());

			// 配额过期日期
			quotaJudgeObj.setCutoffday(quotaBathAdjustObj.getCutoffDay());
			// 配额过期时间
			quotaJudgeObj.setCutofftime(quotaBathAdjustObj.getCutoffTime());

			// 配额调整方式
			quotaJudgeObj.setJudgeType(quotaBathAdjustObj.getJudgeType());

			// 配额数
			quotaJudgeObj.setQuotaNum(quotaBathAdjustObj.getQuotaQty());

			//星期 add by guojun 2009-12-15
			quotaJudgeObj.setJudgeWeeks(quotaBathAdjustObj.getWeeks());
			
			// 是否可退
			if (null != takebackQuota && !"".equals(takebackQuota)) {

				quotaJudgeObj.setBlnBack(Long.parseLong(takebackQuota));

			}

			// 配额所有者
			quotaJudgeObj.setQuotaHolder(quotaHolder);

			quotaJudgeObj.setOperatorTime(DateUtil.getSystemDate());

			quotaJudgeObj.setQuotaPattern(quotaPattern);

			if (null != super.getOnlineRoleUser()) {

				quotaJudgeObj.setOperatorName(super.getOnlineRoleUser()
						.getName());
				quotaJudgeObj.setOperatorId(super.getOnlineRoleUser()
						.getLoginName());

			} else {

				return super.forwardError("获取登陆用户信息失效,请重新登陆");

			}

			quotaManage.sateQuotaJudge(quotaJudgeObj);

			try {
				// 在调存储过程FUN_QUOTAREFACTOR
				quotaManage.saveQuotaJudgeNew(quotaJudgeObj);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}

		}
		//保存并跳转到合同页面 add by shengwei.zuo 2009-10-18
		if ("toContract".equals(backToFlag)){
			
			return backToFlag; 
			
		}
		if(("toTempAdjustQuota").equals(backToFlag)){
			return tempJudge();
		}
		
		return quotaRealTimeReport();
	}

	// 预览页面点击“返回 ”按钮 add by shengwei.zuo
	public String returnback() {
		Map params = super.getParams();
		lstInputCDay = MyBeanUtil.getBatchObjectFromParam(params, QuotaBathAdjust.class, cutoffDayNum);

		//共享方式混乱，modify by zhineng.zhuang
		shareType = Long.parseLong(contractPay);
		HtlQuotaJudge quotaJu = new HtlQuotaJudge();
		quotaJu.setContractId(Long.parseLong((String) params.get("contractId")));
		quotaJu.setHotelId(Long.parseLong((String) params.get("hotelId")));
		quotaJu.setQuotaPattern((String) params.get("quotaPattern"));
		quotaJu.setContractBd(beginDate);
		quotaJu.setContractEd(endDate);
		if (backQuota != null && !"".equals(backQuota)) {
			quotaJu.setBlnBack(Long.parseLong(backQuota));
		}
		super.setEntity(quotaJu);
		hotel = hotelManage.findHotel(hotelId);
		return "toBathAdjustQuota";
	}

	// 根据房型ID得到其床型列表 ajax 调用 add by shengwei.zuo 2009-10-12
	public String getBedLstByRoomId() {
		if (StringUtil.isValidStr(selectRoomTypeId.toString())) {
			lstBedEntity = hotelRoomTypeService.queryBedsByRoomId(selectRoomTypeId);
		}
		return "bedNameShow";
	}

	/**
	 * 批量解除预警
	 */
	public String getFreeBedLstByRoomId() {
		if (StringUtil.isValidStr(selectRoomTypeId.toString())) {
			lstBedEntity = hotelRoomTypeService.queryBedsByRoomId(selectRoomTypeId);
		}
		return "bedNameShowFree";
	}

	// 进入合同修改页面 add by shengwei.zuo 2009-10-20
	public String toContract() {
		HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelId);
		if (contractEntity == null) {
			return super.forwardError("该酒店在当前日期没有对应的合同，请先添加合同！");
		}
		// 合同ID
		contractId = contractEntity.getID();
		return "toContract";
	}
	
	// 根据合同中的付款方法,判断 配额中默认的共享方式 
	public Long  validateShareType(String payment_Method){
		Long  shareType =0L;
		if ("1".equals(payment_Method)) {
			shareType = 3L;
		} else if ("2".equals(payment_Method)) {
			shareType = 1L;
		} else if ("3".equals(payment_Method) || "4".equals(payment_Method)) {
			shareType = 2L;
		}
		return shareType;
	}
	
	/**
	 * 封装配额调整的参数
	 * @return
	 */
	public HtlQuotaJudge  packageQuotaJudgeVO(){
		HtlQuotaJudge  quotaJudgeVO  = new  HtlQuotaJudge();
		quotaJudgeVO.setID(quotaId);
		quotaJudgeVO.setHotelId(hotelId);
		quotaJudgeVO.setStartDate(beginDate);
		quotaJudgeVO.setEndDate(endDate);
		if(null !=quotaType ){
			quotaJudgeVO.setQuotaType(String.valueOf(quotaType));
		}
		quotaJudgeVO.setRoomtypeId(roomTypeId);
		quotaJudgeVO.setQuotaHolder(quotaHolder);
		if(null != shareType){
			quotaJudgeVO.setQuotaShare(String.valueOf(shareType));
		}
		quotaJudgeVO.setBedId(bedType);
		quotaJudgeVO.setWeeks(week);
		quotaJudgeVO.setPageNo(pageNo);
		quotaJudgeVO.setPageSize(pageSize);
		return  quotaJudgeVO;
	}
	
	
	private void showPageNum(Map map){
		//Map map = quotaManage.queryNewQuotaDetail(quotaJudgeVO);
		if ((map.containsKey("query_newquota_detail_list"))
				&& (null != map.get("query_newquota_detail_list"))) {
			quotaList = (List) map.get("query_newquota_detail_list");
			cutoffDayNum = quotaList.size();
		}
		if ((map.containsKey("query_newquota_detail_totalnum"))
				&& (null != map.get("query_newquota_detail_totalnum"))) {
			totalNum = (Long) map.get("query_newquota_detail_totalnum");
			//totalNum = total.longValue();
			totalPage = (int) ((totalNum - 1) / pageSize + 1);
		}
		
	}
	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public List getQuotaList() {
		return quotaList;
	}

	public void setQuotaList(List quotaList) {
		this.quotaList = quotaList;
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

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
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

	public Long getQuotaId() {
		return quotaId;
	}

	public void setQuotaId(Long quotaId) {
		this.quotaId = quotaId;
	}

	public Long getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(Long quotaType) {
		this.quotaType = quotaType;
	}

	public Long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getQuotaHolder() {
		return quotaHolder;
	}

	public void setQuotaHolder(String quotaHolder) {
		this.quotaHolder = quotaHolder;
	}

	public Long getShareType() {
		return shareType;
	}

	public void setShareType(Long shareType) {
		this.shareType = shareType;
	}

	public void setQuotaManage(IQuotaManage quotaManage) {
		this.quotaManage = quotaManage;
	}

	public List getLstInputCDay() {
		return lstInputCDay;
	}

	public void setLstInputCDay(List lstInputCDay) {
		this.lstInputCDay = lstInputCDay;
	}

	public int getCutoffDayNum() {
		return cutoffDayNum;
	}

	public void setCutoffDayNum(int cutoffDayNum) {
		this.cutoffDayNum = cutoffDayNum;
	}

	public HtlRoomtype getHtlRoomtype() {
		return htlRoomtype;
	}

	public void setHtlRoomtype(HtlRoomtype htlRoomtype) {
		this.htlRoomtype = htlRoomtype;
	}

	public long getRoomType() {
		return roomType;
	}

	public void setRoomType(long roomType) {
		this.roomType = roomType;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String getQuotaPattern() {
		return quotaPattern;
	}

	public void setQuotaPattern(String quotaPattern) {
		this.quotaPattern = quotaPattern;
	}

	public long getBlnBack() {
		return blnBack;
	}

	public void setBlnBack(long blnBack) {
		this.blnBack = blnBack;
	}

	public List getRoomTypePriceTypeLis() {
		return roomTypePriceTypeLis;
	}

	public void setRoomTypePriceTypeLis(List roomTypePriceTypeLis) {
		this.roomTypePriceTypeLis = roomTypePriceTypeLis;
	}

	public String getContractPay() {
		return contractPay;
	}

	public void setContractPay(String contractPay) {
		this.contractPay = contractPay;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public String getBackQuota() {
		return backQuota;
	}

	public void setBackQuota(String backQuota) {
		this.backQuota = backQuota;
	}

	public String getTakebackQuota() {
		return takebackQuota;
	}

	public void setTakebackQuota(String takebackQuota) {
		this.takebackQuota = takebackQuota;
	}

	public Date getBD() {
		return bD;
	}

	public void setBD(Date bd) {
		bD = bd;
	}

	public Date getED() {
		return eD;
	}

	public void setED(Date ed) {
		eD = ed;
	}

	public String getIsFromContract() {
		return isFromContract;
	}

	public void setIsFromContract(String isFromContract) {
		this.isFromContract = isFromContract;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public List getLstBedEntity() {
		return lstBedEntity;
	}

	public void setLstBedEntity(List lstBedEntity) {
		this.lstBedEntity = lstBedEntity;
	}

	public Long getSelectRoomTypeId() {
		return selectRoomTypeId;
	}

	public void setSelectRoomTypeId(Long selectRoomTypeId) {
		this.selectRoomTypeId = selectRoomTypeId;
	}

	public Long getBedType() {
		return bedType;
	}

	public void setBedType(Long bedType) {
		this.bedType = bedType;
	}

	public String getIsDisablePage() {
		return isDisablePage;
	}

	public void setIsDisablePage(String isDisablePage) {
		this.isDisablePage = isDisablePage;
	}

	public String getIsQueryReport() {
		return isQueryReport;
	}

	public void setIsQueryReport(String isQueryReport) {
		this.isQueryReport = isQueryReport;
	}

	public Long getForewarn() {
		return forewarn;
	}

	public void setForewarn(Long forewarn) {
		this.forewarn = forewarn;
	}

	public Long getHotelNo() {
		return hotelNo;
	}

	public void setHotelNo(Long hotelNo) {
		this.hotelNo = hotelNo;
	}

	public String getBackToFlag() {
		return backToFlag;
	}

	public void setBackToFlag(String backToFlag) {
		this.backToFlag = backToFlag;
	}

	public String getIsFirsQry() {
		return isFirsQry;
	}

	public void setIsFirsQry(String isFirsQry) {
		this.isFirsQry = isFirsQry;
	}

	public HtlHotel getHotel() {
		return hotel;
	}

	public void setHotel(HtlHotel hotel) {
		this.hotel = hotel;
	}

	public String[] getWeek() {
		return week;
	}

	public void setWeek(String[] week) {
		this.week = week;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getPayment_Method() {
		return payment_Method;
	}

	public void setPayment_Method(String payment_Method) {
		this.payment_Method = payment_Method;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Long getRowsIndex() {
		return rowsIndex;
	}

	public void setRowsIndex(Long rowsIndex) {
		this.rowsIndex = rowsIndex;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public String getReturnFlag() {
		return returnFlag;
	}

	public void setReturnFlag(String returnFlag) {
		this.returnFlag = returnFlag;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
