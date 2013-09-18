package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.manage.HotelPriorityManage;
import com.mangocity.hotel.base.persistence.HtlSetPriority;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class HotelSetPriorityAction extends PersistenceAction {

    /**
     * 酒店Id
     */
    private long hotelId;

    /*
     * 酒店询房优先级
     */
    private String priSeach;

    /*
     * 酒店CC优先级
     */
    private String priCc;
    
    /**
     * 酒店询房时段
     */
    
    private String priTime;

    /*
     * 酒店外网优先级
     */
    private String priWeb;

    /*
     * 持久化类
     */
    private HtlSetPriority htlSetPriority;

    /*
     * 数据库操作
     */
    private HotelPriorityManage hotelPriorityManage;

    protected Class getEntityClass() {
        return HtlSetPriority.class;
    }

    /*
     * 更新优先信息
     */
    public String updatePriority() {
        htlSetPriority = hotelPriorityManage.findHotelPriority(hotelId);
        if (null == htlSetPriority) {
            htlSetPriority = new HtlSetPriority();
        }
        htlSetPriority.setHotelId(hotelId);
        htlSetPriority.setPriCc(priCc);
        htlSetPriority.setPriSeach(priSeach);
        htlSetPriority.setPriWeb(priWeb);
        htlSetPriority.setPriTime(priTime);
        int resultPri = hotelPriorityManage.updatePriority(htlSetPriority);
        if (0 != resultPri) {
            return ERROR;
        }
        return SUCCESS;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public HtlSetPriority getHtlSetPriority() {
        return htlSetPriority;
    }

    public void setHtlSetPriority(HtlSetPriority htlSetPriority) {
        this.htlSetPriority = htlSetPriority;
    }

    public HotelPriorityManage getHotelPriorityManage() {
        return hotelPriorityManage;
    }

    public void setHotelPriorityManage(HotelPriorityManage hotelPriorityManage) {
        this.hotelPriorityManage = hotelPriorityManage;
    }

    public String getPriCc() {
        return priCc;
    }

    public void setPriCc(String priCc) {
        this.priCc = priCc;
    }

    public String getPriSeach() {
        return priSeach;
    }

    public void setPriSeach(String priSeach) {
        this.priSeach = priSeach;
    }

    public String getPriWeb() {
        return priWeb;
    }

    public void setPriWeb(String priWeb) {
        this.priWeb = priWeb;
    }

	public String getPriTime() {
		return priTime;
	}

	public void setPriTime(String priTime) {
		this.priTime = priTime;
	}

}
