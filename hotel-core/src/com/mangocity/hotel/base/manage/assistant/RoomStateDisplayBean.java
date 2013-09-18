package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 只用来显示按天调整房态记录
 * 
 * @author xiaowumi
 * 
 */
public class RoomStateDisplayBean implements Serializable {

    /**
     * 房间的日期
     */
    private Date saleRoomDate;

    /**
     * 星期几
     */
    private int week;

    /**
     * 房型列表，记录当前的日期中有多少个房型要显示,有多少预订条款
     */
    private List lstRoomAndBookItems = new ArrayList();
    
    /**
     * 这个list保存的是新临时配额的数据
     * add by haibo.li
     * 2.9.3V配额改造
     */
    private List lstTempQuota = new ArrayList();
    

    private HashMap mapRoomAndBook = new HashMap();
    
    
	public List getLstRoomAndBookItems() {
        return lstRoomAndBookItems;
    }

    public void setLstRoomAndBookItems(List lstRoomAndBookItems) {
        this.lstRoomAndBookItems = lstRoomAndBookItems;
    }

    public Date getSaleRoomDate() {
        return saleRoomDate;
    }

    public void setSaleRoomDate(Date saleRoomDate) {
        this.saleRoomDate = saleRoomDate;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public HashMap getMapRoomAndBook() {
        return mapRoomAndBook;
    }

    public void setMapRoomAndBook(HashMap mapRoomAndBook) {
        this.mapRoomAndBook = mapRoomAndBook;
    }

	public List getLstTempQuota() {
		return lstTempQuota;
	}

	public void setLstTempQuota(List lstTempQuota) {
		this.lstTempQuota = lstTempQuota;
	}

}
