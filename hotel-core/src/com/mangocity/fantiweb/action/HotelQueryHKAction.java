package com.mangocity.fantiweb.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.fantiweb.service.FantiWebTransactService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;

public class HotelQueryHKAction extends GenericWebAction{

	/**
	 * 查询香港酒店方法
	 */
	private static final long serialVersionUID = 6806329884257246789L;
	
	private HotelManageWeb hotelManageWeb;
	
	private QueryHotelForWebBean queryHotelForWebBean;
	
	private HotelPageForWebBean hotelPageForWebBean;
	
	private int nightNum;
	
	private String queryCityId;
	
	private int toPageIndex;
	
	private List webHotelResultLis;
	
	private int lisNum;
	
	private String cityId;
	
	private String cityCode;
	
	private String inDate;
	
	private String outDate;
//	商业区
	private List businessLis;
	
	private int buNum ;
	
	private int endNum;
	
	private String bizCode;
	private String bizName;
	
	private String bizFlag;
	private String priceFlag;
	private String[] bizCodeList;
	
	private String forWord;
	
	private String cityComparisonId;
	
	//存放我浏览过的酒店的hotelId、hotelName
	private List hotelNameAndIdStr = new ArrayList();
	
	private FantiWebTransactService fantiWebTransactService; // add by diandian.hou	
	
	public String execute() throws Exception{
		
		
		 Map params = super.getParams();
	        if (null == queryHotelForWebBean) {
	        	queryHotelForWebBean = new QueryHotelForWebBean();
	        }
	        MyBeanUtil.copyProperties(queryHotelForWebBean, params);
		
	       
	        //酒店详情页面搜查对应城市中的商业区或行政区酒店、默认开始日期为当天，结束日期为当天之后一天
	        if("1".equals(queryHotelForWebBean.getDateIsNotUse())){
          	 	queryHotelForWebBean.setInDate(DateUtil.getDate(DateUtil.dateToString(DateUtil.getSystemDate())));
          	 	queryHotelForWebBean.setOutDate(DateUtil.getDate(DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1))));
	        }
	        Date checkinDate = queryHotelForWebBean.getInDate();
	        Date checkoutDate = queryHotelForWebBean.getOutDate();
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
	        
	        if(StringUtil.isValidStr(bizCode)&&StringUtil.isValidStr(bizFlag)){
	        	if("1".equals(bizFlag)){
	        		queryHotelForWebBean.setBizZone(bizCode);
	        		bizCodeList=bizCode.split(",");
	        	}
	        	if("2".equals(bizFlag)){
	        		queryHotelForWebBean.setDistrict(bizCode);
	        		queryHotelForWebBean.setBizZone("");
	        	}
	        	
	        }
//		      两种星级参数组装
	        String hotelStar1 = queryHotelForWebBean.getFormalStarLevel();
	        String hotelStar2 = queryHotelForWebBean.getInformalStarLevel();
	        queryHotelForWebBean.setHotelStar((StringUtil.isValidStr(hotelStar1) ? hotelStar1 + ","
	            : "")
	            + (StringUtil.isValidStr(hotelStar2) ? hotelStar2 : ""));
	        
	        queryHotelForWebBean.setCityId(cityCode);
            /*使用json前用 modify by chenjiajie 2010-03-25*/
	        if(cityCode.equals("HKG") || cityCode.equals("MAC")){
	        	forWord = "HKquery";
	        }else{
	        	forWord = "query";
	        }
	        /*使用json后用query-lite add by chenjiajie 2010-03-25*/
            //forWord = "query-lite";
	        
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
//	      为异步分页赋值 ,如果在结果页面重新改变城市,则初始页为1 add by haibo.li 2009-12-24
	        if(cityComparisonId!=null && !cityComparisonId.equals("")){
	        	if(!cityComparisonId.equals(cityCode)){
	        		queryHotelForWebBean.setPageIndex(1);
	        		//queryHotelForWebBean.setPageNo(1);
	        	}
	        }
	        
	        //网站改版优化 是否忽略count总数,1:忽略;0:不忽略,默认是0 add by chenjiajie 2009-12-16
	        queryHotelForWebBean.setIgnorePageCount(1);
	        
	        //网站改版优化 把查询条件放到Session中 异步取页数的时候直接从Session中取 add by chenjiajie 2009-12-21
	        setWebBeanFromSession(queryHotelForWebBean);
	        
//	        hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
	     // 价格繁体网站的渠道 add by diandian.hou
			try {
	             hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
	        
	        
	        webHotelResultLis = hotelPageForWebBean.getList();        
	        inDate = DateUtil.dateToString(queryHotelForWebBean.getInDate());
	        outDate = DateUtil.dateToString(queryHotelForWebBean.getOutDate());
	        lisNum = webHotelResultLis.size();	
	        hotelNameAndIdStr = super.findCookies();
	     // 把币种为澳门币的转为HK add by diandian.hou
	        fantiWebTransactService.addRoomTypeCurrency(webHotelResultLis, CurrencyBean.HKD);
			fantiWebTransactService.showToHK(webHotelResultLis, null);
	      //优惠立减用港币显示
			fantiWebTransactService.changePriceInHKDCurrenyOfBenefit(webHotelResultLis, queryHotelForWebBean.getInDate(), 
					queryHotelForWebBean.getOutDate());
	  //      System.out.println("forWord:"+forWord);
		return forWord ;
	}
	
	
		
	
	
	
	
	
	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}

	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
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

	public int getNightNum() {
		return nightNum;
	}

	public void setNightNum(int nightNum) {
		this.nightNum = nightNum;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
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

	public List getWebHotelResultLis() {
		return webHotelResultLis;
	}

	public void setWebHotelResultLis(List webHotelResultLis) {
		this.webHotelResultLis = webHotelResultLis;
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
	public String getBizName() {
		return bizName;
	}
	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
	public String getForWord() {
		return forWord;
	}
	public void setForWord(String forWord) {
		this.forWord = forWord;
	}
	public String getCityComparisonId() {
		return cityComparisonId;
	}
	public void setCityComparisonId(String cityComparisonId) {
		this.cityComparisonId = cityComparisonId;
	}
	
	public void setFantiWebTransactService(
			FantiWebTransactService fantiWebTransactService) {
		this.fantiWebTransactService = fantiWebTransactService;
	}
}
