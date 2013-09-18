package com.mangocity.hotel.base.manage.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.dao.IPriceDao;
import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.formula.IPriceCalculator;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.manage.assistant.AddPrice;
import com.mangocity.hotel.base.manage.assistant.InputPrice;
import com.mangocity.hotel.base.persistence.HtlAddscope;
import com.mangocity.hotel.base.persistence.HtlAddscopeHdr;
import com.mangocity.hotel.base.persistence.HtlBatchMtnPrice;
import com.mangocity.hotel.base.persistence.HtlBatchSalePrice;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHdlAddscope;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceLOG;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.ContinueDatecomponent;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;

/**
 * 合约价格维护类 1.批量录入合约价格 2.查询价格 3.批量加幅 4.对于加幅与调价的操作，进行日志记录
 * 
 * @author zhengxin 
 */
public class PriceManageImpl implements IPriceManage {

	private static final MyLog log = MyLog.getLogger(PriceManageImpl.class);
	
    private IPriceDao priceDao;
    
    private ISaleDao saleDao;

    private IContractDao contractDao;

    //价格计算器
    private IPriceCalculator priceCalculator;

    public List<InputPrice> calculatePrice(List<InputPrice> inputPrices, Long contractId) 
    {
    	HtlContract ct = contractDao.getContractById(contractId);
        for (InputPrice inputPrice: inputPrices) 
        { 
            //设置是否含服务费及服务费率
            if (null != ct) {
                inputPrice.setIncludeServiceCharge(ct.isIncServiceCharge());
                inputPrice.setServiceCharge(ct.getServiceCharge());
            }

            if (null != inputPrice.getFormulaId()) {
                priceCalculator.compute(inputPrice);
                // 对计算后的数据取小数点
                // ---佣金 底价
                BigDecimal b = new BigDecimal(inputPrice.getCalculatedCommission());
                inputPrice.setCalculatedCommission(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                BigDecimal c = new BigDecimal(inputPrice.getBasePrice());
                inputPrice.setBasePrice(c.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        
        return inputPrices;
    }

    public void updatePrice(Long hotelId, List<InputPrice> inputPrices, List<DateSegment> dateSegments, 
    		Long contractId, String quotaType, Integer[] week) {
        Map<Long, HtlRoom> roomMap = new HashMap<Long, HtlRoom>(1);
        List<HtlPrice> lstUpdatePrice = new ArrayList<HtlPrice>();

        // 循环多个时间段
        for (DateSegment dateSegment: dateSegments) {
            Date start = dateSegment.getStart();
            Date end = dateSegment.getEnd();
            int interval = DateUtil.getDay(start, end) + 1;

            // 循环一个时间段的每一天
            for (int j = 0; j < interval; j++) {
                Date roomDate = DateUtil.getDate(start, j);
                if (null != week && !DateUtil.isMatchWeek(roomDate, week))
                {
                    continue;
                }

                for (InputPrice inputPrice: inputPrices) {                    
                    //根据业务主键去寻找价格 
                    HtlPrice price = saleDao.findPriceBizKey(inputPrice.getHotelId(), 
                    		inputPrice.getRoomTypeId(), inputPrice.getChildRoomTypeId(), 
                    		quotaType, roomDate, inputPrice.getPayMethod());
                    if (null == price) {
                        Long roomTypeId = inputPrice.getRoomTypeId();
                        
                        //先从缓存拿这个房间，如果拿不到，就从数据库中拿，如果再拿不到，就新建一个房间。
                        HtlRoom room = (HtlRoom) roomMap.get(roomTypeId);
                        if (null == room) {
                            room = saleDao.createRoomIfNotExist(roomTypeId, hotelId, roomDate, contractId);
                            roomMap.put(roomTypeId, room);
                        }

                        price = new HtlPrice();
                        price.setRoom(room);
                        price.setAbleSaleDate(room.getAbleSaleDate());
                        price.setWeek(room.getWeek());
                    }
                    populatePrice(price, inputPrice);
                    lstUpdatePrice.add(price);
                }
                roomMap.clear();
            }
        }

        priceDao.batchSaveOrUpdatePrice(lstUpdatePrice);
    }
    
    private void populatePrice(HtlPrice price, InputPrice inputPrice) {

        price.setBasePrice(inputPrice.getBasePrice());
        price.setSalePrice(inputPrice.getSalePrice());
        price.setCommission(inputPrice.getCalculatedCommission()); //佣金  
        price.setCommissionRate(inputPrice.getCommissionRate()); //佣金率
        price.setAdvicePrice(inputPrice.getAdvicePrice());
        price.setIncBreakfastPrice(inputPrice.getBreakfastPrice());
        price.setPayMethod(inputPrice.getPayMethod());
        price.setIncBreakfastNumber(inputPrice.getBreakfastNum());
        price.setIncBreakfastType(inputPrice.getBreakfastType());
        price.setAddScope(inputPrice.getCalculatedCommission());
        price.setSalesroomPrice(inputPrice.getRoomPrice());
        price.setPayMethod(inputPrice.getPayMethod());
        price.setQuotaType(inputPrice.getQuotaType());
        price.setCanAddScope(inputPrice.isFixed());
        price.setRoomTypeId(inputPrice.getRoomTypeId());
        price.setCurrency(inputPrice.getCurrency());
        price.setHotelId(inputPrice.getHotelId());
        price.setChildRoomTypeId(null == inputPrice.getChildRoomTypeId() ? 0 : inputPrice
            .getChildRoomTypeId());
        price.setFormulaId(inputPrice.getFormulaId());
        price.setCreate_by(inputPrice.getCreate_by());
        price.setCreate_by_id(inputPrice.getCreate_by_id());
        price.setCreate_time(inputPrice.getCreate_time());
        price.setModify_by(inputPrice.getModify_by());
        price.setModify_by_id(inputPrice.getModify_by_id());
        price.setModify_time(inputPrice.getModify_time());

    }
    
    private void populatePriceLog(HtlPriceLOG price, HtlPrice inputPrice) {

        price.setPlanID(inputPrice.getPlanID());
        price.setCompagesDate(inputPrice.getCompagesDate());
        price.setReason(inputPrice.getReason());
        price.setCloseFlag(inputPrice.getCloseFlag());
        price.setWeek(inputPrice.getWeek());
        price.setRoomTypeName(inputPrice.getRoomTypeName());
        price.setAbleSaleDate(inputPrice.getAbleSaleDate());
        price.setChileRoomTypeName(inputPrice.getChileRoomTypeName());
        price.setBasePrice(inputPrice.getBasePrice());
        price.setSalePrice(inputPrice.getSalePrice());
        price.setCommission(inputPrice.getCommission()); //佣金
        price.setCommissionRate(inputPrice.getCommissionRate()); //佣金率
        price.setAdvicePrice(inputPrice.getAdvicePrice());
        price.setIncBreakfastPrice(inputPrice.getIncBreakfastPrice());
        price.setPayMethod(inputPrice.getPayMethod());
        price.setIncBreakfastNumber(inputPrice.getIncBreakfastNumber());
        price.setIncBreakfastType(inputPrice.getIncBreakfastType());
        price.setAddScope(inputPrice.getAddScope());
        price.setSalesroomPrice(inputPrice.getSalesroomPrice());
        price.setPayMethod(inputPrice.getPayMethod());
        price.setQuotaType(inputPrice.getQuotaType());
        price.setCanAddScope(inputPrice.isCanAddScope());
        price.setRoomTypeId(inputPrice.getRoomTypeId());
        price.setCurrency(inputPrice.getCurrency());
        price.setHotelId(inputPrice.getHotelId());
        price.setChildRoomTypeId(inputPrice.getChildRoomTypeId());
        price.setFormulaId(inputPrice.getFormulaId());
        price.setCreate_by(inputPrice.getCreate_by());
        price.setCreate_by_id(inputPrice.getCreate_by_id());
        price.setCreate_time(inputPrice.getCreate_time());
        price.setModify_by(inputPrice.getModify_by());
        price.setModify_by_id(inputPrice.getModify_by_id());
        price.setModify_time(inputPrice.getModify_time());
        price.setRoomId(inputPrice.getRoom().getID());

    }
    
    public void batchAddPrice(Long hotelId, List<AddPrice> addPrices, List<DateSegment> dateSegments, 
    		String quotaType, Integer[] week) 
    {
    	List<HtlPrice> prices = new ArrayList<HtlPrice>();
    	
        // 记录加幅日志
        for (DateSegment dateSegment: dateSegments) 
        {
            String weeks = "";
            if (null != week && 0 < week.length)
            {
            	for (Integer weekday: week)
            	{
            		weeks += "," + weekday;
            	}                    
            }
            
            // 写日志头信息
            HtlAddscopeHdr addScopeHdr = new HtlAddscopeHdr();
            addScopeHdr.setBeginDate(dateSegment.getStart());
            addScopeHdr.setEndDate(dateSegment.getEnd());
            addScopeHdr.setQuotaType(quotaType);
            addScopeHdr.setHotelId(hotelId);
            addScopeHdr.setWeek(weeks);

            // 日志明细信息
            List<HtlAddscope> lstAddScope = new ArrayList<HtlAddscope>(addPrices.size());            
            for (AddPrice addPrice: addPrices) 
            {
                HtlAddscope addScope = new HtlAddscope();
                addScope.setRoomTypeId(addPrice.getRoomTypeId());
                addScope.setPriceTypeId(addPrice.getChildRoomTypeId());
                addScope.setPreAddScope(addPrice.getPrePayAmount());
                addScope.setAddScope(addPrice.getPayAmount());
                addScope.setAddScopeHdr(addScopeHdr);
                addScope.setAddType(addPrice.getAddScopeType());
                lstAddScope.add(addScope);
            }
            addScopeHdr.setLstAddscope(lstAddScope);
            priceDao.saveOrUpdateAddscopeHdr(addScopeHdr);
        }

        for (AddPrice addPrice: addPrices) 
        {
            for (DateSegment dateSegment: dateSegments) {
                Date start = dateSegment.getStart();
                Date end = dateSegment.getEnd();

                int interval = DateUtil.getDay(start, end) + 1;
                for (int j = 0; j < interval; j++) {
                    Date roomDate = DateUtil.getDate(start, j);
                    if (null != week && !DateUtil.isMatchWeek(roomDate, week))
                    {
                        continue;
                    }

                    if (0 < addPrice.getPayAmount()) {
                        HtlPrice price = priceDao.findRoomPrice(addPrice.getRoomTypeId(), addPrice.getChildRoomTypeId(), roomDate,
                            PayMethod.PAY, quotaType);

                        if (null != price) {
                            if (price.isCanAddScope()) {
                                // 判断加幅方式是百分比还是金额
                                double payAmount;
                                if (addPrice.getAddScopeType().equals("2")) {
                                    payAmount = price.getBasePrice() * addPrice.getPayAmount();
                                } else {
                                    payAmount = addPrice.getPayAmount();
                                }
                                price.setAddScope(payAmount);
                                prices.add(price);
                            } else {
                                log.error("该面付价格不允许加幅，房型Id:" + addPrice.getRoomTypeId() + "," + addPrice.getChildRoomTypeId()
                                    + "日期" + roomDate);
                            }
                        } else {
                            log.error("没有找到价格，房型Id:" + addPrice.getRoomTypeId() + "," + addPrice.getChildRoomTypeId() + "日期"
                                + roomDate);
                        }
                    }

                    if (0 < addPrice.getPrePayAmount()) {
                        HtlPrice price = priceDao.findRoomPrice(addPrice.getRoomTypeId(), addPrice.getChildRoomTypeId(), roomDate,
                            PayMethod.PRE_PAY, quotaType);

                        if (null != price) {
                            if (price.isCanAddScope()) {
                                // 判断加幅方式是百分比还是金额
                                double payAmount;
                                if (addPrice.getAddScopeType().equals("2")) {
                                    payAmount = price.getBasePrice() * addPrice.getPrePayAmount();
                                } else {
                                    payAmount = addPrice.getPrePayAmount();
                                }
                                price.setAddScope(payAmount);
                                prices.add(price);
                            } else {
                                log.error("该预付价格不允许加幅，房型Id:" + addPrice.getRoomTypeId() + "," + addPrice.getChildRoomTypeId()
                                    + "日期" + roomDate);
                            }
                        } else{

                            log.error("没有找到价格，房型Id:" + addPrice.getRoomTypeId() + "," + addPrice.getChildRoomTypeId() + "日期"
                                + roomDate);
                        }
                    }
                }
            }
        }

        priceDao.batchSaveOrUpdatePrice(prices);
    }

    public void updatePrice(Long hotelId, List<InputPrice> inputPrices) 
    {
    	if (null != inputPrices && !inputPrices.isEmpty()) 
        {
        	List<HtlPrice> ls = new ArrayList<HtlPrice>(inputPrices.size());
            List<HtlPriceLOG> lsLog = new ArrayList<HtlPriceLOG>(inputPrices.size());
            
            for (InputPrice inputPrice: inputPrices) {
                HtlPriceLOG htlPriceLOG = new HtlPriceLOG();
                HtlPrice price = priceDao.qryHtlPrice(inputPrice.getPriceID().longValue());

                populatePrice(price, inputPrice);
                populatePriceLog(htlPriceLOG, price);
                ls.add(price);
                lsLog.add(htlPriceLOG);
            }

            priceDao.batchSaveOrUpdatePrice(ls);
            priceDao.batchSaveOrUpdatePriceLong(lsLog);
        }
    }
    
    public HtlPrice getHtlPrice(long htlPriceId) {
    	return priceDao.qryHtlPrice(htlPriceId);
    }
    
    public void updateHtlPrice(HtlPrice htlPrice) {
    	priceDao.updateHtlPrice(htlPrice);
    }

    public List<HtlPrice> queryPriceInAddPrice(Long hotelId, List<DateSegment> lstDate, String[] weeks,
        String[] priceTypeIds, String quotaType, String payMethodForQurey) {
        return priceDao.queryPriceInAddPrice(hotelId, lstDate, weeks, priceTypeIds, quotaType, payMethodForQurey);
    }

    public List<HtlRoom> batchQueryPrice(Long hotelId, Long[] roomType, List<DateSegment> dateSegments, 
    		Integer[] week)
    {
        return priceDao.batchQueryPrice(hotelId, roomType, dateSegments, week);
    }

    public Map<Long, String> getRoomtypeList(Long hotelId) {
        List<HtlRoomtype> listRoomType = priceDao.getRoomtypeList(hotelId);        
        Map<Long, String> mapRoomType = new HashMap<Long, String>(listRoomType.size());
        for (HtlRoomtype htlRoomtype: listRoomType) {
            mapRoomType.put(htlRoomtype.getID(), htlRoomtype.getRoomName());
        }
        
        return mapRoomType;
    }

    public Map<Long, String> getPriceTypeList(Long hotelId) {
        List<HtlPriceType> listPriceType = priceDao.getPriceTypeList(hotelId);        
        Map<Long, String> mapPriceType = new HashMap<Long, String>(listPriceType.size());
        for (HtlPriceType htlPriceType: listPriceType) {
            mapPriceType.put(htlPriceType.getID(), htlPriceType.getPriceType());
        }
        
        return mapPriceType;
    }
    
    public HtlPrice getPricInfoByFor(Date ableSaleDate, long hotelId, long roomTypeID, long priceTypeId, 
    		String payMethod) {
    	return priceDao.qryPricInfoByFor(ableSaleDate, hotelId, roomTypeID, priceTypeId, payMethod);
    }

    public void saveOrUpdatePrice(List<HtlBatchMtnPrice> batchMtnPriceList) {
        priceDao.batchSaveOrUpdateMtnPrice(batchMtnPriceList);
    }

    public void saveBatchSalePrice(HtlBatchSalePrice batchSalePrice) {
        priceDao.saveBatchSalePrice(batchSalePrice);
    }
    
    public void saveBatchSalePriceList(List<HtlBatchSalePrice> batchSalePriceList) {
        priceDao.saveBatchSalePriceList(batchSalePriceList);
    }

    public List<HtlBatchSalePrice> queryPrice(Long contractId, Date start, Date end, Long[] childRoomTypeId, 
    		String[] payMethod) 
    {
        return priceDao.queryPrice(contractId, start, end, childRoomTypeId, payMethod);
    }

    public HtlBatchSalePrice findPrice(Long priceId) 
    {
        return priceDao.loadBatchSalePrice(priceId.longValue());
    }

    public void updateBatchSalePrice(HtlBatchSalePrice batchSalePrice) {
        priceDao.updateBatchSalePrice(batchSalePrice);
    }

    public String saveOrUpdatePrice(long hotelId, long contractId, String quotaType, String currency, 
    		String week)
    {
         return priceDao.saveOrUpdatePrice(hotelId, contractId, quotaType, currency, week);
    }

    public void saveOrUpdatePrice() {
        priceDao.saveOrUpdatePrice();
    }

    public List getRoomPrices(long roomId) {
        return priceDao.getRoomPrices(roomId);
    }

    public List<HtlBatchSalePrice> getAlreadyLatestAndEarliest(long contractId) {
        return priceDao.getAlreadyLatAndEart(contractId);
    }

    public String saveOrUpdateAddScope(HtlHdlAddscope htlHdlAddscope){
        return priceDao.saveOrUpdateAddScope(htlHdlAddscope);
    }

    public List<HtlHdlAddscope> loadAddScope(Long hotelId) {
        return priceDao.loadAddScope(hotelId);
    }

    public List<String> checkIsAddScope(String newAllPriceId, Long hotelId, Long entityID, 
    		List<ContinueDatecomponent> lsContinueDatecomponent) 
    {
        return priceDao.checkIsAddScope(newAllPriceId, hotelId, entityID, lsContinueDatecomponent);
    }
    
    public List<HtlPrice> getHtlPriceBySaleDateRangePriceType(Date beginDate, Date endDate, long childRoomTypeId, 
            String payMethod, String quotaType) {
    	return priceDao.qryHtlPriceBySaleDateRangePriceType(beginDate, endDate, childRoomTypeId, payMethod, 
    			quotaType);
    }
    
    public List<HtlPrice> getHtlPriceByHotelIds(String hotelIds,
			Date startDate, Date endDate) {
    	return priceDao.queryHtlPriceByHotelIds(hotelIds, startDate, endDate);
	}
    
    public long getEleDayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD){
		return priceDao.qryEleDayPriceNum(hotelId, priceTypeIte, beginD, endD);		
	}
    
    public long getEleDayPayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD) {
    	return priceDao.qryEleDayPayPriceNum(hotelId, priceTypeIte, beginD, endD);
    }
    
    public String getPriceTypeNameById(long priceTypeID) {
    	return priceDao.qryPriceTypeNameById(priceTypeID);
    }

	public void setSaleDao(ISaleDao saleDao) {
		this.saleDao = saleDao;
	}

    public void setContractDao(IContractDao contractDao) {
        this.contractDao = contractDao;
    }

    public void setPriceCalculator(IPriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }

    public void setPriceDao(IPriceDao priceDao) {
        this.priceDao = priceDao;
    }
}
