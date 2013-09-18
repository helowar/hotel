package com.mangocity.hotel.flight.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mangocity.hotel.flight.bean.ConstantUtil;
import com.mangocity.hotel.flight.bean.CryptUtil;
import com.mangocity.hotel.flight.bean.CurrencyBean;
import com.mangocity.hotel.flight.bean.PriceInfo;
import com.mangocity.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 查询酒店价格计划在日期内的最低价或价格详情
 */
public class FlightHotelAction extends ActionSupport{
	protected static final Log log = LogFactory.getLog(FlightHotelAction.class);
	private static final long serialVersionUID = -6296025848263941625L;
	private ConstantUtil constantUtil;
	final static int SIZE = 4096;
	private String hotelIds;
	private String commodityIds;
	private String beginDate;
	private String endDate;
	private String queryType;//1表示查日期内的最低价，2表示查所有日期内价格用于展示
	/*
	 * 根据hotelIds,commodityIds,beginDate,endDate查日期段内酒店的最低价
	 * commodityIds可为空,为空时输出酒店下所有commodity，不为空值输出此commodity
	 */
	public String execute(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("xml", getRequestXML(hotelIds,beginDate,endDate));
		log.info("=========RequestXML:"+map.get("xml"));
		String priceInfoJsonStr = "";
		HttpURLConnection conn=null;
		InputStream in = null;
		boolean requestSuccess = true;
		String message = "";
		List<String> commodityIdList = new ArrayList<String>();
		if(commodityIds!=null && commodityIds.length()>0){
			for(String commodityId : commodityIds.split(",")){
				commodityIdList.add(commodityId);
			}
		}
		try{
			Date end = DateUtil.getDate(endDate);
			Date thirtyDay = DateUtil.getDate(DateUtil.getDate(new Date()),29);
			if(end.getTime() <= thirtyDay.getTime()){//此接口只允许查询30天内的
				in = getInputStream(conn,in,constantUtil.getHubURL(),map);
				SAXReader reader = new SAXReader();
				Document doc = reader.read(in);
				Element root = doc.getRootElement();
				Element body = root.element("Body");
				Element result = body.element("Result");
				if("200".equals(result.elementTextTrim("ResultCode"))){
					
					if("1".equals(queryType)){
						Map<String,PriceInfo> pricemap = getLowestPriceInfo(commodityIdList,body);
						if(pricemap!=null && !pricemap.isEmpty()){
							for(String key:pricemap.keySet()){
								PriceInfo info = pricemap.get(key);
								priceInfoJsonStr += net.sf.json.JSONObject.fromObject(info).toString()+",";
							}
						}
					}else if("2".equals(queryType)){
						List<PriceInfo> pricelist = getAllPriceInfo(commodityIdList, body);
						for(PriceInfo info :pricelist){
							priceInfoJsonStr += net.sf.json.JSONObject.fromObject(info).toString()+",";
						}
					}
				}else{
					message = "没有价格记录，ResultCode="+result.elementTextTrim("ResultCode");
				}
			}else{
				message = "只能查询30天内数据";
			}
		}catch(Exception e) {
			requestSuccess = false;
			log.info("=========Request Exception",e);
			message = "内部服务异常";
		}finally{
			String json = "{\"requestSuccess\":\""+requestSuccess+"\",\"message\":\""+message+"\",\"PriceList\":[";
			json += priceInfoJsonStr.endsWith(",") ? priceInfoJsonStr.substring(0, priceInfoJsonStr.length()-1) : priceInfoJsonStr;
			json += "]}";
			try {
				ActionContext ctx = ActionContext.getContext();
				HttpServletResponse response = (HttpServletResponse) ctx
						.get(ServletActionContext.HTTP_RESPONSE);
				response.setContentType("json;charset=UTF-8");//返回json类型
				response.getWriter().write(json);
				if(in!=null){
					in.close();
				}
				if(conn!=null){
					conn.disconnect();
				}
				if(response.getWriter()!=null){
					response.getWriter().close();
				}
			}catch(IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 获取xml内价格最低的priceInfo
	 * @param commodityIdList
	 * @param body
	 * @return
	 */
	private Map<String,PriceInfo> getLowestPriceInfo(List<String> commodityIdList,Element body){
		List<Element> PriceInfoList = body.element("PriceList").elements();
		Map<String,PriceInfo> pricemap = new HashMap<String,PriceInfo>();
		for(Element priceInfo:PriceInfoList){
			PriceInfo pInfo = pricemap.get(priceInfo.elementTextTrim("HotelId")+"-"+priceInfo.elementTextTrim("RatePlanId"));
			if(!pricemap.isEmpty() && pInfo != null){
				if(new Double(pInfo.getSalePrice()).doubleValue() > new Double(priceInfo.elementTextTrim("SalePrice")).doubleValue()){
					pInfo.setSalePrice(priceInfo.elementTextTrim("SalePrice"));
				}
			}else{
				if(commodityIdList.isEmpty() || commodityIdList.contains(priceInfo.elementTextTrim("RatePlanId"))){
					pInfo = new PriceInfo();
					pInfo.setHotelId(priceInfo.elementTextTrim("HotelId"));
					pInfo.setRoomId(priceInfo.elementTextTrim("RoomId"));
					pInfo.setRatePlanId(priceInfo.elementTextTrim("RatePlanId"));
					pInfo.setSaleDate(priceInfo.elementTextTrim("SaleDate"));
					pInfo.setSalePrice(priceInfo.elementTextTrim("SalePrice"));
					pInfo.setCurrency(convertCurrency(priceInfo.elementTextTrim("Currency")));
					pInfo.setBreakfastType(priceInfo.elementTextTrim("BreakfastType"));
					pInfo.setBreakfastNum(priceInfo.elementTextTrim("BreakfastNum"));
					pricemap.put(priceInfo.elementTextTrim("HotelId")+"-"+priceInfo.elementTextTrim("RatePlanId"),pInfo);
				}
			}
		}
		return pricemap;
	}
	/**
	 * 获取xml内所有日期内的priceInfo
	 * @param commodityIdList
	 * @param body
	 * @return
	 */
	private List<PriceInfo> getAllPriceInfo(List<String> commodityIdList,Element body){
		List<Element> PriceInfoList = body.element("PriceList").elements();
		List<PriceInfo> pInfoList = new ArrayList<PriceInfo>();
		for(Element priceInfo:PriceInfoList){
			if(commodityIdList.isEmpty() || commodityIdList.contains(priceInfo.elementTextTrim("RatePlanId"))){
				PriceInfo pInfo = new PriceInfo();
				pInfo.setHotelId(priceInfo.elementTextTrim("HotelId"));
				pInfo.setRoomId(priceInfo.elementTextTrim("RoomId"));
				pInfo.setRatePlanId(priceInfo.elementTextTrim("RatePlanId"));
				pInfo.setSaleDate(priceInfo.elementTextTrim("SaleDate"));
				pInfo.setSalePrice(priceInfo.elementTextTrim("SalePrice"));
				pInfo.setCurrency(convertCurrency(priceInfo.elementTextTrim("Currency")));
				pInfo.setBreakfastType(priceInfo.elementTextTrim("BreakfastType"));
				pInfo.setBreakfastNum(priceInfo.elementTextTrim("BreakfastNum"));
				pInfoList.add(pInfo);
			}
		}
		return pInfoList;
	}
	/**
	 * 币种表达装RMB TO ￥
	 * @param currencyStr
	 * @return
	 */
	private String convertCurrency(String currencyStr) {
		return new CurrencyBean().curMap.get(currencyStr);
	}

	private String getRequestXML(String hotelIds,String start,String end){
		String hotelstr= "";
		for(String id : hotelIds.split(",")){
			hotelstr += "<HotelId>" + id + "</HotelId>";
		}
		CryptUtil crypt = new CryptUtil();
		String timestamp = "123456789";
		String partnerCode = constantUtil.getPartnerCode();
		//md5(timestamp+partnercode+md5(secretkey)+queryHotelSaleInfo)
		String signature = crypt.encryptToMD5(timestamp+partnerCode+crypt.encryptToMD5(constantUtil.getSecretKey())+"queryHotelSaleInfo");
		String xml = "<?xml version='1.0' encoding='UTF-8'?>"
			+"<Request xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
			+"xmlns:xsd='http://www.w3.org/2001/XMLSchema' "
			+"xmlns='http://api.mangocity.com/hotel/hub'>"
			+"<Header TimeStamp='"+timestamp+"' PartnerCode='"
			+partnerCode+"' "
			+"RequestType='queryHotelSaleInfo' Signature='"
			+signature
			+"' />"
			+"<Body><HotelIDList>"+hotelstr+"</HotelIDList>"
			+"<StartDate>"+start+"</StartDate><EndDate>"+end+"</EndDate>"
			+"</Body></Request>";
		return xml;
	}

	/**
	 * post提交，需要输入HttpURLConnection、InputStream、url、paramap，
	 * 未关闭conn和in
	 * @return
	 * @throws IOException
	 */
	private InputStream getInputStream(HttpURLConnection conn,InputStream in ,String addrUrl,Map<String,String> map) throws IOException{
		StringBuffer params = new StringBuffer("");
		for(String key : map.keySet()){
			String value = map.get(key);
			params.append(key+"="+value+"&");
		}
        URL url = new URL(addrUrl);
        conn = (HttpURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false); 
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(params.length())); 
        conn.setDoInput(true);
        conn.setReadTimeout(6*1000);
        conn.connect();   
        //放入参数
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        out.write(params.toString());
        out.flush();
        out.close();
        int code = conn.getResponseCode();
        if (code == 200) {
        	in = conn.getInputStream();
        }
		return in;
	}
	
	private static String inputStreamToString(InputStream in) throws IOException{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[SIZE];  
        int count = -1;  
        while((count = in.read(data,0,SIZE)) != -1)  
            outStream.write(data, 0, count);  
        data = null;  
        outStream.close();
        return new String(outStream.toByteArray(),"UTF-8");  
    }  

	public String getHotelIds() {
		return hotelIds;
	}
	public void setHotelIds(String hotelIds) {
		this.hotelIds = hotelIds;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCommodityIds() {
		return commodityIds;
	}
	public void setCommodityIds(String commodityIds) {
		this.commodityIds = commodityIds;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public ConstantUtil getConstantUtil() {
		return constantUtil;
	}
	public void setConstantUtil(ConstantUtil constantUtil) {
		this.constantUtil = constantUtil;
	}
}
