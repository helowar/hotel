package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.HraOrderType;
import com.mangocity.hotel.base.constant.WorkType;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.order.manager.HraManager;
import com.mangocity.hotel.order.persistence.OrFaxLog;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrToContractgroup;
import com.mangocity.hotel.order.service.OperOrderDerferTimeService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

/**
 * 中台操作相关
 * 
 * @author chenkeming
 * 
 */
public class HRAOperateAction extends OrderAction {

    private static final long serialVersionUID = 511743134127614421L;

    private Long[] selID;

    private HraManager hraManager;
    
    private SystemDataService systemDataService;

    protected List userList;

    private String selIDs;

    private String hraOrderTypeStr;

    /**
     * 疑难单原因V2.8.1 add by guojun 2009-05-19 16:56
     */
    private long difficultReason;

    /**
     * 疑难单类别V2.8.1 add by guojun 2009-05-19 16:56
     */
    private long difficultType;

    /**
     * 疑难单责任部门V2.8.1 add by guojun 2009-05-19 16:56
     */
    private long difficultDep;

    /**
     * 房控处理结果
     * 
     * @author chenkeming Jul 2, 2009 6:14:37 PM
     */
    private String procRes;
    
    /**
     * 房控重构　增加合约组处理结果
     * 
     * @author chenjuesu 2010-1-4
     */
    private Integer contactResult;

    /**
     * 转合约组原因 add by guojun V2.8.1 2009-05-19 16:56
     */
    private String remark;

    protected static final String CONNECT_ORDER = "connect_order";

    /**
     * 资源接口
     */
    private ResourceManager resourceManager;

    private Integer  relaxTime;
    private Integer groupId;
    private Integer reason;
    private OperOrderDerferTimeService operOrderDerferTimeService;
    /**
     * 从菜单左边点击"我的工作档案"进入
     * 
     * @return
     */
    public String firstMyList() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        // if(!handleMemberLogin()) {
        // return this.forwardError("获取会员信息出错！");
        // }
        // member = getOnlineMember();
        roleUser = getCurrentUser();
        if(roleUser == null)
        	return forwardMsg("您尚未登陆，请重新登陆!");
        user = getOnlineWorkStates();
        //自动分配订单
        orderService.getMidOrderTransfer().autoAssignOrder(user);
        
