package com.mangocity.tmc.parameter;

import java.util.Date;


/**
 * 用于传入SqlMappContract的参数类
 * @author:shizhongwen
 * 创建日期:Sep 24, 2009,5:22:28 PM
 * 描述：
 */
public class SqlMappContractParameter {
    private Long companyid; //商旅公司ID 
    private Long hotelid ;//酒店ID
    private String startDate;//入住日期
    private String endDate;//离店日期
    public Long getCompanyid() {
        return companyid;
    }
    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }
    public Long getHotelid() {
        return hotelid;
    }
    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    } 
  

}
