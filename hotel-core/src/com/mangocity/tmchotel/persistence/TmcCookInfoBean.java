package com.mangocity.tmchotel.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 
 * Cookie 中的相关信息  TMC-V2.0
 * add by shengwei.zuo 
 */

public class TmcCookInfoBean {
	
	//是否是公共会员 1:是；0：否
	private Integer  hasComCompUer;
	
	//是否已授权，可以使用公司的余额支付 
	private boolean  hasUseCompany;
	
	//公司会员CD 
	private String companyNo;

	public Integer getHasComCompUer() {
		return hasComCompUer;
	}

	public void setHasComCompUer(Integer hasComCompUer) {
		this.hasComCompUer = hasComCompUer;
	}

	public boolean isHasUseCompany() {
		return hasUseCompany;
	}

	public void setHasUseCompany(boolean hasUseCompany) {
		this.hasUseCompany = hasUseCompany;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

}