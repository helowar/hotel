package com.mangocity.hotel.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.hagtb2b.assisbean.B2BCommissionRate;
import com.mangocity.hotel.base.dao.impl.CommissionDAOImpl;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.B2BAgentCommUtils;
import com.mangocity.hotel.base.persistence.CommisionAdjust;
import com.mangocity.hotel.base.persistence.HtlB2bComminfo;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.service.CommissionService;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;

public class CommissionServiceImpl implements CommissionService {
	
	private Log log = LogFactory.getLog(CommissionServiceImpl.class);
	
	private CommissionDAOImpl commissionDAO;
	
	private OrOrderDao orOrderDao;
	
	private IPriceManage priceManage;
	
	public IPriceManage getPriceManage() {
		return priceManage;
	}

	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}

	public CommissionDAOImpl getCommissionDAO() {
		return commissionDAO;
	}

	public void setCommissionDAO(CommissionDAOImpl commissionDAO) {
		this.commissionDAO = commissionDAO;
	}

	public OrOrderDao getOrOrderDao() {
		return orOrderDao;
	}

	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}
	/***根据星级查询B2B最高返佣比例和佣金保留比例，参数保存在or_param里***/
	private SystemDataService systemDataService;
	private void setB2BCommssionRate(){
		B2BCommissionRate.b2bMaxReturnRateMap = new HashMap<String,Double>();
		String b2bMaxReturnRateString = systemDataService.getSysParamByName("B2B_MAX_RETURN_RATE").getValue();
		for(String s:b2bMaxReturnRateString.split("&")){
			B2BCommissionRate.b2bMaxReturnRateMap.put(s.split("=")[0], new Double(s.split("=")[1]));
		}
		
		B2BCommissionRate.b2bRemainCommRateMap = new HashMap<String,Double>();
		String b2bRemainCommRateString = systemDataService.getSysParamByName("B2B_REMAIN_COMISSION_RATE").getValue();
		for(String s:b2bRemainCommRateString.split("&")){
			B2BCommissionRate.b2bRemainCommRateMap.put(s.split("=")[0], new Double(s.split("=")[1]));
		}
		
		B2BCommissionRate.b2bElMaxReturnRateMap = new HashMap<String,Double>();
		String b2bElMaxReturnRateString = systemDataService.getSysParamByName("B2B_EL_MAX_RETURN_RATE").getValue();
		for(String s:b2bElMaxReturnRateString.split("&")){
			B2BCommissionRate.b2bElMaxReturnRateMap.put(s.split("=")[0], new Double(s.split("=")[1]));
		}
	}
	//被连住优惠的调用,参数有：售价，芒果佣金，价格类型ID
	public B2BAgentCommUtils getB2BCommInfo(double salePrice, double commisson, String currency, long hotelId,
			long roomTypeID, long priceTypeId, String payMethod, String hotelStar, String agentId) {
		setB2BCommssionRate();
		B2BAgentCommUtils  b2BAgentCommUtils  = new  B2BAgentCommUtils();
		double price = salePrice;//减去服务后的售价
		double agentReadComission = 0d;//代理佣金
		double agentReadComissionRate = 0d;//代理佣金率
		double comissionReturnPercent = 0d;//我们收到的佣金，返给代理商的比例
		double priceReturnPercent = 0d;//售价-服务费后，返给代理商的比例
		
		List<CommisionAdjust> adjustList = commissionDAO.queryByNamedQuery("queryB2BCommAdjust", new Object[] {"0",new Long(hotelId),
				new Long(roomTypeID),new Long(priceTypeId),payMethod});
		if(adjustList!=null && adjustList.size()>0){
			CommisionAdjust adjust = adjustList.get(0);
			if(adjust!=null && adjust.getValueType()==1 && adjust.getComm_value()>0){//如果有对这个酒店的调整，直接用调整的比例来返佣
				priceReturnPercent = adjust.getComm_value()/100;
				agentReadComission = price*priceReturnPercent;
				agentReadComissionRate = price>0 ? agentReadComission/price : 0;
				//set佣金和佣金率
				b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				return b2BAgentCommUtils;
			}else if(adjust!=null && adjust.getValueType()==2 && adjust.getComm_value()>0){
				agentReadComission = getValueFromRMB(adjust.getComm_value(),currency);
				agentReadComissionRate = price>0 ? agentReadComission/price : 0;
				//set佣金和佣金率
				b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				return b2BAgentCommUtils;
			}
		}
		
		//get from or_param
		comissionReturnPercent = 1-B2BCommissionRate.getB2BRemainComRate(hotelStar);
		priceReturnPercent = B2BCommissionRate.getB2BMaxReturnRate(hotelStar, false);
		
		if(commisson != 0 && price != 0){
			//代理佣金需求变化
			/**
			 * 四五星返85%的佣金,其他75%
			 * value1 = 芒果佣金
			 * value2 = (售价-服务费)*9%
			 * value = value1 > value2 ? value2 : value1;
			 */
			double comission1 = commisson*comissionReturnPercent;
			double comission2 = price*priceReturnPercent;
			agentReadComission = comission1<comission2 ? comission1 : comission2;
			agentReadComissionRate = agentReadComission/price;
			//set佣金和佣金率
			b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
			b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
			b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
			
			b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
			b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
			b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
		}
		return b2BAgentCommUtils;
	}
	
	//订单为代理时，调用此方法算出代理佣金
	public B2BAgentCommUtils getB2BCommInfo(boolean isElHotel,String currency,Date abselDate, long hotelId,
			long roomTypeID, long priceTypeId, String payMethod, String hotelStar, String agentId) {
		setB2BCommssionRate();
		B2BAgentCommUtils  b2BAgentCommUtils  = new  B2BAgentCommUtils();
		double price = 0d;//售价-服务费后的价格
		double agentReadComission = 0d;//代理佣金
		double agentReadComissionRate = 0d;//代理佣金率
		double comissionReturnPercent = 0d;//我们收到的佣金，返给代理商的比例
		double priceReturnPercent = 0d;//售价-服务费后，返给代理商的比例
		//get from or_param
		comissionReturnPercent = 1-B2BCommissionRate.getB2BRemainComRate(hotelStar);
		priceReturnPercent = B2BCommissionRate.getB2BMaxReturnRate(hotelStar, false);
		double elMaxReturnPercent = B2BCommissionRate.getB2BMaxReturnRate(hotelStar,true);
		try{
			HtlPrice  priceInfo = priceManage.getPricInfoByFor(abselDate, hotelId,roomTypeID, priceTypeId,payMethod);
			if (priceInfo != null){
				//如果是简单的计算公式,佣金价=售价
				if(priceInfo.getFormulaId()==null||priceInfo.getFormulaId().equals("") || priceInfo.getFormulaId().equals("0")) {
					price = priceInfo.getSalePrice();
				}else{
					price = priceInfo.getCommission()/priceInfo.getCommissionRate();
				}
				
				List<CommisionAdjust> adjustList = commissionDAO.queryByNamedQuery("queryB2BCommAdjust", new Object[] {"0",new Long(hotelId),
						new Long(roomTypeID),new Long(priceTypeId),payMethod});
				if(adjustList!=null && adjustList.size()>0){
					CommisionAdjust adjust = adjustList.get(0);
					if(adjust!=null && adjust.getValueType()==1 && adjust.getComm_value()>0){//如果有对这个酒店的调整，直接用调整的比例来返佣
						if(isElHotel){
							price = priceInfo.getSalePrice();//佣金价
							agentReadComissionRate = adjust.getComm_value()/100;//返佣率
							agentReadComission = price*agentReadComissionRate;//佣金
						}else{
							priceReturnPercent = adjust.getComm_value()/100;
							agentReadComission = price*priceReturnPercent;
							agentReadComissionRate = price>0 ? agentReadComission/price : 0;
						}
						//set佣金和佣金率
						b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
						b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
						b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
						b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
						b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
						b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
						return b2BAgentCommUtils;
					}else if(adjust!=null && adjust.getValueType()==2 && adjust.getComm_value()>0){
						if(isElHotel){
							price = priceInfo.getSalePrice();//佣金价
							agentReadComission = getValueFromRMB(adjust.getComm_value(),currency);//佣金
							agentReadComissionRate =  price>0 ? agentReadComission/price : 0;//返佣率
						}else{
							agentReadComission = getValueFromRMB(adjust.getComm_value(),currency);
							agentReadComissionRate = price>0 ? agentReadComission/price : 0;
						}
						//set佣金和佣金率
						b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
						b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
						b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
						b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
						b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
						b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
						return b2BAgentCommUtils;
					}
				}
				if(isElHotel){
					price = priceInfo.getSalePrice();
					agentReadComission = price*elMaxReturnPercent;
					agentReadComissionRate = price>0 ? elMaxReturnPercent : 0;
				}else{
					double comission1 = priceInfo.getCommission()*comissionReturnPercent;
					double comission2 = price*priceReturnPercent;
					agentReadComission = comission1<comission2 ? comission1 : comission2;
					agentReadComissionRate = agentReadComission/price;
				}
				b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
		        b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
		        b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
		        b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
		        b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
		        b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
			}
		}catch(Exception e) {
			log.error("代理佣金计算出错！",e);
		}
		return b2BAgentCommUtils;
	}
	
	/**
	 * 直联酒店佣金计算公式,直连 芒果佣金=salePrice-basePrice
	 * 
	 */
	public B2BAgentCommUtils getB2BCommInfoZL(double saleprice,double baseprice,String currency, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod,String agentId,String hotelStar) {
		setB2BCommssionRate();
		B2BAgentCommUtils  b2BAgentCommUtils  = new  B2BAgentCommUtils();
		double price = saleprice;//售价-服务费后的价格
		double agentReadComission = 0d;//代理佣金
		double agentReadComissionRate = 0d;//代理佣金率
		double comissionReturnPercent = 0d;//我们收到的佣金，返给代理商的比例
		double priceReturnPercent = 0d;//售价-服务费后，返给代理商的比例
		List<CommisionAdjust> adjustList = commissionDAO.queryByNamedQuery("queryB2BCommAdjust", new Object[] {"0",new Long(hotelId),
				new Long(roomTypeID),new Long(priceTypeId),payMethod});
		if(adjustList!=null && adjustList.size()>0){
			CommisionAdjust adjust = adjustList.get(0);
			if(adjust!=null && adjust.getValueType()==1 && adjust.getComm_value()>0){//如果有对这个酒店的调整，直接用调整的比例来返佣
				priceReturnPercent = adjust.getComm_value()/100;
				agentReadComission = price*priceReturnPercent;
				agentReadComissionRate = price>0 ? agentReadComission/price : 0;
				//set佣金和佣金率
				b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				return b2BAgentCommUtils;
			}else if(adjust!=null && adjust.getValueType()==2 && adjust.getComm_value()>0){//固定金额
				agentReadComission = getValueFromRMB(adjust.getComm_value(),currency);
				agentReadComissionRate = price>0 ? agentReadComission/price : 0;
				//set佣金和佣金率
				b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				return b2BAgentCommUtils;
			}
		}
		
		//get from or_param
		comissionReturnPercent = 1-B2BCommissionRate.getB2BRemainComRate(hotelStar);
		priceReturnPercent = B2BCommissionRate.getB2BMaxReturnRate(hotelStar, false);
		
		double comission1 = (saleprice-baseprice)* comissionReturnPercent;//芒果收到的佣金=saleprice-baseprice
		double comission2 = price*priceReturnPercent;
		agentReadComission = comission1<comission2 ? comission1  : comission2;
		agentReadComissionRate = agentReadComission/price;
		
		//set佣金，佣金率
		b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
        b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(saleprice,2));
        b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
        
        b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
        b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(saleprice,2));
        b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
		return b2BAgentCommUtils;
	}
	
	/**
	 * 优惠信息佣金计算
	 * @param abselDate
	 * @param hotelId
	 * @param roomTypeID
	 * @param priceTypeId
	 * @param payMethod
	 * @param favourableFlag
	 * @param favourableAmount
	 * @param agentId
	 * @param hotelStar
	 * @return
	 */
	public B2BAgentCommUtils getB2BCommInfoForBenefit(String currency,Date abselDate, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod,int favourableFlag,float favourableAmount,String agentId,String hotelStar) {
		setB2BCommssionRate();
		B2BAgentCommUtils  b2BAgentCommUtils  = new  B2BAgentCommUtils();
		double price = 0d;//售价-服务费后的价格
		double agentReadComission = 0d;//代理佣金
		double agentReadComissionRate = 0d;//代理佣金率
		double comissionReturnPercent = 0d;//我们收到的佣金，返给代理商的比例
		double priceReturnPercent = 0d;//售价-服务费后，返给代理商的比例
		//get from or_param
		comissionReturnPercent = 1-B2BCommissionRate.getB2BRemainComRate(hotelStar);
		priceReturnPercent = B2BCommissionRate.getB2BMaxReturnRate(hotelStar, false);
		
		HtlPrice  priceInfo = priceManage.getPricInfoByFor(abselDate, hotelId,roomTypeID, priceTypeId,payMethod);
		//如果是简单的计算公式
		if(priceInfo.getFormulaId()==null||priceInfo.getFormulaId().equals("") || priceInfo.getFormulaId().equals("0")) {
			price = priceInfo.getSalePrice();
		}else{
			price = priceInfo.getCommission()/priceInfo.getCommissionRate();
		}
		
		List<CommisionAdjust> adjustList = commissionDAO.queryByNamedQuery("queryB2BCommAdjust", new Object[] {"0",new Long(hotelId),
				new Long(roomTypeID),new Long(priceTypeId),payMethod});
		if(adjustList!=null && adjustList.size()>0){
			CommisionAdjust adjust = adjustList.get(0);
			if(adjust!=null && adjust.getValueType()==1 && adjust.getComm_value()>0){//如果有对这个酒店的调整，直接用调整的比例来返佣
				priceReturnPercent = adjust.getComm_value()/100;
				agentReadComission = price*priceReturnPercent;
				agentReadComissionRate = price>0 ? agentReadComission/price : 0;
				//set佣金和佣金率
				b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				return b2BAgentCommUtils;
			}else if(adjust!=null && adjust.getValueType()==2 && adjust.getComm_value()>0){//固定金额
				agentReadComission = getValueFromRMB(adjust.getComm_value(),currency);
				agentReadComissionRate = price>0 ? agentReadComission/price : 0;
				//set佣金和佣金率
				b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
				b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
				b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
				return b2BAgentCommUtils;
			}
		}
		
		double comission1 = (priceInfo.getCommission() - favourableAmount)*comissionReturnPercent;//芒果收到的佣金-优惠金额
		double comission2 = price*priceReturnPercent;
		agentReadComission = comission1<comission2 ? comission1  : comission2;
		agentReadComissionRate = agentReadComission/price;
		//set佣金，佣金率
		b2BAgentCommUtils.setAgentComission(StringUtil.Baoliu(agentReadComission,2));
        b2BAgentCommUtils.setAgentComissionPrice(StringUtil.Baoliu(price,2));
        b2BAgentCommUtils.setAgentComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
        
        b2BAgentCommUtils.setAgentReadComission(StringUtil.Baoliu(agentReadComission,2));
        b2BAgentCommUtils.setAgentReadComissionPrice(StringUtil.Baoliu(price,2));
        b2BAgentCommUtils.setAgentReadComissionRate(StringUtil.Baoliu(agentReadComissionRate,3));
		return b2BAgentCommUtils;
	}
	
	public List<HtlB2bComminfo> getAllCommission() {
		return commissionDAO.queryAllCommission();
	}
	//变价
	public Double getCommissionRate(double commissionPrice,double commissionDo,String b2bCD,String hotelStar,String currency, long hotelId,long roomTypeID, long priceTypeId,String payMethod,Date checkinDate) {
		log.info("#################################################变价传值：commissionPrice="+commissionPrice+",commissionDo="+commissionDo
				+",hotelStar="+hotelStar+",currency="+currency+",hotelId="+hotelId+",roomTypeID="+roomTypeID+",priceTypeId="+priceTypeId+",payMethod="+payMethod+",end!");
		setB2BCommssionRate();
		double price = commissionPrice;//佣金价
		double agentReadComission = 0d;//代理佣金
		double agentReadComissionRate = 0d;//代理佣金率
		double comissionReturnPercent = 0d;//我们收到的佣金，返给代理商的比例
		double priceReturnPercent = 0d;//售价-服务费后，返给代理商的比例
		double mangoComission = commissionDo;//芒果佣金
		
		List<CommisionAdjust> adjustList = commissionDAO.queryByNamedQuery("queryB2BCommAdjust", new Object[] {"0",new Long(hotelId),
				new Long(roomTypeID),new Long(priceTypeId),payMethod});
		if(adjustList!=null && adjustList.size()>0){
			CommisionAdjust adjust = adjustList.get(0);
			if(adjust!=null && adjust.getValueType()==1 && adjust.getComm_value()>0){//如果有对这个酒店的调整，直接用调整的比例来返佣
				priceReturnPercent = adjust.getComm_value()/100;
				agentReadComission = price*priceReturnPercent;
				agentReadComissionRate = price>0 ? agentReadComission/price : 0;
				return agentReadComissionRate;
			}else if(adjust!=null && adjust.getValueType()==2 && adjust.getComm_value()>0){//固定金额
				agentReadComission = getValueFromRMB(adjust.getComm_value(),currency);
				agentReadComissionRate = price>0 ? agentReadComission/price : 0;
				return agentReadComissionRate;
			}
		}
		//get from or_param
		comissionReturnPercent = 1-B2BCommissionRate.getB2BRemainComRate(hotelStar);
		priceReturnPercent = B2BCommissionRate.getB2BMaxReturnRate(hotelStar, false);
		
		double comission1 = mangoComission*comissionReturnPercent;
		double comission2 = price*priceReturnPercent;
		agentReadComission = comission1<comission2 ? comission1 : comission2;
		agentReadComissionRate = agentReadComission/price;
		log.info("comission1="+comission1+",comission2="+comission2+",agentReadComission="+agentReadComission);
		return agentReadComissionRate;
	}
	
	
	/**
	 * 取出芒果网保留佣金率
	 * @throws Exception 
	 */
	public double getRemainComission(String hotelStar) throws Exception {
		List<HtlB2bComminfo> listB2bComminfo = new ArrayList<HtlB2bComminfo>();
		listB2bComminfo = commissionDAO.queryByNamedQuery("queryHtlB2bComminfo", new Object[] {hotelStar});
		if(listB2bComminfo!=null && !listB2bComminfo.isEmpty()){
			 HtlB2bComminfo  b2bComminfoObj =   new  HtlB2bComminfo();
			 b2bComminfoObj= listB2bComminfo.get(0);
			 if(b2bComminfoObj != null) {
				 return b2bComminfoObj.getRemainComission();
			 }else {
				 log.error("can't find the comminfoObj where hotelStar :=" + hotelStar);
				 throw new Exception("can't find the comminfoObj where hotelStar :=" + hotelStar);
			 }
		}else {
			 log.error("can't find the comminfoList where hotelStar :=" + hotelStar);
			 throw new Exception("can't find the comminfoList");
		}
	}
	/*
	 * 将当前人民币按照汇率转换为相应币种的金额
	 */
	private double getValueFromRMB(double currentValue, String currency) {
		return currentValue / CurrencyBean.getRateToRMB(currency);
	}

	public SystemDataService getSystemDataService() {
		return systemDataService;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

}
