package com.mangocity.hotel.order.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.hotel.base.dao.IHotelEmapDao;
import com.mangocity.hotel.base.service.assistant.CalculateDistance;
import com.mangocity.hotel.base.service.assistant.HotelEmapResultInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.order.service.IHotelEmapService;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.domain.entity.GisBaseInfo;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.util.log.MyLog;

/**
 */
public class HotelEmapServiceImplRefactor  implements IHotelEmapService {

    private static final long serialVersionUID = 1L;
    private static final MyLog log = MyLog.getLogger(HotelEmapServiceImpl.class);
	private static final String CONSTANT_LONGITUDE_KEY ="LONGITUDE";
	private static final String CONSTANT_LATITUDE_KEY = "LATITUDE";
    
    private IHotelEmapDao hotelEmapDao;
    private GisService gisService;

    public void setGisService(GisService gisService) {
        this.gisService = gisService;
    }
    

    public void setHotelEmapDao(IHotelEmapDao hotelEmapDao) {
		this.hotelEmapDao = hotelEmapDao;
	}


	/****
     * 查询酒店的修改实现
     * 
     * @param condition查询条件
     * @throws SQLException 
     * 
    
     */
    public List queryHotels(HotelQueryCondition condition, Double longitude, Double latitude,
        int dis, LatLng leftTopLatLng, LatLng leftBottomLatLng, LatLng rightTopLatLng,
        LatLng rightBottomLatLng, boolean isAllFlag,
        List<HotelEmapResultInfo> allHotelList) throws SQLException {
        List<HotelEmapResultInfo> list = hotelEmapDao.queryHotelsByCondition(condition);
        //经度,纬度不存在时通过调用gisid接口获取获取该城市经纬度
        if (null == longitude || null == latitude) {
        	Map<String,Double> latLngMap =getLatLng4CityName(condition.getCityName());
        	longitude=latLngMap.get(CONSTANT_LONGITUDE_KEY);
        	latitude=latLngMap.get(CONSTANT_LATITUDE_KEY);
        }
        List<HotelEmapResultInfo> mapList = new ArrayList<HotelEmapResultInfo>();
         // 如果查询出来的酒店不为空
        if (!list.isEmpty()) {
             // 如果距离小于等于0
            if (0 >= dis) {
                 // 如果isAllFlag为true显示所有酒店
                if (isAllFlag) {
                    for (HotelEmapResultInfo mapResultInfo : list) {
                        allHotelList.add(mapResultInfo);
                    }
                }
                 //将框选酒店计算在内
                if (null != leftTopLatLng) {
                	list = getDragMapList(list,leftTopLatLng, leftBottomLatLng,  rightTopLatLng,
                	         rightBottomLatLng);
                }
                int hotelCount = list.size();
                 // 设置总页数
                condition.setTotalPage(setTotalPage(hotelCount,condition.getPageSize()));
                 //如果经度,纬度为空, 搜索gooogle的API,读取相应的经度和纬度数据
                if (null == longitude || null == latitude) {
                     // 如果不存在经度和纬度,直接返回相应的数据.
                	return getMapList(list,hotelCount,condition.getPageSize() , condition.getPageNo());
                } 
                 //依据精度来判断该关键字区域是否存在，
                LatLng keyWordLatLng = new LatLng();
                keyWordLatLng.setLongitude(longitude);
                keyWordLatLng.setLatitude(latitude);
                 //只取范围内的酒店
                if (hotelCount >= condition.getPageSize() * (condition.getPageNo() - 1)) {
                     //查询出来的酒店个数小于　condition.getPageSize() * condition.getPageNo()
                    if (hotelCount < condition.getPageSize() * condition.getPageNo()) {
                    	mapList=getMapListByLatLng(list,condition.getPageSize(),condition.getPageNo(),hotelCount,keyWordLatLng);
                    } else {
                         // 查询出来的酒店个数大于　condition.getPageSize() * condition.getPageNo()
                    	int count=condition.getPageSize()* condition.getPageNo();
                    	mapList=getMapListByLatLng(list,condition.getPageSize(),condition.getPageNo(),count,keyWordLatLng);
                    }
                }
                
            } else {
                 // 如果isAllFlag为true显示所有酒店
                if (isAllFlag) {
                    for (HotelEmapResultInfo mapResultInfo : list) {
                        allHotelList.add(mapResultInfo);
                    }
                }
                 // 将框选酒店计算在内
                if (null != leftTopLatLng) {
                	list = getDragMapList(list,leftTopLatLng, leftBottomLatLng,  rightTopLatLng,
               	         rightBottomLatLng);
                }
                LatLng keyWordLatLng = new LatLng();
                keyWordLatLng.setLongitude(longitude);
                keyWordLatLng.setLatitude(latitude);
                 // 以keyWordLatLng为基点获取经纬度在dis范围内的酒店列表
                mapList=getMapListByDistance(list,keyWordLatLng,dis);
                 // 设置总页数
                int hotelCount = mapList.size();
                condition.setTotalPage(setTotalPage(hotelCount,condition.getPageSize()));
                 //返回范围内的酒店列表
                return getMapList(mapList,hotelCount,condition.getPageSize(),condition.getPageNo());
            }

        }
        return mapList;
	}
    /**
     * 获取某距离内的酒店列表
     * @param list
     * @param keyWordLatLng
     * @param dis
     * @return
     */
    private List<HotelEmapResultInfo> getMapListByDistance(List<HotelEmapResultInfo> list, LatLng keyWordLatLng, int dis) {
    	List<HotelEmapResultInfo>mapList=new ArrayList<HotelEmapResultInfo>();
    	 for (HotelEmapResultInfo hotelMapResultInfo : list) {
             // 酒店的精度与纬度
             if ((0 < hotelMapResultInfo.getLongitude() - 1)
                 && (0 < hotelMapResultInfo.getLatitude() - 1)) {
                 LatLng hotelLatLng = new LatLng();
                 hotelLatLng.setLongitude(hotelMapResultInfo.getLongitude());
                 hotelLatLng.setLatitude(hotelMapResultInfo.getLatitude());
                 Double distance = CalculateDistance.getDistance(keyWordLatLng, hotelLatLng);
                 hotelMapResultInfo.setDistance(Math.abs(distance));
                 if (distance < dis) {
                     mapList.add(hotelMapResultInfo);
                 }
             }
         }
    	 return mapList;
	}


