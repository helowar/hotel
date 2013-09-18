package com.mangocity.hotel.util;

import com.mango.hotel.ebooking.service.IAdjustRoomStateService;
import com.mango.hotel.ebooking.service.ILeavewordAnnalAndRightService;
import com.mango.hotel.ebooking.service.IPriceRedressalService;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 * 主要是用来更新菜单页面上提示的数额
 * @author chenjuesu
 *
 */
public class GetAlertCounts extends GenericAction{
	/**
     * 配额业务接口
     */
	private IQuotaManage quotaManage;
	/**
     * 留言板操作业务接口
     */
	private ILeavewordAnnalAndRightService leaAndRightService;
	/**
     * EBooking房态审核操作业务接口
     */
	private IAdjustRoomStateService adjustRoomStateService;
	
	/**
     * EBooking调价审核操作业务接口
     */
    private IPriceRedressalService priceRedressalService;
	/**
	 * 酒店预警数
	 */
	private int quotaCount;
	/**
	 * 未读留言数
	 */
	private int MsgCount;
	/**
	 * 价格审核数
	 */
	private int priceCount;
	/**
	 * 房态审核数
	 */
	private int roomStateCount;
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		roleUser = getOnlineRoleUser();
		quotaCount = quotaManage.countHotelQuotaForwarnNum().intValue();
        String theAreaLoginerCanCheck = "NONE";
        if(null != roleUser)
        	theAreaLoginerCanCheck = leaAndRightService.getHotelAreaByLoginName(roleUser.getLoginName());
		MsgCount = leaAndRightService.getUnReadMsgCount(theAreaLoginerCanCheck);
		roomStateCount = adjustRoomStateService.getUnAuditRoomstate(theAreaLoginerCanCheck);
		priceCount = priceRedressalService.getUnAuditPrice(theAreaLoginerCanCheck);
		return super.execute();
	}

	public int getMsgCount() {
		return MsgCount;
	}

	public void setMsgCount(int msgCount) {
		MsgCount = msgCount;
	}

	public int getPriceCount() {
		return priceCount;
	}

	public void setPriceCount(int priceCount) {
		this.priceCount = priceCount;
	}

	public int getQuotaCount() {
		return quotaCount;
	}

	public void setQuotaCount(int quotaCount) {
		this.quotaCount = quotaCount;
	}

	public IQuotaManage getQuotaManage() {
		return quotaManage;
	}

	public void setQuotaManage(IQuotaManage quotaManage) {
		this.quotaManage = quotaManage;
	}

	public int getRoomStateCount() {
		return roomStateCount;
	}

	public void setRoomStateCount(int roomStateCount) {
		this.roomStateCount = roomStateCount;
	}

	public IAdjustRoomStateService getAdjustRoomStateService() {
		return adjustRoomStateService;
	}

	public void setAdjustRoomStateService(
			IAdjustRoomStateService adjustRoomStateService) {
		this.adjustRoomStateService = adjustRoomStateService;
	}

	public ILeavewordAnnalAndRightService getLeaAndRightService() {
		return leaAndRightService;
	}

	public void setLeaAndRightService(
			ILeavewordAnnalAndRightService leaAndRightService) {
		this.leaAndRightService = leaAndRightService;
	}

	public IPriceRedressalService getPriceRedressalService() {
		return priceRedressalService;
	}

	public void setPriceRedressalService(
			IPriceRedressalService priceRedressalService) {
		this.priceRedressalService = priceRedressalService;
	}

}
