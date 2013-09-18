package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mangocity.hotel.order.persistence.DaDailyauditItem;

/**
 * 日审记录详情
 * 用于auditDetail.jsp的显示
 * 
 * @author chenjuesu
 * 
 */
public class DaAuditDetailInfo implements Serializable {

    /**
	 * 酒店名
	 */
    private String hotelName;
    /**
	 * 酒店Id
	 */
    private Long hotelId;
    /**
	 * 审核入住的集合
	 */
    private List<DaDailyauditItem> checkInItems = new ArrayList<DaDailyauditItem>();
    /**
	 * 审核退房的集合
	 */
    private List<DaDailyauditItem> checkOutItems =  new ArrayList<DaDailyauditItem>();
    
    public String getHotelName() {
        return hotelName;
    }
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    public List<DaDailyauditItem> getCheckInItems() {
        return checkInItems;
    }
    public void setCheckInItems(List<DaDailyauditItem> checkInItems) {
        this.checkInItems = checkInItems;
    }
    public List<DaDailyauditItem> getCheckOutItems() {
        return checkOutItems;
    }
    public void setCheckOutItems(List<DaDailyauditItem> checkOutItems) {
        this.checkOutItems = checkOutItems;
    }
    public Long getHotelId() {
        return hotelId;
    }
    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    };
    
    class ComparatorDaAuditDetailInfo implements Comparator<DaDailyauditItem>{

		public int compare(DaDailyauditItem o1, DaDailyauditItem o2) {
			// TODO Auto-generated method stub
			return o1.getOrderid().compareTo(o2.getOrderid());
		}
    }
    public void sort(){
    	ComparatorDaAuditDetailInfo compatorAuditDetail = new ComparatorDaAuditDetailInfo();
    	Collections.sort(checkInItems, compatorAuditDetail);
    	Collections.sort(checkOutItems, compatorAuditDetail);
    }
}
