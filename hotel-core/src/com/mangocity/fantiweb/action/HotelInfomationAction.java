package com.mangocity.fantiweb.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.fantiweb.service.FantiWebTransactService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPicture;
import com.mangocity.hotel.base.persistence.HtlPictureInfo;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelAdditionalServe;
import com.mangocity.hweb.persistence.HotelInfoForNewWeb;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.HwClickAmount;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;

public class HotelInfomationAction extends GenericCCAction {

	private long hotelId;

	// 入住日期
	private String inDate;

	// 退房日期
	private String outDate;

	// 城市id
	private String cityId;
	
	private String cityCode;

	// 电子地图城市编码
	private String cityMapCode;

	private HotelManage hotelManage;

	private HotelInfoForWeb hotelInfoForWeb;

	private HotelManageWeb hotelManageWeb;

	private QueryHotelForWebResult hotelInfo = new QueryHotelForWebResult();

	private HotelInfoForNewWeb hotelInfoForNewWeb = new HotelInfoForNewWeb();

	protected IHotelService hotelService;

	private List hotelPictureList;

	// 酒店附加服务
	private HotelAdditionalServe additionalServe = new HotelAdditionalServe();

	// 酒店查询条件
	private QueryHotelForWebBean queryHotelForWebBean;

	// 酒店本部系统[酒店3D图片信息列表]里有图片信息时
	private boolean hasPicture = false;
	
	private int selectBut=1;
	
	private int pictureCount = 4;
	
	private FantiWebTransactService fantiWebTransactService;//add by diandian.hou 2010-8-20

	public int getPictureCount() {
		return pictureCount;
	}

	public void setPictureCount(int pictureCount) {
		this.pictureCount = pictureCount;
	}

