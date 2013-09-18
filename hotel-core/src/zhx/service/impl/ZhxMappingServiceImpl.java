package zhx.service.impl;

import java.util.List;

import zhx.constant.ZhxChannelType;
import zhx.dao.IZhxDao;
import zhx.service.IZhxMappingService;

import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.util.hotel.constant.HotelMappingType;


/**
 * 
 * 中航信的映射相关操作Service实现类
 * 
 * @author chenkeming
 *
 */
public class ZhxMappingServiceImpl implements IZhxMappingService {

    private IZhxDao zhxDao;
    
	/**
	 * 
	 * 查询中航信的所有激活状态的酒店
	 * 
	 * @param channelType
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<ExMapping> getAllZhxHotel(){
		String hsql = " from ExMapping m where m.type = 1 and m.isActive = '1' and m.channeltype = ?";		
		return (List<ExMapping>)zhxDao.query(hsql, new Long[]{Long.valueOf(ZhxChannelType.CHANNEL_ZHX)});		
	}

    /**
     * 
     * 根据酒店ID查询中航信的所有激活状态的价格计划
     * 
     * @param hotelId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<ExMapping> getRatePlanByHotel(Long hotelId){
		String hsql = " from ExMapping m where m.type = 3 and m.isActive = '1' and m.channeltype = ?" +
				" and m.hotelid = ?";		
		return (List<ExMapping>)zhxDao.query(hsql, new Long[]{Long.valueOf(ZhxChannelType.CHANNEL_ZHX), hotelId});		
	}
    
    /**
     * 根据渠道、酒店ID，房型id、子房型id获取直连价格计划编码信息 add by shizhongwen 时间:Mar 17, 2009 2:24:21 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @return
     */
    public ExMapping getMapping(long channelType, long hotelId, long roomTypeId,
        long childRoomTypeId) {
        String hsql = " from ExMapping where channeltype=? and hotelid=? and roomTypeId=? and priceTypeId=? and type=?" ;
        return (ExMapping) zhxDao.find(hsql, new Object[]{ channelType, hotelId,roomTypeId,childRoomTypeId,HotelMappingType.PRICE_TYPE});
    }

	/**
	 * 
	 * 保存mapping对象
	 * 
	 * @param mapping
	 */
	public void saveMapping(ExMapping mapping) {
		zhxDao.saveOrUpdate(mapping);
	}
    
    public void setZhxDao(IZhxDao zhxDao) {
		this.zhxDao = zhxDao;
	}
    
}
