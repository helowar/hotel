package com.mangocity.hotel.base.web;

import java.util.List;

import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.manage.assistant.AddPrice;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class AddPriceAction extends HotelBatchAction {

    // 价格行数
    private int priceRowNum;

    private IPriceManage priceManage;

    /**
     * 配额类型
     */
    private String quotaType;

    private static final String BATCH_INPUT = "batchInput";

    private static final String VIEW = "view";

    private static final String QUERY = "query";

    public String view() {
        return VIEW;
    }

    public String query() {
        return QUERY;
    }

    /**
     * 批量录入加幅数据，系统将保存到数据当中
     * 
     * @return
     */
    public String batchInput() {

        List addPrices = MyBeanUtil.getBatchObjectFromParam(super.getParams(), AddPrice.class,
            priceRowNum);

        // 时间段
        dateList = MyBeanUtil.getBatchObjectFromParam(super.getParams(), DateSegment.class,
            dateRowNum);

        priceManage.batchAddPrice(hotelId, addPrices, dateList, quotaType, super.getIntWeek());

        return BATCH_INPUT;
    }

    public int getPriceRowNum() {
        return priceRowNum;
    }

    public void setPriceRowNum(int priceRowNum) {
        this.priceRowNum = priceRowNum;
    }

    public IPriceManage getPriceManage() {
        return priceManage;
    }

    public void setPriceManage(IPriceManage priceManage) {
        this.priceManage = priceManage;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

}
