package com.mangocity.util.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class CurrencyBean implements Serializable {

    /**
     * 币种汇率
     */
    public static Map<String, String> rateMap = null;

    public static Map<String, String> idCurMap = null;

    static {
        idCurMap = new HashMap<String, String>();
        idCurMap.put("RMB", "RMB");
        idCurMap.put("USD", "USD$");
        idCurMap.put("HKD", "HK$");
        idCurMap.put("EUR", "EUR€");
        idCurMap.put("GBP", "GBP$");
        idCurMap.put("JPY", "JPY￥");
        idCurMap.put("DEM", "DM");
        idCurMap.put("MOP", "MOP");
    }
    // 添加转港币的汇率 add by diandian.hou 2010-9-24
    /**
     * 添加转港币的汇率 
     */
    public static double  getRateToHKD(String currency){
       double rateToHKD = 1.0; 
    	if(RMB.equals(currency)){
    		rateToHKD = 1 / (Double.parseDouble(CurrencyBean.rateMap.get(HKD).toString()));
    	}
    	if(MOP.equals(currency)){
    		 double rate_HKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(HKD).toString());     
    		 double rateMOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(MOP).toString());
    	     rateToHKD = rateMOPToRMB/rate_HKDToRMB;
    	}
    	if(USD.equals(currency)){
    		 double rate_HKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(HKD).toString()); 
    		 double rate_USDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(USD).toString());
    		 rateToHKD = rate_USDToRMB/rate_HKDToRMB;
    	}
    	if(HKD.equals(currency)){
    		rateToHKD =1.0;
    	}
    	return rateToHKD;
    }
    
    /**
     * 添加转人民币的汇率 
     */
    public static double  getRateToRMB(String currency){
       double rateToRMB = 1.0; 
    	if(RMB.equals(currency)){
    		rateToRMB = 1.0;
    	}
    	if(MOP.equals(currency)){    
    		rateToRMB = Double.parseDouble(CurrencyBean.rateMap.get(MOP).toString());
    	}
    	if(HKD.equals(currency)){
    		rateToRMB = Double.parseDouble(CurrencyBean.rateMap.get(HKD).toString());
    	}
    	if(USD.equals(currency)){
    		rateToRMB = Double.parseDouble(CurrencyBean.rateMap.get(USD).toString());
    	}
    	return rateToRMB;
    }
    
    /**
     * 得到currency2对currency1的rate，比如getRate("RMB","HKD") = 0.812
     */
    public static double getRate(String currency1,String currency2){
    	return getRateToRMB(currency2) / getRateToRMB(currency1);  	
    }
    
    public static double getRateToUSD(String currency){
    	double rateToUSD = 1.0; 
    	if(RMB.equals(currency)){
    		rateToUSD = 1 / (Double.parseDouble(CurrencyBean.rateMap.get(USD).toString()));
    	}
    	if(MOP.equals(currency)){
    		 double rateUSDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(HKD).toString());     
  		     double rateMOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(MOP).toString());
  	         rateToUSD = rateMOPToRMB/rateUSDToRMB;
    	}
    	if(HKD.equals(currency)){
      		 double rateHKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(HKD).toString());     
 		     double rateUSDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(MOP).toString());
 	         rateToUSD = rateHKDToRMB/rateUSDToRMB;
    	}
    	if(USD.equals(currency)){
    		rateToUSD = 1.0;
   	    }
    	return rateToUSD;
    }
    
    public static double getRateToMOP(String currency){
    	double rateToMOP = 1.0; 
    	if(RMB.equals(currency)){
    		rateToMOP = 1 / (Double.parseDouble(CurrencyBean.rateMap.get(
    				CurrencyBean.MOP).toString()));
    	}
    	if(MOP.equals(currency)){
    		rateToMOP = 1.0;
    	}
    	if(HKD.equals(currency)){
      		 double rateHKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(HKD).toString());     
 		     double rateMOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(MOP).toString());
 	         rateToMOP = rateHKDToRMB/rateMOPToRMB;
    	}
    	if(USD.equals(currency)){
   		    double rate_MOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(MOP).toString()); 
   		    double rate_USDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(USD).toString());
   		    rateToMOP = rate_USDToRMB/rate_MOPToRMB;
   	    }
    	return rateToMOP;
    }
    
    /*
     * 人民币
     */
    public final static String RMB = "RMB";

    /*
     * 美元
     */
    public final static String USD = "USD";

    /*
     * 港币
     */
    public final static String HKD = "HKD";

    /*
     * 欧元
     */
    public final static String EUR = "EUR";

    /*
     * 英镑
     */
    public final static String GBP = "GBP";

    /*
     * 日元
     */
    public final static String JPY = "JPY";

    /*
     * 德国马克
     */
    public final static String DEM = "DEM";

    /*
     * 澳门元
     */
    public final static String MOP = "MOP";

    /*
     * 人民币标识符
     */
    public final static String RMBB = "RMB￥";

    /*
     * 美元标识符
     */
    public final static String USDB = "USD$";

    /*
     * 港币标识符
     */
    public final static String HKDB = "HK$";

    /*
     * 欧元标识符
     */
    public final static String EURB = "EUR€";

    /*
     * 英镑标识符
     */
    public final static String GBPB = "GBP£";

    /*
     * 日元标识符
     */
    public final static String JPYB = "JPY￥";

    /*
     * 德国马克
     */
    public final static String DEMB = "DM";

    /*
     * 澳门元
     */
    public final static String MOPB = "MOP";
    
    /**
     * 添加转港币的汇率 
     */
    public static double  getRateToHKD(String currency, double money){
       double rateToHKD = 1.0; 
    	if(RMB.equals(currency)){
    		rateToHKD = 1 / (Double.parseDouble(CurrencyBean.rateMap.get(
    				CurrencyBean.HKD).toString()));
    	}
    	if(MOP.equals(currency)){
    		 double rate_HKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
    					CurrencyBean.HKD).toString());     
    		 double rateMOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
    					CurrencyBean.MOP).toString());
    	     rateToHKD = rateMOPToRMB/rate_HKDToRMB;
    	}
    	if(HKD.equals(currency)){
    		rateToHKD =1.0;
    	}
    	
    	 double ratedoures = BigDecimal.valueOf(money).multiply(BigDecimal.valueOf(rateToHKD))
         .doubleValue();
    	
    	return ratedoures;
    }
    // add by diandian.hou 2010-9-24
    /**
     * 保留几位小数，最后一位小数是进一位的
     * @param num 原数
     * @param decimal 小数位
     * @return numNew
     */   
    public static double numSaveInDecimal(double num,int decimal){
    	double numNew = Math.ceil(num*Math.pow(10.0, decimal));
		numNew /= Math.pow(10.0, decimal);
		return numNew;
    }
}
