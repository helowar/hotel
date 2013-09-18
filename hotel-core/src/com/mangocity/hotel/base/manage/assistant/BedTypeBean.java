package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
/**
 * 床型类型实体类
 * @author zuoshengwei
 *
 */
public class BedTypeBean implements Serializable {
	
	//房型类型ID
    private Long roomTypeId;
    
    //床型类型ID
    private Long bedTypeId;
    
    //床型名称
    private String bedTypeName;
  
    /**
     * 配额床型共享
     */
    private Long quotaBedShare;

	public Long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public Long getBedTypeId() {
		return bedTypeId;
	}

	public void setBedTypeId(Long bedTypeId) {
		this.bedTypeId = bedTypeId;
	}

	public String getBedTypeName() {
		return bedTypeName;
	}

	public void setBedTypeName(String bedTypeName) {
		this.bedTypeName = bedTypeName;
	}

	public Long getQuotaBedShare() {
		return quotaBedShare;
	}

	public void setQuotaBedShare(Long quotaBedShare) {
		this.quotaBedShare = quotaBedShare;
	}
	
}
