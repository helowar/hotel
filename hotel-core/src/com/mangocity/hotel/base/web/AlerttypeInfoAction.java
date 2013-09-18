package com.mangocity.hotel.base.web;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class AlerttypeInfoAction extends PersistenceAction {

    // Id
    private Long id;
    
    private String ids;

    // 酒店ID
    private Long hotelId;

    // 合同ID
    private Long contractId;

    // 房型ID
    private Long roomTypeId;

    // 房型名称
    private String roomName;

    // 价格类型ID
    private Long priceTypeId;

    // 价格类型名称
    private String priceTypeName;

    // 开始日期
    private Date beginDate;

    // 结束日期
    private Date endDate;

    // 星期
    private String week;

    // 可见位置标记(1:CC;2:网站;)
    private String localFlag;

    // 提示信息内容
    private String alerttypeInfo;

    private String addfor;

    private String forward;

    private int rowNum;

    /**
     * 在增加或修改预定条款信息时传入合同开始日期
     */
    private String beDate;

    /**
     * 在增加或修改预定条款信息时传入合同的结束日期
     */
    private String enDate;

    private String start;

    private String end;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private ContractManage contractManage;

    private List lstRoomType;

    private String roomTypes;

    // 星期
    private final static String FSWEEK = "1,2,3,4,5,6,7,";

    protected Class getEntityClass() {
        return HtlAlerttypeInfo.class;
    }

    public String view() {
        super.view();
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        HtlAlerttypeInfo alertInfo= (HtlAlerttypeInfo)super.getEntity();
        List alertInfoList = contractManage.queryAlertInfoByConId(alertInfo.getContractId(), false);
        String priceTypeIds = getPriceTypeIds(alertInfoList,alertInfo);
        alertInfo.setPriceTypeId(priceTypeIds);
        log.debug(priceTypeIds);
        return VIEW;
    }
    /**
     * 拼装PriceTypeIds
     * @param alertInfoList
     * @param alertInfo
     * @return
     */
    private String getPriceTypeIds(List alertInfoList, HtlAlerttypeInfo alertInfo) {
    	StringBuilder priceTypeIds=new StringBuilder();
    	for(Iterator i=alertInfoList.iterator();i.hasNext();){
    		HtlAlerttypeInfo tmpalertInfo = (HtlAlerttypeInfo)i.next();
        	if(DateUtil.compare(tmpalertInfo.getModifyTime(), alertInfo.getModifyTime())==0
        			&&tmpalertInfo.getModifyById().equals(alertInfo.getModifyById())){
        		if(priceTypeIds.length()>0){
        			priceTypeIds.append(",");
        		}
        		priceTypeIds.append(tmpalertInfo.getPriceTypeId());
        	}
        }
    	return priceTypeIds.toString();
	}

	public String create() {

        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);

        forward = "create";

        return forward;

    }

    /**
     * 删除一个提示信息
     */
    public String delete() {

        try {
        	if(null!=ids){
        		String[] idArray = ids.split(",");
        		contractManage.deleteAlertTypeInfoByIds(idArray,"HtlAlerttypeInfo");
        	}

        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }

        forward = "recontract";

        return forward;
    }

    public String edit() {
    	if(null==super.getOnlineRoleUser()||null==super.getOnlineRoleUser().getLoginName()){
    		return super.forwardError("获取登陆用户信息失效,请重新登陆");
    	}
        super.setEntity(super.populateEntity());
        HtlAlerttypeInfo htlAlerttypeInfo=getAlertTypeInfoEntity();
        htlAlerttypeInfo.setModifyBy(super.getOnlineRoleUser().getName());
        htlAlerttypeInfo.setModifyById(super.getOnlineRoleUser().getLoginName());
        htlAlerttypeInfo.setModifyTime(DateUtil.getSystemDate());
        if ("addAlerttypeInfo".equals(addfor)) {
        	createAlerttypeInfo(htlAlerttypeInfo,roomTypes);
        } else {
        	modifyAlerttypeInfo(htlAlerttypeInfo,ids);
        }
        if ("recontract".equals(forward)) {
            return forward;
        }
        return SAVE_SUCCESS;
    }

    private void modifyAlerttypeInfo( HtlAlerttypeInfo htlAlerttypeInfo,String ids) {
    	HtlAlerttypeInfo alerttypeInfoBak = new HtlAlerttypeInfo();
    	HtlAlerttypeInfo infonAtity = (HtlAlerttypeInfo)super.getEntity();
    	if(null!=ids && ids.length()>0){
    		List<HtlAlerttypeInfo> htlAlerttypeInfoList=contractManage.queryAlerttTypeInfoByIds(ids);
    		for(HtlAlerttypeInfo tmpAlerttypeInfo : htlAlerttypeInfoList){
    			if(infonAtity.getId().equals(tmpAlerttypeInfo.getId())){
    				continue;
    			}
    			
				MyBeanUtil.copyProperties(alerttypeInfoBak, tmpAlerttypeInfo);
				MyBeanUtil.copyProperties(tmpAlerttypeInfo, htlAlerttypeInfo);
				tmpAlerttypeInfo.setId(alerttypeInfoBak.getId());
				tmpAlerttypeInfo.setPriceTypeId(alerttypeInfoBak.getPriceTypeId());
				tmpAlerttypeInfo.setPriceTypeName(alerttypeInfoBak.getPriceTypeName());
				try {
					contractManage.modifyAlerttypeInfo(tmpAlerttypeInfo);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					log.error(e.getMessage(), e);
				}
    		}    		
    	}
    	
        if (null == htlAlerttypeInfo.getWeek() || htlAlerttypeInfo.getWeek().equals("")) {
            htlAlerttypeInfo.setWeek(FSWEEK);
        }
        super.setEntity(htlAlerttypeInfo);
        try {
            contractManage.modifyAlerttypeInfo(htlAlerttypeInfo);
        } catch (IllegalAccessException e) {
        	log.error(e.getMessage(),e);
        } catch (InvocationTargetException e) {
        	log.error(e.getMessage(),e);
        }		
	}

	private void createAlerttypeInfo(HtlAlerttypeInfo htlAlerttypeInfo, String roomTypes2) {
    	if (null == roomTypes) {
    		return;
    	}
    	
	    String[] roomTypesArray = roomTypes.split(",");
	    for (String strRoomType : roomTypesArray) {
	        String priceName = contractManage.priceTypeName(Long.parseLong(strRoomType));
	        htlAlerttypeInfo.setBeginDate(DateUtil.getDate(start));
	        htlAlerttypeInfo.setEndDate(DateUtil.getDate(end));
	        if (null == week || week.equals("")) {
	            htlAlerttypeInfo.setWeek(FSWEEK);
	        } else {
	            htlAlerttypeInfo.setWeek(week);
	        }
	        htlAlerttypeInfo.setLocalFlag(localFlag);
	        htlAlerttypeInfo.setAlerttypeInfo(alerttypeInfo.trim());
	        htlAlerttypeInfo.setPriceTypeId(strRoomType);
	        htlAlerttypeInfo.setPriceTypeName(priceName);
	        htlAlerttypeInfo.setHotelId(hotelId);
	        htlAlerttypeInfo.setContractId(contractId);
	        htlAlerttypeInfo.setCreateBy(super.getOnlineRoleUser().getName());
	        htlAlerttypeInfo.setCreateById(super.getOnlineRoleUser().getLoginName());
	        htlAlerttypeInfo.setCreateTime(DateUtil.getSystemDate());
	        
	        try {
	        	//保存数据之前，需要清空掉ID，否则就不是新增提示信息，而是修改提示信息了modify by alfred.
	        	htlAlerttypeInfo.setId(null);
	            contractManage.createAlerttypeInfo(htlAlerttypeInfo);
	        } catch (IllegalAccessException e) {
	        	log.error(e.getMessage(),e);
	        } catch (InvocationTargetException e) {
	        	log.error(e.getMessage(),e);
	        }
	    }
        		
	}

	private HtlAlerttypeInfo getAlertTypeInfoEntity() {
    	 if (null != super.getEntity()) {
            return (HtlAlerttypeInfo) super.getEntity();
         } else {
             return (HtlAlerttypeInfo) super.populateEntity();
         }
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLocalFlag() {
        return localFlag;
    }

    public void setLocalFlag(String localFlag) {
        this.localFlag = localFlag;
    }

    public String getAlerttypeInfo() {
        return alerttypeInfo;
    }

    public void setAlerttypeInfo(String alerttypeInfo) {
        this.alerttypeInfo = alerttypeInfo;
    }

    public String getBeDate() {
        return beDate;
    }

    public void setBeDate(String beDate) {
        this.beDate = beDate;
    }

    public String getEnDate() {
        return enDate;
    }

    public void setEnDate(String enDate) {
        this.enDate = enDate;
    }

    public String getAddfor() {
        return addfor;
    }

    public void setAddfor(String addfor) {
        this.addfor = addfor;
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lstRoomType) {
        this.lstRoomType = lstRoomType;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public String getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(String roomTypes) {
        this.roomTypes = roomTypes;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
