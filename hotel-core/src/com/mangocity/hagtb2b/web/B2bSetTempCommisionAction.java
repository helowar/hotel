package com.mangocity.hagtb2b.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mangocity.hagtb2b.persistence.HtlB2bTempComminfo;
import com.mangocity.hagtb2b.persistence.HtlB2bTempComminfoItem;
import com.mangocity.hagtb2b.service.IB2bService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.web.CookieUtils;

public class B2bSetTempCommisionAction extends GenericCCAction {

	private String commisionTempName;
	
	private  String operateFlag;
	
	private static final String UPDATE_OPERATE = "update";

	private String commisionTempRemark;
	
	private List<HtlB2bTempComminfoItem> hotelComTempList;

	private IB2bService b2bService;
	
	private Long hotelId;
	
	private HtlHotel hotel;
	
	private HotelManage hotelManage;
	
	private HotelRoomTypeService hotelRoomTypeService;
	
	private String backURL;
	
	private List lstRoomType = new ArrayList();
	
	private List roomTypePriceTypeLis = new ArrayList();
	
	private int priceRowNum;
	
	private String beginDate;
	
	private String endDate;
	
	private long tempId;
	
	// 提示信息
	private String ajaxTip;

	private HtlB2bTempComminfo htlB2bTempInfo;
	
	/**
     * 资源接口
     */
    private ResourceManager resourceManager;
    
    private static final String ALERT_MSG = "alertMsg";

	public String createTempCommision() {

		return "addTempComm";
	}

	public String saveOrUpdate() {
		//WebUtils.setAgentCode(super.request, super.getHttpResponse());
		// 判断是否已经登录系统
		if (!super.checkLogin()) {
			setErrorMessage("您未登录，请先登录！");
			return "forwardToError";
		}
	
		//封装并保存模板信息
		String status =saveCommissionTemp();
		if("sucess".equals(status)){
			return "queryTemp";
		}else{
			if("alreadyExist".equals(status)){
				ajaxTip="模板名称已经存在请重新修改！";
				return ALERT_MSG;
				
			}else{
				ajaxTip="模板保存失败！";
				return ALERT_MSG;
			}
		}
		
	}
	
	public String saveOrUpdateAndAddComItem() {
		//WebUtils.setAgentCode(super.request, super.getHttpResponse());
		// 判断是否已经登录系统
		if (!super.checkLogin()) {
			setErrorMessage("您未登录，请先登录！");
			return "forwardToError";
		}
		//封装并保存模板信息
		String status =saveCommissionTemp();
		if("sucess".equals(status)){
			return "chooseHotelList";
		}else{
			if("alreadyExist".equals(status)){
				ajaxTip="模板名称已经存在请重新修改！";
				return ALERT_MSG;
				
			}else{
				ajaxTip="模板保存失败！";
				return ALERT_MSG;
			}
		}
	}
	
