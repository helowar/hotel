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
			log.info("ep����ͬ��CC����״̬��ʱ��begin�� ");
			int i = new Random().nextInt(1000);
			Thread.sleep(i);
			
			orParam = systemDataService.getSysParamByName("SYN_EP_ORDERSTATE_TO_ORDER");
			//��orParam��sequence������һ��ֵ�����µ�ǰ���ɵ�sequence��param��valueֵ��
			String nextSeq = synEpOrderService.getOrParamSeqNextVal("SEQ_HTL_SYNEPORDER")+"";
			orParam.setValue(nextSeq);
			orParam.setModifyTime(new Date());
			systemDataService.updateSysParamByName(orParam);
			log.info("��ǰ�߳�sleep "+i+" ����;��ȡ��sequence="+nextSeq+";��ǰ�߳���sleep 5 �룡");
			//sleep 10 �룬�����м�Ⱥ������update֮���ٶ�ȡparam�����valueֵ���ڵ�ǰ���ɵ�sequence,ִ��ep����ͬ��CC����״̬�����򷵻�
			Thread.sleep(10000);
			orParam = systemDataService.getSysParamByName("SYN_EP_ORDERSTATE_TO_ORDER");
			if(nextSeq.equals(orParam.getValue())){
				log.info("--�����˶�ʱ������ʼִ��");
				List<EpOrder> list = synEpOrderService.queryHotelConfirmedEPOrder(5);
				if(null != list && !list.isEmpty()){
					for(EpOrder ep : list){
						synEpOrderService.synEpOrderHandler(ep);	
					}
				}
			}else{
				log.info("---ep����ͬ��CC����״̬��ʱ������ִ���У�");
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			log.info("ep����ͬ��CC����״̬��ʱ��end�� ");
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
