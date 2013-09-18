package com.mangocity.hotel.base.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mangocity.hotel.base.dao.HotelPictureDao;
import com.mangocity.hotel.base.persistence.HtlCoverPicture;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPictureInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOIbatisImpl;

/**
 * @author yuexiaofeng
 * 
 */
public class HotelPictureDAOImp extends DAOIbatisImpl implements HotelPictureDao {
	public List queryCommonMemberCd(String queryID,Object obj){
		 return super.queryForList(queryID, null);
	}
	
	public List queryPictureUrl(String queryID,HtlCoverPicture htlCoverPicture){
		 return super.queryForList(queryID, htlCoverPicture);
	}
	
	public  List queryCoverPictures(String queryID,HtlHotel htlHotel){
	    return super.queryForList(queryID, htlHotel);
	}
	
	public  List queryNoPicHotels(String queryID,Object obj){
	    return super.queryForList(queryID, null);
	}

    public Object addHotelPic(String queryID, HtlPictureInfo hotelPicInfo) {
        // TODO Auto-generated method stub
        return super.save(queryID, hotelPicInfo);
    }

    public int deleteHotelPic(String queryID, HtlPictureInfo hotelPicInfo) {
        // TODO Auto-generated method stub
        return super.delete(queryID, hotelPicInfo);
    }

    public int modifyHotelPic(String queryID, HtlPictureInfo hotelPicInfo) {
        // TODO Auto-generated method stub
        return super.update(queryID, hotelPicInfo);
    }

    public HtlPictureInfo queryHotelPicById(String queryID, HtlPictureInfo hotelPicInfo) {
        // TODO Auto-generated method stub
        return (HtlPictureInfo) super.queryForObject(queryID, hotelPicInfo);
    }

    public List queryHotelPicList(String queryID, HtlPictureInfo hotelPicInfo) {
        // TODO Auto-generated method stub
        return super.queryForList(queryID, hotelPicInfo);
    }

    /**
     * 查询开关房信息
     * 
     * @param roomTypeID
     * @param beginDate
     * @param endDate
     * @return
     */
    public List queryCloseRoom(String roomTypeID, Date beginDate, Date endDate) {
        HashMap queryForCloseRoomMap = new HashMap();
        long roomTypeIDLong = Long.valueOf(roomTypeID).longValue();
        queryForCloseRoomMap.put("endDate", endDate);
        queryForCloseRoomMap.put("roomTypeID", roomTypeIDLong);
        queryForCloseRoomMap.put("beginDate", beginDate);
        return super.queryForList("queryForCloseRoom", queryForCloseRoomMap);
    }
    
    /**
     * 查询开关房信息 add by shengwei.zuo 2009-10-21
     * 
     * @param roomTypeID
     * @param beginDate
     * @param endDate
     * @return
     */
    public List queryCloseRoomQuota(String roomTypeID) {
    	
    	Date beginDate  = DateUtil.getDate("2009-10-21");
          
        Date endDate  = DateUtil.getDate("2009-10-28");
    	
        HashMap queryForCloseRoomMap = new HashMap();
        long roomTypeIDLong = Long.valueOf(roomTypeID).longValue();
        queryForCloseRoomMap.put("endDate", endDate);
        queryForCloseRoomMap.put("roomTypeID", roomTypeIDLong);
        queryForCloseRoomMap.put("beginDate", beginDate);
        return super.queryForList("queryForCloseRoom", queryForCloseRoomMap);
    }
    
}
