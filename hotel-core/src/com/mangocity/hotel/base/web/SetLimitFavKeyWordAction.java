package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.service.LimitFavKeyWordService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

@SuppressWarnings("serial")
public class SetLimitFavKeyWordAction extends PersistenceAction {
	private LimitFavKeyWordService limitFavKeyWordService;
	private String hotelId, fav_a,fav_b,fav_c;
	private String hotelIdStr,batch_a,batch_b,batch_c;
	  //批量设置
    public String batchSetting(){
   	if(null==super.getOnlineRoleUser()||null==super.getOnlineRoleUser().getLoginName()){
    		return  super.forwardError("获取登陆用户信息失效,请重新登陆");
    	}
   	String loginName=super.getOnlineRoleUser().getLoginName();
   	limitFavKeyWordService.batchSettingKeyWord(hotelIdStr, loginName, batch_a, batch_b, batch_c);
     return SUCCESS;
    }
    
	  //批量取消
    public String batchCancel(){
   	if(null==super.getOnlineRoleUser()||null==super.getOnlineRoleUser().getLoginName()){
    		return  super.forwardError("获取登陆用户信息失效,请重新登陆");
    	}
   	String loginName=super.getOnlineRoleUser().getLoginName();
   	limitFavKeyWordService.batchCancelKeyWord(hotelIdStr, loginName, batch_a, batch_b, batch_c);
     return SUCCESS;
    }
    
    //单个酒店设置活动标签
    public String singleSetting() {	
        if(null==super.getOnlineRoleUser()||null==super.getOnlineRoleUser().getLoginName()){
    		return  super.forwardError("获取登陆用户信息失效,请重新登陆");
    	}
     	String loginName=super.getOnlineRoleUser().getLoginName();
     	limitFavKeyWordService.singleSettingKeyWord(hotelId, loginName, fav_a, fav_b, fav_c);
    	return SUCCESS;
    }
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getFav_a() {
		return fav_a;
	}
	public void setFav_a(String favA) {
		fav_a = favA;
	}
	public String getFav_b() {
		return fav_b;
	}
	public void setFav_b(String favB) {
		fav_b = favB;
	}
	public String getFav_c() {
		return fav_c;
	}
	public void setFav_c(String favC) {
		fav_c = favC;
	}
	public LimitFavKeyWordService getLimitFavKeyWordService() {
		return limitFavKeyWordService;
	}
	public void setLimitFavKeyWordService(
			LimitFavKeyWordService limitFavKeyWordService) {
		this.limitFavKeyWordService = limitFavKeyWordService;
	}

	public String getHotelIdStr() {
		return hotelIdStr;
	}

	public void setHotelIdStr(String hotelIdStr) {
		this.hotelIdStr = hotelIdStr;
	}

	public String getBatch_a() {
		return batch_a;
	}

	public void setBatch_a(String batchA) {
		batch_a = batchA;
	}

	public String getBatch_b() {
		return batch_b;
	}

	public void setBatch_b(String batchB) {
		batch_b = batchB;
	}

	public String getBatch_c() {
		return batch_c;
	}

	public void setBatch_c(String batchC) {
		batch_c = batchC;
	}


}
