package com.mangocity.hagtb2b.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hagtb2b.dao.HtlB2bDao;
import com.mangocity.hagtb2b.persistence.HtlB2bIncrease;
import com.mangocity.hagtb2b.service.IHtlB2bService;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.hweb.persistence.QueryHotelForWebRoomType;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.bean.CurrencyBean;

public class HtlB2bService implements IHtlB2bService {
	
	private HtlB2bDao htlB2bDao;
	
	/**
	 * b2b取消优惠立减功能
	 * @param hotelPageForWebBean
	 * @return
	 */
	public HotelPageForWebBean setBenefitFlagAllFlase(HotelPageForWebBean hotelPageForWebBean){
		List<QueryHotelForWebResult> resultList = hotelPageForWebBean.getList();
		for(QueryHotelForWebResult queryHotelResult:resultList){
			if(queryHotelResult.getHasBenefit()>0){
				queryHotelResult.setHasBenefit(0);
			}
			List<QueryHotelForWebRoomType> roomTypeList = queryHotelResult.getRoomTypes();
			for(QueryHotelForWebRoomType  roomTypes:roomTypeList){
				if(roomTypes.getBenefitAmount()>0){
					roomTypes.setBenefitAmount(0);
				}
			}
			
		}
		return hotelPageForWebBean;
	}
	
