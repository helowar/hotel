package com.mangocity.hagtb2b.web;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mangocity.hagtb2b.service.IB2bService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.base.persistence.CommisionAdjust;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.assistant.CutDate;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.web.CookieUtils;

public class B2bAgentCommissionAdjustAction extends GenericAction {
	
	

	private IB2bService b2bService;
	private List<CommisionAdjust> priceLst;
	
	private String b2BIdLst;
	
	private String startDate;
	private String endDate;
	private String backURL;
	
	private int priceRowNum;
	
	private Long hotelId;
	private HtlHotel hotel;
	private HotelManage hotelManage;
	
	private HotelRoomTypeService hotelRoomTypeService;
	
	private List lstRoomType = new ArrayList();
	private List roomTypePriceTypeLis = new ArrayList();
	private RoomControlManage roomControlManage;
	
	
    /**
     * 资源接口
     */
    private ResourceManager resourceManager;
    
	public String queryHotel(){
		
		return "queryHotel";
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
	
	
	/**
	 * 保存佣金调整
	 * @return
	 */
	public String saveAdjust(){
        // 防止重复提交
        if (isRepeatSubmit()) {
            return forwardError("请不要重复提交,谢谢!");
        }
        String opLogonID = CookieUtils.getCookieValue(request,"operaterId");
        opLogonID = opLogonID==null ? "" : opLogonID;
        String agentCode = CookieUtils.getCookieValue(request,"agentCode");
		agentCode = agentCode==null ? "" : agentCode;
		
		Map params = super.getParams();
		float hotelStar = Float.valueOf(params.get("hotelStar").toString());
		int intStar = Math.round(hotelStar);
		if(hotelStar > 10){
        	String strHotelStar = resourceManager.getDescription("res_hotelStarToNum",Math.round(hotelStar));
        	hotelStar = Float.parseFloat(strHotelStar);
        }
        //save to db
		priceLst = MyBeanUtil.getBatchObjectFromParam(params, CommisionAdjust.class, priceRowNum);
		List<CommisionAdjust> newLst = new ArrayList();
		for(CommisionAdjust ca:priceLst){
			//对ca进行数据补充
			ca.setB2BCd("0");
			ca.setHotelId(hotelId);
			ca.setHotelStar(String.valueOf(intStar));
			String temp = ca.getRoomAndPricetypeTemp();
			if(null!=ca && !"".equals(ca)){
				String arr[] = temp.split("&&");
				ca.setRoomTypeId(Long.parseLong(arr[0]));
				ca.setChildRoomId(Long.parseLong(arr[1]));
			}
			ca.setCreateById(opLogonID);
			ca.setCreateBy(agentCode);
			ca.setCreateDate(new Date());
			CommisionAdjust newRecord = new CommisionAdjust();
			try{
				BeanUtils.copyProperties(newRecord,ca);
				newLst.add(newRecord);
			}catch(Exception ex){
				log.error(ex.getMessage(),ex);
				return super.forwardError("B2bAgentCommissionAdjustAction保存错误"+ex.getMessage());
			}
		}
		b2bService.batchUpdate(newLst);
		return super.SUCCESS;
	}
	
	
	
	public String viewAdjustLstByB2bCd(){
			priceLst = b2bService.getAllCommAdjustByHotelId(hotelId);
			List<CommisionAdjust> adjustList = b2bService.getDistinctCommAdjustByHotelId(hotelId);//去重复的
			request.setAttribute("adjustList", adjustList);
			
			hotel = hotelManage.findHotel(hotelId);//酒店
			roomTypePriceTypeLis = hotelManage.findRoomTypePriceTypeLis(hotelId);//价格类型
			return "view";
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
    
    

    /**
     * 得到DB中已存在的日期区间
     * @param ca
     * @param b2BCD
     * @return
     */
	private List<CommisionAdjust> getDateCops(CommisionAdjust ca){
		List<CommisionAdjust> resultLst = null;
		if(null!=ca){
			String hsql = "select b from CommisionAdjust b where b.b2BCd='"+ca.getB2BCd()+"' and b.hotelId="+ca.getHotelId() +
					" and b.roomTypeId="+ca.getRoomTypeId()+" and b.childRoomId="+ca.getChildRoomId()+" and b.payType='"+ca.getPayType()+"'";
			
			resultLst  = b2bService.getCommAdjustLst(hsql);
			
		}
		
		return resultLst;
		
	}
	
private void adjustUtil(CommisionAdjust addComm,List<CommisionAdjust> oldComms) throws IllegalAccessException, InvocationTargetException{
		
		DateComponent dateComponent = new DateComponent();
		dateComponent.setBeginDate(addComm.getStartDate());
		dateComponent.setEndDate(addComm.getEndDate());
		List dateCops = new ArrayList();
		Map resultMap = new HashMap(); 			
		for(int ii=0; ii<oldComms.size(); ii++){
			CommisionAdjust curcomm = oldComms.get(ii);
			DateComponent aComponent = new DateComponent();
			aComponent.setId(curcomm.getAdjustID());
			aComponent.setBeginDate(curcomm.getStartDate());
			aComponent.setEndDate(curcomm.getEndDate());
			dateCops.add(aComponent);				
		}
		resultMap = CutDate.cut(dateComponent,CutDate.sort(dateCops));
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			b2bService.remove(CommisionAdjust.class, bb.getId());	
		}	
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldComms.size(); i++){
			CommisionAdjust aRecord = oldComms.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(aRecord.getAdjustID())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							CommisionAdjust newRecord = new CommisionAdjust();
							BeanUtils.copyProperties(newRecord,aRecord);							
							newRecord.setAdjustID(null);
							newRecord.setStartDate(dateCop.getBeginDate());
							newRecord.setEndDate(dateCop.getEndDate());							
							results.add(newRecord);
						}else{
							aRecord.setStartDate(dateCop.getBeginDate());
							aRecord.setEndDate(dateCop.getEndDate());	
							results.add(aRecord);
						}
					}
				}else if(nullFlag==false){
					if(addComm.getAdjustID()!= null){	//处理修改情况					
						CommisionAdjust record = new CommisionAdjust();
						BeanUtils.copyProperties(record,addComm);						
						record.setAdjustID(null);						
						results.add(record);
					}else{//处理新增情况
						results.add(addComm);
					}
					nullFlag = true;
				}
			}		
		}	
		b2bService.batchUpdate(results);
		
	}

	public String getB2BIdLst() {
		return b2BIdLst;
	}
	public void setB2BIdLst(String idLst) {
		b2BIdLst = idLst;
	}



	public IB2bService getB2bService() {
		return b2bService;
	}

	public void setB2bService(IB2bService service) {
		b2bService = service;
	}




	public List<CommisionAdjust> getPriceLst() {
		return priceLst;
	}




	public void setPriceLst(List<CommisionAdjust> priceLst) {
		this.priceLst = priceLst;
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




	public RoomControlManage getRoomControlManage() {
		return roomControlManage;
	}




	public void setRoomControlManage(RoomControlManage roomControlManage) {
		this.roomControlManage = roomControlManage;
	}


	public int getPriceRowNum() {
		return priceRowNum;
	}


	public void setPriceRowNum(int priceRowNum) {
		this.priceRowNum = priceRowNum;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
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

	public String getBackURL() {
		return backURL;
	}

	public void setBackURL(String backURL) {
		this.backURL = backURL;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

	

}
