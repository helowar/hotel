package com.mangocity.hotel.order.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderRequest;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.dao.IOrSMSRecvDao;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrSMSRecv;
import com.mangocity.hotel.order.service.IOrSMSService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.service.OrderCancelConfirmService;

/**
 * 主要功能：短信取消定时器，根据order判断是否要发送验证码，对OrSMSRecv的增删改查，查询发送随机码的订单
 * @author luoguangming
 */
public class OrSMSService implements IOrSMSService {
	private static final MyLog log = MyLog.getLogger(OrSMSService.class);
	private IOrSMSRecvDao orSMSRecvDao;
	private IOrderService orderService;
	private OrderCancelConfirmService orderCancelConfirmService;
	private IHDLService hdlService; 
	/**
	 * 短信取消定时器work
	 * 放在定时器中，在有关联关系一对多查询时，session有问题，会出现懒加载异常
	 * 放到service层来避免此问题
	 */
	public void work(){
		Date date = new Date();
		//1.查询今天已发送短信验证码的订单信息，没有返回
		List<OrOrder> orderlist = this.querySMSOrder(date);
		if(null==orderlist||orderlist.size()==0){
			log.info("没有找到已发送入住提醒（带验证码）的订单!");
			return;
		}
		log.info("找到的已发送入住提醒（带验证码）的订单数量："+orderlist.size());
		
		//2.获取接收的短信list，没有返回
		List<OrSMSRecv> lstRecv = this.querySMSByDate(date);
		if(null==lstRecv||lstRecv.size()==0){
			log.info("没有找到客人回复的验证码短信!");
			return;
		}
		log.info("找到的客人回复的验证码短信数量："+lstRecv.size());
		
    	//3.批量验证短信，并更改短信处理状态
    	this.process(lstRecv, orderlist);
	}
	
	/**
     * 根据order,项目编号aliasId 判断是否要发送验证码,如果要发送验证码同时会save到OrOrderExtInfo
     * @param order
     * @param aliasId
     * @return 0不发验证码，1验证码生成失败，其他返回5位随机码-表示成功生成验证码并保存
     */
    public String getSendStr(Long ID, String aliasId){
    	OrOrder order = orderService.findOrOrder(ID);
    	String checkcode = getCheckCode(String.valueOf(order.getID()),order.getMobile());
    	if("".equals(checkcode)) {
    		log.error("此单随机码生成失败！ ordercd="+order.getOrderCD());
    		return null;
    	}else{
    		//保存验证码到or_orderextinfo中去,这里直接save order报DAException,未知原因
    		orSMSRecvDao.addCheckcodeToExtInfoBySql(checkcode,order.getID());
    		return checkcode;
    	}	
    }
    /**
     * 生成5位的随机验证码，第一位固定为1代表酒店，后四位:订单id后四位加手机号的后三位和取后四位
     * @param orderid 
     * @param mobile2 11位
     * @return
     */
	private static String getCheckCode(String orderid, String mobile) {
		if(!StringUtil.isValidStr(orderid) 
				|| !StringUtil.isValidStr(mobile) 
				|| mobile.length()<3){
			log.error("获取的手机号不正确！orderiD="+orderid+",and mobile="+mobile+"!");
			return "";
		}else if(!isValidNum(mobile)){
			log.error("获取的手机号不是数字！mobile="+mobile+"!");
			return "";
		}
		mobile = mobile.substring(mobile.length()-3);
		BigInteger cd = new BigInteger(orderid);
		BigInteger phone = new BigInteger(mobile);
		BigInteger sum = cd.add(phone);
		String checkCode = String.valueOf(sum);
		return "1"+checkCode.substring(checkCode.length()-4);
	}
	private static boolean isValidNum(String str){
		boolean b = true;
		String strs = str.trim();
		for(char ch :strs.toCharArray()){
			if(ch > '9' || ch < '0'){
				b = false;
				break;
			}
		}
		return b;
	}
	/**
	 * 获取所有接收到的短信
	 * @return
	 */
	public List<OrSMSRecv> queryAll() {
		return orSMSRecvDao.queryAll();
	}
	/**
	 * 根据手机号和日期获取所有接收短信
	 * @return
	 */
	public List<OrSMSRecv> querySMSByMobileAndDate(String mobile,String date,String orderid){
		return orSMSRecvDao.querySMSByMobileAndDate(mobile,date,orderid);
	}
	/**
	 * 根据日期获取短信
	 * @param date
	 * @return
	 */
	public List<OrSMSRecv> querySMSByDate(Date date) {
		return orSMSRecvDao.querySMSByDate(date);
	}
	/**
	 * 批量更新 
	 * @param orSMSRecv
	 */
	public void batchUpdate(List<OrSMSRecv> lst) {
		if(null==lst || lst.size()<=0){
			log.error("短信列表为空，更新状态失败！");
			return;
		}
		orSMSRecvDao.batchUpdate(lst);
	}
	/**
	 * 根据日期查询已发送短信验证码的订单信息
	 * @param date
	 */
	public List<OrOrder> querySMSOrder(Date date) {
		return orSMSRecvDao.querySMSOrder(date);
	}
	
