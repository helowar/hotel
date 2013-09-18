package com.mangocity.hotel.dreamweb.search.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.mangocity.hotel.dreamweb.search.dao.HotelQueryAjaxDao;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionSupport;

public class HotelNameAutoShow extends ActionSupport{

	 private List<String> content;
	 private String hotelName;
	 private String cityCode;
	 private static final MyLog log = MyLog.getLogger(HotelNameAutoShow.class);
	 //注入的bean
	 private HotelQueryAjaxDao hotelQueryAjaxDao;

	 public List<String> getContent() {
	  return content;
	 }

	 public void setContent(List<String> content) {
	  this.content = content;
	 }

	 public String execute()  {

		 //cityCode为空，默认为北京
		 //System.out.println("cityCode:"+cityCode);
		 if(null==cityCode||"".equals(cityCode.trim())){
			 cityCode ="PEK";
		 }
		 List<String> hotelList = new ArrayList<String>();
		 List<Map> hotelListSpring = new ArrayList();
		 hotelName=StringEscapeUtils.escapeSql(hotelName);
		 try{
			 long time1 = System.currentTimeMillis();
			 hotelList = hotelQueryAjaxDao.autoHotelNameQuery(hotelName, cityCode);
	         long time2 = System.currentTimeMillis();
	         //System.out.println("hotelquery.autoQuery用时："+(time2 - time1));
		 }catch(Exception e){
			 log.error("模糊查询错误", e);
		 }
	       // System.out.println("aa:"+hotelList);
		 //test
//		    List testList = new ArrayList();
//		    testList.add("aa");
//		    testList.add("bb");
//	        this.setContent(testList);
	        this.setContent(hotelList);
			return SUCCESS;
		}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setHotelQueryAjaxDao(HotelQueryAjaxDao hotelQueryAjaxDao) {
		this.hotelQueryAjaxDao = hotelQueryAjaxDao;
	}
}
