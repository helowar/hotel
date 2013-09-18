package com.mangocity.hotel.order.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.base.service.assistant.RoomType;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * v2.6 修改基本信息，重新查询酒店相关action
 * 
 * @author chenkeming Feb 5, 2009 3:52:26 PM
 */
public class QueryBaseInfoAction extends GenericCCAction {

    private static final long serialVersionUID = 1545897139529168696L;

    /**
     * 查询入住日期
     * 
     * @author chenkeming Feb 5, 2009 6:00:28 PM
     */
    private Date beginDate;

    /**
     * 查询离店日期
     * 
     * @author chenkeming Feb 5, 2009 6:00:26 PM
     */
    private Date endDate;

    /**
     * 酒店服务bean
     * 
     * @author chenkeming Feb 5, 2009 6:01:16 PM
     */
    private IHotelService hotelService;

    /**
     * 返回页面的酒店查询结果类
     * 
     * @author chenkeming Feb 5, 2009 6:11:24 PM
     */
    private HotelInfo hotel;

    /**
     * 日期字符串数组
     * 
     * @author chenkeming Feb 5, 2009 6:11:36 PM
     */
    private List dateStrList = new ArrayList();

    /**
     * 是否是修改订单
     * 
     * @author chenkeming Feb 11, 2009 11:18:39 AM
     */
    private boolean forEditOrder;

    /**
     * 预订房间数
     * 
     * @author chenkeming Feb 16, 2009 9:07:38 AM
     */
    private int hotelCount;

    /**
     * 原单的子房型id
     * 
     * @author chenkeming Feb 17, 2009 2:02:48 PM
     */
    private long childRoomTypeId;

    /**
     * 是否已经创建预授权工单
     * 
     * @author chenkeming Feb 25, 2009 6:51:11 PM
     */
    private boolean hasCreatePreAuth;

    private long reservId;

    private double sumRmb;

    /**
     * 新下订单修改基本信息，重新查询酒店方法
     * 
     * @author chenkeming Feb 5, 2009 3:47:45 PM
     * @return
     * @throws SQLException 
     */
    public String queryBaseInfo() throws SQLException {
        member = getOnlineMember();
        roleUser = getOnlineRoleUser();
        Map map = super.getParams();

        HotelQueryCondition queryCond = new HotelQueryCondition();
        MyBeanUtil.copyProperties(queryCond, map);
        if (null == member) { // 如果当前没登录会员, 则根据roleUser属性设置
            if (roleUser.isOrg114()) {
                String memberState = roleUser.getState();
                if (StringUtil.isValidStr(memberState)) {
                    if (memberState.equals("WTBJ")) { // 北京网通
                        queryCond.setSaleChannel("04");
                    } else if (memberState.equals("WTT")) { // 网通
                        queryCond.setSaleChannel("05");
                    } else if (memberState.equals("LTT")) { // 联通
                        queryCond.setSaleChannel("02");
                    } else if (memberState.equals("GDLT")) { // 联通116114
                        queryCond.setSaleChannel("04");
                    } else if (!memberState.equals("NHZY")) { // 非南航的，就取电信114
                        queryCond.setSaleChannel("01");
                    }
                } else {
                    return forwardError("操作员没有省份!");
                }
            }
        } else if (!member.isMango()) {
            String memberState = member.getState();
            if (StringUtil.isValidStr(memberState)) {
                if (memberState.equals("WTBJ")) { // 北京网通
                    queryCond.setSaleChannel("04");
                } else if (memberState.equals("WTT")) { // 网通
                    queryCond.setSaleChannel("05");
                } else if (memberState.equals("LTT")) { // 联通
                    queryCond.setSaleChannel("02");
                } else if (memberState.equals("GDLT")) { // 联通116114
                    queryCond.setSaleChannel("04");
                } else if (!memberState.equals("NHZY")) { // 非南航的，就取电信114
                    queryCond.setSaleChannel("01");
                }
            } else {
                return forwardError("会员没有省份!");
            }
        }

        hotel = hotelService.queryBaseInfoHotel(queryCond);
        int difdays = DateUtil.getDay(beginDate, endDate);
        request.setAttribute("difdays", difdays);
        int weekNum = (difdays - 1) / 7 + 1;

        List roomTypes = hotel.getRoomTypes();
        for (int j = 0; j < roomTypes.size(); j++) {
            RoomType roomType = (RoomType) roomTypes.get(j);
            roomType.setTotalCount(roomType.getSaleItems().size() * weekNum);
        }
        if (null != hotel.getClueInfo()) {
            String temp = hotel.getClueInfo().replace("\r\n", "");
            hotel.setClueInfo(temp);
        }

        dateStrList = DateUtil.getDateStrList(beginDate, endDate, true);
        return SUCCESS;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public List getDateStrList() {
        return dateStrList;
    }

    public void setDateStrList(List dateStrList) {
        this.dateStrList = dateStrList;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public HotelInfo getHotel() {
        return hotel;
    }

    public void setHotel(HotelInfo hotelInfo) {
        this.hotel = hotelInfo;
    }

    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    public boolean isForEditOrder() {
        return forEditOrder;
    }

    public void setForEditOrder(boolean forEditOrder) {
        this.forEditOrder = forEditOrder;
    }

    public int getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(int hotelCount) {
        this.hotelCount = hotelCount;
    }

    public long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public boolean isHasCreatePreAuth() {
        return hasCreatePreAuth;
    }

    public void setHasCreatePreAuth(boolean hasCreatePreAuth) {
        this.hasCreatePreAuth = hasCreatePreAuth;
    }

    public long getReservId() {
        return reservId;
    }

    public void setReservId(long reservId) {
        this.reservId = reservId;
    }

    public double getSumRmb() {
        return sumRmb;
    }

    public void setSumRmb(double sumRmb) {
        this.sumRmb = sumRmb;
    }

}
