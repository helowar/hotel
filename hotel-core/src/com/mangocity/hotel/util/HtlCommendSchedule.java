package com.mangocity.hotel.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HwHotelIndex;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.log.MyLog;

/**
 * 网站首页特推酒店
 * 
 * @author xuyiwen
 * 
 */
public class HtlCommendSchedule extends QuartzJobBean {
	private HotelManageWeb hotelManageWeb;

	private static final MyLog log = MyLog.getLogger(HtlCommendSchedule.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		
		StringBuilder builder = new StringBuilder();

		List<HwHotelIndex> hwHotelIndexList= hotelManageWeb.queryTTHtlData();

		if (null == hwHotelIndexList) {
			return;
		} else {
			// 组装数据，构建JS文件的内容

			// 推荐栏
			builder.append("var htlCommends = new Array();\n");
			for (int j = 0; j < hwHotelIndexList.size(); j++) {
				HwHotelIndex item = (HwHotelIndex) hwHotelIndexList.get(j);
				String chnName = "";
				if (12 < item.getChnName().length()) {
					chnName = item.getChnName().substring(0, 12) + "...";
				} else {
					chnName = item.getChnName();
				}
				builder.append("htlCommends[" + j + "] = " + "new Array('" + item.getCity() + "' , '"
						+ item.getHotelId() + "' , '" + chnName + "' , '" + item.getHotelStar() + "' , '"
						+ item.getCurrency() + "' , '");
				int price = Double.valueOf(item.getLowestPrice()).intValue();
				// v2.4.2非人民币统一逢一进十
				if (!item.getCurrency().equals(CurrencyBean.RMB)) {
					builder.append(price);
				} else {
					// v2.4.2假如是人民币，并且小数部分非零，原版输出小数，否则逢一进十
					if (Double.valueOf(item.getLowestPrice())
					.intValue() == item.getLowestPrice()) {
						builder.append(price);
					} else {
						builder.append(item.getLowestPrice());
					}
				}
				double lowestFavPrice = item.getLowestFavPrice();
				builder.append("','" + Double.valueOf(lowestFavPrice).intValue()); // 增加最低返现
				builder.append("');\n");
			}
			builder.append("\n\n");
		}
		log.info("HtlCommendSchedule:网站首页特推酒店定时执行,生成htlCommend.js文件");
		String file = InitServlet.saveHWEBpath+"/htlCommend.js";
		writeFile(file,builder);
	}
	
	private void writeFile(String file,StringBuilder builder){
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		PrintWriter pw = null;
		//用文件流写入js文件中
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos,"UTF-8");
			pw = new PrintWriter(osw);
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

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}
}
