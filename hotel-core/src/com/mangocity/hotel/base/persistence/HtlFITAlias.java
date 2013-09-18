/**
 * 
 */
package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

/**
 * @author xiongxiaojun
 *
 */
public class HtlFITAlias implements Serializable {
	private static final long serialVersionUID = 1L;
    
	//ID
	private Long ID;
	
	//项目号
	private String aliasId;
	
	//会籍名称
	private String name;
	
	//备注
	private String memo;
	
	//是否激活
	private String active;
	
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public String getAliasId() {
		return aliasId;
	}
	public void setAliasId(String aliasId) {
		this.aliasId = aliasId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
}
