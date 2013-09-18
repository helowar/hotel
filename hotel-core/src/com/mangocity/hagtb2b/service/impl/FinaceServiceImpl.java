package com.mangocity.hagtb2b.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mangocity.hagtb2b.assisbean.AgentOrderStatis;
import com.mangocity.hagtb2b.condition.StaticInfoQueryCon;
import com.mangocity.hagtb2b.dao.IFinaceDao;
import com.mangocity.hagtb2b.persistence.AgentOrder;
import com.mangocity.hagtb2b.persistence.AgentOrderItem;
import com.mangocity.hagtb2b.persistence.AgentOrg;
import com.mangocity.hagtb2b.persistence.AgentUser;
import com.mangocity.hagtb2b.persistence.CommPolicySecond;
import com.mangocity.hagtb2b.persistence.StatisticsInfo;
import com.mangocity.hagtb2b.service.IB2bService;
import com.mangocity.hagtb2b.service.IFinaceService;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.collections.FormatMap;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.hotel.constant.PayMethod;

public class FinaceServiceImpl implements IFinaceService {
	
	private IFinaceDao finaceDao;
	
	private IOrderService orderService;
	 
	private DAOIbatisImpl queryDao;
	
	/**B2B 所得税率常量 */
	private final static  double commision_rate  = 0.0561;
	private IB2bService b2bService;
	public IOrderService getOrderService() {
		return orderService;
	}
	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}
	

	
	public IB2bService getB2bService() {
		return b2bService;
	}
	public void setB2bService(IB2bService service) {
		b2bService = service;
	}
	public void confirmStaticAgent(StatisticsInfo con,Map param) throws Exception {	
		if(null!=param.get("operaterId")){
			con.setOperId(param.get("operaterId").toString());
		}else{
			con.setOperId("mangotest");
		}
		con.setConfirmed(1);
		finaceDao.confirmStaticAgent(con);
	}
	public List<StatisticsInfo> staticAgentOrderQuery(StaticInfoQueryCon con) throws Exception {
		List<StatisticsInfo> infoList = null;
		infoList = finaceDao.staticAgentOrderQuery(con);
		return infoList;
	}

	public IFinaceDao getFinaceDao() {
		return finaceDao;
	}

	public void setFinaceDao(IFinaceDao finaceDao) {
		this.finaceDao = finaceDao;
	}
	/*private StatisticsInfo getAgentSatis(List list) throws Exception {
		StatisticsInfo agentStatis = new StatisticsInfo();
		int nightsNum = 0;
		double sumAcount = 0.0;
		int actNightsNum = 0;
		double actSumAcount = 0.0;
		double commsion = 0.0;
		for(int i=0;i<list.size();i++){
			Map objMap = (Map)list.get(i);
			long orderId = Long.parseLong(objMap.get("ORDERID").toString());
	        OrOrder order = orderService.getOrder(orderId);
	        List<OrOrderItem> itemList = finaceDao.getOrderitem(orderId);
			nightsNum = nightsNum + itemList.size();//总间夜量
			sumAcount = sumAcount + order.getSumRmb();//总金额
			AgentOrderStatis statis = convertAgentOrder(order,itemList);
			actNightsNum = actNightsNum + statis.getActNights();//实际间夜量
			actSumAcount = actSumAcount + statis.getActSumRmb();//实际金额
			commsion = commsion + statis.getCommision();//佣金
		}
		agentStatis.setOrderNum(list.size());//订单总数
		agentStatis.setNightsNum(nightsNum);
		agentStatis.setSumAcount(sumAcount);
		agentStatis.setActNightsNum(actNightsNum);
		agentStatis.setActSumAcount(actSumAcount);
		agentStatis.setCommsion(commsion);
		return agentStatis;
	}*/
	private AgentOrderStatis convertAgentOrder(AgentOrder agentOrder,List<OrOrderItem> items,String payMethod){
		AgentOrderStatis satis = new AgentOrderStatis();
		int   actNights=0;
		double actSumRmb=0.0d;
		double commision=0.0d;
		double commPrice =0.0d;
		double commRate =0.0d;
		List<AgentOrderItem> agentItemList = new ArrayList();
		OrOrderItem item = null;
		double tempRate = agentOrder.getRate();
		if(tempRate==0){
			tempRate=1;
			}
		for(int i=0;i<items.size();i++){
			item = items.get(i);
			 if(PayMethod.PRE_PAY.equals(payMethod)||(PayMethod.PAY.equals(payMethod)&&item.getOrderState()==1&&item.isSettlement()==true)){
			if(item.getSalePrice()>0 && item.getSalePrice()<99999)
				actNights++;
				actSumRmb = actSumRmb + item.getAgentReadComissionPrice()*tempRate;
				commision = commision + item.getAgentReadComission()*tempRate;
				commPrice+=item.getAgentReadComissionPrice();
				commRate = item.getAgentReadComissionRate();

//				if(B2bCookConstant.PERCENT==order.getFootWay()){//返佣方式1佣金
//					commision = commision + Double.parseDouble(order.getFootFee());
//				}else if(B2bCookConstant.PERCENT==order.getFootWay()){//返佣方式2百分比
//					if(item.getBreakfastWay().indexOf(".")>0
//							&&Integer.parseInt(item.getBreakfastWay())<2){
//						commision = commision + item.getSalePrice()*(Double.parseDouble(order.getFootFee()));
//					}else{
//						commision = commision + item.getSalePrice()*(Double.parseDouble(order.getFootFee()))/100;
//					}
//				}
				
				AgentOrderItem agentOrderitemStatis = new AgentOrderItem();
				
				
				MyBeanUtil.copyProperties(agentOrderitemStatis, item);
				agentOrderitemStatis.setID(null);
				agentOrderitemStatis.setSaleprice(StringUtil.Baoliu(item.getSalePrice()*tempRate,2));
				agentOrderitemStatis.setFellowname(item.getFellowName());
				agentOrderitemStatis.setOutNight(DateUtil.getDate(item.getNight(),1));
				agentOrderitemStatis.setSubsidyBeforeComission(StringUtil.Baoliu(item.getAgentReadComission()*tempRate,2));
				agentOrderitemStatis.setAgentReadComission(StringUtil.Baoliu(item.getAgentReadComission()*tempRate,2));
				agentOrderitemStatis.setAgentComission(StringUtil.Baoliu(agentOrderitemStatis.getAgentComission()*tempRate,2));
				agentOrderitemStatis.setAgentComissionPrice(StringUtil.Baoliu(agentOrderitemStatis.getAgentComissionPrice()*tempRate,2));
				agentOrderitemStatis.setAgentReadComissionPrice(StringUtil.Baoliu(agentOrderitemStatis.getAgentReadComissionPrice()*tempRate,2));

				agentOrderitemStatis.setAgentOrder(agentOrder);
				agentItemList.add(agentOrderitemStatis);
			}
		}
		satis.setActNights(actNights);
		satis.setActSumRmb(StringUtil.Baoliu(actSumRmb,2));
		satis.setCommisionPrice(StringUtil.Baoliu(commPrice,2));
		satis.setCommisionRate(StringUtil.Baoliu(commRate,2));
		satis.setCommision(StringUtil.Baoliu(commision,2));
		satis.setCommision_old(StringUtil.Baoliu(commision,2));
		satis.setAgentOrderitemStatis(agentItemList);
		return satis;
	}
	/**
	 * 得到Orderitem的统计 add by yong.zeng
	 * 
	 * @param order
	 * @param items
	 * @param cp
	 * @return
	 */
	private AgentOrderStatis convertAgentOrder(AgentOrder agentOrder,List<OrOrderItem> items,CommPolicySecond cp,long totalNigthRooms,String payMethod) throws Exception{
		AgentOrderStatis satis = new AgentOrderStatis();
		int   actNights=0;
		double actSumRmb=0.0d;
		double commision=0.0d;
		double commision_old = 0.0d;
		double oldItemComission=0.0d;
		double commPrice =0.0d;
		double commRate =0.0d;
		
		List<AgentOrderItem> agentItemList = new ArrayList();
		OrOrderItem item = null;
		double tempRate = agentOrder.getRate();
		if(tempRate==0){
			tempRate=1;
			}
		for(int i=0;i<items.size();i++){
			double curItemComission = 0.0d;
			item = items.get(i);
				 if(PayMethod.PRE_PAY.equals(payMethod)||(PayMethod.PAY.equals(payMethod)&&item.getOrderState()==1&&item.isSettlement()==true)){
				if(item.getSalePrice()>0 && item.getSalePrice()<99999)
				actNights++;
				
				actSumRmb = actSumRmb + item.getAgentReadComissionPrice()*tempRate;
				commision_old = commision_old + item.getAgentReadComission()*tempRate;
				if(null!=cp){//适用于第二套政策\
					if(totalNigthRooms<=cp.getNightRoomNum()){//小于等于设定值，如：<=100
					/*	if(item.getAgentReadComission()<cp.getComm1()&&item.getSalePrice()>0 && item.getSalePrice()<99999){//如小于20,就改为20
							curItemComission = cp.getComm1();
						}else{
							curItemComission = item.getAgentReadComission()*tempRate;
						}*/
						if(item.getSalePrice()>0 && item.getSalePrice()<99999){
							curItemComission = cp.getComm1();
						}
					}else{//大于设定值,如>100
/*						if(item.getAgentReadComission()<cp.getComm2()&&item.getSalePrice()>0 && item.getSalePrice()<99999){//如小于20,就改为20
							curItemComission = cp.getComm2();
						}else{
							curItemComission = item.getAgentReadComission()*tempRate;
						}*/
						if(item.getSalePrice()>0 && item.getSalePrice()<99999){
							curItemComission = cp.getComm2();
						}
					}
				}else{
					curItemComission = item.getAgentReadComission()*tempRate;
				}
//				if(B2bCookConstant.PERCENT==order.getFootWay()){//返佣方式1佣金
//					commision = commision + Double.parseDouble(order.getFootFee());
//				}else if(B2bCookConstant.PERCENT==order.getFootWay()){//返佣方式2百分比
//					if(item.getBreakfastWay().indexOf(".")>0
//							&&Integer.parseInt(item.getBreakfastWay())<2){
//						commision = commision + item.getSalePrice()*(Double.parseDouble(order.getFootFee()));
//					}else{
//						commision = commision + item.getSalePrice()*(Double.parseDouble(order.getFootFee()))/100;
//					}
//				}
				
				AgentOrderItem agentOrderitemStatis = new AgentOrderItem();
				MyBeanUtil.copyProperties(agentOrderitemStatis, item);
				agentOrderitemStatis.setID(null);
				
				agentOrderitemStatis.setSaleprice(StringUtil.Baoliu(item.getSalePrice()*tempRate,2));
				agentOrderitemStatis.setFellowname(item.getFellowName());
				agentOrderitemStatis.setOutNight(DateUtil.getDate(item.getNight(),1));
				agentOrderitemStatis.setSubsidyBeforeComission(StringUtil.Baoliu(item.getAgentReadComission()*tempRate,2));
				agentOrderitemStatis.setAgentReadComission(StringUtil.Baoliu(curItemComission,2));
				agentOrderitemStatis.setAgentComission(StringUtil.Baoliu(agentOrderitemStatis.getAgentComission()*tempRate,2));
				agentOrderitemStatis.setAgentComissionPrice(StringUtil.Baoliu(agentOrderitemStatis.getAgentComissionPrice()*tempRate,2));
				agentOrderitemStatis.setAgentReadComissionPrice(StringUtil.Baoliu(agentOrderitemStatis.getAgentReadComissionPrice()*tempRate,2));
				
				agentOrderitemStatis.setAgentOrder(agentOrder);
				commPrice+=agentOrderitemStatis.getAgentReadComissionPrice();
				commRate = item.getAgentReadComissionRate();
				commision= commision+curItemComission;
				oldItemComission = oldItemComission + item.getAgentReadComission()*tempRate;
				agentItemList.add(agentOrderitemStatis);
			}
		}
		satis.setActNights(actNights);//间夜
		satis.setActSumRmb(StringUtil.Baoliu(actSumRmb,2));//各间夜销售价之和
		satis.setCommisionPrice(StringUtil.Baoliu(commPrice,2));
		satis.setCommisionRate(StringUtil.Baoliu(commRate,2));
		satis.setCommision(StringUtil.Baoliu(commision,2));//佣金总和
		satis.setCommision_old(StringUtil.Baoliu(oldItemComission,2));//阶梯奖励之前的佣金
		satis.setAgentOrderitemStatis(agentItemList);//orderitem列表
		return satis;
	}
	

	/**
	 * 当满足阶梯返佣时，要用新的政策佣金替代原来的佣金，并更新到database
	 * add by yong.zeng
	 * @param params
	 * @param agentStatis
	 * @param curAgentCode
	 * @return
	 * @throws Exception
	 */
	public List<AgentOrder> getAgentOrderList(Map params,StatisticsInfo agentStatis,String curAgentCode) throws Exception {
		if(null!=curAgentCode&&!"".equals(curAgentCode)){
			params.put("agentCode", curAgentCode);
		}
		List list = queryDao.queryForList("queryAgentOrder", params);
		

		String orderIdLst = "";
		long orderitemCount = 0;//间夜量
		List<OrOrderItem> addLst = new ArrayList();
		int policyScope = orderService.getPolicyScope(curAgentCode);

		for(Map aorder:(List<Map>)list){
			orderIdLst +=aorder.get("ORDERID").toString()+",";
		}
		if(orderIdLst.endsWith(","))orderIdLst = orderIdLst.substring(0,orderIdLst.length()-1);
		params.put("orderIdLst", orderIdLst);
		params.put("listSize", list.size());
		orderitemCount = finaceDao.getOrderitemCount(params);

		

		List<AgentOrder> orderList = new ArrayList<AgentOrder>();
		//int nightsNum = 0;
		double sumAcount = 0.0;
		int actNightsNum = 0;
		double actSumAcount = 0.0;
		double commsion = 0.0;
		double commsion_old = 0.0;
		for(int i=0;i<list.size();i++){
			Map objMap = (Map)list.get(i);
			//封装代理订单信息 begin
			AgentOrder agentOrder = new AgentOrder();
			agentOrder.setOrderId(Long.parseLong(objMap.get("ORDERID").toString()));
			agentOrder.setOrderCd(objMap.get("ORDERCD").toString());
			//agentOrder.setIsMinPrice(true);
			if(null!=objMap.get("ISMINPRICE"))agentOrder.setIsMinPrice(objMap.get("ISMINPRICE").toString());
			if(null!=objMap.get("MEMBERCD"))agentOrder.setAgentCode(objMap.get("MEMBERCD").toString());
			if(null!=objMap.get("HOTELNAME"))agentOrder.setHotelName(objMap.get("HOTELNAME").toString());
			if(null!=objMap.get("ROOMTYPENAME"))agentOrder.setRoomName(objMap.get("ROOMTYPENAME").toString());
			if(null!=objMap.get("CHILDROOMTYPENAME"))agentOrder.setChildRoomName(objMap.get("CHILDROOMTYPENAME").toString());
			if(null!=objMap.get("ROOMQUANTITY"))agentOrder.setRoomNum(Integer.parseInt(objMap.get("ROOMQUANTITY").toString()));
			if(null!=objMap.get("BEDTYPE"))agentOrder.setBedType(Integer.parseInt(objMap.get("BEDTYPE").toString()));
			if(null!=objMap.get("CHECKINDATE"))agentOrder.setCheckInDate(DateUtil.getDate(objMap.get("CHECKINDATE").toString()));
			if(null!=objMap.get("CHECKOUTDATE"))agentOrder.setCheckOutDate(DateUtil.getDate(objMap.get("CHECKOUTDATE").toString()));
			if(null!=objMap.get("SUMRMB"))agentOrder.setSumRmb(Double.parseDouble(objMap.get("SUMRMB").toString()));
			
			if(null!=objMap.get("RATEID"))agentOrder.setRate(Double.parseDouble(objMap.get("RATEID").toString()));
			
			if(null!=objMap.get("FELLOWNAMES"))agentOrder.setFellowNames(objMap.get("FELLOWNAMES").toString());
			String tsource = "";
			if(null!=objMap.get("SOURCE")){
				tsource= objMap.get("SOURCE").toString();
			}
			if(tsource.equals("NET")){
				if(null!=objMap.get("CREATOR"))agentOrder.setBookingname(objMap.get("CREATOR").toString());
			}else if(tsource.equals("PHE")){
				if(null!=objMap.get("LINKMAN"))agentOrder.setBookingname(objMap.get("LINKMAN").toString());
			}
			agentOrder.setSatisInfo(agentStatis);
			orderList.add(agentOrder);
			//封装代理订单信息 end
			long orderId = Long.parseLong(objMap.get("ORDERID").toString());
	        OrOrder order = orderService.getOrderItems(orderId);
	        //List<OrOrderItem> itemList = finaceDao.getOrderitem(orderId);
	        List<OrOrderItem> itemList = order.getOrderItems();
			//nightsNum = nightsNum + itemList.size();//总间夜量
			sumAcount = sumAcount + order.getSumRmb();//总金额
			
			AgentOrderStatis statis;
			//add by yong.zeng
			 
			if(policyScope==2){//适用于二套政策，阶梯返佣
				CommPolicySecond secondPolicy = new CommPolicySecond();
				secondPolicy.setAgentCode(curAgentCode);
				secondPolicy.setNightRoomNum(100);
				secondPolicy.setComm1(20.00);
				secondPolicy.setComm2(25.00);
				
				 statis = convertAgentOrder(agentOrder,itemList,secondPolicy,orderitemCount,order.getPayMethod());
			}else{
				 statis = convertAgentOrder(agentOrder,itemList,order.getPayMethod());	
			}
			
			actNightsNum = actNightsNum + statis.getActNights();//实际间夜量
			actSumAcount = actSumAcount + statis.getActSumRmb();//实际金额
			commsion = commsion + statis.getCommision();//佣金
			commsion_old+=statis.getCommision_old();//阶梯奖励前的佣金
			
			agentOrder.setCommisionPrice(statis.getCommisionPrice());
			agentOrder.setCommisionRate(statis.getCommisionRate());
			
			agentOrder.setCommision(statis.getCommision());//填写订单佣金
			agentOrder.setBackCommission(statis.getCommision_old());//阶梯奖励前的佣金
			agentOrder.setOrderItems(statis.getAgentOrderitemStatis());//agentOrderitems
		}
		if(orderList.size()>0&&null!=orderList.get(0).getAgentCode())
			agentStatis.setAgentCode(orderList.get(0).getAgentCode());
		if(null!=params.get("operaterId"))
			agentStatis.setOperId(params.get("operaterId").toString());
		if(null!=params.get("agentName"))
			agentStatis.setAgentName(params.get("agentName").toString());
		if(null!=params.get("year"))
			agentStatis.setStatYear(params.get("year").toString());
		if(null!=params.get("month"))
			agentStatis.setStatMonth(Integer.parseInt(params.get("month").toString()));
		agentStatis.setOrderNum(list.size());//订单总数
		//agentStatis.setNightsNum(nightsNum);
		agentStatis.setNightsNum(Long.valueOf(orderitemCount).intValue());
		sumAcount = StringUtil.Baoliu(sumAcount, 2);
		agentStatis.setSumAcount(sumAcount);
		agentStatis.setActNightsNum(actNightsNum);
		actSumAcount = StringUtil.Baoliu(actSumAcount, 2);
		agentStatis.setActSumAcount(actSumAcount);
		commsion = StringUtil.Baoliu(commsion, 2);
		agentStatis.setCommsion(commsion);
		commsion_old = StringUtil.Baoliu(commsion_old, 2);
		agentStatis.setBackCommission(commsion_old);
		agentStatis.setOrderItems(orderList);
		
		//个人所得税
		double commrate = getCommRate(commsion,curAgentCode);
		agentStatis.setCommrate(commrate);
		agentStatis.setFactcomm(StringUtil.Baoliu(commsion-commrate,2));
		
		
		//add by yong.zeng
		//更新OrOrderItem中的佣金(满足阶梯返佣政策)
		//finaceDao.saveOrUpdateAll(addLst);
		return orderList;
	}
	
	
	
	
	
	
	
	public List<AgentOrder> getAgentOrderList(Map params,StatisticsInfo agentStatis) throws Exception {
		List list = queryDao.queryForList("queryAgentOrder", params);
		List<AgentOrder> orderList = new ArrayList<AgentOrder>();
		//int nightsNum = 0;
		double sumAcount = 0.0;
		int actNightsNum = 0;
		double actSumAcount = 0.0;
		double commsion = 0.0;
		double commsion_old = 0.0;
		for(int i=0;i<list.size();i++){
			Map objMap = (Map)list.get(i);
			//封装代理订单信息 begin
			AgentOrder agentOrder = new AgentOrder();
			agentOrder.setOrderId(Long.parseLong(objMap.get("ORDERID").toString()));
			agentOrder.setOrderCd(objMap.get("ORDERCD").toString());
			if(null!=objMap.get("MEMBERCD"))agentOrder.setAgentCode(objMap.get("MEMBERCD").toString());
			if(null!=objMap.get("HOTELNAME"))agentOrder.setHotelName(objMap.get("HOTELNAME").toString());
			if(null!=objMap.get("ROOMTYPENAME"))agentOrder.setRoomName(objMap.get("ROOMTYPENAME").toString());
			if(null!=objMap.get("CHILDROOMTYPENAME"))agentOrder.setChildRoomName(objMap.get("CHILDROOMTYPENAME").toString());
			if(null!=objMap.get("ROOMQUANTITY"))agentOrder.setRoomNum(Integer.parseInt(objMap.get("ROOMQUANTITY").toString()));
			if(null!=objMap.get("BEDTYPE"))agentOrder.setBedType(Integer.parseInt(objMap.get("BEDTYPE").toString()));
			if(null!=objMap.get("CHECKINDATE"))agentOrder.setCheckInDate(DateUtil.getDate(objMap.get("CHECKINDATE").toString()));
			if(null!=objMap.get("CHECKOUTDATE"))agentOrder.setCheckOutDate(DateUtil.getDate(objMap.get("CHECKOUTDATE").toString()));
			if(null!=objMap.get("SUMRMB"))agentOrder.setSumRmb(Double.parseDouble(objMap.get("SUMRMB").toString()));
			if(null!=objMap.get("RATEID"))agentOrder.setRate(Double.parseDouble(objMap.get("RATEID").toString()));
			if(null!=objMap.get("FELLOWNAMES"))agentOrder.setFellowNames(objMap.get("FELLOWNAMES").toString());
			String tsource = "";
			if(null!=objMap.get("SOURCE")){
				tsource= objMap.get("SOURCE").toString();
			}
			if(tsource.equals("NET")){
				if(null!=objMap.get("CREATOR"))agentOrder.setBookingname(objMap.get("CREATOR").toString());
			}else if(tsource.equals("PHE")){
				if(null!=objMap.get("LINKMAN"))agentOrder.setBookingname(objMap.get("LINKMAN").toString());
			}
			agentOrder.setSatisInfo(agentStatis);
			orderList.add(agentOrder);
			//封装代理订单信息 end
			long orderId = Long.parseLong(objMap.get("ORDERID").toString());
	        OrOrder order = orderService.getOrder(orderId);
	        List<OrOrderItem> itemList = finaceDao.getOrderitem(orderId);
			//nightsNum = nightsNum + itemList.size();//总间夜量
			sumAcount = sumAcount + order.getSumRmb();//总金额
			AgentOrderStatis statis = convertAgentOrder(agentOrder,itemList,order.getPayMethod());
			actNightsNum = actNightsNum + statis.getActNights();//实际间夜量
			actSumAcount = actSumAcount + statis.getActSumRmb();//实际金额
			commsion = commsion + statis.getCommision();//佣金
			commsion_old+=statis.getCommision_old();//阶梯奖励前的佣金
			agentOrder.setCommision(statis.getCommision());//填写订单佣金
			agentOrder.setBackCommission(statis.getCommision_old());//阶梯奖励前的佣金
			agentOrder.setOrderItems(statis.getAgentOrderitemStatis());//agentOrderitems
		}
		if(orderList.size()>0&&null!=orderList.get(0).getAgentCode())
			agentStatis.setAgentCode(orderList.get(0).getAgentCode());
		if(null!=params.get("operaterId"))
			agentStatis.setOperId(params.get("operaterId").toString());
		if(null!=params.get("agentName"))
			agentStatis.setAgentName(params.get("agentName").toString());
		if(null!=params.get("year"))
			agentStatis.setStatYear(params.get("year").toString());
		if(null!=params.get("month"))
			agentStatis.setStatMonth(Integer.parseInt(params.get("month").toString()));
		agentStatis.setOrderNum(list.size());//订单总数
		//agentStatis.setNightsNum(nightsNum);
		agentStatis.setSumAcount(sumAcount);
		agentStatis.setActNightsNum(actNightsNum);
		agentStatis.setActSumAcount(actSumAcount);
		agentStatis.setCommsion(commsion);
		agentStatis.setBackCommission(commsion);
		agentStatis.setOrderItems(orderList);
		return orderList;
	}
	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}
	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}
	public List<AgentOrder> getAgentOrderList(long orderId) throws Exception {
		List<AgentOrder> retList = null;
		retList = finaceDao.getAgentItem(orderId);
		return retList;
	}
	/**
	 * add yong.zeng
	 * @param id
	 * @return
	 */
	public StatisticsInfo getStatisticsInfo(Long id){
		StatisticsInfo statis = (StatisticsInfo)finaceDao.load(StatisticsInfo.class, id);
		if(null!=statis){
			Hibernate.initialize(statis.getOrderItems());
		}
		
		return statis;
	}
	
	/**
	 * 根据当前loginname得到其会角色
	 * @param loginname
	 * @return
	 */
	public String getCurRole(String loginname){
		String role = "";
		AgentUser au = (AgentUser)queryDao.queryForObject("queryAgentRole",loginname);
		if(au==null) return "";
		if("2".equals(au.getUsertype())||"3".equals(au.getUsertype()))//芒果操作员 
		{
			role= "mango";
		}else if("1".equals(au.getUsertype())){//代理商
			if(1==au.getPermission()){
				role = "agentAdmin";
			}else{
				role = "agent";
			}
		}
		return role;
		}
	
	/**
	 * 收入为x
	*if 800 0
	*else 4000 (x-800)*0.2
	*else 5000 x*0.8*0.2
	*else 20000 x*.0.052+x*(1-0.052-0.2)*0.2
	*else 50000 x*.0.052+x*(1-0.052-0.2)*0.3-2000
	*else  x*.0.052+x*(1-0.052-0.2)*0.4-7000
	*保留两位小数
	 * @param commision
	 * @param agentCode
	 * @return
	 */
	public double getCommRate(double commision,String agentCode){
		
		double result = 0.00d;
		FormatMap agentmap = new FormatMap();
		agentmap.put("agentcode", agentCode);
		AgentOrg agentOrg = queryAgentOrg(agentmap);
		
		if(null!=agentOrg){
			if(1==agentOrg.getAgentkind()){//公司
				return 0;
			}
		}
		
		//计算所得税
		if(commision<=800){
			result = 0;
		}else if(commision<=4000){
			result = (commision-800)*0.2;
		}else if(commision<=5000){
			result = commision*0.8*0.2;
		}else if(commision<=20000){
			result = commision*commision_rate+commision*(1-commision_rate-0.2)*0.2;
		}else if(commision<=50000){
			result = commision*commision_rate+commision*(1-commision_rate-0.2)*0.3-2000;
		}else{
			result = commision*commision_rate+commision*(1-commision_rate-0.2)*0.4-7000;
		}
		result = (Math.round(result*100))/100.0;
		
		return result;
	}
	
	public AgentOrg queryAgentOrg(Map maps){
		return (AgentOrg)queryDao.queryForObject("queryAgentOrgObject", maps);
	}
	
}
