package com.mangocity.hotel.base.manage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlTempRoomState;
import com.mangocity.hotel.user.UserWrapper;

/**
 * refactor
 * 
 * @author zuoshengwei
 * 
 */
public interface RoomStateManageRefactor {

	/**
	 * refactor 更新房态 其中的值的形式为 key=房间Id value
	 * =房间状态(1:2/2:3)第一个1表示床型,":"表示床形与床型的状态，"/"分开床型与床型
	 * 
	 * @param roomStateMap
	 *            是一个hashMap，
	 * 
	 */
	public void batchUpdateRoomStatus(Map<String, String> roomStateMap);

	/**
	 * refactor 查询已经关房的时间段
	 * 
	 * @param hotelId
	 * @return
	 */
	public List<HtlOpenCloseRoom> selectRoomCloseDate(long hotelId);

	/**
	 * refactor 批量更新临时配额 add by haibo.li 2.9.3配额改造,改成往新表更新
	 * 
	 * @param idLst
	 * @return
	 */
	public void updateTempQuota(String idLst);

	/**
	 * refactor 异步保存交接事项和是否主动报房态 add by zhijie.gu 2010-01-05
	 * 
	 * @param roomStateBean
	 */
	public void saveRoomStateProcess(RoomStateBean roomStateBean);

	/**
	 * refactor mailType 0,为配额>0，房态设置为满房；1,连续设置满房超过7天发送mail；2，房态由F调整为满房发送邮件
	 * 
	 * @param list
	 * @param hotel
	 * @param user
	 * @param mailType
	 */
	public void sendRoomStateFull(List<HtlTempRoomState> list, HtlHotel hotel, UserWrapper user, Integer mailType);

	/**
	 * 获取CC设置关房的日期的列表
	 * 
	 * @param hotelID
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Date> queryCCSetRoomDateList(long hotelID, int pageNo, int pageSize);

	/**
	 * 获取CC设置关房的日期的数量
	 * 
	 * @param hotelID
	 * @return
	 */
	public List<Long> queryCCSetRoomDateNum(long hotelID);

}
