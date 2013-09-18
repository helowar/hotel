package com.mangocity.hotel.order.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.RoomStateManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlTempRoomState;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.order.persistence.HtlRoomstateCc;
import com.mangocity.hotel.order.persistence.HtlRoomstateCcBean;
import com.mangocity.hotel.order.service.IRoomStratFromCCService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.RoomTypeStatus;

/**
 */
public class RoomStratFromCCAction extends OrderAction {

    private Long hotelId;

    private Date begindate;

    private Date enddate;
    
    private Date start;

    private Date end;

    private Long roomTypeId;

    private int rowNum;

    private String inform;

    private String remark;

    private String submittype;

    private Long roomStateCCID;

    /**
     * cutoff行数
     */
    private int cutoffDayNum;

    private List htlRoomtypes = new ArrayList();

    private HtlRoomstateCcBean htlRoomstateCcBean = new HtlRoomstateCcBean();

    private HtlRoomstateCc htlRoomstateCc = new HtlRoomstateCc();

    private IRoomStratFromCCService roomStratFromCCService;
    private HotelManage hotelManage;

    private RoomStateManage roomStateManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    public String forward() {
        hotelId = Long.valueOf(request.getParameter("hotelId"));
        htlRoomtypes = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        request.setAttribute("htlRoomtypes", htlRoomtypes);
        begindate = DateUtil.getDate(request.getParameter("begindate"));
        enddate = DateUtil.getDate(DateUtil.getDate(request.getParameter("enddate")), -1);

        return "forward";
    }

    public String SaveRoomStateCC() throws SQLException {
        Map params = super.getParams();

        // 界面上传过来的 房态对象数组
        List ls = MyBeanUtil.getBatchObjectFromParam(params, RoomTypeStatus.class, rowNum - 1);
        List lstRoomTypeState = new ArrayList();
        
        for (int n = 0; n < ls.size(); n++) {
            RoomTypeStatus rts = (RoomTypeStatus) ls.get(n);
            if (null != rts.getRoomBedStatus() && rts.getRoomBedStatus().equals("4")){
                lstRoomTypeState.add(rts);
            }
        }
        
        List dls = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, cutoffDayNum);
        UserWrapper user = getOnlineRoleUser();
        for (int i = 0; i < dls.size(); i++) {
            DateSegment ds = new DateSegment();
            ds = (DateSegment) dls.get(i);
            htlRoomstateCcBean.setRoomTypeStatus(lstRoomTypeState);
            htlRoomstateCcBean.setBegindate(ds.getStart());
            htlRoomstateCcBean.setEnddate(DateUtil.getDate(ds.getEnd(), 1));
            htlRoomstateCcBean.setHotelid(Long.parseLong((String) params.get("hotelId")));
            htlRoomstateCcBean.setInform((String) params.get("inform"));
            htlRoomstateCcBean.setRemark((String) params.get("remark"));

            if (null != user) {
                htlRoomstateCcBean.setProcessbyid(user.getLoginName());
                htlRoomstateCcBean.setProcessby(user.getName());
                if (user.getLoginName().equals("") || user.getName().equals("")) {
                    htlRoomstateCcBean.setProcessbyid(super.getBackLoginName());
                    htlRoomstateCcBean.setProcessby(super.getBackUserName());
                }
            } else {
                htlRoomstateCcBean.setProcessbyid(super.getBackLoginName());
                htlRoomstateCcBean.setProcessby(super.getBackUserName());
            }
            roomStratFromCCService.saveRoomState(htlRoomstateCcBean);
        }
        List dateList = DateUtil.getDates(start, end);
        List lstTempRoomState=new ArrayList();
        for(int i=0;i<dateList.size();i++){
        	Date date=(Date) dateList.get(i);
	        for (int n = 0; n < ls.size(); n++) {
	            RoomTypeStatus rts = (RoomTypeStatus) ls.get(n);
	            if (null != rts.getRoomBedStatus() && rts.getRoomBedStatus().equals("4")){
	            	HtlTempRoomState tempRoom=new HtlTempRoomState();
        			tempRoom.setSaleDate(date);
        			tempRoom.setBedId(rts.getRoomBedId());
        			tempRoom.setRoomName(rts.getRoomName());
        			tempRoom.setRoomType(Long.valueOf(rts.getRoomTypeID()));
        			lstTempRoomState.add(tempRoom);
	            }
	        }
        }
        HtlHotel hotel = hotelManage.findHotel(hotelId);
        if(null!=lstTempRoomState){
        	roomStateManage.sendRoomStateFull(lstTempRoomState, hotel, user,0);
	        if(null!=dateList&&dateList.size()>6){
	        	roomStateManage.sendRoomStateFull(lstTempRoomState, hotel, user,1);
	        }
        }
        
