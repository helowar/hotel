package com.mangocity.webnew.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.util.log.MyLog;

/**
 * MD5加密以及验证加密信息工具类 功能：1.加密 2.加密验证
 * @author shengwei.zuo
 * 
 */
public class HotelPayOnlieUtil {
    
    public static final int CONNECTION_TIME = 10*1000;//连接超时时间
        
    public static final int SEVICE_TIME=80*1000;//服务器返回超时时间(毫秒)
    
    /**
     * 客户id
     */
    public static final String CUSTOMER_ID=ParamServiceImpl.getInstance().getConfValue("{hotel.payment.customerId}");
    
    //需确认
    public static final String CUSTOMER_TMC_ID="1004";//TMC客户id 
    
    /**
     * 密钥
     */
    public static final String PSD_KEY = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.privateKey}");
    
    public static final String CURRENCY_RMB="RMB";//人民币
    
    public static final String CURRENCY_HKD="HKD";//港币
    
    public static final String GATHERING_UNITCODE="CENTER";
    
    //需确认
    public static final String PRODUCT_TYPE_HOTEL="HOTEL";
    
    public static final String SIGN_TYPE="MD5";
    
    //"http://10.10.7.101/MpmWeb/servlet/PaymentServlet"
   // public static final String REQUEST_URL=getConfigValue("TOP_mpmdivr_req_url");//信用卡离线或现金请求地址
    
    public static final String ONLINE_REQUEST_URL=getConfigValue("HWEB_mpmonline_req_url");//在线支付请求地址
    public static final String CCBONLINE_REQUEST_URL=getConfigValue("HWEB_mpmCCBonline_req_url");//在线支付请求地址
    
    private static final MyLog log = MyLog.getLogger(HotelPayOnlieUtil.class);  
    /**
     * 加密数据之前，对参数进行字母排序和拼装。
     * 
     * @param params
     *            进行签名的数据
     * @param privateKey
     *            key密钥
     * 
     * @return 加密后的密文
     */
    public static String buildSign(Map<String, String> params, String privateKey) {
        // 如果map为空，则不进行任何操作
        if (null == params) {
            return "";
        }
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer paramsStrBuf = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (null == key || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("signType")) {
                continue;
            }
            String value = params.get(key);
            if (null == value || 0 == value.trim().length() || "null".equals(value)) {
                continue;
            }
            paramsStrBuf.append((0 == i ? "" : "&"));
            paramsStrBuf.append(key);
            paramsStrBuf.append("=");
            paramsStrBuf.append(value);

        }
        return Md5Encrypt.md5(paramsStrBuf.append(privateKey).toString());
    }

    /**
     * 加密信息验证
     * 
     * @param sign
     *            密文
     * @param Params
     *            进行签名的数据
     * @param key
     *            密钥
     * @return 验签结果
     */
    public static boolean verifySign(String sign, Map<String, String> Params, String key) {
        boolean verifyResult = false;
        String verifySign = buildSign(Params, key);
        log.info("sign=" + sign + ",verifySign=" + verifySign);
        if (sign.toLowerCase().equals(verifySign.toLowerCase())) {
            verifyResult = true;
        }
        return verifyResult;
    }
    
    public static String getConfigValue(String param) {
		String value = ParamServiceImpl.getInstance().getConfValue(
				"{" + param + "}");
		return value;
	}
    
    /**
     * 获得非代金券和积分的支付方式  add by diandian.hou 2011-11-07
     */
    public static  OrPayment getPaymentNotCouponNotPoint(List<OrPayment> paymentLst) {
    	OrPayment paymentEntiy = new OrPayment();
	    for(int i = 0 ; i<paymentLst.size();i++){
	    	OrPayment _paymentEntiy =  paymentLst.get(i);
	    	if(PrepayType.Coupon != _paymentEntiy.getPayType() && PrepayType.Points != _paymentEntiy.getPayType()){
	    		paymentEntiy = _paymentEntiy;
	    	}
	    }
	    return paymentEntiy;
    }
    /**
     * 截取请求地址 
     * */
	public static String getRequestUrl(String url) {

		String urlArry[] = url.split("/");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			sb.append(urlArry[i]);
			if (i < 3) {
				sb.append("/");
			}
		}
		return sb.toString();
	}
}
