package zhx.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.ExMapping;

/**
 * 
 * 中航信的映射相关service
 * 
 * @author chenkeming
 *
 */
public interface IZhxMappingService {
	
	/**
	 * 
	 * 查询中航信的所有激活状态的酒店
	 * 
	 * @param channelType
	 * @return
	 */
	public List<ExMapping> getAllZhxHotel();

    /**
     * 
     * 根据酒店ID查询中航信的所有激活状态的价格计划
     * 
     * @param hotelId
     * @return
     */
	public List<ExMapping> getRatePlanByHotel(Long hotelId);
	
    /**
     * 根据渠道、酒店ID，房型id、子房型id获取直连价格计划编码信息
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @return
     */
    public ExMapping getMapping(long channelType, long hotelId, long roomTypeId,
        long childRoomTypeId);
	
	/**
	 * 
	 * 保存mapping对象
	 * 
	 * @param mapping
	 */
	public void saveMapping(ExMapping mapping);
}
