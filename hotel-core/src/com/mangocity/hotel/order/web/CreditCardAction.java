package com.mangocity.hotel.order.web;

import com.mangocity.framework.creditcard.SidGenerator;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.WarningContant;


/**
 * 
 * @author alfred 
 *
 */
public class CreditCardAction extends OrderAction {

	private static final long serialVersionUID = 7387855879548843772L;
	
	/*
	 * 会员编号
	 */
	private String memberCd;
	
	/*
	 * 交易类型
	 */
	private String transType;
	
	/*
	 * 安全标示
	 */
	private String sid;
	
	/*
	 * 操作人
	 */
	private String operator;
	
	
	/**
	 * 卡中配置
	 */
	private String cardTypeConfig="6/7/8/9/10/11/12/13/16/17/18/19/20/21/22/23/26";
	private SystemDataService systemDataService;
	

	/**
	 * 
	 */
	public String execute() {
		
		//设置财务安全标示sid
		try {
			OrParam orParam=systemDataService.getSysParamByName(BaseConstant.HOP_CREDIT_CONFIG);
			// 将从数据库中获取的信用卡参数赋值给cardTypeConfig
			cardTypeConfig=orParam.getValue();
			String tmpSid = SidGenerator.generate("H1");
			if(tmpSid != null && !"".equals(tmpSid)) {
				this.setSid(tmpSid);
			}else {
				return super.forwardError(WarningContant.OBTAIN_SID_ERROR);
			}
		}catch(Exception e) {
			log.error("ErrorMSG-MPM10000:occured a exception while attempt to obtain a sid",e);
			return super.forwardError(WarningContant.OBTAIN_SID_ERROR);
		}
		
		//获取登录用户，设置操作人
		try {
			roleUser = getOnlineRoleUser();
			if(roleUser != null) {
				this.setOperator(roleUser.getLoginName());
			}else {
				return super.forwardError(WarningContant.ROLEUSER_NOT_LOGIN);
			}
		}catch(Exception e) {
			return super.forwardError(WarningContant.ROLEUSER_OBTAIN_ERROR);
		}
			
		
        return SUCCESS;
    }
	

	public String getMemberCd() {
		return memberCd;
	}


	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}


	public String getTransType() {
		return transType;
	}


	public void setTransType(String transType) {
		this.transType = transType;
	}


	public String getSid() {
		return sid;
	}


	public void setSid(String sid) {
		this.sid = sid;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}


	public String getCardTypeConfig() {
		return cardTypeConfig;
	}


	public void setCardTypeConfig(String cardTypeConfig) {
		this.cardTypeConfig = cardTypeConfig;
	}


	public SystemDataService getSystemDataService() {
		return systemDataService;
	}


	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}
	
	
    
}