        return SUCCESS;
    }

    public String queryRoomStateCclog() {

        setHtlRoomstateCc(roomStratFromCCService.findRoomFulLog(roomStateCCID));
        return "log";

    }

    public String updateRoomStateCclog() {
        if (null != super.getOnlineRoleUser()) {
            getHtlRoomstateCc().setReviewid(super.getOnlineRoleUser().getLoginName());
            getHtlRoomstateCc().setReviewname(super.getOnlineRoleUser().getName());
            if (super.getOnlineRoleUser().getLoginName().equals("")
                || super.getOnlineRoleUser().getName().equals("")) {
                getHtlRoomstateCc().setReviewid(super.getBackLoginName());
                getHtlRoomstateCc().setReviewname(super.getBackUserName());
            }
        } else {
            getHtlRoomstateCc().setReviewid(super.getBackLoginName());
            getHtlRoomstateCc().setReviewname(super.getBackUserName());
        }

        getHtlRoomstateCc().setReviewdept("本部");
        getHtlRoomstateCc().setProcessdate(new Date());
        getHtlRoomstateCc().setReviewstate("1");
        roomStratFromCCService.updateRoomFulLog(getHtlRoomstateCc());

        return "save";

    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public List getHtlRoomtypes() {
        return htlRoomtypes;
    }

    public void setHtlRoomtypes(List htlRoomtypes) {
        this.htlRoomtypes = htlRoomtypes;
    }

    public IRoomStratFromCCService getRoomStratFromCCService() {
        return roomStratFromCCService;
    }

    public void setRoomStratFromCCService(IRoomStratFromCCService roomStratFromCCService) {
        this.roomStratFromCCService = roomStratFromCCService;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
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

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getCutoffDayNum() {
        return cutoffDayNum;
    }

    public void setCutoffDayNum(int cutoffDayNum) {
        this.cutoffDayNum = cutoffDayNum;
    }

    public String getInform() {
        return inform;
    }

    public void setInform(String inform) {
        this.inform = inform;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public HtlRoomstateCcBean getHtlRoomstateCcBean() {
        return htlRoomstateCcBean;
    }

    public void setHtlRoomstateCcBean(HtlRoomstateCcBean htlRoomstateCcBean) {
        this.htlRoomstateCcBean = htlRoomstateCcBean;
    }

    public Long getRoomStateCCID() {
        return roomStateCCID;
    }

    public void setRoomStateCCID(Long roomStateCCID) {
        this.roomStateCCID = roomStateCCID;
    }

    public String getSubmittype() {
        return submittype;
    }

    public void setSubmittype(String submittype) {
        this.submittype = submittype;
    }

    public void setHtlRoomstateCc(HtlRoomstateCc htlRoomstateCc) {
        this.htlRoomstateCc = htlRoomstateCc;
    }

    public HtlRoomstateCc getHtlRoomstateCc() {
        return htlRoomstateCc;
    }

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public void setRoomStateManage(RoomStateManage roomStateManage) {
		this.roomStateManage = roomStateManage;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
