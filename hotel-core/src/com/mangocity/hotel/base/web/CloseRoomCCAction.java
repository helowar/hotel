package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IChangePriceManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.base.manage.assistant.UpdatePriceBean;
import com.mangocity.hotel.base.persistence.HtlChangePrice;
import com.mangocity.hotel.base.persistence.HtlChangePriceLog;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.NewDateSegment;
import com.mangocity.util.hotel.constant.BaseConstant;

/**
 */
public class CloseRoomCCAction extends PersistenceAction {

    private String forward;

    private long hotelID;

    private String hotelCD;

    private String operate;

    private int rowNum;

    private String[] roomType;

    private RoomControlManage roomControlManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private HotelManage hotelManage;

    private HtlHotel htlHotel;

    private List lstRoomType = new ArrayList();

    private List htlOpenCloseRoomItem = new ArrayList();

    private String[] childRoomId;

    private String[] operates;

    private String causeSign;

    private String message;

    private String notes;

    /**
     * 是否紧急变价
     */
    private boolean urgency;

    /**
     * 变价工单管理类
     */
    private IChangePriceManage changePriceManage;

    public String forward() {
        htlHotel = hotelManage.findHotel(hotelID);
        hotelCD = htlHotel.getHotelCd();
        if ("close".equals(forward)) {
            lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
        }
        return forward;
    }

    public String saveRoomOpenClose() {
        Map params = super.getParams();
        List ls = MyBeanUtil.getBatchObjectFromParam(params, NewDateSegment.class, rowNum);
        List htlOpenCloseRoomLis = new ArrayList();
        List updatePriceBeanLis = new ArrayList();

        for (int i = 0; i < ls.size(); i++) {
            NewDateSegment dateSegment = (NewDateSegment) ls.get(i);

            String[] weeks = dateSegment.getWeek();
            StringBuffer weekBuf = new StringBuffer();
            int iLength = 7;
            if (null != weeks) {
                iLength = weeks.length;
            }
            Integer[] weekInt = new Integer[iLength];
            if (null != weeks) {
                for (int k = 0; k < weeks.length; k++) {
                    // week += weeks[k] + ",";
                    weekBuf.append(weeks[k]).append(",");
                    String yys = weeks[k];
                    weekInt[k] = Integer.valueOf(yys);
                }
            } else {
                for (int k = 1; 8 > k; k++) {

                    weekBuf.append(k).append(",");
                    weekInt[k - 1] = Integer.valueOf(k);
                }

            }
            String week = weekBuf.toString();
            
            for (int j = 0; j < childRoomId.length; j++) {
                HtlOpenCloseRoom htlOpenCloseRoom = new HtlOpenCloseRoom();

                UpdatePriceBean updatePriceBean = new UpdatePriceBean();
                String strTemp = childRoomId[j];
                String[] strTemps = strTemp.split("&");
                htlOpenCloseRoom.setBeginDate(dateSegment.getStart());
                htlOpenCloseRoom.setCauseSign(causeSign);
                htlOpenCloseRoom.setCloseRoomTime(new Date());
                htlOpenCloseRoom.setEndDate(dateSegment.getEnd());
                htlOpenCloseRoom.setHotelId(hotelID);
                htlOpenCloseRoom.setOpCloseSign(BizRuleCheck.getGuanFangCode());
                htlOpenCloseRoom.setMessage(message);
                htlOpenCloseRoom.setRemark(notes);
                htlOpenCloseRoom.setRoomType(strTemps[0]);
                htlOpenCloseRoom.setRoomTypeId(strTemps[1]);
                htlOpenCloseRoom.setWeek(week);
                htlOpenCloseRoomLis.add(htlOpenCloseRoom);

                updatePriceBean.setBeginDate(dateSegment.getStart());
                updatePriceBean.setEndDate(dateSegment.getEnd());
                updatePriceBean.setChildRoomID(Long.valueOf(strTemps[1]).longValue());
                updatePriceBean.setRoomTypeID(Long.valueOf(strTemps[2]).longValue());
                updatePriceBean.setWeeks(weekInt);
                updatePriceBean.setCauseSign(causeSign);
                updatePriceBeanLis.add(updatePriceBean);

                /* 生产bug725 用户在CC中台的CC关房操作后没有记录操作人工号和姓名到日志中 add by chenajiejie 2009-08-10 */
                if (null != super.getOnlineRoleUser()) {
                    htlOpenCloseRoom.setCloseRoomOP(super.getOnlineRoleUser().getName());
                    if (super.getOnlineRoleUser().getName().equals("")) {
                        htlOpenCloseRoom.setCloseRoomOP(super.getBackUserName());
                    }
                } else {
                    htlOpenCloseRoom.setCloseRoomOP(super.getBackUserName());
                }
            }

        }
        roomControlManage.saveOpenCloseRoom(htlOpenCloseRoomLis, causeSign, Long.valueOf(hotelID));

        // 按天更新价格表改为在下面IF代码的外面，MODYFY BY ZHINENG ZHUANG
        for (int i = 0; i < updatePriceBeanLis.size(); i++) {
            UpdatePriceBean updatePriceBean = (UpdatePriceBean) updatePriceBeanLis.get(i);
            roomControlManage.updateHtlPriceWithCloseRoom(updatePriceBean);
        }
        
        /* 房控改版 当CC设置关房需要更新酒店工作列表 CC关房状态 add by chenjiajie 2010-01-15 begin */
        List<HtlRoomcontrolHotelSchedule> hotelScheduleList = roomControlManage.getHotelScheduleByHtlIdSchDate(hotelID, new Date());
        if(null != hotelScheduleList && !hotelScheduleList.isEmpty()){
        	for (HtlRoomcontrolHotelSchedule htlRoomcontrolHotelSchedule : hotelScheduleList) {
        		htlRoomcontrolHotelSchedule.setCcsetroomfullorclose(new Integer(1));
        		htlRoomcontrolHotelSchedule.setModifytime(new Date());
        		if (null != super.getOnlineRoleUser()) {
            		htlRoomcontrolHotelSchedule.setModifybyid(super.getOnlineRoleUser().getLoginName());
            		htlRoomcontrolHotelSchedule.setModifybyname(super.getOnlineRoleUser().getName());
                } else {
            		htlRoomcontrolHotelSchedule.setModifybyid("CC关房(取不到用户)");
            		htlRoomcontrolHotelSchedule.setModifybyname("CC关房(取不到用户)");
                }
        		roomControlManage.updateHtlRoomcontrolHotelSchedule(htlRoomcontrolHotelSchedule);
			}
        }
        /* 房控改版 当CC设置关房需要更新酒店工作列表 CC关房状态 add by chenjiajie 2010-01-15 end */
        
        // 如果为1，则是调价原因关房，需要生成调价工单
        if (BizRuleCheck.getChangePriceCode().equals(causeSign)) {
            // 生成调价工单
            newChangePrice();

        }
        // 如果新添了备注信息,那么就要更新相应的酒店的备注信息
        if (!(null == notes || notes.equals(""))) {
            hotelManage.updateHotelNotes(hotelID, notes);
        }
        return SUCCESS;
    }

