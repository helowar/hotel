package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.B2BIncreaseManage;
import com.mangocity.hotel.base.persistence.B2BIncrease;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

public class B2BIncreaseAction extends PersistenceAction{
	private B2BIncreaseManage b2BIncreaseManage; 
	private List<B2BIncrease> b2BIncreases;
	private String chn_name;
	
	
	/**
	 * B2B加幅设置
	 * @return
	 */
	public String batchB2BIncrease(){
		Map params=super.getParams();
		String hotelIDs=params.get("hotelIDs")+"";
		if(hotelIDs.length() == 0) return SUCCESS; //此处理论上不会存在没有酒店ID的情况
		List<Long> hotelIdList = new ArrayList<Long>();
		
		for(String str:hotelIDs.split(",")){ //得到需要加幅酒店的ID
			hotelIdList.add(Long.parseLong(str));
		}
		System.out.println(params.get("increase"));
		double increaseRate =Double.parseDouble(params.get("increase")+"");
		
		String createName;
		if (null != super.getOnlineRoleUser()) { //获取修改加幅记录的登录用户
			createName = super.getOnlineRoleUser().getName();
               if (super.getOnlineRoleUser().getName().equals("")) {
            	   createName = super.getBackUserName();
               }
           } else {
        	   createName = super.getBackUserName();
        }
		b2BIncreaseManage.saveB2BIncrease(hotelIdList, increaseRate, createName);
		return super.SUCCESS;
	}

	public B2BIncreaseManage getB2BIncreaseManage() {
		return b2BIncreaseManage;
	}

	public void setB2BIncreaseManage(B2BIncreaseManage increaseManage) {
		b2BIncreaseManage = increaseManage;
	}

	public List<B2BIncrease> getB2BIncreases() {
		return b2BIncreases;
	}

	public void setB2BIncreases(List<B2BIncrease> increases) {
		b2BIncreases = increases;
	}

	public String getChn_name() {
		return chn_name;
	}

	public void setChn_name(String chn_name) {
		this.chn_name = chn_name;
	}
	
	
}
