package com.mangocity.hotel.util;

import java.util.Date;
import java.util.Random;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.order.service.IOrSMSService;
import com.mangocity.util.log.MyLog;
/**
 * 短信取消订单 定时器
 * @author 
 */
public class NoteJob extends QuartzJobBean{
	private static final MyLog log = MyLog.getLogger(NoteJob.class);
	private IOrSMSService orSMSService;
	private SystemDataService systemDataService;
	/**
	 * 每5分钟刷取取消订单的短信，
	 * 根据验证码、日期、手机号来进行取消订单操作
	 * @param args
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		OrParam orParam = new OrParam();
		try {
			log.info("短信取消定时器begin！ ");
			int i = new Random().nextInt(1000);
			Thread.sleep(i);
			
			orParam = systemDataService.getSysParamByName("IS_CANCEL_ORDER_FROM_SMS");
			//给orParam的sequence生成下一个值，更新当前生成的sequence到param的value值中
			String nextSeq = orSMSService.getOrParamSeqNextVal("SEQ_HTL_JOB_AUTOCANCEL")+"";
			orParam.setValue(nextSeq);
			orParam.setModifyTime(new Date());
			systemDataService.updateSysParamByName(orParam);
			log.info("当前线程sleep "+i+" 毫秒;获取的sequence="+nextSeq+";当前线程再sleep 5 秒！");
			//sleep 10 秒，等所有集群服务器update之后，再读取param，如果value值等于当前生成的sequence,执行短信取消，否则返回
			Thread.sleep(10000);
			orParam = systemDataService.getSysParamByName("IS_CANCEL_ORDER_FROM_SMS");
			if(nextSeq.equals(orParam.getValue())){
				log.info("--抢到了定时器，开始执行");
				orSMSService.work();
			}else{
				log.info("---短信取消定时器已在执行中！");
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			log.info("短信取消定时器end！ ");
		}
	}
	public IOrSMSService getOrSMSService() {
		return orSMSService;
	}
	public void setOrSMSService(IOrSMSService orSMSService) {
		this.orSMSService = orSMSService;
	}
	public SystemDataService getSystemDataService() {
		return systemDataService;
	}
	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}
	
}