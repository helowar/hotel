package com.mangocity.hotel.newroomcontrol.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IPriceDao;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.RoomStateManage;
import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.HtlTempRoomState;
import com.mangocity.hotel.base.service.assistant.RoomBedRecord;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.newroomcontrol.service.NewRoomControlService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 按时段调整
 */
public class RoomStateProcessByPeriodAction extends PersistenceAction {

    private long hotelID;

    private List htlCtct;

    private List roomType;

    private List lstEexhibits = new ArrayList();

    private List lstSeasons = new ArrayList();

    private int rowNum;

    private HotelManage hotelManage;

    private RoomStateManage roomStateManage;

    private IPriceDao priceDao;

    private HtlHotel hotel;

    private Date begindate;

    private Date enddate;

    private String[] week;

    // 房态负责人
    private String roomStateManagerStr;

    private String isRoomStatusReport;

    /*
     * 昨天到今天所有房态更改记录
     */
    private List dateRoomStatusProcess = new ArrayList();

    /**
     * 交接事项
     */
    private String processRemark;

    /**
     * 合同起始日期，只做显示用，
     */
    private String bD;

    /**
     * 合同结束日期
     */
    private String eD;
    
    private NewRoomControlService newRoomControlService;
    /**
     * 酒店记录ID
     */
    private String hotelScheduleId;
    
    // modified by wuyun
    private ContractManage contractManage;
    
    //交接事项最后操作人
	private String  newlyModifyByName;
	
	//交接事项最后操作时间
	private Date newlyModifyTime;
	
	public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    /**
     * 按时段处理
     * 
     * @return
     */
    public String processByTimes() {
    	
        super.getOnlineRoleUser();

        dateRoomStatusProcess = hotelManage.findRoomStatusDateProcess(hotelID);
        int i = dateRoomStatusProcess.size();
        if (0 != i) {
            HtlRoomStatusProcess htlRoomStatusProcess = new HtlRoomStatusProcess();
            htlRoomStatusProcess = (HtlRoomStatusProcess) dateRoomStatusProcess.get(i - 1);
            isRoomStatusReport = htlRoomStatusProcess.getIsRoomStatusReport().toString();
            processRemark = htlRoomStatusProcess.getProcessRemark();
            newlyModifyByName = htlRoomStatusProcess.getProcessBy();
            newlyModifyTime = htlRoomStatusProcess.getProcessDate();
            
        }
        hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
        List list = hotel.getHtelHotelExt();
        for (int j = 0; j < list.size(); j++) {
            HtlHotelExt hext = (HtlHotelExt) list.get(j);
            String roomStateManager = hext.getRoomStateManager();
            String isGreenMangoHotel = hext.getIsGreenMangoHotel();
            request.setAttribute("roomStateManager", roomStateManager);
            request.setAttribute("isGreenMangoHotel", isGreenMangoHotel);
        }
        // modify by shizhongwen 去掉属于中旅的房型(因为中旅香港房型不存在房态管理)
        roomType = hotelManage.lstRoomTypeByHotelIdRemoveHK(hotelID);
        rowNum = roomType.size();
        // 根据城市查询展会,传递城市到页面上，以供查询
        //if (null != hotel.getCity() && !("".equals(hotel.getCity())))
         //   request.setAttribute("city", hotel.getCity());

        // 根据酒店查询大假期
        //lstSeasons = hotel.getSellSeason();

        // Modified by WUYUN ，根据当前日期和酒店id去找合同
        HtlContract contract = contractManage.checkContractDateNew(hotelID, new Date());
        if (null != contract) {
            bD = DateUtil.dateToString(contract.getBeginDate());
            eD = DateUtil.dateToString(contract.getEndDate());
        }

        return "processByTimes";
    }

