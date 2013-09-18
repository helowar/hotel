package com.mangocity.hotel.base.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.B2BAgentCommUtils;
import com.mangocity.hotel.base.persistence.HtlB2bComminfo;

public interface CommissionService {
	
	/**
	 * 获得所有佣金率
	 * 
	 * @return
	 */
	public List<HtlB2bComminfo> getAllCommission();
	
	public B2BAgentCommUtils getB2BCommInfo(boolean isElongHotel,String currency,Date abselDate, long hotelId,
			long roomTypeID, long priceTypeId, String payMethod, String hotelStar, String agentId);
	
	public B2BAgentCommUtils getB2BCommInfo(double salePrice, double commisson, String currency, long hotelId,
			long roomTypeID, long priceTypeId, String payMethod, String hotelStar, String agentId);
	
	public B2BAgentCommUtils getB2BCommInfoZL(double saleprice,double baseprice,String currency, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod,String agentId,String hotelStar);
	
	public B2BAgentCommUtils getB2BCommInfoForBenefit(String currency,Date abselDate, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod,int favourableFlag,float favourableAmount,String agentId,String hotelStar);
	
	
	/**
	 * 变价
	 * @param commissionPrice
	 * @param commissionNo
	 * @param b2bCD
	 * @param hotelStar
	 * @param abselDate
	 * @param hotelId
	 * @param roomTypeID
	 * @param priceTypeId
	 * @param payMethod
	 * @return
	 */
	public Double getCommissionRate(double commissionPrice,double commissionNo,String b2bCD,String hotelStar,String currency, long hotelId,long roomTypeID, long priceTypeId,String payMethod,Date checkinDate);
	
	/**
	 * 取出芒果网保留的佣金率
	 * @return
	 * @throws Exception 
	 */
	public double getRemainComission(String hotelStar) throws Exception;
	

}
