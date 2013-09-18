package com.mangocity.hotel.quotationdisplay.vo;

/**
 * 城区对象，多个ZoneVO对象将以json数组的形式返回给客户端，用于动态显示城市的商业区
 */
public class ZoneVO {
	private String zoneCode;  //城区编码
	private String zoneName;  //城区名称
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	
	
	

}
