package com.mangocity.hotel.order.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 预付订单的付款方式
 * 
 * @author chenkeming
 *
 */
public class PrepayType {
    
    /**
	 * 和CDM支付方式的转化
	 */
    public static Map<Integer,String> payMap = null;
    
    /**
	 * 和中文支付方式的转化
	 */
    public static Map<Integer,String> payStrMap = null;
    
    /**
     * 预付方式的字符串常量
     * 
     * @author chenkeming Apr 1, 2009 9:46:32 AM
     */
    public static String[] strNames = { "Cash", "CredInt", "CredDom", "Pt", "Bank",
            "Voucher", "Pos", "IPSDom", "IPSInt", "CMBInt", "ALIInt", 
            "TENInt", "COMMInt", "YEEInt", "BILLInt", "ABCInt", "Coupon","MonthPay","RMHKEPSInt","RMHKPPSInt","RMCITICPAYInt",
            "DEBITCARDInt","Bla","HKIVR","RMUNIONPAY","RMPSBCD","ALLSCORE","HOTELMONTHPAY","UnionPayPhone","RMCCBLIFEPAY"};
    
    static {
        payMap = new HashMap<Integer, String>();
        payMap.put(Integer.valueOf(1), "CASH");
        payMap.put(Integer.valueOf(2), "IIVR");
        payMap.put(Integer.valueOf(3), "DIVR");
        payMap.put(Integer.valueOf(4), "ITG");
        payMap.put(Integer.valueOf(5), "BKTA");
        payMap.put(Integer.valueOf(6), "CST");
        payMap.put(Integer.valueOf(7), "DPOS");
        payMap.put(Integer.valueOf(8), "IIPS");
        payMap.put(Integer.valueOf(9), "DIPS");
        payMap.put(Integer.valueOf(10), "ONLP");
        
        /**
		 * add by wuyun 增加在线支付方式 2009-03-16
		 */
        payMap.put(Integer.valueOf(11), "ALIPAY");
        payMap.put(Integer.valueOf(12), "TENPAY");
        payMap.put(Integer.valueOf(13), "COMM");
        payMap.put(Integer.valueOf(14), "YEE");
        payMap.put(Integer.valueOf(15), "99BILL");
        payMap.put(Integer.valueOf(16), "ABC");

        /**
         * 代金券 支付方式 hotel2.9.3 add by chenjiajie 2009-09-02
         */
        payMap.put(Integer.valueOf(17), "Coupon");
        payMap.put(Integer.valueOf(18), "MonthPay");
        //香港EPS add by shegnwei.zuo 2009-12-22
        payMap.put(Integer.valueOf(19), "RMHKEPS");
        //香港PPS add by shegnwei.zuo 2009-12-22
        payMap.put(Integer.valueOf(20), "RMHKPPS");
        //中信银行 add by chenjiajie 2010-01-15
        payMap.put(Integer.valueOf(21), "RMCITICPAY");
        //RMS3105借记卡(银联手机支付) add by chenjiajie 2010-04-06
        payMap.put(Integer.valueOf(22), "DEBITCARD");
        //账户支付 TMC-V2.0 add by shengwei.zuo   2010-3-23
        payMap.put(Integer.valueOf(23), "MPOS");
        //香港信用卡 add by diandian.hou 2010-8-30
        payMap.put(Integer.valueOf(24), "HKIVR");
        //银联互联网支付 add by alfred 2011-6-15
        payMap.put(Integer.valueOf(25), "RMUNIONPAY");
        payMap.put(Integer.valueOf(26), "RMPSBCD");//邮政储蓄（借记卡） add by diandian.hou 2011-12-14
        //奥斯卡支付 add by xuyiwen 2012-4-27
        payMap.put(Integer.valueOf(27), "ALLSCORE");
        payMap.put(Integer.valueOf(28),"HOTELMONTHPAY");
        //银联电话支付 add by wupingxiang at 2012-12-13
        payMap.put(Integer.valueOf(29),"UnionPayPhone");
        payMap.put(Integer.valueOf(30), "RMCCBLIFEPAY");
		
        payStrMap = new HashMap<Integer, String>();
        payStrMap.put(Integer.valueOf(1), "现金支付");
        payStrMap.put(Integer.valueOf(2), "信用卡支付（国际）");
        payStrMap.put(Integer.valueOf(3), "信用卡支付（国内）");
        payStrMap.put(Integer.valueOf(4), "积分支付");
        payStrMap.put(Integer.valueOf(5), "银行转账");
        payStrMap.put(Integer.valueOf(6), "现金券");
        payStrMap.put(Integer.valueOf(7), "POS支付");
        payStrMap.put(Integer.valueOf(8), "IPS支付(国内)");
        payStrMap.put(Integer.valueOf(9), "IPS支付(国际)");
        payStrMap.put(Integer.valueOf(10), "招行在线");
        
        /**
		 * add by wuyun 增加在线支付说明 2009-03-16
		 */
        payStrMap.put(Integer.valueOf(11), "支付宝");
        payStrMap.put(Integer.valueOf(12), "财付通");
        payStrMap.put(Integer.valueOf(13), "交行在线");
        payStrMap.put(Integer.valueOf(14), "易宝");
        payStrMap.put(Integer.valueOf(15), "快钱");
        payStrMap.put(Integer.valueOf(16), "农业银行");
        payStrMap.put(Integer.valueOf(17), "代金券");
        payStrMap.put(Integer.valueOf(18), "月结");
        
        payStrMap.put(Integer.valueOf(19), "香港EPS");
        payStrMap.put(Integer.valueOf(20), "香港PPS");
        //中信银行 add by chenjiajie 2010-01-15
        payStrMap.put(Integer.valueOf(21), "中信银行");
        //RMS3105借记卡(银联手机支付) add by chenjiajie 2010-04-06
        payStrMap.put(Integer.valueOf(22), "借记卡(银联手机支付)");
        //账户支付 TMC-V2.0 add by shengwei.zuo   2010-3-23
        payStrMap.put(Integer.valueOf(23), "账户支付");
        payStrMap.put(Integer.valueOf(24), "香港信用卡");
        //银联互联网支付 modify by alfred 2011-6-15
        payStrMap.put(Integer.valueOf(25), "银联互联网支付");
        payStrMap.put(Integer.valueOf(26), "邮政储蓄(借记卡)");//邮政储蓄（借记卡） add by diandian.hou 2011-12-14
      //奥斯卡支付 add by xuyiwen 2012-4-27
        payStrMap.put(Integer.valueOf(27), "奥斯卡支付");
        payStrMap.put(Integer.valueOf(28), "酒店月结");// add by diandian.hou 2012-07-09
        payStrMap.put(Integer.valueOf(29), "银联电话支付");
		payStrMap.put(Integer.valueOf(30), "建行悦生活");
        
    }
    
