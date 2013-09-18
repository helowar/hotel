package com.mangocity.hotel.util;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.ep.entity.EpOrder;
import com.mangocity.ep.service.SynEpOrderService;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.util.log.MyLog;

public class SynpOrderSchedule extends QuartzJobBean{
    
	private static final MyLog log = MyLog.getLogger(SynpOrderSchedule.class);
	
	private SynEpOrderService synEpOrderService;
	private SystemDataService systemDataService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		OrParam orParam = new OrParam();
		try {
			log.info("ep订单同步CC订单状态定时器begin！ ");
			int i = new Random().nextInt(1000);
			Thread.sleep(i);
			
			orParam = systemDataService.getSysParamByName("SYN_EP_ORDERSTATE_TO_ORDER");
			//给orParam的sequence生成下一个值，更新当前生成的sequence到param的value值中
			String nextSeq = synEpOrderService.getOrParamSeqNextVal("SEQ_HTL_SYNEPORDER")+"";
			orParam.setValue(nextSeq);
			orParam.setModifyTime(new Date());
			systemDataService.updateSysParamByName(orParam);
			log.info("当前线程sleep "+i+" 毫秒;获取的sequence="+nextSeq+";当前线程再sleep 5 秒！");
			//sleep 10 秒，等所有集群服务器update之后，再读取param，如果value值等于当前生成的sequence,执行ep订单同步CC订单状态，否则返回
			Thread.sleep(10000);
			orParam = systemDataService.getSysParamByName("SYN_EP_ORDERSTATE_TO_ORDER");
			if(nextSeq.equals(orParam.getValue())){
				log.info("--抢到了定时器，开始执行");
				List<EpOrder> list = synEpOrderService.queryHotelConfirmedEPOrder(5);
				if(null != list && !list.isEmpty()){
					for(EpOrder ep : list){
						synEpOrderService.synEpOrderHandler(ep);	
					}
				}
			}else{
				log.info("---ep订单同步CC订单状态定时器已在执行中！");
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			log.info("ep订单同步CC订单状态定时器end！ ");
		}
	}

	public SynEpOrderService getSynEpOrderService() {
		return synEpOrderService;
	}

	public void setSynEpOrderService(SynEpOrderService synEpOrderService) {
		this.synEpOrderService = synEpOrderService;
	}

	public SystemDataService getSystemDataService() {
		return systemDataService;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

}
