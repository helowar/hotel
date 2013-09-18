package com.mangocity.hotel.schedule;



import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.dreamweb.comment.service.IDaoDaoCommentService;
import com.mangocity.util.log.MyLog;

/***
 * @author panjianping
 * 操作到到网点评基本信息的定时器
 * 执行从下载xml文件、解析xml文件、把解析的对象同步到数据库中的一些列完成的过程
 *
 */
public class HotelCommentDaoDaoToDBSchedule extends QuartzJobBean {
    
	private final MyLog log = MyLog.getLogger(this.getClass());
	private IDaoDaoCommentService daoDaoCommentService;    
	private SystemDataService systemDataService;
	
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		OrParam orParam =systemDataService.getSysParamByName("IS_DAODAO_TASK_EXECUTIG");
		if("0".equals(orParam.getValue())){
			try{
				orParam.setValue("1");
				systemDataService.updateSysParamByName(orParam);
				daoDaoCommentService.updateHtlCommentDaoDaoTable(); 
			}catch(Exception e){
				log.error("HotelCommentDaoDaoToDBSchedule executeInternal:", e);
			}finally{
							
			}
			
		}	    
	}

	public IDaoDaoCommentService getDaoDaoCommentService() {
		return daoDaoCommentService;
	}

	public void setDaoDaoCommentService(IDaoDaoCommentService daoDaoCommentService) {
		this.daoDaoCommentService = daoDaoCommentService;
	}

	public SystemDataService getSystemDataService() {
		return systemDataService;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

		   
	
}
