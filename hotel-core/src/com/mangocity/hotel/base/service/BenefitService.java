package com.mangocity.hotel.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.FavourableDecreaseDAO;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.B2BAgentCommUtils;
import com.mangocity.hotel.base.persistence.HtlB2bComminfo;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;

/**
 * 优惠立减服务实现 2009-10-15 
 * @author chenjiajie
 *
 */
public class BenefitService implements IBenefitService {

	private FavourableDecreaseDAO favourableDecreaseDAO;
	
	private IPriceManage priceManage;
	
	private CommissionService commissionService;

    /**
     * 资源接口
     */
    private ResourceManager resourceManager;
	
	/**
	 * 查询某时间段的优惠立减信息
	 * @param childRoomTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public List<HtlFavourableDecrease> queryBenefitByDate(String childRoomTypeId, Date checkInDate,
			Date checkOutDate){		
		return favourableDecreaseDAO.qryBenefitByPriceTypeAndDate(childRoomTypeId, checkInDate, checkOutDate);
	}
	
	/**
	 * 计算某价格类型在入住时间段的优惠总金额 (参与币种换算)
	 * @param childRoomTypeId 价格类型id
	 * @param checkInDate 入住日期
	 * @param checkOutDate 离店日期
	 * @param roomQuantity 预订房间数
	 * @param currency 币种
	 * @return 计算得出的优惠总金额
	 */
	@SuppressWarnings("unchecked")
	public int calculateBenefitAmount(String childRoomTypeId, Date checkInDate,
			Date checkOutDate, int roomQuantity,String currency) {
		int totalAmount = 0;
		
		//查询某时间段的优惠立减信息
		List<HtlFavourableDecrease> list = queryBenefitByDate(childRoomTypeId,checkInDate,checkOutDate);
		Map<String, String> rateMap = CurrencyBean.rateMap;
		//入住离店相差天数
		int days = DateUtil.getDay(checkInDate, checkOutDate);
		if(null != list && !list.isEmpty()){
			double rate = 1.0;
			// 如果非人民币，立减金额需要算上汇率
			if(!CurrencyBean.RMB.equals(currency)){
				String rateStr = rateMap.get(currency);
				if(StringUtil.isValidStr(rateStr)){
					rate = Double.parseDouble(rateStr);
				}
			}
			
			for (HtlFavourableDecrease htlFavourableDecrease : list) {
				String week = htlFavourableDecrease.getWeek();
				for (int i = 0; i < days; i++) {
					Date tempDate = DateUtil.getDate(checkInDate, i);
					//从入住日期到离店日期开始遍历，检查日期是否在规则的开始结束日期区间
					boolean isBetween = DateUtil.checkDateBetween(tempDate,htlFavourableDecrease.getBeginDate(),htlFavourableDecrease.getEndDate(),week);
					if(isBetween){
						//优惠金额累加，需要乘以汇率，RMB汇率为1.0
						totalAmount += (int)htlFavourableDecrease.getDecreasePrice();
					}
				}
			}

			totalAmount = (int) (totalAmount * rate);
			totalAmount = totalAmount * roomQuantity;
		}
		return totalAmount;
	}

	/**
	 * 计算某价格类型在入住时间段的优惠总金额 (不参与币种换算)
	 * @param childRoomTypeId 价格类型id
	 * @param checkInDate 入住日期
	 * @param checkOutDate 离店日期
	 * @param roomQuantity 预订房间数
	 * @return 计算得出的优惠总金额
	 */
	public int calculateBenefitAmount(String childRoomTypeId, Date checkInDate,
			Date checkOutDate, int roomQuantity) {
		int totalAmount = 0;
		
		//查询某时间段的优惠立减信息
		List<HtlFavourableDecrease> list = queryBenefitByDate(childRoomTypeId,checkInDate,checkOutDate);
		
		//入住离店相差天数
		int days = DateUtil.getDay(checkInDate, checkOutDate);
		if(null != list && !list.isEmpty()){			
			for (HtlFavourableDecrease htlFavourableDecrease : list) {
				String week = htlFavourableDecrease.getWeek();
				for (int i = 0; i < days; i++) {
					Date tempDate = DateUtil.getDate(checkInDate, i);
					//从入住日期到离店日期开始遍历，检查日期是否在规则的开始结束日期区间
					boolean isBetween = DateUtil.checkDateBetween(tempDate,htlFavourableDecrease.getBeginDate(),htlFavourableDecrease.getEndDate(),week);
					if(isBetween){
						//优惠金额累加，需要乘以汇率，RMB汇率为1.0
						totalAmount += (int)htlFavourableDecrease.getDecreasePrice();
					}
				}
			}
			
			totalAmount = totalAmount * roomQuantity;
		}
		return totalAmount;
	}
	
	

