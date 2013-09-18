package com.mangocity.hotel.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.util.log.MyLog;

public class HotelAllInfoSchedule extends QuartzJobBean{
	private HotelManageWeb hotelManageWeb;
	private static final MyLog log = MyLog.getLogger(HotelAllInfoSchedule.class);
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		List list=hotelManageWeb.queryAllHotleActive();
		wirteFile(queryAllHotelInfo(list));
	}
	
	private void wirteFile(String content){
        OutputStreamWriter output=null;
        try {
            output = new OutputStreamWriter(
                    new FileOutputStream( InitServlet.saveHWEBpath+ "/HotelWebAllInfo.js"),"UTF-8");
            output.write(content);
        } catch (IOException e) {
        	log.error("js write io error:"+e);
        }finally{
            try {
                if(null != output){
                    output.close();
                }
            } catch (IOException e) {
                log.error("js write close stream error:"+e);
            }
        }
	}
	
	 private String queryAllHotelInfo(List list) {
	    	StringBuilder sb=new StringBuilder();
			sb.append("var hotelInfos = new Array();");
			for(int i=0;i<list.size();i++){
				Object[] hotel=(Object[]) list.get(i);
				sb.append("hotelInfos[").append(i).append("]=");
				sb.append("new Array('").append(hotel[0]).append("','")
				.append(hotel[1]).append("','")
				.append(hotel[2]).append("','")
				.append(hotel[3]).append("');");
			}
			return sb.toString();
		}
	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}
	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}
	
	
	

}
