package com.mangocity.hotel.base.manage.impl;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.base.dao.HtlProjectCodeDao;
import com.mangocity.hotel.base.manage.HtlProjectCodeManage;
import com.mangocity.hotel.base.persistence.HtlProjectCode;

public class HtlProjectCodeManageImpl implements HtlProjectCodeManage {
	
	private HtlProjectCodeDao htlProjectCodeDao;
	
	public void saveHtlProjectCode(Cookie[] cookies,String orderCD) {
		if(htlProjectCodeDao.haveProjectCode(orderCD)){
			return;
		}
		HtlProjectCode htlProjectCode = new HtlProjectCode();
		htlProjectCode.setOrderCD(orderCD);
		 for (int i = 0; i < cookies.length; i++) {
             Cookie cookie = cookies[i];
             String value = cookie.getValue();
             String name = cookie.getName();
             if (("projectcode").equals(name)) {
            	 htlProjectCode.setProjectCode(value);
             }else if (("exprojectcode1").equals(name)){
            	 htlProjectCode.setExProjectCode1(value);
             }else if(("exprojectcode2").equals(name)){
            	 htlProjectCode.setExProjectCode2(value);
             }else if("zjcode".equals(name)){
            	 htlProjectCode.setZjCode(value);
             }
         }
		 boolean flag_save = null != htlProjectCode.getProjectCode()          //渠道号，比如去哪儿
		                  || null != htlProjectCode.getExProjectCode1()       //TODO
		                  || null != htlProjectCode.getExProjectCode2()       //TODO
		                  || null != htlProjectCode.getZjCode();        // 专辑号
		 if(flag_save){
		     htlProjectCodeDao.saveHtlProjectCode(htlProjectCode);
		 }
	}
	public HtlProjectCodeDao getHtlProjectCodeDao() {
		return htlProjectCodeDao;
	}
	public void setHtlProjectCodeDao(HtlProjectCodeDao htlProjectCodeDao) {
		this.htlProjectCodeDao = htlProjectCodeDao;
	}
}
