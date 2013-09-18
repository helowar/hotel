/**
 * 
 */
package com.mangocity.hotel.base.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.mangocity.hotel.base.dao.HtlFavourableReturnDao;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.HtlFITAlias;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.assistant.CutDate;
import com.mangocity.hotel.order.persistence.FITCashItem;
import com.mangocity.hotel.order.persistence.FITOrderCash;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;

/**
 * 现金返还service实现
 * @author xiongxiaojun
 *
 */
public class HotelFavourableReturnServiceImpl extends DAOHibernateImpl implements IHotelFavourableReturnService {
	protected static final MyLog log = MyLog.getLogger(HotelFavourableReturnServiceImpl.class);
	
	private static final String pweeks = "1,2,3,4,5,6,7,";
	
	private static final Integer PAY_METHOD_2 = 2;
	
	private static final Integer PAY_METHOD_1 = 1;
	
	private HtlFavourableReturnDao htlFavourableReturnDao;
	
	private IPriceManage priceManage;
	
	private HotelRoomTypeService hotelRoomTypeService;
	
	/**
	 * 取出对应酒店所有的现金返还条款 add by xiaojun.xiong 2010-9-13
	 * @param hotelId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HtlFavourableReturn> queryAllFavourableReturn(long hotelId) {

		List<HtlFavourableReturn> lstFavReturn = new ArrayList<HtlFavourableReturn>();
		
		String sql = " select  a  from HtlFavourableReturn a where " +
		 " a.hotelId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		Object[] obj = new Object[] {hotelId};
		lstFavReturn=(List<HtlFavourableReturn>)super.query(sql, obj);
		
		return lstFavReturn;
	}
	
	/**
	 * 根据ID查询对应的现金返还规则 add by xiaojun.xiong 2010-9-13
	 * @param id
	 * @return
	 */
	public HtlFavourableReturn getFavReturnById(long id) {
		String sql = " select  a  from HtlFavourableReturn a   where a.id=? ";
		Object[] obj = new Object[] { id };
		HtlFavourableReturn favReturn = (HtlFavourableReturn) super.find(sql,obj);

		return favReturn;
	}

	/**
	 * 新增现金返还 add by xiaojun.xiong 2010-9-13
	 * 
	 * @param hotelId
	 *            酒店id
	 * @return roomtype 的 List;
	 */
	@SuppressWarnings("unchecked")
	public Long createFavourableReturn(HtlFavourableReturn htlFavourableReturn)
			 {
		List<HtlFavourableReturn> oldFavourableReturn = htlFavourableReturnDao.getFavourableReturnOrderListByPayMethod(htlFavourableReturn.getHotelId(), htlFavourableReturn.getPriceTypeId(), htlFavourableReturn.getPayMethod());	
		if(oldFavourableReturn.isEmpty()){
			htlFavourableReturnDao.saveHtlFavourableReturn(htlFavourableReturn);
		}else{
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			Boolean flag = false;
			if(oldFavourableReturn.size()>1){
				List<DateComponent> dateCopList = new ArrayList<DateComponent>();
				for(HtlFavourableReturn fReturn:oldFavourableReturn){
					dateCopList.add(initDateComponent(fReturn,true,true));
				}
				if(CutDate.compareConflictWithWeek(dateCopList)){//如果存在重叠，则要拆分
					for(int j=1;j<oldFavourableReturn.size();j++){
						HtlFavourableReturn subFavReturn = (HtlFavourableReturn)oldFavourableReturn.get(j);
						List<HtlFavourableReturn> subList = htlFavourableReturnDao.getSubFavReturnListByModifyTime(subFavReturn.getHotelId(),subFavReturn.getPriceTypeId(),subFavReturn.getModifyTime());
						this.favourableReturnUtil(subFavReturn, subList);
						flag = true;
					}
				}
				if(flag){
					oldFavourableReturn = htlFavourableReturnDao.getFavourableReturnOrderListByPayMethod(htlFavourableReturn.getHotelId(), htlFavourableReturn.getPriceTypeId(), htlFavourableReturn.getPayMethod());
				}
			}
			this.favourableReturnUtil(htlFavourableReturn, oldFavourableReturn);
		}
		return htlFavourableReturn.getId();
	}
	
