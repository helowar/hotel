package com.mangocity.hotel.newroomcontrol.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.HtlTempRoomState;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.newroomcontrol.service.NewRoomControlService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 按日历调整
 */
public class RoomStateProcessByCalendarAction extends PersistenceAction {
	private static final int REFERENCE_NUMBER = 6;
	private NewRoomControlService newRoomControlService;
	
	private HotelRoomTypeService hotelRoomTypeService;
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

   
    /**
     * 分页 add by shengwei.zuo 2009-10-22 begin
     */
	private int pageSize = 10;

	private int totalPage;

	private Long totalNum;

	private int pageNo = 1;
	
	private Date now=new Date();
	
    /**
     * 分页 add by shengwei.zuo 2009-10-22 end
     */
	
	//配额manage add by shengwei.zuo 2009-10-21
	private IQuotaManage  quotaManage;
	
	/**
	 * 行数 add by shengwei.zuo hotel 2.9.3
	 */
	private int cutoffDayNum;
	
	//有配额预警的日期集合  add by shengwei.zuo hotel 2.9.3
	private List quotaDateList;
	
	// 判断上一页或下一页的按钮是否需要灰选;
	private String isDisablePage;
	
	private String isRoomStatusReportValue;
	
	//交接事项最后操作人
	private String  newlyModifyByName;
	
	//交接事项最后操作时间
	private Date newlyModifyTime;
	
	//交接事项查询时间begin
	private Date bnDate;
	
	private Date enDate;
	//交接事项查询时间end
	
	//房态会展信息查询用begin
	private String cityCode;
	
	private String stateForRoomState;
	
	private Date beginD;
	
	private Date endD;
	//房态会展信息查询用end
	private long contractId;
	
	private String hotelScheduleId;
	/**
	 * 标记是否查配额预警还是查CC设置满房 2010-6-4 by juesuchen
	 */
	private String queryCCSetRoomState;
	
	private List lisRoomcontrolOperationLogs;
	public List getLisRoomcontrolOperationLogs() {
		return lisRoomcontrolOperationLogs;
	}

	public void setLisRoomcontrolOperationLogs(List lisRoomcontrolOperationLogs) {
		this.lisRoomcontrolOperationLogs = lisRoomcontrolOperationLogs;
	}

	public String getIsRoomStatusReportValue() {
		return isRoomStatusReportValue;
	}

	public void setIsRoomStatusReportValue(String isRoomStatusReportValue) {
		this.isRoomStatusReportValue = isRoomStatusReportValue;
	}

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
    
    /**
     * 按天调整房态
     * 
     * @return
     */
    public String processByCalendar() {
    	roleUser = getCurrentUser();
    	if(null == roleUser)
    		return forwardMsgAndColse("您未登陆，请先登陆再操作！");
    	HtlRoomcontrolHotelSchedule hotelSchedule = newRoomControlService.getRoomcontrolHotelScheduleById(hotelScheduleId);//得到列表记录
        if(null == hotelSchedule)
        	return forwardMsgAndColse("找不到该酒店的房控记录!");
        if(0 == hotelSchedule.getAcquirestate() || !StringUtil.isValidStr(hotelSchedule.getNowassignedid())){
        	//如果是未获取,则视为手工获取记录
        	newRoomControlService.allotHotelScheduleByHand(hotelSchedule, roleUser);
        }else if(!roleUser.getLoginName().equals(hotelSchedule.getNowassignedid())){
        	//已被他人获取,则提示
        	return forwardMsgAndColse("该酒店正被 "+hotelSchedule.getNowassignedname()+" 处理中!");
        }
        
        dateRoomStatusProcess = hotelManage.findRoomStatusDateProcess(hotelID);
        int i = dateRoomStatusProcess.size();
        if (0 != i) {
            HtlRoomStatusProcess htlRoomStatusProcess = new HtlRoomStatusProcess();
            htlRoomStatusProcess = (HtlRoomStatusProcess) dateRoomStatusProcess.get(i - 1);
            isRoomStatusReport = htlRoomStatusProcess.getIsRoomStatusReport().toString();
            processRemark = htlRoomStatusProcess.getProcessRemark();
            newlyModifyByName = htlRoomStatusProcess.getProcessBy();
            newlyModifyTime = htlRoomStatusProcess.getProcessDate();
        }
        hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
        List list = hotel.getHtelHotelExt();
        for (int j = 0; j < list.size(); j++) {
            HtlHotelExt hext = (HtlHotelExt) list.get(j);
            String roomStateManager = hext.getRoomStateManager();
            isGreenMangoHotel = hext.getIsGreenMangoHotel();
            request.setAttribute("roomStateManagerStr", roomStateManager);
        }
        //获取合同id
        HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelID);
        if(null != contractEntity && contractEntity.getID() != null){
       	 contractId = contractEntity.getID();
        }
        setBeginDate(DateUtil.getSystemDate());
        setEndDate(DateUtil.getDate(DateUtil.getSystemDate(), 10));
        super.getParams().put("init", "Y");

