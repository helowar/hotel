package com.mangocity.hotel.dreamweb.datacheck.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.HotelRoomService;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.HotelQuotaNewService;
import com.mangocity.hotel.base.service.assistant.BookRoomCondition;
import com.mangocity.hotel.dreamweb.datacheck.service.BookingDataCheckService;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.IHotelReservationInfoService;
import com.mangocity.util.DateUtil;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

public class BookingDataCheckServiceImpl implements BookingDataCheckService {

	private HotelQuotaNewService hotelQuotaNewService ;
	private HotelRoomService hotelRoomService ;
	private HotelRoomTypeService hotelRoomTypeService;
	private IHotelReservationInfoService hotelReservationInfoService;
	
	
	/**
	 * 过滤掉满房的床型
	 */
	public String filterFullBedType(HotelOrderFromBean hotelOrderFromBean) {
		String fullBedTypeStr = checkRoomFull(hotelOrderFromBean);
		String oldBedTypeStr = hotelOrderFromBean.getBedTypeStr();
		String newBedTypeStr = null;
		if (fullBedTypeStr != null && fullBedTypeStr.length() > 0) {

			oldBedTypeStr = oldBedTypeStr.replaceAll(",", "");
			if (fullBedTypeStr.equals(oldBedTypeStr)) {

				newBedTypeStr = null;

			} else {
				for (int i = 0; i < fullBedTypeStr.length(); i++) {
					oldBedTypeStr = oldBedTypeStr.replace(fullBedTypeStr.substring(i, i + 1), "");
				}

				StringBuilder tempBedType = new StringBuilder();
				for (int i = 0; i < oldBedTypeStr.length(); i++) {
					tempBedType.append(oldBedTypeStr.charAt(i));
					tempBedType.append(",");
				}
				newBedTypeStr = tempBedType.toString();
				int len = newBedTypeStr.length();
				newBedTypeStr = newBedTypeStr.substring(0, len - 1);
			}
		} else {
			newBedTypeStr = oldBedTypeStr;
		}
		return newBedTypeStr;
		
	}
	
