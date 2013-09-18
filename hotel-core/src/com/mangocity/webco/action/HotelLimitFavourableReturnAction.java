/**
 * 提供给电商部门查看活动的状态
 */
package com.mangocity.webco.action;

import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;

/**
 * @author xiongxiaojun
 *
 */
public class HotelLimitFavourableReturnAction {
	private long favId;
	private boolean favStatus;
	private HtlLimitFavourableManage limitFavourableManage;
	
    public String judgeFavStart(){
    	setFavStatus(limitFavourableManage.judgeFavStart(favId));
    	return "result";
    }
	public long getFavId() {
		return favId;
	}
	public void setFavId(long favId) {
		this.favId = favId;
	}
	public boolean isFavStatus() {
		return favStatus;
	}
	public void setFavStatus(boolean favStatus) {
		this.favStatus = favStatus;
	}
	public HtlLimitFavourableManage getLimitFavourableManage() {
		return limitFavourableManage;
	}
	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}
}