	/**
	 * 批量处理短信取消订单
	 * @param lstRecv
	 * @param lstInfo
	 */
	public void process(List<OrSMSRecv> lstRecv,List<OrOrder> orderlist){
		UserWrapper user = new UserWrapper();
		user.setName("system");
		user.setLoginName("system");
		MemberDTO member = new MemberDTO();
		member.setMemberstate("2");
		member.setState("XJG");//设置是是mango的
		member.setName("system");
		for(OrSMSRecv recv:lstRecv){
			String fromno = recv.getFromno();
			String content = recv.getContent();
			boolean  bIsCancel = false;//是否找到短信对应的订单并已成功取消
			String matchOrderid="";//匹配的orderid
			//查找匹配的订单，执行取消订单操作
			for(OrOrder order:orderlist){
				if(order.getOrderState()!=14 && fromno.equals(order.getMobile())){//手机号匹配
					OrOrderExtInfo ext = null;
					for(int i=0;i<order.getOrOrderExtInfoList().size();i++){
						if("03".equals(order.getOrOrderExtInfoList().get(i).getType())){
							ext = order.getOrOrderExtInfoList().get(i);
							break;
						}
					}
					if(ext!=null && content.equals(ext.getContext())){//且随机码匹配，取消订单
						log.info("找到匹配的短信，开始取消订单，订单CD=="+order.getOrderCD());
						matchOrderid = String.valueOf(order.getID());
						bIsCancel = true;
						try{
							order.setCancelReason(4);//取消原因，客人原因
							order.setGuestCancelMessage("14");//二级原因，短信取消
							orderService.cancelOrder(order, 4, "短信取消", "14", user);
							//取消elong
					        if(ChannelType.CHANNEL_ELONG == order.getChannel() && order.getOrderCdForChannel()!=null &&!"".equals(order.getOrderCdForChannel())){
					        	CancelExRoomOrderRequest req=new CancelExRoomOrderRequest();
					        	req.setOrderId(order.getID());
					        	req.setHotelId(order.getHotelId());
					        	req.setChannelType(order.getChannel());
					        	req.setChannelCode(order.getChannel()+"");
					        	req.setChainCode("");
					        	req.setCancelReason("");
					        	req.setCancelMessage("");
								hdlService.cancelExRoomOrder(req);
					        }
							//根据确认方式发送传真或email ,如果是直连或艺龙不需要发送
							if(ChannelType.CHANNEL_ELONG != order.getChannel() && order.isOriChannel()){
								log.info("订单--CD："+order.getOrderCD()+",开始发送确认");
								orderCancelConfirmService.orderCancelSend(order, member, user);
							}
							log.info("订单--CD："+order.getOrderCD()+",已成功通过短信取消！");
							orderlist.remove(order);//清掉此订单，不再匹配
							break;
						}catch(Exception e){//生产日志只见有info
							log.info("短信取消订单处理失败,订单CD:"+order.getOrderCD());
							log.error("短信取消订单处理失败,订单CD:"+order.getOrderCD());
						}
					}
				}
			}
			//保存sms_recv的处理结果
			if(bIsCancel){//成功找到匹配订单,状态设为1,匹配成功
				recv.setStatus(Long.valueOf(1));
			}else{//没找到匹配订单 状态改为-1
				recv.setStatus(Long.valueOf(-1));
			}
			recv.setOrderid(matchOrderid);
			recv.setTimechar(DateUtil.datetimeToString(new Date()));//记录短信处理时间
		}
		//批量更新OrSMSRecv状态
		this.batchUpdate(lstRecv);
	}
	/**
	 * 获取序列的下一个值
	 * @param seqName
	 * @return
	 */
	public long getOrParamSeqNextVal(String seqName){
		return orSMSRecvDao.getOrParamSeqNextVal(seqName);
	}
	
	public IOrSMSRecvDao getOrSMSRecvDao() {
		return orSMSRecvDao;
	}

	public void setOrSMSRecvDao(IOrSMSRecvDao orSMSRecvDao) {
		this.orSMSRecvDao = orSMSRecvDao;
	}
	public IOrderService getOrderService() {
		return orderService;
	}
	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}
	public OrderCancelConfirmService getOrderCancelConfirmService() {
		return orderCancelConfirmService;
	}
	public void setOrderCancelConfirmService(
			OrderCancelConfirmService orderCancelConfirmService) {
		this.orderCancelConfirmService = orderCancelConfirmService;
	}
	public IHDLService getHdlService() {
		return hdlService;
	}

	public void setHdlService(IHDLService hdlService) {
		this.hdlService = hdlService;
	}
}
