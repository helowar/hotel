package com.mangocity.hdl.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hdl.dao.IExDao;
import com.mangocity.hdl.service.IExMappingService;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.util.hotel.constant.HotelMappingType;

public class ExMappingServiceImpl implements IExMappingService {

	private IExDao exHdlDao;

	/**
	 * 根据芒果编码信息获取直连价格计划编码信息
	 * @param channelType
	 * @param hotelId
	 * @param roomTypeId
	 * @param childRoomTypeId
	 * @return
	 */
	public ExMapping getMapping(long channelType, long hotelId,
			long roomTypeId, long childRoomTypeId) {
		String hsql = " from ExMapping where channeltype = ? "
				+ " and hotelid= ? and roomTypeId= ?" + " and priceTypeId= ?"
				+ " and type= ? ";

		return (ExMapping) exHdlDao.find(hsql, new Object[] { channelType,
				hotelId, String.valueOf(roomTypeId),
				String.valueOf(childRoomTypeId), HotelMappingType.PRICE_TYPE });
	}

	/**
	 * 根据芒果酒店id列表获取直连编码信息
	 * @param channelType
	 * @param hotelIds
	 * @return
	 */
	public List getMapping(long channelType, List<Long> hotelIds) {
		if (hotelIds == null || hotelIds.size() <= 0) {
			return null;
		}
		String hsql = " from ExMapping where channeltype=" + channelType
				+ " and type=" + HotelMappingType.HOTEL_TYPE + " and (";
		for (int i = 0; i < hotelIds.size(); i++) {
			Long hotelId = hotelIds.get(i);
			if (i > 0) {
				hsql += " or ";
			}
			hsql += "hotelid=" + hotelId;
		}
		hsql += ")";
		return exHdlDao.query(hsql, null);
	}

	/**
	 * 根据芒果编码信息获取直连房型编码信息
	 * @param channelType
	 * @param hotelId
	 * @param roomTypeId
	 * @return
	 * @author chenjiajie
	 */
	public ExMapping getMapping(long channelType, long hotelId, long roomTypeId) {
		String hsql = " from ExMapping where channeltype=? and hotelid=? and roomTypeId=? and type=?";
		return (ExMapping) exHdlDao.find(hsql, new Object[]{channelType,hotelId,roomTypeId,HotelMappingType.ROOM_TYPE});
	}

	/**
	 * 根据芒果编码信息获取直连房型编码的价格计划信息
	 * @param channelType
	 * @param hotelId
	 * @param roomTypeId
	 * @return
	 * @author chenjiajie
	 */
	public List getPriceMapping(long channelType, long hotelId, long roomTypeId) {
		String hsql = " from ExMapping where channeltype=? and hotelid=? and roomTypeId=? and type=?";
		return exHdlDao.query(hsql, new Object[]{channelType,hotelId,roomTypeId,HotelMappingType.PRICE_TYPE});
	}

	/**
	 * 查询合作方的所有可用,处于激活状态的价格计划
	 * @param channelType
	 * @return
	 * @author guojun
	 */
	public List getAllRoomTypeMapping(long channelType) {
		//String hsql = " from ExMapping where channeltype=? and type=3 and isActive=1 ";
		String hsql = " from ExMapping m";
		hsql = hsql + " where m.hotelid IN ";
		hsql = hsql + " ( ";
		hsql = hsql + "    SELECT hotelid ";
		hsql = hsql + "    FROM ExMapping ex ";
		hsql = hsql + "    WHERE ex.type = 1 ";
		hsql = hsql + "    AND ex.isActive =1 ";
		hsql = hsql + "    AND ex.channeltype = ? ";
		hsql = hsql + " ) ";
		hsql = hsql + "  AND m.type = 3 ";
		hsql = hsql + "  AND m.isActive = 1 ";
		hsql = hsql + "  AND m.hotelname NOT LIKE '%如家%' ";
		hsql = hsql + "  AND m.hotelname NOT LIKE '%莫泰%' ";
		hsql = hsql + "  AND m.hotelcodeforchannel NOT LIKE '%HOM%' ";
		hsql = hsql + "  AND m.hotelcodeforchannel NOT LIKE '%MOTMT%' ";
		hsql = hsql + "  AND m.hotelname NOT LIKE '%快捷假日%'  ";
		hsql = hsql + "  AND m.hotelcodeforchannel NOT LIKE '%IHG%' ";
		//下面这行测试的时候用，正常要注释掉
		//hsql = hsql + "  AND m.exMappingId = 1883 ";

		return exHdlDao.query(hsql, new Object[] { channelType });

	}

	/**
	 * 根据合作方的编码类型读取合作方的映射信息
	 * @param channelType
	 * @param hotelCodeChannel
	 * @param roomCodeChannel
	 * @return
	 */
	public List getColPriceMapping(long channelType, String hotelCodeChannel,
			String roomCodeChannel) {
		String hsql = "";
		hsql = hsql + " From ExMapping ";
		hsql = hsql + " where channeltype= ? ";
		hsql = hsql + " and hotelcodeforchannel=? ";
		hsql = hsql + " and roomtypecodeforchannel= ? ";
		hsql = hsql + " and type= ? ";
		return exHdlDao.query(hsql, new Object[] { channelType, hotelCodeChannel,
				roomCodeChannel, HotelMappingType.PRICE_TYPE });
	}

	/**
	 * 根据type查询所有属于type类型的Mapping
	 * @author:shizhongwen
	 * @date:Mar 8, 2010  11:16:44 AM
	 * @param type
	 * @return
	 */
	public List getAllPriceMapping(long type) {
		String hsql = " from ExMapping m where m.type=?";
		return exHdlDao.query(hsql, new Object[] { type });
	}

	/**
	 * 根据渠道，酒店ID，房型ID， 查询相应的Exampping 
	 * @param channelType
	 * @param hotelid
	 * @param roomtypeid  
	 * @param type  
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public List<ExMapping> getHotelRoomMapping(long channelType, long hotelid,
			String roomtypeid, long type) {
		List<ExMapping> dataLis = new ArrayList<ExMapping>();
		String hqlStr = "";
		hqlStr = "from ExMapping  where channeltype=? and hotelid=? and roomTypeId=? and type=?";
		dataLis = exHdlDao.query(hqlStr, new Object[] { channelType, hotelid,
				roomtypeid, type });

		return dataLis;
	}

	public void setExHdlDao(IExDao exHdlDao) {
		this.exHdlDao = exHdlDao;
	}


}
