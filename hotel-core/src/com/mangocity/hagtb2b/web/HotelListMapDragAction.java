package com.mangocity.hagtb2b.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.hagtb2b.service.IB2bInterfaceService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.domain.valueobject.GisInfo;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.util.action.GenericWebAction;
import com.opensymphony.xwork2.ActionContext;

public class HotelListMapDragAction  extends GenericWebAction{

	private static final long serialVersionUID = -3201709974628822945L;
	private GisService gisService;
	private double neLat;

    private double neLng;

    private double swLat;

    private double swLng;

    private double seLat;

    private double seLng;

    private double nwLat;

    private double nwLng;
    private Map gisMaps = new HashMap();
    private HotelManageWeb hotelManageWeb;
    
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
	private String bizCode;
	private String bizFlag;
	private String priceFlag;
	private String[] bizCodeList;
	private String actionUrl="hotel-list-map-drag.shtml";
	private String  flag_userNoLogin;//游客使用，其值为shieldComission
	private String agent_imgUrl;
	private String flag_showCommission ="0"; //代理用户是否显示佣金，1表示显示；
	public String getFlag_showCommission() {
		return flag_showCommission;
	}
	
	private IB2bInterfaceService b2bInterfaceService;
	//private String  flagShowComission ="shieldComission";//打开即跳到预订弹出图表
    /**
     * 查询结果类
     */
    private List<QueryHotelForWebResult> webHotelResultLis =  new ArrayList<QueryHotelForWebResult>();
    /**
     * 地图框选功能
     */
	public String execute(){
		try {
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) ctx
			.get(ServletActionContext.HTTP_REQUEST);
			//标示是否显示佣金，1显示，其他值不显示
			flag_showCommission = CookieUtils.getCookieValue(request, "showCommissionFlag");
			log.info("HotelQueryAction flag_showCommission====="+flag_showCommission);
			log.info("calculate Area data begin : "+DateUtil.toStringByFormat(new Date(), "yyyy-mm-dd hh:MM:ss"));
			List<GisInfo> list = gisService.calculateZoomArea(new LatLng(neLng, neLat), new LatLng(
			            swLng, swLat), new LatLng(seLng, seLat), new LatLng(nwLng, nwLat), 0);
			log.info("calculate Area data end : "+DateUtil.toStringByFormat(new Date(), "yyyy-mm-dd hh:MM:ss"));
			String gisLst="";
			String hotelIdLst = "";
			 for (Iterator<GisInfo> i = list.iterator(); i.hasNext();) {
                GisInfo gisInfo = i.next();
                gisLst += gisInfo.getGisId() + ",";
            }
			 Map params = super.getParams();
		        if (null == queryHotelForWebBean) {
		        	queryHotelForWebBean = new QueryHotelForWebBean();
		        }
		        MyBeanUtil.copyProperties(queryHotelForWebBean, params);
		        
		        Date checkinDate = queryHotelForWebBean.getInDate();
		        Date checkoutDate = queryHotelForWebBean.getOutDate();
//		        // 城市ID,入住和离店日期不能为空
//		        if (null == inDate || null == inDate
//		            || !StringUtil.isValidStr(queryHotelForWebBean.getCityId())) {
//		            return "";
//		        }

		        // 查询不能超过60天
		        nightNum = DateUtil.getDay(checkinDate, checkoutDate);
		        if (0 >= nightNum || 60 < nightNum) {
		            return "";
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
		        
		        if(StringUtil.isValidStr(bizCode)&&StringUtil.isValidStr(bizFlag)){
		        	if("1".equals(bizFlag)){
		        		queryHotelForWebBean.setBizZone(bizCode);
		        		bizCodeList=bizCode.split(",");
		        	}
		        	if("2".equals(bizFlag)){
		        		queryHotelForWebBean.setDistrict(bizCode);
		        	}
		        	
		        }
		        /* 获取酒店id */
	            if (!gisLst.equals("") && null != gisLst) {
	                hotelIdLst = hotelManageWeb.getHotelIdLst(gisLst);
	                queryHotelForWebBean.setHotelIdLst(hotelIdLst);
	            }
		        queryHotelForWebBean.setCityId(cityCode);
//			      网站改版优化 是否忽略count总数,1:忽略;0:不忽略,默认是0 add by chenjiajie 2009-12-16
		        queryHotelForWebBean.setIgnorePageCount(1);
		        
		        //网站改版优化 把查询条件放到Session中 异步取页数的时候直接从Session中取 add by chenjiajie 2009-12-21
		        setWebBeanFromSession(queryHotelForWebBean);
		        try {
					hotelPageForWebBean = b2bInterfaceService.queryHotelsForWeb(queryHotelForWebBean);
				} catch (Exception e) {
					log.error("HotelQueryAction b2bInterfaceService.queryHotelsForWeb 报错"+e.getMessage(),e);
				}
		        	
		        webHotelResultLis = hotelPageForWebBean.getList();
		        inDate = DateUtil.dateToString(queryHotelForWebBean.getInDate());
		        outDate = DateUtil.dateToString(queryHotelForWebBean.getOutDate());
		        lisNum = webHotelResultLis.size();
				String path = request.getRealPath("/");
				gisMaps = hotelManageWeb.queryMapskmlGenerator(webHotelResultLis, 0, 0, path);
		  
		} catch (ServiceException e) {
			log.error("电子地图拖动数据异常! : "+e);
		}
		if("shieldComission".equals(flag_userNoLogin)){
			return "success-ow";
		}
		return SUCCESS;
	}
	public double getNeLat() {
		return neLat;
	}
	public void setNeLat(double neLat) {
		this.neLat = neLat;
	}
	public double getNeLng() {
		return neLng;
	}
	public void setNeLng(double neLng) {
		this.neLng = neLng;
	}
	public double getNwLat() {
		return nwLat;
	}
	public void setNwLat(double nwLat) {
		this.nwLat = nwLat;
	}
	public double getNwLng() {
		return nwLng;
	}
	public void setNwLng(double nwLng) {
		this.nwLng = nwLng;
	}
	public double getSeLat() {
		return seLat;
	}
	public void setSeLat(double seLat) {
		this.seLat = seLat;
	}
	public double getSeLng() {
		return seLng;
	}
	public void setSeLng(double seLng) {
		this.seLng = seLng;
	}
	public double getSwLat() {
		return swLat;
	}
	public void setSwLat(double swLat) {
		this.swLat = swLat;
	}
	public double getSwLng() {
		return swLng;
	}
	public void setSwLng(double swLng) {
		this.swLng = swLng;
	}
	public GisService getGisService() {
		return gisService;
	}
	public void setGisService(GisService gisService) {
		this.gisService = gisService;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public HotelPageForWebBean getHotelPageForWebBean() {
		return hotelPageForWebBean;
	}
	public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
		this.hotelPageForWebBean = hotelPageForWebBean;
	}
	
