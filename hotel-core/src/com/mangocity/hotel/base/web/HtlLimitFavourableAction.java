package com.mangocity.hotel.base.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HtlFavourableHotelManage;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.HtlFavourableHotel;
import com.mangocity.hotel.base.persistence.HtlLimitFavourable;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

public class HtlLimitFavourableAction extends PersistenceAction{

	private HtlLimitFavourable htlLimitFavourable;
	
	private HtlLimitFavourableManage htlLimitFavourableManage;
	
	private HtlFavourableHotelManage htlFavourableHotelManage;
	
	private Long[] hotelID;
	
	private String status;
	
	private String json;
		
	private static final String ADDHOTEL = "ADDHOTEL";
	
	public String save(){
		Map params = super.getParams();
		
		 if (null == htlLimitFavourable) {
			 htlLimitFavourable = new HtlLimitFavourable();
	            MyBeanUtil.copyProperties(htlLimitFavourable, params);
	        }
		 UserWrapper userWrapper = super.getOnlineRoleUser();
		 String createName = "System";
		 if(null != userWrapper){
			 createName = userWrapper.getLoginName();
		 }
		 htlLimitFavourable.setCreateName(createName);
		 htlLimitFavourable.setCreateTime(DateUtil.getSystemDate());
		 htlLimitFavourable.setFavIsStart(0);
		 htlLimitFavourable.setFlag(1);
		 
		 htlLimitFavourableManage.save(htlLimitFavourable);
		 return SAVE_SUCCESS;
	}
	
	 public String addHotel() {
		    Map params = super.getParams();
			List<HtlFavourableHotel> htlFavourableHotels = new ArrayList<HtlFavourableHotel>();
			// 循环赋值
			Long favId = Long.valueOf(params.get("favId")+"");
			for (int i = 0; i < hotelID.length; i++) {
				HtlFavourableHotel htlFavourableHotel = new HtlFavourableHotel();
				htlFavourableHotel.setFavId(favId);
				htlFavourableHotel.setHtlFlag(1);
				htlFavourableHotel.setCreateTime(DateUtil.getSystemDate());
				htlFavourableHotel.setModifyTime(DateUtil.getSystemDate());
				htlFavourableHotel.setHotelId(hotelID[i]);
	
				htlFavourableHotels.add(htlFavourableHotel);
			}
	
			htlFavourableHotelManage.saveOrUpdateAll(htlFavourableHotels);
	
			return ADDHOTEL;
	}
	 /**
	  * 
	  * @return
	  */
	 public String viewFav(){
		 Map params=super.getParams();
		 Long favId = Long.valueOf((params.get("favId")+""));
		 //根据favId得到HtlLimitFavourable对象
		 htlLimitFavourable = htlLimitFavourableManage.queryLimitFav(favId);
		 return "viewFav";
	 }
	 
	 /**
	  * 修改返现活动
	  * @return
	  */
	 public String editFav(){
		 Map params = super.getParams();
		 if (null == htlLimitFavourable) {
			 htlLimitFavourable = new HtlLimitFavourable();
	            MyBeanUtil.copyProperties(htlLimitFavourable, params);
	        }
		 UserWrapper userWrapper = super.getOnlineRoleUser();
		 String modifyName = "System";
		 if(null != userWrapper){
			 modifyName = userWrapper.getLoginName();
		 }
		 htlLimitFavourable.setModifyName(modifyName);
		 htlLimitFavourable.setModifyTime(DateUtil.getSystemDate());
		 htlLimitFavourableManage.save(htlLimitFavourable);
		 return "editFav";
	 }
	 
	 /**
	  * 删除活动
	  * @return
	  */
	 public String deleteFav(){
		 Map params=super.getParams();
		 Long favId = Long.valueOf((params.get("favId")+""));
		 htlLimitFavourableManage.deleteFav(favId);
		 return "deleteFav";
	 }
	 
	 /**
	  * 删除活动下的酒店
	  * @return
	  */
	 public String deleteHotel(){
		 Map params=super.getParams();
		 Long favId = Long.valueOf((params.get("favId")+""));
		 Long htlID = Long.valueOf((params.get("htlID")+""));
		 if(null!=htlID && null!= favId){
			 htlFavourableHotelManage.delete(htlID,favId);
		 }
		 return "deleteHotel";
	 }
	 
