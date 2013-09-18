package com.mangocity.hotel.quotationdisplay.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.mangocity.hotel.search.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




import com.mangocity.hotel.quotationdisplay.dao.IAveragePriceDao;
import com.mangocity.hotel.quotationdisplay.model.QueryParam;
import com.mangocity.hotel.quotationdisplay.service.IAveragePriceService;
import com.mangocity.hotel.quotationdisplay.vo.HotelAveragePriceVO;
import com.mangocity.util.hotel.constant.HotelStarType;

/***
 * 
 * @author panjianping
 *
 */
public class AveragePriceServiceImpl implements IAveragePriceService {
    private IAveragePriceDao averagePriceDao;
    private int maxAvgPrice;//未来30的平均价格的最大值
    private static final Log log= LogFactory.getLog(AveragePriceServiceImpl.class);
    /***
	 * 查询满足条件的酒店在未来30的平均价格
	 * @param queryParam
	 * @return 含有未来30天酒店平均价格信息的对象列表
	 */
	public List<HotelAveragePriceVO> getAveragePrice(QueryParam queryParam) {

		List<HotelAveragePriceVO> avgPrices = new ArrayList<HotelAveragePriceVO>();
		List<Integer> prices = new ArrayList<Integer>();
		for (int i = 0; i < 30; i++) {
			queryParam.setDate(DateUtil.getDate(DateUtil.getSystemDate(), i));
			List hotelPriceSortList = averagePriceDao
					.queryHotelOnCondition(queryParam);
			HotelAveragePriceVO avgPrice = new HotelAveragePriceVO();
			double sum = 0;
			int num = 0;
			if (0 != hotelPriceSortList.size()) {
				for (Iterator itr = hotelPriceSortList.iterator(); itr
						.hasNext();) {
					Map objMap = (Map) itr.next();
					if (objMap.get("min_price") != null) {
						double lowestPrice = Double.parseDouble((objMap
								.get("min_price")).toString());
						if (lowestPrice > 50 && lowestPrice < 50000) {
							sum += lowestPrice;
							num++;
						}
					}
				}
				int averagePrice = (int) (sum / num);			
				prices.add(averagePrice);
				avgPrice.setPrice(new Integer(averagePrice).toString());
			} else {
				avgPrice.setPrice("");//无数据
			}
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd E");
			String date = dateformat.format(queryParam.getDate());
			String outDate = dateformat.format(DateUtil.getNextDate(queryParam.getDate()));//默认离店时间为当前日期的后一天
			avgPrice.setDate(date);
			StringBuffer linkBuf =new StringBuffer();
			linkBuf.append(avgPrice.getLink());
			linkBuf.append("?cityCode="+queryParam.getCityCode());
			linkBuf.append("&inDate="+date.substring(0,10));
			linkBuf.append("&outDate="+outDate.substring(0,10));		
			if(null!=queryParam.getZone()&&""!=queryParam.getZone()&&!"bxdq".equals(queryParam.getZone().trim())){//增加商圈限制条件
				  linkBuf.append("&distinctCode="+queryParam.getZone());
				}
			if(queryParam.getLevel()==true){  //查询中高档酒店
				linkBuf.append("&hotelStar="+HotelStarType.ADVANCED);
			}else{                             //查询经济型酒店
				linkBuf.append("&hotelStar="+HotelStarType.ECONOMY);
			}
			avgPrice.setLink(linkBuf.toString());
			avgPrices.add(avgPrice);
		}
		if (prices.size()!=0){
				 maxAvgPrice = Collections.max(prices);	
		}
		return avgPrices;
	}

	public IAveragePriceDao getAveragePriceDao() {
		return averagePriceDao;
	}

	public void setAveragePriceDao(IAveragePriceDao averagePriceDao) {
		this.averagePriceDao = averagePriceDao;
	}

	public int getMaxAveragePrice() {
		return this.maxAvgPrice;
	}
    
	

}
