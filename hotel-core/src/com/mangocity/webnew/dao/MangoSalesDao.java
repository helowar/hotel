package com.mangocity.webnew.dao;

import java.util.Date;
import java.util.List;

/** Add by Shengwei.Zuo 2009-02-01 根据酒店ID查询出该酒店
 */
/** edit by diandian.hou 2010-10-13 refactor
 */
public interface MangoSalesDao {
    
    /**
     * 得到价格类型信息
     */
    //public List getPriceType(Long hotelId,Long roomTypeId,Long priceTypeId);
	public List getRoomType(Long hotelid,Long roomTypeID,Long priceTypeId);
    
    /**
     * 得到销售价
     */
    //public List getSalePrice(Long hotelId,LIng roomTypeId,Long priceTypeId,Date CurrencyDate);
    public List getSalePrice(Long hotelid, Long roomTypeID, Long priceTypeId,Date currDate);
    
    /**
     * 判断是不是如家酒店，可以和OrderImmedConfirmService中的validateRujiaHotel方法合并的
     */
    //public List IsRuJiaHotel(Long hotelId);
    public List getRuJiaHotel(Long hotelid);

}
