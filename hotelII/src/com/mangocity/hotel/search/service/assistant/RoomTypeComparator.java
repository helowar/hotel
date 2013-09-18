package com.mangocity.hotel.search.service.assistant;

import java.util.Comparator;
import com.mangocity.util.StringUtil;



/**
 * 房型排序
 * 不可预订的房型（价格类型）排在最后
 *可预订房型按照本部设定推荐房型级别进行排序
 *基础房型排在非基础房型前面
 * @author 
 *
 */
public class RoomTypeComparator implements Comparator<RoomTypeInfo>{
	

	
	public int compare(RoomTypeInfo r1, RoomTypeInfo r2){
		int r1int = r1.isCanbook()==true?1:0;
		int r2int = r2.isCanbook()==true?1:0;
		if(r1int>r2int){
			return 1;
		}else if(r1int<r2int){
			return -1;
		}
		r1int = false==StringUtil.isValidStr(r1.getRecommend())?0:Integer.parseInt(r1.getRecommend());
		r2int = false==StringUtil.isValidStr(r2.getRecommend())?0:Integer.parseInt(r2.getRecommend());
		if(r1int>r2int){
			return 1;
		}else if(r1int<r2int){
			return -1;
		}
		
		
		if(r1.isBaseRoom()&&r2.isBaseRoom()){
			return 0;
		}else if(!r1.isBaseRoom()&&!r2.isBaseRoom()){
			return 0;
		}else if(r1.isBaseRoom()){
			return 1;
		}else{
			return -1;
		}


	}
}