    /**
	 * 现金
	 */
    public static int Cash = 1; 
    
    /**
	 * 信用卡授权支付-国际
	 */
    public static int CreditCardInt = 2; 
    
    /**
	 * 信用卡授权支付-国内
	 */
    public static int CreditCardDom = 3; 
    
    /**
	 * 积分
	 */
    public static int Points = 4; 
    
    /**
	 * 银行转账
	 */
    public static int Bank = 5; 
    
    /**
	 * 现金券
	 */    
    public static int Voucher = 6; 
    
    /**
	 * POS刷卡
	 */
    public static int POS = 7; 
        
    /**
	 * IPS国内
	 */
    public static int IPSDom = 8;     
    
    /**
	 * IPS国际
	 */
    public static int IPSInt = 9; 
    /**
	 *	CMB招行
	 */
    public static int CMBInt = 10; 
    
    /**
	 * add by wuyun 增加在线支付 2009-03-16
	 */
    /**
	 *	支付宝
	 */
    public static int ALIInt = 11;
    /**
	 *	财付通
	 */
    public static int TENInt = 12;
    /**
	 *	交行在线
	 */
    public static int COMMInt = 13;
    /**
	 *	易宝
	 */
    public static int YEEInt = 14;
    /**
	 *	快钱
	 */
    public static int BILLInt = 15;
    /**
	 *	农业银行
	 */
    public static int ABCInt = 16;
    
    /**
	 * 代金券
	 * hotel2.9.3 add by chenjiajie 2009-08-26
	 */
    public static int Coupon = 17;
    
    /**
     * 月结 
     * Haier add by shizhongwen 2009-10-17 
     */
    public static int MonthPay=18;
    
