package com.mangocity.proxy.payment.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.proxy.dao.CreditCardSerialNoDAO;
import com.mangocity.proxy.payment.service.CreditCardPreAuthInterface;
import com.mangocity.proxy.payment.service.RequestTypeTemplate;
import com.mangocity.proxy.payment.service.ServletSignUtil;
import com.mangocity.proxy.persistence.CreditCardSerialNo;
import com.mangocity.util.StringUtil;
import com.mangocity.util.XMLUtils;
import com.mangocity.util.log.MyLog;

public class PaymentStreamImpl implements CreditCardPreAuthInterface {
	
	private static final MyLog log = MyLog.getLogger(PaymentStreamImpl.class);

	private CreditCardSerialNoDAO creditCardSerialNoDAO;
	
	private static String postURL = "";           // 支付接口请求URL
	private static String returnURL = "";         // 支付接口请求URL
    private static String customerId = "";        // 酒店在支付接口中的ID号
	private static String privateKey = "";        // 酒店在支付接口中的加密密文
	private static String payChargeUrl = "";      //担保转支付URL
	
	public PaymentStreamImpl(){
	    try {
	        //从数据库参数配制文件configpara表中读取数据
	    	
            postURL = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.posturl}");
            returnURL = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.returnurl}");
            customerId = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.customerId}");
            privateKey = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.privateKey}");
            payChargeUrl = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.payChargeUrl}");
            
            
        } catch (Exception e) {
            DeBug("支付接口URL读取出错!!!");
        }
	}
	
	/**
	 * 在线支付接口, 外部订单号 00 开始;
	 * 
	 * @param orderCode      
	 *             订单编号（不是ID）
	 * @param amount         
	 *             支付金额
     * @param memberCD
     *            （会员CD，不是ID）
	 * @param description    
	 *             备注信息，如（酒店前台自动生成，机票前台自动生成，机票中台自动生成等）
	 * @param payType        
	 *             支付类型
	 * @return 
	 */
	public static String OnlinePayment(String orderCode, String amount, String memberCD,
            String description, String payType) {
	  //请求的xml格式的字符串
        String xmlStr = "";
        String outTradeNo = customerId + orderCode + "00";
        
        DeBug("在线支付接口开始");
        xmlStr = RequestTypeTemplate.OnlinePayment(orderCode, customerId, memberCD, "CENTER",
            outTradeNo, "RMB", payType, amount, description, returnURL, "MD5", privateKey);
        
        DeBug("信在线支付 xmlStr = " + xmlStr);
        try {
            String result = ServletSignUtil.HttpClientPost(postURL, xmlStr);
            DeBug("在线支付 result = " + result);
//            Element resultElement = XMLUtils.getElementByXmlString(result);
//            List<Element> reList = resultElement.getChildren();
//            for(Element ele : reList) {
//                DeBug("在线支付查询结果 success = " + ele.getChild("success").getText() + 
//                    ", message = " + ele.getChild("message").getText());
//                if(ele.getChild("success").getText().equals("T"))
//                    return "success";
//                else
//                    return ele.getChild("message").getText();
//            }
        } catch (Exception e) {
            DeBug("在线支付错误 ! " + e);
        }
	    return "";
	}
	
	/**
	 * 更改信用卡信息
	 * v2.9.3 add by shaojun.yang 2009-09-16
	 * @param orderCode 订单号
	 * @param creditCardId 信用卡号
	 * @return
	 */
	public String ChangeCreditCard(String orderCode,String creditCardId){
		//请求的xml格式的字符串
        String xmlStr = "";
        String outTradeNo = customerId + orderCode + "00";
        DeBug("修改信用卡卡号在线支付接口开始");
        xmlStr=RequestTypeTemplate.ChangeCreditCardSwitchPayment(customerId, outTradeNo, creditCardId, "MD5", privateKey);
        
        DeBug("修改信用卡卡号在线支付 xmlStr = " + xmlStr);
        String flag="";
        String message="";
		try {
			String resultStr = ServletSignUtil.HttpClientPost(postURL, xmlStr);
			Element resultElement = XMLUtils.getElementByXmlString(resultStr);
	        List<Element> reList = resultElement.getChildren();
	        for(Element result : reList) {
	        	flag = result.getChild("success").getText();
                message = result.getChild("message").getText();
	        }
			DeBug("修改信用卡卡号在线支付 result = " + resultStr);
		} catch (Exception e) {
			DeBug("修改信用卡卡号在线支付 ! " + e);
		}
		return (StringUtil.isValidStr(flag) && flag.equals("T"))?"": message;
	}
	
    /**
     * 生成预授权工单到CTIIWeb系统, 外部订单号 10 开始;
     * 
     * @param orderCode
     *            订单编号（不是ID）
     * @param orderType
     *            订单来源，酒店为(HOTEL)、机票请填(TICKET)
     * @param preAuthAmount
     *            担保金额（保留两位小数）
     * @param memberCD
     *            （会员CD，不是ID）
     * @param loginName
     *            （操作人员的登陆名）
     * @param creditCardIds
     *            优先使用的信用卡id，以;号分隔。如（001254;001255;)，001254和001255都是信用卡ID
     * @param description
     *            备注信息，如（酒店前台自动生成，机票前台自动生成，机票中台自动生成等）
     * @param prePayType
     *            预付不审担保：1（全额预付），2（担保）
     * @param currencyType
     *            支付的币种：RMB，HKD
     * @return 返回"succeed"表示成功，其它不成功
     * @throws Exception 
     */
	public String createPreAuthList(String orderCode, String orderType,
	    double preAuthAmount, String memberCD, String loginName, String creditCardIds, String sid, 
	        String description, String prePayType, String currencyType) throws Exception {
		
	//	if(sid == null || "".equals(sid)) {
	//		log.error("ErrorMSG:MPM:sid is null,refuse to invocate OfflinePayment method!");
	//		throw new Exception();
	//	}
	    //请求的xml格式的字符串
		String serialNo="";
		List<CreditCardSerialNo> serialNoList = creditCardSerialNoDAO.getSerialNoByCustomerIdOrderCode(customerId, 
				orderCode);
		
		if(!serialNoList.isEmpty()){
			CreditCardSerialNo creditCardSerialNo = serialNoList.get(0);
			serialNo = String.valueOf(Integer.parseInt(creditCardSerialNo.getSerialNo()) + 1);
			creditCardSerialNo.setSerialNo(serialNo);
			creditCardSerialNoDAO.updateCreditCardSerialNo(creditCardSerialNo);
		}else{
			CreditCardSerialNo newCreditCardSerialNo = new CreditCardSerialNo();
			newCreditCardSerialNo.setCustomerId(customerId);
			newCreditCardSerialNo.setOrderCode(orderCode);
			serialNo="10";
			newCreditCardSerialNo.setSerialNo(serialNo);
			creditCardSerialNoDAO.saveCreditCardSerialNo(newCreditCardSerialNo);
		}
	    String xmlStr = "";
	    String outTradeNo = customerId + orderCode + serialNo;
        String amount = new java.text.DecimalFormat("#.00").format(preAuthAmount);
        if(!StringUtil.isValidStr(currencyType)){
	        currencyType = "RMB";
	    }
        
	    if (prePayType.equals("1")) {          // 信用卡全额预付,在线
	        DeBug("信用卡全额预付调用支付接口开始");
	        xmlStr = RequestTypeTemplate.OfflinePayment(orderCode, customerId, memberCD, "CENTER",
	            outTradeNo, currencyType, "DIVR", amount, description, creditCardIds, sid, "ALL","0", "MD5", privateKey);
	    } else if (prePayType.equals("2")) {   // 信用卡担保支付
	        DeBug("信用卡担保支付调用支付接口开始");
	        xmlStr = RequestTypeTemplate.OfflinePayment(orderCode, customerId, memberCD, "CENTER",
                outTradeNo, currencyType, "DIVR", amount, description, creditCardIds, sid, "ASS","0", "MD5", privateKey);
	    }
	    DeBug("信用卡支付 xmlStr = " + xmlStr);
        try {
            String result = ServletSignUtil.HttpClientPost(postURL, xmlStr);
            DeBug("信用卡支付 result = " + result);
            Element resultElement = XMLUtils.getElementByXmlString(result);
            List<Element> reList = resultElement.getChildren();
            for(Element ele : reList) {
                DeBug("用卡担保支付查询结果 success = " + ele.getChild("success").getText() + 
                    ", message = " + ele.getChild("message").getText());
                if(ele.getChild("success").getText().equals("T"))
                    return "succeed";
                else
                    return ele.getChild("message").getText();
            }
        } catch (IOException e) {
            DeBug("有可能是超时错误" + e);
            return "succeed";   // 工行DDN BUG,超时错误,先认为其是正常支付.
        } catch (Exception e) {
            DeBug("信用卡支付错误 ! " + e);
        }
       
        return "false";
	}

    /**
     * 重载createPreAuthList()方法
     * 
     * @return
     * @throws Exception 
     */
    public String createPreAuthList(String orderCode, String orderType, double preAuthAmount,
        String memberCD, String loginName, String creditCardIds, String sid, String description,
        String prePayType, int installment, String currencyType) throws Exception {
        return createPreAuthList(orderCode, orderType, preAuthAmount, memberCD, loginName,
            creditCardIds, sid, description, prePayType,currencyType);
    }
    
    /**
     * 查询信用卡担保状态,根据订单号查询 状态有:succeed：担保成功;failed:担保失败;null:未做;notfound:未找到
     * 
     * @param orderType
     *            订单类型，机票订单（TICKET）、酒店订单（HOTEL）
     * @param orderCode
     *            订单编码
     * 
     * @return 信用卡担保状态
     */
    public String getPreAuthSucceedFlag(String orderType, String orderCode) {
	   
	    DeBug("信用卡担保状态查询接口开始");
	    String serialNo="10";
		List<CreditCardSerialNo> serialNoList = creditCardSerialNoDAO.getSerialNoByCustomerIdOrderCode(customerId, 
				orderCode);		
		if(!serialNoList.isEmpty()){
			serialNo = serialNoList.get(0).getSerialNo();
		}
		
        String outTradeNoStr = customerId + orderCode + serialNo;
	    String xmlStr = RequestTypeTemplate.CreditCardPaymentState(customerId, outTradeNoStr, "MD5", privateKey);
	    DeBug("信用卡担保状态查询 xmlStr = " + xmlStr);
        try {
            String resultStr = ServletSignUtil.HttpClientPost(postURL, xmlStr);
            DeBug("信用卡担保状态查询 result = " + resultStr);
            Element resultElement = XMLUtils.getElementByXmlString(resultStr);
            List<Element> reList = resultElement.getChildren();
            for(Element result : reList) {
                
                // 生成预授权请求是否成功
                String resultPrePay = result.getChild("success").getText();
                String messagePrePay = result.getChild("message").getText();
                DeBug("success = " + resultPrePay + ", message = " + messagePrePay);
                Element response = result.getChild("response");
                // 外部交易号
                String outTradeNo = response.getChild("outTradeNo").getText();
                // 成功SUCCESS, 取消CANCEL, 失败FAIL, 退款：REFUND, 支付中WAIT, 异常：EXCEPTION
                String payResult = response.getChild("payResult").getText();  
                // 支付信息
                String payMessage = response.getChild("payMessage").getText();  
                // 支付时间
                String payTime = response.getChild("payTime").getText();
                // 支付金额
                String payAmount  = response.getChild("payAmount").getText();
                // 加密方式
                String signType = response.getChild("signType").getText();
                // 密文
                String sign = response.getChild("sign").getText();
                // 输出调试信息
                DeBug("outTradeNo = " + outTradeNo + ", payMessage = " + payMessage + 
                      ", payTime = " + payTime + ", payAmount = " + payAmount + ", signType = " + signType);
                
                Map<String, String> payProperty = new HashMap<String, String>();
                payProperty.put("outTradeNo", outTradeNo);
                payProperty.put("payResult", payResult);
                payProperty.put("payMessage", payMessage);
                payProperty.put("payTime", payTime);
                payProperty.put("payAmount", payAmount);
                
//                if(ServletSignUtil.buildSign(payProperty,privateKey).equals(sign)) {
                    if(payResult.equals("SUCCESS")){ //支付成功
                        return "succeed";
                    } else if(payResult.equals("WAIT")) { // 等待支付中
                        return "undo";
                    } else if(payResult.equals("CANCEL")) { // 取消
                        return "cancel";
                    } else if(payResult.equals("FAIL")) { // 失败
                        return "failed";
                    } else if(payResult.equals("REFUND")) { // 退款
                        return "refund";
                    } else if(payResult.equals("EXCEPTION")) { // 异常
                        return "exception";
                    }
//                }
                
            }
        } catch (Exception e) {
            DeBug("信用卡担保状态查询错误 ! " + e);
        }
		return "failed";
	}

    /**
     * 根据信用卡Id获取信用卡历史交易记录
     * 
     * @param creditCardId
     *            信用卡Id
     * @return 信用卡历史交易记录
     */
	public String getPreAuthTransactionRecordsByCreditcardId(long creditCardId) {
		return ParamServiceImpl.getInstance().getConfValue("{hotel.payment.creditCardIdQeryCreditUrl}") + creditCardId;
	}
	
	
	
	
	/**
     * 面付担保转支付,是担保单但是取消和修改时产生一定的金额，需要再次调用接口扣款
     * @author lihaibo
     * @param 客户ID
     * 		   外部交易号
     * 			支付金额
     * 加密类型
     * 密文
     * 
     */
    public String payCharge(double chargeSum, String orderCD){
    	DeBug("担保转支付接口开始");
    	//payChargeUrl = "http://10.10.7.101/MpmWeb/servlet/PaymentServlet";
    	log.info(payChargeUrl);
    	String resultPrePay = "";
    	String outTradeNoStr = "";
		List<CreditCardSerialNo> serialNoList = creditCardSerialNoDAO.getSerialNoByCustomerIdOrderCode(customerId, 
				orderCD);	
		if(!serialNoList.isEmpty()){
			 outTradeNoStr = customerId + orderCD + serialNoList.get(0).getSerialNo();//外部交易号
		}else{
			 outTradeNoStr = customerId + orderCD + "10";//外部交易号
		}
		
		String amount = new java.text.DecimalFormat("#.00").format(chargeSum);//扣款金额
		String key = RequestTypeTemplate.buildSign(customerId,outTradeNoStr,amount,privateKey);
		String xmlStr = RequestTypeTemplate.Charge(amount,outTradeNoStr,customerId,"MD5",key);//封装xml
		DeBug(xmlStr);
		try{
			String resultStr = ServletSignUtil.HttpClientPost(payChargeUrl, xmlStr);//发送请求
			if(StringUtil.isValidStr(resultStr)){
				Element resultElement = XMLUtils.getElementByXmlString(resultStr);
	            List<Element> reList = resultElement.getChildren();
	            for(Element result : reList) {
	            	 resultPrePay = result.getChild("success").getText();//接受返回信号：成功：T，失败：F
	            }
			}
			
		}catch(Exception e){
			DeBug("担保转支付错误 ! " + e);
		}
		return resultPrePay;
		
		
    }
    
    
    
//    public String prePayCharge(double chargeSum,String orderCD){
//    	
//    	
//    	return null;
//    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        PaymentStreamImpl.postURL = postURL;
    }
    
    private static void DeBug(String info) {
        log.info(info);
    }

	public static String getPayChargeUrl() {
		return payChargeUrl;
	}

	public static void setPayChargeUrl(String payChargeUrl) {
		PaymentStreamImpl.payChargeUrl = payChargeUrl;
	}

	public void setCreditCardSerialNoDAO(CreditCardSerialNoDAO creditCardSerialNoDAO) {
		this.creditCardSerialNoDAO = creditCardSerialNoDAO;
	}
}
