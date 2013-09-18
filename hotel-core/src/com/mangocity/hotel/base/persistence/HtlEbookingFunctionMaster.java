package com.mangocity.hotel.base.persistence;

import java.io.Serializable;


public class HtlEbookingFunctionMaster implements Serializable{

private static final long serialVersionUID = 885529214448113945L;
	
	/**
	 * 功能模块ID
	 */
	private Long funmasID;
	/**
	 * 功能模块名称
	 */
	private String funmasName;
	/**
	 * 功能模块描述
	 */
	private String funmasCom;
	/**
	 * 功能有效性
	 */
	private int funmasvalidity;//0无效，1有效
	/**
	 * 根目录名称 
	 */
	private String rootName;
	/**
	 * 根目录名称 
	 */
	private String isCheck;
	
	public Long getFunmasID() {
		return funmasID;
	}
	public void setFunmasID(Long funmasID) {
		this.funmasID = funmasID;
	}
	public String getFunmasName() {
		return funmasName;
	}
	public void setFunmasName(String funmasName) {
		this.funmasName = funmasName;
	}
	public String getFunmasCom() {
		return funmasCom;
	}
	public void setFunmasCom(String funmasCom) {
		this.funmasCom = funmasCom;
	}
	public int getFunmasvalidity() {
		return funmasvalidity;
	}
	public void setFunmasvalidity(int funmasvalidity) {
		this.funmasvalidity = funmasvalidity;
	}
	public String getRootName() {
		return rootName;
	}
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	

}
