package com.mangocity.hotel.util;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IncreaseBean;

/**
 * 刷新加幅的Job
 * 
 * @author chenjiajie
 * 
 */
public class IncreaseSchedule extends QuartzJobBean {


    private HotelManage hotelManage;

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        // ��ʱ����ɼӷ��¼
        IncreaseBean.increaseMap = hotelManage.getIncreasePriceMap();
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

}