	/**
     * 封装计算关键字（点选）范围内的酒店列表
     * @param list
     * @param pageSize
     * @param pageNo
     * @param hotelCount
     * @param keyWordLatLng
     * @return
     */
    private List<HotelEmapResultInfo> getMapListByLatLng(List<HotelEmapResultInfo> list, int pageSize, int pageNo, int hotelCount, LatLng keyWordLatLng) {
    	List<HotelEmapResultInfo>mapList=new ArrayList<HotelEmapResultInfo>();
    	 for (int i = pageSize * (pageNo - 1); i < hotelCount; i++) {
                 HotelEmapResultInfo mapResultInfo = list.get(i);
                 // 酒店的精度与纬度
                 if ((0 < mapResultInfo.getLongitude() - 2)
                     && (0 < mapResultInfo.getLatitude() - 2)) {
                     LatLng hotelLatLng = new LatLng();
                     hotelLatLng.setLongitude(mapResultInfo.getLongitude());
                     hotelLatLng.setLatitude(mapResultInfo.getLatitude());
                     Double distance = CalculateDistance.getDistance(keyWordLatLng,
                         hotelLatLng);
                     mapResultInfo.setDistance(Math.abs(distance));
                 }
                 mapList.add(mapResultInfo);
             }
		return mapList;
	}


