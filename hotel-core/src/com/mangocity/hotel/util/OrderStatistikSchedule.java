package com.mangocity.hotel.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.util.log.MyLog;

/**
 * 为电商统计网站来源订单用 每十分钟执行一次 写js文件
 * @author xuyiwen
 *
 */
public class OrderStatistikSchedule extends QuartzJobBean{
	
	 private static final MyLog log = MyLog.getLogger(OrderStatistikSchedule.class);

	 private HotelManageWeb hotelManageWeb;
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		Long netOrders = hotelManageWeb.queryNetOrderStatistik();
		log.info("OrderStatistikSchedule--------来源于网站订单量："+netOrders);
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		PrintWriter pw = null;
		//用文件流写入js文件中
		try {
			fos = new FileOutputStream(InitServlet.saveHWEBpath+"/netOrderStatistik.js");
			osw = new OutputStreamWriter(fos,"UTF-8");
			pw = new PrintWriter(osw);
			StringBuilder builder = new StringBuilder(" var netOrderCount = ");
			builder.append(netOrders).append(";");
			pw.println(new String(builder.toString().getBytes("UTF-8"),"UTF-8"));
			pw.flush();
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}finally{
			if(pw != null) pw.close();
			if(osw != null)
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
		}
	}
	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}
	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}
}
