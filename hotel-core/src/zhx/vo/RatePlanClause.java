
package zhx.vo;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrPriceDetail;

/**
 * 中航信政策的返回类
 * 
 * @author zuoshengwei
 *
 */
public class RatePlanClause {
                    
    private long hotelId;
       
    private long roomTypeId;
       
    private long childRoomTypeId;
       
    private double totalNum;
    
    private double firstDayNum;
       
    private String revStatus;
    
    private String bookingCluaseTip;
    
    private boolean overTimeGuarantee;
    
    private String lastGuaranteeTime;
    
    private boolean rationRoomsGuarantee;
    
    private int  rationRooms;
    
    private boolean rationNightsGuarantee;
    
    private int rationNights;
    
    private boolean advanceNightsGuarantee;
    
    private int advanceNights;
                    
    private List<GuaranteeClause> lstGua = new ArrayList<GuaranteeClause>();
       
    private List<CancelClause> lstCancel = new ArrayList<CancelClause>();
    
    private List<OrPriceDetail> lstPriceDetail = new ArrayList<OrPriceDetail>();

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

	public long getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(long childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	public double getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(double totalNum) {
		this.totalNum = totalNum;
	}

	public String getRevStatus() {
		return revStatus;
	}

	public void setRevStatus(String revStatus) {
		this.revStatus = revStatus;
	}

	public List<GuaranteeClause> getLstGua() {
		return lstGua;
	}

	public void setLstGua(List<GuaranteeClause> lstGua) {
		this.lstGua = lstGua;
	}

	public List<CancelClause> getLstCancel() {
		return lstCancel;
	}

	public void setLstCancel(List<CancelClause> lstCancel) {
		this.lstCancel = lstCancel;
	}

	public String getBookingCluaseTip() {
		return bookingCluaseTip;
	}

	public void setBookingCluaseTip(String bookingCluaseTip) {
		this.bookingCluaseTip = bookingCluaseTip;
	}

	public boolean isOverTimeGuarantee() {
		return overTimeGuarantee;
	}

	public void setOverTimeGuarantee(boolean overTimeGuarantee) {
		this.overTimeGuarantee = overTimeGuarantee;
	}

	public String getLastGuaranteeTime() {
		return lastGuaranteeTime;
	}

	public void setLastGuaranteeTime(String lastGuaranteeTime) {
		this.lastGuaranteeTime = lastGuaranteeTime;
	}

	public boolean isRationRoomsGuarantee() {
		return rationRoomsGuarantee;
	}

	public void setRationRoomsGuarantee(boolean rationRoomsGuarantee) {
		this.rationRoomsGuarantee = rationRoomsGuarantee;
	}

	public int getRationRooms() {
		return rationRooms;
	}

	public void setRationRooms(int rationRooms) {
		this.rationRooms = rationRooms;
	}

	public boolean isRationNightsGuarantee() {
		return rationNightsGuarantee;
	}

	public void setRationNightsGuarantee(boolean rationNightsGuarantee) {
		this.rationNightsGuarantee = rationNightsGuarantee;
	}

	public int getRationNights() {
		return rationNights;
	}

	public void setRationNights(int rationNights) {
		this.rationNights = rationNights;
	}

	public boolean isAdvanceNightsGuarantee() {
		return advanceNightsGuarantee;
	}

	public void setAdvanceNightsGuarantee(boolean advanceNightsGuarantee) {
		this.advanceNightsGuarantee = advanceNightsGuarantee;
	}

	public int getAdvanceNights() {
		return advanceNights;
	}

	public void setAdvanceNights(int advanceNights) {
		this.advanceNights = advanceNights;
	}

	public double getFirstDayNum() {
		return firstDayNum;
	}

	public void setFirstDayNum(double firstDayNum) {
		this.firstDayNum = firstDayNum;
	}

	public List<OrPriceDetail> getLstPriceDetail() {
		return lstPriceDetail;
	}

	public void setLstPriceDetail(List<OrPriceDetail> lstPriceDetail) {
		this.lstPriceDetail = lstPriceDetail;
	}
				
}
