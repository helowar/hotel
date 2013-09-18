package com.mangocity.hotel.base.manage;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.manage.assistant.RoomControlBean;
import com.mangocity.hotel.base.manage.assistant.UpdatePriceBean;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomtype;

/**
 */
public interface RoomControlManage {

    /*
     * 查找房间
     */
    public HtlRoom findRoom(HtlRoom room);

    /*
     * 修改房间
     */
    public int updateRoom(RoomControlBean roomBean);

    /*
     * 新增关房记录
     */
    public void saveOpenCloseRoom(List<HtlOpenCloseRoom> htlOpenCloseRooms, String code, Long hotelId);

    /*
     * 修改价格表（如果业务关房)
     */
    public int updateHtlPriceWithCloseRoom(UpdatePriceBean updatePriceBean);

    /*
     * 查询开关房记录表
     */
    public List queryHtlOpenCloseRoom(long hotelID);

    /**
     * 查询一条开关房记录，根据记录的ID
     */
    public HtlOpenCloseRoom findhtlOpenCloseRoom(long ID);

    /**
     * 检查合同的起止时间是否重复
     * 
     * @param 酒店id
     *            ,起止时间
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkMainCommendDate(long commendId, long hotelid, String beginDate, String endDate,
        String bDate, String eDate);

    /*
     * 修改价格表（如果业务开房)
     */
    public int updateHtlPriceWithOpenRoom(UpdatePriceBean updatePriceBean);
    
    /**
     * 根据酒店ID，查询出有配额预警的房型列表 
     * 
     * hotel2.9.3 add by shengwei.zuo  2009-10-21
     * 
     * @param hotelId
     * @return
     */
    public List getRoomTypeHavaForewarn(long hotelId);

	public List<HtlRoomtype> getRoomTypeCCSetted(long hotelID);
	
	/**
	 * 根据酒店ID和计划日期获取计划
	 * 
	 * @param hotelId
	 * @param schDate
	 * @return
	 */
	public List<HtlRoomcontrolHotelSchedule> getHotelScheduleByHtlIdSchDate(long hotelId, Date schDate);
	
	/**
	 * 更新酒店计划
	 * 
	 * @param htlRoomcontrolHotelSchedule
	 */
	public void updateHtlRoomcontrolHotelSchedule(HtlRoomcontrolHotelSchedule htlRoomcontrolHotelSchedule);
	
	public void updateScheduleWarnFlag(Long hotelId, Date schDate);

}