    public String saveRoomStatu() {
    	roleUser = getCurrentUser();
    	if(null == roleUser)
       		return forwardMsg("您未登陆，请先登陆再操作！");
        Map params = super.getParams();
        List ls = MyBeanUtil.getBatchObjectFromParam(params, RoomBedRecord.class, rowNum);
        /**
         * 将ls中roomTypeStatus不为空的取出放到templs中
         * 
         * @guojun 2008-12-19 10:12 begin
         */
        List<RoomBedRecord> templs = new ArrayList<RoomBedRecord>();
        List<RoomBedRecord> tempFulls=new ArrayList<RoomBedRecord>();
        for (Iterator itr = ls.iterator(); itr.hasNext();) {
        	RoomBedRecord roomTS = (RoomBedRecord) itr.next();
        	//去掉没有选择的，和没有选房态的
            if(0 == roomTS.getTheSelected() || 
            		(null == roomTS.getBigBedStatus() && null == roomTS.getDoubleBedStatus() && 
            				null == roomTS.getSingleBedStatus()))
            	continue;
            if((null!=roomTS.getBigBedStatus()&&roomTS.getBigBedStatus()==4)||(null!=roomTS.getDoubleBedStatus()&&roomTS.getDoubleBedStatus()==4)||(null!=roomTS.getSingleBedStatus()&&roomTS.getSingleBedStatus()==4)){
            	tempFulls.add(roomTS);
            }
            templs.add(roomTS);
        }
        StringBuffer logsb = new StringBuffer();
        logsb.append("按时段调整房态:");
        for (RoomBedRecord record : templs) {
        	roomStateManage.saveRoomStateBatch(record);
        	logsb.append(record.toString());
		}
        newRoomControlService.saveRoomStatusOptLogs(hotelScheduleId,roleUser,logsb.toString());
        
        /**
         * 将ls中roomTypeStatus不为空的取出放到templs中
         * 
         * @guojun 2008-12-19 10:12 end
         */
        // 更新房态负责人 haibo.li by 2008.12.01 begin
        hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
        String isGreenMangoHotel = (String)params.get("isGreenMangoHotel");
        List lsHtlHotelExt = hotel.getHtelHotelExt();
        if (0 < lsHtlHotelExt.size()) {
            if (StringUtil.isValidStr(roomStateManagerStr) || 
            		StringUtil.isValidStr(isGreenMangoHotel)) {
                HtlHotelExt he = (HtlHotelExt) lsHtlHotelExt.get(0);
                he.setHtlHotel(hotel);
                he.setRoomStateManager(roomStateManagerStr);
                he.setIsGreenMangoHotel(isGreenMangoHotel);
                hotelManage.saveOrUpdateExt(he);
            }
        } else if (0 == lsHtlHotelExt.size()) {
            if (StringUtil.isValidStr(roomStateManagerStr) || 
            		StringUtil.isValidStr(isGreenMangoHotel)) {
                HtlHotelExt he = new HtlHotelExt();
                he.setHtlHotel(hotel);
                he.setRoomStateManager(roomStateManagerStr);
                he.setIsGreenMangoHotel(isGreenMangoHotel);
                hotelManage.saveHtlHotelExt(he);
            }
        }
        List lstTempRoomStateDays=new ArrayList();
        List lstTempRoomState=new ArrayList();
        //收回配额预警&满房预警(发邮件）
        for (RoomBedRecord record : tempFulls) {
        	List dateList = DateUtil.getDates(record.getTheStart(), record.getTheEnd());
        	boolean selWeek=((null==record.getTheWeeks())||("".equals(record.getTheWeeks()))||("1,2,3,4,5,6,7".equals(record.getTheWeeks())))?true:false;
        	if(null!=dateList){
        		//连续7天
	        	if(dateList.size()>6&&selWeek){
	        		for(Iterator i = dateList.iterator();i.hasNext();){
	        			Date date=(Date) i.next();
	        			String [] beds=record.getBeds().split(",");
	        			for(int j=0;j<beds.length;j++){
	        				if(null!=record.getBigBedStatus()&&record.getBigBedStatus()==4){
	        					if(beds[j].equals("1")){
			        				HtlTempRoomState tempRoom=new HtlTempRoomState();
				        			tempRoom.setSaleDate(date);
				        			tempRoom.setBedId("1");
				        			tempRoom.setRoomName(record.getRoomName());
				        			tempRoom.setRoomType(record.getTheRoomTypeId());
				        			lstTempRoomStateDays.add(tempRoom);
	        					}
	        				}else{
	        					if(null!=record.getDoubleBedStatus()&&record.getDoubleBedStatus()==4){
	        						if(beds[j].equals("2")){
				        				HtlTempRoomState tempRoom=new HtlTempRoomState();
					        			tempRoom.setSaleDate(date);
					        			tempRoom.setBedId("2");
					        			tempRoom.setRoomName(record.getRoomName());
					        			tempRoom.setRoomType(record.getTheRoomTypeId());
					        			lstTempRoomStateDays.add(tempRoom);
		        					}
	        					}else{
	        						if(beds[j].equals("3")){
				        				HtlTempRoomState tempRoom=new HtlTempRoomState();
					        			tempRoom.setSaleDate(date);
					        			tempRoom.setBedId("3");
					        			tempRoom.setRoomName(record.getRoomName());
					        			tempRoom.setRoomType(record.getTheRoomTypeId());
					        			lstTempRoomStateDays.add(tempRoom);
		        					}
	        					}
	        				}
	        			}
	        		}
	        	}
	        	//星期全选
        		if(selWeek){
        			for(Iterator i = dateList.iterator();i.hasNext();){
	        			Date date=(Date) i.next();
	        			String [] beds=record.getBeds().split(",");
	        			for(int j=0;j<beds.length;j++){
	        				
	        				if(null!=record.getBigBedStatus()&&record.getBigBedStatus()==4){
	        					if(beds[j].equals("1")){
			        				HtlTempRoomState tempRoom=new HtlTempRoomState();
				        			tempRoom.setSaleDate(date);
				        			tempRoom.setBedId("1");
				        			tempRoom.setRoomName(record.getRoomName());
				        			tempRoom.setRoomType(record.getTheRoomTypeId());
				        			lstTempRoomState.add(tempRoom);
	        					}
	        				}else{
	        					if(null!=record.getDoubleBedStatus()&&record.getDoubleBedStatus()==4){
	        						if(beds[j].equals("2")){
				        				HtlTempRoomState tempRoom=new HtlTempRoomState();
					        			tempRoom.setSaleDate(date);
					        			tempRoom.setBedId("2");
					        			tempRoom.setRoomName(record.getRoomName());
					        			tempRoom.setRoomType(record.getTheRoomTypeId());
					        			lstTempRoomState.add(tempRoom);
		        					}
	        					}else{
	        						if(beds[j].equals("3")){
				        				HtlTempRoomState tempRoom=new HtlTempRoomState();
					        			tempRoom.setSaleDate(date);
					        			tempRoom.setBedId("3");
					        			tempRoom.setRoomName(record.getRoomName());
					        			tempRoom.setRoomType(record.getTheRoomTypeId());
					        			lstTempRoomState.add(tempRoom);
		        					}
	        					}
	        				}
	        				
	        			}
	        		}
	        		
        		}else{
        			//选中个别星期
        			for(Iterator i = dateList.iterator();i.hasNext();){
	        			Date date=(Date) i.next();
	        			if(DateUtil.isMatchWeek(date, record.getTheWeeks().split(","))){
		        			String [] beds=record.getBeds().split(",");
		        			for(int j=0;j<beds.length;j++){
		        				if(null!=record.getBigBedStatus()&&record.getBigBedStatus()==4){
		        					if(beds[j].equals("1")){
				        				HtlTempRoomState tempRoom=new HtlTempRoomState();
					        			tempRoom.setSaleDate(date);
					        			tempRoom.setBedId("1");
					        			tempRoom.setRoomName(record.getRoomName());
					        			tempRoom.setRoomType(record.getTheRoomTypeId());
					        			lstTempRoomState.add(tempRoom);
		        					}
		        				}else{
		        					if(null!=record.getDoubleBedStatus()&&record.getDoubleBedStatus()==4){
		        						if(beds[j].equals("2")){
					        				HtlTempRoomState tempRoom=new HtlTempRoomState();
						        			tempRoom.setSaleDate(date);
						        			tempRoom.setBedId("2");
						        			tempRoom.setRoomName(record.getRoomName());
						        			tempRoom.setRoomType(record.getTheRoomTypeId());
						        			lstTempRoomState.add(tempRoom);
			        					}
		        					}else{
		        						if(beds[j].equals("3")){
					        				HtlTempRoomState tempRoom=new HtlTempRoomState();
						        			tempRoom.setSaleDate(date);
						        			tempRoom.setBedId("3");
						        			tempRoom.setRoomName(record.getRoomName());
						        			tempRoom.setRoomType(record.getTheRoomTypeId());
						        			lstTempRoomState.add(tempRoom);
			        					}
		        					}
		        				}
		        			}
	        			}
	        		}
        		}
        	}
        }
        if(null!=lstTempRoomStateDays){
        	roomStateManage.sendRoomStateFull(lstTempRoomStateDays, hotel, roleUser,1);
        }
        if(null!=lstTempRoomState){
        	roomStateManage.sendRoomStateFull(lstTempRoomState, hotel, roleUser,0);
        }
        // end
        
        //根据房态取消或设置配额预警，如果为满、不可超、freesale，把配额预警数据取消
        newRoomControlService.changeQuotaWarningByRoomstatePeriod(templs, hotelID);
        
        //return SUCCESS;
        return "backToByCalendar";//保存成功后,返回按日历调整
    }
    /**
     * 异步保存交接事项和是否主动报房态 add by zhijie.gu 2010-01-05
     * processRemark交接事项内容
     * isRoomStatusReport 是否主动报房态
     * @return
     */
    public String saveProcessRemarkAndIsRoomStatus() {
    	RoomStateBean roomStateBean = new RoomStateBean();
    	roomStateBean.setHotelID(hotelID);
        if (null != super.getOnlineRoleUser()) {
            roomStateBean.setUserName(super.getOnlineRoleUser().getName());
            roomStateBean.setUserId(super.getOnlineRoleUser().getLoginName());
            if (super.getOnlineRoleUser().getLoginName().equals("")
                || super.getOnlineRoleUser().getName().equals("")) {
                roomStateBean.setUserName(super.getBackUserName());
                roomStateBean.setUserId(super.getBackLoginName());
            }
        }
        roomStateBean.setIsRoomStatusReport(isRoomStatusReport);
        roomStateBean.setProcessRemark(processRemark);
        roomStateManage.saveRoomStateProcess(roomStateBean);
    	return "success";
    }
    
