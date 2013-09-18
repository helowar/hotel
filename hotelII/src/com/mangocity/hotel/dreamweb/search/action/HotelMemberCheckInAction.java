package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;

import com.mangocity.hotel.dreamweb.search.dao.HotelQueryAjaxDao;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionSupport;

public class HotelMemberCheckInAction extends ActionSupport{
	private List<Object[]> memberList;
	private HotelQueryAjaxDao hotelQueryAjaxDao;
	private String memberId;
	private static final MyLog log = MyLog.getLogger(HotelMemberCheckInAction.class);
	public String execute(){
		try{
		    memberList=hotelQueryAjaxDao.findMemberHotelCheckOut(memberId);
		}catch(Exception e){
			log.error("会员登录错误",e);
		}
		return SUCCESS;
	}
	public HotelQueryAjaxDao getHotelQueryAjaxDao() {
		return hotelQueryAjaxDao;
	}
	public void setHotelQueryAjaxDao(HotelQueryAjaxDao hotelQueryAjaxDao) {
		this.hotelQueryAjaxDao = hotelQueryAjaxDao;
	}
	public List<Object[]> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<Object[]> memberList) {
		this.memberList = memberList;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}



}