	/**
	 * 
	 */
	public String execute() {

		Map params = super.getParams();
		if (null == queryHotelForWebBean) {
			queryHotelForWebBean = new QueryHotelForWebBean();
		}
		MyBeanUtil.copyProperties(queryHotelForWebBean, params);
		// 查询酒店基本信息：中文名、星级
		hotelInfoForWeb = hotelManageWeb.queryHotelInfoForWeb(hotelId);
		if (null != hotelInfoForWeb) {
			//存放附加服务字符串
			String serviceStr = "";
			
			queryHotelForWebBean.setHotelName(hotelInfoForWeb.getHotelName());
			queryHotelForWebBean.setCityId(hotelInfoForWeb.getCity());
			
			//把日期拆分为14：00   add by haibo.li 网站改版
			if(hotelInfoForWeb.getCheckInTime()!=null && !hotelInfoForWeb.getCheckInTime().equals("") && hotelInfoForWeb.getCheckOutTime()!=null && !hotelInfoForWeb.getCheckOutTime().equals("")){
				hotelInfoForWeb.setCheckInTime(DateUtil.subStrTime(hotelInfoForWeb.getCheckInTime()));
				hotelInfoForWeb.setCheckOutTime(DateUtil.subStrTime(hotelInfoForWeb.getCheckOutTime()));
			}
			List hotelNameAndIdLis = new ArrayList();	
			/** ==增加酒店点击量记录Begin add by Chenjiajie==* */
			String ipAddress = request.getRemoteAddr();
			addHwClickAmount(hotelId, ipAddress);
			/** ==增加酒店点击量记录End add by Chenjiajie==* */
			
			//对cookie的值进行处理
			hotelNameAndIdLis = addCookies(hotelId);
			// 城市ID,入住和离店日期不能为空
			//如果查询日期空，则默认为今天，离店日期默认为第二天
//			if(null == queryHotelForWebBean.getInDate() && null ==queryHotelForWebBean.getOutDate()){
//				queryHotelForWebBean.setInDate(DateUtil.getDate(new Date(),1));
//				queryHotelForWebBean.setOutDate(DateUtil.getDate(new Date(),2));
//				//queryHotelForWebBean.setInDate( DateUtil.getDateTwo(new Date()));
//				//queryHotelForWebBean.setOutDate(DateUtil.getNextDate(new Date()));
//			}
			
			
			
			Date checkIn = queryHotelForWebBean.getInDate();
			Date checkOut = queryHotelForWebBean.getOutDate();
			HtlHotel hotel = hotelManage.findHotel(hotelId);
			if(null!=hotel){
				queryHotelForWebBean.setCityId(hotel.getCity());
			}else{
				return super.forwardError("你查询的酒店不存在");
			}
			
			
			//酒店详情页面，点击我预览过的酒店、行政区酒店、商业区酒店的链接，均默认查询当天。
			if ("1".equals(queryHotelForWebBean.getDateIsNotUse())
					|| null == checkIn || null == checkOut) {
				checkIn = DateUtil.getDate(DateUtil.dateToString(DateUtil
						.getDate(DateUtil.getSystemDate(), 1)));
				checkOut = DateUtil.getDate(DateUtil.dateToString(DateUtil
						.getDate(DateUtil.getSystemDate(), 2)));
				queryHotelForWebBean.setInDate(checkIn);
				queryHotelForWebBean.setOutDate(checkOut);
			}

			serviceStr = hotelManageWeb.queryHotelAdditionalServeForHotelInfo(
					hotelId, queryHotelForWebBean.getInDate(),
					queryHotelForWebBean.getOutDate());
			

			
			if (null == checkIn || null == checkOut
					|| !StringUtil.isValidStr(queryHotelForWebBean.getCityId())) {
				return SUCCESS;
			}
			// 查询不能超过28天
			int nightNum = DateUtil.getDay(checkIn, checkOut);
			if (0 >= nightNum || 28 < nightNum) {
				return SUCCESS;
			}
			request.setAttribute("nightNum", nightNum);

			
			// 如果不传城市中文名则根据城市代码设置
			if (!StringUtil.isValidStr(queryHotelForWebBean.getCityName())) {
				queryHotelForWebBean.setCityName(InitServlet.cityObj
						.get(queryHotelForWebBean.getCityId()));
			}
			//拿出酒店所在城市的商业区和行政区数据
			Map map=hotelManageWeb.queryBusinessForCityId(queryHotelForWebBean.getCityName());
			List districtList=(List) map.get("district");
			List businessList=(List) map.get("business");
			//存放cookie中保存的酒店id对应的中文名跟id值
			
			// 根据hotel,inDate,outDate查询酒店房型价格
			try{
				HotelPageForWebBean hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
				// 根据酒店ID查询，因此只会返回一家酒店
		        if (null != hotelPageForWebBean.getList() && 0 < hotelPageForWebBean.getList().size()) {
		        	hotelInfo = hotelPageForWebBean.getList().get(0);
		        }
			}catch(Exception e){
				log.error(e.getMessage(),e);
			}
			if((null==hotelInfo.getHotelChnName())||("".equals(hotelInfo.getHotelChnName()))){
				return "notFound";
			}
			
			//给roomType中的currencyValue赋值，用于港澳酒店 add by diandian.hou 2010-8-22
			if(CurrencyBean.HKD.equals(hotelInfo.getCurrency())){
				fantiWebTransactService.addRoomTypeCurrency(hotelInfo, hotelInfo.getCurrency());
			}
			//酒店查询时，把内地酒店预付的价格用港币显示 add by diandian.hou 2010-8-20	
			if(CurrencyBean.RMB.equals(hotelInfo.getCurrency())){
				fantiWebTransactService.showForRMBToHKByOneHotel(hotelInfo, null);
			}
			if(CurrencyBean.MOP.equals(hotelInfo.getCurrency())){
				fantiWebTransactService.showForMOPToHKByOneHotel(hotelInfo, null);
			}

			MyBeanUtil.copyProperties(hotelInfoForNewWeb, hotelInfoForWeb);

			if (!StringUtil.isValidStr(hotelInfoForNewWeb.getDistrictName())) {
				hotelInfoForNewWeb.setDistrictName(InitServlet.businessSozeObj
						.get(hotelInfo.getBizZone()));
			}
			//获取酒店图片信息begin
			HtlPictureInfo htlPictureInfo = new HtlPictureInfo();
			htlPictureInfo.setHotelId(hotelId);
			// 根据hotelId查询图片
			hotelPictureList = hotelManage.queryHotelPicList(htlPictureInfo);

	        /* RMS3124 网站酒店图片展示功能 add by chenjiajie begin 2010-04-07 */
	        List<HtlPicture> htlPictureList = hotelManage.findHtlPicture(hotelId);
	        request.setAttribute("htlPictureList", htlPictureList);
	        /* RMS3124 网站酒店图片展示功能 add by chenjiajie end 2010-04-07 */
	        

			//cookie中的酒店信息
			hotelInfoForNewWeb.setHotelNameAndIdStr(hotelNameAndIdLis);
			//酒店所在城市的商业区列表
			hotelInfoForNewWeb.setBusinessList(businessList);
			//酒店所在城市的行政区列表
			hotelInfoForNewWeb.setDistrictList(districtList);
			hotelInfoForNewWeb.setServiceStr(serviceStr);
			hotelInfoForNewWeb.setCityName(queryHotelForWebBean.getCityName());
			assemblySomeStr(hotelInfoForNewWeb, hotelInfo.getFitmentYear());

		}else{
			return super.forwardError("你查询的酒店不存在");
		}
		cityMapCode = InitServlet.platObj.get(cityId);
		// 根据hotelId查询图片列表

		int picCount = hotelManage.getPictureCountByHotelId(hotelId);
		if (0 < picCount) {
			hasPicture = true;
		}

		inDate = DateUtil.dateToString(queryHotelForWebBean.getInDate());
		outDate = DateUtil.dateToString(queryHotelForWebBean.getOutDate());
		cityCode = queryHotelForWebBean.getCityId();
        
		//优惠立减用港币显示
		fantiWebTransactService.changePriceInHKDCurrenyOfBenefit(hotelInfo, queryHotelForWebBean.getInDate(), queryHotelForWebBean.getOutDate());
		return SUCCESS;

	}

