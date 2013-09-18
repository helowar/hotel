package com.mangocity.hotel.sendmessage.job;

import java.util.Date;
import java.util.Random;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.sendmessage.service.AbstractSendMessageSystemDataService;
import com.mangocity.hotel.sendmessage.service.SendPromotionMessageService;
import com.mangocity.util.log.MyLog;

public class SendPromotionMessageJob{

	private AbstractSendMessageSystemDataService sendPMsgSystemDataService;
	private SendPromotionMessageService sendPromotionMessageService;
	private static final MyLog log = MyLog.getLogger(SendPromotionMessageJob.class);

	protected void execute(){
		OrParam orParam = new OrParam();
		try {
			log.info("发送促销信息定时器begin！ ");
			int i = new Random().nextInt(1000);
			Thread.sleep(i);
			
			orParam = sendPMsgSystemDataService.getSendPMsgSystemData();
			//给orParam的sequence生成下一个值，更新当前生成的sequence到param的value值中
			String nextSeq = sendPromotionMessageService.getJobSendPromotionSeqNextVal()+"";
			orParam.setValue(nextSeq);
			orParam.setModifyTime(new Date());
			sendPMsgSystemDataService.updateSendPMsgSystemData(orParam);
			log.info("当前线程sleep "+i+" 毫秒;获取的sequence="+nextSeq+";当前线程再sleep 5 秒！");
			//sleep 10 秒，等所有集群服务器update之后，再读取param，如果value值等于当前生成的sequence,执行短信取消，否则返回
			Thread.sleep(10000);
			orParam = sendPMsgSystemDataService.getSendPMsgSystemData();
			if(nextSeq.equals(orParam.getValue())){			
				sendPromotionMessageService.sendPromotionMessage();
			}else{
				log.info("发送促销信息定时器在执行中");
				return;
			}

			
		} catch (Exception e) {
			log.error("发送促销信息定时器发生异常", e);
		}
		
		finally{
			log.info("发送促销信息定时器 end！ ");
		}
	}
	
	
	public void setSendPMsgSystemDataService(AbstractSendMessageSystemDataService sendPMsgSystemDataService) {
		this.sendPMsgSystemDataService = sendPMsgSystemDataService;
	}


	public void setSendPromotionMessageService(SendPromotionMessageService sendPromotionMessageService) {
		this.sendPromotionMessageService = sendPromotionMessageService;
	}

	
}