    /**
	 *	香港EPS add by shengwei.zuo 2009-12-22
	 */
    public static int RMHKEPSInt = 19;
    
    /**
	 *	香港PPS add by shengwei.zuo 2009-12-22
	 */
    public static int RMHKPPSInt = 20;
    
    /**
     * 中信银行 add by chenjiajie 2010-01-15
     */
    public static int RMCITICPAYInt = 21;
    
    /**
     * RMS3105借记卡(银联手机支付) add by chenjiajie 2010-04-06
     */
    public static int DEBITCARDInt = 22;
    
    /**
     * 账户支付 TMC-V2.0  add by shengwei.zuo  2010-3-23
     */
    public static int BALANCEPAYInt = 23;
    /**
     * 香港信用卡支付 add by diandian.hou 2010-8-30
     */
    public static int HKIVRInt = 24;
    
    /**
     * 银联互联网支付 add by alfred 2011-6-15
     */
    public static int RMUNIONPAYInt = 25;
    public static int RMPSBCDInt = 26 ;//邮政储蓄（借记卡） add by diandian.hou 2011-12-14
  //奥斯卡支付 add by xuyiwen 2012-4-27
    public static int ALLSCOREInt = 27 ;
    
    /**
     * 酒店月结,开始于魅影的代理商月结
     */
    public static int HOTELMONTHPAYInt = 28; //酒店月结,开始于魅影的代理商月结 add by diandian.hou 2012-07-09
    /**
     * 银联电话支付
     */
    public static int UNTIONPAYPHONE = 29;
	
	/**
	*/
	
	public static int RMCCBLIFEPAY = 30;
    /** 
	 * 支付宿 
	 */ 
    public static final String ALIPAY = "ALIPAY";
     
    /** 
	 * 财付逿 
	 */ 
    public static final String TENPAY = "TENPAY";
     
    /** 
	 * 块钱 
	*/ 
    public static final String BILL99 = "99BILL";
     
    /** 
	* 招行在线 
	*/ 
    public static final String CMB = "cmb";
 
    /** 
	* 交行在线 
	 */ 
    public static final String COMM = "comm";
     
    /** 
	* IPS支付-（国内卡＿ 
	*/ 
    public static final String IPSR = "8ipsr";

    /** 
	* 在线支付-（国外卡＿ 
	 */ 
    public static final String IPSO = "8ipso";
 
    /** 
	* 易宝 
	*/ 
    public static final String YEEPAY = "yeePay";
 
    /** 
	* 易宝（招商银行） 
	*/ 
    public static final String CMBYEEPAY = "CMBYeePay";

    /** 
	 * 易宝（农业银行） 
	*/ 
    public static final String ABCYEEPAY = "ABCYeePay";
    
    /** 
	 *  香港EPS  add by shengwei.zuo 2009-12-22
	*/ 
    public static final String RMHKEPS = "RMHKEPS";
    
    /** 
	 *  香港PPS  add by shengwei.zuo 2009-12-22
	*/ 
    public static final String RMHKPPS = "RMHKPPS";
    
    /**
     * 中信银行 add by chenjiajie 2010-01-15
     */
    public static final String RMCITICPAY = "RMCITICPAY";
    
    /**
     * RMS3105借记卡(银联手机支付) add by chenjiajie 2010-04-06
     */
    public static final String DEBITCARD = "DEBITCARD";
    
    /**
     * 账户支付 TMC-V2.0  add by shengwei.zuo   2010-3-23
     */
    public static final String BALANCEPAY = "MPOS";
    
    /**
     * 香港信用卡 add by diandian.hou 
     */
    public static final String HKIVR = "HKIVR";
    
    /**
     * 银联互联网支付 modify by alfred 2011-6-15
     */
    public static final String RMUNIONPAY = "RMUNIONPAY";
    
    public static final String RMPSBCD = "RMPSBCD"; //邮政储蓄（借记卡） add by diandian.hou 2011-12-14
    public static final String HOTELMONTHPAY = "HOTELMONTHPAY"; //酒店月结,开始于魅影的代理商月结 add by diandian.hou 2012-07-09
    
    public static final String ALLSCORE = "ALLSCORE";  //奥斯卡支付 add by xuyiwen 2012-4-27
}