	/**
	 * 批量查询某时间段的优惠立减信息，缓存到Map中 key是价格类型id
	 * @param childRoomTypeIds 批量价格类型ID，用,分隔
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public Map<String,List<HtlFavourableDecrease>> queryBatchBenefitByDate(
			String childRoomTypeIds, Date checkInDate, Date checkOutDate) {
		Map<String,List<HtlFavourableDecrease>> decreaseMap = new HashMap<String, List<HtlFavourableDecrease>>();
		List<HtlFavourableDecrease> list = favourableDecreaseDAO.qryBenefitByMultiPriceTypeAndDate(childRoomTypeIds.split(","), 
				checkInDate, checkOutDate);
		
		if(!list.isEmpty()){
			for (HtlFavourableDecrease htlFavourableDecrease : list) {
				//生成缓存Map中的key 
				String mapKey = genDecreaseMapKey(String.valueOf(htlFavourableDecrease.getPriceTypeId()),checkInDate,checkOutDate);
				decreaseMap.put(mapKey, new ArrayList<HtlFavourableDecrease>());
			}
			for (HtlFavourableDecrease htlFavourableDecrease : list) {
				//生成缓存Map中的key 
				String mapKey = genDecreaseMapKey(String.valueOf(htlFavourableDecrease.getPriceTypeId()),checkInDate,checkOutDate);
				List<HtlFavourableDecrease> decreaseList = decreaseMap.get(mapKey);
				decreaseList.add(htlFavourableDecrease);
				decreaseMap.put(mapKey, decreaseList);
			}
		}
		
		return decreaseMap;
	}
	
	/**
	 * 计算某价格类型在入住时间段的优惠总金额 (参与币种换算)
	 * @param childRoomTypeId 价格类型id
	 * @param checkInDate 入住日期
	 * @param checkOutDate 离店日期
	 * @param roomQuantity 预订房间数
	 * @param currency 币种
	 * @param decreaseMap 缓存的优惠立减结果集
	 * @return 计算得出的优惠总金额
	 */
	public int calculateBenefitAmount(String childRoomTypeId,Date checkInDate,Date checkOutDate,int roomQuantity,String currency,Map<String,List<HtlFavourableDecrease>> decreaseMap){
		int totalAmount = 0;
		
		//从缓存中取出某价格类型的优惠立减信息
		String mapKey = genDecreaseMapKey(childRoomTypeId,checkInDate,checkOutDate);
		List<HtlFavourableDecrease> list = decreaseMap.get(mapKey);
		Map<String, String> rateMap = CurrencyBean.rateMap;
		//入住离店相差天数
		int days = DateUtil.getDay(checkInDate, checkOutDate);
		if(null != list && !list.isEmpty()){
			double rate = 1.0;
			// 如果非人民币，立减金额需要算上汇率
			if(!CurrencyBean.RMB.equals(currency)){
				String rateStr = rateMap.get(currency);
				if(StringUtil.isValidStr(rateStr)){
					rate = Double.parseDouble(rateStr);
				}
			}
			
			for (HtlFavourableDecrease htlFavourableDecrease : list) {
				String week = htlFavourableDecrease.getWeek();
				for (int i = 0; i < days; i++) {
					Date tempDate = DateUtil.getDate(checkInDate, i);
					//从入住日期到离店日期开始遍历，检查日期是否在规则的开始结束日期区间
					boolean isBetween = DateUtil.checkDateBetween(tempDate,htlFavourableDecrease.getBeginDate(),htlFavourableDecrease.getEndDate(),week);
					if(isBetween){
						//优惠金额累加，需要乘以汇率，RMB汇率为1.0
						totalAmount += (int)htlFavourableDecrease.getDecreasePrice();
					}
				}
			}

			totalAmount = (int) (totalAmount * rate);
			totalAmount = totalAmount * roomQuantity;
		}
		return totalAmount;
	}
	
	/**
	 * 生成缓存Map中的key
	 * @param childRoomTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	private String genDecreaseMapKey(String childRoomTypeId,Date checkInDate,Date checkOutDate){
		StringBuffer mapKey = new StringBuffer();
		mapKey.append(childRoomTypeId);
		mapKey.append("_");
		mapKey.append(DateUtil.dateToStringNew(checkInDate));
		mapKey.append("_");
		mapKey.append(DateUtil.dateToStringNew(checkOutDate));
		return mapKey.toString();
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setFavourableDecreaseDAO(FavourableDecreaseDAO favourableDecreaseDAO) {
		this.favourableDecreaseDAO = favourableDecreaseDAO;
	}

	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}



}
