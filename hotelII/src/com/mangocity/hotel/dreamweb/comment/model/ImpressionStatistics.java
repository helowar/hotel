package com.mangocity.hotel.dreamweb.comment.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author liting
 * 用于记录点评信息统计中的印象点评统计信息
 */
public class ImpressionStatistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long impressionStatisticsId;
	private Long hotelId;
	private Long impressionId;		//印象项Id
	private String impressionName;	//印象项名称
	private int impressionNumber;	//印象点评人数
	private Date updateDate;		//更新时间
	
	public Long getImpressionStatisticsId() {
		return impressionStatisticsId;
	}
	public void setImpressionStatisticsId(Long impressionStatisticsId) {
		this.impressionStatisticsId = impressionStatisticsId;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public Long getImpressionId() {
		return impressionId;
	}
	public void setImpressionId(Long impressionId) {
		this.impressionId = impressionId;
	}
	public String getImpressionName() {
		return impressionName;
	}
	public void setImpressionName(String impressionName) {
		this.impressionName = impressionName;
	}
	public int getImpressionNumber() {
		return impressionNumber;
	}
	public void setImpressionNumber(int impressionNumber) {
		this.impressionNumber = impressionNumber;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	

}