    public String forwardToRoomStateHistory(){
    	
    	return "forwardQueryRoomStateHistory";
    }

    public List getHtlCtct() {
        return htlCtct;
    }

    public void setHtlCtct(List htlCtct) {
        this.htlCtct = htlCtct;
    }

    public List getRoomType() {
        return roomType;
    }

    public void setRoomType(List roomType) {
        this.roomType = roomType;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;
    }

    public RoomStateManage getRoomStateManage() {
        return roomStateManage;
    }

    public void setRoomStateManage(RoomStateManage roomStateManage) {
        this.roomStateManage = roomStateManage;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public HtlHotel getHotel() {
        return hotel;
    }

    public void setHotel(HtlHotel hotel) {
        this.hotel = hotel;
    }

    public List getLstEexhibits() {
        return lstEexhibits;
    }

    public void setLstEexhibits(List lstEexhibits) {
        this.lstEexhibits = lstEexhibits;
    }

    public List getLstSeasons() {
        return lstSeasons;
    }

    public void setLstSeasons(List lstSeasons) {
        this.lstSeasons = lstSeasons;
    }

    public IPriceDao getPriceDao() {
        return priceDao;
    }

    public void setPriceDao(IPriceDao priceDao) {
        this.priceDao = priceDao;
    }

    public String getBD() {
        return bD;
    }

    public void setBD(String bd) {
        bD = bd;
    }

    public String getED() {
        return eD;
    }

    public void setED(String ed) {
        eD = ed;
    }

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }

