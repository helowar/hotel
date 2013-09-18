package com.mangocity.util;

import java.math.BigDecimal;
import java.util.Map;

import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;

/**
 * 
 * 对money 进行处理
 * 
 * @author:shizhongwen 创建日期:Dec 30, 2008,3:10:52 PM 描述：
 */
public class MoneyHandle {
	private static final MyLog log = MyLog.getLogger(MoneyHandle.class);

    public static String staticrateStr = "1"; // 默认转换率

    /**
     * RMS 2118港澳酒店预付订单金额小数点的处理 ,逢一进十 ,该功能只针对预付，不针对面付/面付担保的情况。 add by shizhongwen 时间:Dec 30,
     * 2008:3:11:18 PM
     * 
     * @param money
     * @return
     */
    public static int getMoneyOfInteger(double money) {
        return Double.valueOf(Math.ceil(money)).intValue();

    }

    /**
     * 
     * 根据currencyCompare 合同币种 将 money 转换成RMB金钱 (为双精度型) add by shizhongwen 时间:Dec 31, 2008:10:48:57
     * AM
     * 
     * @param currencyCompare
     *            合同币种
     * @param money
     *            所要转换的金钱
     * @return double
     */
    public static double getDoubleRMBbyAnyMoney(String currencyCompare, double money) {
        Map<String, String> rateExchangeMap = CurrencyBean.rateMap;
        String rateStr = ""; // 转换率
        if (null != rateExchangeMap) {
            rateStr = rateExchangeMap.get(currencyCompare);
        } else {
            rateStr = staticrateStr;
            log.error("get rateExchange ERROR:rateExchangeMap is null ");
        }
        if (null == rateStr || rateStr.equals("")) {
            rateStr = staticrateStr;
            log.error("get rateExchange ERROR:NO such currency in DB");
        }

        double ratedoures = BigDecimal.valueOf(money).multiply(new BigDecimal(rateStr.trim()))
            .doubleValue();
        return ratedoures;
    }

    /**
     * 根据合同币种currencyCompare 将 money 转换成RMB金钱 (为整型) 针对(港澳酒店预付订单金额小数点的处理)采取 逢一进十 如:12.012-->12,
     * 12.122->13 即小数点后第一位数大于0,则整+1 ,否则只须取整 add by shizhongwen 时间:Dec 31, 2008 12:08:35 PM
     * 
     * @param currencyCompare
     * @param money
     * @return
     */
    public static int getIntRMBbyAnyMoney(String currencyCompare, double money) {
        double tranmoney = getDoubleRMBbyAnyMoney(currencyCompare, money);
        int intmoney = getMoneyOfInteger(tranmoney);
        return intmoney;
    }
    
    /**
     * 根据合同币种currencyCompare 将 money 转换成HKD金钱 (为整型) 针对(港澳酒店预付订单金额小数点的处理)采取 逢一进十 如:12.012-->12,
     * 12.122->13 即小数点后第一位数大于0,则整+1 ,否则只须取整 add by shengwei.zuo  2010-9-24
     * 
     * @param currencyCompare
     * @param money
     * @return
     */
    public static int getIntHKDbyAnyMoney(String currencyCompare, double money) {
        double tranmoney = CurrencyBean.getRateToHKD(currencyCompare,money);
        int intmoney = getMoneyOfInteger(tranmoney);
        return intmoney;
    }
    

    /**
     * 根据合同币种currencyCompare 将 money*nday 转换成RMB金钱 (为整型) 针对(港澳酒店预付订单金额小数点的处理)采取 逢一进十 如:12.012-->12,
     * 12.122->13 即小数点后第一位数大于0,则整+1 ,否则只须取整 add by shizhongwen 时间:Jan 4, 2009 9:50:08 AM
     * 
     * @param currencyCompare
     *            合同币种
     * @param money
     *            单天的金额
     * @param nday
     *            天数
     * @return
     */
    public static int getIntRMBbyAnyMoney(String currencyCompare, double money, int nday) {
        double tranmoney = getDoubleRMBbyAnyMoney(currencyCompare, money * nday);
        int intmoney = getMoneyOfInteger(tranmoney);
        return intmoney;
    }
    
    /**
     * 取整 add by chenjiajie 2009-10-20
     * @param money
     * @return
     */
    public static int getFloorMoney(double money){
    	return (int)Math.floor(money);
    }
    
    /**
     * 香港组紧急需求 支持香港币种支付 用于订单总金额币种显示
     * @param orderPaymentCurrency 订单的合同币种
     * @param acturlPaymentCurrency 订单的实际支付币种
     * @param money 订单的总金额
     * @param payMethod 订单的支付方式 面付/预付
     * @return 返回 显示的币种
     */
    public static String getConvertCurrency(String orderPaymentCurrency
            ,String acturlPaymentCurrency,double money,String payMethod){
        String returnStr = "";
        //面付情况
        if(PayMethod.PAY.equals(payMethod)){
            returnStr = CurrencyBean.idCurMap.get(orderPaymentCurrency);
        }
        //预付情况
        else{
            returnStr = CurrencyBean.idCurMap.get(acturlPaymentCurrency);
        }
        return returnStr;
    }
    
    /**
     * 获得兑换人民币的汇率
     * @param curreny
     * @return
     */
    public static double getCurrenyRate(String curreny){
        String rateStr = "1";
        if(StringUtil.isValidStr(CurrencyBean.rateMap.get(curreny))){
            rateStr = CurrencyBean.rateMap.get(curreny);
        }
        return Double.parseDouble(rateStr);
    }
}