	/**
	 * 判断是否是预付并且是底价支付
	 * @param hotelPageForWebBean
	 * @param inDate 入住的第一天
	 * @return
	 */
	public HotelPageForWebBean judgeB2BIncrease(HotelPageForWebBean hotelPageForWebBean,Date inDate,Date outDate){
		List<QueryHotelForWebResult> resultList = hotelPageForWebBean.getList();
////		boolean flag = judgeFavourableFlag(resultList); 如果同时查询多家酒店 这里做判断就是错误的
////		if(flag) return hotelPageForWebBean;
//		
//		List<Long> minPriceParams=new ArrayList<Long>(); //判断是否是底价支付的查询参数
//		
//		//如果是底价支付 根据 价格类型ID 入住日期 查询底价
//		List<Long> hotelIdParams=new ArrayList<Long>(); //酒店ID
//		List<Long> priceTypeIdParams = new ArrayList<Long>(); //价格类型ID
//		
//		//封装查询参数
//		for(QueryHotelForWebResult queryHotelForWebResult:resultList){
//			for(QueryHotelForWebRoomType queryHotelForWebRoomType:queryHotelForWebResult.getRoomTypes()){
//				//判断优惠信息
//				if(queryHotelForWebRoomType.getFk() == 1){
//					if(judgeFavurable(queryHotelForWebRoomType.getItemsList())) continue; //如果有优惠信息
//				}else if(queryHotelForWebRoomType.getFk() == 3){ 
//					if(judgeFavurable(queryHotelForWebRoomType.getSaleItems())) continue; //如果有优惠信息
//				}
//				//中旅房型，并且是预付的也适应该规则(低价+加幅)
//				if(queryHotelForWebRoomType.getRoomChannel() == 8&&(queryHotelForWebRoomType.getFk()==1||queryHotelForWebRoomType.getFk()==3)){
//					//封装查询参数(价格类型ID)
//					if(!priceTypeIdParams.contains(Long.valueOf(queryHotelForWebRoomType.getChildRoomTypeId())))
//					priceTypeIdParams.add(Long.valueOf(queryHotelForWebRoomType.getChildRoomTypeId()));
//					
//					if(!hotelIdParams.contains(queryHotelForWebResult.getHotelId())) 
//						hotelIdParams.add(queryHotelForWebResult.getHotelId());
//				}else{
//					if(queryHotelForWebRoomType.getFk()==1||queryHotelForWebRoomType.getFk()==3){ //判断为预付的
//						//step1 根据酒店ID和可售日期查询是否为底价支付 （为了提高效率将ID封装为集合一起查询）
//						long hotelId = queryHotelForWebResult.getHotelId();					
//						if(!minPriceParams.contains(hotelId)) minPriceParams.add(hotelId);
//					}
//				}
//			}
//		}
//		
//		if(minPriceParams.size() > 0){
//			//step 根据查询参数得到哪些酒店的哪些价格类型是底价支付的 数组中Object[]{1:酒店ID 2:价格类型ID}
//			List<Object[]> isMinPriceList=htlB2bDao.queryMinPrice(minPriceParams,inDate);
//			if(isMinPriceList.size()>0){
//				for(QueryHotelForWebResult queryHotelForWebResult:resultList){
//					for(QueryHotelForWebRoomType queryHotelForWebRoomType:queryHotelForWebResult.getRoomTypes()){
//						//判断优惠信息
//						if(queryHotelForWebRoomType.getFk() == 1){
//							if(judgeFavurable(queryHotelForWebRoomType.getItemsList())) continue; //如果有优惠信息
//						}else if(queryHotelForWebRoomType.getFk() == 3){ 
//							if(judgeFavurable(queryHotelForWebRoomType.getSaleItems())) continue; //如果有优惠信息
//						}
//						for(Object[] obj: isMinPriceList){
//							if(queryHotelForWebRoomType.getChildRoomTypeId().equals(obj[1].toString())){ //价格类型相等
//								//queryHotelForWebRoomType.setJudgeMinPrice(true); //设置为底价支付
//								//封装查询参数(价格类型ID)
//								if(!priceTypeIdParams.contains(Long.valueOf(queryHotelForWebRoomType.getChildRoomTypeId())))
//									priceTypeIdParams.add(Long.valueOf(queryHotelForWebRoomType.getChildRoomTypeId()));
//								
//								if(!hotelIdParams.contains(queryHotelForWebResult.getHotelId())) 
//									hotelIdParams.add(queryHotelForWebResult.getHotelId());
//							}
//						}
//					}
//				}
//			}
//		}
//			
//			List<Date> listDate = (List<Date>)com.mangocity.util.DateUtil.getDates(inDate, outDate); //根据住店日期和离店日期计算
//			
//			if(hotelIdParams.size()>0&&priceTypeIdParams.size()>0){
//				List<HtlB2bIncrease>  htlB2bIncreases= htlB2bDao.queryIncreasePrice(hotelIdParams, priceTypeIdParams, inDate, outDate);
//				if(htlB2bIncreases.size()>0){
//					//酒店级别
//					for(QueryHotelForWebResult queryHotelForWebResult:resultList){
//						//价格类型的级别
//						for(QueryHotelForWebRoomType queryHotelForWebRoomType:queryHotelForWebResult.getRoomTypes()){
//							//判断优惠信息
//							if(queryHotelForWebRoomType.getFk() == 1){
//								if(judgeFavurable(queryHotelForWebRoomType.getItemsList())) continue; //如果有优惠信息
//							}else if(queryHotelForWebRoomType.getFk() == 3){ 
//								if(judgeFavurable(queryHotelForWebRoomType.getSaleItems())) continue; //如果有优惠信息
//							}
//							
//								for(HtlB2bIncrease htlB2bIncrease:htlB2bIncreases){ //遍历查询得到的结果集
//									if(Long.valueOf(queryHotelForWebRoomType.getChildRoomTypeId()) == htlB2bIncrease.getPriceTypeId()){
//										// 设置为底价支付 加幅表中有值才设置为底价 不应当在上面设置(方便页面出现是低价但是加幅表中没有记录的情况，而页面还是显示的是底价)
//										queryHotelForWebRoomType.setJudgeMinPrice(true); 
//										
//										//如果是只有预付的情况 只需要更改QueryHotelForWebRoomType中的itemsList结果集
//										if(queryHotelForWebRoomType.getFk() == 1){ 
//											//这里需要遍历itemsList设置每天的价格
//											List<QueryHotelForWebSaleItems> itemsList = fillIncreasePrice(queryHotelForWebRoomType.getItemsList(),htlB2bIncrease);
//											queryHotelForWebRoomType.setOneDayPrice(htlB2bIncrease.getIncreasePrice());
//											queryHotelForWebRoomType.setItemsList(itemsList);
//										}else if(queryHotelForWebRoomType.getFk() == 3){ //面预付都有的情况 只需要更改saleItems
//											//这里需要遍历saleItems设置每天的价格
//											List<QueryHotelForWebSaleItems> saleItemsList = fillIncreasePrice(queryHotelForWebRoomType.getSaleItems(),htlB2bIncrease);
//											queryHotelForWebRoomType.setOneDayPrice(htlB2bIncrease.getIncreasePrice());
//											queryHotelForWebRoomType.setSaleItems(saleItemsList);
//										}
//									}
//								}
//								if(queryHotelForWebRoomType.getJudgeMinPrice()){
//									//计算均价，并写入
//									double totalPrice = 0;
//									if(queryHotelForWebRoomType.getFk() == 1){
//										totalPrice = calculateTotalPrice(queryHotelForWebRoomType.getItemsList());
//										queryHotelForWebRoomType.setItemsPrice(totalPrice);
//									} 
//									if(queryHotelForWebRoomType.getFk() == 3){
//										totalPrice = calculateTotalPrice(queryHotelForWebRoomType.getSaleItems());
//										queryHotelForWebRoomType.setPrepayPrice(totalPrice);
//									} 
//									if(listDate.size()>2){
//										queryHotelForWebRoomType.setAvlPrice(totalPrice/(listDate.size()-1));
//									}
//								}
//							
//						}
//					}
//				}
//			}
//		return hotelPageForWebBean;
		return null;
	}
	
