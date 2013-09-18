package com.mangocity.webnew.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.webnew.dao.MangoSalesDao;
import com.mangocity.webnew.persistence.QueryHotelForSalesResult;
import com.mangocity.webnew.persistence.QueryHotelForSalesRoomType;
import com.mangocity.webnew.persistence.QueryHotelForSalesSaleItems;
import com.mangocity.webnew.service.MangoSalesService;
import com.mangocity.webnew.util.MangoSalesEntity;
import com.mangocity.webnew.util.MangoSalesUtil;

public class MangoSalesServiceImpl implements MangoSalesService{
	
	private  MangoSalesDao  mangoSalesDao;
	
	private IHotelService hotelService;
	
	public List<QueryHotelForSalesResult> queryHotelsForSales() {
		String hotelAry [] = MangoSalesUtil.hotelIdArray; 
		if(null!=hotelAry && hotelAry.length>0){
			List<QueryHotelForSalesResult>  lstHotel = new ArrayList<QueryHotelForSalesResult>();
			for(int i=0;i<hotelAry.length;i++){
				Long hotelid = Long.parseLong(hotelAry[i]);
				MangoSalesEntity salesEntity= new MangoSalesEntity();
				List<MangoSalesEntity> lstSalesEntity = MangoSalesUtil.mango_sales.get(hotelid);
				if(null!=lstSalesEntity && !lstSalesEntity.isEmpty()){
					salesEntity = lstSalesEntity.get(0);
					String salesEndDate = salesEntity.getSalesEndDate();
					if(DateUtil.getDay(DateUtil.getDate(salesEndDate), DateUtil.getDate(DateUtil.getSystemDate()))>0){
						break;
					}
				}
				
				//酒店信息
				HtlHotel htlHotel = hotelService.findHotel(hotelid);
				if(htlHotel == null) {
					continue;
				}
				
				QueryHotelForSalesResult hotelForSalesResult = new QueryHotelForSalesResult();
				hotelForSalesResult.setHotelId(hotelid);
				hotelForSalesResult.setHotelChnName(htlHotel.getChnName());
				hotelForSalesResult.setCity(htlHotel.getCity());
				long gisId = StringUtil.setStringToLong(htlHotel.getGisid().toString());
				hotelForSalesResult.setGisid(gisId);
				String chnAddress = addressInfo(hotelid);
				hotelForSalesResult.setChnAddress(chnAddress);
				String hotelStar = startInfo(hotelid);
				hotelForSalesResult.setHotelStar(hotelStar);
				
				//房型信息
				List<MangoSalesEntity> lstMango = MangoSalesUtil.mango_sales.get(hotelid);
				List<QueryHotelForSalesRoomType> lstRoomTp =  new ArrayList<QueryHotelForSalesRoomType>();
				for(int j =0;j<lstMango.size();j++){
					MangoSalesEntity mangoSalesEntity= new MangoSalesEntity();
					mangoSalesEntity = lstMango.get(j);
					String salesEnD = mangoSalesEntity.getSalesEndDate();
				    List lstRoom = getRoomType(mangoSalesEntity.getHotelId(), mangoSalesEntity.getRoomTypeId(), mangoSalesEntity.getPriceTypeId());
					for(Iterator itRoom = lstRoom.iterator();itRoom.hasNext();){
						Map roomM = (Map)itRoom.next();
						QueryHotelForSalesRoomType hotelForSalesRoomType  = new  QueryHotelForSalesRoomType();
						long roomTpId = StringUtil.setStringToLong(roomM.get("ROOM_TYPE_ID").toString());
						long priceTpId = StringUtil.setStringToLong(roomM.get("PRICE_TYPE_ID").toString());
						
						hotelForSalesRoomType.setRoomTypeId(String.valueOf(roomTpId));
						hotelForSalesRoomType.setChildRoomTypeId(String.valueOf(priceTpId));
						hotelForSalesRoomType.setRoomTypeName((String)roomM.get("ROOM_NAME"));
						hotelForSalesRoomType.setChildRoomTypeName((String)roomM.get("PRICE_TYPE"));
						
						List<QueryHotelForSalesSaleItems> lstSaleItems =  new ArrayList<QueryHotelForSalesSaleItems>();
						//当前日期
						Date currDate = DateUtil.getSystemDate();
						Date lastDate = DateUtil.getDate(salesEnD);
						int diffDays = DateUtil.getDay(currDate,lastDate)+1;
						if(diffDays>14){
							diffDays = 14;
						}
						Date dt = null;
						for(int k = 0;k<diffDays;k++){
							dt = DateUtil.getDate(currDate, k);
							dt = DateUtil.getDate(DateUtil.dateToString(dt));
							List lstSaleInfo = getSalePrice(hotelid,roomTpId,priceTpId,dt);
							for(Iterator isSales =lstSaleInfo.iterator();isSales.hasNext(); ){
								Map salesM = (Map)isSales.next();
								int confirmFlag = StringUtil.setStringToInt(salesM.get("FLAGSTR").toString());
								double salePri = StringUtil.setStringToDouble(salesM.get("SALE_PRICE").toString());
								//价格取整
								Double salePriD = Math.floor(salePri);
								QueryHotelForSalesSaleItems hotelForSalesSaleItems = new  QueryHotelForSalesSaleItems();
								hotelForSalesSaleItems.setSaleNum(salePriD.intValue());
								hotelForSalesSaleItems.setConfrimFlag(confirmFlag);
								hotelForSalesSaleItems.setFellowDate(dt);
								lstSaleItems.add(hotelForSalesSaleItems);
							}
						}
						hotelForSalesRoomType.setSaleItems(lstSaleItems);
						lstRoomTp.add(hotelForSalesRoomType);
						
					}
				}
				
				hotelForSalesResult.setRoomTypes(lstRoomTp);
				
				lstHotel.add(hotelForSalesResult);
				
			}
			if(null == lstHotel){
				return MangoSalesUtil.EMPTY_LIST;
			}
			return lstHotel;
		}else{
			return MangoSalesUtil.EMPTY_LIST;
		}
	}
	
