package com.mangocity.hotel.base.service;

import com.mangocity.hotel.base.persistence.MangoEmapVo;

public interface MangoEmapService {
	public MangoEmapVo queryMangoEmapById(MangoEmapVo mapVo);

	public void saveMangoEmap(MangoEmapVo mangoEmapVo);
	
	/**
	 * 保存百度地标
	 */
	public void saveMangoEmapBaidu(MangoEmapVo mangoEmapVo);
}
