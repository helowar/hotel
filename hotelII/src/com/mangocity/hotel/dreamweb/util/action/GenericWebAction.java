package com.mangocity.hotel.dreamweb.util.action;

import java.util.Map;

import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;

/**
 * 盗梦计划的action的父类
 * 主要提供一些公用的全局变量和封装Bean的方法
 * @author diandian.hou
 *
 */
public class GenericWebAction extends GenericAction {

	/**
	 * 把查询条件放到Session中
	 * @param queryHotelForWebBean
	 */
	protected void setWebBeanFromSession(QueryHotelForWebBean queryHotelForWebBean){
		Map session = super.getSession();
		session.put("queryHotelForWebBean", queryHotelForWebBean);
	}

}
