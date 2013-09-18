package com.mangocity.hagtb2b.assisbean;

import java.util.Map;
/**
 * @see 存放B2B的返佣率map
 */
public class B2BCommissionRate {
	public static Map<String,Double> b2bMaxReturnRateMap = null;//B2B最大返佣率map
	public static Map<String,Double> b2bRemainCommRateMap = null;//B2B保留的芒果佣金比率map
	public static Map<String,Double> b2bElMaxReturnRateMap = null;//艺龙酒店B2B最大返佣率map
	
	/**
	 * 根据hotelStar获取最大返佣率
	 * @param hotelStar
	 * @param isElong
	 * @return
	 */
	public static double getB2BMaxReturnRate(String hotelStar,boolean isElong){
		if(b2bMaxReturnRateMap==null
				||b2bRemainCommRateMap==null
				||b2bElMaxReturnRateMap==null){
			throw new RuntimeException("B2B的返佣率设置为空！");
		}
		double drate = 0.0;
		if("5".equals(hotelStar) || "19".equals(hotelStar) || "4.5".equals(hotelStar) || "29".equals(hotelStar)){//五星/豪华
			if(isElong) drate = b2bElMaxReturnRateMap.get("5");
			else drate = b2bMaxReturnRateMap.get("5");
		}else if("39".equals(hotelStar) || "49".equals(hotelStar) || "4".equals(hotelStar) || "3.5".equals(hotelStar)){//四星/高档
			if(isElong) drate = b2bElMaxReturnRateMap.get("4");
			else drate = b2bMaxReturnRateMap.get("4");
		}else if("59".equals(hotelStar) || "64".equals(hotelStar) || "3".equals(hotelStar) || "2.5".equals(hotelStar)){//三星/舒适
			if(isElong) drate = b2bElMaxReturnRateMap.get("3");
			else drate = b2bMaxReturnRateMap.get("3");
		}else{//二星及以下/经济
			if(isElong) drate = b2bElMaxReturnRateMap.get("2");
			else drate = b2bMaxReturnRateMap.get("2");
		}
		return drate;
	}
	/**
	 * 根据hotelStar获取芒果佣金最大保留率
	 * @param hotelStar
	 * @return
	 */
	public static double getB2BRemainComRate(String hotelStar){
		if(b2bMaxReturnRateMap==null
				||b2bRemainCommRateMap==null
				||b2bElMaxReturnRateMap==null){
			throw new RuntimeException("B2B的返佣率设置为空！");
		}
		double drate = 0.0;
		if("5".equals(hotelStar) || "19".equals(hotelStar) || "4.5".equals(hotelStar) || "29".equals(hotelStar)){//五星/豪华
			drate = b2bRemainCommRateMap.get("5");
		}else if("39".equals(hotelStar) || "49".equals(hotelStar) || "4".equals(hotelStar) || "3.5".equals(hotelStar)){//四星/高档
			drate = b2bRemainCommRateMap.get("4");
		}else if("59".equals(hotelStar) || "64".equals(hotelStar) || "3".equals(hotelStar) || "2.5".equals(hotelStar)){//三星/舒适
			drate = b2bRemainCommRateMap.get("3");
		}else if("66".equals(hotelStar) || "69".equals(hotelStar) || "2".equals(hotelStar)){//二星/经济
			drate = b2bRemainCommRateMap.get("2");
		}else{//二星以下/公寓
			drate = b2bRemainCommRateMap.get("1");
		}
		return drate;
	}
}