	/**
	 * 返还现金拆分日期的辅助类  add by xiaowei.wang 2010-11-16
	 * @param htlFavourableReturn
	 * @param oldFavourableDecreaseList
	 */
	@SuppressWarnings("unchecked")
	private void favourableReturnUtil(HtlFavourableReturn htlFavourableReturn,List<HtlFavourableReturn> oldFavourableReturn){
		List<DateComponent> dateCops = new ArrayList<DateComponent>();
		Map resultMap = new HashMap(); 	
		for(HtlFavourableReturn favReturnII:oldFavourableReturn){
			dateCops.add(initDateComponent(favReturnII,false,true));			
		}
		resultMap = CutDate.cutWithWeek(initDateComponent(htlFavourableReturn,false,false),CutDate.sort(dateCops,htlFavourableReturn.getBeginDate(),htlFavourableReturn.getEndDate(),htlFavourableReturn.getWeek()));
		List<DateComponent> removeList = (List<DateComponent>) resultMap.get("remove");
		List<DateComponent> updateList = (List<DateComponent>) resultMap.get("update");
		removeHtlFavourableReturnByList(removeList);
		updateHtlFavourableReturnByList(updateList,oldFavourableReturn,htlFavourableReturn);
	}
	
	/**
	 * 初始化DateComponent
	 * 注意 重构前favReturnUtil 方法里面没有setModifyDate 
	 * @param htlFavourableReturn
	 * @return
	 */
	private DateComponent initDateComponent(HtlFavourableReturn htlFavourableReturn,Boolean flag,Boolean isSetId){
		DateComponent aComponent = new DateComponent();
		if(isSetId){
			aComponent.setId(htlFavourableReturn.getId());
		}
		aComponent.setBeginDate(htlFavourableReturn.getBeginDate());
		aComponent.setEndDate(htlFavourableReturn.getEndDate());
		aComponent.setWeeks(htlFavourableReturn.getWeek());
		if(flag){
			aComponent.setModifyDate(htlFavourableReturn.getModifyTime());
		}
		return aComponent;
	}
	
	/**
	 * 根据ID移除现金返还
	 * @param bblist
	 */
	public void removeHtlFavourableReturnByList(List<DateComponent> bblist){
		for(DateComponent bb:bblist){
			htlFavourableReturnDao.removeHtlFavourableReturnById(bb.getId());
		}
	}
	
