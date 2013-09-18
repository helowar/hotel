package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.BatchRoomControlManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IChangePriceManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.base.manage.assistant.UpdatePriceBean;
import com.mangocity.hotel.base.persistence.HtlChangePrice;
import com.mangocity.hotel.base.persistence.HtlChangePriceLog;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.NewDateSegment;
import com.mangocity.util.hotel.constant.BaseConstant;

/**
 * 批量开关房设置
 * @author xuyiwen
 *
 */
public class BatchRoomControlAction extends PersistenceAction {

	private static final long serialVersionUID = 1L;
		
	private String causeSign; //开关房原因
	
	private boolean urgency; //是否紧急变价 这个有问题
	
	 private int rowNum;

	private String message; //提示信息

    private String remark; //备注
    
    private boolean isContract; //是否在合同范围之内
    
//    private String hotelCD; //直接根据酒店ID 查询出hotelCD
    
    private boolean sign; //关房成功标志
    
    private BatchRoomControlManage batchRoomControlManage;
    
    private RoomControlManage roomControlManage;
    
    private IChangePriceManage changePriceManage; //变价工单管理
    
    private HotelManage hotelManage;
	

	public boolean isContract() {
		return isContract;
	}

	public void setContract(boolean isContract) {
		this.isContract = isContract;
	}

	public boolean isSign() {
		return sign;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}


	public IChangePriceManage getChangePriceManage() {
		return changePriceManage;
	}

	public void setChangePriceManage(IChangePriceManage changePriceManage) {
		this.changePriceManage = changePriceManage;
	}

	public boolean isUrgency() {
		return urgency;
	}

	public void setUrgency(boolean urgency) {
		this.urgency = urgency;
	}

	public RoomControlManage getRoomControlManage() {
		return roomControlManage;
	}

	public void setRoomControlManage(RoomControlManage roomControlManage) {
		this.roomControlManage = roomControlManage;
	}

	public BatchRoomControlManage getBatchRoomControlManage() {
		return batchRoomControlManage;
	}

	public void setBatchRoomControlManage(
			BatchRoomControlManage batchRoomControlManage) {
		this.batchRoomControlManage = batchRoomControlManage;
	}

	public String getCauseSign() {
		return causeSign;
	}

