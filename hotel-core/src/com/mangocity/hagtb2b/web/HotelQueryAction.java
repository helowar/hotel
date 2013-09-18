package com.mangocity.hagtb2b.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hagtb2b.service.IB2bInterfaceService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.util.action.GenericWebAction;

public class HotelQueryAction extends GenericWebAction{

	
	private static final long serialVersionUID = -8331487657049289041L;
	
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
	
	//商业区
	private List businessLis;
	
	private int buNum ;
	
	private int endNum;
	
	private String bizCode;
	private String bizName;
	private String bizFlag;
	
	//处理seo传来的商业区
	private String bizZone;
	
	//存放我浏览过的酒店的hotelId、hotelName
	private List hotelNameAndIdStr = new ArrayList();
	private String priceFlag;
	private String[] bizCodeList;
	private String hotelName;
	private String forWord;
	private String hotelIdLst;
	private String cityComparisonId;
	private String  flag_userNoLogin;//游客使用，其值为shieldComission
	private String agent_imgUrl;
	private String flag_showCommission ="0"; //代理用户是否显示佣金，1表示显示；flag_showComission;
	//private String  flagShowComission ="shieldComission";//打开即跳到预订弹出图表
	
	private IB2bInterfaceService b2bInterfaceService;
	
	
	public IB2bInterfaceService getB2bInterfaceService() {
		return b2bInterfaceService;
	}

	public void setB2bInterfaceService(IB2bInterfaceService interfaceService) {
		b2bInterfaceService = interfaceService;
	}

	public String getCityComparisonId() {
		return cityComparisonId;
	}



	public void setCityComparisonId(String cityComparisonId) {
		this.cityComparisonId = cityComparisonId;
	}



	public String getForWord() {
		return forWord;
	}



	public void setForWord(String forWord) {
		this.forWord = forWord;
	}



	public int getEndNum() {
		return endNum;
	}



	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}



	public int getBuNum() {
		return buNum;
	}



	public void setBuNum(int buNum) {
		this.buNum = buNum;
	}



	public String execute(){
		//WebUtils.setAgentCode(super.request, super.getHttpResponse());
		//用于测试获取B2B代理  根据cookie获取会员信息
		if(!"shieldComission".equals(flag_userNoLogin)){
		   if(!super.checkLogin()){
			  setErrorMessage("您未登录，请先登录！");
			  return "forwardToError";
		  }
		}
		//标示是否显示佣金，1显示，其他值不显示
		flag_showCommission = CookieUtils.getCookieValue(request, "showCommissionFlag");
		log.info("HotelQueryAction flag_showCommission====="+flag_showCommission);
		
		 Map params = super.getParams();
	        if (null == queryHotelForWebBean) {
	        	queryHotelForWebBean = new QueryHotelForWebBean();
	        }
	        MyBeanUtil.copyProperties(queryHotelForWebBean, params);
	        
	        //处理seo发过来的请求  add by haibo.li begin
	        if(null == queryHotelForWebBean.getInDate() && null==queryHotelForWebBean.getOutDate()){
	        	queryHotelForWebBean.setInDate(DateUtil.getDate(DateUtil.dateToString(DateUtil
						.getDate(DateUtil.getSystemDate(), 1))));
	        	queryHotelForWebBean.setOutDate(DateUtil.getDate(DateUtil.dateToString(DateUtil
						.getDate(DateUtil.getSystemDate(), 2))));
	        }
	        if(null !=hotelName && !hotelName.equals("")){
					queryHotelForWebBean.setHotelName(request.getParameter("hotelName"));
	        }
	        if(null !=bizCode && !bizCode.equals("")){
	        	queryHotelForWebBean.setBizZone(bizCode);
	        }
	        //处理seo发过来的请求  add by haibo.li end
	       
	        
	        //酒店详情页面搜查对应城市中的商业区或行政区酒店、默认开始日期为当天，结束日期为当天之后一天
	        if("1".equals(queryHotelForWebBean.getDateIsNotUse())){
           	 	queryHotelForWebBean.setInDate(DateUtil.getDate(DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1))));
                queryHotelForWebBean.setOutDate(DateUtil.getDate(DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 2))));
            }
	        Date checkinDate = queryHotelForWebBean.getInDate();
	        Date checkoutDate = queryHotelForWebBean.getOutDate();