	public List getRoomType(Long hotelid, Long roomTypeID, Long priceTypeId) {
		return mangoSalesDao.getRoomType(hotelid, roomTypeID, priceTypeId);
	}
	
	public List getSalePrice(Long hotelid, Long roomTypeID, Long priceTypeId,Date currDate) {
		return mangoSalesDao.getSalePrice(hotelid, roomTypeID, priceTypeId, currDate);
	}
	
	public String addressInfo(Long hotelid){
		StringBuffer  bd = new StringBuffer();
		if(hotelid ==30005500){
			bd.append("毗邻浦东新国际博览中心，可步行12分钟至地铁6号线，方便到达浦东世博园区");
		}else if(hotelid ==30105683){
			bd.append("距离浦东世博园区仅1公里，周围环境幽静");
		}else if(hotelid ==30080425){
			bd.append("距离世博园4号门仅0.5公里，房间可观江景及世博园中国馆");
		}else if(hotelid ==30002694){
			bd.append("交通便利，舒适、卫生、安静");
		}
		return  bd.toString();
	}
	
	public String startInfo(Long hotelid){
		StringBuffer  bd = new StringBuffer();
		if(hotelid ==30005500){
			bd.append("（舒适型酒店）");
		}else if(hotelid ==30105683){
			bd.append("（舒适型酒店）");
		}else if(hotelid ==30080425){
			bd.append("（舒适型酒店）");
		}else if(hotelid ==30002694){
			bd.append("（高档型）");
		}
		return  bd.toString();
	}

	public MangoSalesDao getMangoSalesDao() {
		return mangoSalesDao;
	}

	public void setMangoSalesDao(MangoSalesDao mangoSalesDao) {
		this.mangoSalesDao = mangoSalesDao;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}


	
}
