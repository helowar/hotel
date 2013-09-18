package com.mangocity.hotel.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mangocity.hotel.base.dao.ExMappingDao;
import com.mangocity.hotel.base.dao.HtlElAssureRuleDao;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlElAssure;
import com.mangocity.hotel.base.persistence.HtlElAssureRule;
import com.mangocity.hotel.order.service.HotelElOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

public class HotelElOrderServiceImpl implements HotelElOrderService {
	private ExMappingDao  exMappingDao;
	
	private HtlElAssureRuleDao htlElAssureRuleDao;
	
	private Logger logger = Logger.getLogger(HotelElOrderServiceImpl.class);

	public HtlElAssureRule queryElAssureRule(long priceTypeId,Date checkInDate) {
		//获取该订单价格类型的映射
    	ExMapping ex=exMappingDao.queryExPrice(priceTypeId);
    	//获取艺龙担保条款
    	List<HtlElAssureRule> htlElAssureRules = htlElAssureRuleDao.queryElAssureList(ex.getHotelcodeforchannel(), ex.getRateplancode(), checkInDate);
    	
    	//首晚星期
    	String weekOfDay=DateUtil.getWeekOfDate(checkInDate)+"";
    	
    	HtlElAssureRule htlElAssureRule = null;
    	//取出艺龙担保 ---取出第一条满足周设置的担保规则
    	if(htlElAssureRules!=null && htlElAssureRules.size()>0){
    	for(HtlElAssureRule rule:htlElAssureRules){
    		if(StringUtil.isValidStr(weekOfDay) && rule.getWeekset().indexOf(weekOfDay)!=-1){
    			htlElAssureRule = rule;
    			break;
    		}
    	}
    	}
		return htlElAssureRule;
	}
	
	
	public HtlElAssure calculateElongAssureRule(long priceTypeId,Date checkInDate,double priceNum,double firstNightSalePrice){

		HtlElAssure htlElAssure = new HtlElAssure();
		
		BigDecimal suretyAmount = new BigDecimal(0);
		
		HtlElAssureRule htlElAssureRule = this.queryElAssureRule(priceTypeId,checkInDate);
		if(null == htlElAssureRule){
			return null;
		}
		//根据担保类型计算担保金额
		long vouchMoneyType = htlElAssureRule.getVouchmoneytype();
		if(vouchMoneyType == 1){//担保首晚房费
			suretyAmount = suretyAmount.add(new BigDecimal(Double.valueOf(firstNightSalePrice)));
		}else if(vouchMoneyType == 2){//担保全额房费
			suretyAmount = suretyAmount.add(new BigDecimal(priceNum));
		}
		htlElAssure.setMoneyType(vouchMoneyType);
		htlElAssure.setAssureAmount(suretyAmount.doubleValue());
		
		//是否校验到店时间
		long isArrivetimeVouch = htlElAssureRule.getIsarrivetimevouch();
		//是否校验房间数量
		long isRoomCountVouch = htlElAssureRule.getIsroomcountvouch();
		
		if(0 == isArrivetimeVouch && 0 == isRoomCountVouch){
			htlElAssure.setAssureType(1); //无条件担保
			htlElAssure.setAssureDate(htlElAssureRule.getArrivestattime());
			htlElAssure.setAssureCondition("无条件担保");
		}else if(1 == isArrivetimeVouch && 1 == isRoomCountVouch){
			htlElAssure.setAssureType(5);  //既校验到店时间又校验房量
			htlElAssure.setAssureDate(htlElAssureRule.getArrivestattime());
			htlElAssure.setAssureRoomQuantity(htlElAssureRule.getRoomcount().intValue());
			htlElAssure.setAssureCondition("超时("+htlElAssureRule.getArrivestattime()+")超房量("+htlElAssureRule.getRoomcount().intValue()+")担保");
		}else if(1 == isArrivetimeVouch && 0 == isRoomCountVouch){
			htlElAssure.setAssureType(2);  //校验到店时间
			htlElAssure.setAssureDate(htlElAssureRule.getArrivestattime());
			htlElAssure.setAssureCondition("超时担保("+htlElAssureRule.getArrivestattime()+")");
		}else if(0 == isArrivetimeVouch && 1 == isRoomCountVouch){
			htlElAssure.setAssureType(3);  //校验房量
			htlElAssure.setAssureRoomQuantity(htlElAssureRule.getRoomcount().intValue());
			htlElAssure.setAssureDate(htlElAssureRule.getArrivestattime());
			htlElAssure.setAssureCondition("超房担保("+htlElAssureRule.getRoomcount().intValue()+")");
		}
		int changeRule = htlElAssureRule.getChangerule().intValue();
		String cancelAndModifySentence = "";
		if(2 == htlElAssureRule.getIschange()){ //不允许变更取消
			htlElAssure.setCancelAssureType("1");
			cancelAndModifySentence = "该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。";
		}else if(1 == htlElAssureRule.getIschange()){ //允许变更取消
			switch(changeRule){
				case 1:  //不允许变更取消
						htlElAssure.setCancelAssureType("1");
					    cancelAndModifySentence = "该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。";
				        break;
				case 2: { //允许变更/取消,需在XX日YY时之前通知
					htlElAssure.setCancelAssureType("2");
					Date dayNum = htlElAssureRule.getDaynum();
					
					String cancelDate = "";
					String timeNum = htlElAssureRule.getTimenum();
					if(StringUtil.isValidStr(timeNum)){
						String timeNums[] = timeNum.split(":");
						if(timeNums.length == 2 && "59".equals(timeNums[1])){
							cancelDate = DateUtil.datetimeToString(DateUtil.getDate(dayNum, 1));
						}else{
							cancelDate = DateUtil.datetimeToString(DateUtil.getDateByHour(dayNum, Integer.valueOf(timeNums[0])));
						}
					}
					htlElAssure.setAssureDateDay(cancelDate.split(" ")[0]);
					htlElAssure.setAssureSaveDate(cancelDate.split(" ")[1]);
					cancelAndModifySentence = "需取消或修改本次预订，请您务必于 "+cancelDate + " 前致电40066 40066提出变更，否则将按酒店规定比例扣取您的担保金额。"; 
					break;
				}
				case 3: { //允许变更/取消,需在最早到店时间之前几小时通知   注意：经查看，艺龙最早到店时间以14:00来计算
					htlElAssure.setCancelAssureType("3");
					int hourNum = htlElAssureRule.getHournum().intValue();
					Date arriveEarlyTime = DateUtil.getDateByHour(checkInDate, 14);
					
					String cancelDate = DateUtil.datetimeToString(DateUtil.getDateByHour(arriveEarlyTime,-hourNum));
					htlElAssure.setAssureDateDay(cancelDate.split(" ")[0]);
					htlElAssure.setAssureSaveDate(cancelDate.split(" ")[1]);
					cancelAndModifySentence = "需取消或修改本次预订，请您务必于 "+cancelDate + " 前致电40066 40066提出变更，否则将按酒店规定比例扣取您的担保金额。"; 
					break;
				}
				case 4:{ //允许变更/取消,需在到店日期的24点之前几小时通知
					htlElAssure.setCancelAssureType("4");
					int hourNum = htlElAssureRule.getHournum().intValue();
					String cancelDate = DateUtil.datetimeToString(DateUtil.getDateByHour(DateUtil.getDate(checkInDate, 1), -hourNum));
					htlElAssure.setAssureDateDay(cancelDate.split(" ")[0]);
					htlElAssure.setAssureSaveDate(cancelDate.split(" ")[1]);
					cancelAndModifySentence = "需取消或修改本次预订，请您务必于 "+ cancelDate + " 前致电40066 40066提出变更，否则将按酒店规定比例扣取您的担保金额。"; 
					break;
				}	
			}
		}
		
		htlElAssure.setCancelAndModifySentence(cancelAndModifySentence);
		logger.info(htlElAssure.toString());	
    	return htlElAssure;
	}


	public void setExMappingDao(ExMappingDao exMappingDao) {
		this.exMappingDao = exMappingDao;
	}


	public void setHtlElAssureRuleDao(HtlElAssureRuleDao htlElAssureRuleDao) {
		this.htlElAssureRuleDao = htlElAssureRuleDao;
	}
}
