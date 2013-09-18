package com.mangocity.hotel.base.manage.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.IQueryQuotaDao;
import com.mangocity.hotel.base.dao.RoomControlDao;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.base.manage.assistant.RoomControlBean;
import com.mangocity.hotel.base.manage.assistant.UpdatePriceBean;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.util.DateUtil;

/**
 */
public class RoomControlManageImpl implements RoomControlManage {

    private RoomControlDao roomControlDao;
    
    private IQueryQuotaDao qryQuotaDao;

    public HtlRoom findRoom(HtlRoom room) {
        // TODO Auto-generated method stub
        return null;
    }

    public int updateRoom(RoomControlBean roomBean) {

        roomControlDao.update(roomBean);
        return 0;
    }

    public RoomControlDao getRoomControlDao() {
        return roomControlDao;
    }

    public void setRoomControlDao(RoomControlDao roomControlDao) {
        this.roomControlDao = roomControlDao;
    }

    public void saveOpenCloseRoom(List<HtlOpenCloseRoom> htlOpenCloseRooms, String code, Long hotelId) {
		roomControlDao.batchUpdateOrSaveOpenCloseRooms(htlOpenCloseRooms);
		if (null != hotelId) {
			roomControlDao.updateHotelStatus(code, hotelId);
		}
	}

    public List<HtlOpenCloseRoom> queryHtlOpenCloseRoom(long hotelId) {
		return roomControlDao.qryCloseRoomRecordsByHtlId(hotelId);
	}

    public HtlOpenCloseRoom findhtlOpenCloseRoom(long ID) {

        return roomControlDao.findHtlOpenCloseRoomById(ID);
    }

    public int updateHtlPriceWithCloseRoom(UpdatePriceBean updatePriceBean) {
        return roomControlDao.updateOpenOrCloseRoom(updatePriceBean, "G");
    }

    public int checkMainCommendDate(long commendId, long hotelid, String beginDate, String endDate,
        String bDate, String eDate) {
        Date beginD = DateUtil.getDate(beginDate);
        Date endD = DateUtil.getDate(endDate);
        Date bD = DateUtil.getDate(bDate);
        Date eD = DateUtil.getDate(eDate);
        return roomControlDao.checkMainCommendDate(commendId, hotelid, beginD, endD, bD, eD);
    }
    
    public int updateHtlPriceWithOpenRoom(UpdatePriceBean updatePriceBean) {
        return roomControlDao.updateOpenOrCloseRoom(updatePriceBean, "K");
    }
    
    public List getRoomTypeHavaForewarn(long hotelId) {
		return roomControlDao.getRoomTypeHavaForewarn(hotelId);
	}

	public List<HtlRoomtype> getRoomTypeCCSetted(long hotelID) {
		return roomControlDao.getRoomTypeOrDatesByCCSet(1, hotelID, 0, 0);
	}
	
	public List<HtlRoomcontrolHotelSchedule> getHotelScheduleByHtlIdSchDate(long hotelId, Date schDate) {
		return roomControlDao.getHotelScheduleByHtlIdSchDate(hotelId, schDate);
	}
	
	public void updateHtlRoomcontrolHotelSchedule(HtlRoomcontrolHotelSchedule htlRoomcontrolHotelSchedule) {
		roomControlDao.updateHtlRoomcontrolHotelSchedule(htlRoomcontrolHotelSchedule);
	}
	
	public void updateScheduleWarnFlag(Long hotelId, Date schDate) {
		long count = qryQuotaDao.getQuotaNewCountByHtlId(hotelId);
		List<HtlRoomcontrolHotelSchedule> hotelScheduleList = this.getHotelScheduleByHtlIdSchDate(hotelId,
				schDate);
		for (HtlRoomcontrolHotelSchedule hotelSchedule : hotelScheduleList){
			if(0L == count){
				hotelSchedule.setQuotawarninghotel(Integer.valueOf(0));
			}else{
				hotelSchedule.setQuotawarninghotel(Integer.valueOf(1));
			}
			
			this.updateHtlRoomcontrolHotelSchedule(hotelSchedule);
		}
	}

	public IQueryQuotaDao getQryQuotaDao() {
		return qryQuotaDao;
	}

	public void setQryQuotaDao(IQueryQuotaDao qryQuotaDao) {
		this.qryQuotaDao = qryQuotaDao;
	}

	
	
	
}
