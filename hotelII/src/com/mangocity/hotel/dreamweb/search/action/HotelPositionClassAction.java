package com.mangocity.hotel.dreamweb.search.action;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

public class HotelPositionClassAction  extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private Map<Integer,String> classMap;
	public String execute(){
		classMap=new HashMap<Integer,String>();
		classMap.put(21, "交通枢纽");
		classMap.put(23, "景点地标");
		classMap.put(24, "地铁站");
		classMap.put(26, "大学");
		classMap.put(27, "医院");
		classMap.put(28, "主题");
		classMap.put(29, "交通");
		classMap.put(30, "商企");
		classMap.put(31, "生活服务");
		classMap.put(32, "购物");
		classMap.put(33, "餐馆");
		return SUCCESS;
	}
	public Map<Integer, String> getClassMap() {
		return classMap;
	}
	public void setClassMap(Map<Integer, String> classMap) {
		this.classMap = classMap;
	}

}
