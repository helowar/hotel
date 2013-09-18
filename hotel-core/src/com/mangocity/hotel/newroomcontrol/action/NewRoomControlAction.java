package com.mangocity.hotel.newroomcontrol.action;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HotelRoomcontrolLogsDisplay;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlRoomControlWorkSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolWorkstation;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.newroomcontrol.service.NewRoomControlService;
import com.mangocity.hotel.newroomcontrol.service.assistant.FaxInfoForRoomcontrol;
import com.mangocity.hotel.newroomcontrol.service.assistant.NewRoomControlFaxInfo;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class NewRoomControlAction extends GenericAction {

    private String forward;
    
    private NewRoomControlService newRoomControlService;
    
    /**
     * 酒店本部接口
     */
    private IHotelService hotelService;
    
    //查询条件 begin
    //根据员工登陆名查询
    private String personId;
    
    //根据员工中文名查询
    private String personIdName;
    
    //根据上班日期查询
    private String queryBeginDate;
    
    private String queryEndDate;
    
    //根据所出来区域查询
    private String queryAreaGroup;
    
    private String queryAreaGroupValue;
    //查询条件 end
    
    //查询当天所有的排班信息
    private List queryWorkSchedule = new ArrayList();
    
    //批量增加的时候获取房控人员名单和昨天对应人员所负责的区域
    private List allWorkSchedule;
    
    private int roomStatusWorkerNumber;
    
    //默认为明天日期
    private Date bDate;
    
    //默认为明天的日期
    private Date eDate;
    
    private String sucessSaveSign;
    
    private long workSchedualId;
    /**
     * 酒店记录ID
     */
    private String hotelScheduleId;
    /**
     * 操作类型: 操作类型，1：完成 2,：释放 3,工作状态关闭时退出的
     */
    private int optType;
    /**
     * 释放原因：1：电话未通 2：负责人不在 3：稍后提供
     */
    private Integer relaxreason;
    /**
     * 释放时选择的时间点
     */
    private Integer priTime;
    
    private int dateNum;
    
    private String tagForModifySchedule;
    
    //标示查询的时候是否为默认查询
    private String queryDateIsActive;
    
    //获取某个员工的信息，进行修改
    private HtlRoomControlWorkSchedule oneWorkerSchedule ;
    private String workAreaState;
    private String workAreaCity="";
    private String workMessage;
    
    //房态日志查询日期
    private Date queryRommControlBeginDate;
    
    private Date queryRommControlEndDate;
    
    private boolean controlRoomStateSchedule =false;
    
    private long hotelID;
    
    //延时操作时，新增一条交接事项记录，通过id获取增加之前最新的记录
	private  long htlRoomStatusProcessID;
	
	private List roomstateLogsLists;
	
	private int pageNumber;
	
	private int totalPageNumber;
	
	//查询范围内上A班的人数
	private int ondutyIsACount;
	
	//查询范围内上B班的人数
	private int ondutyIsBCount;
	
	//查询范围内总人数
	private int ondutyCount;
	
	//传真号码
	private String faxNum = "";
	
	private List hotelBookSetup;
	
	//传真个数
	private int hotelBookSetupNumber;
	
	//2为传真类型
	private String faxType= "2";
	/*
     * 昨天到今天所有房态更改记录
     */
    private List dateRoomStatusProcess = new ArrayList();
    
    private List protocolRommInfoLis;
    
    //发传真用begin 
    private String protocolBeginDate;
    
    private String protocolEndDate;
    
    private String linkMan;
    
    private String linkNumber;
    
    //记录总共用多少条房型+床型的数据
    private int roomBedNumber;
    
    private String sendStatus;
    
    private NewRoomControlFaxInfo roomControlFaxInfoForPreview;
	//发传真用end
    
    //房态处理来源记：1呼出，2呼入，3MSN，4QQ，5邮件
    private String dealWithSource;
	
	public String getDealWithSource() {
		return dealWithSource;
	}

	public void setDealWithSource(String dealWithSource) {
		this.dealWithSource = dealWithSource;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public List getDateRoomStatusProcess() {
		return dateRoomStatusProcess;
	}

	public void setDateRoomStatusProcess(List dateRoomStatusProcess) {
		this.dateRoomStatusProcess = dateRoomStatusProcess;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getTotalPageNumber() {
		return totalPageNumber;
	}

	public void setTotalPageNumber(int totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}

	public List getRoomstateLogsLists() {
		return roomstateLogsLists;
	}

	public void setRoomstateLogsLists(List roomstateLogsLists) {
		this.roomstateLogsLists = roomstateLogsLists;
	}

	public long getWorkSchedualId() {
		return workSchedualId;
	}

	public void setWorkSchedualId(long workSchedualId) {
		this.workSchedualId = workSchedualId;
	}

	public HtlRoomControlWorkSchedule getOneWorkerSchedule() {
		return oneWorkerSchedule;
	}

	public void setOneWorkerSchedule(HtlRoomControlWorkSchedule oneWorkerSchedule) {
		this.oneWorkerSchedule = oneWorkerSchedule;
	}

	public String getSucessSaveSign() {
		return sucessSaveSign;
	}

	public void setSucessSaveSign(String sucessSaveSign) {
		this.sucessSaveSign = sucessSaveSign;
	}

	public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }
    /**
     * 进入我的工作档案
     * @return
     */
    public String forwordToMyWorkSpace(){
    	roleUser = getCurrentUser();
    	if(null == roleUser)
    		return forwardMsg("您未登陆，请先登陆再操作！");
    	log.info("=================进入工作档案,检查用户是否可用======================");
    	if(checkRSCUserAvalible()){
    		log.info("=================进入工作档案,用户可用,进行分配======================");
    		newRoomControlService.allotHotelSchedule(roleUser);
    	}
    	List records = newRoomControlService.queryHotelScheduleByLoginName(roleUser.getLoginName());//查出已获取的酒店
    	HtlRoomcontrolWorkstation workstation = newRoomControlService.findWorkStationBy(roleUser);//查出当前工作进度
    	request.setAttribute("records", records);
    	request.setAttribute("workstation", workstation);
    	forward = "forwordToMyWorkSpace";
    	return forward;
    }
    /**
     * 进入房控酒店查询列表
     * @return
     * addby chenjuesu at 2009-12-31下午02:54:02
     */
    public String forwordToQueryHotelSchedule(){
    	roleUser = getCurrentUser();
    	if(null != roleUser){
    		request.setAttribute("loginName", roleUser.getLoginName());
    		String workareas= newRoomControlService.findWorkareasByUser(roleUser.getLoginName());
    		workAreaState=workareas;
    		if(null!=workAreaState){
	    		//是否是广州组
	    		if((workAreaState.indexOf("GDN")>=0)&&(workareas.indexOf("SZX")<0)&&(workareas.indexOf("ZHH")<0)){
	    			workAreaCity="other,";
	    		
	    		}else{
	    			//判断是否深圳组
	    			if((workareas.indexOf("SZX")>0)||(workareas.indexOf("ZHH")>0)&&(workAreaState.indexOf("GDN")<0)){
	    				workAreaState=workareas+"GDN,";
		    			workAreaCity="SZX,ZHH,";
	    			}
	    		}
    		}else{
    			workMessage="你今天还未排班，请与主管联系！";
    		}
    	}
    	forward = "forwordToQueryHotelSchedule";
    	return forward;
    }
    /**
     * 进入工作进度查询
     * @return
     * addby chenjuesu at 2010-1-7下午06:01:46
     */
    public String forwordToWorkStation(){
    	roleUser = getCurrentUser();
    	if(null == roleUser)
    		return forwardMsg("您未登陆，请先登陆再操作！");
    	List records = newRoomControlService.findAllWorkStations(roleUser);//查出所有员工的工作进度
    	int pageSize = 10;
    	int total = records.size();
    	int myPageCount = (total-1)/pageSize + 1;
    	if(myPageCount > 1){
	    	String pageNums = "";
	    	for(int i = 1 ; i <= myPageCount;i++){
	    		pageNums += i+",";
	    	}
	    	request.setAttribute("pageNums", pageNums);
    	}
    	request.setAttribute("pageSize", pageSize);
    	request.setAttribute("records", records);
    	request.setAttribute("isRSCAdmin", roleUser.isRSCAdmin());
    	request.setAttribute("thisLoginName", roleUser.getLoginName());
    	forward = "forwordToWorkStation";
    	return forward;
    }
    /**
     * optType :1：完成 2,：释放
     * @return
     * addby chenjuesu at 2010-1-11上午10:47:13
     */
    public String completeAndUnlock(){
    	roleUser = getCurrentUser();
        if(null == roleUser)
    		return forwardMsg("您未登陆，请先登陆再操作！");
    	newRoomControlService.completeAndUnlock(hotelScheduleId, roleUser, optType, relaxreason,priTime,dealWithSource);
    	//获取最新交接事项内容
    	if(2 == optType){
	    	dateRoomStatusProcess = newRoomControlService.findRoomStatusDateProcess(hotelID);
	        int i = dateRoomStatusProcess.size();
	        if (0 != i) {
	            HtlRoomStatusProcess htlRoomStatusProcess = new HtlRoomStatusProcess();
	            htlRoomStatusProcess = (HtlRoomStatusProcess) dateRoomStatusProcess.get(i - 1);
	            htlRoomStatusProcessID = htlRoomStatusProcess.getID();
	        }
	    	//新增一条交接事项
	    	newRoomControlService.saveProcessRemarkAndIsRoomStatus(hotelID,roleUser,relaxreason,priTime,htlRoomStatusProcessID);
    	}
    	return "forwordToWorkStation";//此结果没意义,只是为了需要
    }
    /**
     * 权限控制查询
     * @return
     */
    public String queryRoomControlPerson(){
    	String forward = "";
		if(super.getOnlineRoleUser()!=null && super.getOnlineRoleUser().getLoginName()!=null){
			UserWrapper roleUser = (UserWrapper)super.getOnlineRoleUser();
			//主管人员才能进行add/modify/delete操作
			if(roleUser.isRSCAdmin()){
				controlRoomStateSchedule = true;
			}
			try {
	    		if(null !=queryAreaGroup && !"".equals(queryAreaGroup)){
	    			queryAreaGroup = java.net.URLDecoder.decode(queryAreaGroup,"utf-8");
	    		}
	    		if(null !=personIdName && !"".equals(personIdName)){
	    			personIdName = java.net.URLDecoder.decode(personIdName,"utf-8");
	    		}
	    		if(null !=personId && !"".equals(personId)){
	    			personId = java.net.URLDecoder.decode(personId,"utf-8");
	    		}
				
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(),e);
			}
			//默认查询时间为当天
			if(!"1".equals(queryDateIsActive)){
				queryBeginDate=DateUtil.dateToString(DateUtil.getSystemDate());
				queryEndDate=DateUtil.dateToString(DateUtil.getSystemDate());
		    }
			
	    	queryWorkSchedule  = newRoomControlService.searchRoomControlPerson(personId,personIdName,queryBeginDate,queryEndDate,queryAreaGroupValue);
	    	
	    	//统计上A/B班的人数 新房控后续需求 add by zhijie.gu 2010-2-26
	    	for(int i=0;i<queryWorkSchedule.size();i++){
	    		HtlRoomControlWorkSchedule htlRoomControlWorkScheduleItems = (HtlRoomControlWorkSchedule)queryWorkSchedule.get(i);
	    		if("A".equals(htlRoomControlWorkScheduleItems.getOnDutyTime())){
	    			ondutyIsACount++;
	    			
	    		}else{
	    			ondutyIsBCount++;
	    		}
	    	 }
	    	//统计总人数
	    	ondutyCount = queryWorkSchedule.size();
	    	forward = "forwordToRoomControlPerson";	
			
		}else{
			return  super.forwardError("获取登陆用户信息失效,请重新登陆");
		}
    	return forward;
    	
    }
    
    public String addAllControlPerson(){
    	String forward = "";
		if(super.getOnlineRoleUser()!=null && super.getOnlineRoleUser().getLoginName()!=null){
			UserWrapper roleUser = (UserWrapper)super.getOnlineRoleUser();
			//主管人员才能进行add/modify/delete操作
			if(roleUser.isRSCAdmin()){
				allWorkSchedule = newRoomControlService.queryAllWorkSchedule();
		    	roomStatusWorkerNumber = allWorkSchedule.size();
		    	bDate = DateUtil.getDate(DateUtil.dateToString(DateUtil
						.getDate(DateUtil.getSystemDate(), 1)));
		    	eDate = DateUtil.getDate(DateUtil.dateToString(DateUtil
						.getDate(DateUtil.getSystemDate(), 1)));
			    forward = "forwardAddAllSchedule";
			}else{
				return super.forwardError("你没有操作的权限!");
			}
		}else{
			return  super.forwardError("获取登陆用户信息失效,请重新登陆");
		}
    	return forward;
    }
    
    public String  saveOrUpdate(){
    	
    	Map params = super.getParams();
    	List listRoomControlWorkSchedule = MyBeanUtil.getBatchObjectFromParam(params, HtlRoomControlWorkSchedule.class, roomStatusWorkerNumber);
    	List listRoomControlWorkScheduleTemp = new ArrayList();
    	if(null != listRoomControlWorkSchedule){
    		//过滤数据，当区域为空时，表示明天不上班，这条数据不保存进数据库
    		for(int j =0;j<listRoomControlWorkSchedule.size();j++){
    			HtlRoomControlWorkSchedule roomControlWorkScheduleItem =  (HtlRoomControlWorkSchedule)listRoomControlWorkSchedule.get(j);
    			if(!"".equals(roomControlWorkScheduleItem.getWorkAreas()) && null != roomControlWorkScheduleItem.getWorkAreas()){
    				listRoomControlWorkScheduleTemp.add(roomControlWorkScheduleItem);
    			}
    		}
    		
        	for(int i =0;i<listRoomControlWorkScheduleTemp.size();i++){
        		HtlRoomControlWorkSchedule roomControlWorkScheduleItems =  (HtlRoomControlWorkSchedule)listRoomControlWorkScheduleTemp.get(i);
        		roomControlWorkScheduleItems.setCreateById(super.getOnlineRoleUser().getLoginName());
        		roomControlWorkScheduleItems.setCreateByName(super.getOnlineRoleUser().getName());
        		roomControlWorkScheduleItems.setCreateTime(DateUtil.getSystemDate());
        		roomControlWorkScheduleItems.setModifyById(super.getOnlineRoleUser().getLoginName());
        		roomControlWorkScheduleItems.setModifyByName(super.getOnlineRoleUser().getName());
        		roomControlWorkScheduleItems.setModifyTime(DateUtil.getSystemDate());
        		try {
        			newRoomControlService.saveOrUpdateRoomSchedule(roomControlWorkScheduleItems);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(),e);
				} catch (InvocationTargetException e) {
					log.error(e.getMessage(),e);
				}
        	}
        }
    	sucessSaveSign = "1";
    	forward = "forwardAddAllSchedule";
    	return forward;
    }
    public String delectWorkerSchedule(){
    	String forward = "";
		if(super.getOnlineRoleUser()!=null && super.getOnlineRoleUser().getLoginName()!=null){
			UserWrapper roleUser = (UserWrapper)super.getOnlineRoleUser();
			//主管人员才能进行add/modify/delete操作
			if(roleUser.isRSCAdmin()){
				newRoomControlService.deleteWorkerSchedule(workSchedualId,super.getOnlineRoleUser().getLoginName(),super.getOnlineRoleUser().getName());
		    	//删除操作之后跳回到查询页面、查询条件保存原有，不用默认查询。
		    	queryDateIsActive = "1";
		    	forward ="forwardToQuery";
			}else{
				return super.forwardError("你没有操作的权限!");
			}
		}else{
			return  super.forwardError("获取登陆用户信息失效,请重新登陆");
		}
	
    	
		return queryRoomControlPerson();
    	
    }
    
    public String modifyOneWorkerSchedule(){
    	String forward = "";
		if(super.getOnlineRoleUser()!=null && super.getOnlineRoleUser().getLoginName()!=null){
			UserWrapper roleUser = (UserWrapper)super.getOnlineRoleUser();
			//主管人员才能进行add/modify/delete操作
			if(roleUser.isRSCAdmin()){
				oneWorkerSchedule = newRoomControlService.getOneWorkerSchedule(workSchedualId);
		    	forward ="forwardAddOneWorkerSchedule";
		    	tagForModifySchedule ="1" ; 
		    	
			}else{
				return super.forwardError("你没有操作的权限!");
			}
		}else{
			return  super.forwardError("获取登陆用户信息失效,请重新登陆");
		}
    	return forward;
    	
    }
    public String addOneWorkerSchedule(){
    	
    	String forward = "";
		if(super.getOnlineRoleUser()!=null && super.getOnlineRoleUser().getLoginName()!=null){
			UserWrapper roleUser = (UserWrapper)super.getOnlineRoleUser();
			//主管人员才能进行add/modify/delete操作
			if(roleUser.isRSCAdmin()){
				oneWorkerSchedule = newRoomControlService.getOneWorkerSchedule(workSchedualId);
		    	forward ="forwardAddOneWorkerSchedule";
			}else{
				return super.forwardError("你没有操作的权限!");
			}
		}else{
			return  super.forwardError("获取登陆用户信息失效,请重新登陆");
		}
    	return forward;
    }
    public String saveOneWorkerSchedule(){
    	
    	Map params = super.getParams();
    	List listRoomControlWorkSchedule = MyBeanUtil.getBatchObjectFromParam(params, HtlRoomControlWorkSchedule.class,dateNum);
    	if(null != listRoomControlWorkSchedule){
    		for(int i =0;i<listRoomControlWorkSchedule.size();i++){
        		HtlRoomControlWorkSchedule roomControlWorkScheduleItems =  (HtlRoomControlWorkSchedule)listRoomControlWorkSchedule.get(i);
        		roomControlWorkScheduleItems.setCreateById(super.getOnlineRoleUser().getLoginName());
        		roomControlWorkScheduleItems.setCreateByName(super.getOnlineRoleUser().getName());
        		roomControlWorkScheduleItems.setCreateTime(DateUtil.getSystemDate());
        		roomControlWorkScheduleItems.setModifyById(super.getOnlineRoleUser().getLoginName());
        		roomControlWorkScheduleItems.setModifyByName(super.getOnlineRoleUser().getName());
        		roomControlWorkScheduleItems.setModifyTime(DateUtil.getSystemDate());
        		try {
        			newRoomControlService.saveOrUpdateRoomSchedule(roomControlWorkScheduleItems);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(),e);
				} catch (InvocationTargetException e) {
					log.error(e.getMessage(),e);
				}
        	}
        }
    	sucessSaveSign = "1";
    	forward ="forwardSucessAddOneSchedule";
    	return forward;
    }
    
    /**
     * 跳转到房态操作日志页面 add by zhijie.gu 2010-01-13
     * @return
     */
    public String operateLogMethod(){
    	int pageN=0;
    	if(null == queryRommControlBeginDate || null ==queryRommControlEndDate){
    		queryRommControlBeginDate = DateUtil.getSystemDate();
        	queryRommControlEndDate = DateUtil.getSystemDate();
    	}
    	if(0 == pageNumber){
    		pageNumber=1;
    	}else if(1 == pageNumber){
    		pageN = 0;
    		
    	}else{
    		pageN = (pageNumber-1)*10;
    	}
    	roomstateLogsLists = newRoomControlService.queryRommControlLog(hotelID,queryRommControlBeginDate,queryRommControlEndDate,pageN);
    	
    	if(!roomstateLogsLists.isEmpty()){
    		for(int i=0;i<roomstateLogsLists.size();i++){
        		HotelRoomcontrolLogsDisplay roomControlLogsItems = (HotelRoomcontrolLogsDisplay)roomstateLogsLists.get(i);
        			totalPageNumber = roomControlLogsItems.getPageSize();
        			//循环一次，把总页数拿出来
        			break;
        	}
    		
    	}else{
    		totalPageNumber =0;
    	}
    	return "forwardToOperteLog";
    }
    
    
    /**
     * 保留房协议发传真给酒店（准备页面，填充相应数据） add by zhijie.gu 2010-5-18
     * @return
     */
    public String sendFaxToHotelPre(){
    	
    	faxNum = hotelService.getHotelFaxNo(hotelID);
    	
    	hotelBookSetup = hotelService.getHtlBookSetupList(hotelID, faxType);
//    	protocolRommInfoLis = newRoomControlService.findAllRoomBedByHotelId(hotelID);
//    	if(null !=protocolRommInfoLis)
//    		roomBedNumber = protocolRommInfoLis.size();
        if (null != hotelBookSetup) {
             hotelBookSetupNumber = hotelBookSetup.size();
         }
    	return "sendFaxToHotel";
    }
    
    /**
     * 保留房协议发传真给酒店 add by zhijie.gu 2010-5-18
     * @return
     */
    public String sendFaxToHotel(){
    	Map paramsMap = this.getParams();
    	Map roomTypeMap = new HashMap();
    	Map bedType = new HashMap();
		bedType.put("1", "大床");
		bedType.put("2", "双床");
		bedType.put("3", "单床");
    	List<FaxInfoForRoomcontrol> faxInfoList = MyBeanUtil.getBatchObjectFromParam(paramsMap, FaxInfoForRoomcontrol.class,
    			 roomBedNumber+1);
    	//获取roomtype中id和对应房型名称的map
    	if(hotelID>0){
    		roomTypeMap = newRoomControlService.getRoomTypeIdNameMapByHotelId(hotelID);
    		
    	}
    	//将房型id和床型参数转换成房型名称、床型名称
    	if( null != faxInfoList){
    		for(FaxInfoForRoomcontrol faxInfoItem : faxInfoList){
    			String roomTypeName = roomTypeMap.get(faxInfoItem.getRoomTypeName()).toString();
    			
    			String bedName = bedType.get(faxInfoItem.getBedTypeName()).toString();
    			faxInfoItem.setRoomTypeName(roomTypeName);
    			faxInfoItem.setBedTypeName(bedName);
    		}
    	}
    	NewRoomControlFaxInfo roomControlFaxInfo = new NewRoomControlFaxInfo();
    	roomControlFaxInfo.setLinkMan(linkMan);
    	roomControlFaxInfo.setLinkNumber(linkNumber);
    	roomControlFaxInfo.setProtocolBeginDate(protocolBeginDate);
    	roomControlFaxInfo.setProtocolEndDate(protocolEndDate);
    	roomControlFaxInfo.setRoomBedInfo(faxInfoList);
    	roomControlFaxInfo.setToday(DateUtil.dateToString(new Date()));
    	Long ret = null;
    	//发传真
    	try {
            ret = 	newRoomControlService.sendFaxForRoomcontrol(hotelID,faxType,faxNum,
            		this.getOnlineRoleUser(),roomControlFaxInfo);
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            // 发送失败处理
            sendStatus = "2";
        }
        if (null == ret || 0 >= ret) {
            sendStatus = "2";
        } else {
            sendStatus = "1";
        }
    
        if (null != hotelBookSetup) {
             hotelBookSetupNumber = hotelBookSetup.size();
         }
        if ("2".equals(sendStatus)) {
            return super.forwardMsgBox("发送失败！", "");
        } else {
            return super.forwardMsgBox("发送成功！", "");
        }
    }

    public String  sendFaxToHotelPreview(){
    	Map paramsMap = this.getParams();
    	Map roomTypeMap = new HashMap();
    	Map bedType = new HashMap();
		bedType.put("1", "大床");
		bedType.put("2", "双床");
		bedType.put("3", "单床");
    	List<FaxInfoForRoomcontrol> faxInfoList = MyBeanUtil.getBatchObjectFromParam(paramsMap, FaxInfoForRoomcontrol.class,
    			 roomBedNumber+1);
    	//获取roomtype中id和对应房型名称的map
    	if(hotelID>0){
    		roomTypeMap = newRoomControlService.getRoomTypeIdNameMapByHotelId(hotelID);
    		
    	}
    	//将房型id和床型参数转换成房型名称、床型名称
    	if( null != faxInfoList){
    		for(FaxInfoForRoomcontrol faxInfoItem : faxInfoList){
    			String roomTypeName="";
    			String bedName="";
    			if(null!=roomTypeMap.get(faxInfoItem.getRoomTypeName())){
    				roomTypeName= roomTypeMap.get(faxInfoItem.getRoomTypeName()).toString();
    			}
    			if(null!=bedType.get(faxInfoItem.getBedTypeName())){
    				bedName = bedType.get(faxInfoItem.getBedTypeName()).toString();
    			}
    			
    			faxInfoItem.setRoomTypeName(roomTypeName);
    			faxInfoItem.setBedTypeName(bedName);
    		}
    	}
    	HtlHotel hotel = hotelService.findHotel(hotelID);
    	roomControlFaxInfoForPreview = new NewRoomControlFaxInfo();
    	roomControlFaxInfoForPreview.setLinkMan(linkMan);
    	roomControlFaxInfoForPreview.setLinkNumber(linkNumber);
    	roomControlFaxInfoForPreview.setProtocolBeginDate(protocolBeginDate);
    	roomControlFaxInfoForPreview.setProtocolEndDate(protocolEndDate);
    	roomControlFaxInfoForPreview.setRoomBedInfo(faxInfoList);
    	roomControlFaxInfoForPreview.setToday(DateUtil.dateToString(new Date()));
    	roomControlFaxInfoForPreview.setHotelChnName(hotel.getChnName() == null?hotel.getEngName():hotel.getChnName());
 
    	return "sendFaxToHotelPreview";
    	
    }
	public NewRoomControlService getNewRoomControlService() {
		return newRoomControlService;
	}

	public void setNewRoomControlService(NewRoomControlService newRoomControlService) {
		this.newRoomControlService = newRoomControlService;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonIdName() {
		return personIdName;
	}

	public void setPersonIdName(String personIdName) {
		this.personIdName = personIdName;
	}

	public String getQueryAreaGroup() {
		return queryAreaGroup;
	}

	public void setQueryAreaGroup(String queryAreaGroup) {
		this.queryAreaGroup = queryAreaGroup;
	}

	public List getQueryWorkSchedule() {
		return queryWorkSchedule;
	}

	public List getAllWorkSchedule() {
		return allWorkSchedule;
	}

	public void setAllWorkSchedule(List allWorkSchedule) {
		this.allWorkSchedule = allWorkSchedule;
	}

	public void setQueryWorkSchedule(List queryWorkSchedule) {
		this.queryWorkSchedule = queryWorkSchedule;
	}

	public int getRoomStatusWorkerNumber() {
		return roomStatusWorkerNumber;
	}

	public void setRoomStatusWorkerNumber(int roomStatusWorkerNumber) {
		this.roomStatusWorkerNumber = roomStatusWorkerNumber;
	}

	public Date getBDate() {
		return bDate;
	}

	public void setBDate(Date date) {
		bDate = date;
	}

	public Date getEDate() {
		return eDate;
	}

	public void setEDate(Date date) {
		eDate = date;
	}

	public String getQueryAreaGroupValue() {
		return queryAreaGroupValue;
	}

	public void setQueryAreaGroupValue(String queryAreaGroupValue) {
		this.queryAreaGroupValue = queryAreaGroupValue;
	}

	public int getDateNum() {
		return dateNum;
	}

	public void setDateNum(int dateNum) {
		this.dateNum = dateNum;
	}

	public String getTagForModifySchedule() {
		return tagForModifySchedule;
	}

	public void setTagForModifySchedule(String tagForModifySchedule) {
		this.tagForModifySchedule = tagForModifySchedule;
	}

	public String getQueryDateIsActive() {
		return queryDateIsActive;
	}

	public void setQueryDateIsActive(String queryDateIsActive) {
		this.queryDateIsActive = queryDateIsActive;
	}

	public String getQueryBeginDate() {
		return queryBeginDate;
	}

	public void setQueryBeginDate(String queryBeginDate) {
		this.queryBeginDate = queryBeginDate;
	}

	public String getQueryEndDate() {
		return queryEndDate;
	}

	public void setQueryEndDate(String queryEndDate) {
		this.queryEndDate = queryEndDate;
	}

	public String getHotelScheduleId() {
		return hotelScheduleId;
	}

	public void setHotelScheduleId(String hotelScheduleId) {
		this.hotelScheduleId = hotelScheduleId;
	}

	public int getOptType() {
		return optType;
	}

	public void setOptType(int optType) {
		this.optType = optType;
	}

	public Integer getRelaxreason() {
		return relaxreason;
	}

	public void setRelaxreason(Integer relaxreason) {
		this.relaxreason = relaxreason;
	}

	public Integer getPriTime() {
		return priTime;
	}

	public void setPriTime(Integer priTime) {
		this.priTime = priTime;
	}

	public String getWorkAreaState() {
		return workAreaState;
	}

	public void setWorkAreaState(String workAreaState) {
		this.workAreaState = workAreaState;
	}

	public String getWorkAreaCity() {
		return workAreaCity;
	}

	public void setWorkAreaCity(String workAreaCity) {
		this.workAreaCity = workAreaCity;
	}

	public Date getQueryRommControlBeginDate() {
		return queryRommControlBeginDate;
	}

	public void setQueryRommControlBeginDate(Date queryRommControlBeginDate) {
		this.queryRommControlBeginDate = queryRommControlBeginDate;
	}

	public Date getQueryRommControlEndDate() {
		return queryRommControlEndDate;
	}

	public void setQueryRommControlEndDate(Date queryRommControlEndDate) {
		this.queryRommControlEndDate = queryRommControlEndDate;
	}

	public boolean isControlRoomStateSchedule() {
		return controlRoomStateSchedule;
	}

	public void setControlRoomStateSchedule(boolean controlRoomStateSchedule) {
		this.controlRoomStateSchedule = controlRoomStateSchedule;
	}

	public long getHotelID() {
		return hotelID;
	}

	public void setHotelID(long hotelID) {
		this.hotelID = hotelID;
	}

	public String getWorkMessage() {
		return workMessage;
	}

	public void setWorkMessage(String workMessage) {
		this.workMessage = workMessage;
	}

	public long getHtlRoomStatusProcessID() {
		return htlRoomStatusProcessID;
	}

	public void setHtlRoomStatusProcessID(long htlRoomStatusProcessID) {
		this.htlRoomStatusProcessID = htlRoomStatusProcessID;
	}

	public int getOndutyIsACount() {
		return ondutyIsACount;
	}

	public void setOndutyIsACount(int ondutyIsACount) {
		this.ondutyIsACount = ondutyIsACount;
	}

	public int getOndutyIsBCount() {
		return ondutyIsBCount;
	}

	public void setOndutyIsBCount(int ondutyIsBCount) {
		this.ondutyIsBCount = ondutyIsBCount;
	}

	public int getOndutyCount() {
		return ondutyCount;
	}

	public void setOndutyCount(int ondutyCount) {
		this.ondutyCount = ondutyCount;
	}

	public List getHotelBookSetup() {
		return hotelBookSetup;
	}

	public void setHotelBookSetup(List hotelBookSetup) {
		this.hotelBookSetup = hotelBookSetup;
	}

	public int getHotelBookSetupNumber() {
		return hotelBookSetupNumber;
	}

	public void setHotelBookSetupNumber(int hotelBookSetupNumber) {
		this.hotelBookSetupNumber = hotelBookSetupNumber;
	}

	public IHotelService getHotelService() {
		return hotelService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public String getFaxNum() {
		return faxNum;
	}

	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}

	public List getProtocolRommInfoLis() {
		return protocolRommInfoLis;
	}

	public void setProtocolRommInfoLis(List protocolRommInfoLis) {
		this.protocolRommInfoLis = protocolRommInfoLis;
	}

	public String getProtocolBeginDate() {
		return protocolBeginDate;
	}

	public void setProtocolBeginDate(String protocolBeginDate) {
		this.protocolBeginDate = protocolBeginDate;
	}

	public String getProtocolEndDate() {
		return protocolEndDate;
	}

	public void setProtocolEndDate(String protocolEndDate) {
		this.protocolEndDate = protocolEndDate;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkNumber() {
		return linkNumber;
	}

	public void setLinkNumber(String linkNumber) {
		this.linkNumber = linkNumber;
	}

	public int getRoomBedNumber() {
		return roomBedNumber;
	}

	public void setRoomBedNumber(int roomBedNumber) {
		this.roomBedNumber = roomBedNumber;
	}

	public NewRoomControlFaxInfo getRoomControlFaxInfoForPreview() {
		return roomControlFaxInfoForPreview;
	}

	public void setRoomControlFaxInfoForPreview(
			NewRoomControlFaxInfo roomControlFaxInfoForPreview) {
		this.roomControlFaxInfoForPreview = roomControlFaxInfoForPreview;
	}
  
}