//	        // 城市ID,入住和离店日期不能为空
//	        if (null == inDate || null == inDate
//	            || !StringUtil.isValidStr(queryHotelForWebBean.getCityId())) {
//	            return "";
//	        }
	        if(null!=cityCode && !cityCode.equals("") ){
	        	cityCode = cityCode.toUpperCase();//转换成大写
	        }
	        String city = InitServlet.cityObj.get(cityCode);
	        if(null !=city && !city.equals("")){
	        	queryHotelForWebBean.setCityName(city);
	        }else{
	        	return super.forwardError("非常抱歉,您查询的结果没有");
	        }
	        if(cityCode.equals("HKG") || cityCode.equals("MAC")){
	        	if("shieldComission".equals(flag_userNoLogin)){
	        		forWord = "HKquery-ow";
	        	}else{
	        	     forWord = "HKquery";
	        	}
	        }else{
	        	if( "shieldComission".equals(flag_userNoLogin)){
	        		forWord = "query-ow";
	        	}else{	
	        		forWord = "query";
	        	}
	        	
	        }
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
	        		queryHotelForWebBean.setBizZone("");
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
	        //为异步分页赋值 ,如果在结果页面重新改变城市,则初始页为1 add by haibo.li 2009-12-24
	        if(cityComparisonId!=null && !cityComparisonId.equals("")){
	        	if(!cityComparisonId.equals(cityCode)){
	        		queryHotelForWebBean.setPageIndex(1);
	        		//queryHotelForWebBean.setPageNo(1);
	        	}
	        }
	        
	        //设置代码账号 add by yong.zeng 2010.3.23
	        String agentCodeCook = CookieUtils.getCookieValue(request,"agentCode");
	        queryHotelForWebBean.setB2bCd(agentCodeCook);
	        
	        //网站改版优化 是否忽略count总数,1:忽略;0:不忽略,默认是0 add by chenjiajie 2009-12-16
	        queryHotelForWebBean.setIgnorePageCount(1);
	        
	        //网站改版优化 把查询条件放到Session中 异步取页数的时候直接从Session中取 add by chenjiajie 2009-12-21
	        setWebBeanFromSession(queryHotelForWebBean);
	        
	        //TODO:delete debug
	        long beginTime = System.currentTimeMillis();
	        try {
	        	//hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
				hotelPageForWebBean = b2bInterfaceService.queryHotelsForWeb(queryHotelForWebBean);
			} catch (Exception e) {
				log.error("HotelQueryAction b2bInterfaceService.queryHotelsForWeb 报错"+e.getMessage(),e);
				e.printStackTrace();
			}
	        //TODO:delete debug
	        long endTime = System.currentTimeMillis();
	        log.info("==========+++++++++debug 003:城市:"+queryHotelForWebBean.getCityId()+";本次总查询共用" + (endTime - beginTime) + "毫秒+++++++++==========");
	        	
	        webHotelResultLis = hotelPageForWebBean.getList();
	        inDate = DateUtil.dateToString(queryHotelForWebBean.getInDate());
	        outDate = DateUtil.dateToString(queryHotelForWebBean.getOutDate());
	        lisNum = webHotelResultLis.size();
	        hotelNameAndIdStr = super.findCookies();	
	        
	        //设置城市名称和城市三字码 add by shengwei.zuo 2009-11-27
	        setCityCookie();
	        
		return forWord ;
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
			log.error(e.getMessage(),e);
		}
    	
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




	public HotelPageForWebBean getHotelPageForWebBean() {
		return hotelPageForWebBean;
	}

	public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
		this.hotelPageForWebBean = hotelPageForWebBean;
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

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
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



	public List getBusinessLis() {
		return businessLis;
	}



	public void setBusinessLis(List businessLis) {
		this.businessLis = businessLis;
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



	public String[] getBizCodeList() {
		return bizCodeList;
	}



	public void setBizCodeList(String[] bizCodeList) {
		this.bizCodeList = bizCodeList;
	}



	public String getPriceFlag() {
		return priceFlag;
	}



	public void setPriceFlag(String priceFlag) {
		this.priceFlag = priceFlag;
	}



	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}



	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}



	public String getHotelName() {
		return hotelName;
	}



	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}



	public String getBizZone() {
		return bizZone;
	}



	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}



	public String getBizName() {
		return bizName;
	}



	public void setBizName(String bizName) {
		this.bizName = bizName;
	}



	public String getHotelIdLst() {
		return hotelIdLst;
	}



	public void setHotelIdLst(String hotelIdLst) {
		this.hotelIdLst = hotelIdLst;
	}

	public String getFlag_showCommission() {
		return flag_showCommission;
	}



	public String getAgent_imgUrl() {
		return agent_imgUrl;
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
