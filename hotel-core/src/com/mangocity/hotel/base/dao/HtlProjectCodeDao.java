package com.mangocity.hotel.base.dao;

import com.mangocity.hotel.base.persistence.HtlProjectCode;

/**
 * 用于保存cookie中的值
 * @author xuyiwen
 *
 */
public interface HtlProjectCodeDao {
	void saveHtlProjectCode(HtlProjectCode htlProjectCode);
	
	boolean haveProjectCode(String orderCD);
}
