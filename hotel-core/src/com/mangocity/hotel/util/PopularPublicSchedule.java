package com.mangocity.hotel.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HwHotelIndex;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.log.MyLog;

/**
 */
public class PopularPublicSchedule extends QuartzJobBean {

	private static final MyLog log = MyLog.getLogger(PopularPublicSchedule.class);

	private HotelManageWeb hotelManageWeb;
    
    private SystemDataService systemDataService;

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

        Double dsleepNumber = 1000 + Math.random() * 9000;
        try {
            Thread.sleep(dsleepNumber.longValue());
        } catch (Exception e) {
            log.error("Thread.sleep error" + e);
        }

        OrParam param = systemDataService.getSysParamByName("save_popular");

        Date now_main = new Date();
        log.info("time = now_main.getTime()- param.getModifyTime().getTime()==="
            + (now_main.getTime() - param.getModifyTime().getTime()));
        if (30000 > (now_main.getTime() - param.getModifyTime().getTime())) {
            log.info(" set getPopularMode = false  in PopularPublicSchedule.executeInternal()");
            return;
        } else {
            param.setModifyTime(now_main);
            systemDataService.updateSysParamByName(param);
            log.info(" set getPopularMode = true  in PopularPublicSchedule.executeInternal()");
        }

        StringBuffer buffer = new StringBuffer();
        Map resultMap = hotelManageWeb.queryHwHotelIndexItems("popular");
        if (null == resultMap) {
            log.error("hwManage.queryHwHotelIndexItems('popular')== null ");
            return;
        } else {
            // 组装数据，构建JS文件的内容
            List clickNumList = (List) resultMap.get("clickNum");

            // 最畅销栏
            buffer.append("var htlPopularArray = new Array();\n");
            for (int k = 0; k < clickNumList.size(); k++) {
                HwHotelIndex item = (HwHotelIndex) clickNumList.get(k);
                String chnName = "";
                if (12 < item.getChnName().length()) {
                    chnName = item.getChnName().substring(0, 12) + "...";
                } else {
                    chnName = item.getChnName();
                }
                /** v2.4.2 非人民币逢一进十，人民币小数部分无效也逢一进十 by chenjiajie@2009-01-12 begin **/
                buffer.append("htlPopularArray[" + k + "] = " + "new Array('" + item.getCity()
                    + "' , '" + item.getHotelId() + "' , '" + chnName + "' , '"
                    + item.getHotelStar() + "' , '" + item.getCurrency() + "' , '");
                int price = Double.valueOf(item.getLowestPrice()).intValue();
                // v2.4.2非人民币统一逢一进十
                if (!item.getCurrency().equals(CurrencyBean.RMB)) {
                    buffer.append(price);
                } else {
                    // v2.4.2假如是人民币，并且小数部分非零，原版输出小数，否则逢一进十
                    if (Double.valueOf(item.getLowestPrice())// parasoft-suppress PB.DCF "暂不修改"
                        .intValue()== item.getLowestPrice()) {// parasoft-suppress PB.DCF "暂不修改"
                        buffer.append(price);
                    } else {
                        buffer.append(item.getLowestPrice());
                    }
                }
                buffer.append("');\n");
                /** v2.4.2 非人民币逢一进十，人民币小数部分无效也逢一进十 by chenjiajie@2009-01-12 end **/
            }
            OutputStream os = null;
            OutputStreamWriter osw =  null;
            BufferedWriter bfw =  null;
            try {
                // 指定生成文件及路径
                os = new FileOutputStream(InitServlet.saveHWEBpath
                    + "/hwHtlPopularPublic.js");
                // 创建使用指定字符集UTF-8的writer
                osw = new OutputStreamWriter(os, "UTF-8");
                bfw = new BufferedWriter(osw);
                // 新String对象使用UTF-8来解码字节数组，以UTF-8编码输出文件
                bfw.write(new String(buffer.toString().getBytes("UTF-8"), "UTF-8"));
                bfw.flush();
                bfw.close();
                buffer.delete(0, buffer.length() - 1);
                log.info(" hwHtlPopularPublic.js file is OK!!");
                // count ++ ;
                return;
            } catch (IOException e) {
                log.error("PopularPublicSchedule 新建文件操作出错 : " + e.toString());
                return;
            }finally{
                try {
                    bfw.close();
                    osw.close();
                    os.close();
                } catch (IOException e) {
                	log.error(e.getMessage(),e);
                }
            }

        }

    }

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

}