	/**
	 * 创建Action
	 * @return
	 */
	public String create(){
        if (null != hotelId) {
            hotel = hotelManage.findHotel(hotelId);//酒店
            lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());//房型
            roomTypePriceTypeLis = hotelManage.findRoomTypePriceTypeLis(hotelId);//价格类型
        }
		String tempUrl = request.getRequestURL().toString();
        if(request.getQueryString()!=null)    
        	tempUrl+="?"+request.getQueryString();  
		backURL = tempUrl;
        return "create";
	}
	
	public String viewAdjustLstByB2bCd() {

		hotelComTempList = b2bService.getCommTempByHotel(hotelId,tempId);
		hotel = hotelManage.findHotel(hotelId);// 酒店
		roomTypePriceTypeLis = hotelManage.findRoomTypePriceTypeLis(hotelId);// 价格类型

		return "view";
	}
	
	/**
	 * 保存佣金调整
	 * 
	 * @return
	 */
	public String saveHotelCommItem() {

		// dateComponent中含有当前要提交对象的日期区间
		// dateCops是根据b2bcd,hotelid,roomtypeid,pricetypeid,payMethod从数据库里取出的List,然后封装成DateComponent对象,DateComponent主要包括开始结束日期和ID
		// resultMap会得到以"remove"和"update"为key的Map,需要将remove里的对象对应的实体删除掉,将update里的对象进行组合进行保存，其中ID不为空的就是更新，为空的就是增加．
		// 防止重复提交
		if (isRepeatSubmit()) {
			return forwardError("请不要重复提交,谢谢!");
		}

		String loginId = CookieUtils.getCookieValue(request, "operaterId", 1);
		loginId = loginId == null ? "" : loginId;
		Map params = super.getParams();

		Map<String, List<DateComponent>> resultMap = new HashMap();

		List<HtlB2bTempComminfoItem> dateCops = new ArrayList();

		List<HtlB2bTempComminfoItem> newLst = new ArrayList();

		hotelComTempList = MyBeanUtil.getBatchObjectFromParam(params,
				HtlB2bTempComminfoItem.class, priceRowNum);

		float hotelStar = Float.valueOf(params.get("hotelStar").toString());

		if (hotelStar > 10) {
			String strHotelStar = resourceManager.getDescription(
					"res_hotelStarToNum", Math.round(hotelStar));
			hotelStar = Float.parseFloat(strHotelStar);
		}
		int intStar = Math.round(hotelStar);

		for (HtlB2bTempComminfoItem hl : hotelComTempList) {
			// 对ca进行数据补充
			hl.setHotelId(hotelId);
			hl.setBeginDate(DateUtil.getDate(beginDate));
			hl.setEndDate(DateUtil.getDate(endDate));
			hl.setHotelStar(String.valueOf(intStar));
			String temp = hl.getRoomAndPricetypeTemp();
			if (null != hl && !"".equals(hl)) {
				String arr[] = temp.split("&&");
				hl.setRoomtypeId(Long.parseLong(arr[0]));
				hl.setChileRoomtypeId(Long.parseLong(arr[1]));
			}
			// ca.setCreateBy(opName);
			hl.setTempId(tempId);
			hl.setActive(1);
			hl.setCreateId(loginId);
			hl.setCreateName(loginId);
			hl.setCreateTime(new Date());
			hl.setModifyId(loginId);
			hl.setModifyName(loginId);
			hl.setModifyTime(new Date());
			HtlB2bTempComminfoItem newRecord = new HtlB2bTempComminfoItem();
			try {
				BeanUtils.copyProperties(newRecord, hl);
				newLst.add(newRecord);
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				return super.forwardError("B2bAgentCommissionAdjustAction保存错误"
						+ ex.getMessage());
				
			}
		}// end inner for

		for (HtlB2bTempComminfoItem nca : newLst) {

			// 根据ca得到当前database中重复的日期区间
			dateCops = b2bService.getDateCops(nca);
			try {
				if (null != dateCops && !dateCops.isEmpty()) {
					b2bService.adjustUtil(nca, dateCops);
				} else {
					b2bService.updateCommAdjust(nca);
				}
			} catch (Exception ex) {
				log.error("B2bAgentCommissionAdjustAction保存错误"
						+ ex.getMessage(), ex);
				return super.forwardError("B2bAgentCommissionAdjustAction保存错误"
						+ ex.getMessage());
			}

		}

		return "chooseHotelList";
	}
	
	/**
	 * 修改模板
	 * @return
	 */
	public String editCommission(){
		if (!super.checkLogin()) {
			setErrorMessage("您未登录，请先登录！");
			return "forwardToError";
		}
		if(tempId>0){
			htlB2bTempInfo =b2bService.getcommTempTempId(tempId);
		}
		return "addTempComm";
	}
	
	/**
	 * 查看模板
	 * @return
	 */
	public String queryCommTemp(){
		if (!super.checkLogin()) {
			setErrorMessage("您未登录，请先登录！");
			return "forwardToError";
		}
		try{
			if(tempId>0){
				hotelComTempList = b2bService.getcommTempItemByTempId(tempId);
				htlB2bTempInfo =b2bService.getcommTempTempId(tempId);
				if(null != hotelComTempList && !hotelComTempList.isEmpty()){
					hotelComTempList = b2bService.fillHotelNameAndPriceTypeName(hotelComTempList);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("B2bSetTempCommisionAction queryCommTemp出错"+ex.getMessage(),ex);	
		}
		
		return "viewTemp";
	}
	
	/**
	 * 删除模板
	 * @return
	 */
	public String deleteCommTemp(){
		if (!super.checkLogin()) {
			setErrorMessage("您未登录，请先登录！");
			return "forwardToError";
		}
		if(tempId>0){
			String loginId = CookieUtils.getCookieValue(request, "operaterId",
					1);
			b2bService.deleteCommTempByTempId(tempId,"",loginId);
			
		}
		return "queryTemp";
	}
	
	private String saveCommissionTemp(){
		try {
			// 通过cookie获取登录人的中文名
			
			if(!UPDATE_OPERATE.equals(operateFlag) && b2bService.checkTempNameIsExist(commisionTempName)){
				return "alreadyExist";
			}else{
				String loginId = CookieUtils.getCookieValue(request, "operaterId",
						1);
				HtlB2bTempComminfo htlB2bTempComminfo = new HtlB2bTempComminfo();
				if(tempId >0){
					htlB2bTempComminfo.setId(tempId);
				}
				htlB2bTempComminfo.setCommisionTempName(commisionTempName);
				htlB2bTempComminfo.setRemark(commisionTempRemark);
				htlB2bTempComminfo.setActive(1);
				htlB2bTempComminfo.setCreateId(loginId);
				htlB2bTempComminfo.setCreateName(loginId);
				htlB2bTempComminfo.setCreateTime(new Date());
				htlB2bTempComminfo.setModifyId(loginId);
				htlB2bTempComminfo.setModifyName(loginId);
				htlB2bTempComminfo.setModifyTime(new Date());
				b2bService.saveOrUpdateComTemp(htlB2bTempComminfo);
				tempId =htlB2bTempComminfo.getId();
				return "sucess";
			}
			
		} catch (Exception e) {
			log.error("b2b佣金模板设置保存或修改出错"+e.getMessage(),e);
			return "error";
		}
	}
	
	 /**
     * 处理重复提交
     */
    private boolean isRepeatSubmit() {
        String strutsToken = (String) getParams().get("struts.token");
        String sessionToken = (String) getFromSession("struts.token.session");
        if (StringUtil.StringEquals2(strutsToken, sessionToken)) {
            return true;
        }
        putSession("struts.token.session", strutsToken);
        return false;
    }
    
	public String getCommisionTempName() {
		return commisionTempName;
	}

	public void setCommisionTempName(String commisionTempName) {
		this.commisionTempName = commisionTempName;
	}

	public String getCommisionTempRemark() {
		if (null == commisionTempRemark)
			return "";
		else
			return commisionTempRemark;
	}

	public void setCommisionTempRemark(String commisionTempRemark) {
		this.commisionTempRemark = commisionTempRemark;
	}

	public IB2bService getB2bService() {
		return b2bService;
	}

	public void setB2bService(IB2bService service) {
		b2bService = service;
	}

	public List<HtlB2bTempComminfoItem> getHotelComTempList() {
		return hotelComTempList;
	}

	public void setHotelComTempList(List<HtlB2bTempComminfoItem> hotelComTempList) {
		this.hotelComTempList = hotelComTempList;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public HtlHotel getHotel() {
		return hotel;
	}

	public void setHotel(HtlHotel hotel) {
		this.hotel = hotel;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public String getBackURL() {
		return backURL;
	}

	public void setBackURL(String backURL) {
		this.backURL = backURL;
	}

	public List getLstRoomType() {
		return lstRoomType;
	}

	public void setLstRoomType(List lstRoomType) {
		this.lstRoomType = lstRoomType;
	}

	public List getRoomTypePriceTypeLis() {
		return roomTypePriceTypeLis;
	}

	public void setRoomTypePriceTypeLis(List roomTypePriceTypeLis) {
		this.roomTypePriceTypeLis = roomTypePriceTypeLis;
	}

	public int getPriceRowNum() {
		return priceRowNum;
	}

	public void setPriceRowNum(int priceRowNum) {
		this.priceRowNum = priceRowNum;
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

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public long getTempId() {
		return tempId;
	}

	public void setTempId(long tempId) {
		this.tempId = tempId;
	}

	public HtlB2bTempComminfo getHtlB2bTempInfo() {
		return htlB2bTempInfo;
	}

	public void setHtlB2bTempInfo(HtlB2bTempComminfo htlB2bTempInfo) {
		this.htlB2bTempInfo = htlB2bTempInfo;
	}

	public String getAjaxTip() {
		return ajaxTip;
	}

	public void setAjaxTip(String ajaxTip) {
		this.ajaxTip = ajaxTip;
	}

	public String getOperateFlag() {
		return operateFlag;
	}

	public void setOperateFlag(String operateFlag) {
		this.operateFlag = operateFlag;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
