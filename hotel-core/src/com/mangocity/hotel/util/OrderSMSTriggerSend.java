package com.mangocity.hotel.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderOfSMS;
import com.mangocity.hotel.order.service.IOrSMSService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.msg.inside.dto.MsgRequest;
import com.mangocity.msg.inside.dto.MsgResponse;
import com.mangocity.msg.service.IMsgService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.AutoSendSMSConstant;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Sms;

/**
 * 根据CC短信需求,自动发送入住提醒短信
 * 
 * @author:shizhongwen 创建日期:Jan 15, 2009,5:15:32 PM 描述：
 */
public class OrderSMSTriggerSend extends QuartzJobBean implements Serializable {
	private static final MyLog log = MyLog.getLogger(OrderSMSTriggerSend.class);

    private IOrderService orderService;

    /**
     * message接口
     */
    private CommunicaterService communicaterService;
    
    /**
     * 短信平台service，后缀自填
     */
    private IMsgService mgMsgInterface;

    
    private SystemDataService systemDataService;
    
    private IOrSMSService orSMSService;

    @Override
    /*
     * 每天的9点和下午的4点向客户发送提醒短信 add by shizhongwen 时间:Jan 15, 2009 5:12:56 PM
     */
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    	
    	OrParam orParam = new OrParam();
		try {
			log.info("SEND_REMIND_SMS:发送入住提醒短信定时器begin！ ");
			int i = new Random().nextInt(1000);
			Thread.sleep(i);
			
			orParam = systemDataService.getSysParamByName("IS_SEND_SMS");
			//给orParam的sequence生成下一个值，更新当前生成的sequence到param的value值中
			String nextSeq = orSMSService.getOrParamSeqNextVal("SEQ_HTL_JOB_SEND_REMIND_SMS")+"";
			orParam.setValue(nextSeq);
			orParam.setModifyTime(new Date());
			systemDataService.updateSysParamByName(orParam);
			log.info("SEND_REMIND_SMS:当前线程sleep "+i+" 毫秒;获取的sequence="+nextSeq+";当前线程再sleep 10 秒！");
			//sleep 10 秒，等所有集群服务器update之后，再读取param，如果value值等于当前生成的sequence,执行短信取消，否则返回
			Thread.sleep(10000);
			orParam = systemDataService.getSysParamByName("IS_SEND_SMS");
			if(nextSeq.equals(orParam.getValue())){
				log.info("SEND_REMIND_SMS:抢到了定时器，开始执行");
				work();
			}else{
				log.info("SEND_REMIND_SMS:短信取消定时器已在执行中！");
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			log.info("SEND_REMIND_SMS:发送入住提醒短信定时器end！ ");
		}
    }

    private void work(){
    	Date newDate = new Date();
    	int newhours = newDate.getHours();
        log.info("现在时间:" + newhours + "点,开始向已订房的客户发送通知短信!!!");
        if (9 == newhours) { // 清空上一天的OrOrderOfSMS所有记录
            String hql = "delete OrOrderOfSMS ";
            orderService.delete(hql);
        }
        String date = DateUtil.dateToString(new Date()); // 如:2008-3-1

        // date="2008-3-1"; //测试所用到日期
        List<OrOrderOfSMS> orderlistall = orderService.getOrderforTrigger(date); // 根据日期获得当天符合条件的订单
        log.debug("根据日期获得当天符合条件的订单orderlistall的个数为:" + orderlistall.size());

        List orderexist = orderService.listAll(OrOrderOfSMS.class); // 从数据库中获取已发送的订单
        log.debug("从数据库中获取已发送的订单orderexist的个数为:" + orderexist.size());

        // 去掉orderlistall 中与orderexist相同的订单
        List<OrOrderOfSMS> orderlist = removeDuplicate(orderlistall, orderexist);
        log.debug("去掉orderlistall 中与orderexist相同的订单 剩余的订单orderlist的个数:" + orderlist.size());
        log.info("根据日期获得当天符合条件的订单orderlist的个数为:" + orderlist.size());

        if (0 < orderlist.size()) {
            for (OrOrderOfSMS orordersms : orderlist) {
                if (null != orordersms) {
                   String mobile = orordersms.getMobile();
                    if (null != mobile && !"".equals(mobile)) {
                        // to do 发送短信
                        try {
                            orordersms.setSendDate(new Date()); // 发送日期
                            SendSMSToCus(orordersms);
                        } catch (Exception e) {
                            log.error(e);
                            continue;
                        }
                        orderService.saveOrUpdateOrderSMS(orordersms);
                    }
                } else {
                    log.info("orordersms 为空");
                }
            }
            log.info("发送通知短信完成!!!");
        } else {
            log.info("无符合条件的短信可进行发送");
        }
    }