	/**
	 * 
	 * @param items  QueryHotelForWebSaleItems集合
	 * @param hotelId 酒店ID
	 * @param priceTypeIds 价格类型ID集合
	 * @param inDate 入店日期
	 * @param outDate 离店日期
	 * @return
	 */
	public List<QueryHotelForWebSaleItems> modifyIncreaePrice(List<QueryHotelForWebSaleItems> items,long hotelId,long priceTypeId,Date inDate,Date outDate){
		List<Long> hotelIdParams =  new ArrayList<Long>();
		hotelIdParams.add(hotelId);
		List<Long> priceTypeIdParams = new ArrayList<Long>();
		priceTypeIdParams.add(priceTypeId);
		List<HtlB2bIncrease>  htlB2bIncreases= htlB2bDao.queryIncreasePrice(hotelIdParams, priceTypeIdParams, inDate, outDate);
		for(HtlB2bIncrease htlB2bIncrease:htlB2bIncreases){
			items = fillIncreasePrice(items,htlB2bIncrease);
		}
		return items;
	}
	
	/**
	 * 提供给订单校验的接口  需要更改priceList里面的价格和orderitems的价格
	 * @param order
	 * @return
	 */
	public OrOrder modifyOrderIncreasePrice(OrOrder order){
		long hotelId = order.getHotelId();
		long priceTypeId = order.getChildRoomTypeId();
		Date inDate = order.getCheckinDate();
		Date outDate = order.getCheckoutDate();
		List<Long> hotelIdParams =  new ArrayList<Long>();
		hotelIdParams.add(hotelId);
		List<Long> priceTypeIdParams = new ArrayList<Long>();
		priceTypeIdParams.add(priceTypeId);
		List<HtlB2bIncrease>  htlB2bIncreases= htlB2bDao.queryIncreasePrice(hotelIdParams, priceTypeIdParams, inDate, outDate);
		double sum = 0;
		for(HtlB2bIncrease htlB2bIncrease:htlB2bIncreases){
			//priceList
			for(OrPriceDetail priceDetail:order.getPriceList()){
				if(priceDetail.getNight().getTime() == htlB2bIncrease.getAbleSaleDate().getTime()){
					priceDetail.setBasePrice(htlB2bIncrease.getBasePrice());
					priceDetail.setSalePrice(htlB2bIncrease.getIncreasePrice());
				}
			}
			//orderitems
			for(OrOrderItem orderItem:order.getOrderItems()){
				if(orderItem.getNight().getTime() == htlB2bIncrease.getAbleSaleDate().getTime()){					
					orderItem.setSalePrice(htlB2bIncrease.getIncreasePrice());
					orderItem.setAgentReadComissionPrice(htlB2bIncrease.getIncreasePrice()); //设置佣金价
					orderItem.setAgentReadComissionRate(0);//返佣率设置为0
					orderItem.setAgentReadComission(0); //佣金额设置为0
				}
			}
			
			sum += htlB2bIncrease.getIncreasePrice();
			
		}
		sum = sum*order.getRoomQuantity(); //注意还需要乘房间数量
		order.setSum(Math.ceil(sum)); //设置原币种
		if(order.getRateId() == 0){
			double sumRmb = sum * CurrencyBean.getRateToRMB(order.getPaymentCurrency()); //设置RMB币种
		}
		double sumRmb = sum* order.getRateId();
		order.setSumRmb(Math.ceil(sumRmb));//逢一进十
		return order;
	}