	/**
	 * 
	 * @param updateList
	 * @param oldFavourableReturn
	 * @param htlFavourableReturn
	 */
	public void updateHtlFavourableReturnByList(List<DateComponent> updateList,List<HtlFavourableReturn> oldFavourableReturn,HtlFavourableReturn htlFavourableReturn){
		List<HtlFavourableReturn> results = new ArrayList<HtlFavourableReturn>();
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(HtlFavourableReturn favReturnObj:oldFavourableReturn){
			int doubleFlag = 0;
			for(DateComponent dateCop:updateList){
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(favReturnObj.getId())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlFavourableReturn newFavReturnObj= new HtlFavourableReturn();
							BeanUtils.copyProperties(favReturnObj,newFavReturnObj);
							newFavReturnObj.setId(null);
							newFavReturnObj.setBeginDate(dateCop.getBeginDate());
							newFavReturnObj.setEndDate(dateCop.getEndDate());							
							results.add(newFavReturnObj);
						}else{
							favReturnObj.setBeginDate(dateCop.getBeginDate());
							favReturnObj.setEndDate(dateCop.getEndDate());	
							results.add(favReturnObj);
						}
					}	
				}else if(nullFlag==false){
					if(htlFavourableReturn.getId()!= null){	//处理修改情况		
						HtlFavourableReturn favReturnUpt = new HtlFavourableReturn();
						BeanUtils.copyProperties(htlFavourableReturn,favReturnUpt);
						favReturnUpt.setId(null);
						results.add(favReturnUpt);
					}else{//处理新增情况
						results.add(htlFavourableReturn);
					}
					nullFlag = true;
				}
			}
		}
		htlFavourableReturnDao.saveHtlFavourableReturnList(results);
		
	}
    
    /**
	 * hotel 2.9.3
	 * 
	 * 修改一个现金返还的信息 add by xiaojun.xiong 2010-9-13
	 * @param favourableReturn
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public long modifyFavReturn(HtlFavourableReturn favourableReturn)
			//throws IllegalAccessException, InvocationTargetException 
			{
		if(null ==favourableReturn.getId()||0==favourableReturn.getId()){
			this.createFavourableReturn(favourableReturn);
		}else{
			List<HtlFavourableReturn> oldFavourableReturn =htlFavourableReturnDao.getFavourableReturnOrderListByPayMethod(favourableReturn.getHotelId(),favourableReturn.getPriceTypeId(),favourableReturn.getPayMethod());
			Boolean flag =false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldFavourableReturn.size()>1){
				List<DateComponent> dateCopList = new ArrayList<DateComponent>();
				for(HtlFavourableReturn aFavReturn:oldFavourableReturn){
					dateCopList.add(initDateComponent(aFavReturn,true,true));
				}
				if(CutDate.compareConflictWithWeek(dateCopList)){//如果存在重叠，则要拆分
					for(int j=1; j<oldFavourableReturn.size(); j++){
						HtlFavourableReturn subFavReturn = oldFavourableReturn.get(j);
						List<HtlFavourableReturn> subList =htlFavourableReturnDao.getSubFavReturnListByModifyTime(subFavReturn.getHotelId(),subFavReturn.getPriceTypeId(),subFavReturn.getModifyTime());
						this.favourableReturnUtil(subFavReturn,subList);
						flag = true;
					}
					if(flag){
						oldFavourableReturn = htlFavourableReturnDao.getFavourableReturnOrderListByPayMethod(favourableReturn.getHotelId(),favourableReturn.getPriceTypeId(),favourableReturn.getPayMethod());
					}
				}
			}
			///////////////////////////////////////////////////
			this.favourableReturnUtil(favourableReturn,oldFavourableReturn);
		}
		return 1;		
	}
	
	/**
	 * 删除现金返还 add by xiaojun.xiong 2010-9-13
	 * @param favourableReturn
	 * @return
	 */
	public long deleteFavReturnObj(HtlFavourableReturn favourableReturn){
		super.remove(favourableReturn);
		return favourableReturn.getId();
	}
	
	/**
	 * 批量设置返现规则  add by xiaojun.xiong 2010-9-13
	 * @param returnList
	 * @return
	 */
	public long batchSettingReturn(List<HtlFavourableReturn> returnList) {
		return 0;
	}
	
	/**
	 * 取出对应酒店价格类型现金返还条款 add by xiaojun.xiong j2010-9-13
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	@SuppressWarnings( { "unchecked", "unchecked" })
	public List<HtlFavourableReturn> queryFavourableReturnForPriceTypeId(
			long hotelId, long priceTypeId) {
		List<HtlFavourableReturn> lstFavReturn = new ArrayList<HtlFavourableReturn>();

		String sql = " select  a  from HtlFavourableReturn a   where "
				+ " a.hotelId=?  and a. priceTypeId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		Object[] obj = new Object[] { hotelId, priceTypeId };
		lstFavReturn = (List<HtlFavourableReturn>) super.query(sql, obj);

		return lstFavReturn;
		}

	/**
	 * 批量查询某时间段的现金返还信息，缓存到Map中 key是价格类型Id串
	 * 
	 * @param priceTypeIds 价格类型Id串
	 * @param inDate
	 * @param leaveDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<HtlFavourableReturn>> queryFavourableReturnForPriceTypeIds(
			String priceTypeIds, Date checkInDate, Date checkOutDate) {
		Map<String, List<HtlFavourableReturn>> favourableReturnMap = new HashMap<String, List<HtlFavourableReturn>>();
		List<HtlFavourableReturn> lstFavReturn = new ArrayList<HtlFavourableReturn>();

		String sql = " select  a  from HtlFavourableReturn a   where "
				+ " a.priceTypeId in(" + priceTypeIds + ") "
				+ " and endDate >= ? " + " and beginDate < ? "
				+ " and fun_date_week_judge(?,?,week) > 0";
		
		lstFavReturn = (List<HtlFavourableReturn>) super.query(sql, new Object[]{checkInDate,checkOutDate,checkInDate,checkOutDate});

		// 判断是否存在指定价格类型的现金返还规则
		if (lstFavReturn != null && !lstFavReturn.isEmpty()) {
			for (Iterator it = lstFavReturn.iterator(); it.hasNext();) {
				HtlFavourableReturn htlFavourableReturn = (HtlFavourableReturn) it
						.next();
				// 生成缓存Map中的key
				String mapKey = getFavourableReturnMapKey(String
						.valueOf(htlFavourableReturn.getPriceTypeId()),
						checkInDate, checkOutDate);
				favourableReturnMap.put(mapKey,
						new ArrayList<HtlFavourableReturn>());
			}
			for (Iterator it = lstFavReturn.iterator(); it.hasNext();) {
				HtlFavourableReturn htlFavourableDecrease = (HtlFavourableReturn) it
						.next();
				// 生成缓存Map中的key
				String mapKey = getFavourableReturnMapKey(String
						.valueOf(htlFavourableDecrease.getPriceTypeId()),
						checkInDate, checkOutDate);
				List<HtlFavourableReturn> decreaseList = favourableReturnMap
						.get(mapKey);
				decreaseList.add(htlFavourableDecrease);
				favourableReturnMap.put(mapKey, decreaseList);
			}
		}
		return favourableReturnMap;
	}

	/**
	 * 生成缓存Map中的key
	 * 
	 * @param childRoomTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	private String getFavourableReturnMapKey(String priceTypeId,
			Date checkInDate, Date checkOutDate) {
		StringBuffer mapKey = new StringBuffer();
		mapKey.append(priceTypeId);
		mapKey.append("_");
		mapKey.append(DateUtil.dateToStringNew(checkInDate));
		mapKey.append("_");
		mapKey.append(DateUtil.dateToStringNew(checkOutDate));
		return mapKey.toString();
	}

	/**
	 * 计算返现总金额
	 * 
	 * @param priceTypeId    价格类型   
	 * @param checkInDate    入住日期
	 * @param checkOutDate   离店日期
	 * @param night          入住夜晚
	 * @param roomQuantity   房间数 
	 * @param price          净价
	 * @param payType        支付方式
	 * @param currency       币种
	 * @param cashReturns    返现规则缓存Map
	 * @return
	 */
	public int calculateCashReturnAmount(String priceTypeId, Date checkInDate,
			Date checkOutDate,Date night, int roomQuantity, BigDecimal price, String payMethod,
			String currency, Map<String, List<HtlFavourableReturn>> cashReturns) {
		if(price.doubleValue() <= 0 || null == night) return 0;
		BigDecimal returnTotalAmount = new BigDecimal(0);
		String mapKey = getFavourableReturnMapKey(priceTypeId, checkInDate,
				checkOutDate);
		BigDecimal rate = new BigDecimal(getCurrency(currency));
		int pay = PayMethod.PAY.equalsIgnoreCase(payMethod) ? 1 : 2;
		//从缓存Map中取出返现规则
		List<HtlFavourableReturn> returnList = cashReturns.get(mapKey);
		if (null != returnList && !returnList.isEmpty()) {
			BigDecimal returnScale = getReturnScaleFromList(returnList, night, pay);
			if (returnScale.doubleValue() < 1) {
				returnTotalAmount = returnScale.multiply(price);
			} else {
				returnTotalAmount = returnScale;
			}
			returnTotalAmount = returnTotalAmount.multiply(rate).multiply(new BigDecimal(roomQuantity));
		}
		return returnTotalAmount.intValue();
	}
	
	/**
	 * 从返现规则列表获取满足条件的最新的规则
	 * @param list
	 * @param night
	 * @param payMothed
	 * @return
	 */
	private BigDecimal getReturnScaleFromList(List<HtlFavourableReturn> list,Date night,  int payMothed) {
		long index = 0;
		BigDecimal returnScale = new BigDecimal(0);
		
		String weekOfDate = String.valueOf(DateUtil.getWeekOfDate(night));
		for(HtlFavourableReturn favourableReturn : list){	
			boolean isBetween = favourableReturn.getWeek().indexOf(weekOfDate) >= 0 && DateUtil.between(night, favourableReturn.getBeginDate(), favourableReturn.getEndDate());
			if (payMothed == favourableReturn.getPayMethod() && isBetween) {
				if (favourableReturn.getId() > index) {
					returnScale = new BigDecimal(favourableReturn.getReturnScale());
					index = favourableReturn.getId();
				}
			}
		}
		return returnScale;
	}
	
	
	private double getCurrency(String currency){
		double rate = 1.0;
    	if (!CurrencyBean.RMB.equals(currency)) {
			String rateStr = CurrencyBean.rateMap.get(currency);
			if (StringUtil.isValidStr(rateStr)) {
				rate = Double.parseDouble(rateStr);
			}
		}
    	
    	return rate;
	}
	

	/**
	 * 计算某价格类型现金返还金额
	 * 
	 * @param priceTypeId  价格类型
	 * @param night        入住夜晚 
	 * @param payMethod    支付方式
	 * @param currency     币种
	 * @param roomQuantity 房间总数
	 * @param price        净价
	 * @return
	 */
	public int calculateCashReturnAmount(long priceTypeId, Date night, int payMethod, String currency, int roomQuantity, BigDecimal price) {
		log.info("单步进算返现金额：：：：：：计算参数：priceTypeId:"+priceTypeId+" 日期："+night+" 支付方式："+payMethod+" 支付币种："+currency+" 净价："+price);
		List<HtlFavourableReturn> lstFavReturn = new ArrayList<HtlFavourableReturn>();

		Date afterNight = DateUtil.getDate(night, 1);
		String sql = " select  a  from HtlFavourableReturn a   where "
				+ " a.priceTypeId = ?" + " and a.endDate >= ? "
				+ " and a.beginDate <= ? "
				+ " and fun_date_week_judge(?,?,a.week) > 0 "
				+ " and a.payMethod = ?";
		lstFavReturn = super.doquery(sql, new Object[] { priceTypeId, night, night, night, afterNight, payMethod }, false);
		
		BigDecimal rate = new BigDecimal(getCurrency(currency));
		BigDecimal returnTotalAmount = new BigDecimal(0);
		if (null != lstFavReturn && !lstFavReturn.isEmpty()) {
			BigDecimal returnScale = getReturnScaleFromList(lstFavReturn, night, payMethod);
			log.info("查询到最新返现规则为："+returnScale);
			// 返现金额累加,当返现规则大于0是，则相加， 否则相乘
			if (returnScale.doubleValue() < 1) {
				returnTotalAmount = returnScale.multiply(price);
			} else {
				returnTotalAmount = returnScale;
			}
			// 需要乘以汇率，RMB汇率为1.0
			returnTotalAmount = returnTotalAmount.multiply(rate).multiply(new BigDecimal(roomQuantity));
		}else{
			log.info("没有查询到返现规则");
		}
		return returnTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
	}
	
	/**
	 * 查询订单是否有返现
	 * @param orderCd
	 * @return
	 */
	public boolean isOrderHasCashReturn(String orderCd) {
		List<FITOrderCash> list;
		String sql = " select  a  from FITOrderCash a  where a.orderCd= ?";
	    Object[] params = new Object[1];
	    params[0] = orderCd;
	    list = super.query(sql, params);
	    if(null!=list && list.size()>0)
	    	return true;
		return false;
	}
	
	
	public Map<String,Double> fillCashItem(long priceTypeId,String payMethod,Date checkInDate,Date checkOutDate){
		List<HtlFavourableReturn> lstFavReturn = new ArrayList<HtlFavourableReturn>();
		Map<String,Double> scaleMap = new HashMap<String,Double>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int pay = PayMethod.PAY.equals(payMethod) ? 1 : 2;
		String sql = " select  a  from HtlFavourableReturn a   where "
				+ " a.priceTypeId = ?" + " and endDate >= ? "
				+ " and beginDate < ? "
				+ " and fun_date_week_judge(?,?,week) > 0 and a.payMethod = ?";
		
		lstFavReturn = super.query(sql, new Object[]{priceTypeId,checkInDate,checkOutDate,checkInDate,checkOutDate,pay});	
		
		if (null != lstFavReturn && !lstFavReturn.isEmpty()) {
			int days = DateUtil.getDay(checkInDate, checkOutDate);
			for (int i = 0; i < days; i++) {
				Date tempDate = DateUtil.getDate(checkInDate, i);
				BigDecimal returnScale = getReturnScaleFromList(lstFavReturn, tempDate, pay);
				scaleMap.put(format.format(tempDate), returnScale.doubleValue());
			}
		}
		return scaleMap;
	}
	
	/**
	 * 计算房型净价（仅适用现金返还）
	 * @param formulaId 计算公式
	 * @param commission 佣金
	 * @param commissionRate 佣金率
	 * @param salePrice 售价
	 * @return
	 */
	public BigDecimal calculateRoomTypePrice(String formulaId, BigDecimal commission,
			BigDecimal commissionRate, BigDecimal salePrice) {
		BigDecimal price = new BigDecimal(0);
		if(!StringUtil.isValidStr(formulaId)) return price;
		if("0" .equals(formulaId)|| "pricef".equals(formulaId.toLowerCase())){
	        price = salePrice;
        }else if(commission.doubleValue() > 0 && commissionRate.doubleValue() > 0){
        	price = commission.divide(commissionRate,1,RoundingMode.UP);
        }
		return price;
	}

	
	public void saveCashInformation(FITOrderCash orderCash,List<FITCashItem> cashItems){
		save(orderCash);
		saveOrUpdateAll(cashItems);
	}
	
	/**
	 * 清除返现信息
	 */
	public void clearCashInformation(FITOrderCash orderCash){
		String fSql="delete from FITOrderCash f where f.orderCd = ?";
		String fiSql="delete from FITCashItem fi where fi.orderCd = ?";
		super.remove(fSql, new Object[]{orderCash.getOrderCd()});
		super.remove(fiSql, new Object[]{orderCash.getOrderCd()});
	}

	/**
	 * 批量设置现金返还信息
	 * @param hotelIdArray
	 * @param favourableReturnBean
	 * @param onlineRoleUser
	 */
	public void batchCreateFavReturn(String hotelIds, HtlFavourableReturn favourableReturnBean, UserWrapper onlineRoleUser) {
		List<HtlRoomtype> roomTypeList = hotelRoomTypeService.getHtlRoomTypeListByHotelIds(hotelIds);
		for(HtlRoomtype roomType:roomTypeList){
			List<HtlPriceType> priceTypeList = roomType.getLstPriceType();
			for(HtlPriceType priceType:priceTypeList){
				//判断对应房型在指定时间段内的支付方式  countPrepayNum >0 表示存在预付的支付方式  countPayNum>0表示存在面付的支付方式
				long countPrepayNum = priceManage.getEleDayPriceNum(roomType.getHotelID(),priceType.getID(),favourableReturnBean.getBeginDate(),favourableReturnBean.getEndDate());
				long countPayNum = priceManage.getEleDayPayPriceNum(roomType.getHotelID(), priceType.getID(),favourableReturnBean.getBeginDate(),favourableReturnBean.getEndDate());
				if(countPrepayNum>0){
					convertFavourableReuurn(roomType.getHotelID(),favourableReturnBean,onlineRoleUser,PAY_METHOD_2,priceType.getID());
				}
				if(countPayNum>0){
					convertFavourableReuurn(roomType.getHotelID(),favourableReturnBean,onlineRoleUser,PAY_METHOD_1,priceType.getID());
				}
			}
		}
    	
	}

	public void batchFavourableReturn(List<HtlFavourableReturn> htlFavReturnLis, HtlFavourableReturn htlFavourableReturn, Long hotelId, UserWrapper onlineRoleUser)throws IllegalAccessException, InvocationTargetException {
		String[] priceTstr = htlFavourableReturn.getPriceTypeIdStr().split(",");
		for(int i=0;i<priceTstr.length;i++){
			for(HtlFavourableReturn htlFavReturnItems:htlFavReturnLis){
				HtlFavourableReturn favourableReturn = new HtlFavourableReturn();
				favourableReturn.setBeginDate(htlFavReturnItems.getBeginDate());
				favourableReturn.setEndDate(htlFavReturnItems.getEndDate());
				favourableReturn.setPriceTypeId(Long.parseLong(priceTstr[i]));
				favourableReturn.setReturnScale(htlFavReturnItems.getReturnScale());
				favourableReturn.setHotelId(hotelId);
				String priceName = priceManage.getPriceTypeNameById(Long.parseLong(priceTstr[i]));
				favourableReturn.setPriceTypeName(priceName);
				favourableReturn.setPayMethod(htlFavReturnItems.getPayMethod());
				//设置星期，如果星期为空，默认为1,2,3,4,5,6,7
				if(null == htlFavReturnItems.getWeek()){
					favourableReturn.setWeek(pweeks);
				}else{
					favourableReturn.setWeek(htlFavReturnItems.getWeek());
				}
				favourableReturn.setCreateBy(onlineRoleUser.getName());
				favourableReturn.setCreateById(onlineRoleUser.getLoginName());
				favourableReturn.setCreateTime(DateUtil.getSystemDate());
				favourableReturn.setModifyBy(onlineRoleUser.getName());
				favourableReturn.setModifyById(onlineRoleUser.getLoginName());
				favourableReturn.setModifyTime(DateUtil.getSystemDate());
				createFavourableReturn(favourableReturn);
			}
		}		
	}

	/**
	 * 根据ID删除现金返还信息
	 * @param favReturnID
	 */
	public void removeFavReturn(String favReturnID) {
		htlFavourableReturnDao.removeHtlFavourableReturnById(Long.parseLong(favReturnID));
	}

	public void updateFavourableReturn(HtlFavourableReturn favourableReturn, String favReturnID, UserWrapper onlineRoleUser) throws IllegalAccessException, InvocationTargetException{
		if(null==favReturnID)return;
		HtlFavourableReturn  oldFavReturn  =htlFavourableReturnDao.getHtlFavourableReturnById(Long.valueOf(favReturnID));
		if(null != oldFavReturn){
			HtlFavourableReturn  updFavReturn =new HtlFavourableReturn();
			updFavReturn.setBeginDate(favourableReturn.getBeginDate());
			updFavReturn.setEndDate(favourableReturn.getEndDate());
			updFavReturn.setReturnScale(favourableReturn.getReturnScale());
			updFavReturn.setWeek(favourableReturn.getWeek());
			updFavReturn.setId(oldFavReturn.getId());
			updFavReturn.setPriceTypeId(oldFavReturn.getPriceTypeId());
			updFavReturn.setHotelId(oldFavReturn.getHotelId());
			updFavReturn.setCreateBy(oldFavReturn.getCreateBy());
			updFavReturn.setCreateById(oldFavReturn.getCreateById());
			updFavReturn.setCreateTime(oldFavReturn.getCreateTime());
			updFavReturn.setPriceTypeName(oldFavReturn.getPriceTypeName());
			updFavReturn.setPayMethod(oldFavReturn.getPayMethod());
			updFavReturn.setModifyBy(onlineRoleUser.getName());
			updFavReturn.setModifyById(onlineRoleUser.getLoginName());
			updFavReturn.setModifyTime(DateUtil.getSystemDate());
			modifyFavReturn(updFavReturn);
		}
	}
	
	private void convertFavourableReuurn(Long htlId, HtlFavourableReturn favourableReturnBean,UserWrapper onlineRoleUser, Integer pay_method, Long priceTypeId) {
		HtlFavourableReturn favReturn = new HtlFavourableReturn();
		favReturn.setHotelId(htlId);
		favReturn.setBeginDate(favourableReturnBean.getBeginDate());
		favReturn.setEndDate(favourableReturnBean.getEndDate());
		favReturn.setWeek(favourableReturnBean.getWeek());
		favReturn.setReturnScale(favourableReturnBean.getReturnScale());
		favReturn.setPriceTypeId(priceTypeId);
		favReturn.setPriceTypeName(priceManage.getPriceTypeNameById(priceTypeId));
		favReturn.setPayMethod(pay_method);
		favReturn.setCreateBy(onlineRoleUser.getName());
		favReturn.setCreateById(onlineRoleUser.getLoginName());
		favReturn.setCreateTime(DateUtil.getSystemDate());
		favReturn.setModifyBy(onlineRoleUser.getName());
		favReturn.setModifyById(onlineRoleUser.getLoginName());
		favReturn.setModifyTime(DateUtil.getSystemDate());
		createFavourableReturn(favReturn);
	}
	
	@SuppressWarnings("unchecked")
	public List<HtlFITAlias> getFITAlias(String active) {
		return queryByNamedQuery("queryAllFITAliasId",new Object[]{active});
	}

	public void setHtlFavourableReturnDao(
			HtlFavourableReturnDao htlFavourableReturnDao) {
		this.htlFavourableReturnDao = htlFavourableReturnDao;
	}

	public HtlFavourableReturnDao getHtlFavourableReturnDao() {
		return htlFavourableReturnDao;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}
	
}
