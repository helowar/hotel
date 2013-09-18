package com.mangocity.hotel.base.web;


import com.mangocity.hweb.manage.HotelSynManage;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author tuzihui
 * 按酒店Id同步酒店信息到读表
 */
public class HotelSynAction  extends ActionSupport {
	
	private static final long serialVersionUID = -1136703646232453139L;
	
	private String hotelId;
	
	private HotelSynManage hotelSynManage;
	
	private static final MyLog log = MyLog.getLogger(HotelSynAction.class);
	
	public String execute(){
		try{
			if(null != hotelId){						
				hotelSynManage.hotelSynByHotelId(hotelId);	
			}
		}catch(Exception e){
			log.error("hotelinfo syn error",e);
		}
	   return null ;
	}



	public String getHotelId() {
		return hotelId;
	}



	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}



	public HotelSynManage getHotelSynManage() {
		return hotelSynManage;
	}



	public void setHotelSynManage(HotelSynManage hotelSynManage) {
		this.hotelSynManage = hotelSynManage;
	}

}