	public void setCauseSign(String causeSign) {
		this.causeSign = causeSign;
	}

	
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	
	 /**
     * 批量关房操作  add by xuyiwen 2010-09-20
     * @return
     */
    @SuppressWarnings("unchecked")
	public String batchCloseRoom(){
    	 Map params=super.getParams();    	
    	 List<NewDateSegment> newDateSegments = MyBeanUtil.getBatchObjectFromParam(params, NewDateSegment.class, rowNum);
    	
    	 for(NewDateSegment newDateSement:newDateSegments){
    		 String[] weeks = newDateSement.getWeek();
    		 StringBuffer weekBuffer=new StringBuffer(); //用以拼接星期的字符串  格式为：1,2,3....
    		 if(null!=weeks&&weeks.length!=0){
    			 Integer[] weeksInt=new Integer[weeks.length];
    			 int i=0;
    			 for(String str:weeks){
    				 weekBuffer.append(str).append(",");
    				 weeksInt[i]=Integer.valueOf(str);
    				 i++;
    			 }
    			 String week=weekBuffer.substring(0, weekBuffer.length()-1).toString(); //去除最后的逗号
    			 //1.得到页面的酒店ID集合
    			 String hotelID=params.get("hotelIDs")+"";
    			 String[] hotelIDs=hotelID.split(",");
    			 
    			 for(String s:hotelIDs){
    				 List<HtlOpenCloseRoom> htlOpenCloseRooms=new ArrayList<HtlOpenCloseRoom>();
        			 List<UpdatePriceBean> updatePriceBeans=new ArrayList<UpdatePriceBean>();
    				 
    				 
    				 
    				 //2.根据酒店ID 得到酒店下的房型ID hotel_roomtype
    				 //根据房型ID 得到价格类型ID hotel_price_type
    				List<Map> hotelRoomTypes= batchRoomControlManage.getHotelRoomType(new Long(s));
    				for(Map map:hotelRoomTypes){
    				 HtlOpenCloseRoom htlOpenCloseRoom = new HtlOpenCloseRoom();
       				 UpdatePriceBean updatePriceBean = new UpdatePriceBean();
       				 
       				 htlOpenCloseRoom.setBeginDate(newDateSement.getStart());
       				 htlOpenCloseRoom.setEndDate(newDateSement.getEnd());
       				 htlOpenCloseRoom.setCloseRoomTime(new Date());
       				 htlOpenCloseRoom.setCauseSign(causeSign);
       				 htlOpenCloseRoom.setMessage(message);
       				 htlOpenCloseRoom.setRemark(remark);
       				 htlOpenCloseRoom.setHotelId(Long.valueOf(s));
       				 htlOpenCloseRoom.setWeek(week);
       				 htlOpenCloseRoom.setOpCloseSign(BizRuleCheck.getGuanFangCode());
       				 if (null != super.getOnlineRoleUser()) { //设置关房人
       	                    htlOpenCloseRoom.setCloseRoomOP(super.getOnlineRoleUser().getName());
       	                    if (super.getOnlineRoleUser().getName().equals("")) {
       	                        htlOpenCloseRoom.setCloseRoomOP(super.getBackUserName());
       	                    }
       	                } else {
       	                    htlOpenCloseRoom.setCloseRoomOP(super.getBackUserName());
       	             }
       				 
       				 updatePriceBean.setBeginDate(newDateSement.getStart());
       				 updatePriceBean.setCauseSign(causeSign);
       				 updatePriceBean.setEndDate(newDateSement.getEnd());
       				 updatePriceBean.setWeeks(weeksInt);
    					
    					
    					htlOpenCloseRoom.setRoomTypeId(map.get("PRICE_TYPE_ID")+"");
    					String roomType=map.get("ROOM_NAME")+"("+map.get("PRICE_TYPE")+")";
    					htlOpenCloseRoom.setRoomType(roomType);
    					
    					htlOpenCloseRooms.add(htlOpenCloseRoom);
    					updatePriceBean.setRoomTypeID(Long.valueOf(map.get("ROOM_TYPE_ID")+""));
    					updatePriceBean.setChildRoomID(Long.valueOf(map.get("PRICE_TYPE_ID")+""));
    					
    					updatePriceBeans.add(updatePriceBean);
    				}
    				//3.从酒店对象中拿出房型对每个进行关房的处理
    				roomControlManage.saveOpenCloseRoom(htlOpenCloseRooms, causeSign, Long.valueOf(s));
    				//4.更新每日价格
    				for(UpdatePriceBean u:updatePriceBeans){
    					roomControlManage.updateHtlPriceWithCloseRoom(u);
    				}
    				//5. 如果为1，则是调价原因关房，需要生成调价工单
    		        if (BizRuleCheck.getChangePriceCode().equals(causeSign)) {
    		            // 生成调价工单
    		            newChangePrice(Long.valueOf(s));
    		        }
    				
    		        // 6.更新酒店外网是否显示基本信息字段
    		        String webShowBase = (String) params.get("webShowBaseInfo");
    		        hotelManage.updateHotelWebShowBase(Long.valueOf(s), webShowBase);
    		        
    		        // 7.如果新添了备注信息,那么就要更新相应的酒店的备注信息
    		        if (!(null == remark || remark.equals(""))) {
    		            hotelManage.updateHotelNotes(Long.valueOf(s), remark);
    		        }
    			 }
    		 }
    	 }
    	return SUCCESS;
    }
    
    
    // 创建变价工单方法 (该方法copy于RoomControlAction中)
    private void newChangePrice(Long hotelID) {
        HtlChangePrice changePrice = new HtlChangePrice();
        changePrice.setHotelId(hotelID);
        changePrice.setUrgency(urgency);
        if (null != super.getOnlineRoleUser())
            changePrice.setCreateByUserId(super.getOnlineRoleUser().getLoginName());
        if (null != super.getOnlineRoleUser())
            changePrice.setCreateBy(super.getOnlineRoleUser().getName());
        changePrice.setCreateTime(DateUtil.getDate(DateUtil.getSystemDate()));
        changePrice.setChangeDate(DateUtil.getDate(DateUtil.getSystemDate()));
        changePrice.setStatus(BaseConstant.CP_NEW);
        
        HtlHotel htlHotel= hotelManage.findHotel(hotelID); //根据酒店ID查询酒店对象
        
        changePrice.setTaskCode(BizRuleCheck.getCreateChangePriceCode(htlHotel.getHotelCd()));
        HtlChangePriceLog cpl = new HtlChangePriceLog();
        cpl.setChangeDate(changePrice.getChangeDate());
        cpl.setHotelId(changePrice.getHotelId());
        cpl.setUrgency(urgency);
        cpl.setOperateDate(DateUtil.getDate(DateUtil.getSystemDate()));
        cpl.setOperateState(changePrice.getStatus());
        if (null != super.getOnlineRoleUser()) {
            cpl.setOperater(super.getOnlineRoleUser().getName());
            cpl.setOperaterId(super.getOnlineRoleUser().getLoginName());
        }
        cpl.setTaskCode(changePrice.getTaskCode());

        changePriceManage.createOrUpdateChangePriceWithLog(changePrice, cpl);
    }
    