	/**
	 * 更改结果集中的 价格
	 * @param itemsList
	 * @param htlB2bIncrease
	 */
	private List<QueryHotelForWebSaleItems> fillIncreasePrice(
			List<QueryHotelForWebSaleItems> itemsList,
			HtlB2bIncrease htlB2bIncrease) {
		for(QueryHotelForWebSaleItems item:itemsList){
			if(null!=item.getFellowDate()&&null!=htlB2bIncrease.getAbleSaleDate()){
				if(item.getFellowDate().getTime() == (htlB2bIncrease.getAbleSaleDate().getTime())){
					//if(item.getAgentReadComissionPrice() == 0) break;
					item.setSalePrice(htlB2bIncrease.getIncreasePrice());
					item.setAgentReadComissionPrice(htlB2bIncrease.getIncreasePrice()); //设置佣金价
					item.setAgentReadComissionRate(0);//返佣率设置为0
					item.setAgentReadComission(0); //佣金额设置为0
					item.setCurrency(htlB2bIncrease.getCurrency());
				}
			}
		}
		return itemsList;
	}
	
	/**
	 * 计算总价
	 * @param itemsList
	 * @return
	 */
	private double calculateTotalPrice(List<QueryHotelForWebSaleItems> itemsList){
		double totalPrice= 0 ;
		for(QueryHotelForWebSaleItems item:itemsList){
			totalPrice+=item.getAgentReadComissionPrice();
		}
		return totalPrice;
	}
	
	/**
	 * 判断是否有优惠信息
	 * @param resultList
	 * @return
	 */
//	private boolean judgeFavourableFlag(List<QueryHotelForWebResult> resultList){
//		boolean flag =false;
//		for(QueryHotelForWebResult queryHotelForWebResult:resultList){
//			for(QueryHotelForWebRoomType queryHotelForWebRoomType:queryHotelForWebResult.getRoomTypes()){
//				if(queryHotelForWebRoomType.getFk() == 1) {
//					flag = judgeFavurable(queryHotelForWebRoomType.getItemsList());
//					if(flag) break;
//				}
//				if(queryHotelForWebRoomType.getFk() == 3){
//					flag = judgeFavurable(queryHotelForWebRoomType.getSaleItems());
//					if(flag) break;
//				} 
//			}
//			if(flag) break;
//		}
//		return flag;
//	}
	
	private boolean judgeFavurable(List<QueryHotelForWebSaleItems> itemsList){
		boolean flag =false;
		for(QueryHotelForWebSaleItems item:itemsList){
			if(item.getFavourableFlag()){
				flag = true;
				break;
			}
		}
		return flag;
	}
	

	public HtlB2bDao getHtlB2bDao() {
		return htlB2bDao;
	}

	public void setHtlB2bDao(HtlB2bDao htlB2bDao) {
		this.htlB2bDao = htlB2bDao;
	}

	public List<HtlB2bIncrease> queryIncreasePrice(Long hotelIdParam,
			Long priceTypeIdParam, Date inDate, Date outDate) {
		List<Long> hotelIdParams = new ArrayList<Long>();
		hotelIdParams.add(hotelIdParam);
		List<Long> priceTypeIdParams = new ArrayList<Long>();
		priceTypeIdParams.add(priceTypeIdParam);
		return htlB2bDao.queryIncreasePrice(hotelIdParams, priceTypeIdParams, inDate, outDate);
	}
	
}