    // 创建变价工单方法
    private void newChangePrice() {
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
        changePrice.setTaskCode(BizRuleCheck.getCreateChangePriceCode(hotelCD));
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

    public String getCauseSign() {
        return causeSign;
    }

    public void setCauseSign(String causeSign) {
        this.causeSign = causeSign;
    }

    public IChangePriceManage getChangePriceManage() {
        return changePriceManage;
    }

    public void setChangePriceManage(IChangePriceManage changePriceManage) {
        this.changePriceManage = changePriceManage;
    }

    public String[] getChildRoomId() {
        return childRoomId;
    }

    public void setChildRoomId(String[] childRoomId) {
        this.childRoomId = childRoomId;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String getHotelCD() {
        return hotelCD;
    }

    public void setHotelCD(String hotelCD) {
        this.hotelCD = hotelCD;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public List getHtlOpenCloseRoomItem() {
        return htlOpenCloseRoomItem;
    }

    public void setHtlOpenCloseRoomItem(List htlOpenCloseRoomItem) {
        this.htlOpenCloseRoomItem = htlOpenCloseRoomItem;
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lstRoomType) {
        this.lstRoomType = lstRoomType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String[] getOperates() {
        return operates;
    }

    public void setOperates(String[] operates) {
        this.operates = operates;
    }

    public RoomControlManage getRoomControlManage() {
        return roomControlManage;
    }

    public void setRoomControlManage(RoomControlManage roomControlManage) {
        this.roomControlManage = roomControlManage;
    }

    public String[] getRoomType() {
        return roomType;
    }

    public void setRoomType(String[] roomType) {
        this.roomType = roomType;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public boolean isUrgency() {
        return urgency;
    }

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