    /**
     * 批量开房操作 add by xuyiwen 2010-09-20
     * @return
     */
    public String batchOpenRoom(){
    	//1.得到页面过来的酒店ID集合
    	 Map params=super.getParams(); 
    	 //String reason=params.get("reason")+"";
    	 String hotelID=params.get("hotelIdStr")+"";
		 String[] hotelIDs=hotelID.split(",");
		 
    	//2.根据酒店ID 查询htl_open_close_room表 得到每一条关房记录
		 for(String id:hotelIDs){
			 List<HtlOpenCloseRoom> htlOpenCloseRoomList = new ArrayList<HtlOpenCloseRoom>();
			 List<HtlOpenCloseRoom> htlOpenCloseRooms = roomControlManage.queryHtlOpenCloseRoom(Long.valueOf(id));			 
			 for(HtlOpenCloseRoom hocr:htlOpenCloseRooms){
				 
				 // 判断关房原因是否与选择的开房原因符合
				//if(!reason.equals( hocr.getCauseSign())) continue;
				 
				hocr.setOpCloseSign(BizRuleCheck.getKaiFangCode());
			 	if (null != super.getOnlineRoleUser()) {
				 hocr.setOpenRoomOP(super.getOnlineRoleUser().getName());
                    if (super.getOnlineRoleUser().getName().equals("")) {
                    	hocr.setOpenRoomOP(super.getBackUserName());
                    }
                } else {
                	hocr.setOpenRoomOP(super.getBackUserName());
                }
			 	hocr.setOpenRoomTime(DateUtil.getSystemDate());
			 	htlOpenCloseRoomList.add(hocr);
			 	//更新价格信息
			 	UpdatePriceBean updatePriceBean = new UpdatePriceBean();
			 	updatePriceBean.setBeginDate(hocr.getBeginDate());
			 	updatePriceBean.setChildRoomID(Long.valueOf(hocr.getRoomTypeId()));
			 	updatePriceBean.setEndDate(hocr.getEndDate());
			 	updatePriceBean.setCauseSign(hocr.getCauseSign());
			 	Integer[] weekInt=new Integer[7];
			 	int i=0;
			 	for(String str:hocr.getWeek().split(",")){
			 		weekInt[i]=Integer.valueOf(str);
			 		i++;
			 	}
			 	updatePriceBean.setWeeks(weekInt);
			 	//3.对其每一个进行开房操作
			 	roomControlManage.updateHtlPriceWithOpenRoom(updatePriceBean);
			 }
			 if(htlOpenCloseRoomList.size()!=0) roomControlManage.saveOpenCloseRoom(htlOpenCloseRoomList, "", Long.valueOf(id));
		 }
    	return "batchOpen";
    }
}
