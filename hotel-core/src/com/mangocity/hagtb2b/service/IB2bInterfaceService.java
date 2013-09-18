package com.mangocity.hagtb2b.service;

import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;

/**
 * 调用hessian接口service
 * add by zhiejie.gu 2010-8-20
 */
public interface IB2bInterfaceService  {
	
	/**
	 * b2b酒店列表查询
	 * @param queryHotelForWebBean
	 * @return
	 * @throws Exception
	 */
	public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean queryHotelForWebBean)throws Exception;
	
	/**
	 * b2b酒店详情页面调用查询
	 * @param queryHotelForWebBean
	 * @return
	 * @throws Exception
	 */
	public QueryHotelForWebResult queryHotelInfoBeanForWebs(QueryHotelForWebBean queryHotelForWebBean)throws Exception;
	
	/**
	 * b2b单独查询分页信息页数，页码，总记录数
	 * @param queryHotelForWebBean
	 * @return
	 * @throws Exception
	 */
	public HotelPageForWebBean queryHotelPagesForWeb(QueryHotelForWebBean queryHotelForWebBean)throws Exception;
	
    
}
