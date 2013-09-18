package com.mangocity.hotel.newroomcontrol.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IPriceDao;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IChangePriceManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.base.manage.assistant.UpdatePriceBean;
import com.mangocity.hotel.base.persistence.HtlChangePrice;
import com.mangocity.hotel.base.persistence.HtlChangePriceLog;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.NewDateSegment;
import com.mangocity.util.hotel.constant.BaseConstant;

public class NewDisplayPriceAction extends PersistenceAction {

    /**
     * 房间id
     */
    private long roomId;

    /**
     * 酒店id
     */
    private long hotelId;
    

    /**
     * 价格列表
     */
    private List lstPrice = new ArrayList();

    private IPriceDao priceDao;
    private RoomControlManage roomControlManage;
    
    private HotelRoomTypeService hotelRoomTypeService;
    
    private List lstRoomType = new ArrayList();
    private HotelManage hotelManage;
    private String hotelCD;
    private HtlHotel htlHotel;
    private int rowNum;
    private String[] childRoomId;
    private String causeSign;
    private String message;

    private String remark;
    
    /**
     * 是否紧急变价
     */
    private boolean urgency;
    
    /**
     * 变价工单管理类
     */
    private IChangePriceManage changePriceManage;
    
    
    public String displayPrice() {
        lstPrice = priceDao.getRoomPrices(roomId);
        super.getOnlineRoleUser();
        htlHotel = hotelManage.findHotel(hotelId);
        hotelCD = htlHotel.getHotelCd();
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        return "displayPrice";
    }

    // 关房操作
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
                htlOpenCloseRoom.setHotelId(hotelId);
                htlOpenCloseRoom.setOpCloseSign(BizRuleCheck.getGuanFangCode());
                htlOpenCloseRoom.setRemark(remark);
                htlOpenCloseRoom.setMessage(message);
                htlOpenCloseRoom.setRoomType(strTemps[0]);
                htlOpenCloseRoom.setRoomTypeId(strTemps[1]);
                htlOpenCloseRoom.setWeek(week);
                if (null != super.getOnlineRoleUser()) {
                    htlOpenCloseRoom.setCloseRoomOP(super.getOnlineRoleUser().getName());
                    if (super.getOnlineRoleUser().getName().equals("")) {
                        htlOpenCloseRoom.setCloseRoomOP(super.getBackUserName());
                    }
                } else {
                    htlOpenCloseRoom.setCloseRoomOP(super.getBackUserName());
                }
                htlOpenCloseRoomLis.add(htlOpenCloseRoom);

                updatePriceBean.setBeginDate(dateSegment.getStart());
                updatePriceBean.setEndDate(dateSegment.getEnd());
                updatePriceBean.setChildRoomID(Long.valueOf(strTemps[1]).longValue());
                updatePriceBean.setRoomTypeID(Long.valueOf(strTemps[2]).longValue());
                updatePriceBean.setWeeks(weekInt);
                updatePriceBean.setCauseSign(causeSign);
                updatePriceBeanLis.add(updatePriceBean);
            }

        }
        roomControlManage.saveOpenCloseRoom(htlOpenCloseRoomLis, causeSign, Long.valueOf(hotelId));

        // 按天更新价格表
        for (int i = 0; i < updatePriceBeanLis.size(); i++) {
            UpdatePriceBean updatePriceBean = (UpdatePriceBean) updatePriceBeanLis.get(i);
            roomControlManage.updateHtlPriceWithCloseRoom(updatePriceBean);
        }
        // 如果为1，则是调价原因关房，需要生成调价工单
        if (BizRuleCheck.getChangePriceCode().equals(causeSign)) {
            // 生成调价工单
            newChangePrice();
        }

        // 更新酒店外网是否显示基本信息字段
        String webShowBase = (String) params.get("webShowBaseInfo");
        hotelManage.updateHotelWebShowBase(hotelId, webShowBase);

        // 如果新添了备注信息,那么就要更新相应的酒店的备注信息
        if (!(null == remark || remark.equals(""))) {
            hotelManage.updateHotelNotes(hotelId, remark);
        }
        message="保存成功!";
        return displayPrice();
    }
    
    // 创建变价工单方法
    private void newChangePrice() {
        HtlChangePrice changePrice = new HtlChangePrice();
        changePrice.setHotelId(hotelId);
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
    
    public List getLstPrice() {
        return lstPrice;
    }

    public void setLstPrice(List lstPrice) {
        this.lstPrice = lstPrice;
    }

    public IPriceDao getPriceDao() {
        return priceDao;
    }

    public void setPriceDao(IPriceDao priceDao) {
        this.priceDao = priceDao;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

	public List getLstRoomType() {
		return lstRoomType;
	}

	public void setLstRoomType(List lstRoomType) {
		this.lstRoomType = lstRoomType;
	}

	public RoomControlManage getRoomControlManage() {
		return roomControlManage;
	}

	public void setRoomControlManage(RoomControlManage roomControlManage) {
		this.roomControlManage = roomControlManage;
	}

	public String getHotelCD() {
		return hotelCD;
	}

	public void setHotelCD(String hotelCD) {
		this.hotelCD = hotelCD;
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