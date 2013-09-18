package com.mangocity.tmc.manage;

import java.util.Date;
import java.util.List;

import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.tmc.persistence.TbhotelContract;

/**
 * tmc应用接口
 * @author:shizhongwen
 * 创建日期:Sep 24, 2009,5:55:44 PM
 * 描述：
 */
public interface TmcManage {    

    /**
     * 根据商旅公司ID，酒店ID，入住日期，离店日期查询商旅公司与酒店签订的三方协义合同
     * add by shizhongwen
     * 时间:Sep 24, 2009  4:48:23 PM
     * @param companyid //商旅公司ID 
     * @param hotelid //酒店ID
     * @param startDate //入住日期
     * @param endDate //离店日期
     * @return
     */
    public TbhotelContract getTbhotelContract(Long companyid,Long hotelid,Date startDate,Date endDate);
    
    /**
     * 判断此酒店是否为此商旅公司的三方协议酒店
     * add by shizhongwen
     * 时间:Sep 24, 2009  5:40:51 PM
     * @param companyid
     * @param hotelid
     * @param startDate
     * @param endDate
     */
    public boolean isAgreementHotel(Long companyid,Long hotelid,Date startDate,Date endDate);
    
    /**
     * 根据酒店网站查询条件类查询此酒店是否在散客里也有
     * add by shizhongwen
     * 时间:Sep 27, 2009  4:09:05 PM
     * @param queryHotelForWebBean
     * @return
     */
    public boolean isPriceTypeOfHotel(QueryHotelForWebBean queryHotelForWebBean);

}
