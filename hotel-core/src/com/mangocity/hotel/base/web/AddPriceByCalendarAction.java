package com.mangocity.hotel.base.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class AddPriceByCalendarAction extends GenericAction {

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

    private HotelRoomTypeService hotelRoomTypeService;

    private IPriceManage priceManage;

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

    private String quotaType;

    // 价格行数
    private int priceRowNum;

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
        super.getParams().put("ISINIT", "NO");
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
        // 取到价格ID、价格、上一次加幅、修改后的加幅、能否加幅这几个数据
        Map params = super.getParams();
        // 价格
        lstPrice = MyBeanUtil.getBatchObjectFromParam(params, HtlPrice.class, priceRowNum);
        log.debug("priceList====" + lstPrice.size());
        log.debug("priceRowNum====" + priceRowNum);
        // 根据价格ID，取到价格记录
        for (int i = 0; i < lstPrice.size(); i++) {
            HtlPrice hp = (HtlPrice) lstPrice.get(i);

            HtlPrice price = priceManage.getHtlPrice(hp.getID().longValue());

            // 新价格＝价格－上一次加幅＋修改后的加幅
            price.setSalePrice(price.getSalePrice() - price.getAddScope() + hp.getAddScope());

            // 修改新数据：新价格、新加幅、能否加幅
            price.setAddScope(hp.getAddScope());
            // 保存数据
            priceManage.updateHtlPrice(price);

        }
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

    public int getPriceRowNum() {
        return priceRowNum;
    }

    public void setPriceRowNum(int priceRowNum) {
        this.priceRowNum = priceRowNum;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
