package com.mangocity.hotel.base.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class AdjustPriceByDayExtAction extends GenericAction {

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 合同id
     */
    private long contractId;

    /**
     * 合同开始日期
     */
    private Date beginDate;

    /**
     * 合同结束日期
     */
    private Date endDate;

    private IPriceManage priceManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private List lstRoomType;

    private List lstPrice;

    /**
     * 从页面传入参数
     */
    private List lstDate;

    /**
     * 页面传入多个日期段的数量
     */
    private int dateRowNum;

    private String[] week;

    private String[] priceTypeIds;

    /**
     * 配额类型
     */
    private String quotaType;

    // 保存选中的星期
    private String htlWeek;

    // 保存选中的房型
    private String htlRoomType;

    private String currency;

    /**
     * 保持前面页面的调价提示
     */
    private String changePriceHint;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 初始化按日历查询加幅界面
     * 
     * @return
     */
    public String initQueryPrice() {
        super.getParams().put("ISINIT", "YES");
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        return "initQueryPrice";
    }

    /**
     * 查询要加幅的价格记录
     * 
     * @return
     */
    public String queryPriceByCalendar() {
        Map params = super.getParams();
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        lstDate = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, dateRowNum);
        lstPrice = priceManage.queryPriceInAddPrice(hotelId, lstDate, week, priceTypeIds, quotaType, null);

        return "queryPriceByCalendar";
    }

    /**
     * 更新修改结果。
     * 
     * @return
     */
    public String updateAddPriceByCalendar() {
        return "updateAddPriceByCalendar";
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public IPriceManage getPriceManage() {
        return priceManage;
    }

    public void setPriceManage(IPriceManage priceManage) {
        this.priceManage = priceManage;
    }

    public List getLstPrice() {
        return lstPrice;
    }

    public void setLstPrice(List lstPrice) {
        this.lstPrice = lstPrice;
    }

    public int getDateRowNum() {
        return dateRowNum;
    }

    public void setDateRowNum(int dateRowNum) {
        this.dateRowNum = dateRowNum;
    }

    public List getLstDate() {
        return lstDate;
    }

    public void setLstDate(List lstDate) {
        this.lstDate = lstDate;
    }

    public String[] getPriceTypeIds() {
        return priceTypeIds;
    }

    public void setPriceTypeIds(String[] priceTypeIds) {
        this.priceTypeIds = priceTypeIds;
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getHtlRoomType() {
        return htlRoomType;
    }

    public void setHtlRoomType(String htlRoomType) {
        this.htlRoomType = htlRoomType;
    }

    public String getHtlWeek() {
        return htlWeek;
    }

    public void setHtlWeek(String htlWeek) {
        this.htlWeek = htlWeek;
    }

    public String getChangePriceHint() {
        return changePriceHint;
    }

    public void setChangePriceHint(String changePriceHint) {
        this.changePriceHint = changePriceHint;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
