package com.mango.hotel.ebooking.service.impl;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
import com.mango.hotel.ebooking.service.IUpdateHotelPrice;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.HtlBatchSalePrice;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.util.DateUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;
import com.mangocity.util.log.MyLog;

public class UpdateHotelPriceImpl implements IUpdateHotelPrice , Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final MyLog log = MyLog.getLogger(UpdateHotelPriceImpl.class);
	/**
     * 价格管理类
     */
	private IPriceManage priceManage;
	/**
     * 合同管理类
     */
    private ContractManage contractManage;
    
	public String batchUpdatePrice(
			HtlEbookingPriceRedressal priceRedressalBean, String[] infos) {
		log.info("............EBooking调用HBIZ..........更新价格开始...............contractManage:"+contractManage);
		// TODO Auto-generated method stub
		Long hotelId = Long.parseLong(priceRedressalBean.getHotelid());
		log.info(".....EBooking调用HBIZ.......hotelId:"+hotelId);
		
        HtlContract contract = contractManage.queryCurrentContractByHotelId(hotelId);
        log.info(".....EBooking调用HBIZ.......contract:"+contract);
        
        String currency = contract.getCurrency(); // 币种
        log.info(".....EBooking调用HBIZ...currency:"+currency);
        long contractId = contract.getID(); // 合同id
        // 插入数据到数据表中
        HtlBatchSalePrice batchSalePrice = new HtlBatchSalePrice();
        batchSalePrice.setContractId(contractId);
        batchSalePrice.setAdjustWeek(priceRedressalBean.getWeek());
        batchSalePrice.setPayMethod(priceRedressalBean.getPayMethod());
        // 佣金
        float commision = priceRedressalBean.getCommision();
        // 售价
        float salePrice = 0F;
        // 底价
        float basePrice = 0F;
        // 佣金率
        float commisionRate = 0F;
        DecimalFormat df = new DecimalFormat("0.00");
        /**
         * 面付 计算规则： 传入的佣金>=1 底价 = 销售价(对方酒店录入) - 佣金(本部员工录入) 传入的佣金<1 底价 = 销售价(对方酒店录入) * ( 1 -
         * 佣金率(本部员工录入) )
         */
        salePrice = priceRedressalBean.getSellingPrice();
        if (priceRedressalBean.getPayMethod().equals(PayMethod.PAY) && salePrice > 0) {
            
            // 小于1，则为佣金率
            if (1 > commision) {
                basePrice = Float.parseFloat(df.format(salePrice * (1 - commision)));
                commisionRate = commision;
                commision = salePrice - basePrice;
            }
            // 其他就是佣金
            else {
                basePrice = salePrice - commision;
                commisionRate = Float.parseFloat(df.format((salePrice - basePrice) / salePrice));
            }
        }
        /**
         * 预付 计算规则： 传入的佣金>=1 销售价 = 底价(对方酒店录入) + 佣金(本部员工录入) 传入的佣金<1 销售价 = 底价(对方酒店录入) / ( 1 -
         * 佣金率(本部员工录入) )
         */
        else {
            basePrice = priceRedressalBean.getBasePrice();
            // 小于1，则为佣金率
            if (1 > commision) {
                salePrice = Float.parseFloat(df.format(basePrice / (1 - commision)));
                commisionRate = commision;
                commision = salePrice - basePrice;
            }
            // 其他就是佣金
            else {
                salePrice = basePrice + commision;
                commisionRate = Float.parseFloat(df.format((salePrice - basePrice) / salePrice));
            }
        }
        batchSalePrice.setSalePrice(salePrice);
        batchSalePrice.setBasePrice(basePrice);
        batchSalePrice.setCommission(commision);
        batchSalePrice.setCommissionRate(commisionRate);
        batchSalePrice.setBeginDate(priceRedressalBean.getBeginDate());
        batchSalePrice.setEndDate(priceRedressalBean.getEndDate());
        batchSalePrice.setCalcFormula("0");
        batchSalePrice.setPreCalcFormula("0");
        batchSalePrice.setCanAddScope(contract.isIncServiceCharge());
        batchSalePrice.setRoomTypeId(priceRedressalBean.getRoomTypeID());
        if (0L < priceRedressalBean.getPriceTypeID())
            batchSalePrice.setChildRoomTypeId(priceRedressalBean.getPriceTypeID());
        batchSalePrice.setCreateTime(DateUtil.getSystemDate());
        batchSalePrice.setIncBreakfastNumber(String.valueOf(priceRedressalBean.getBreakfastNum()));
        // 简单公式计算 没有加早价格
        batchSalePrice.setIncBreakfastPrice(0);
        batchSalePrice.setIncBreakfastType(String.valueOf(priceRedressalBean.getBreakfastTypeID()));
        batchSalePrice.setModifyTime(new Date());
        batchSalePrice.setSalesroomPrice(0);
        batchSalePrice.setUpdateFlag("A");
        if (null != infos) {
            batchSalePrice.setCreateBy(infos[1] + "(ebooking)");
            batchSalePrice.setCreateById(infos[0]);
            batchSalePrice.setModifyBy(infos[1] + "(ebooking)");
            batchSalePrice.setModifyById(infos[0]);
        }
        // 服务费率
        batchSalePrice.setServiceCharge(contract.getServiceCharge());
        priceManage.saveBatchSalePrice(batchSalePrice);
        // 用存储过程替代下面的程序,更新或插入价格表
        String result = null;
         try {
        	 result = priceManage.saveOrUpdatePrice(hotelId, contractId, 
                QuotaType.GENERALQUOTA, currency,
                 priceRedressalBean.getWeek());
            
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
        return result;
	}
	
	/* start by wangjian -20121219 */
	public String batchUpdatePriceList(
			List<HtlEbookingPriceRedressal> priceRedressalBeanList, String[] infos) {
		log.info("EBooking调用HBIZ批量更新价格开始，操作人:"+infos[1]);
		
		List<HtlBatchSalePrice> htlBatchSalePriceList = new ArrayList<HtlBatchSalePrice>();
		
		Long hotelId = Long.parseLong(priceRedressalBeanList.get(0).getHotelid());
		log.info("EBooking调用HBIZ   hotelId:"+hotelId);
        HtlContract contract = contractManage.queryCurrentContractByHotelId(hotelId);
        String currency = contract.getCurrency(); // 币种
        log.info("EBooking调用HBIZ   currency:"+currency);
        long contractId = contract.getID(); // 合同id
        log.info("EBooking调用HBIZ   contractId:"+contractId);
        
		for(HtlEbookingPriceRedressal priceRedressalBean: priceRedressalBeanList){
	        // 插入数据到数据表中
	        HtlBatchSalePrice batchSalePrice = new HtlBatchSalePrice();
	        batchSalePrice.setContractId(contractId);
	        batchSalePrice.setAdjustWeek(priceRedressalBean.getWeek());
	        batchSalePrice.setPayMethod(priceRedressalBean.getPayMethod());
	        // 佣金
	        float commision = priceRedressalBean.getCommision();
	        // 售价
	        float salePrice = 0F;
	        // 底价
	        float basePrice = 0F;
	        // 佣金率
	        float commisionRate = 0F;
	        DecimalFormat df = new DecimalFormat("0.00");
	        /**
	         * 面付 计算规则： 传入的佣金>=1 底价 = 销售价(对方酒店录入) - 佣金(本部员工录入) 传入的佣金<1 底价 = 销售价(对方酒店录入) * ( 1 -
	         * 佣金率(本部员工录入) )
	         */
	        salePrice = priceRedressalBean.getSellingPrice();
	        if (priceRedressalBean.getPayMethod().equals(PayMethod.PAY) && salePrice > 0) {
	            
	            // 小于1，则为佣金率
	            if (1 > commision) {
	                basePrice = Float.parseFloat(df.format(salePrice * (1 - commision)));
	                commisionRate = commision;
	                commision = salePrice - basePrice;
	            }
	            // 其他就是佣金
	            else {
	                basePrice = salePrice - commision;
	                commisionRate = Float.parseFloat(df.format((salePrice - basePrice) / salePrice));
	            }
	        }
	        /**
	         * 预付 计算规则： 传入的佣金>=1 销售价 = 底价(对方酒店录入) + 佣金(本部员工录入) 传入的佣金<1 销售价 = 底价(对方酒店录入) / ( 1 -
	         * 佣金率(本部员工录入) )
	         */
	        else {
	            basePrice = priceRedressalBean.getBasePrice();
	            // 小于1，则为佣金率
	            if (1 > commision) {
	                salePrice = Float.parseFloat(df.format(basePrice / (1 - commision)));
	                commisionRate = commision;
	                commision = salePrice - basePrice;
	            }
	            // 其他就是佣金
	            else {
	                salePrice = basePrice + commision;
	                commisionRate = Float.parseFloat(df.format((salePrice - basePrice) / salePrice));
	            }
	        }
	        batchSalePrice.setSalePrice(salePrice);
	        batchSalePrice.setBasePrice(basePrice);
	        batchSalePrice.setCommission(commision);
	        batchSalePrice.setCommissionRate(commisionRate);
	        batchSalePrice.setBeginDate(priceRedressalBean.getBeginDate());
	        batchSalePrice.setEndDate(priceRedressalBean.getEndDate());
	        batchSalePrice.setCalcFormula("0");
	        batchSalePrice.setPreCalcFormula("0");
	        batchSalePrice.setCanAddScope(contract.isIncServiceCharge());
	        batchSalePrice.setRoomTypeId(priceRedressalBean.getRoomTypeID());
	        if (0L < priceRedressalBean.getPriceTypeID())
	            batchSalePrice.setChildRoomTypeId(priceRedressalBean.getPriceTypeID());
	        batchSalePrice.setCreateTime(DateUtil.getSystemDate());
	        batchSalePrice.setIncBreakfastNumber(String.valueOf(priceRedressalBean.getBreakfastNum()));
	        // 简单公式计算 没有加早价格
	        batchSalePrice.setIncBreakfastPrice(0);
	        batchSalePrice.setIncBreakfastType(String.valueOf(priceRedressalBean.getBreakfastTypeID()));
	        batchSalePrice.setModifyTime(new Date());
	        batchSalePrice.setSalesroomPrice(0);
	        batchSalePrice.setUpdateFlag("A");
	        if (null != infos) {
	            batchSalePrice.setCreateBy(infos[1] + "(ebooking)");
	            batchSalePrice.setCreateById(infos[0]);
	            batchSalePrice.setModifyBy(infos[1] + "(ebooking)");
	            batchSalePrice.setModifyById(infos[0]);
	        }
	        // 服务费率
	        batchSalePrice.setServiceCharge(contract.getServiceCharge());
	        htlBatchSalePriceList.add(batchSalePrice);
		}
        priceManage.saveBatchSalePriceList(htlBatchSalePriceList);
        // 用存储过程替代下面的程序,更新或插入价格表
        htlBatchSalePriceList = null;
        String result = null;
         try {
        	 result = priceManage.saveOrUpdatePrice(hotelId, contractId, 
                QuotaType.GENERALQUOTA, currency,"");
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
        return result;
	}
	/* end by wangjian -20121219 */

	public ContractManage getContractManage() {
		return contractManage;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public IPriceManage getPriceManage() {
		return priceManage;
	}

	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}

}
