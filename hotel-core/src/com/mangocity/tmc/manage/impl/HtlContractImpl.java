package com.mangocity.tmc.manage.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.tmc.manage.IHtlContract;
import com.mangocity.tmc.persistence.HotelContractPrice;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;
/**
 * 商旅会员查询合同类
 * 
 * @author:shizhongwen 创建日期:Jul 22, 2009,5:54:37 PM 描述：
 */
public class HtlContractImpl extends DAOHibernateImpl implements IHtlContract {

	private static final MyLog log = MyLog.getLogger(HtlContractImpl.class);
    private static final long serialVersionUID = 7818399524283745198L;
    private DAOIbatisImpl queryDao;

    /**
     * 根据协议酒店合同id, 协议酒店id, 预订开始和结束日期查询间夜价格
     * @param hotelcontractid
     * @param hotelid
     * @param roomtypeid
     * @param beginDate 开始日
     * @param endDate  离店日
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<HotelContractPrice> queryHotelcontractPrice(long hotelcontractid, long hotelid, long roomtypeid, Date beginDate, Date endDate) {
        
        List<HotelContractPrice> ret = new ArrayList<HotelContractPrice>();
        try{
            Map params = new HashMap<String, String>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            params.put("hotelcontractid", "" + hotelcontractid);
            params.put("hotelid", "" + hotelid);
            params.put("roomtypeid", "" + roomtypeid);
            params.put("beginDate",  sdf.format(DateUtil.getDate(beginDate, -1)));
            //不用查离店日价格
            params.put("endDate",  sdf.format(DateUtil.getDate(endDate, -1)));
            
            ret = queryDao.queryForList("queryHotelcontractPrice", params);
        }catch(Exception e){
            log.error("根据协议酒店合同id, 协议酒店id, 预订开始和结束日期查询间夜价格报错："+e);
        }
        return ret;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

}
