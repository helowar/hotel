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
public class MainPublicSchedule extends QuartzJobBean {
	
    private static final MyLog log = MyLog.getLogger(MainPublicSchedule.class);

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

        OrParam param = systemDataService.getSysParamByName("save_main");

        Date now_main = new Date();
        log.info("time = now_main.getTime()- param.getModifyTime().getTime()==="
            + (now_main.getTime() - param.getModifyTime().getTime()));
        if (30000 > (now_main.getTime() - param.getModifyTime().getTime())) {
            log.info(" set saveMainCan = false  in MainPublicSchedule.executeInternal()");
            return;
        } else {
            param.setModifyTime(now_main);
            systemDataService.updateSysParamByName(param);
            log.info(" set saveMainCan = true  in MainPublicSchedule.executeInternal()");
        }

        StringBuffer buffer = new StringBuffer();
        Map resultMap = hotelManageWeb.queryHwHotelIndexItems("public");
        if (null == resultMap) {
            return;
        } else {
            // 组装数据，构建JS文件的内容
            List lowestPriceList = (List) resultMap.get("lowestPrice");
            List hotelCommendList = (List) resultMap.get("hotelCommend");
            List orderNumList = (List) resultMap.get("orderNum");

            // 今日低价栏
            buffer.append("var htlLowestPriceArray = new Array();\n");
            for (int i = 0; i < lowestPriceList.size(); i++) {
                HwHotelIndex item = (HwHotelIndex) lowestPriceList.get(i);
                /** v2.4.2 非人民币逢一进十，人民币小数部分无效也逢一进十 by chenjiajie@2009-01-12 begin * */
                buffer.append("htlLowestPriceArray[" + i + "] = " + "new Array('" + item.getCity()
                    + "' , '" + item.getCurrency() + "' , '");
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
                buffer.append("'");
                /** v2.4.2 非人民币逢一进十，人民币小数部分无效也逢一进十 by chenjiajie@2009-01-12 end * */
                int star = Integer.parseInt(item.getHotelStar());
                if (29 >= star) {// 5星级
                    buffer.append(",'" + 5 + "');\n");
                } else if (49 >= star) {
                    buffer.append(",'" + 4 + "');\n");
                } else if (64 >= star) {
                    buffer.append(",'" + 3 + "');\n");
                } else if (79 >= star) {
                    buffer.append(",'" + 2 + "');\n");
                }
            }
            buffer.append("\n\n");

            // 推荐栏
            buffer.append("var htlCommendArray = new Array();\n");
            for (int j = 0; j < hotelCommendList.size(); j++) {
                HwHotelIndex item = (HwHotelIndex) hotelCommendList.get(j);
                String chnName = "";
                if (12 < item.getChnName().length()) {
                    chnName = item.getChnName().substring(0, 12) + "...";
                } else {
                    chnName = item.getChnName();
                }
                /** v2.4.2 非人民币逢一进十，人民币小数部分无效也逢一进十 by chenjiajie@2009-01-12 begin * */
                buffer.append("htlCommendArray[" + j + "] = " + "new Array('" + item.getCity()
                    + "' , '" + item.getHotelId() + "' , '" + chnName + "' , '"
                    + item.getHotelStar() + "' , '" + item.getCurrency() + "' , '");
                int price = Double.valueOf(item.getLowestPrice()).intValue();
                // v2.4.2非人民币统一逢一进十
                if (!item.getCurrency().equals(CurrencyBean.RMB)) {
                    buffer.append(price);
                } else {
                    // v2.4.2假如是人民币，并且小数部分非零，原版输出小数，否则逢一进十
                    if (Double.valueOf(item.getLowestPrice())// parasoft-suppress PB.DCF "暂不修改"
                        .intValue() == item.getLowestPrice()) {// parasoft-suppress PB.DCF "暂不修改"
                        buffer.append(price);
                    } else {
                        buffer.append(item.getLowestPrice());
                    }
                }
                double lowestFavPrice = item.getLowestFavPrice();
                buffer.append("','"+Double.valueOf(lowestFavPrice).intValue()); //增加最低返现 add by xuyiwen 2011-3-18
                buffer.append("');\n");
                /** v2.4.2 非人民币逢一进十，人民币小数部分无效也逢一进十 by chenjiajie@2009-01-12 end * */
            }
            buffer.append("\n\n");

            // 最畅销栏
            buffer.append("var htlBestSellArray = new Array();\n");
            for (int k = 0; k < orderNumList.size(); k++) {
                HwHotelIndex item = (HwHotelIndex) orderNumList.get(k);
                String chnName = "";
                if (12 < item.getChnName().length()) {
                    chnName = item.getChnName().substring(0, 12) + "...";
                } else {
                    chnName = item.getChnName();
                }
                /** v2.4.2 非人民币逢一进十，人民币小数部分无效也逢一进十 by chenjiajie@2009-01-12 begin * */
                buffer.append("htlBestSellArray[" + k + "] = " + "new Array('" + item.getCity()
                    + "' , '" + item.getHotelId() + "' , '" + chnName + "' , '"
                    + item.getHotelStar() + "' , '" + item.getCurrency() + "' , '");
                int price = Double.valueOf(item.getLowestPrice()).intValue();
                // v2.4.2非人民币统一逢一进十
                if (!item.getCurrency().equals(CurrencyBean.RMB)) {
                    buffer.append(price);
                } else {
                    // v2.4.2假如是人民币，并且小数部分非零，原版输出小数，否则逢一进十
                    if (Double.valueOf(item.getLowestPrice())// parasoft-suppress PB.DCF "暂不修改"
                        .intValue() == item.getLowestPrice()) {// parasoft-suppress PB.DCF "暂不修改"
                        buffer.append(price);
                    } else {
                        buffer.append(item.getLowestPrice());
                    }
                }
                buffer.append("');\n");
                /** v2.4.2 非人民币逢一进十，人民币小数部分无效也逢一进十 by chenjiajie@2009-01-12 end * */
            }

            OutputStream os = null;
            OutputStreamWriter osw = null;
            BufferedWriter bfw = null;
            try {
                // 指定生成文件及路径
                os = new FileOutputStream(InitServlet.saveHWEBpath + "/hwHtlMainPublic.js");
                // 创建使用指定字符集UTF-8的writer
                osw = new OutputStreamWriter(os, "UTF-8");
                bfw = new BufferedWriter(osw);
                // 新String对象使用UTF-8来解码字节数组，以UTF-8编码输出文件
                bfw.write(new String(buffer.toString().getBytes("UTF-8"), "UTF-8"));
                bfw.flush();
                bfw.close();
                buffer.delete(0, buffer.length() - 1);
                return;
            } catch (IOException e) {
                log.error("MainPublicSchedule 新建文件操作出错 : " + e.toString());
                return;
            } finally {
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
