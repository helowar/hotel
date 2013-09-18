package com.mangocity.hotel.base.persistence;


/**
 * B2B�ӷ�
 * @author xuyiwen
 *
 */
public class B2BIncrease {
	
	private long id;
	
	private long hotelId; //酒店ID
	
	private double increaseRate; //�ӷ���� С��加幅情况�
	
	private java.util.Date createTime; //创建时间 方便取时分秒����ʱ��
	
	private String createName; //创建人������
	
	private int flag; //�Ƿ���Ч���是否生效��Ч

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public double getIncreaseRate() {
		return increaseRate;
	}

	public void setIncreaseRate(double increaseRate) {
		this.increaseRate = increaseRate;
	}

	

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public String toString(){
		return "id:" + id +"hotelId:" + hotelId + "increaseRate:" + increaseRate + "createName:" + createName + "flag:" + flag; 
	}
}
