package com.mangocity.webnew.service.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlElAssureRuleDao;
import com.mangocity.hotel.base.persistence.HtlElAssureRule;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.webnew.persistence.ElongAssureResult;
import com.mangocity.webnew.service.CheckElongAssureService;

public class CheckElongAssureServiceImpl implements CheckElongAssureService {
	private HtlElAssureRuleDao htlElAssureRuleDao;
	
	/**
	 * 根据输入参数检查是否需要担保，
	 * 返回ElongAssureResult艺龙判断担保结果
	 * @param hotelId
	 * @param priceTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @param roomQty
	 * @param arriveTime
	 * @return ElongAssureResult
	 */
	public ElongAssureResult checkIsAssure(long hotelId, long priceTypeId,
			Date checkInDate, Date checkOutDate, int roomQty, String arriveTime) {
		List<HtlElAssureRule>  rulelist = htlElAssureRuleDao.queryElAssureMap(hotelId,priceTypeId,checkInDate, checkOutDate);
		
		HtlElAssureRule type1Rule =  null;//保存到店担保条款
		HtlElAssureRule type2Rule =  null;//保存在店担保条款
		//1.check是否有到店担保
		for(HtlElAssureRule rule:rulelist){
			if(rule.getDatetype()==1
					&& DateUtil.between(checkInDate, rule.getStartdate(), rule.getEnddate())
					&& isInWeekSet(checkInDate,rule.getWeekset())){
				type1Rule = rule;
				break;
			}
		}
		//2.check是否有在店担保
		List<Date> days = DateUtil.getDates(checkInDate, DateUtil.getDate(checkOutDate, -1));
		label1 : for(Date d:days){
			for(HtlElAssureRule rule:rulelist){
				if(rule.getDatetype()==2 
						&& DateUtil.between(d, rule.getStartdate(), rule.getEnddate())
						&& isInWeekSet(d,rule.getWeekset())){
					type2Rule = rule;
					break label1;
				}
			}
		}
		//优先选择在店担保条款
		HtlElAssureRule rule = type2Rule==null ? (type1Rule==null ? null : type1Rule) : type2Rule;
		
		if(rule!=null){
			Date modifyBeforeDate = null;
			String[] modifyStr=this.getModifyStr(rule,checkInDate);
			if(!"".equals(modifyStr[1])){
				modifyBeforeDate = DateUtil.getDateTimeFromTimeStr(modifyStr[1]+":00");
			}
			//是否有超时条款、有超房条款、根据arrivetime判断是否真实超时、根据roomqty判断是否真实超房
			boolean overTime=false,overRoomQty=false,overTimeReal=false,overRoomQtyReal=false;
			if(rule.getIsarrivetimevouch()==0 && rule.getIsroomcountvouch()==0){//无条件
				return new ElongAssureResult(true,1,"该时段内入住该房型，需按酒店要求提供信用卡担保。", modifyStr[0], rule.getVouchmoneytype().intValue(), 0, 
						0, 0, modifyBeforeDate);
			}else{
				if(rule.getIsarrivetimevouch()==1 ){//超时
					overTime = true;
					if(isArriveTimeBetween(arriveTime,rule.getArrivestattime(),rule.getArriveendtime(),rule.getIstomorrow())){
						overTimeReal = true;
					}
				}
				if(rule.getIsroomcountvouch()==1 ){//超房
					overRoomQty = true;
					if(rule.getRoomcount()<=roomQty){
						overRoomQtyReal = true;
					}
				}
				if(overTime||overRoomQty){
					ElongAssureResult assureResult= new ElongAssureResult();
					assureResult.setAssureMoneyType(rule.getVouchmoneytype().intValue());
					String condStr="";
					if(overTime){
						assureResult.setAssureType(3);
						assureResult.setOverTimeHour(Integer.parseInt(rule.getArrivestattime().split(":")[0]));
						assureResult.setOverTimeMin(Integer.parseInt(rule.getArrivestattime().split(":")[1]));
						condStr="入住时间超过酒店规定最晚时间"+rule.getArrivestattime();
					}
					if(overRoomQty){
						assureResult.setAssureType(2);
						if(condStr.length()>1){
							condStr+="或";
							assureResult.setAssureType(4);
						}
						condStr+="预订该房型超过"+(rule.getRoomcount()-1)+"间房以上";
						assureResult.setOverQtyNum(rule.getRoomcount().intValue()-1);
					}
					
					condStr+="需要担保。";
					
					assureResult.setConditionStr(condStr);
					assureResult.setNeedAssure(overTimeReal||overRoomQtyReal);
					assureResult.setModifyStr(modifyStr[0]);
					assureResult.setModifyBeforeDate(modifyBeforeDate);
					return assureResult;
				}
			}
		}
		return new ElongAssureResult();
	}
	