    public List getDateRoomStatusProcess() {
        return dateRoomStatusProcess;
    }

    public void setDateRoomStatusProcess(List dateRoomStatusProcess) {
        this.dateRoomStatusProcess = dateRoomStatusProcess;
    }

    public String getIsRoomStatusReport() {
        return isRoomStatusReport;
    }

    public void setIsRoomStatusReport(String isRoomStatusReport) {
        this.isRoomStatusReport = isRoomStatusReport;
    }

    public String getRoomStateManagerStr() {
        return roomStateManagerStr;
    }

    public void setRoomStateManagerStr(String roomStateManagerStr) {
        this.roomStateManagerStr = roomStateManagerStr;
    }

	public String getNewlyModifyByName() {
		return newlyModifyByName;
	}

	public void setNewlyModifyByName(String newlyModifyByName) {
		this.newlyModifyByName = newlyModifyByName;
	}

	public Date getNewlyModifyTime() {
		return newlyModifyTime;
	}

	public void setNewlyModifyTime(Date newlyModifyTime) {
		this.newlyModifyTime = newlyModifyTime;
	}

	public NewRoomControlService getNewRoomControlService() {
		return newRoomControlService;
	}

	public void setNewRoomControlService(NewRoomControlService newRoomControlService) {
		this.newRoomControlService = newRoomControlService;
	}

	public String getHotelScheduleId() {
		return hotelScheduleId;
	}

	public void setHotelScheduleId(String hotelScheduleId) {
		this.hotelScheduleId = hotelScheduleId;
	}
	
}
