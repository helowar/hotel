package com.mangocity.hotel.order.web;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.OrWorkStatesManager;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.hotel.order.persistence.OrOrderStatistics;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 *设置我的工作状态
 * 
 * @author HWL
 * 
 */
public class UncontractOrderOperateAction extends OrderAction {

    private static final long serialVersionUID = -8785157932094025443L;

//    private OrWorkStatesManager workStatesManager;

    protected List workStatesListByHra;

    protected List workStatesListByAudit;

    protected OrWorkStates workStates;

    private String mostStar;

    private String mostPriceLevel;
    
    private String cusId;

    public String pre() {
    	 /**qc 653 没有传cusId到跳转的Action add by xiaowei.wang  begin */
    	Map params = super.getParams();
    	if(null != cusId && null == params.get("cusId")){
    		params.put("cusId", cusId);
    	}
    	 /**qc 653 没有传cusId到跳转的Action add by xiaowei.wang  end */
    	try {
            if (!handleMemberLogin()) {
                return this.forwardError("获取会员信息出错！");
            }
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return this.forwardError("会员不存在！");
        }
        
        member = getOnlineMember();

        if (null != member) {
            OrOrderStatistics orderStat = orderService.getOrderStatByMemberCd(member.getMembercd());
            request.setAttribute("orderStat", orderStat);
            if (null != orderStat) {
                double price = orderStat.getAvgPrice();
                double star = orderStat.getAvgStar();
                mostStar = OrderUtil.getMostStar(star);
                request.setAttribute("mostStar", mostStar);
                mostPriceLevel = OrderUtil.getPriceLevel(price);
                request.setAttribute("mostPriceLevel", mostPriceLevel);
            }
        }
        return "pre";
    }

    public String selectTypeCommit() {

        Map params = super.getParams();

        String groupHra2 = (String) params.get("groupHra2");
        String groupAudit2 = (String) params.get("groupAudit2");

        workStates = new OrWorkStates();

        // workStates.setID();

        MyBeanUtil.copyProperties(workStates, params);

        if (1 == workStates.getType()) {
            workStates.setGroups(groupHra2.replace("'", ""));
        } else if (2 == workStates.getType()) {
            workStates.setGroups(groupAudit2.replace("'", ""));
        }

        if (0 == workStates.getID()) {
            workStates.setID(null);
        }

        workStatesManager.saveOrUpdate(workStates);

        workStatesListByHra = workStatesManager.lstWorkStatesByType(1);// 1:HRA
        workStatesListByAudit = workStatesManager.lstWorkStatesByType(2);// 1:日审
        return "selectTypeCommit";

    }

    public String openStates() {

        workStates = (OrWorkStates) getFromSession("workStates");
        workStates.setState(1);
        workStatesManager.saveOrUpdate(workStates);

        workStatesListByHra = workStatesManager.lstWorkStatesByType(1);// 1:HRA
        workStatesListByAudit = workStatesManager.lstWorkStatesByType(2);// 1:日审

        return "openStates";

    }

    public String closeStates() {
        workStates = (OrWorkStates) getFromSession("workStates");
        workStates.setState(2);
        workStatesManager.saveOrUpdate(workStates);

        workStatesListByHra = workStatesManager.lstWorkStatesByType(1);// 1:HRA
        workStatesListByAudit = workStatesManager.lstWorkStatesByType(2);// 1:日审

        return "closeStates";

    }

    public OrWorkStates getWorkStates() {
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

    public OrWorkStatesManager getWorkStatesManager() {
        return workStatesManager;
    }

    public void setWorkStatesManager(OrWorkStatesManager workStatesManager) {
        this.workStatesManager = workStatesManager;
    }

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
    
}