	public int getLisNum() {
		return lisNum;
	}
	public void setLisNum(int lisNum) {
		this.lisNum = lisNum;
	}
	public int getNightNum() {
		return nightNum;
	}
	public void setNightNum(int nightNum) {
		this.nightNum = nightNum;
	}
	public String getQueryCityId() {
		return queryCityId;
	}
	public void setQueryCityId(String queryCityId) {
		this.queryCityId = queryCityId;
	}
	public QueryHotelForWebBean getQueryHotelForWebBean() {
		return queryHotelForWebBean;
	}
	public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
		this.queryHotelForWebBean = queryHotelForWebBean;
	}
	public int getToPageIndex() {
		return toPageIndex;
	}
	public void setToPageIndex(int toPageIndex) {
		this.toPageIndex = toPageIndex;
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
	public String[] getBizCodeList() {
		return bizCodeList;
	}
	public void setBizCodeList(String[] bizCodeList) {
		this.bizCodeList = bizCodeList;
	}
	public String getBizFlag() {
		return bizFlag;
	}
	public void setBizFlag(String bizFlag) {
		this.bizFlag = bizFlag;
	}
	public String getPriceFlag() {
		return priceFlag;
	}
	public void setPriceFlag(String priceFlag) {
		this.priceFlag = priceFlag;
	}
	public String getAgent_imgUrl() {
		return agent_imgUrl;
	}
	public IB2bInterfaceService getB2bInterfaceService() {
		return b2bInterfaceService;
	}
	public void setB2bInterfaceService(IB2bInterfaceService interfaceService) {
		b2bInterfaceService = interfaceService;
	}
	public String getFlag_userNoLogin() {
		return flag_userNoLogin;
	}
	public void setFlag_userNoLogin(String flag_userNoLogin) {
		this.flag_userNoLogin = flag_userNoLogin;
	}
	public void setAgent_imgUrl(String agent_imgUrl) {
		this.agent_imgUrl = agent_imgUrl;
	}
	public void setFlag_showCommission(String flag_showCommission) {
		this.flag_showCommission = flag_showCommission;
	}
	
}
