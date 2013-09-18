package com.mangocity.hotel.quotationdisplay.service;

import java.util.List;

import com.mangocity.hotel.quotationdisplay.model.QueryParam;
import com.mangocity.hotel.quotationdisplay.vo.HotelAveragePriceVO;
/***
 * 
 * @author panjianping
 *
 */
public interface IAveragePriceService {
	/***
	 * 查询满足条件的酒店在未来30的平均价格
	 * @param queryParam
	 * @return 含有未来30天酒店平均价格信息的对象列表
	 */
     public List<HotelAveragePriceVO> getAveragePrice(QueryParam queryParam);
     
     /**
      * 获取最大平均值
      * @return
      */
     public int getMaxAveragePrice();
}