	/**
     * 封装页面所需的酒店地图数据
     * @param list
     * @param hotelCount
     * @param pageSize
     * @param pageNo
     * @return
     */
    private List<HotelEmapResultInfo> getMapList(List<HotelEmapResultInfo> list, int hotelCount, int pageSize, int pageNo) {
    	List<HotelEmapResultInfo> mapList = new ArrayList<HotelEmapResultInfo>();
    	if (hotelCount < pageSize * pageNo) {
            for (int i = pageSize * (pageNo - 1); i < hotelCount; i++) {
                HotelEmapResultInfo mapResultInfo = list.get(i);
                mapList.add(mapResultInfo);
            }
        } else {
             //查询出来的酒店个数大于　condition.getPageSize() * condition.getPageNo()
    	   for (int i = pageSize * (pageNo - 1); i < pageSize* pageNo; i++) {
                HotelEmapResultInfo mapResultInfo = list.get(i);
                // 酒店的精度与纬度
                mapList.add(mapResultInfo);
            }
        }
		return mapList;
	}
	/**
     * 设置页数
     * @param hotelCount
     * @param pageSize
     * @return
     */
   private int setTotalPage(int hotelCount, int pageSize) {
	   int totalPage=0;
	   if (0 < pageSize) {
           if (0 < hotelCount % pageSize) {
               totalPage=hotelCount / pageSize + 1;
           } else {
               totalPage=hotelCount / pageSize;
           }
       }
	   return totalPage;
	}


/**
    * 过滤框选内的HotelEmapResultInfo对象，不在矩形框内的就过滤掉
    * @param list
    * @param leftTopLatLng
    * @param leftBottomLatLng
    * @param rightTopLatLng
    * @param rightBottomLatLng
    * @return
    */
    private List<HotelEmapResultInfo> getDragMapList(List<HotelEmapResultInfo> list, LatLng leftTopLatLng, LatLng leftBottomLatLng, LatLng rightTopLatLng, LatLng rightBottomLatLng) {
    	List<HotelEmapResultInfo>dragMapList=new ArrayList<HotelEmapResultInfo>();
    	 for (HotelEmapResultInfo mapResultInfo : list) {
             if (0 < mapResultInfo.getLongitude() - 1) {
                 LatLng hotelLatLng = new LatLng();
                 hotelLatLng.setLongitude(mapResultInfo.getLongitude());
                 hotelLatLng.setLatitude(mapResultInfo.getLatitude());
                 if (getPointResult(hotelLatLng, leftTopLatLng, leftBottomLatLng,
                     rightTopLatLng, rightBottomLatLng)) {
                     dragMapList.add(mapResultInfo);
                 }
             }
         }
		return dragMapList;
	}


	/**
     * 通过城市名获取该城市中心的经纬度
     * @param cityName
     * @return
     */
    private Map<String, Double> getLatLng4CityName(String cityName) {
    	Map<String, Double> latLngMap=new HashMap<String,Double>();
    	try {
			List gisList = gisService.getQueryList4CityAndType("", cityName, -1);
			if ((null != gisList) && (0 < gisList.size())) {
              GisBaseInfo baseInfo = (GisBaseInfo) gisList.get(0);
              latLngMap.put(CONSTANT_LONGITUDE_KEY, baseInfo.getLongitude());
              latLngMap.put(CONSTANT_LATITUDE_KEY, baseInfo.getLatitude());
          }
		} catch (ServiceException e) {
			log.error("Get gisService getQueryList4CityAndType by "
                  + cityName + " error: " + e);
		}
		return latLngMap;
	}

    /**
     * 比较该经纬度是否在矩形框内
     * @param middleLatLng
     * @param leftTopLatLng
     * @param leftBottomLatLng
     * @param rightTopLatLng
     * @param rightBottomLatLng
     * @return
     */
	public boolean getPointResult(LatLng middleLatLng, LatLng leftTopLatLng,
        LatLng leftBottomLatLng, LatLng rightTopLatLng, LatLng rightBottomLatLng) {
        boolean longitudePoint;
        boolean latitudePoint;
        Double midLongitude = middleLatLng.getLongitude();
        Double midLatitude = middleLatLng.getLatitude();
        if (midLongitude >= leftTopLatLng.getLongitude()
            && midLongitude <= rightTopLatLng.getLongitude()) {
            longitudePoint = true;
        } else {
            longitudePoint = false;
        }

        if (midLatitude >= leftBottomLatLng.getLatitude()
            && midLatitude <= leftTopLatLng.getLatitude()) {
            latitudePoint = true;
        } else {
            latitudePoint = false;
        }

        // 如果两者为真就为真,否则为假
        return longitudePoint && latitudePoint;

    }
}
