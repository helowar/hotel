package com.mangocity.fantiweb.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;

import com.handsome.ip.app.IPSeeker;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.service.HotelQueryService;


public class HotelSearchAction extends GenericCCAction {
	
	private static final MyLog log = MyLog.getLogger(HotelSearchAction.class);

	private static final long serialVersionUID = -6371488760878200329L;
	
	//城市ID
	private String cityIdCookie;
	
	//城市名称
	private String cityNameCookie;
	
	/**
	 * 客户端IP所在的城市编码
	 */
	private String ipCityCode;
	
	/**
     * 客户端IP所在的城市中文名
     */
	private String ipCityName;
	
	//存放我浏览过的酒店的hotelId、hotelName
	private List hotelNameAndIdStr = new ArrayList();
	
	/**
	 * 判断默认Ip城市需要使用的文件路径
	 */
	private final String IP_CITY_FILE_PATH = "/ipcity";
	
	/**
	 * 网站查询接口 
	 */
	private HotelQueryService hotelQueryService;

	public String execute(){
		
		Cookie[] cookies = super.request.getCookies();
		if(null!=cookies){
    		for (int j = 0; j < cookies.length; j++) {
    			if(cookies[j].getName().equalsIgnoreCase("cityIdCookie")){
    				cityIdCookie = cookies[j].getValue();
    		    }else if(cookies[j].getName().equalsIgnoreCase("cityNameCookie")){
    		        cityNameCookie = cookies[j].getValue();
    		    }
    		}
		}
		//对cityNameCookie进行解码 add by diandian.hou
		try{
			if(cityNameCookie != null){
			    cityNameCookie =  URLDecoder.decode(cityNameCookie, "utf-8");
			}
		}catch(Exception e){
		log.error("解码出错",e);
		return "forwardToError";
		}
		hotelNameAndIdStr = super.findCookies();
		
		/** 获取客户端Ip所在城市 begin add by chenjiajie 2010-03-29 */
		String[] cityInfoArr = getIpCity();
		if(null != cityInfoArr){
		    ipCityCode = cityInfoArr[0];
		    ipCityName = cityInfoArr[1];
		}
        /** 获取客户端Ip所在城市 end add by chenjiajie 2010-03-29 */
		
		return SUCCESS ;
		
	}
	
	/**
	 * 获取Ip的城市信息
	 * @return 城市编码数组 {城市编码,城市中文名}
	 */
	private String[] getIpCity(){
	    //城市编码数组 {城市编码,城市中文名}
	    String[] cityInfoArr = new String[]{"",""};;
        ServletContext req = ServletActionContext.getServletContext();
        try {
            String file_path = req.getRealPath(IP_CITY_FILE_PATH);
            //QQWry.Dat 是数据文件
            IPSeeker ip = new IPSeeker("QQWry.Dat", file_path);
            String cityCode = "";
            String ipAddr = getIpAddr();
            log.info("=====客户端IP地址："+ipAddr+"=====");
            String cityChnName = ip.getCountry(ipAddr);
            log.info("=====ip.getCountry(ipAddr)："+cityChnName+"=====");
            if(StringUtil.isValidStr(cityChnName)){
                int beginIndex = cityChnName.indexOf("省");
                int endIndex = cityChnName.indexOf("市");
                if (beginIndex > 0 && endIndex > 0)
                    cityChnName = cityChnName.substring(beginIndex + 1, endIndex);
                if (cityChnName.equalsIgnoreCase("本地局网") 
                        || cityChnName.equalsIgnoreCase("本机IP")
                        || cityChnName.equalsIgnoreCase("从ip的字符串形式得到字节数组形式报错")
                        || cityChnName.equalsIgnoreCase("本机地址")) {
                    cityChnName = "北京";
                }                
                //按照城市中文名找城市编码 
                cityCode = hotelQueryService.queryCityCodeByCityName(cityChnName);
                cityInfoArr[0] = cityCode;
                cityInfoArr[1] = cityChnName;
            }
		} catch (Exception e) {
			log.error("error:58612589 获取Ip的城市信息错误", e);
		}
		log.info("=====客户端IP城市code："+cityInfoArr[0]+";客户端IP城市name:"+cityInfoArr[1]+"=====");
	    return cityInfoArr;
	}
	
	/**
     * 获取IP
     * 
     * @return
     */
    public String getIpAddr() {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

	public String getCityIdCookie() {
		return cityIdCookie;
	}

	public void setCityIdCookie(String cityIdCookie) {
		this.cityIdCookie = cityIdCookie;
	}

	public String getCityNameCookie() {
		return cityNameCookie;
	}

	public void setCityNameCookie(String cityNameCookie) {
		this.cityNameCookie = cityNameCookie;
	}

	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}

	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}

    public HotelQueryService getHotelQueryService() {
        return hotelQueryService;
    }

    public void setHotelQueryService(HotelQueryService hotelQueryService) {
        this.hotelQueryService = hotelQueryService;
    }

    public String getIpCityCode() {
        return ipCityCode;
    }

    public void setIpCityCode(String ipCityCode) {
        this.ipCityCode = ipCityCode;
    }

    public String getIpCityName() {
        return ipCityName;
    }

    public void setIpCityName(String ipCityName) {
        this.ipCityName = ipCityName;
    }


}

