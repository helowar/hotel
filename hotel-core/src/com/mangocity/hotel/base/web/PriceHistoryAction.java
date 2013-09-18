package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.IChangePriceManage;
import com.mangocity.hotel.base.manage.assistant.ChildRoomTypeInfo;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;

/**
 * @author yuexiaofeng
 * 
 */
public class PriceHistoryAction extends PersistenceAction {

    private String forward = "query";

    private long hotelID;

    private String startDate;

    private String endDate;

    private long priceTypeID;

    private Map<Long, String> priceType = new HashMap<Long, String>();

    private IChangePriceManage changePriceManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private List childRoomInfoLis = new ArrayList();

    private List priceLis = new ArrayList();

    private String quotaType;

    private String payMethod;

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String forward() {
        commonRoomType(hotelID);
        return forward;
    }

    public String queryPriceHistory() {
        commonRoomType(hotelID);
        HashMap params = new HashMap();
        if (null != startDate && !"".equals(startDate)) {
            params.put("startDate", DateUtil.getDate(startDate));
            params.put("endDate", DateUtil.getDate(endDate));
        }
        params.put("payMethod", payMethod);
        params.put("quotaType", quotaType);
        params.put("priceTypeID", Long.valueOf(priceTypeID));
        priceLis = changePriceManage.queryPriceHistory(params);
        return forward;
    }

    public void commonRoomType(long hotelID) {
        List htlRoomtypeLis = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
        int roomTypeSize = htlRoomtypeLis.size();
        for (int i = 0; i < roomTypeSize; i++) {
            HtlRoomtype htlRoomtype = (HtlRoomtype) htlRoomtypeLis.get(i);
            ChildRoomTypeInfo childRoomTypeInfo = new ChildRoomTypeInfo();
            int priceTypeSize = htlRoomtype.getLstPriceType().size();
            List htlPriceTypeLis = htlRoomtype.getLstPriceType();
            for (int j = 0; j < priceTypeSize; j++) {
                HtlPriceType htlPriceType = (HtlPriceType) htlPriceTypeLis.get(j);
                priceType.put(htlPriceType.getID(), htlPriceType.getPriceType());
            }
            childRoomTypeInfo.setPriceType(priceType);
            priceType = new HashMap<Long, String>();
            childRoomTypeInfo.setChildRoomId(htlRoomtype.getID().toString());
            childRoomTypeInfo.setChildRoomName(htlRoomtype.getRoomName());
            if (0 != priceTypeSize) {
                childRoomInfoLis.add(childRoomTypeInfo);
            }
        }
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public List getChildRoomInfoLis() {
        return childRoomInfoLis;
    }

    public void setChildRoomInfoLis(List childRoomInfoLis) {
        this.childRoomInfoLis = childRoomInfoLis;
    }

    public long getPriceTypeID() {
        return priceTypeID;
    }

    public void setPriceTypeID(long priceTypeID) {
        this.priceTypeID = priceTypeID;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<Long, String> getPriceType() {
        return priceType;
    }

    public void setPriceType(Map<Long, String> priceType) {
        this.priceType = priceType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List getPriceLis() {
        return priceLis;
    }

    public void setPriceLis(List priceLis) {
        this.priceLis = priceLis;
    }

    public IChangePriceManage getChangePriceManage() {
        return changePriceManage;
    }

    public void setChangePriceManage(IChangePriceManage changePriceManage) {
        this.changePriceManage = changePriceManage;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
