package com.mangocity.webnew.service.impl;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.time.DateUtils;

public class TaskManager implements ServletContextListener{
	/**                                
	  * 每天的毫秒数                                
	  */                                
	 public static final long PERIOD_HOUR = DateUtils.MILLIS_PER_HOUR;                                
	                            
	 /**                                
	  * 无延迟                                
	  */                                
	 public static final long NO_DELAY = 0;
	 /**                                
	  * 定时器                                
	  */                                
	 private Timer timer;  
	 
	public void contextDestroyed(ServletContextEvent arg0) {
		if(null!=timer){
			timer.cancel();
		}

	}

	public void contextInitialized(ServletContextEvent arg0) {
		
		timer = new Timer("删作临时文件",true);    
		// 360000*12 定时一天删除一次
		timer.schedule(new DeleteTempFileTask(arg0.getServletContext().getRealPath("/upload/emap")),NO_DELAY, 360000*12);
	}



}