    /**
     * 删除List 相同的部分 add by shizhongwen 时间:Jan 14, 2009 10:22:35 AM
     * 
     * @param listall
     *            原list
     * @param listexist
     *            要比较的listexist 去掉list 中与listexist相同的数据
     * @return
     */
    public List<OrOrderOfSMS> removeDuplicate(List listall, List listexist) {
        List<OrOrderOfSMS> temp = new ArrayList<OrOrderOfSMS>();

        for (int y = 0; y < listall.size(); y++) {
            boolean tag = true;
            OrOrderOfSMS allsms = (OrOrderOfSMS) listall.get(y);
            for (int i = 0; i < listexist.size(); i++) {
                OrOrderOfSMS existsms = (OrOrderOfSMS) listexist.get(i);
                if (existsms.getID().equals(allsms.getID())) {
                    tag = false;
                    break;
                }
            }
            if (tag) {
                temp.add(allsms);
            }
        }
        return temp;
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 发送短信给客户 add by shizhongwen 时间:Jan 15, 2009 5:12:56 PM
     * 
     * @param orordersms
     */
    public void SendSMSToCus(OrOrderOfSMS orordersms) {
        StringBuffer smsTxtBuf = new StringBuffer();
        
        if(null != orordersms){
        	OrOrder order = orderService.findOrOrder(orordersms.getID());
        	boolean isPayAndNotSurety = "pay".equals(order.getPayMethod()) && !order.isCreditAssured();
        	String aliasId = orderService.getMemberAliasId(order.getMemberCd());
        	smsTxtBuf.append("芒果网提示您：您今天将入住 " + orordersms.getHotelName()+order.getRoomQuantity()+"间"+order.getRoomTypeName()+"");
        	String confirmNum = orderService.getConfirmNum(orordersms.getID());//酒店确认号 add at 2013-3-26 17:52:13
        	if(StringUtil.isValidStr(confirmNum)){
        		smsTxtBuf.append(",酒店确认号("+confirmNum+")");
        	}
        	if(isPayAndNotSurety){//面付非担保的发送带随机码的取消短信，其他的只发送入住提醒 changed by luoguangming 2012年3月31日16:49:32
        		String smsStr = orSMSService.getSendStr(order.getID(), aliasId);
        		if(smsStr==null) return;//null表示生成验证码失败
        		if(order.isCooperateOrder()){
                	smsTxtBuf.append(", 如果不能入住即回短信“"+smsStr+"”;晚到或修改请电4006785167");
                }else{
                	smsTxtBuf.append(", 如果不能入住即回短信“"+smsStr+"”;晚到或修改请电4006640066");	
                }
        	}
        	smsTxtBuf.append(".祝您旅途愉快！");
        	
        	String smstext = smsTxtBuf.toString();
            // 调用发送短信接口发送短信
            if (StringUtil.isValidStr(smstext)) {
            	if(OrderType.TYPE_MANGO == order.getType()){
            		this.sendMessageOrSMS(orordersms.getMobile(), smstext);
            	}else if(OrderType.TYPE_114 == order.getType()){
            		this.sendMessageOrSMSByYswp(orordersms.getMobile(), smstext);
            	}
            } else {
                log.error("短信内容为空!!!");
            }
        } else {
            log.error("orordersms 对象为空!!! ");
        }

    }
    
	/**
     * 调用发送短信接口 add by shizhongwen 时间:Jan 15, 2009 4:30:52 PM
     * 
     * @param phoneNo
     * @param smsText
     */
    private void sendMessageOrSMS(String phoneNo, String smsText) {
        Sms sms = new Sms();
        sms.setApplicationName("hotel");
        sms.setTo(new String[] { phoneNo });
        sms.setMessage(smsText);
        sms.setFrom("客服");
        if (null != communicaterService) {
            communicaterService.sendSms(sms);
            log.info("发送通知短信: phone:" + phoneNo + " message:" + smsText);
        } else {
            log.error("communicaterService 短信接口为空,不能发送短信");
        }
    }
    
    /**
     * 短信平台的接口
     * @param phoneNo
     * @param smsText
     * @return
     */
    private Long sendMessageOrSMSByYswp(String phoneNo, String smsText) {
    	
    	Long reRS = -1L;
    	
    	MsgRequest msgRequest = new MsgRequest();
    	try {
	    		msgRequest.setMobile(phoneNo);
	    		msgRequest.setContent(smsText);
	    		msgRequest.setSource("114");
	    		MsgResponse mResponse = mgMsgInterface.sendMsg(msgRequest);
	    		if(mResponse!=null && mResponse.getSpid()!=null && !"".equals(mResponse.getSpid())){
	    			reRS = Long.parseLong(mResponse.getSpid().replace(",", ""));
	    		}else{
	    			reRS = -1L;
	    		}
    		} catch (Exception e) {
    			reRS = -1L;
    			e.printStackTrace();
    		}   
    	return reRS;
    
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }
    
    public void setMgMsgInterface(IMsgService mgMsgInterface) {
		this.mgMsgInterface = mgMsgInterface;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

	public IOrSMSService getOrSMSService() {
		return orSMSService;
	}

	public void setOrSMSService(IOrSMSService orSMSService) {
		this.orSMSService = orSMSService;
	}

}
