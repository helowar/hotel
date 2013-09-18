package com.mangocity.webco.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.webnew.service.HotelQueryService;
import com.mangocity.webnew.util.action.GenericWebAction;


public class HotelListMapAction  extends GenericWebAction {
	
	private static final long serialVersionUID = 4193037512252716414L;
	private Map gisMaps = new HashMap();
	/**
     * manage类
     */
    private HotelManageWeb hotelManageWeb;
    private HotelQueryService hotelQueryService;
	private QueryHotelForWebBean queryHotelForWebBean;
	private HotelPageForWebBean hotelPageForWebBean;
	private int toPageIndex;
	private int lisNum;
	private int nightNum;
    private String city;
    private String queryCityId;
    private String cityCode;
	private String inDate;
	private String outDate;
	private String actionUrl;
	//商业区
	private List businessLis;
	
	private int buNum ;
	
	private int endNum;
	private String bizCode;
	private String bizName;
	private String bizFlag;
	private String priceFlag;
	private String[] bizCodeList;
    /**
     * 查询结果类
     */
    private List<QueryHotelForWebResult> webHotelResultLis =  new ArrayList<QueryHotelForWebResult>();
//  存放我浏览过的酒店的hotelId、hotelName
	private List hotelNameAndIdStr = new ArrayList();
	/**
	 * 电子地图查询
	 */
	@SuppressWarnings("deprecation")
	public String execute() {
		 Map params = super.getParams();
	        if (null == queryHotelForWebBean) {
	        	queryHotelForWebBean = new QueryHotelForWebBean();
	        }
	        MyBeanUtil.copyProperties(queryHotelForWebBean, params);
	        Date checkinDate = queryHotelForWebBean.getInDate();
	        Date checkoutDate = queryHotelForWebBean.getOutDate();
	        
	        //酒店详情页面搜查对应城市中的商业区或行政区酒店、默认开始日期为当天，结束日期为当天之后一天
	        if("1".equals(queryHotelForWebBean.getDateIsNotUse())){
        	 	queryHotelForWebBean.setInDate(DateUtil.getDate(DateUtil.dateToString(DateUtil.getSystemDate())));
             queryHotelForWebBean.setOutDate(DateUtil.getDate(DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1))));
             checkinDate = queryHotelForWebBean.getInDate();
             checkoutDate= queryHotelForWebBean.getOutDate();
	        }
