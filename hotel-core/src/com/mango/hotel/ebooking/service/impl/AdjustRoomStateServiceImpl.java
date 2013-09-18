package com.mango.hotel.ebooking.service.impl;

import java.util.Date;
import java.util.List;

import com.mango.hotel.ebooking.persistence.HtlEbookingRoomstate;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.service.IAdjustRoomStateService;
import com.mango.hotel.ebooking.service.ILeavewordAnnalAndRightService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAO;

@SuppressWarnings("all")
public class AdjustRoomStateServiceImpl implements IAdjustRoomStateService {

	private DAO entityManager;
	/**
     * EBooking留言板和酒店区域查看权限业务接口
     */
    private ILeavewordAnnalAndRightService leaAndRightService;
	
	public DAO getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(DAO entityManager) {
		this.entityManager = entityManager;
	}

	public void updateRoomState(String adjustBeanIds,UserWrapper roleUser,int auditResult) {
		// TODO Auto-generated method stub
		String[] ids = adjustBeanIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			HtlEbookingRoomstate roomStateBean = (HtlEbookingRoomstate)entityManager.find(HtlEbookingRoomstate.class, Long.valueOf(ids[i]));
			roomStateBean.setAuditorid(roleUser.getLoginName());
			roomStateBean.setAuditorname(roleUser.getName());
			roomStateBean.setAuditortime(new Date());
			roomStateBean.setAuditresult(auditResult);
			roomStateBean.setAuditstate(2);
			entityManager.update(roomStateBean);
			if(1 == auditResult){
				//如果通过,则更新房态
				int v_thisBedId = roomStateBean.getBedid();
				int p_roomState = roomStateBean.getRoomsate();
				String hsql = "update HtlRoom r set r.roomState = REGEXP_REPLACE(r.roomState,'"
						+ v_thisBedId
						+ ":[0-9]','"
						+ v_thisBedId
						+ ":"
						+ p_roomState
						+ "')  where r.hotelId = "
						+ roomStateBean.getHotelid()
						+ " and r.roomTypeId = "
						+ roomStateBean.getRoomtypeid()
						+ " and to_char(r.ableSaleDate, 'yyyy-MM-dd') in "
						+ roomStateBean.getSetdates();
				entityManager.doUpdateBatch(hsql, null);
				//留言实体
		    	LeavewordAnnalBean leaveAnnal = new LeavewordAnnalBean();
		    	leaveAnnal.setTopic(DateUtil.dateToString(roomStateBean.getOperationertime())+"满房修改,已通过审核。");
		    	leaveAnnal.setAddresser(LeavewordAnnalBean.MANGOCITY);
		    	leaveAnnal.setAddressee(String.valueOf(roomStateBean.getHotelid()));
		    	leaveAnnal.setOperationdate(new Date());
		    	leaveAnnal.setOperationer(roleUser.getName());
		    	leaveAnnal.setOperationerID(roleUser.getLoginName());
		    	leaveAnnal.setLeaveType(1L);//留言类型为1房态
		    	leaveAnnal.setContent(roomStateBean.getRoombedname()+"\n日期：\n"+roomStateBean.getSetdates());
		    	//保存留言
		        leaAndRightService.saveLeavewordAnnal(leaveAnnal);
			}else{
				//留言实体
				LeavewordAnnalBean leaveAnnal = new LeavewordAnnalBean();
				leaveAnnal.setTopic(DateUtil.dateToString(roomStateBean.getOperationertime())+"满房修改,未通过审核。");
				leaveAnnal.setAddresser(LeavewordAnnalBean.MANGOCITY);
				leaveAnnal.setAddressee(String.valueOf(roomStateBean.getHotelid()));
				leaveAnnal.setOperationdate(new Date());
				leaveAnnal.setOperationer(roleUser.getName());
				leaveAnnal.setOperationerID(roleUser.getLoginName());
				leaveAnnal.setLeaveType(1L);//留言类型为1房态
				leaveAnnal.setContent(roomStateBean.getRoombedname()+"\n日期：\n"+roomStateBean.getSetdates());
				//保存留言
				leaAndRightService.saveLeavewordAnnal(leaveAnnal);
			}
		}
	}

	public int getUnAuditRoomstate(String theAreaLoginerCanCheck) {
		// TODO Auto-generated method stub
		String hql = "select count(*) from HtlEbookingRoomstate r, HtlHotel h " +
			 "where r.hotelid = h.ID and r.hasneedaudit = 1 and r.auditstate = 0 ";
		if(!"ALL".equals(theAreaLoginerCanCheck)){
			hql += " and h.city in (select hr.cityCode from HtlArea hr where hr.areaCode = '"+theAreaLoginerCanCheck+"')";
		}
		List result = entityManager.doquery(hql, false);
		Long cont = (Long)result.get(0);
		return cont.intValue();
	}

	public ILeavewordAnnalAndRightService getLeaAndRightService() {
		return leaAndRightService;
	}

	public void setLeaAndRightService(
			ILeavewordAnnalAndRightService leaAndRightService) {
		this.leaAndRightService = leaAndRightService;
	}

}