	 /**
	  * 批量删除活动下的酒店
	  * @return
	  */
	 public String batchDeleteHotel(){
		 Map params=super.getParams();
		 String hotelIDs=params.get("hotelIDs")+"";
		 
		 Long favId = Long.valueOf((params.get("favId")+""));
		 
		 List<Long> hotelIdList = new ArrayList<Long>();
			for(String str:hotelIDs.split(",")){ //得到需要加幅酒店的ID
				hotelIdList.add(Long.parseLong(str));
			}
			
		 if(hotelIdList.size() != 0)
		 htlFavourableHotelManage.batchDelete(hotelIdList,favId);
		 return "batchDeleteHotel";
	 }
	 
	 /**
	  * 开始或者结束活动
	  * @return
	  */
//	 public String favActiviyBeginOrEnd()throws IOException{
//		 Map params=super.getParams();
//		 Long favId = Long.valueOf((params.get("favId")+""));
//		 if(null!=status){
//			 if("1".equals(status)){
//				 if(htlLimitFavourableManage.isHaveAnotherActivityBegin()){
//					 json = "{msg:'" +1+ "'}";    //构造JSON格式的字符串      
//					 sendMsg(json); 
//				 }else{
//					 htlLimitFavourableManage.favActiviyBeginOrEnd(favId, status);
//					 json = "{msg:'" +0+ "'}";    //构造JSON格式的字符串      
//					 sendMsg(json);
//				 }
//			 }else{
//				 htlLimitFavourableManage.favActiviyBeginOrEnd(favId, status);
//				 json = "{msg:'" +0+ "'}";    //构造JSON格式的字符串      
//				 sendMsg(json);
//			 }
//		 }
//		 return null;
//	 }
	 public String favActiviyBeginOrEnd(Long favId,String status)throws IOException{
		 String flag = "0";
		 if(null!=status){
			 if("1".equals(status)){
				 if(htlLimitFavourableManage.isHaveAnotherActivityBegin(favId)){
					  flag = "1";
				 }else{
					 htlLimitFavourableManage.favActiviyBeginOrEnd(favId, status);
				 }
			 }else{
				 htlLimitFavourableManage.favActiviyBeginOrEnd(favId, status);
			 }
		 }
		 return flag;
	 }
	 /**
	  * 查询间夜量 和 返现总金额
	  * @return
	  */
//	 public String getRoomNightAndReturnCash()throws IOException{
//		 Map params=super.getParams();
//		 Long favId = Long.valueOf((params.get("favId")+""));
//		 String roomnight_ReturnCash = htlLimitFavourableManage.getRoomNight_ReturnCash(favId);
//		 json = "{msg:'" + roomnight_ReturnCash + "'}";    //构造JSON格式的字符串      
//		 sendMsg(json); 
//		 return null;
//	 }
//	 
	 public String getRoomNightAndReturnCash(Long favId)throws IOException{
		 String roomnight_ReturnCash = htlLimitFavourableManage.getRoomNight_ReturnCash(favId);
		 //json = "{msg:'" + roomnight_ReturnCash + "'}";    //构造JSON格式的字符串      
		// sendMsg(json); 
		 return roomnight_ReturnCash;
	 }

	 
//	   public void sendMsg(String content) throws IOException{       
//		          HttpServletResponse response = ServletActionContext.getResponse();       
//		          response.setCharacterEncoding("UTF-8");       
//		          response.getWriter().write(content);       
//		      }  
	 

	public Long[] getHotelID() {
		return hotelID;
	}

	public void setHotelID(Long[] hotelID) {
		this.hotelID = hotelID;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public HtlLimitFavourable getHtlLimitFavourable() {
		return htlLimitFavourable;
	}
	
	public void setHtlLimitFavourable(HtlLimitFavourable htlLimitFavourable) {
		this.htlLimitFavourable = htlLimitFavourable;
	}
	
	public HtlLimitFavourableManage getHtlLimitFavourableManage() {
		return htlLimitFavourableManage;
	}
	
	public void setHtlLimitFavourableManage(HtlLimitFavourableManage htlLimitFavourableManage) {
		this.htlLimitFavourableManage = htlLimitFavourableManage;
	}

	public HtlFavourableHotelManage getHtlFavourableHotelManage() {
		return htlFavourableHotelManage;
	}

	public void setHtlFavourableHotelManage(HtlFavourableHotelManage htlFavourableHotelManage) {
		this.htlFavourableHotelManage = htlFavourableHotelManage;
	}
 
}