	/**
	 * 获取床型对应的最小配额数。
	 * 入住离店日期段内，在床型房态出现不可超的情况下，Ｙ等于不可超的床型取该床型该时间段内最小配额数，需要大于等于１，小于等于５。
	 * y为预订最多房间数
	 * @param hotelOrderFromBean
	 * @return
	 */
	public Map<String,Integer> getBedTypeMinQuato(HotelOrderFromBean hotelOrderFromBean){
		String bedTypeStr = hotelOrderFromBean.getBedTypeStr();
		Map<String, Integer> bedQuotaMap = new HashMap<String, Integer>();
		if (bedTypeStr != null) {
			String[] bedType = bedTypeStr.split(",");
			List<HtlRoom> htlRooms = hotelRoomService.getHtlRooms(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean
					.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
			HtlRoomtype roomType = hotelRoomTypeService.getHtlRoomTypeByIdAndHtlId(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId());
			List<HtlQuotaNew> htlQuotas = hotelQuotaNewService.queryQuotaByRoomTypeId(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(),
					hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());

			Map<String, HtlQuotaNew> htlQuotaMap = getHtlQuotaNewMap(htlQuotas);

			for (int i = 0; i < bedType.length; i++) {
				int minBedQuota = 5;
				bedQuotaMap.put(bedType[i], minBedQuota);
				for (HtlRoom room : htlRooms) {
					String roomState = room.getRoomState();
					String impassableState = bedType[i] + ":3";
					String quotaKey = null;
					// 配额共享,配额数为共享配额的配额 ,共享的配额设置在双床上
					if (roomType.getQuotaBedShare() == 1L) {
						quotaKey = getQuotaMapKey(room, "2");
					} else {
						quotaKey = getQuotaMapKey(room, bedType[i]);
					}

					if (roomState.indexOf(impassableState) >= 0) {
						int bedQuota = countQuataNum(htlQuotaMap.get(quotaKey));
						if (bedQuota >= 1 && bedQuota < 5) {
							minBedQuota = getMinQuota(bedQuota, bedQuotaMap.get(bedType[i]));
							bedQuotaMap.put(bedType[i], minBedQuota);
						}
					}
				}
			}
		}
		return bedQuotaMap;
	}
	
	/**
	 * 如果来自页面的担保判断字段与预订条款中的不相等，客人有可能已经修改数据了
	 */
	public boolean checkChangeBookData(HotelOrderFromBean hotelOrderFromBean) {
		boolean fagHasChange =false;
		OrReservation reservation = getReservation(hotelOrderFromBean);
		if(reservation!=null){
		boolean fagHasChangeAssure = checkIsNeedAssure(hotelOrderFromBean, reservation);
		if(fagHasChangeAssure){
			hotelOrderFromBean.setNeedAssure(true);
			fagHasChange=true;
		}
		}
		return fagHasChange;
	}

	private OrReservation getReservation(HotelOrderFromBean hotelOrderFromBean) {
		OrReservation reservation;
		OrOrder order=new OrOrder();
		order.setHotelId(hotelOrderFromBean.getHotelId());
		order.setCheckinDate(hotelOrderFromBean.getCheckinDate());
		order.setCheckoutDate(hotelOrderFromBean.getCheckoutDate());
		order.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());
		
		 BookRoomCondition bookRoomCond=new  BookRoomCondition();
		 bookRoomCond.setCheckinDate(hotelOrderFromBean.getCheckinDate());
		 bookRoomCond.setHotelId(hotelOrderFromBean.getHotelId());
		 bookRoomCond.setCheckoutDate(hotelOrderFromBean.getCheckoutDate());
		 bookRoomCond.setChildRoomTypeId(hotelOrderFromBean.getChildRoomTypeId());
		 bookRoomCond.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());
		// hotelReservationInfoService.getReservationInfoForWeb(bookRoomCond,order);
		 reservation=hotelReservationInfoService.getAssureReservation(bookRoomCond);
		return reservation;
	}


	private boolean checkIsNeedAssure(HotelOrderFromBean hotelOrderFromBean, OrReservation reservation) {
		boolean fromPageAssure=hotelOrderFromBean.isNeedAssure();
		boolean fagHasChange=false;
		//无条件担保
		if(reservation.isUnCondition()){
			if(!fromPageAssure){
				fagHasChange=true;
			}			
		}

		//超房担保
		if(reservation.isRoomsAssure()){
			String fromPageRoomsStr=hotelOrderFromBean.getRoomQuantity();
			int fromPageRooms=fromPageRoomsStr==null?0:Integer.parseInt(fromPageRoomsStr);
			if(fromPageRooms>reservation.getRooms()){
				if(!fromPageAssure){
					fagHasChange=true;
				}
			}
		}	
		
		//超间夜担保
		if(reservation.isNightsAssure()){
			String fromPageRoomsStr=hotelOrderFromBean.getRoomQuantity();
			int fromPageRooms=fromPageRoomsStr==null?0:Integer.parseInt(fromPageRoomsStr);
			int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),  hotelOrderFromBean.getCheckoutDate());
			int fromPageNights=days*fromPageRooms;
			if(fromPageNights>reservation.getNights()){
				if(!fromPageAssure){
					fagHasChange=true;
				}
			}			
		}
		
		//超时担保
		if(reservation.isOverTimeAssure()){
			String fromPageArrivalTime=hotelOrderFromBean.getLatestArrivalTime();
			fromPageArrivalTime=fromPageArrivalTime.replace(":","");
			String assureTime=reservation.getLateSuretyTime();
			assureTime=assureTime.replace(":","");
			int assureTimeInt=Integer.parseInt(assureTime);
			int fromPageArrivalTimeInt=Integer.parseInt(fromPageArrivalTime);
			if(fromPageArrivalTimeInt>assureTimeInt){
				if(!fromPageAssure){
					fagHasChange=true;
				}
			}
						
		}
		return fagHasChange;
	}
	
	
	
	/**
	 * 判断满房，判断预订日期内，是否存在不可超和满房的床型型.add by ting.li
	 * @param hotelId
	 * @param roomTypeId
	 * @param checkinDate
	 * @param checkOutDate
	 * @return 满房的床型Id字符串
	 */
	private String checkRoomFull(HotelOrderFromBean hotelOrderFromBean) {
		boolean fagNotexceed = false;
		StringBuilder fullBedType = new StringBuilder();
		List<HtlRoom> htlRooms = hotelRoomService.getHtlRooms(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean
				.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
		
		if (htlRooms != null && htlRooms.size() > 0) {
			for (HtlRoom htlroom : htlRooms) {
				if (htlroom.getRoomState().indexOf(":3") >= 0 || htlroom.getRoomState().indexOf(":4") >= 0) {
					fagNotexceed = true;
					break;
				}
			}
			if (fagNotexceed) {
				String bedTypeStr = hotelOrderFromBean.getBedTypeStr();
				String[] bedType = bedTypeStr.split(",");
				HtlRoomtype roomType = hotelRoomTypeService.getHtlRoomTypeByIdAndHtlId(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId());
				List<HtlQuotaNew> htlQuotas = hotelQuotaNewService.queryQuotaByRoomTypeId(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(),
						hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());

				Map<String, HtlQuotaNew> htlQuotaMap = getHtlQuotaNewMap(htlQuotas);

				for (int i = 0; i < bedType.length; i++) {
					if (checkIsFullBed(htlRooms, bedType[i], htlQuotaMap, roomType)) {
						if (!hasFullBedType(fullBedType.toString(), bedType[i])) {
							fullBedType.append(bedType[i]);
						}
					}
				}

				htlQuotaMap = null;

			}
		}
		htlRooms = null;
		return fullBedType.toString();
	}
		
	private int  getMinQuota(int newQuota,int oldQuota){
		if(newQuota>=oldQuota){
			return oldQuota;
		}
		else{
			return newQuota;
		}
	}
	
	/**
	 * 判断是否是床型共享配额，并且是否有配额.共享配额只录在双床上，如果存在共享配额，就一定有双床。业务是这样的。
	 * @param roomType
	 * @param htlroom
	 * @param htlQuotaMap
	 * @return 共享配额而且是满房返回false，否则返回true
	 */
	private boolean checkIsQuotaBedShare(HtlRoomtype roomType,HtlRoom htlRoom,Map<String,HtlQuotaNew> htlQuotaMap){
		
		if(roomType.getQuotaBedShare()==1L){		
				if(isQuotaFull(htlRoom,htlQuotaMap,"2")){
					return false;
				}				
			}
		
		return true;
	}
	
	/**
	 * add by ting.li 判断传进来的床型是否是不可超或满房
	 * @param htlRooms
	 * @param bedType
	 * @param htlQuotaMap
	 * @return
	 */
	private boolean checkIsFullBed(List<HtlRoom> htlRooms,String bedType,Map<String,HtlQuotaNew> htlQuotaMap,HtlRoomtype roomType){
		boolean fagFullBed=false;
		for(HtlRoom htlroom:htlRooms){
			
			if(htlroom.getRoomState().indexOf(bedType+":3")>=0||htlroom.getRoomState().indexOf(bedType+":4")>=0){
								
					//是共享配额，判断共享配额是否为0.
					if (roomType.getQuotaBedShare() == 1L) {
						if (!checkIsQuotaBedShare(roomType, htlroom, htlQuotaMap)) {
							fagFullBed = true;
							break;
						}
					}
					//不是共享配额
					else{
						if(isQuotaFull(htlroom,htlQuotaMap,bedType)){
							fagFullBed=true;
							break;
						}
						
					}
				}
			}
		
		return fagFullBed;
	}
	
	/**
	 * 判断该床型在该房型下是否为满房。
	 * add by ting.li
	 * @param htlroom 房型
	 * @param htlQuotaMap 查出的配额
	 * @param bedType 床型
	 * @return 配额是0，返回true；否则返回true
	 */
	private boolean isQuotaFull(HtlRoom htlroom,Map<String,HtlQuotaNew> htlQuotaMap,String bedType){
		boolean fagQuotaFull = false;
		
		String key = getQuotaMapKey(htlroom, bedType);
		HtlQuotaNew htlQuota = htlQuotaMap.get(key);				
		int ableQuotaSum = countQuataNum(htlQuota);		
		if (ableQuotaSum == 0) {
			fagQuotaFull = true;
		}		
		return fagQuotaFull;
	}

	/**
	 * 获取配额对象的key
	 * @param htlroom
	 * @param bedType
	 * @return
	 */
	private String getQuotaMapKey(HtlRoom htlroom, String bedType) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		StringBuilder key = new StringBuilder();
		if(htlroom!=null){
		key.append(dateFormat.format(htlroom.getAbleSaleDate()));
		key.append(htlroom.getHotelId());
		key.append(htlroom.getRoomTypeId());
		key.append(bedType);
		}
		return key.toString();
	}
	
	/**
	 * 计算配额
	 * @param htlQuota
	 * @return
	 */
	private int countQuataNum(HtlQuotaNew htlQuota){
		int ableQuotaSum = 0;
		if (htlQuota != null) {
			int buyQuotaAbleNum = (htlQuota.getBuyQuotaAbleNum() == null) ? 0 : htlQuota.getBuyQuotaAbleNum().intValue();
			int casualQuotaAbleNum = (htlQuota.getCasualQuotaAbleNum() == null) ? 0 : htlQuota.getCasualQuotaAbleNum().intValue();
			int commonQuotaAbleNum = (htlQuota.getCommonQuotaAbleNum() == null) ? 0 : htlQuota.getCommonQuotaAbleNum().intValue();
			ableQuotaSum = buyQuotaAbleNum + casualQuotaAbleNum + commonQuotaAbleNum;
		}
		return ableQuotaSum;
	}
	
	/**
	 * add by ting.li
	 * @param htlQuotas，配额对象
	 * @return 配额的Map
	 */
	private Map<String,HtlQuotaNew> getHtlQuotaNewMap(List<HtlQuotaNew> htlQuotas){
		 SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
		 Map<String,HtlQuotaNew> htlQuotaMap=new HashMap<String,HtlQuotaNew>();
		 StringBuilder key;
		 if(htlQuotas!=null){
		 for(HtlQuotaNew htlQuota : htlQuotas){
			 key=new StringBuilder();
			 key.append(dateFormat.format(htlQuota.getAbleSaleDate()));
			 key.append(htlQuota.getHotelId());
			 key.append(htlQuota.getRoomtypeId());
			 key.append(htlQuota.getBedId());
			 htlQuotaMap.put(key.toString(), htlQuota);
		 }
		 }
		 dateFormat=null;
		return htlQuotaMap;
	}
	
	/**
	 * 判断某床型是否在满房的床型的字符中
	 * @param fullBedTypeStr
	 * @param bedType
	 * @return
	 */
	private boolean hasFullBedType(String fullBedTypeStr,String bedType){
		if(fullBedTypeStr.indexOf(bedType)>=0){
			return true;
		}
		return false;
	}
	
	
	public void setHotelQuotaNewService(HotelQuotaNewService hotelQuotaNewService) {
		this.hotelQuotaNewService = hotelQuotaNewService;
	}

	public void setHotelRoomService(HotelRoomService hotelRoomService) {
		this.hotelRoomService = hotelRoomService;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

	public void setHotelReservationInfoService(IHotelReservationInfoService hotelReservationInfoService) {
		this.hotelReservationInfoService = hotelReservationInfoService;
	}


	

}
