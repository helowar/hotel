package com.mangocity.hotel.search.service.assistant;
import com.mangocity.hotel.search.constant.HotelRoomFixtureConstant;
import com.mangocity.hotel.search.constant.MealFixtureConstant;
import com.mangocity.hotel.search.constant.SpecialRequestConstant;
import com.mangocity.hotel.search.constant.FreeServiceConstant;
public class SpecialRequestDividation {
	
	/**
	 * 
	 * @param specialRequest
	 * @return
	 */
	public static SpecialRequest divideSpecialRequest(String specialRequest){
		//该代码基本没有什么扩展性，改的话，一定要注意 以后加个12,用contians("2")会出现问题的。
		if(null==specialRequest ){return new SpecialRequest();}	
		SpecialRequest specialRequestPara = new SpecialRequest();
		//-------------宽带-----------------------
		if(specialRequest.contains(SpecialRequestConstant.broadBand)){
			specialRequestPara.setContainBroadBand(true);
		}
		//------------含早对应htl_price中的inc_Breakfast_Number--------------
		//含早
		if(specialRequest.contains(SpecialRequestConstant.containsBreakFast)){
			specialRequestPara.setContainBreakfast(true);
		}
		//------------最近开业装修-----------------
		if(specialRequest.contains(SpecialRequestConstant.recentlyOpenedAndFit)){
			specialRequestPara.setRecentlyOpenedAndFit(true);
		}
		//-------------房间设施--------------------
		//roomEquipment对应htl_hotel下的roomfixture
		//电脑
		if (specialRequest.contains(SpecialRequestConstant.computer)){
			specialRequestPara.getListRoomEquipment().add(HotelRoomFixtureConstant.computer);
		}
        //-------------餐饮休闲设施-----------------
		//mealFixtrue对应htl_hotel下的meal_fixture
		//会议厅
		if(specialRequest.contains(SpecialRequestConstant.conferenceHall)){
			specialRequestPara.getListMealFixtrue().add(MealFixtureConstant.conferenceHall);
		}
		//--------------免费设施-------------------
		//freeService对应htl_hotel下的free_service
		//免费游泳池
		if(specialRequest.contains(SpecialRequestConstant.freeSwimmingPool)){
			specialRequestPara.getFreeService().add(FreeServiceConstant.freeSwimmingPool);
		}
		//免费健身房
		if(specialRequest.contains(SpecialRequestConstant.freeGym)){
			specialRequestPara.getFreeService().add(FreeServiceConstant.freeGym);
		}
		//免费停车场
		if(specialRequest.contains(SpecialRequestConstant.freeParking)){
			specialRequestPara.getFreeService().add(FreeServiceConstant.freeParking);
		}
	    return specialRequestPara;
	}
	
}