//	        // 城市ID,入住和离店日期不能为空
//	        if (null == inDate || null == inDate
//	            || !StringUtil.isValidStr(queryHotelForWebBean.getCityId())) {
//	            return "";
//	        }

	        // 查询不能超过28天
	        nightNum = DateUtil.getDay(checkinDate, checkoutDate);
	        if (0 >= nightNum || 28 < nightNum) {
	            return super.forwardError("查询不能超过28天");
	        }
	        
	        // 如果这个查询城市ID不过空，以这个为查询城市
	        if (StringUtil.isValidStr(queryCityId)) {
	            queryHotelForWebBean.setCityId(queryCityId);
	        }
	        

	        // 如果不传城市中文名则根据城市代码设置
	        String tmpCityName = queryHotelForWebBean.getCityName();
	        if (!StringUtil.isValidStr(tmpCityName)) {
	            queryHotelForWebBean.setCityName(InitServlet.cityObj.get(queryHotelForWebBean
	                .getCityId()));
	        }

	        // 处理和讯过来的连接, 酒店中文名要从ISO-8859-1转成GBK编码
	        String hn = (String) params.get("hx");
	        if (null != hn && hn.equals("1")) {
	            try {
	                queryHotelForWebBean.setHotelName(new String(queryHotelForWebBean.getHotelName()
	                    .getBytes("ISO-8859-1"), "GBK"));
	            } catch (Exception ex) {
	                log.error("change hotel name to GBK error!" + ex);
	            }
	        }

	        if (0 != toPageIndex) {
	            queryHotelForWebBean.setPageIndex(toPageIndex);
	        }

	        String[] priceStr = null;
	        if (StringUtil.isValidStr(queryHotelForWebBean.getPriceStr())) {
	            priceStr = queryHotelForWebBean.getPriceStr().split("-");
	            queryHotelForWebBean.setMinPrice(priceStr[0]);
	            if (1 < priceStr.length) {
	                queryHotelForWebBean.setMaxPrice(priceStr[1]);
	            }
	        } else {
	            String minStr = queryHotelForWebBean.getMinPrice();
	            String maxStr = queryHotelForWebBean.getMaxPrice();
	            if (StringUtil.isValidStr(minStr) || StringUtil.isValidStr(maxStr)) {
	                queryHotelForWebBean.setPriceStr((null != minStr ? minStr : "") + "-"
	                    + (null != maxStr ? maxStr : ""));
	            }
	        }
	        
	        //	      两种星级参数组装
	        String hotelStar1 = queryHotelForWebBean.getFormalStarLevel();
	        String hotelStar2 = queryHotelForWebBean.getInformalStarLevel();
	        queryHotelForWebBean.setHotelStar((StringUtil.isValidStr(hotelStar1) ? hotelStar1 + ","
	            : "")
	            + (StringUtil.isValidStr(hotelStar2) ? hotelStar2 : ""));
	        
	        if(StringUtil.isValidStr(bizCode)&&StringUtil.isValidStr(bizFlag)){
	        	if("1".equals(bizFlag)){
	        		queryHotelForWebBean.setBizZone(bizCode);
	        		bizCodeList=bizCode.split(",");
	        	}
	        	if("2".equals(bizFlag)){
	        		queryHotelForWebBean.setDistrict(bizCode);
	        	}
	        	
	        }
	        queryHotelForWebBean.setCityId(cityCode);
	        //根据城市code获取商业区
	        businessLis = hotelManageWeb.queryBusinessForHotelInfo(cityCode);
	        if(businessLis.size()>0){
	        	buNum = businessLis.size();
	        	if(businessLis.size()>4){
	        		endNum = 4;
	        	}else{
	        		endNum = businessLis.size();
	        	}
	        }
	         
	        //	      网站改版优化 是否忽略count总数,1:忽略;0:不忽略,默认是0 add by chenjiajie 2009-12-16
	        queryHotelForWebBean.setIgnorePageCount(1);
	        
	        //网站改版优化 把查询条件放到Session中 异步取页数的时候直接从Session中取 add by chenjiajie 2009-12-21
	        setWebBeanFromSession(queryHotelForWebBean);
	        
	        queryHotelForWebBean.setForCooperate(true);
	        
	        hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
	        	
	        webHotelResultLis = hotelPageForWebBean.getList();
	        inDate = DateUtil.dateToString(queryHotelForWebBean.getInDate());
	        outDate = DateUtil.dateToString(queryHotelForWebBean.getOutDate());
	        
	        lisNum = webHotelResultLis.size();
	        hotelNameAndIdStr = super.findCookies();	
	        //设置城市名称和城市三字码 add by shengwei.zuo 2009-11-27
	        setCityCookie();
			String path = request.getRealPath("/");
			gisMaps = hotelManageWeb.queryMapskmlGenerator(webHotelResultLis, 0, 0, path);
			if((null!=actionUrl)&&(!"".equals(actionUrl))){
				return "toMapPage";
			}
        return SUCCESS;
	}

	
	
	//设置城市名称和城市三字码 add by shengwei.zuo 2009-11-27
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
			log.error("HotelListMapAction setCityCookie UnsupportedEncodingException error: ",e);
		}
    	
    }

	public Map getGisMaps() {
		return gisMaps;
	}

	public void setGisMaps(Map gisMaps) {
		this.gisMaps = gisMaps;
	}

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}



	public List<QueryHotelForWebResult> getWebHotelResultLis() {
		return webHotelResultLis;
	}



	public void setWebHotelResultLis(List<QueryHotelForWebResult> webHotelResultLis) {
		this.webHotelResultLis = webHotelResultLis;
	}



	public HotelPageForWebBean getHotelPageForWebBean() {
		return hotelPageForWebBean;
	}



	public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
		this.hotelPageForWebBean = hotelPageForWebBean;
	}



	public HotelQueryService getHotelQueryService() {
		return hotelQueryService;
	}



	public void setHotelQueryService(HotelQueryService hotelQueryService) {
		this.hotelQueryService = hotelQueryService;
	}



	public QueryHotelForWebBean getQueryHotelForWebBean() {
		return queryHotelForWebBean;
	}



	public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
		this.queryHotelForWebBean = queryHotelForWebBean;
	}



	public int getLisNum() {
		return lisNum;
	}



	public void setLisNum(int lisNum) {
		this.lisNum = lisNum;
	}



	public int getToPageIndex() {
		return toPageIndex;
	}



	public void setToPageIndex(int toPageIndex) {
		this.toPageIndex = toPageIndex;
	}



	public int getNightNum() {
		return nightNum;
	}



	public void setNightNum(int nightNum) {
		this.nightNum = nightNum;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getQueryCityId() {
		return queryCityId;
	}



	public void setQueryCityId(String queryCityId) {
		this.queryCityId = queryCityId;
	}



	public String getCityCode() {
		return cityCode;
	}



	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}



	public String getInDate() {
		return inDate;
	}



	public void setInDate(String inDate) {
		this.inDate = inDate;
	}



	public String getOutDate() {
		return outDate;
	}



	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}



	public String getActionUrl() {
		return actionUrl;
	}



	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}



	public String getBizCode() {
		return bizCode;
	}



	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}



	public String getBizFlag() {
		return bizFlag;
	}



	public void setBizFlag(String bizFlag) {
		this.bizFlag = bizFlag;
	}



	public int getBuNum() {
		return buNum;
	}



	public void setBuNum(int buNum) {
		this.buNum = buNum;
	}



	public List getBusinessLis() {
		return businessLis;
	}



	public void setBusinessLis(List businessLis) {
		this.businessLis = businessLis;
	}



	public int getEndNum() {
		return endNum;
	}



	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}



	public String getPriceFlag() {
		return priceFlag;
	}



	public void setPriceFlag(String priceFlag) {
		this.priceFlag = priceFlag;
	}



	public String[] getBizCodeList() {
		return bizCodeList;
	}



	public void setBizCodeList(String[] bizCodeList) {
		this.bizCodeList = bizCodeList;
	}

	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}

	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}



	public String getBizName() {
		return bizName;
	}



	public void setBizName(String bizName) {
		this.bizName = bizName;
	}







	
}
