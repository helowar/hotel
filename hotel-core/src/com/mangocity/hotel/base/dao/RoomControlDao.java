package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.mangocity.hotel.base.manage.assistant.RoomControlBean;
import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.manage.assistant.UpdatePriceBean;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.assistant.RoomBedRecord;

public interface RoomControlDao {

	/**
	 * 根据给定的RoomControlBean更新HtlRoom
	 * @param roomBean
	 * @return
	 * @throws HibernateException
	 */
	public int update(RoomControlBean roomBean)
			throws HibernateException;

	/**
	 * 根据给定的RoomStateBean更新HtlRoom
	 * @param roomStateBean
	 * @return
	 */
	public int updateRoomStatu(RoomStateBean roomStateBean);

	

	/**
	 * 根据给定的房型ID、房态信息更新
	 * @param roomId
	 * @param roomState
	 * @return
	 */
	public int updateRoomState(long roomId,
			final String roomState);

	/**
	 * 根据酒店id 查询开关房记录
	 * @param hotelId
	 * @return
	 */
	public List<HtlOpenCloseRoom> selectCloseRoomDate(long hotelId);

	/**
	 * 查询主推酒店
	 * @param pCommendId 主推酒店id
	 * @param pHotelId 酒店id
	 * @param beginD 有效时间
	 * @param endD
	 * @param bD
	 * @param eD
	 * @return
	 */
	public int checkMainCommendDate(long pCommendId, long pHotelId,
			Date beginD, Date endD, Date bD, Date eD);

	/**
	 * 根据HtlRoom 更新房态信息
	 * @param htlroom
	 * @return
	 */
	public int updateRoomStatuByHtlRoomID(HtlRoom htlroom);

	/**
	 * 查询房态信息
	 * @param roomStateBean
	 * @return
	 */
	public List<HtlRoom> selectRoomStatu(RoomStateBean roomStateBean);

	/**
	 * 根据酒店ID，查询出有配额预警的房型列表 
	 * 
	 * hotel2.9.3 add by shengwei.zuo  2009-10-21
	 * 
	 * @param hotelId
	 * @return
	 */
	public List getRoomTypeHavaForewarn(long hotelId);

	/**
	 * 新房控按时段调整一个床型的房态
	 * @param roomBed
	 * @param v_thisBedId  床型
	 * @param p_roomState  房态
	 */
	public int saveRoomStateBatch(RoomBedRecord roomBed, int v_thisBedId, int p_roomState);

	/**
	 * 新房控按时段调整态
	 * @param roomBed
	 * @param roomStatusReg 房态的正则表达式
	 * @param roomStatus 房态
	 * addby chenjuesu at 2009-12-30下午03:35:47
	 */
	public int saveRoomStateBatch(final RoomBedRecord roomBed,
			final String roomStatusReg, final String roomStatus);

	/**
	 * 根据酒店ID找出当天以后CC设置的房型和日期
	 * @param type 1：房型 2：分页的日期 3：日期总个数
	 * @param hotelID
	 * @param pageNo 取日期时，传入开始数
	 * @param pageSize 取日期时，传入长度数
	 * @return
	 */
	public List getRoomTypeOrDatesByCCSet(int type, long hotelID,
			int startNo, int pageSize);
	
	/**
	 * refactor
	 * 保存交接事项和是否主动报房态
	 * @param roomStaPro
	 */
	public void  saveOrUpdateRoomStatuPro(HtlRoomStatusProcess roomStaPro);
	
	/**
	 * refactor
	 * 获取CC设置关房,满房房型ID
	 * @param hotelID
	 * @return
	 */
	public List<HtlRoomtype> getRoomTypeByCCSet(long hotelID);
	
	/**
	 * refactor
	 * 获取CC设置关房的日期
	 * @param hotelID
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	public List<Date> getCloseRoomDateByCCSet(long hotelID,int startNo, int pageSize);
	
	/**
	 * refactor
	 * 获取CC设置关房的日期的总数
	 * @param hotelID
	 * @return
	 */
	public List<Long> getCloseRoomDateNumByCCSet(long hotelID);
	
	/**
	 * refactor
	 * 更新临时配额
	 * @param sql
	 * @param quotaId
	 */
	public void updateTempQuotaByPro(String sql,Long[] quotaId);
	
	/**
	 * 批量更新房间表信息
	 * @param htlRoom
	 */
	public void saveOrUpdateHtlRoom(List<HtlRoom> htlRoom);
	
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
	
	/**
	 * 批量更新或保存开关房记录
	 * 
	 * @param htlOpenCloseRoomList
	 */
	public void batchUpdateOrSaveOpenCloseRooms(List<HtlOpenCloseRoom> htlOpenCloseRoomList);
	
	/**
	 * 根据ID查找HtlOpenCloseRoom对象
	 * 
	 * @param id
	 */
	public HtlOpenCloseRoom findHtlOpenCloseRoomById(long id);
	
	/**
	 * 更新酒店状态
	 * 
	 * @param hotelStatus
	 * @param hotelId
	 */
	public void updateHotelStatus(String hotelStatus, long hotelId);
	
	/**
	 * 根据酒店ID查询酒店的关房记录
	 * 
	 * @param hotelId
	 * @return
	 */
	public List<HtlOpenCloseRoom> qryCloseRoomRecordsByHtlId(long hotelId);

	/**
	 * 更新开关房标志和理由
	 * 
	 * @param updatePriceBean
	 * @param openOrCloseFlag
	 * @return
	 */
	public int updateOpenOrCloseRoom(UpdatePriceBean updatePriceBean, String openOrCloseFlag);

}