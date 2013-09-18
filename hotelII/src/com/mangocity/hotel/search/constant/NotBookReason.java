package com.mangocity.hotel.search.constant;

import java.util.HashMap;
import java.util.Map;

public class NotBookReason {

	public static Map<String,String> reason = new HashMap<String,String>();
	/**
	 * 价格为0
	 */
	public static final String NoPrice = "1";
	
	/**
	 * 关房
	 */
	public static final String CloseRoom = "2";

	/**
	 * 不满足条款
	 */
	public static final String NotSatisfyClause = "3";
	
	/**
	 * 满房
	 */
	public static final String Roomful = "4";
	
    static {
    	reason.put("", "");
    	reason.put(null,"");
    	reason.put(NoPrice, "非常抱歉，暂时无法安排此房型的房间，建议您预订该酒店的其他房型或改订其他酒店");
    	reason.put(CloseRoom, "非常抱歉，暂时无法安排此房型的房间，建议您预订该酒店的其他房型或改订其他酒店");
    	reason.put(NotSatisfyClause, "无法安排此房间");
    	reason.put(Roomful, "满房");
    }

}