	/**
	 * 组装页面要用到的字符串
	 */

	private void assemblySomeStr(HotelInfoForNewWeb hotelInfoForNewWeb,
			String fitmentYear) {

		if (hotelInfo.getPraciceYear() > 0) {
			hotelInfoForNewWeb.setPraciceAndFitmentStr(hotelInfo
					.getPraciceYear()
					+ "年开业 ");
			if (null != hotelInfo.getFitmentYear()) {

				hotelInfoForNewWeb.setPraciceAndFitmentStr(hotelInfoForNewWeb
						.getPraciceAndFitmentStr()
						+ fitmentYear);

			}

		}

	}

	//对cookie处理
	private List addCookies(long hotelId) {

		List hotelIdCookies = new ArrayList();
		List hotelNameAndIdLis = new ArrayList();
		boolean hasExist = false;
		Cookie[] cookies = super.request.getCookies();
		if(null != cookies && cookies.length>0){
			for (int j = 0; j < cookies.length; j++) {
				if (cookies[j].getName().indexOf("hotelId") > -1) {
					hotelIdCookies.add(cookies[j]);
	
				}
			}
	
			// 对cookie进行排序
			for (int ii = 0; ii < hotelIdCookies.size(); ii++) {
	
				Cookie cookieItems = (Cookie) hotelIdCookies.get(ii);
	
				for (int jj = 1; jj < hotelIdCookies.size(); jj++) {
					Cookie cookieItem = (Cookie) hotelIdCookies.get(jj);
					if (cookieItems.getMaxAge() < cookieItem.getMaxAge()) {
	
						hotelIdCookies.set(ii, cookieItem);
						hotelIdCookies.set(jj, cookieItems);
					}
	
				}
			}
			boolean cookieIsExist = false;
	
			// 判断是否有重复
			for (int k = 0; k < hotelIdCookies.size(); k++) {
				Cookie cook = (Cookie) hotelIdCookies.get(k);
				if (cook.getValue().equals(hotelId + "")) {
					cookieIsExist = true;
				}
	
			}
	
			// 如果存放酒店名称的cookie已经有5个了。再添加的时候要删除第一个cookie里的酒店名称，再添加新的酒店名称到cookie
			if (hotelIdCookies.size() >= 5) {
	
				if (cookieIsExist) {
					String cookieKey = "hotelId" + hotelId;
					Cookie cc = new Cookie(cookieKey, (hotelId + ""));
					cc.setMaxAge(0);
					cc.setPath("/");
					super.getHttpResponse().addCookie(cc);
	
					Cookie c = new Cookie(cookieKey, (hotelId + ""));
					// cookie寿命为1个月
					c.setMaxAge(60 * 24 * 3600);
					c.setPath("/");
					super.getHttpResponse().addCookie(c);
	
				} else {
					// 删除一个cookie
					Cookie cookieIte = (Cookie) hotelIdCookies.get(0);
					String cKey = cookieIte.getName();
					String cValue = cookieIte.getValue();
					Cookie c = new Cookie(cKey, cValue);
					c.setMaxAge(0);
					c.setPath("/");
					super.getHttpResponse().addCookie(c);
	
					// 新增一个cookie
					String ccKey = "hotelId" + hotelId;
					Cookie cc = new Cookie(ccKey, (hotelId + ""));
					// cookie寿命为1个月
					cc.setMaxAge(60 * 24 * 3600);
					cc.setPath("/");
					super.getHttpResponse().addCookie(cc);
	
				}
	
			} else {
	
				// 重复先删后曾
				if (cookieIsExist) {
					String cookieKey = "hotelId" + hotelId;
					Cookie cc = new Cookie(cookieKey, (hotelId + ""));
					cc.setMaxAge(0);
					cc.setPath("/");
					super.getHttpResponse().addCookie(cc);
	
					Cookie c = new Cookie(cookieKey, (hotelId + ""));
					// cookie寿命为1个月
					c.setMaxAge(60 * 24 * 3600);
					c.setPath("/");
					super.getHttpResponse().addCookie(c);
	
				} else {// hotelI的的cookie数量少于5个，直接增加。
					String cookieKey = "hotelId" + hotelId;
					Cookie cc = new Cookie(cookieKey, (hotelId + ""));
					// cookie寿命为1个月
					cc.setMaxAge(60 * 24 * 3600);
					cc.setPath("/");
					super.getHttpResponse().addCookie(cc);
				}
	
			}
	
			// 排序之后封装cookie中的酒店id、酒店中文名，用于在页面显示
			for (int kk = hotelIdCookies.size() - 1; kk >= 0; kk--) {
				String[] forHotel = new String[2];
				Cookie cookieIte = (Cookie) hotelIdCookies.get(kk);
				if (cookieIte.getMaxAge() != 0) {
					String hotelIdd = cookieIte.getValue();
					String hotelName = hotelManageWeb.queryHotelName(Long
							.parseLong(hotelIdd));
					forHotel[0] = hotelIdd;
					forHotel[1] = hotelName;
					hotelNameAndIdLis.add(forHotel);
				} else {
					hotelIdCookies.remove(kk);
				}
	
			}

		}

		return hotelNameAndIdLis;
	}

