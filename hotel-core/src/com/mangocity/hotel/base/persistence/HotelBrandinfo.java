package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.Entity;
public class HotelBrandinfo implements Serializable  {

    /**
     * 酒店品牌名称
     */
    protected String brandname;

    /**
     * 酒店品牌编码
     */
    protected String brandcode;
    
    /**
     * 品牌记录创建时间
     */
    protected String creator;
    
    /**
     * 品牌记录创建人
     */
    protected Date createdate;
    
    /**
     * 备注
     */
    protected String remark;

    
	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public String getBrandcode() {
		return brandcode;
	}

	public void setBrandcode(String brandcode) {
		this.brandcode = brandcode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
