package com.mangocity.hotel.base.service;

import com.mangocity.hotel.base.persistence.MangoEmapVo;

public interface MangoEmapService {
	public MangoEmapVo queryMangoEmapById(MangoEmapVo mapVo);

	public void saveMangoEmap(MangoEmapVo mangoEmapVo);
	
	/**
	 * ����ٶȵر�
	 */
	public void saveMangoEmapBaidu(MangoEmapVo mangoEmapVo);
}