	private void addHwClickAmount(long hotelId, String ipAddress) {
		HwClickAmount hwClickAmount = new HwClickAmount();
		hwClickAmount.setHotelId(String.valueOf(hotelId));
		hwClickAmount.setIpAddress(ipAddress);
		hwClickAmount.setClickType("1");
		hwClickAmount.setClickDate(DateUtil.getDate(new Date()));
		hotelManageWeb.addClickAmount(hwClickAmount);

	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
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

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityMapCode() {
		return cityMapCode;
	}

	public void setCityMapCode(String cityMapCode) {
		this.cityMapCode = cityMapCode;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public HotelInfoForWeb getHotelInfoForWeb() {
		return hotelInfoForWeb;
	}

	public void setHotelInfoForWeb(HotelInfoForWeb hotelInfoForWeb) {
		this.hotelInfoForWeb = hotelInfoForWeb;
	}

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public QueryHotelForWebResult getHotelInfo() {
		return hotelInfo;
	}

	public void setHotelInfo(QueryHotelForWebResult hotelInfo) {
		this.hotelInfo = hotelInfo;
	}

	public QueryHotelForWebBean getQueryHotelForWebBean() {
		return queryHotelForWebBean;
	}

	public void setQueryHotelForWebBean(
			QueryHotelForWebBean queryHotelForWebBean) {
		this.queryHotelForWebBean = queryHotelForWebBean;
	}

	public boolean isHasPicture() {
		return hasPicture;
	}

	public void setHasPicture(boolean hasPicture) {
		this.hasPicture = hasPicture;
	}

	public IHotelService getHotelService() {
		return hotelService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public HotelAdditionalServe getAdditionalServe() {
		return additionalServe;
	}

	public void setAdditionalServe(HotelAdditionalServe additionalServe) {
		this.additionalServe = additionalServe;
	}

	public List getHotelPictureList() {
		return hotelPictureList;
	}

	public void setHotelPictureList(List hotelPictureList) {
		this.hotelPictureList = hotelPictureList;
	}

	public HotelInfoForNewWeb getHotelInfoForNewWeb() {
		return hotelInfoForNewWeb;
	}

	public void setHotelInfoForNewWeb(HotelInfoForNewWeb hotelInfoForNewWeb) {
		this.hotelInfoForNewWeb = hotelInfoForNewWeb;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public int getSelectBut() {
		return selectBut;
	}

	public void setSelectBut(int selectBut) {
		this.selectBut = selectBut;
	}

	public void setFantiWebTransactService(
			FantiWebTransactService fantiWebTransactService) {
		this.fantiWebTransactService = fantiWebTransactService;
	}	
}
