package com.mangocity.hotel.dreamweb.search.action;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.mangocity.hotel.base.persistence.HotelBrandinfo;
import com.mangocity.hotel.base.service.HotelBrandQueryService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.dreamweb.search.dao.HotelQueryAjaxDao;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.search.service.HotelMgisSearchService;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class HotelEnterActionNew extends ActionSupport implements ServletRequestAware{
	private static final long serialVersionUID = -7557338701832957954L;
	private static final MyLog log = MyLog.getLogger(HotelEnterActionNew.class);
	/**
	 * request对象获取
	 */
	protected HttpServletRequest request;
	
	/**
	 * response对象获取
	 */
	protected HttpServletResponse response;
	
	/**
	 * 酒店品牌查询service
	 */
	private HotelBrandQueryService hotelBrandQueryService;
	/**
	 * 地标查询service
	 */
	private HotelMgisSearchService hotelMgisSearchService;
	
	/**
	 * 酒店地址模糊查询service
	 */
	private HotelInfoService hotelInfoService;
	
	/**
	 * 关键词搜索，酒店名称匹配查询service
	 */
	private HotelQueryAjaxDao hotelQueryAjaxDao;
	
	/**
	 * 城市编码，页面初始化时通过cookie获取和通过ip获取
	 */
	private String cityCode;
	
	/**
	 * 城市编码，页面初始化时通过cookie获取和通过ip获取
	 */
	private String cityName;
	
	/**
	 * 关键词，页面输入关键词搜索传递信息
	 */
	private String keyword;
	
	/**
	 * 关键词搜索返回content信息
	 */
	private List<String> content;
	
	/**
	 * 酒店品牌map，初始化页面时传递的酒店品牌信息
	 */
	private String brandString;
	
	/**
	 * 地标信息map，页面同步地标信息
	 */
	private Map<String,List> resultMap;
	
	/**
	 * 页面初始化，包括初始化城市，初始化酒店品牌
	 */
	public String initPage(){
		
		//1,初始化城市信息
		initcityshow();
		
		//2,初始化酒店品牌信息
		inithotelbrandshow();
		
	   return SUCCESS ;
	}
	
	/**
	 * 页面初始化酒店品牌信息
	 */
	private void inithotelbrandshow(){
		log.info("---inithotelbrandshow star-----------");
		List<HotelBrandinfo> list = null;
		brandString = "";
		//1，初始化品牌信息
		list = hotelBrandQueryService.queryHotelBrands();
		if(list != null){
			for(int i = 0;i<list.size();i++){
				HotelBrandinfo  hotelBrandinfo = list.get(i);
				brandString = brandString+hotelBrandinfo.getBrandcode()+"="+hotelBrandinfo.getBrandname()+";";
			}
		}else{
			log.info("没有找到酒店品牌信息！");
		}
		log.info("---inithotelbrandshow end-----------brandString="+brandString);
	}
	
	/**
	 * 页面初始化城市信息
	 */
	private void initcityshow(){
		
		log.info("---initcityshow star-----------");
		//1,从cookie获取城市编码
		request = getRequest();
		String cookiecityCode = CookieUtils.getCookieValue(request, "cityCode");
		String cookiecityName="";
		String ipcityCode="";
		String ipcityName="";
		String ip="";
		cityCode = CookieUtils.getCookieValue(request, "cityCode");
		if(!"".equals(cityCode) && cityCode != null){
			cookiecityName = InitServlet.cityObj.get(cityCode);
		}
		cityCode = cookiecityCode;
		cityName = cookiecityName;
		//2,通过ip获取城市名称
		if(cityName == null ||"".equals(cityName) || "null".equals(cityName)){
			
			//2.1,ip获取
			ip = request.getHeader("clientip");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            
            //2.2，ip合法性验证 flag为true则ip合法，否则不合法
            boolean flag = false;
    		Pattern p=Pattern.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." +"(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
            Matcher m = p.matcher(ip);
            flag = m.matches();
			
            //2.3,通过ip发送http请求获取城市信息
            String message ="";
            String city = "";
            if(flag){
            	GetMethod method =null;
        		try {
        			HttpClient client = new HttpClient();
        			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);  //设置连接超时
        			client.getHttpConnectionManager().getParams().setSoTimeout(5000);
        			String url = "http://10.10.5.166/ip2city/lookup.action?ip=" + ip;
        			method = new GetMethod(url);
        			int statusCode = client.executeMethod(method);
        			if(statusCode == HttpStatus.SC_OK) {
        				Reader reader = new InputStreamReader(method
        						.getResponseBodyAsStream(), method
        						.getResponseCharSet());
        				int readBit;
        				StringBuilder result = new StringBuilder();
        				while ((readBit = reader.read()) != -1) {
        					result.append((char) readBit);
        				}
        				message = result.toString();
        				if(message != null && !"".equals(message)) {
        					city = message.split("\\|")[0]; // 转义 add by diandian.hou 
        				}else {
        					log.info("获取城市信息为空，ip="+ip+",message="+message);
        				}
        			}else {
        				log.info("获取城市信息连接失败：" + statusCode);
        			}
        		} catch (HttpException e) {
        			e.printStackTrace();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}catch (Exception e){
        			e.printStackTrace();
        		}finally{
        			method.releaseConnection();
        		}
            }else{
            	log.info("this is clientIP illegal : "+ip);
            } 
			
            //2.4,城市信息获取
			if(!"".equals(city)){
				String[] list = city.split("	"); 
				for(int i=0;i<list.length;i++){
					ipcityName =list[4];
					cityName = ipcityName;
				}
				ipcityCode = InitServlet.cityObjs.get(ipcityName);
				cityCode = ipcityCode;
				if(cityName == null || "".equals(cityName)||cityCode==null ||"".equals(cityCode)){
					cityName="请选择城市";
					cityName="";
				}
				
			}else{
				cityName="请选择城市";
			}
		}
		log.info("---initcityshow end-----------"+"cookiecityCode ="+cookiecityCode+",cookiecityName="+cookiecityName+",ip="+ip+",ipcityCode=="+ipcityCode+",ipcityName=="+ipcityName+",cityCode="+cityCode+",cityName="+cityName);
	}
	
	
	/**
	 * 异步获取关键词信息，输入关键词展示关键词信息，包括酒店名称，酒店品牌，酒店地理位置
	 */
	public String ajaxgetKeywordMsg(){
		log.info("---hotel index ajaxgetKeywordMsg start-----------keyword:"+keyword);
		 //1,数据收集
		 if(null==cityCode||"".equals(cityCode.trim())){
			 cityCode ="PEK";
		 }
		 List<String> nameList = new ArrayList<String>();
		 List<String> gisList = new ArrayList<String>();
		 List<HotelBrandinfo> brandList = new ArrayList<HotelBrandinfo>() ;
		 List<String>  nameshow = new ArrayList<String>();
		 List<String> nameprocess = new ArrayList<String>();
		 keyword=StringEscapeUtils.escapeSql(keyword).trim();
		 if(keyword == null || "".equals(keyword.trim())){
			 nameprocess.add("无相应记录");
			 this.setContent(nameprocess);
			return SUCCESS;
		 }
		try{
			 //2,把关键字看成酒店名称查询
			 long time1 = System.currentTimeMillis();
			 nameList = hotelQueryAjaxDao.autoHotelNameQuery(keyword, cityCode);
			 if(nameList != null){
				 for(int i=0;i<nameList.size();i++){
					 nameprocess.add("酒店："+nameList.get(i));
				 }
			 }
	         //3,把关键字看成地理位置查询
	         gisList=hotelInfoService.queryHotelInfoByMgis(cityCode,keyword);
	         if(gisList != null){
		         for(int i=0;i<gisList.size();i++){
					 nameprocess.add("位置："+gisList.get(i));
				 }
	         }
	         //4,把关键字看成酒店品牌查询
	         brandList = hotelBrandQueryService.queryHotelBybrands(cityCode, keyword);
	         if(brandList != null){
	        	 for(int i=0;i<brandList.size();i++){
		        	 HotelBrandinfo HotelBrandinfo = brandList.get(i);
					 nameprocess.add("品牌："+HotelBrandinfo.getBrandname()+"|"+HotelBrandinfo.getBrandcode());
				 }
	         }
	        
	         long time2 = System.currentTimeMillis();
		 
		}catch(Exception e){
			 log.error("模糊查询错误", e);
		 }
		if(nameprocess.size()<11){
				if(nameprocess.size()== 0){
					nameprocess.add("无相应记录");
				}
			  this.setContent(nameprocess);
		}else{
			for(int i=0;i<nameprocess.size();i++){
				nameshow.add(nameprocess.get(i));
			}
			this.setContent(nameshow);
		}
		log.info("---hotel index ajaxgetKeywordMsg end-----------content:"+content);
		return SUCCESS;
	}
	
	/**
	 * 异步获取地标信息，用于页面切换城市相应地标获取
	 */
	@SuppressWarnings("unchecked")
	public String ajaxgetMgisMap(){
		log.info("---ajaxgetMgisMap start-----------");
		//获取城市地标信息
		resultMap=hotelMgisSearchService.getIndexMgisInfo(cityCode);
		log.info("---ajaxgetMgisMap end-----------resultMap："+resultMap);
		return SUCCESS;
	}
	
    //add by shengwei.zuo 2009-11-04  begin
    public HttpServletRequest getRequest() {
    	ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return request;
	}
    
    public HttpServletResponse getHttpResponse() {
        ActionContext ctx = ActionContext.getContext();        
        response = (HttpServletResponse)ctx.get(ServletActionContext.HTTP_RESPONSE);
        return response;
    }
	

	public HotelBrandQueryService getHotelBrandQueryService() {
		return hotelBrandQueryService;
	}

	public void setHotelBrandQueryService(
			HotelBrandQueryService hotelBrandQueryService) {
		this.hotelBrandQueryService = hotelBrandQueryService;
	}
	
	public HotelMgisSearchService getHotelMgisSearchService() {
		return hotelMgisSearchService;
	}

	public void setHotelMgisSearchService(
			HotelMgisSearchService hotelMgisSearchService) {
		this.hotelMgisSearchService = hotelMgisSearchService;
	}

	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getBrandString() {
		return brandString;
	}

	public void setBrandString(String brandString) {
		this.brandString = brandString;
	}

	public Map<String, List> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, List> resultMap) {
		this.resultMap = resultMap;
	}

	public HotelQueryAjaxDao getHotelQueryAjaxDao() {
		return hotelQueryAjaxDao;
	}

	public void setHotelQueryAjaxDao(HotelQueryAjaxDao hotelQueryAjaxDao) {
		this.hotelQueryAjaxDao = hotelQueryAjaxDao;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		 this.request = request;
	}

	public HotelInfoService getHotelInfoService() {
		return hotelInfoService;
	}

	public void setHotelInfoService(HotelInfoService hotelInfoService) {
		this.hotelInfoService = hotelInfoService;
	}
}
