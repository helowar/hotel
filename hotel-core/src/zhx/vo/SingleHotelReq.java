 
package zhx.vo;


/**
 * 和各个合作方直连的单酒店查询接口参数类 
 * 
 * @author chenkeming
 *
 */
public class SingleHotelReq {

    /**
     * 渠道
     */
    protected int channelType;
    
    /**
     * 芒果网的酒店ID
     */
    protected long hotelId;
    
    /**
     * 芒果网的房型ID
     */
    protected long roomTypeId;
    
    /**
     * 芒果网的价格类型ID
     */
    protected long childRoomTypeId;
    
    /**
     * 入住日期字符串
     */
    protected String checkInDate;
    
    /**
     * 离店日期字符串
     */
    protected String checkOutDate;
    
    /**
     * 房间数
     */
    private int quantity;

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public long getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(long childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}    

}
