package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlCoverPicture;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPictureInfo;


/**
 * @author yuexiaofeng
 * 
 */
public interface HotelPictureDao {
	/**
	 * 查询所有公共会籍
	 * @return
	 */
	public List queryCommonMemberCd(String queryID,Object obj);
	/**
	 * 根据图片ID查询图片路径
	 * @param queryID
	 * @param htlPictureUrl
	 * @return
	 */
	public List queryPictureUrl(String queryID,HtlCoverPicture htlCoverPicture);
	/**
	 * 根据酒店id查询酒店基本相册封面
	 * @param queryID
	 * @param htlHotel
	 * @return
	 */
	public  List queryCoverPictures(String queryID,HtlHotel htlHotel);
	/**
	 * 查询apche无图而Artifactory有图的酒店
	 * @param queryID
	 * @param obj
	 * @return
	 */
	public  List queryNoPicHotels(String queryID,Object obj);

    /**
     * 查询3D图片信息列表
     * 
     * @param queryID
     * @param hotelPicInfo
     * @return
     */
    public List queryHotelPicList(String queryID, HtlPictureInfo hotelPicInfo);

    /**
     * 查询3D图片信息
     * 
     * @param queryID
     * @param hotelPicInfo
     * @return
     */
    public HtlPictureInfo queryHotelPicById(String queryID, HtlPictureInfo hotelPicInfo);

    /**
     * 增加3D图片信息
     * 
     * @param queryID
     * @param hotelPicInfo
     * @return
     */
    public Object addHotelPic(String queryID, HtlPictureInfo hotelPicInfo);

    /**
     * 修改3D图片信息
     * 
     * @param queryID
     * @param hotelPicInfo
     * @return
     */
    public int modifyHotelPic(String queryID, HtlPictureInfo hotelPicInfo);

    /**
     * 删除3D图片信息
     * 
     * @param queryID
     * @param hotelPicInfo
     * @return
     */
    public int deleteHotelPic(String queryID, HtlPictureInfo hotelPicInfo);

    /**
     * 查询开关房信息
     * 
     * @param roomTypeID
     * @param beginDate
     * @param endDate
     * @return
     */
    public List queryCloseRoom(String roomTypeID, Date beginDate, Date endDate);
    
    /**
     * 查询开关房信息 add by shengwei.zuo 2009-10-21
     * 
     * @param roomTypeID
     * @param beginDate
     * @param endDate
     * @return
     */
    public List queryCloseRoomQuota(String roomTypeID);

}
