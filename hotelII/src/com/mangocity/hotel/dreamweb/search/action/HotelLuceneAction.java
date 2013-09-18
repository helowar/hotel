package com.mangocity.hotel.dreamweb.search.action;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.dreamweb.util.action.GenericWebAction;
import com.mangocity.hotel.search.index.HotelInfoIndexer;
import com.mangocity.hotel.search.searchengine.service.IndexBuilderService;

public class HotelLuceneAction extends GenericWebAction {		
	
	private static final long serialVersionUID = 482446547733332681L;

	private static final Log log = LogFactory.getLog(HotelLuceneAction.class);
	
	private HotelInfoIndexer hotelInfoIndexerForUpdate;
	
	private IndexBuilderService indexBuilderService;
	
	private SystemDataService systemDataService;
	
	private String from;
	
	private String task;
	
	public String execute(){
		
		if(!"HBIZ".equals(from)) {
			return super.forwardError("");
		}

		if(!"read".equals(task)) {
			return build();
		} else { // 读取
			return read();
		}
	}
	
	private String read() {
		//indexBuilderService.reload();
		return super.forwardError("重新读取盗梦网站的lucene文件成功!");
	}
	
	private String build() {
//		OrParam orParam = systemDataService.getSysParamByName("UPDATE_LUCENE");
//
//        Date newDate = new Date();
//        if (null == orParam) { // 如果从表中取出为空,则赋值
//        	orParam = new OrParam();
//        	orParam.setName(AutoSendSMSConstant.NAME);
//        	orParam.setValue(AutoSendSMSConstant.SENDWORKING);
//        	orParam.setModifyTime(newDate);
//        	systemDataService.updateSysParamByName(orParam); // 更数数据
//        } else {
//            if (10 * 60000 > (newDate.getTime() - orParam.getModifyTime().getTime())) {
//                // 如果当前日期与param的时间之差小于25分钟,则是集群重新发送,
//                // 采用不响应方式
//            	//return super.forwardError("最近一次开始生成lucene文件时间为：" + orParam.getModifyTime() + ", 请在距离该时间25分钟后再尝试.");
//            	return super.forwardError("系统正在升级！一般升级时间为30分钟，请稍后再试！");
//            } else {
//            	orParam.setValue(AutoSendSMSConstant.SENDWORKING);
//            	orParam.setModifyTime(newDate);
//            	systemDataService.updateSysParamByName(orParam); // 更数数据
//            }
//        }
//		
//		
//		if(1 > InitServlet.businessSozeObj.size()) {
//			log.info("InitServlet尚未启动");
//			return super.forwardError("InitServlet尚未启动");
//		}
		
//		boolean bInit = hotelInfoIndexerForUpdate.testInit();		
//		if(!bInit) {
//			log.info("lucene索引文件可能已被锁住");
//			return super.forwardError("lucene索引文件可能已被锁住");			
//		}
		
		(new BuildLuceneThread()).start();
		
		return super.forwardError("已开始创建lucene索引文件，可能需要花15-20分钟左右，请耐心等待。");		
	}
	
	private class BuildLuceneThread extends Thread {
		@Override
		public void run() {
			log.info("开始创建lucene索引文件");
			long lBegin = System.currentTimeMillis();
			
		//	hotelInfoIndexerForUpdate.createHotelInfo2Index();
					
			log.info("创建lucene索引文件共花 : "	+ (System.currentTimeMillis() - lBegin) + "毫秒");
		}
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setHotelInfoIndexerForUpdate(
			HotelInfoIndexer hotelInfoIndexerForUpdate) {
		this.hotelInfoIndexerForUpdate = hotelInfoIndexerForUpdate;
	}

	public void setIndexBuilderService(IndexBuilderService indexBuilderService) {
		this.indexBuilderService = indexBuilderService;
	}
	
}
