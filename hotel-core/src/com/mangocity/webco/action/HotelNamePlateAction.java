package com.mangocity.webco.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.util.DateUtil;

public class HotelNamePlateAction extends GenericCCAction{
	
	/**
	 * 按品牌查询 add by haibo.li
	 */
	private static final long serialVersionUID = -4590014098894485039L;
	
	private String cityCode; //城市ID
	
	private String parentId; //品牌ID

	private HotelManageWeb hotelManageWeb;
	
	private HotelPageForWebBean hotelPageForWebBean;
	
	private QueryHotelForWebBean queryHotelForWebBean;
	
	private List webHotelResultLis;
	
	private String inDate;
	
	private String outDate;
	
	private int lisNum = 0;
	
	private String forWord;
	
	
//	存放我浏览过的酒店的hotelId、hotelName
	private List hotelNameAndIdStr = new ArrayList();
	
	private String hotelIdLst ="";



	public String getHotelIdLst() {
		return hotelIdLst;
	}


	public void setHotelIdLst(String hotelIdLst) {
		this.hotelIdLst = hotelIdLst;
	}


	public String execute(){
		queryHotelForWebBean = new QueryHotelForWebBean();
		if(null!=cityCode && !cityCode.equals("") ){
        	cityCode = cityCode.toUpperCase();//转换成大写
        }
		
        String city = InitServlet.cityObj.get(cityCode);//如果穿进来的城市编码有错误,则跳转
        if(null !=city && !city.equals("")){
        	queryHotelForWebBean.setCityName(city);
        }else{
        	return super.forwardError("非常抱歉,您查询的结果没有");
        }
        if(cityCode.equals("HKG") || cityCode.equals("MAC")){
        	forWord = "HKForWord";
        }else{
        	forWord = "forWord";
        }
        queryHotelForWebBean.setInDate(DateUtil.getDate(DateUtil.dateToString(DateUtil
				.getDate(DateUtil.getSystemDate(), 1))));//设置开始时间
    	queryHotelForWebBean.setOutDate(DateUtil.getDate(DateUtil.dateToString(DateUtil
				.getDate(DateUtil.getSystemDate(), 2))));//设置离店日期
    	queryHotelForWebBean.setCityId(cityCode);
		List <HtlHotel>list = hotelManageWeb.queryParentHotelInfo(cityCode,parentId);//根据品牌ID，城市ID查出对应的酒店ID,在用存储过程去过滤
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				HtlHotel htl =list.get(i);
				hotelIdLst = hotelIdLst+htl.getID().toString()+",";
			}
		}
		
        // 交行全卡商旅等渠道查询
        queryHotelForWebBean.setForCooperate(true);
		
		if(hotelIdLst!=null && !hotelIdLst.equals("")){
			hotelIdLst = hotelIdLst.substring(0, hotelIdLst.length()-1);
			queryHotelForWebBean.setHotelIdLst(hotelIdLst);
	    	hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);//查询存储过程	
	    	webHotelResultLis = hotelPageForWebBean.getList();
	    	lisNum = webHotelResultLis.size();
		}
		parentId=hotelManageWeb.queryParentNaHtlInfo(cityCode, parentId);
        inDate = DateUtil.dateToString(queryHotelForWebBean.getInDate());
        outDate = DateUtil.dateToString(queryHotelForWebBean.getOutDate());
        hotelNameAndIdStr = super.findCookies();	
		return forWord;
	}
	
	
	 private void setCityCookie(){
	    	
	    	//城市ID（三字码）
	    	String cityIdCookieStr ="";
	    	
	    	//城市名称
	    	String cityNameCookieStr ="";
	    	
	    	if(cityCode!=null&&!"".equals(cityCode)){
	    		cityIdCookieStr = cityCode;
	    		cityNameCookieStr = InitServlet.cityObj.get(cityCode);
	    	}else{
	    		cityIdCookieStr = queryHotelForWebBean.getCityId();
	    		cityNameCookieStr = InitServlet.cityObj.get(cityIdCookieStr);
	    	}
	    	
			try {
				
				//把输入的城市ID和城市名称放到cookie里
		    	Cookie cityIdCookie = new Cookie("cityIdCookie",cityIdCookieStr);
					
				Cookie cityNameCookie = new Cookie("cityNameCookie",URLEncoder.encode(cityNameCookieStr, "UTF-8"));
				
				//生命周期    
				cityIdCookie.setMaxAge(60*60*24*365);
				cityNameCookie.setMaxAge(60*60*24*365);
				    
				getHttpResponse().addCookie(cityIdCookie);
				getHttpResponse().addCookie(cityNameCookie); 
				
			} catch (UnsupportedEncodingException e) {
				log.error("HotelNamePlateAction setCityCookie UnsupportedEncodingException error",e);
			}
	    	
	    }
	
	
		

	public List getWebHotelResultLis() {
			return webHotelResultLis;
	}
	
	public void setWebHotelResultLis(List webHotelResultLis) {
			this.webHotelResultLis = webHotelResultLis;
	}
	
	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public HotelPageForWebBean getHotelPageForWebBean() {
		return hotelPageForWebBean;
	}

	public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
		this.hotelPageForWebBean = hotelPageForWebBean;
	}


	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}







	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public QueryHotelForWebBean getQueryHotelForWebBean() {
		return queryHotelForWebBean;
	}


	public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
		this.queryHotelForWebBean = queryHotelForWebBean;
	}


	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}


	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}


	public String getInDate() {
		return inDate;
	}


	public void setInDate(String inDate) {
		this.inDate = inDate;
	}


	public int getLisNum() {
		return lisNum;
	}


	public void setLisNum(int lisNum) {
		this.lisNum = lisNum;
	}


	public String getOutDate() {
		return outDate;
	}


	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}


	public String getForWord() {
		return forWord;
	}


	public void setForWord(String forWord) {
		this.forWord = forWord;
	}




	

	
}