	/**
     * 判断日期的星期 是否在weekset里
     * @param checkInDate
     * @param weekset
     * @return
     */
    private static boolean isInWeekSet(Date checkInDate, String weekset) {
    	if(weekset==null||"".equals(weekset)){return false;}
    	int i = DateUtil.getWeekOfDate(checkInDate);
    	return weekset.indexOf(i+"")>-1 ? true:false;
    	
	}
    
	/**
	 * 判断到店时间18:50是否在俩时间start,end中间，不包含start边界
	 * @param arriveTime
	 * @param arrivestattime
	 * @param arriveendtime
	 * @param istomorrow 0当天/1次日
	 * @return
	 */
	private boolean isArriveTimeBetween(String arriveTime, String arrivestattime,
			String arriveendtime, Long istomorrow) {
		boolean isBetween = false;
		int[] arrive = new int[]{Integer.parseInt(arriveTime.split(":")[0]),Integer.parseInt(arriveTime.split(":")[1])};
		int[] start = new int[]{Integer.parseInt(arrivestattime.split(":")[0]),Integer.parseInt(arrivestattime.split(":")[1])};
		int[] end = new int[]{Integer.parseInt(arriveendtime.split(":")[0]),Integer.parseInt(arriveendtime.split(":")[1])};
		if(istomorrow==1){
			end[0] = 24+end[0];
		}
		if(arrive[0] > start[0] || (arrive[0] == start[0] && arrive[1] > start[1])){
			isBetween = true;
		}
		return isBetween;
	}

	/**
	 * 根据rule获取修改取消规则str
	 * @param htlElAssureRule
	 * @param checkInDate
	 * @return
	 */
	private String[] getModifyStr(HtlElAssureRule htlElAssureRule,Date checkInDate) {
		int changeRule = htlElAssureRule.getChangerule().intValue();
		String cancelAndModifySentence = "";
		String modifyBeforeTime="";
		if(2 == htlElAssureRule.getIschange()){ //不允许变更取消
			cancelAndModifySentence = "该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。";
		}else if(1 == htlElAssureRule.getIschange()){ //允许变更取消
			switch(changeRule){
				case 1:  //不允许变更取消
					    cancelAndModifySentence = "该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。";
				        break;
				case 2: { //允许变更/取消,需在XX日YY时之前通知
					Date dayNum = htlElAssureRule.getDaynum();
					String timeNum = htlElAssureRule.getTimenum();
					if(StringUtil.isValidStr(timeNum)){
						String timeNums[] = timeNum.split(":");
						if(timeNums.length == 2 && "59".equals(timeNums[1])){
							modifyBeforeTime = DateUtil.datetimeToString(DateUtil.getDate(dayNum, 1));
						}else{
							modifyBeforeTime = DateUtil.datetimeToString(DateUtil.getDateByHour(dayNum, Integer.valueOf(timeNums[0])));
						}
					}
					cancelAndModifySentence = "需取消或修改本次预订，请您务必于 "+modifyBeforeTime + " 前致电40066 40066提出变更，否则将按酒店规定比例扣取您的担保金额。"; 
					
					break;
				}
				case 3: { //允许变更/取消,需在最早到店时间之前几小时通知   注意：经查看，艺龙最早到店时间以14:00来计算
					int hourNum = htlElAssureRule.getHournum().intValue();
					Date arriveEarlyTime = DateUtil.getDateByHour(checkInDate, 14);
					
					modifyBeforeTime = DateUtil.datetimeToString(DateUtil.getDateByHour(arriveEarlyTime,-hourNum));
					cancelAndModifySentence = "需取消或修改本次预订，请您务必于 "+modifyBeforeTime + " 前致电40066 40066提出变更，否则将按酒店规定比例扣取您的担保金额。"; 
					break;
				}
				case 4:{ //允许变更/取消,需在到店日期的24点之前几小时通知
					int hourNum = htlElAssureRule.getHournum().intValue();
					modifyBeforeTime = DateUtil.datetimeToString(DateUtil.getDateByHour(DateUtil.getDate(checkInDate, 1), -hourNum));
					cancelAndModifySentence = "需取消或修改本次预订，请您务必于 "+ modifyBeforeTime + " 前致电40066 40066提出变更，否则将按酒店规定比例扣取您的担保金额。"; 
					break;
				}	
			}
		}
		return new String[]{cancelAndModifySentence,modifyBeforeTime};
	}
	public HtlElAssureRuleDao getHtlElAssureRuleDao() {
		return htlElAssureRuleDao;
	}
	public void setHtlElAssureRuleDao(HtlElAssureRuleDao htlElAssureRuleDao) {
		this.htlElAssureRuleDao = htlElAssureRuleDao;
	}
	
}
