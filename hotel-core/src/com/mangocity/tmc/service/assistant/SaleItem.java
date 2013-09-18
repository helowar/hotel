package com.mangocity.tmc.service.assistant;



import java.util.ArrayList;
import java.util.List;

/**
 * 销售项
 * 主要是支付方式和配额类型
 * 支付方式分为面付、预付
 * 配额类型有包房、普通配额等
 * @author bruce.yang
 *
 */
public class SaleItem {
	/**
	 * 配额类型
	 */
	private String quotaType;
	
	/**
	 * 配额类型描述
	 */
	private String quotaTypeNote;
	
	/**
	 * 支付方法代码
	 */
	private String payMethod;
	
	/**
	 * 支付方法描述
	 */
	private String payMethodNote;
	
	/**
	 * 首日价格
	 */
	private String firstDayPrice;
	/**
	 * 无价格日期列表
	 */
	private String noPriceDateStr = "";	
	/**
	 * 按日期排列的房态信息 Date1=1:2/2:3/3:4,Date2=....
	 */
	private String roomStateDateStr = "";
	/**
	 * 是否能被预订，主要判断每天的房态信息
	 */
	private boolean beBooked = true;
	/**
	 * 最晚担保时间(入住时间段内最早的担保时间)
	 */
	private String lastAssureTime;
	
	/**
	 * 房间价格明细, RoomInfo类
	 */
	private List roomInfos = new ArrayList();
	
	/**
	 * 预订按钮显示名称 ‘0’表示面付 ‘1’表示预付 ‘2’表示担保  	
	 * xiaoyong.li 2009-03-14
	 */
	private String bookButton;
	
	
	/**
	 *预订提示：在某日期之前预订，且要预订此房型，必须连住4晚，且入住日期必须包括某天。 
	 * xiaoyong.li 2009-03-14
	 */
	private String bookHintNotMeet;
	
	/**
	 *预订提示：在某日期之前预订，且要预订此房型，必须连住4晚，且入住日期必须包括某天。 
	 * xiaoyong.li 2009-03-14
	 */
	private boolean  canSelect;
	
	/**
	 * 是否有预定条款属性 0为false 1为true
	 * xiaoyong.li 2009-03-14
	 */
	private boolean  hasReserv;

	public String getBookButton() {
		return bookButton;
	}

	public void setBookButton(String bookButton) {
		this.bookButton = bookButton;
	}

	public String getBookHintNotMeet() {
		return bookHintNotMeet;
	}

	public void setBookHintNotMeet(String bookHintNotMeet) {
		this.bookHintNotMeet = bookHintNotMeet;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}

	public List getRoomInfos() {
		return roomInfos;
	}

	public void setRoomInfos(List roomInfos) {
		this.roomInfos = roomInfos;
	}
	
	public int getWeekNum(){
		return (this.roomInfos.size() - 1) / 7 + 1;
	}

	public String getFirstDayPrice() {
		if(this.roomInfos.size() > 0){
			RoomInfo roomInfo =  realFirstRoom();
			firstDayPrice = roomInfo.getSalePrice();
		}
		return firstDayPrice;
	}
	
	public void addRoomInfo(Object o){
		this.roomInfos.add(o);
	}
	
	private RoomInfo realFirstRoom(){
		RoomInfo roomInfo = null;
		for(int i = 0; i < this.roomInfos.size(); i++){
			roomInfo =  (RoomInfo)this.roomInfos.get(i);
			if(!"无".equals(roomInfo.getSalePrice())){
				break;
			}
		}
		return roomInfo;
	}

	public String getNoPriceDateStr() {
		return noPriceDateStr;
	}

	public void setNoPriceDateStr(String noPriceDateStr) {
		this.noPriceDateStr = noPriceDateStr;
	}
	
	public void addNoPriceDate(String dateStr){
		if("".equals(this.noPriceDateStr)){
			this.noPriceDateStr = dateStr;
		}else{
			this.noPriceDateStr = this.noPriceDateStr + "," + dateStr;
		}
	}

	public String getPayMethodNote() {
		return payMethodNote;
	}

	public void setPayMethodNote(String payMethodNote) {
		this.payMethodNote = payMethodNote;
	}

	public String getQuotaTypeNote() {
		return quotaTypeNote;
	}

	public void setQuotaTypeNote(String quotaTypeNote) {
		this.quotaTypeNote = quotaTypeNote;
	}

	public String getRoomStateDateStr() {
		return roomStateDateStr;
	}

	public void setRoomStateDateStr(String roomStateDateStr) {
		this.roomStateDateStr = roomStateDateStr;
	}
	
	public void addRoomStateDate(String stateDateStr){
		if("".equals(this.roomStateDateStr)){
			this.roomStateDateStr = stateDateStr;
		}else{
			this.roomStateDateStr = this.roomStateDateStr + "," + stateDateStr;
		}
	}

	public void setBeBooked(boolean beBooked) {
		this.beBooked = beBooked;
	}
	
	public boolean getBeBooked() {
		return beBooked;
	}

	public String getLastAssureTime() {
		return lastAssureTime;
	}

	public void setLastAssureTime(String lastAssureTime) {
		this.lastAssureTime = lastAssureTime;
	}

	public boolean isCanSelect() {
		return canSelect;
	}

	public void setCanSelect(boolean canSelect) {
		this.canSelect = canSelect;
	}

	public boolean isHasReserv() {
		return hasReserv;
	}

	public void setHasReserv(boolean hasReserv) {
		this.hasReserv = hasReserv;
	}
}
