package com.mangocity.hotel.search.service;

import java.util.List;

import com.mangocity.hotel.search.model.SortCondition;
import com.mangocity.hotel.search.service.assistant.Commodity;
import com.mangocity.hotel.search.service.assistant.HotelInfo;
import com.mangocity.hotel.search.service.assistant.RoomTypeInfo;
import com.mangocity.hotel.search.service.assistant.SortResType;


/**
 * 酒店排序Service
 * 用于酒店的排序\分页
 * 房型排序
 * 商品排序
 * @author zengyong
 *
 */
public interface HotelSortService {

	/**
	 * 酒店排序,按基本排序逻辑对酒店进行排序
	 * @param queryHotelCondition
	 * @param hotelIdLst
	 * @return
	 */
	public void sortHotelDefault(List<HotelInfo> hotelinfo);
	
	
	/**
	 * 酒店排序,按指定排序方式对应的逻辑排序
	 * @param queryHotelCondition
	 * @param hotelIdLst
	 * @param updown
	 * @return
	 */
	public void sortHotel(List<HotelInfo> hotelinfo,String type,int updown);

	/**
	 * 分页
	 * @param sortedHotelIdLst
	 * @param currpage
	 * @param pageSize
	 * @return
	 */
	public List<HotelInfo> getHotelLstByListForPage(List<HotelInfo> hotelinfo,int currpage,int pageSize);
	
	
	public List<HotelInfo> getHotelLstByIdForPage(String sortedHotelIdLst,int currpage,int pageSize);
	
	/**
	 * 房型排序
	 * @param roomtypeLst
	 * @return
	 */
	public void sortRoomType(List<RoomTypeInfo> roomtypeLst);
	
	/**
	 * 商品排序
	 * @param commodityLst
	 * @return
	 */
	public void sortCommodity(List<Commodity> commodityLst);
	
	/**
	 * 根据排序条件排序 将不显示的酒店对应的ID删除,按照排序逻辑进行排序
	 * 
	 * @param sortcon
	 * @param hotelIdLst 
	 * @return
	 */
	public SortResType sortHotelByCondition(SortCondition sortcon, String hotelIdLst);
}
