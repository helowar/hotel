package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.WorkType;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.persistence.WorkStatesSkill;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.newroomcontrol.service.NewRoomControlService;
import com.mangocity.hotel.order.manager.MidOrderTransfer;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 *设置我的工作状态
 * 
 * @author HWL
 * 
 */
public class workStatesOperateAction// parasoft-suppress NAMING.NCL "类名修改牵涉太广，暂不修改。" 
        extends GenericAction {

    protected List workStatesListByHra;

    protected List workStatesListByAudit;
    private List workStateSkillList;
    protected OrWorkStates workStates;

    /** add by chenjiajie 2008-10-20 begin **/
    protected OrWorkStates workStatesQueryBean;
    /** addby juesuchen 房控退出工作档案时,清除里面的酒店 2010-1-13 **/
    private NewRoomControlService newRoomControlService;

    protected int displayTag = 0; // 是否默认展开,0:隐藏,1:展开

    /** add by chenjiajie 2008-10-20 end **/

    private String logonId;
    
    private String forwordStr;
    
    /**
     * 中台订单流转类
     */
    private MidOrderTransfer midOrderTransfer;
    
    public MidOrderTransfer getMidOrderTransfer() {
		return midOrderTransfer;
	}

	public void setMidOrderTransfer(MidOrderTransfer midOrderTransfer) {
		this.midOrderTransfer = midOrderTransfer;
	}

	public String setMyWorkStates() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();

        roleUser = getOnlineRoleUser();

        workStates = getOnlineWorkStates();

        // workStatesListByHra=workStatesManager.lstWorkStatesByType(WorkType.HRA);//1:HRA
        // workStatesListByAudit=workStatesManager.lstWorkStatesByType(WorkType.AUDIT);//1:日审
        // putSession("workStates", workStates);

        OrWorkStates queryBean = new OrWorkStates();
        queryBean.setState(-1); // 设为-1，查询所有状态
        workStatesListByHra = workStatesManager.lstWorkStatesDynamic(queryBean, 0); // 查询所有工作状态
        displayTag = 0; // 默认隐藏

        return "setMyWorkStates";
    }

    public String getLogonId() {
        return logonId;
    }

    public void setLogonId(String logonId) {
        this.logonId = logonId;
    }

    public String selectType() {
        workStates = workStatesManager.returnWorkStatesBylogonId(logonId);
        workStateSkillList=workStatesManager.returnWorkStateSkillByLogonId(logonId);
        return "selectType";
    }

    public String selectTypeCommit() {

        Map params = super.getParams();
        
        String groupHra2 = (String) params.get("groupHra2");
        String groupAudit2 = (String) params.get("groupAudit2");
        String groupRSC2 = (String) params.get("groupRSC2");
        //String groupTMC2 = (String) params.get("groupTMC2");

        workStates = new OrWorkStates();

        // workStates.setID();

        MyBeanUtil.copyProperties(workStates, params);
        workStatesManager.deleteWorkStateSkillByLogonId(workStates.getLogonId());
        if (workStates.getType() == WorkType.HRA) {
            workStates.setGroups(groupHra2.replace("'", ""));
            String[] groupHra = workStates.getGroups().split(",");
            List skillList=new ArrayList();
            for(int i=0;i<groupHra.length;i++){
            	WorkStatesSkill workStatesSkill=new WorkStatesSkill();
            	String clientType=conversionArray("clientType"+groupHra[i],"clientType_"+groupHra[i]);
            	workStatesSkill.setClientType(clientType);
            	String cooperator=conversionArray("cooperator"+groupHra[i],"cooperator_"+groupHra[i]);
            	workStatesSkill.setCooperator(cooperator);
            	String orderType=conversionArray("orderType"+groupHra[i],"orderType_"+groupHra[i]);
            	workStatesSkill.setOrderType(orderType);
            	String orderSource = conversionArray("orderSource"+groupHra[i],"orderSource_"+groupHra[i]);
            	workStatesSkill.setOrderSource(orderSource);
            	String orderArea=conversionArray("orderArea"+groupHra[i],"orderArea_"+groupHra[i]);
            	workStatesSkill.setOrderArea(orderArea);
            	workStatesSkill.setCreateTime(new Date());
            	workStatesSkill.setLogonId(workStates.getLogonId());
            	workStatesSkill.setCreator(getOnlineRoleUser().getLoginName());
            	workStatesSkill.setGroupId(Long.valueOf(groupHra[i]));
            	String count_str="count_"+groupHra[i];
            	String count=(String)params.get(count_str);
            	workStatesSkill.setCount(Integer.valueOf(count==null?"0":count));
            	skillList.add(workStatesSkill);
            }
            workStates.setRscCount(0);
            workStatesManager.saveWorkStateSkill(skillList,workStates.getLogonId());
        } else if (workStates.getType() == WorkType.AUDIT) {
            workStates.setGroups(groupAudit2.replace("'", ""));
            workStates.setRscCount(0);
        } else if (workStates.getType() == WorkType.RSC) {
            workStates.setGroups(groupRSC2.replace("'", ""));
        }
        /*
        else if (workStates.getType() == WorkType.TMC) {
            workStates.setGroups(groupTMC2.replace("'", ""));
        }*/

        if (0 == workStates.getID()) {
            workStates.setID(null);
        }

        workStatesManager.saveOrUpdate(workStates);

        user = getOnlineWorkStates();
        if (null != user && user.getLogonId().equals(workStates.getLogonId())) {
            putSession("onlineUser", workStates);
        }

        // workStatesListByHra=workStatesManager.lstWorkStatesByType(WorkType.HRA);//1:HRA
        // workStatesListByAudit=workStatesManager.lstWorkStatesByType(WorkType.AUDIT);//1:日审
        if(forwordStr.equals("selectTypeBatchCommit")){
        	return "selectTypeBatchCommit";
        }
        return "selectTypeCommit";

    }
    
    public String selectTypeBatchCommit(){
    	Map params = super.getParams();
        String groupHra2 = (String) params.get("groupHra2");
        String groupAudit2 = (String) params.get("groupAudit2");
        String groupRSC2 = (String) params.get("groupRSC2");
        String selIDs= (String)params.get("selIDs");
        String type=(String)params.get("type");
        selIDs=selIDs.replaceAll(",","','");
       
        List list = workStatesManager.lstWorkStatesByLogonId(selIDs);
        for(Iterator i = list.iterator();i.hasNext();){
        	OrWorkStates workStates=(OrWorkStates)i.next();
        	workStatesManager.deleteWorkStateSkillByLogonId(workStates.getLogonId());
        	if(Integer.valueOf(type) == WorkType.HRA){
        		workStates.setType(Integer.valueOf(type));
        		workStates.setGroups(groupHra2.replace("'", ""));
                String[] groupHra = workStates.getGroups().split(",");
                List skillList=new ArrayList();
                for(int j=0;j<groupHra.length;j++){
                	WorkStatesSkill workStatesSkill=new WorkStatesSkill();
                	String clientType=conversionArray("clientType"+groupHra[j],"clientType_"+groupHra[j]);
                	workStatesSkill.setClientType(clientType);
                	String cooperator=conversionArray("cooperator"+groupHra[j],"cooperator_"+groupHra[j]);
                	workStatesSkill.setCooperator(cooperator);
                	String orderType=conversionArray("orderType"+groupHra[j],"orderType_"+groupHra[j]);
                	workStatesSkill.setOrderType(orderType);
                	String orderSource=conversionArray("orderSource"+groupHra[j],"orderSource_"+groupHra[j]);
                	workStatesSkill.setOrderSource(orderSource);
                	String orderArea=conversionArray("orderArea"+groupHra[j],"orderArea_"+groupHra[j]);
                	workStatesSkill.setOrderArea(orderArea);
                	workStatesSkill.setCreateTime(new Date());
                	workStatesSkill.setLogonId(workStates.getLogonId());
                	workStatesSkill.setCreator(getOnlineRoleUser().getLoginName());
                	workStatesSkill.setGroupId(Long.valueOf(groupHra[j]));
                	String count_str="count_"+groupHra[j];
                	String count=(String)params.get(count_str);
                	workStatesSkill.setCount(Integer.valueOf(count==null?"0":count));
                	skillList.add(workStatesSkill);
                }
                workStates.setRscCount(0);
                workStatesManager.saveWorkStateSkill(skillList,workStates.getLogonId());
        	}else if (Integer.valueOf(type)== WorkType.AUDIT) {
        		workStates.setType(Integer.valueOf(type));
        		workStates.setRscCount(0);
                workStates.setGroups(groupAudit2.replace("'", ""));
            } else if (Integer.valueOf(type) == WorkType.RSC) {
            	workStates.setType(Integer.valueOf(type));
                workStates.setGroups(groupRSC2.replace("'", ""));
            }
        	workStatesManager.saveOrUpdate(workStates);
        }
    	return "selectTypeBatchCommit";
    }
    
    private String conversionArray(String hidParam,String param){
    	Map params = super.getParams();
    	String hidStr=(String)params.get(hidParam);
    	if((null!=hidStr)&&("all".equals(hidStr))){
    		return hidStr;
    	}else{
	    	Object o=(Object)params.get(param);
	    	if(null!=o){
		    	if(o instanceof String){
		    		return (String)o;
		    	}else{
			    	String [] array=(String[])o;
			    	String str="";
			    	for(int i=0;i<array.length;i++){
			    		str=str+array[i];
			    		if(i!=array.length-1){
			    			str+=",";
			    		}
			    	}
			    	return str;
		    	}
	    	}
    	}
    	return "";
    }

    public String openStates() {

        if (StringUtil.isValidStr(logonId) && !logonId.equals("undefined")) {
            workStates = workStatesManager.returnWorkStatesBylogonId(logonId);
            workStates.setState(1);
            workStatesManager.saveOrUpdate(workStates);
        } else {
            workStates = getOnlineWorkStates();
            workStates.setState(1);
            workStatesManager.saveOrUpdate(workStates);
        }
        //如果是中台人员打开档案，则新增一条当天工作记录
        if(workStates.getType() == WorkType.HRA){
        	midOrderTransfer.createMyWorkingRateToday(workStates);
        }
        workStates = getOnlineWorkStates();
        /**
         * workStatesListByHra=workStatesManager.lstWorkStatesByType(WorkType.HRA);//1:HRA
         * workStatesListByAudit=workStatesManager.lstWorkStatesByType(WorkType.AUDIT);//1:日审
         * 
         * return setMyWorkStates();
         **/
        return setMyWorkStates();
    }

    public String closeStates() {

        if (StringUtil.isValidStr(logonId) && !logonId.equals("undefined")) {
            workStates = workStatesManager.returnWorkStatesBylogonId(logonId);
            workStates.setState(0);
            workStatesManager.saveOrUpdate(workStates);
            workStates = getOnlineWorkStates();
        } else {
            workStates = getOnlineWorkStates();
            workStates.setState(0);
            workStatesManager.saveOrUpdate(workStates);
        }
        /** addby juesuchen 房控退出工作档案时,清除里面的酒店 2010-1-13 begin **/
        roleUser = getCurrentUser();
        if(null != roleUser && roleUser.isRSCUser())
        	newRoomControlService.clearUpHotelScheduleInMyWorkSpace(roleUser);
        /** addby juesuchen 房控退出工作档案时,清除里面的酒店 2010-1-13 end **/
        /**
         * workStatesListByHra=workStatesManager.lstWorkStatesByType(WorkType.HRA);//1:HRA
         * workStatesListByAudit=workStatesManager.lstWorkStatesByType(WorkType.AUDIT);//1:日审
         * 
         * return "closeStates";
         **/
        return setMyWorkStates();
    }

    /**
     * 删除操作员
     * 
     * @return
     */
    public String batchDel() {
        roleUser = getOnlineRoleUser();
        workStates = getOnlineWorkStates();

        String selIDs = (String) getParams().get("selIDs");
        String[] ids = selIDs.split(",");
        workStatesManager.batchDel(ids);
        /**
         * workStatesListByHra=workStatesManager.lstWorkStatesByType(WorkType.HRA);//1:HRA
         * workStatesListByAudit=workStatesManager.lstWorkStatesByType(WorkType.AUDIT);//1:日审
         * 
         * return "delStates";
         **/
        return setMyWorkStates();
    }
    
    public String batchUpdate(){
    	 roleUser = getOnlineRoleUser();
         workStates = getOnlineWorkStates();

         String selIDs = (String) getParams().get("selIDs");
    	return "chnBatchGroup";
    }

    /** add by chenjiajie 2008-10-21 Begin **/
    /**
     * 按条件查询hra,日审工作状态
     * 
     * @return
     */
    public String setWorkStatesDynamic() {
        Map params = super.getParams();
        int noAssignTo = 0;
        if (null != params.get("noAssignTo") && !"".equals(params.get("noAssignTo"))) {
            noAssignTo = Integer.parseInt((String) params.get("noAssignTo"));
        }
        roleUser = getOnlineRoleUser();
        workStates = getOnlineWorkStates();
        workStatesQueryBean.setGroups(workStatesQueryBean.getGroups().replace("'", ""));
        workStatesListByHra = workStatesManager.lstWorkStatesDynamic(workStatesQueryBean,
            noAssignTo);// 按条件查询
        setDisplayTag(1);
        return "setMyWorkStates";
    }

    /** add by chenjiajie 2008-10-21 End **/

    /** getter and setter begin */

    public OrWorkStates getWorkStates() {
        log.info("getWorkStates");
        return workStates;
    }

    public void setWorkStates(OrWorkStates workStates) {
        this.workStates = workStates;
    }

    public List getWorkStatesListByAudit() {
        return workStatesListByAudit;
    }

    public void setWorkStatesListByAudit(List workStatesListByAudit) {
        this.workStatesListByAudit = workStatesListByAudit;
    }

    public List getWorkStatesListByHra() {
        return workStatesListByHra;
    }

    public void setWorkStatesListByHra(List workStatesListByHra) {
        this.workStatesListByHra = workStatesListByHra;
    }

    public int getDisplayTag() {
        return displayTag;
    }

    public void setDisplayTag(int displayTag) {
        this.displayTag = displayTag;
    }

    public OrWorkStates getWorkStatesQueryBean() {
        return workStatesQueryBean;
    }

    public void setWorkStatesQueryBean(OrWorkStates workStatesQueryBean) {
        this.workStatesQueryBean = workStatesQueryBean;
    }

	public NewRoomControlService getNewRoomControlService() {
		return newRoomControlService;
	}

	public void setNewRoomControlService(NewRoomControlService newRoomControlService) {
		this.newRoomControlService = newRoomControlService;
	}

	public List getWorkStateSkillList() {
		return workStateSkillList;
	}

	public void setWorkStateSkillList(List workStateSkillList) {
		this.workStateSkillList = workStateSkillList;
	}

	public String getForwordStr() {
		return forwordStr;
	}

	public void setForwordStr(String forwordStr) {
		this.forwordStr = forwordStr;
	}

	

    /** getter and setter end */
}
