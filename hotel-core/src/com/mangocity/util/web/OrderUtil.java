package com.mangocity.util.web;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.mangocity.util.log.MyLog;


/**
 * 
 * @author Luoqi
 *
 */
public class OrderUtil
{
	private static final MyLog log = MyLog.getLogger(OrderUtil.class);
	/**
	 * 根据输入的精度计算类型，返回精度计算结果
	 * @param oldValue 需进行精度计算的值
	 * @param precisionType 精度计算类型
	 * @return
	 */
	public static double calcPrecision(double oldValue,int precisionType)
	{
		BigDecimal newValueBig = new BigDecimal(0.0);
		BigDecimal oldValueBig = new BigDecimal(oldValue);
		
		
		BigDecimal compDig = oldValueBig.setScale(0, RoundingMode.CEILING);
		//表示小数点后有实际值
		if(compDig.compareTo(oldValueBig) == 1)
		{
			// 先四舍五入保留四位小数，避免0.0000000000001及0.999999999999999的值出现
			oldValueBig = oldValueBig.setScale(4,RoundingMode.HALF_UP);
		}
		else
		{
			precisionType = 1000;
			oldValueBig = oldValueBig.setScale(0,RoundingMode.FLOOR);
		}
		
		switch (precisionType)
		{
			case 1://在第三位小数四舍五入保留两位小数
				newValueBig = oldValueBig.setScale(2,RoundingMode.HALF_UP);
				break;
			case 2://遇到小数直接进到十位 23.8=30,35.142=40
				BigDecimal temp = oldValueBig.setScale(0, RoundingMode.FLOOR);
				
				//modify of changrui begin
				double tempV = (temp.intValue()/10 + 1) * 10;
				newValueBig = new BigDecimal(tempV);
				
				//if(0 != oldValueBig.compareTo(temp))
				//{
				//	oldValueBig = oldValueBig.add(new BigDecimal(10));
				//}
				//newValueBig = oldValueBig.setScale(0,RoundingMode.FLOOR);
				//modify of changrui end
				
				break;
			case 3://第一位小数四舍五入到个位
				newValueBig = oldValueBig.setScale(0,RoundingMode.HALF_UP);
				break;
			case 4://第一位小数四舍五入到十位 24.1256=24.13,  24.531=30,  24.30=24.30
				
				BigDecimal bd = oldValueBig.setScale(0,RoundingMode.HALF_UP);
				if(bd.compareTo(oldValueBig) == 1)
				{
					double bdV = (bd.intValue()/10 + 1) * 10;
					newValueBig = new BigDecimal(bdV);
				}
				else
				{
					newValueBig = oldValueBig.setScale(2,RoundingMode.HALF_UP);
				}
				//if(bd.compareTo(oldValueBig) > 0)
				//{
				//	oldValueBig = oldValueBig.add(new BigDecimal(10));
				//}
				//newValueBig = oldValueBig.setScale(0,RoundingMode.FLOOR);
				
				break;
			default:
				newValueBig = oldValueBig;
				break;
		}
		return newValueBig.doubleValue();
	}
	
	public static double calcPrecision(double oldValue,String precisionType){
		double result = 0d;
		if(precisionType!=null &&  !precisionType.equals("")){
			int p = 0;
			try{
				p = Integer.parseInt(precisionType);
				result  = calcPrecision(oldValue,p);
			}catch(Exception ex){
				log.error(ex);
			}
		}
		return result;
	}
	
	/**
	 * 四舍五入 精确两位小数
	 * @param vouchAmountIn
	 * @return
	 */
	public static double roundPrice(double vouchAmountIn){
		return BigDecimal.valueOf(vouchAmountIn).setScale(
				2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	

	
	
	public static void main(String[] args)
	{
		System.out.println(OrderUtil.calcPrecision(24.0,"2"));
		System.out.println(OrderUtil.calcPrecision(24.531,"4"));
		System.out.println(OrderUtil.calcPrecision(24.30,"4"));
		System.out.println(OrderUtil.calcPrecision(35.142,"4"));
	}
}
