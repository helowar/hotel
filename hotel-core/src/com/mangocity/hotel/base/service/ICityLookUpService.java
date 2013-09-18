package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlArea;

public interface ICityLookUpService {

	 /**
     * 网站查询全部城市 add by zhineng.zhuang 2008-9-24
     * 
     * 
     */
	// refactor : that method is from com.mangocity.hweb.manage.HotelManageWeb
    public List<HtlArea> queryAllCity();
}
