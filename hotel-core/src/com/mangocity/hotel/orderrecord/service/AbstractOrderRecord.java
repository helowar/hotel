package com.mangocity.hotel.orderrecord.service;

import java.util.Date;

import org.springframework.web.context.WebApplicationContext;

import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

public abstract class AbstractOrderRecord{
	private OrderRecordService orderRecordService;
	private OrderRecord orderRecord;
	private HotelOrderFromBean hotelOrderFromBean;
	private static final MyLog log = MyLog.getLogger(AbstractOrderRecord.class);
	public AbstractOrderRecord(){} 
	
	public  AbstractOrderRecord(OrderRecord orderRecord,HotelOrderFromBean hotelOrderFromBean){
		this.orderRecord=orderRecord;
		this.hotelOrderFromBean=hotelOrderFromBean;
	}
	
	/**
	 * 封装日志记录信息
	 * @param orderRecord
	 * @param hotelOrderFromBean
	 * @return
	 */
	public abstract void combineOrderRecord() throws Exception;
	
	protected void init(){

		//公共
		orderRecord.setArriveTime(hotelOrderFromBean.getArrivalTime());
		orderRecord.setBedType(hotelOrderFromBean.getBedType());
		orderRecord.setCheckinDate(hotelOrderFromBean.getCheckinDate());
		orderRecord.setCheckoutDate(hotelOrderFromBean.getCheckoutDate());
		orderRecord.setHotelId(hotelOrderFromBean.getHotelId());
		orderRecord.setLastArriveTime(hotelOrderFromBean.getLatestArrivalTime());
		orderRecord.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());
		orderRecord.setPriceTypeId(hotelOrderFromBean.getChildRoomTypeId());
		orderRecord.setRealPaymoney(hotelOrderFromBean.getActuralAmount());
		orderRecord.setPointMoney(hotelOrderFromBean.getUlmPoint());
		orderRecord.setUlmMoney(hotelOrderFromBean.getUlmCoupon());
		orderRecord.setPayMethod(hotelOrderFromBean.getPayMethod());
		orderRecord.setContractCurrency(hotelOrderFromBean.getCurrency());
		orderRecord.setReturnCashMoney(hotelOrderFromBean.getReturnAmount());
		orderRecord.setRoomQuantity(hotelOrderFromBean.getRoomQuantity());
		orderRecord.setSupplierChannel(hotelOrderFromBean.getRoomChannel()+"");
		orderRecord.setIsNeedAssue(hotelOrderFromBean.isNeedAssure() ? 1 : 0);
		orderRecord.setCreateDate(new Date());
		
		//设置默认值
		if (null != hotelOrderFromBean.getSource()
				&& !"".equals(hotelOrderFromBean.getSource())) {
			orderRecord.setSource(hotelOrderFromBean.getSource());
		} else {
			orderRecord.setSource("net");
		}
		orderRecord.setCreatorName("System");
		orderRecord.setCreatorId("System");
		
	}

	/**
	 *  新增日志记录模板
	 * @param orderRecord
	 * @param hotelOrderFromBean
	 * @throws Exception 
	 */
   public void createOrderRecordTemplete(WebApplicationContext context) throws Exception{
	   try {
		    init();
			combineOrderRecord();
			orderRecordService = (OrderRecordService) context.getBean("orderRecordService");
			if (null != orderRecord) {
				if (null == orderRecord.getActionId() || 0L == orderRecord.getActionId().longValue()) {

					orderRecord.setActionId(queryActionId());
				}
				else if((null == orderRecord.getActionId()||0L == orderRecord.getActionId().longValue())
						&& orderRecord.getOrorderCd()!=null){
					Long oldActionId=orderRecordService.queryActionIdByOrderCd(orderRecord.getOrorderCd());
					orderRecord.setActionId(oldActionId);
				}
				 createOrderRecord(orderRecord);

			}
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}
   }
   
   /**
    * 添加记录到数据库中
    * @param orderRecord
    * @return
 * @throws Exception 
    */
	private void createOrderRecord(OrderRecord orderRecord) throws Exception{
		orderRecordService.addOrderRecord(orderRecord);
	}
	
   /**
    * 查询actionId
    * @return
 * @throws Exception 
    */
	private Long queryActionId() throws Exception{
		return orderRecordService.getActionId();
	}

	
}
