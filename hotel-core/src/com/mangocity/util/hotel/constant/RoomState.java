package com.mangocity.util.hotel.constant;

/**
 * 订单的状态常量 OrOrder.orderState
 * 
 * @author chenkeming
 * 
 */
public interface RoomState {
	
	//满房
	public final static String ROOM_STATE_FULL = "4";
	
	//不可超
	public final static String ROOM_STATE_NOTOVERRUN = "3";
	
	//紧张
	public final static String ROOM_STATE_TENSE = "2";
	
	//良好
	public final static String ROOM_STATE_NICE = "1";
	
	//freesale
	public final static String ROOM_STATE_FREESALE ="0";
	

}