        return "processByCalendar";
    }
    
    
    /**
     * 查询有配额预警的房态，临时配额，预警数，预警状态，可用配额总数 add by shengwei.zuo 2009-10-20
     * 或者：查询CC设置的房态，关房或设置满房
     */
    public String processByRoomControl() {
    	Map params = getParams();
    	//房态
        dateRoomStatusProcess = hotelManage.findRoomStatusDateProcess(hotelID);
        int i = dateRoomStatusProcess.size();
        if (0 != i) {
            HtlRoomStatusProcess htlRoomStatusProcess = new HtlRoomStatusProcess();
            htlRoomStatusProcess = (HtlRoomStatusProcess) dateRoomStatusProcess.get(i - 1);
            isRoomStatusReport = htlRoomStatusProcess.getIsRoomStatusReport().toString();
            processRemark = htlRoomStatusProcess.getProcessRemark();
        }
        //是否查询CC设置房态
        String noFoundMsg = null;
        if(StringUtil.isValidStr(queryCCSetRoomState)){
        	//有CC设置房态的房型列表
        	hRTList = roomControlManage.getRoomTypeCCSetted(hotelID);
        	noFoundMsg ="该酒店的房型没有CC设置信息";
        }else{
        	//有配额预警的房型列表
        	hRTList = roomControlManage.getRoomTypeHavaForewarn(hotelID);
        	queryCCSetRoomState = null;
        	noFoundMsg = "该酒店的房型没有对应的预警信息";
        }
        
        if(hRTList==null||hRTList.isEmpty()){
        	 return super.forwardMsgBox(noFoundMsg,"queryRoomState()");
        }
        Map<String, ?> map = null;
        if(StringUtil.isValidStr(queryCCSetRoomState)){
        	//查询有CC设置房态的日期列表的总的记录数。
    		map = roomStateManage.queryCCSetRoomDateList(hotelID, hRTList, pageNo, pageSize);
        }else{
        	//查询有配额预警的日期列表的总的记录数。
    		map = quotaManage.queryNewQuotaDateList(hotelID, hRTList, pageNo, pageSize);
        }
		if ((map.containsKey("query_newquota_detail_list"))
				&& (null != map.get("query_newquota_detail_list"))) {
			quotaDateList = (List) map.get("query_newquota_detail_list");
			cutoffDayNum = quotaDateList.size();
		}
		if ((map.containsKey("query_newquota_detail_totalnum"))
				&& (null != map.get("query_newquota_detail_totalnum"))) {
			totalNum = (Long) map.get("query_newquota_detail_totalnum");
			totalPage = (int) ((totalNum - 1) / pageSize + 1);
		}

		if(quotaDateList==null||quotaDateList.isEmpty()){
			 return super.forwardMsgBox(noFoundMsg,"queryRoomState()");
		}
		//房态，临时配额，以及相关预警信息的list;
		List lstDisplayRoomStates = hotelManage.qryRoomForQuota(hotelID,week,hRTList,quotaDateList,queryCCSetRoomState);
		
		if(lstDisplayRoomStates==null||lstDisplayRoomStates.isEmpty()){
			 return super.forwardMsgBox(noFoundMsg, "queryRoomState()");
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
     * 修改有配额预警的房态，预警状态，和临时配额信息
     * add by shengwei.zuo 2009-10-22
     * @return
     */
    public String updateQuota() {
        HashMap tqMap = new HashMap();
        List lstActiveTempQuota = new ArrayList();
        List lstTempQuotaNew = new ArrayList();
        Map params = super.getParams();
        List lstTempQuota = MyBeanUtil.getBatchObjectFromParam(params, HtlTempQuota.class, iCount);
        
        //查询出合同，得到合同中的配额模式，赋值到调整表中  add by shengwei.zuo 2009-10-26
		HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelID);

		if (contractEntity == null) {
			return super.forwardError("该酒店在当前日期没有对应的合同，请先添加合同！");
		}
        
        for (int i = 0; i < lstTempQuota.size(); i++) {
        	
            HtlTempQuota tq = (HtlTempQuota) lstTempQuota.get(i);
            String bedSttu = (String) tqMap.get("" + tq.getRoomId());
            if (null != bedSttu) {
                bedSttu += "/" + tq.getBedId() + ":" + tq.getBedStatus();
            } else {
                bedSttu = tq.getBedId() + ":" + tq.getBedStatus();
            }
            tqMap.put("" + tq.getRoomId(), bedSttu);
            if (null != super.getOnlineRoleUser()) {
                tq = (HtlTempQuota) CEntityEvent.setCEntity(tq,
                    super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
            }

            	//加入更新临时配额 往新表中也写入数据 add by haibo.li 配额改造
            	HtlQuotaJudge hqNew = new HtlQuotaJudge();
                //hqNew.setQuotaNum(new Long(tq.getQuotaQty()));//配额数
                hqNew.setBedId(new Long(tq.getBedId()));//床型
                hqNew.setCutofftime(tq.getCutofftime());//cutofftime
                hqNew.setCutoffday(new Long(0));//临时配额直接写入0
                hqNew.setQuotaPattern(contractEntity.getQuotaPattern()==null?HotelBaseConstantBean.QUOTAPATTERNSI:contractEntity.getQuotaPattern());//配额模式
                hqNew.setHotelId(hotelID);//酒店
                hqNew.setQuotaHolder("CC");//
                hqNew.setRoomtypeId(tq.getRoomTypeId());//房型ID
                hqNew.setStartDate(tq.getStartDate());//开始时间
                hqNew.setEndDate(tq.getStartDate());//结束时间
                hqNew.setQuotaType(HotelBaseConstantBean.TEMPQUOTA);//配额类型
                hqNew.setQuotaChannel(HotelBaseConstantBean.CC);//配额渠道
                hqNew.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPE);
                //addQuotaQty 是页面中增加配额 ,delQuotaQty是页面中的减少配额
                hqNew.setQuotaCutoffDayNewId(tq.getQuotaCutoffDayNewId());//配额明细ID 有则加 没有则无
                hqNew.setBlnBack(1L);
                if(tq.getAddQuotaQty()!=0 &&tq.getDelQuotaQty()!=0 ){
                	if(tq.getAddQuotaQty() > tq.getDelQuotaQty()){
                		long qty = tq.getAddQuotaQty() - tq.getDelQuotaQty();
                		hqNew.setQuotaNum(qty);
                		hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_ADD);
                	}else if(tq.getAddQuotaQty() < tq.getDelQuotaQty()){
                		long qty = tq.getDelQuotaQty() - tq.getAddQuotaQty();
                		hqNew.setQuotaNum(qty);
                		hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_DEC);
                	}else if(tq.getAddQuotaQty() == tq.getDelQuotaQty()){
                		hqNew.setQuotaNum(new Long(tq.getQuotaQty()));//配额数
                	}
                	
                }

                if(tq.getAddQuotaQty()!=0 && tq.getDelQuotaQty()==0){//判断是增加配额还是减少配额
                	hqNew.setQuotaNum(tq.getAddQuotaQty());
                	hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_ADD);	
                }else if(tq.getAddQuotaQty()==0 && tq.getDelQuotaQty()!=0){
                	hqNew.setQuotaNum(tq.getDelQuotaQty());
                	hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_DEC);
                }else if(tq.getAddQuotaQty()==0 && tq.getDelQuotaQty()==0){
                	hqNew.setQuotaNum(new Long(tq.getQuotaQty()));//配额数
                	hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_UTP);
                }
                	

                if (null != super.getOnlineRoleUser()){ //登陆人员
                	hqNew.setOperatorId( super.getOnlineRoleUser().getName());
                	hqNew.setOperatorName(super.getOnlineRoleUser().getLoginName());
                	hqNew.setOperatorDept("HBIZ");
                	hqNew.setOperatorTime(new Date());
                	
                	tq.setOperatorId( super.getOnlineRoleUser().getName());
                	tq.setOperatorName(super.getOnlineRoleUser().getLoginName());
                	tq.setOperatorDept("HBIZ");
                	tq.setOperatorTime(new Date());
                	
                }
                
                lstTempQuotaNew.add(hqNew);
           
            lstActiveTempQuota.add(tq);
        }

        RoomStateBean roomStateBean = new RoomStateBean();
        roomStateBean.setHotelID(hotelID);
        if (null != super.getOnlineRoleUser()) {
            roomStateBean.setUserName(super.getOnlineRoleUser().getName());
            roomStateBean.setUserId(super.getOnlineRoleUser().getLoginName());
            if (super.getOnlineRoleUser().getLoginName().equals("")
                || super.getOnlineRoleUser().getName().equals("")) {
                roomStateBean.setUserName(super.getBackUserName());
                roomStateBean.setUserId(super.getBackLoginName());
            }
        }
        roomStateBean.setIsRoomStatusReport(isRoomStatusReport);

        roomStateBean.setProcessRemark(processRemark);

        // end
        roomStateManage.batchUpdateRoomStatus(roomStateBean, tqMap, lstActiveTempQuota);
        //调用方法更新临时配额 add by haibo.li 2.9.3配额改造
        try{
        	if(lstTempQuotaNew.size()>0){
            	String idStr = roomStateManage.batchUpdateTempQuota(lstTempQuotaNew);
            	roomStateManage.UpdateTempQuota(idStr);
            }
        	
        	roomStateManage.modifyQuotaNewInfo(lstActiveTempQuota);
        }catch(Exception e){
        	log.error(e.getMessage(),e);
        	log.error("保存临时配额出现异常");
        }
        super.getParams().put("roomTypeArray", roomTypeArray);
        super.getParams().put("refreshParent", "true");
        // 解除酒店房态锁定
        // 不需要解除锁定
        // lockedRecordService.deleteLockedRecord(String.valueOf(hotelID),02);
        return "queryQuotaFlag";
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
            newlyModifyByName = htlRoomStatusProcess.getProcessBy();
            newlyModifyTime = htlRoomStatusProcess.getProcessDate();
        }
        if (null == roomTypeArray) {
            roomTypeArray = "";
        }
        if (null == roomType || 0 == roomType.length) {
            roomType = roomTypeArray.split(",");
        }
        hRTList = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
        List lstDisplayRoomStates = hotelManage
            .qryRoom(hotelID, beginDate, endDate, week, roomType);
        hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
        List<HtlHotelExt> list = hotel.getHtelHotelExt();
        for ( HtlHotelExt hext:list) {
            roomStateManagerStr = hext.getRoomStateManager();
            isGreenMangoHotel = hext.getIsGreenMangoHotel();
        }
        if (null != isGreenMangoHotelReport && !("").equals(isGreenMangoHotelReport)) {
            isGreenMangoHotel = isGreenMangoHotelReport;
        }
        if (null != roomState && !roomState.equals("")) {
            roomStateManagerStr = roomState;
        }
        if (null != isRoomStatus && !isRoomStatus.equals("")) {
            isRoomStatusReport = isRoomStatus;
        }
        //获取合同id
        HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelID);
        if(null != contractEntity && null != contractEntity.getID() && !"".equals(contractEntity.getID())){
        	 contractId = contractEntity.getID();
        }
        
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
    	try{
    	HashMap tqMap = new HashMap();
        List lstActiveTempQuota = new ArrayList();
        List lstTempQuotaNew = new ArrayList();
        Map params = super.getParams();
        List lstTempQuota = MyBeanUtil.getBatchObjectFromParam(params, HtlTempQuota.class, iCount);
        List lstTempRoomState=new ArrayList();
        //查询出合同，得到合同中的配额模式，赋值到调整表中  add by shengwei.zuo 2009-10-26
		HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelID);

		if (contractEntity == null) {
			return super.forwardMsgAndColse("该酒店在当前日期没有对应的合同，请先添加合同！");
		}
		StringBuffer logSb = new StringBuffer();
		logSb.append("日历:(");
		Date firstDate = null;
		lstTempQuota = sortLstTempQuota(lstTempQuota);
        for (int i = 0; i < lstTempQuota.size(); i++) {
            HtlTempQuota tq = (HtlTempQuota) lstTempQuota.get(i);
            String bedSttu = (String) tqMap.get("" + tq.getRoomId());
            
            if (null != bedSttu) {
                bedSttu += "/" + tq.getBedId() + ":" + tq.getBedStatus();
            } else {
                bedSttu = tq.getBedId() + ":" + tq.getBedStatus();
            }
            if(!tq.getOldBedStatus().equals(tq.getBedStatus())){
            	if(firstDate ==null){
            		logSb.append("["+DateUtil.dateToStringNew(tq.getStartDate())+"]"+"("+tq.getRoomTypeStr()+"/" +getBedTypeStr(tq.getBedId())+");"+getBedStatuStr(tq.getOldBedStatus())+"/"+getBedStatuStr(tq.getBedStatus())+"; ");
            		firstDate = tq.getStartDate();
            	}else{
            		if(firstDate.getTime()==tq.getStartDate().getTime()){
            			logSb.append("("+tq.getRoomTypeStr()+"/" +getBedTypeStr(tq.getBedId())+");"+getBedStatuStr(tq.getOldBedStatus())+"/"+getBedStatuStr(tq.getBedStatus())+"; ");
            		}else{
            			logSb.append("["+DateUtil.dateToStringNew(tq.getStartDate())+"]"+"("+tq.getRoomTypeStr()+"/" +getBedTypeStr(tq.getBedId())+");"+getBedStatuStr(tq.getOldBedStatus())+"/"+getBedStatuStr(tq.getBedStatus())+"; ");
            			firstDate = tq.getStartDate();
            			
            		}
            	}
            }
            tqMap.put("" + tq.getRoomId(), bedSttu);
            if (null != super.getOnlineRoleUser()) {
                tq = (HtlTempQuota) CEntityEvent.setCEntity(tq,
                    super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
            }
            	if((tq.getBedStatus().equals("4"))&&(!tq.getOldBedStatus().equals("4"))){
            		HtlTempRoomState tempRoom=new HtlTempRoomState();
            		tempRoom.setRoomName(tq.getRoomTypeStr());
            		tempRoom.setRoomType(tq.getRoomTypeId());
            		tempRoom.setBedId(tq.getBedId());
            		tempRoom.setSaleDate(tq.getStartDate());
            		tempRoom.setNewBedStatus(tq.getBedStatus());
            		tempRoom.setOldBedStatus(tq.getOldBedStatus());
            		lstTempRoomState.add(tempRoom);
            	}
            	//加入更新临时配额 往新表中也写入数据 add by haibo.li 配额改造
            	HtlQuotaJudge hqNew = new HtlQuotaJudge();
                //hqNew.setQuotaNum(new Long(tq.getQuotaQty()));//配额数
                hqNew.setBedId(new Long(tq.getBedId()));//床型
                hqNew.setCutofftime(tq.getCutofftime());//cutofftime
                hqNew.setCutoffday(tq.getCutoffday());//临时配额直接写入0
                hqNew.setQuotaPattern(contractEntity.getQuotaPattern()==null?HotelBaseConstantBean.QUOTAPATTERNSI:contractEntity.getQuotaPattern());//配额模式
                hqNew.setHotelId(hotelID);//酒店
                hqNew.setQuotaHolder("CC");//
                hqNew.setRoomtypeId(tq.getRoomTypeId());//房型ID
                hqNew.setStartDate(tq.getStartDate());//开始时间
                hqNew.setEndDate(tq.getStartDate());//结束时间
                hqNew.setQuotaType(HotelBaseConstantBean.TEMPQUOTA);//配额类型
                hqNew.setQuotaChannel(HotelBaseConstantBean.CC);//配额渠道
                hqNew.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPE);
                //addQuotaQty 是页面中增加配额 ,delQuotaQty是页面中的减少配额
                hqNew.setQuotaCutoffDayNewId(tq.getQuotaCutoffDayNewId());//配额明细ID 有则加 没有则无
                hqNew.setJudgeWeeks("1,2,3,4,5,6,7");
                hqNew.setBlnBack(1L);
                if(tq.getAddQuotaQty()!=0 &&tq.getDelQuotaQty()!=0 ){
                	if(tq.getAddQuotaQty() > tq.getDelQuotaQty()){
                		long qty = tq.getAddQuotaQty() - tq.getDelQuotaQty();
                		hqNew.setQuotaNum(qty);
                		hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_ADD);
                		logSb.append("配 ["+tq.getRoomTypeStr()+"/"+getBedTypeStr(tq.getBedId())+";增临"+qty+" ]; ");
                	}else if(tq.getAddQuotaQty() < tq.getDelQuotaQty()){
                		long qty = tq.getDelQuotaQty() - tq.getAddQuotaQty();
                		hqNew.setQuotaNum(qty);
                		hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_DEC);
                		logSb.append("配 ["+tq.getRoomTypeStr()+"/"+getBedTypeStr(tq.getBedId())+";减临"+qty+" ]; ");
                	}else if(tq.getAddQuotaQty() == tq.getDelQuotaQty()){
                		hqNew.setQuotaNum(new Long(tq.getQuotaQty()));//配额数
                	}
                	
                }

                if(tq.getAddQuotaQty()!=0 && tq.getDelQuotaQty()==0){//判断是增加配额还是减少配额
                	logSb.append("配 ["+tq.getRoomTypeStr()+"/"+getBedTypeStr(tq.getBedId())+";增临"+tq.getAddQuotaQty()+" ]; ");
                	hqNew.setQuotaNum(tq.getAddQuotaQty());
                	hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_ADD);	
                }else if(tq.getAddQuotaQty()==0 && tq.getDelQuotaQty()!=0){
                	logSb.append("配 ["+tq.getRoomTypeStr()+"/"+getBedTypeStr(tq.getBedId())+";减临"+tq.getAddQuotaQty()+" ]; ");
                	hqNew.setQuotaNum(tq.getDelQuotaQty());
                	hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_DEC);
                }else if(tq.getAddQuotaQty()==0 && tq.getDelQuotaQty()==0){
                	//logSb.append(";修改cutoffDay为："+tq.getCutoffday()+";修改cutofftime为"+tq.getCutofftime());
                	hqNew.setQuotaNum(new Long(tq.getQuotaQty()));//配额数
                	hqNew.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_UTP);
                }
                	

                if (null != super.getOnlineRoleUser()){ //登陆人员
                	hqNew.setOperatorId( super.getOnlineRoleUser().getName());
                	hqNew.setOperatorName(super.getOnlineRoleUser().getLoginName());
                	hqNew.setOperatorDept("HBIZ");
                	hqNew.setOperatorTime(new Date());
                	
                	tq.setOperatorId( super.getOnlineRoleUser().getName());
                	tq.setOperatorName(super.getOnlineRoleUser().getLoginName());
                	tq.setOperatorDept("HBIZ");
                	tq.setOperatorTime(new Date());
                	
                }
                lstTempQuotaNew.add(hqNew);
           
            lstActiveTempQuota.add(tq);
        }
        logSb.append(")");
        newRoomControlService.saveRoomStatusOptLogs(hotelScheduleId,super.getOnlineRoleUser(),logSb.toString());
      
        RoomStateBean roomStateBean = new RoomStateBean();
        roomStateBean.setHotelID(hotelID);
        if (null != super.getOnlineRoleUser()) {
            roomStateBean.setUserName(super.getOnlineRoleUser().getName());
            roomStateBean.setUserId(super.getOnlineRoleUser().getLoginName());
            if (super.getOnlineRoleUser().getLoginName().equals("")
                || super.getOnlineRoleUser().getName().equals("")) {
                roomStateBean.setUserName(super.getBackUserName());
                roomStateBean.setUserId(super.getBackLoginName());
            }
        }
        roomStateBean.setIsRoomStatusReport(isRoomStatusReport);

        roomStateBean.setProcessRemark(processRemark);
        // 更新房态负责人 haibo.li by 2008.12.01 begin
        hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
        List lsHtlHotelExt = hotel.getHtelHotelExt();
        if (0 < lsHtlHotelExt.size()) {
            if (StringUtil.isValidStr(roomStateManagerStr)
                || StringUtil.isValidStr(isGreenMangoHotel)) {
                HtlHotelExt he = (HtlHotelExt) lsHtlHotelExt.get(0);
                he.setHtlHotel(hotel);
                he.setRoomStateManager(roomStateManagerStr);
                he.setIsGreenMangoHotel(isGreenMangoHotel);
                // request.setAttribute("roomStateManagerStr",roomStateManagerStr);
                hotelManage.saveOrUpdateExt(he);
            }
        } else if (0 == lsHtlHotelExt.size()
            && ((null != roomStateManagerStr && 
                !roomStateManagerStr.trim().equals(""))
                || (null != isGreenMangoHotel && !("")
                .equals(isGreenMangoHotel.trim())))) {
            if (StringUtil.isValidStr(roomStateManagerStr)
                || StringUtil.isValidStr(isGreenMangoHotel)) {
                HtlHotelExt he = new HtlHotelExt();
                he.setHtlHotel(hotel);
                he.setRoomStateManager(roomStateManagerStr);
                he.setIsGreenMangoHotel(isGreenMangoHotel);
                // request.setAttribute("roomStateManagerStr",roomStateManagerStr);
                hotelManage.saveHtlHotelExt(he);
            }
        }
        
        // end
        roomStateManage.batchUpdateRoomStatus(roomStateBean, tqMap, lstActiveTempQuota);
        if(null!=lstTempRoomState&&lstTempRoomState.size()>0){
        	
        	//用来判断房态是否有F转为满房的数据，有则要发email
        	boolean isSendMail = false;
        	for(Iterator i =lstTempRoomState.iterator();i.hasNext();){
        		HtlTempRoomState tempRoom = (HtlTempRoomState)i.next();
        		if("0".equals(tempRoom.getOldBedStatus()) && "4".equals(tempRoom.getNewBedStatus())){
        			isSendMail = true;
        			break;
        		}
        	}
        	if(isSendMail){
        		roomStateManage.sendRoomStateFull(lstTempRoomState, hotel, this.getOnlineRoleUser(),2);
        	}
        	//房态为“满房”时的列表 
        	roomStateManage.sendRoomStateFull(lstTempRoomState, hotel, this.getOnlineRoleUser(),0);
        		List list = valiRoomStateDate(lstTempRoomState);
        		//当某一酒店同一房型设置“满房”连续超过7天
        		if(null!=list&&list.size()>6){
        			roomStateManage.sendRoomStateFull(list, hotel, this.getOnlineRoleUser(),1);
        		}
        }
      
        //调用方法更新临时配额 add by haibo.li 2.9.3配额改造
        try{
        	if(lstTempQuotaNew.size()>0){
            	String idStr = roomStateManage.batchUpdateTempQuota(lstTempQuotaNew);
            	roomStateManage.UpdateTempQuota(idStr);
            }
        	
        	roomStateManage.modifyQuotaNewInfo(lstActiveTempQuota);
        }catch(Exception e){
        	log.error(e.getMessage(),e);
        	log.error("保存临时配额出现异常");
        }
        
        newRoomControlService.changeQuotaWarningByRoomstate(lstTempQuota,hotelID);
        // roomStateManage.logRoomStatusProcess(htlRoomStatusProcess);
        super.getParams().put("roomTypeArray", roomTypeArray);
    	}catch(Exception e){
    		log.error("roomcontrol update error:", e);
    	}
        return "update";
    }

    private List valiRoomStateDate(List lstTempRoomState) {
    	Map<Long,List<HtlTempRoomState>> map=new HashMap<Long,List<HtlTempRoomState>>();
    	List<HtlTempRoomState> result=new ArrayList<HtlTempRoomState>();
    	for(Iterator i = lstTempRoomState.iterator();i.hasNext();){
    		HtlTempRoomState roomState=(HtlTempRoomState) i.next();
    		Long key=roomState.getRoomType()*Long.valueOf(roomState.getBedId());
    		if(map.containsKey(key)){
    			List<HtlTempRoomState> list=(List<HtlTempRoomState>) map.get(key);
    			list.add(roomState);
    		}else{
    			List<HtlTempRoomState> list=new ArrayList<HtlTempRoomState>();
    			list.add(roomState);
    			map.put(key, list);
    		}
    	}
    	List resultList=new ArrayList();
    	for(Iterator<Long> i = map.keySet().iterator();i.hasNext();){
    		Long roomType=i.next();
    		List<HtlTempRoomState> list=map.get(roomType);
    		Set dateSet=new TreeSet();
    		for(Iterator<HtlTempRoomState> j = list.iterator();j.hasNext();){
    			HtlTempRoomState tempRoomState = j.next();
    			dateSet.add(tempRoomState.getSaleDate());
    		}
    		if(null!=dateSet){
    			if(dateSet.size()>6&&validateSequence(dateSet)){
    				resultList.add(map.get(roomType));
    			}
    		}
    	}
    	if(null!=resultList){
    		for(Iterator i = resultList.iterator();i.hasNext();){
    			List tempList=(List) i.next();
    			for(Iterator j = tempList.iterator();j.hasNext();){
    				HtlTempRoomState tempRoom=(HtlTempRoomState) j.next();
    				result.add(tempRoom);
    			}
    		}
    	}
		return result;
	}
    
    private static boolean validateSequence(Set<Date> dates) {
		if (REFERENCE_NUMBER <= dates.size()) {
			log.info("satisfy the reference number " + REFERENCE_NUMBER);
			/*Collections.sort(dates, new Comparator<Date>(){
				public int compare(Date d1, Date d2) {
					return (null != d1) ? d1.compareTo(d2) : -1;
				}});*/
			int count = 0;
			Date tmpDate = null;
			for (Date date : dates) {
				log.info(date);
				if (isNextDate(tmpDate, date)) {
					log.debug("isNextDate");
					count++;
					if (REFERENCE_NUMBER <= 1 + count) {
						log.debug("satisfy sequence dates " + (1 + count));
						return true;
					}
				} else {
					count = 0;
				}
				tmpDate = date;
			}
			return (REFERENCE_NUMBER <= 1 + count);
		}
		return false;
	}
    
    /**
	 * 判断d2是否为d1的下一天日期
	 * @param d1 前一天
	 * @param d2 后一天
	 * @return true-d1是d2的前一天
	 */
	private static boolean isNextDate(Date d1, Date d2) {
		log.debug("isNextDate(" + d1 + "," + d2 + ")");
		if ((null == d1) || (null == d2)) {
			return false;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		c1.roll(Calendar.DAY_OF_YEAR, 1);
		return c1.equals(c2);
	}

	/**
     * 返回查询，并解锁
     * 
     * @return
     */
    public String backToList() {
        Map params = super.getParams();
        return "query";
    }
    
    
    /**
     * 异步保存交接事项和是否主动报房态 add by zhijie.gu 2010-01-05
     * processRemark交接事项内容
     * isRoomStatusReport 是否主动报房态
     * @return
     */
    public String saveProcessRemarkAndIsRoomStatus() {
    	RoomStateBean roomStateBean = new RoomStateBean();
    	roomStateBean.setHotelID(hotelID);
        if (null != super.getOnlineRoleUser()) {
            roomStateBean.setUserName(super.getOnlineRoleUser().getName());
            roomStateBean.setUserId(super.getOnlineRoleUser().getLoginName());
            if (super.getOnlineRoleUser().getLoginName().equals("")
                || super.getOnlineRoleUser().getName().equals("")) {
                roomStateBean.setUserName(super.getBackUserName());
                roomStateBean.setUserId(super.getBackLoginName());
            }
        }
        roomStateBean.setIsRoomStatusReport(isRoomStatusReport);
        roomStateBean.setProcessRemark(processRemark);
        roomStateManage.saveRoomStateProcess(roomStateBean);
    	return "processByCalendar";
    }
    
    /**
     * 跳转到房态交接事项查询页面 add by zhijie.gu 2010-01-05
     * @return
     */
    public String forwardToRoomStateHistory(){
    	//初始化默认时间
    	bnDate = DateUtil.getDate(DateUtil.getSystemDate(),-6);
    	enDate = DateUtil.getSystemDate();
    	return "forwardQueryRoomStateHistory";
    	
    }
    
    /**
     * 跳转到房态会展信息查询处理页面 add by zhijie.gu 2010-01-05
     * @return
     */
    public String queryExhibitInfomation(){
    	//初始化默认时间
    	beginD = DateUtil.getSystemDate();
    	endD = DateUtil.getDate(DateUtil.getSystemDate(),90);
    	return "forwardToExhibitInfomation"; 
    }
    
    public String getBedTypeStr(String bedId){
    	if(null==bedId||"".equals(bedId)){
    		return "无"; //无此房型
    	}
    	if(bedId.equals("1")){
    		return "大"; //大床
    	}
    	if(bedId.equals("2")){
    		return "双";//双床
    	}
    	if(bedId.equals("3")){
    		return "单";//单床
    	}
    	return "无";//无此房型
    }
    
    public String getBedStatuStr(String bedStatu){
    	if(null==bedStatu||"".equals(bedStatu)){
    		return "无";//无此房态
    	}
    	if(bedStatu.equals("0")){
    		return "F";//freesale
    	}
    	if(bedStatu.equals("1")){
    		return "良";//良好
    	}
    	if(bedStatu.equals("2")){
    		return "紧";//紧张
    	}
    	if(bedStatu.equals("3")){
    		return "不";//不可超
    	}
    	if(bedStatu.equals("4")){
    		return "满";//满房
    	}
    	return "无";//无此房态
    }
    
    //把list按StartDate升序排序
    private List sortLstTempQuota(List lstTempQuota){
    	HtlTempQuota htlTemp =null;
    	for(int i=0 ;i<lstTempQuota.size();i++){
    		for(int j=0;j<lstTempQuota.size()-i-1;j++){
    			HtlTempQuota tq1 = (HtlTempQuota) lstTempQuota.get(j);
    			HtlTempQuota tqTemp = new HtlTempQuota();
    			tqTemp = tq1;
        		Date htlTemp1 = tqTemp.getStartDate();
    			HtlTempQuota tq2 = (HtlTempQuota) lstTempQuota.get(j+1);
    			Date htlTemp2 = tq2.getStartDate();
    			if(htlTemp1.getTime()>htlTemp2.getTime()){
    				lstTempQuota.set(j, tq2);
    				lstTempQuota.set(j+1, tqTemp);
    				
    			}
    		}
    		
    	}
    	return lstTempQuota;
    }
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate){
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

	public String getNewlyModifyByName() {
		return newlyModifyByName;
	}

	public void setNewlyModifyByName(String newlyModifyByName) {
		this.newlyModifyByName = newlyModifyByName;
	}

	public Date getNewlyModifyTime() {
		return newlyModifyTime;
	}

	public void setNewlyModifyTime(Date newlyModifyTime) {
		this.newlyModifyTime = newlyModifyTime;
	}

	public Date getBnDate() {
		return bnDate;
	}

	public void setBnDate(Date bnDate) {
		this.bnDate = bnDate;
	}

	public Date getEnDate() {
		return enDate;
	}

	public void setEnDate(Date enDate) {
		this.enDate = enDate;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Date getBeginD() {
		return beginD;
	}

	public void setBeginD(Date beginD) {
		this.beginD = beginD;
	}

	public Date getEndD() {
		return endD;
	}

	public void setEndD(Date endD) {
		this.endD = endD;
	}

	public long getContractId() {
		return contractId;
	}

	public void setContractId(long contractId) {
		this.contractId = contractId;
	}
	public NewRoomControlService getNewRoomControlService() {
		return newRoomControlService;
	}

	public void setNewRoomControlService(NewRoomControlService newRoomControlService) {
		this.newRoomControlService = newRoomControlService;
	}

	public String getHotelScheduleId() {
		return hotelScheduleId;
	}

	public void setHotelScheduleId(String hotelScheduleId) {
		this.hotelScheduleId = hotelScheduleId;
	}

	public String getStateForRoomState() {
		return stateForRoomState;
	}

	public void setStateForRoomState(String stateForRoomState) {
		this.stateForRoomState = stateForRoomState;
	}

	public Date getNow() {
		return now;
	}

	public void setNow(Date now) {
		this.now = now;
	}

	public String getQueryCCSetRoomState() {
		return queryCCSetRoomState;
	}

	public void setQueryCCSetRoomState(String queryCCSetRoomState) {
		this.queryCCSetRoomState = queryCCSetRoomState;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
