package com.mangocity.hotel.order.web;

import java.io.Serializable;
import java.sql.SQLException;

import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.order.constant.SessionNames;
import com.mangocity.hotel.base.service.IHotelService;

/**
 * CC查询酒店异步分页Action
 * @author chenjiajie
 *
 */
public class HotelSearchPagesAction extends OrderAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7574013830461402022L;
	
	/**
     * 酒店查询条件类
     */
    private HotelQueryCondition queryCond;

    /**
     * 对外酒店本部服务提供接口
     */
	protected IHotelService hotelService;
    
	/**
	 * 查询分页数据
	 * @return
	 */
	public String searchPages(){
		queryCond = (HotelQueryCondition) getFromSession(SessionNames.QUERY_COND);
        if (null != queryCond) {
        	try {
				//异步分页，设置查询条件，不忽略分页查询 add by chenjiajie 2010-03-22
				queryCond.setIgnorePageCount(0);
				queryCond.setIgnoreQueryList(1);
				hotelService.queryHotels(queryCond);
        	} catch (SQLException e) {
				log.error(e.getMessage(),e);
			}
        }
		return SUCCESS;
	}

	/** getter and setter **/
	public HotelQueryCondition getQueryCond() {
		return queryCond;
	}

	public void setQueryCond(HotelQueryCondition queryCond) {
		this.queryCond = queryCond;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}
}
