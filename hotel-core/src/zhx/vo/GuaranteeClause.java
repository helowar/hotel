
package zhx.vo;

/**
 * 担保政策的返回类
 * 
 * @author zuoshengwei
 *
 */
public class GuaranteeClause {

					private String guaranteeType;
                    private String shortDesc;
                    private String longDesc;
                    private String guaranteeDate;
                    private String unitOfFee;
                    private String feeValue;
                    private String holdTime;
                    private String daysOfStay;
                    private String numOfRoom;
                    private String startDate;
                    private String endDate;
                    private long hotelId;
                    private long roomTypeId;
                    private long childRoomTypeId;
                    private String guaranteeClauseTip;
                    
					public String getGuaranteeType() {
						return guaranteeType;
					}
					public void setGuaranteeType(String guaranteeType) {
						this.guaranteeType = guaranteeType;
					}
					public String getShortDesc() {
						return shortDesc;
					}
					public void setShortDesc(String shortDesc) {
						this.shortDesc = shortDesc;
					}
					public String getLongDesc() {
						return longDesc;
					}
					public void setLongDesc(String longDesc) {
						this.longDesc = longDesc;
					}
					public String getGuaranteeDate() {
						return guaranteeDate;
					}
					public void setGuaranteeDate(String guaranteeDate) {
						this.guaranteeDate = guaranteeDate;
					}
					public String getUnitOfFee() {
						return unitOfFee;
					}
					public void setUnitOfFee(String unitOfFee) {
						this.unitOfFee = unitOfFee;
					}
					public String getFeeValue() {
						return feeValue;
					}
					public void setFeeValue(String feeValue) {
						this.feeValue = feeValue;
					}
					public String getHoldTime() {
						return holdTime;
					}
					public void setHoldTime(String holdTime) {
						this.holdTime = holdTime;
					}
					public String getDaysOfStay() {
						return daysOfStay;
					}
					public void setDaysOfStay(String daysOfStay) {
						this.daysOfStay = daysOfStay;
					}
					public String getNumOfRoom() {
						return numOfRoom;
					}
					public void setNumOfRoom(String numOfRoom) {
						this.numOfRoom = numOfRoom;
					}
					public long getChildRoomTypeId() {
						return childRoomTypeId;
					}
					public void setChildRoomTypeId(long childRoomTypeId) {
						this.childRoomTypeId = childRoomTypeId;
					}
					public String getStartDate() {
						return startDate;
					}
					public void setStartDate(String startDate) {
						this.startDate = startDate;
					}
					public String getEndDate() {
						return endDate;
					}
					public void setEndDate(String endDate) {
						this.endDate = endDate;
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
					public String getGuaranteeClauseTip() {
						return guaranteeClauseTip;
					}
					public void setGuaranteeClauseTip(String guaranteeClauseTip) {
						this.guaranteeClauseTip = guaranteeClauseTip;
					}
				
}
