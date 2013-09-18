package com.mangocity.hotel.base.util;

public class ResourceUtil {
	private String hotel_artifactory_user;
	private String hotel_artifactory_password;
	private String htl_pic_online_url;
	private String htl_pic_offline_url;
	private ResourceUtil resourceUtil;
	public  String[] getArtifactoryInfo(){
		String[] artiInfo = new String[2];
		artiInfo[0] = hotel_artifactory_user;
		artiInfo[1] = hotel_artifactory_password;
		return artiInfo;
	}
	 //查询artifactory路径
    public  String queryArtifactoryUrl(){
    	return htl_pic_online_url;
    }
    
	public String getHtl_pic_online_url() {
		return htl_pic_online_url;
	}

	public void setHtl_pic_online_url(String htlPicOnlineUrl) {
		htl_pic_online_url = htlPicOnlineUrl;
	}

	public String getHtl_pic_offline_url() {
		return htl_pic_offline_url;
	}

	public void setHtl_pic_offline_url(String htlPicOfflineUrl) {
		htl_pic_offline_url = htlPicOfflineUrl;
	}
	public String getHotel_artifactory_user() {
		return hotel_artifactory_user;
	}
	public void setHotel_artifactory_user(String hotelArtifactoryUser) {
		hotel_artifactory_user = hotelArtifactoryUser;
	}
	public String getHotel_artifactory_password() {
		return hotel_artifactory_password;
	}
	public void setHotel_artifactory_password(String hotelArtifactoryPassword) {
		hotel_artifactory_password = hotelArtifactoryPassword;
	}
	public ResourceUtil getResourceUtil() {
		return resourceUtil;
	}
	public void setResourceUtil(ResourceUtil resourceUtil) {
		this.resourceUtil = resourceUtil;
	}
	
}