        return "firstMyList";
    }

    /**
     * 从菜单左边点击"处理预订单"进入
     * 
     * @return
     */
    public String firstWaitAssign() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        // if(!handleMemberLogin()) {
        // return this.forwardError("获取会员信息出错！");
        // }
        // member = getOnlineMember();

        user = getOnlineWorkStates();
        roleUser = getOnlineRoleUser();
        String choose = (String) getFromSession("waitAssignChoose");
        if (!StringUtil.isValidStr(choose)) {
            super.putSession("waitAssignChoose", user.getGroups());
        }

        if (roleUser.isOrgMidAdmin()) {
            OrParam orParam = systemDataService.getSysParamByName("ASSIGN_MODE");
            request.setAttribute("orParam", orParam);
        }

        // 房态疑难组人员查询条件组别默认是RSC下的四个组别
        if (null != roleUser && null != user && roleUser.isOrgRSC()
            && user.getType() == WorkType.RSC) {
            // tempHraOrderTypeStr = "11,12,13,14"
        	String tempHraOrderTypeStr = HraOrderType.RSC_BBQ + "," + HraOrderType.RSC_GZQ + ","
						            + HraOrderType.RSC_GAZ + "," + HraOrderType.RSC_SHZ+","+ HraOrderType.RSC_BJZ + "," 
						            + HraOrderType.RSC_DBZ +","+ HraOrderType.RSC_JJZ;
            super.putSession("waitAssignChoose", tempHraOrderTypeStr);
        }

        //userList = workStatesManager.lstWorkStatesByType(WorkType.HRA);
        return "firstWaitAssign";
    }

	/**
     * 从菜单左边点击"退款审批"进入
     * 
     * @return
     */
    public String firstWaitRefund() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        // if(!handleMemberLogin()) {
        // return this.forwardError("获取会员信息出错！");
        // }
        // member = getOnlineMember();

        return "firstWaitRefund";
    }

    /**
     * 从菜单左边点击"财务付款"进入
     * 
     * @return
     */
    public String firstWaitPrepay() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        // if(!handleMemberLogin()) {
        // return this.forwardError("获取会员信息出错！");
        // }
        // member = getOnlineMember();

        return "firstWaitPrepay";
    }

    /**
     * 从菜单左边点击"财务退款"进入
     * 
     * @return
     */
    public String firstWaitRefunding() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        // if(!handleMemberLogin()) {
        // return this.forwardError("获取会员信息出错！");
        // }
        // member = getOnlineMember();

        return "firstWaitRefunding";
    }

    /**
     * 从菜单左边点击"交接班订单处理"进入
     * 
     * @return
     */
    public String firstNextTeam() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        /**
         * 当房控疑难部门工号人员进入该界面，"RSC深圳组"、"RSC广州组"、"RSC华北组"和"RSC华东组"选项默认被选中，并以这四个组别为条件查询结果。 v2.4.2 by
         * chenjiajie 2008-12-29 begin
         **/
        roleUser = super.getOnlineRoleUser();
        user = super.getOnlineWorkStates();
        // 房态疑难组人员默认查询房态疑难部门的交接订单，页面默认选中其组别
        if (null != roleUser && null != user && roleUser.isOrgRSC()
            && user.getType() == WorkType.RSC) {
            // String tempHraOrderTypeStr = "11,12,13,14"
        	String tempHraOrderTypeStr = HraOrderType.RSC_BBQ + "," + HraOrderType.RSC_GZQ + ","
				            + HraOrderType.RSC_GAZ + "," + HraOrderType.RSC_SHZ+","+ HraOrderType.RSC_BJZ + "," 
				            + HraOrderType.RSC_DBZ +","+ HraOrderType.RSC_JJZ;
            hraOrderTypeStr = tempHraOrderTypeStr;
            super.putSession("waitAssignChoose", tempHraOrderTypeStr);
        }
        /** v2.4.2 by chenjiajie 2008-12-29 end **/
        // if(!handleMemberLogin()) {
        // return this.forwardError("获取会员信息出错！");
        // }
        // member = getOnlineMember();
        return "firstNextTeam";
    }

    /**
     * 交接页面
     * 
     * @return
     */
    public String connect() {
        String[] ids = selIDs.split(",");
        if (1 >= ids.length) {
            order = super.getOrder(Long.parseLong(ids[0]));
            userList = workStatesManager.lstHraWorkerByGroup(order.getHraOrderType(), true);
        } else {
            userList = workStatesManager.lstWorkStatesByType(WorkType.HRA);
        }

        return CONNECT_ORDER;
    }

    /**
     * 批量分配订单
     * 
     * @return
     */
    public String batchPutToOrderList() {
    	try{
	        Map params = super.getParams();
	        boolean bResult = false;
	        roleUser = getOnlineRoleUser();
	        OrWorkStates user = workStatesManager.returnWorkStatesBylogonId((String) params
	            .get("Operator"));
	        selIDs = (String) params.get("checkedId");
	       /* String[] ids = selIDs.split(",");
	        selID = new Long[ids.length];
	        for (int i = 0; i < ids.length; i++) {
	           selID[i] = Long.parseLong(ids[i]);
	           
	        }
	        
	        bResult = hraManager.batchArrangeOrder(selID, user, roleUser);*/
	        //改为新的订单分配规则（中台订单流转优化）
	        String orderIds[]=selIDs.split(",");
	        orderService.getMidOrderTransfer().adminAssignOrder(roleUser, selIDs, user);	       
	        for(int m=0;m<orderIds.length;m++){
	         operOrderDerferTimeService.modifyDerferOrder(Long.parseLong(orderIds[m]));
	        }
            return forwardMsgBox("分配给:" + user.getName() + ",成功！", "refreshSelf()");
    	}catch(Exception e){
            return forwardMsgBox("分配给:" + user.getName() + ",失败！", null);
    	}

    }

    /**
     * 转移到中台专家组
     * 
     * @return
     */
    public String moveToMiddleHard() {

        order = getOrder(orderId);

        roleUser = getOnlineRoleUser();

        Date now = new Date();

        // 写日志
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        String diffRes = "<font color='red'>";
        diffRes += "疑难单类别:&nbsp;"
            + resourceManager.getDescription("res_difficultOrderType", difficultType) + ",";
        diffRes += "疑难单原因:&nbsp;"
            + resourceManager.getDescription("res_difficultOrderReason", difficultReason) + ",";
        diffRes += "疑难单责任部门:&nbsp;"
            + resourceManager.getDescription("res_difficultOrderDept", difficultDep);
        diffRes += "</font>";
        handleLog.setContent("疑难订单转中台专家组:" + diffRes);
        handleLog.setModifiedTime(now);
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);
        order.setHraOrderType(HraOrderType.HARD);
        // 将日志保存到数据库
        saveOrUpdateOrder(order);

        // 保存疑难单类别，原因，责任部门
        OrToContractgroup orToContractgroup = orderService.getContractgroup(orderId);
        if (null == orToContractgroup) {
            // 创建实体类型
            orToContractgroup = new OrToContractgroup();
            orToContractgroup.setOrderid(orderId);
            orToContractgroup.setOrdercd(order.getOrderCD());
            orToContractgroup.setHotelid(order.getHotelId());

            // 写日志
            log.error("房控疑难单没找到相应的OrToContractgroup记录,订单编号:" + order.getOrderCD());
        }
        orToContractgroup.setDifficultType(difficultType);
        orToContractgroup.setDifficultReason(difficultReason);
        orToContractgroup.setDifficultDep(difficultDep);
        orToContractgroup.setConfirst(procRes);
        /**房控重构　增加合约组处理结果和房控转专家组人　addby juesuchen 2010-1-4 begin **/
        orToContractgroup.setContactResult(contactResult);
        orToContractgroup.setContractToRoomcontroller(roleUser.getLoginName());//主要用于区分当合约组转回房控时，人名不一致的情况
        /**房控重构　增加合约组处理结果和房控转专家组人　addby juesuchen 2010-1-4 end **/
        orToContractgroup.setRscToCCTime(now);
        orToContractgroup.setRemark(remark);
        orderService.toContractGoup(orToContractgroup);
        
        /** 让此订单在中台专家组执行分配 Addby juesuchen 2010-3-30 begin **/
        orderService.getMidOrderTransfer().orderTransTo(roleUser, orderId, 6, 99);// 6 代表 专家组,原因传入 99，特指是房控转专家
        /** 让此订单在中台专家组执行分配 Addby juesuchen 2010-3-30 begin **/
        
        
        /*  中台订单流转优化后，不需要此分配模式 addby juesuchen 2010-3-30 begin
        user = getOnlineWorkStates();
        selID = new Long[1];
        selID[0] = orderId;

        boolean bResult = hraManager.batchMoveToHard(String.valueOf(orderId), user, true);
        if (bResult) {
            request.setAttribute("message", "OK");
        }  end */
        request.setAttribute("message", "OK");
        return "toMidHardGroup";
    }

    /**
     * 在处理预订单页面中分配或重新分配指定订单
     * 
     * @return
     */
    public String assign() {
        String[] ids = selIDs.split(",");
        /** 中台房控疑难部门人员分配逻辑改变 begin by chenjiajie V2.4.2 2008-12-29 **/
        roleUser = super.getOnlineRoleUser();
        user = super.getOnlineWorkStates();
        Long firstOrder = Long.parseLong(ids[0]);
        Long orderGroupId=orderService.getOrderGroup(firstOrder);
        // 判断是否中台房控疑难部门人员
        if (null != roleUser && null != user && roleUser.isOrgRSC()
            && user.getType() == WorkType.RSC) {
        	log.info("在处理预订单页面中分配或重新分配指定订单[中台房控疑难部门人员],OrderId:" + firstOrder);
            // 只能分配给同一个组别的人员
            userList = workStatesManager.lstRSCWorkerByGroup(orderGroupId.intValue(), true);
            if (1 >= ids.length) {
                // 只查询中台专家组的人员
                List userList1 = workStatesManager.lstHraWorkerByGroup(
                    new int[] { 6 }, true);//新中台流转后，专家组为 6
                request.setAttribute("userList1", userList1);
                request.setAttribute("orderGroupId", orderGroupId);
                request.setAttribute("idLen", 1);
            }
        }
        /** 中台房控疑难部门人员分配逻辑改变 end by chenjiajie V2.4.2 2008-12-29 **/
        else {
           /* if (1 >= ids.length) {
                order = super.getOrder(Long.parseLong(ids[0]));
                userList = workStatesManager.lstHraWorkerByGroup(order.getHraOrderType(), true);
                List userList1 = workStatesManager.lstHraWorkerNotByGroup(order.getHraOrderType(),
                    true);
                request.setAttribute("userList1", userList1);
                request.setAttribute("idLen", 1);
            } else {*/
        	  //userList = workStatesManager.lstWorkStatesByType(WorkType.HRA, true);
	        	//List workList = workStatesManager.lstWorkStatesByTypeNew(user.getLogonId());
	        	userList=workStatesManager.lstWorkStatesByGroups(user.getLogonId(), String.valueOf(orderGroupId));
	        	List userList1=workStatesManager.lstWorkStatesByOtherGroups(user.getLogonId(), String.valueOf(orderGroupId));
	            request.setAttribute("userList1", userList1);
            	return "assign_order_new";
            //}
        }

        // Map params = super.getParams();
        // selIDs = (String)params.get("selIDs");
        return "assign_order";
    }    

    /**
     * 读FaxLog
     * 
     * @return
     */
    public String readFaxLog() {
        roleUser = getOnlineRoleUser();
        String assignTo = (String) getParams().get("assignTo");
        OrFaxLog faxLog = null;
        if (StringUtil.isValidStr(assignTo) && assignTo.equals(roleUser.getLoginName())) {
            faxLog = orderService.updateFaxLog(Long.parseLong(selIDs), true);
        } else {
            faxLog = orderService.updateFaxLog(Long.parseLong(selIDs), false);
        }
        request.setAttribute("faxLogURL", faxLog.getUrl());
        return "readFaxLog";
    }

    /**
     * 跳转到转中台专家组页面
     * addby juesuchen 2010-1-5
     * @return
     */
    public String gotoMidHardPre(){
    	if(orderId != null){
    		OrToContractgroup orToContractgroup = orderService.getContractgroup(orderId);
    		if(null != orToContractgroup){
    			request.setAttribute("procRes", orToContractgroup.getConsecond());
    		}
    		return "gotoMidHardPre";
    	}
    	return forwardError("订单ID不存在！");
    }
    /**
     * 订单流转：订单实时监控及工作效率查询
     * chenjuesu editAt 2010-3-24 上午11:22:13
     * @return
     */
    public String orderAndWorkingRateQuery(){
    	Map params = super.getParams();
    	String beginDate = (String)params.get("beginDate");
    	String endDate = (String)params.get("endDate");
    	String groups = (String)params.get("groups");
    	String workStatusCheck = (String)params.get("workStatusCheck");
    	Object optType = params.get("optType");
    	if(!(StringUtil.isValidStr(beginDate) && StringUtil.isValidStr(endDate))){
    		Date today = new Date();
        	beginDate = DateUtil.dateToString(today);
        	endDate = DateUtil.dateToString(today);
        	params.put("beginDate", beginDate);
        	params.put("endDate", endDate);
    	}
    	params.put("workStatusCheck", workStatusCheck);
    	if(null == optType)
    		optType = "1";
    	params.put("optType", optType);
    	List orderstatus = hraManager.getWorkingRateAndOrderStations(params);
    	Date[] days = DateUtil.getDaysBetween(DateUtil.getDate(beginDate), DateUtil.getDate(endDate));
    	List dayList = new ArrayList();
    	for (int i = 0; i < days.length; i++) {
    		dayList.add(DateUtil.getDayAndMonth(days[i]));
		}
    	request.setAttribute("orderstatus", orderstatus);
    	request.setAttribute("dayList", dayList);
    	request.setAttribute("beginDate", beginDate);
    	request.setAttribute("endDate", endDate);
    	request.setAttribute("groups", groups);
    	request.setAttribute("workStatusCheck", workStatusCheck);
    	request.setAttribute("optType", optType);
    	request.setAttribute("loginName", params.get("loginName"));
    	request.setAttribute("name", params.get("name"));
    	return "toOrderAndWorkingRateQuery";
    }
    
    public String getOrderStations(){
    	Map params = super.getParams();
    	Integer optType = Integer.parseInt(params.get("optType").toString());
    	if(4 == optType){//在工作档案点击获取工作效率
    		roleUser = getCurrentUser();
    		if(!roleUser.isRSCUser()){
    	        List workStateSkillList=workStatesManager.returnWorkStateSkillByLogonId(roleUser.getLoginName());
    	        List workRateList=hraManager.getMyWorkingRateByLoginName(roleUser.getLoginName());
    	        request.setAttribute("workStateSkillList", workStateSkillList);
    	        request.setAttribute("workStateSkillCount", workStateSkillList.size());
    	        request.setAttribute("workRateList", workRateList);
    	        request.setAttribute("workRateListCount", workRateList.size());
            }
    	}else{
    		List records = hraManager.getWorkingRateAndOrderStationsByAjax(optType,params);
        	if(optType == 3)
        		optType = 2;//用显示员工的格式
        	request.setAttribute("records", records);
    	}
    	request.setAttribute("optType", optType);
    	return "toWorkingRateByAjax";
    }
    public String autoAllotOrderBy(){
    	user = getOnlineWorkStates();
    	int result = orderService.getMidOrderTransfer().autoAllotOrderBy(user);
    	request.setAttribute("result", result);
    	return "jqueryAjax";
    }
    public String hraOrderConnect(){
    	
    	roleUser = getOnlineRoleUser();
    	user = getOnlineWorkStates();
    	String msg = null;
    	if(groupId==99){
    		orderService.getMidOrderTransfer().orderRelaxTo(roleUser, orderId, relaxTime, reason);//释放	
    		msg = "订单释放到"+relaxTime+"分钟后处理！";
    	}else{
    		if(groupId==9){
    			orderService.getMidOrderTransfer().orderToDoTomorrow(roleUser, orderId, reason);//交接班
    			msg = "订单已交接到明天9：00后处理！";
    		}else{
    			orderService.getMidOrderTransfer().orderTransTo(roleUser, orderId, groupId, reason);
    			msg = "订单交接成功！";
    		}
    	}
    	//交接完后，再进行分配订单
    	orderService.getMidOrderTransfer().autoAssignOrder(user);
    	operOrderDerferTimeService.modifyDerferOrder(orderId);
    	return  forwardMsgBox(msg, "closeMsgBox()");
    }
    /** getter and setter begin */

    public Long[] getSelID() {
        return selID;
    }

    public void setSelID(Long[] selID) {
        this.selID = selID;
    }

    public HraManager getHraManager() {
        return hraManager;
    }

    public void setHraManager(HraManager hraService) {
        this.hraManager = hraService;
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

    public String getSelIDs() {
        return selIDs;
    }

    public void setSelIDs(String selIDs) {
        this.selIDs = selIDs;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public String getHraOrderTypeStr() {
        return hraOrderTypeStr;
    }

    public void setHraOrderTypeStr(String hraOrderTypeStr) {
        this.hraOrderTypeStr = hraOrderTypeStr;
    }

    /** getter and setter end */

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getDifficultDep() {
        return difficultDep;
    }

    public void setDifficultDep(long difficultDep) {
        this.difficultDep = difficultDep;
    }

    public long getDifficultReason() {
        return difficultReason;
    }

    public void setDifficultReason(long difficultReason) {
        this.difficultReason = difficultReason;
    }

    public long getDifficultType() {
        return difficultType;
    }

    public void setDifficultType(long difficultType) {
        this.difficultType = difficultType;
    }

    /** getter and setter end */

    public String getProcRes() {
        return procRes;
    }

    public void setProcRes(String procRes) {
        this.procRes = procRes;
    }

	public Integer getContactResult() {
		return contactResult;
	}

	public void setContactResult(Integer contactResult) {
		this.contactResult = contactResult;
	}
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getReason() {
		return reason;
	}

	public void setReason(Integer reason) {
		this.reason = reason;
	}

	public Integer getRelaxTime() {
		return relaxTime;
	}

	public void setRelaxTime(Integer relaxTime) {
		this.relaxTime = relaxTime;
	}
	
	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

	public OperOrderDerferTimeService getOperOrderDerferTimeService() {
		return operOrderDerferTimeService;
	}

	public void setOperOrderDerferTimeService(
			OperOrderDerferTimeService operOrderDerferTimeService) {
		this.operOrderDerferTimeService = operOrderDerferTimeService;
	}
	
}
