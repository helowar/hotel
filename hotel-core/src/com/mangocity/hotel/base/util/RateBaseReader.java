package com.mangocity.hotel.base.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.outservice.IRateService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.util.log.MyLog;

/**
 * 定时获取外币对人民币的汇率
 * 
 * @author kun.chen
 */
public class RateBaseReader extends QuartzJobBean implements Serializable {

    private IRateService rateService;

    private String rateValue;

    private Map map = new HashMap();

    private Map currencyMap = new HashMap();

    private static final MyLog log = MyLog.getLogger(RateBaseReader.class);

    public static Map rateMap = new HashMap();

    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        // 获取基础数据
        map = InitServlet.mapHotelCommmonObj;
        // 从基础数据获取所有货币
        currencyMap = (HashMap) map.get("currency");

        Object currencyObj;
        Set setObj = currencyMap.keySet();

        Iterator it = setObj.iterator();
        String curreryCode = "";
        while (it.hasNext()) {
            currencyObj = it.next();
            if (null != currencyObj) {
                curreryCode = (String) currencyObj;// currencyObj就是货币代码
                if (null != curreryCode && !curreryCode.equalsIgnoreCase("RMB")) {
                    rateValue = rateService.getRate("RMB", curreryCode);
                    rateMap.put(curreryCode, rateValue);
                    log.info("RateBaseReader info :curreryCode===" + curreryCode
                        + "---ratevalue=====" + rateValue);
                }
            }
        }
    }

    public IRateService getRateService() {
        return rateService;
    }

    public void setRateService(IRateService rateService) {
        this.rateService = rateService;
    }

}
