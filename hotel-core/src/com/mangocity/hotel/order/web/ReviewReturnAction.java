package com.mangocity.hotel.order.web;

import java.util.List;

import com.mangocity.hotel.base.service.assistant.URLClient;
import com.mangocity.hotel.order.constant.DailyAuditConstant;
import com.mangocity.hotel.order.persistence.DaAuditingWorkload;
import com.mangocity.hotel.order.persistence.DaDailyauditItem;
import com.mangocity.hotel.order.persistence.DaReturnvisit;
import com.mangocity.hotel.order.service.IDailyAuditService;
import com.mangocity.hotel.order.service.assistant.ReturnInfo;
import com.mangocity.hotel.user.UserWrapper;

public class ReviewReturnAction extends GenericCCAction {

	private static final long serialVersionUID = 1L;
	
	public IDailyAuditService dailyAuditService;
	
	/* 回访ID */
	private String returnVisitID;
	/* 回访数据 */
	private DaReturnvisit returnvisit;
	/* 回访数据 */
	public List<ReturnInfo> returnDateList;
	/* 总的工作量情况(包括:今天需审核/历史未审核/今天已完成)*/
	private List<DaAuditingWorkload> auditWorkload;
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
	 * 初始化查询页面
	 * @return
	 */
	public String initQuery(){
//		 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
		auditWorkload = (List<DaAuditingWorkload>)dailyAuditService.getWorkloadByType(DailyAuditConstant.RETURNVISIT_TYPE);
		return "initQuery";
	}
	
	
	/**
	 * 初使化查看页面
	 * @return
	 */
	public String openReview(){
		//回访信息
		returnvisit = dailyAuditService.getReturnvisit(Long.valueOf(returnVisitID).longValue());
		//日审明细
		returnDateList = dailyAuditService.getReturnData(Long.valueOf(returnVisitID).longValue());
//		 TMC订单记录查询
        HOP_TMC_ORDER = URLClient.HOP_TMC_ORDER;
        if (1 == returnvisit.getReturnstate() || 1 == returnvisit.getAquirestate())
			request.setAttribute("auditState", 1);
		else
			request.setAttribute("auditState", 0);
		String memberCd = null;
		for(ReturnInfo returnInfo : returnDateList){
			List<DaDailyauditItem> list = returnInfo.getReturnDateList();
			for(DaDailyauditItem item : list ){
				memberCd = item.getMemberCd();
				if(memberCd != null && !"".equals(memberCd)){
					break; 
				}
			}
		}
		//会员信息
		if(memberCd != null) {
			member = getMemberSimpleInfoByMemberCd(memberCd, false);
		}else {
			member = getMemberSimpleInfoByMemberCd("", false);
		}
		return SUCCESS;
	}
	/**
	 * 手动获取回访记录到我的工作档案
	 * @return
	 */
	public String achieveRetrunForWorkspace(){
//		判断此用户是否日审人员
        if(!checkAuditUser()){
    		return SUCCESS;
        }
		//当前登录信息
		UserWrapper user = super.getOnlineRoleUser();
		
		boolean achiereRetrun = dailyAuditService.achieveRetrunForUser(user.getLoginName(), user.getName(),returnVisitID);
		
		
		return SUCCESS;
	}
	
	
/** ===get set=== */
	public String getReturnVisitID() {
		return returnVisitID;
	}
	public void setReturnVisitID(String returnVisitID) {
		this.returnVisitID = returnVisitID;
	}
	public IDailyAuditService getDailyAuditService() {
		return dailyAuditService;
	}
	public void setDailyAuditService(IDailyAuditService dailyAuditService) {
		this.dailyAuditService = dailyAuditService;
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
	public List<DaAuditingWorkload> getAuditWorkload() {
		return auditWorkload;
	}
	public void setAuditWorkload(List<DaAuditingWorkload> auditWorkload) {
		this.auditWorkload = auditWorkload;
	}
}
