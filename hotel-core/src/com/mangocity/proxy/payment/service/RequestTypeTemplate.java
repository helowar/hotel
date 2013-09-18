package com.mangocity.proxy.payment.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
public final class RequestTypeTemplate {

    /**
     * 离线支付
     * @param orderCd 订单ID
     * @param customerId 客户ID
     * @param gatheringUnitCode 收款单位
     * @param memberCD 会员CD
     * @param outTradeNo 外部交易号
     * @param currencyType 币种
     * @param payMode 支付方式,CDM数据
     * @param amount 支付金额
     * @param remark 备注
     * @param creditCardId 信用卡ID
     * @param cardPayType 离线支付类别,,全额=ALL，担保=ASS，分期=AFQ，信用卡积分=CJF 
     * @param stageCount 分期数
     * @param signType 加密方式(MD5),目前只支持MD5
     * @param privateKey 加密Key
     * @return
     */
    public static String OfflinePayment(String orderCd, String customerId, String memberCD,
        String gatheringUnitCode, String outTradeNo, String currencyType, String payMode, 
        String amount, String remark, String creditCardId, String sid, String cardPayType,
        String stageCount, String signType, String privateKey) {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date = new Date();      
        String nowDate = sf.format(date);
        Map<String, String> payProperty = new HashMap<String, String>();
        payProperty.put("orderCd", orderCd);
        payProperty.put("outTradeNo", outTradeNo);
        payProperty.put("memberCD", memberCD);
        payProperty.put("customerId", customerId);
        if(sid!=null && sid.length()!=0){
        	payProperty.put("sid", sid);
        }
        
        payProperty.put("gatheringUnitCode", gatheringUnitCode);
        payProperty.put("currencyType", currencyType);
        payProperty.put("requestDate", nowDate);
        payProperty.put("productType", "HOTEL");
        payProperty.put("payMode", payMode);
        payProperty.put("amount", amount);
        payProperty.put("creditCardId", creditCardId);
        payProperty.put("remark", remark);
        payProperty.put("cardPayType", cardPayType);
        payProperty.put("stageCount", stageCount);

        StringBuffer xmlStr = new StringBuffer();
        // XML头
        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
        // 支付接口
        xmlStr.append("<mpm>");
        // 接口名称：在线支付
        xmlStr.append("<service>PAYMENT</service>");
        // 请求参数
        xmlStr.append("<request>");
        // 订单ID
        xmlStr.append("<orderCd>" + orderCd + "</orderCd>");
        // 外部交易号,生成规则：客户ID+订单编号+2位流水码
        xmlStr.append("<outTradeNo>" + outTradeNo + "</outTradeNo>");
        // 客户CD
        xmlStr.append("<memberCD>" + memberCD + "</memberCD>");
        // 客户ID, 在收款系统注册的ID
        xmlStr.append("<customerId>" + customerId + "</customerId>");
        //支付凭证
        if(sid!=null && sid.length()!=0){
         xmlStr.append("<sid>" + sid + "</sid>");
        }
        // 收款单位
        xmlStr.append("<gatheringUnitCode>" + gatheringUnitCode + "</gatheringUnitCode>");
        // 币种
        xmlStr.append("<currencyType>" + currencyType + "</currencyType>");
        // 请求时间
        xmlStr.append("<requestDate>" + nowDate + "</requestDate>");
        // 产品类型
        xmlStr.append("<productType>HOTEL</productType>");
        // 支付方式
        xmlStr.append("<payMode>" + payMode + "</payMode>");
        // 支付金额,担保转支付金额 单位：元，格式1.00，支付金额不能大于担保金额
        xmlStr.append("<amount>" + amount + "</amount>");
        // 备注
        xmlStr.append("<remark>" + remark + "</remark>");    
        // 信用卡ID
        xmlStr.append("<creditCardId>" + creditCardId + "</creditCardId>");   
        // 离线支付类别,,全额=ALL，担保=ASS，分期=AFQ，信用卡积分=CJF 
        xmlStr.append("<cardPayType>" + cardPayType + "</cardPayType>");   
        // 分期数
        xmlStr.append("<stageCount>" + stageCount + "</stageCount>");   
        // 加密方式(MD5),目前只支持MD5
        xmlStr.append("<signType>" + signType + "</signType>");                 
        // 密文
        xmlStr.append("<sign>" + ServletSignUtil.buildSign(payProperty,privateKey) + "</sign>");       
        xmlStr.append("</request>");
        xmlStr.append("</mpm>");
        return xmlStr.toString();
    }

    /**
     * 在线支付
     * @param orderCd 订单ID
     * @param customerId 客户ID
     * @param memberCD 会员CD
     * @param gatheringUnitCode 收款单位
     * @param outTradeNo 外部交易号
     * @param currencyType 币种
     * @param payMode 支付方式,CDM数据
     * @param amount 支付金额
     * @param remark 备注
     * @param signType 加密方式(MD5),目前只支持MD5
     * @param privateKey 加密Key
     * @return
     */
    public static String OnlinePayment(String orderCd, String customerId, String memberCD,
        String gatheringUnitCode, String outTradeNo, String currencyType, String payMode, 
        String amount, String remark, String returnURL, String signType, String privateKey) {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date = new Date();      
        String nowDate = sf.format(date);
        Map<String, String> payProperty = new HashMap<String, String>();
        payProperty.put("orderCd", orderCd);
        payProperty.put("outTradeNo", outTradeNo);
        payProperty.put("memberCD", memberCD);
        payProperty.put("customerId", customerId);
        payProperty.put("gatheringUnitCode", gatheringUnitCode);
        payProperty.put("currencyType", currencyType);
        payProperty.put("requestDate", nowDate);
        payProperty.put("productType", "HOTEL");
        payProperty.put("payMode", payMode);
        payProperty.put("remark", remark);
        payProperty.put("amount", amount);
        payProperty.put("returnURL", returnURL);

        StringBuffer xmlStr = new StringBuffer();
        // XML头
        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
        // 支付接口
        xmlStr.append("<mpm>");
        // 接口名称：在线支付
        xmlStr.append("<service>PAYMENT</service>");
        // 请求参数
        xmlStr.append("<request>");
        // 订单ID
        xmlStr.append("<orderCd>" + orderCd + "</orderCd>");
        // 外部交易号,生成规则：客户ID+订单编号+2位流水码
        xmlStr.append("<outTradeNo>" + outTradeNo + "</outTradeNo>");
        // 客户CD
        xmlStr.append("<memberCD>" + memberCD + "</memberCD>");
        // 客户ID, 在收款系统注册的ID
        xmlStr.append("<customerId>" + customerId + "</customerId>");
        // 收款单位
        xmlStr.append("<gatheringUnitCode>" + gatheringUnitCode + "</gatheringUnitCode>");
        // 币种
        xmlStr.append("<currencyType>" + currencyType + "</currencyType>");
        // 请求时间
        xmlStr.append("<requestDate>" + nowDate + "</requestDate>");
        // 产品类型
        xmlStr.append("<productType>HOTEL</productType>");
        // 支付方式
        xmlStr.append("<payMode>" + payMode + "</payMode>");
        // 支付金额,担保转支付金额 单位：元，格式1.00，支付金额不能大于担保金额
        xmlStr.append("<amount>" + amount + "</amount>");
        // 返回URL
        xmlStr.append("<returnURL>" + returnURL + "</returnURL>");  
        // 备注
        xmlStr.append("<remark>" + remark + "</remark>");    
        // 加密方式(MD5),目前只支持MD5
        xmlStr.append("<signType>" + signType + "</signType>");                 
        // 密文
        xmlStr.append("<sign>" + ServletSignUtil.buildSign(payProperty,privateKey) + "</sign>");       
        xmlStr.append("</request>");
        xmlStr.append("</mpm>");
        return xmlStr.toString();
    }


    /**
     * 信用卡担保状态查询模版
     * @param customerId 客户ID, 在收款系统注册的ID
     * @param outTradeNo 外部交易号,生成规则：客户ID+订单编号+2位流水码
     * @param signType 加密方式,目前只支持MD5
     * @param privateKey 加密Key
     * @return String XML格式的支付请求
     */
    public static String CreditCardPaymentState(String customerId, String outTradeNo, 
        String signType, String privateKey) {

        Map<String, String> payProperty = new HashMap<String, String>();
        payProperty.put("customerId", customerId);
        payProperty.put("outTradeNo", outTradeNo);

        StringBuffer xmlStr = new StringBuffer();
        // XML头
        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
        // 支付接口
        xmlStr.append("<mpm>");
        // 接口名称：查询
        xmlStr.append("<service>QUERY</service>");
        // 请求参数
        xmlStr.append("<request>");
        // 客户ID, 在收款系统注册的ID
        xmlStr.append("<customerId>" + customerId + "</customerId>");
        // 外部交易号,生成规则：客户ID+订单编号+2位流水码
        xmlStr.append("<outTradeNo>" + outTradeNo + "</outTradeNo>");
        // 加密方式(MD5),目前只支持MD5
        xmlStr.append("<signType>" + signType + "</signType>");                 
        // 密文
        xmlStr.append("<sign>" + ServletSignUtil.buildSign(payProperty,privateKey) + "</sign>");       
        xmlStr.append("</request>");
        xmlStr.append("</mpm>");
        return xmlStr.toString();
    }

    /**
     * 信用卡担保转支付请求模版
     * @param customerId 客户ID, 在收款系统注册的ID
     * @param outTradeNo 外部交易号,生成规则：客户ID+订单编号+2位流水码
     * @param amount 支付金额,担保转支付金额 单位：元，格式1.00，支付金额不能大于担保金额
     * @param signType 加密方式,目前只支持MD5
     * @param privateKey 加密Key
     * @return String XML格式的支付请求
     */
    public static String CreditCardGuaranteeSwitchPayment(String customerId, String outTradeNo, 
        String amount, String signType, String privateKey) {
        Map<String, String> payProperty = new HashMap<String, String>();
        payProperty.put("customerId", customerId);
        payProperty.put("outTradeNo", outTradeNo);
        payProperty.put("amount", amount);

        StringBuffer xmlStr = new StringBuffer();
        // XML头
        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
        // 支付接口
        xmlStr.append("<mpm>");
        // 接口名称：担保转支付
        xmlStr.append("<service>ASSURETOPAY</service>");
        // 请求参数
        xmlStr.append("<request>");
        // 客户ID, 在收款系统注册的ID
        xmlStr.append("<customerId>" + customerId + "</customerId>");
        // 外部交易号,生成规则：客户ID+订单编号+2位流水码
        xmlStr.append("<outTradeNo>" + outTradeNo + "</outTradeNo>");
        // 支付金额,担保转支付金额 单位：元，格式1.00，支付金额不能大于担保金额
        xmlStr.append("<amount>" + amount + "</amount>");
        // 加密方式(MD5),目前只支持MD5
        xmlStr.append("<signType>" + signType + "</signType>");                 
        // 密文
        xmlStr.append("<sign>" + ServletSignUtil.buildSign(payProperty,privateKey) + "</sign>");       
        xmlStr.append("</request>");
        xmlStr.append("</mpm>");

        return xmlStr.toString();
    }
    /**
     * 更改信用卡信息请求模板
     * v2.9.3 add by shaojun.yang 2009-9-16
     * @param customerId 客户ID, 在收款系统注册的ID
     * @param outTradeNo 外部交易号,生成规则：客户ID+订单编号+2位流水码
     * @param creditCardId
     * @param signType 加密方式,目前只支持MD5
     * @param privateKey 加密Key
     * @return String XML格式的支付请求
     */
    public static String ChangeCreditCardSwitchPayment(String customerId, String outTradeNo, 
            String creditCardId, String signType, String privateKey) {
    	
    	 Map<String, String> payProperty = new HashMap<String, String>();
         payProperty.put("customerId", customerId);
         payProperty.put("outTradeNo", outTradeNo);
         payProperty.put("creditCardId", creditCardId);

         StringBuffer xmlStr = new StringBuffer();
         // XML头
         xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
         // 支付接口
         xmlStr.append("<mpm>");
         // 接口名称：更改信用卡信息
         xmlStr.append("<service>CHANGECARD</service>");
         // 请求参数
         xmlStr.append("<request>");
         // 客户ID, 在收款系统注册的ID
         xmlStr.append("<customerId>" + customerId + "</customerId>");
         // 外部交易号,生成规则：客户ID+订单编号+2位流水码
         xmlStr.append("<outTradeNo>" + outTradeNo + "</outTradeNo>");
         //信用卡ID
         xmlStr.append("<creditCardId>" + creditCardId + "</creditCardId>");
         // 加密方式(MD5),目前只支持MD5
         xmlStr.append("<signType>" + signType + "</signType>");                 
         // 密文
         xmlStr.append("<sign>" + ServletSignUtil.buildSign(payProperty,privateKey) + "</sign>");       
         xmlStr.append("</request>");
         xmlStr.append("</mpm>");

         return xmlStr.toString();
    	
    }
    
    /**
     * 担保转支付
     * add by haibo.li
     * 模版
     */
    public static String Charge(String amount,String outTradeNoStr,String customerId, String signType,String privateKey){
    	StringBuffer xmlStr = new StringBuffer();
    	//XML头
        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
        //支付接口
        xmlStr.append("<mpm>");
//      接口名称：担保转支付
        xmlStr.append("<service>ASSURETOPAY</service>");
        //请求参数
        xmlStr.append("<request>");
        //客户ID
        xmlStr.append("<customerId>" + customerId + "</customerId>");
        //外部交易号
        xmlStr.append("<outTradeNo>" +outTradeNoStr + "</outTradeNo>");
        //支付金额 
        xmlStr.append("<amount>" + amount + "</amount>");
        //加密方式 暂时只支持MD5
        xmlStr.append("<signType>"+signType+"</signType>");
        //密文
        xmlStr.append("<sign>"+privateKey+"</sign>");
        
        xmlStr.append("</request>");
        
        xmlStr.append("</mpm>");
        return xmlStr.toString();
    }
    
//    /**
//     * 预付退款
//     * add by haibo.li
//     */
//    public static String prePayCharge(String orderCD,String amount,String customerId,String currencyType,String outTradeNo,String refundReasonCode){
//    	StringBuffer xmlStr = new StringBuffer();
////    	XML头
//        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
////      支付接口
//        xmlStr.append("<mpm>");
////      接口名称：预付退款
//        xmlStr.append("<service>REFUNDINFOAPPLY</service>");
////      请求参数
//        xmlStr.append("<request>");
//        
////      请求参数
//        xmlStr.append("<customerId>"+customerId+"</customerId>");
//        
//        
////      流水号,初始值是10001
//        xmlStr.append("<outTradeNo>"+outTradeNo+"</outTradeNo>");
//        
//        
////      请求参数
//        xmlStr.append("<orderCd>"+orderCD+"</orderCd>");
//        
//        
//        //金额
//        xmlStr.append("<amount>"+amount+"</amount>");
//        
//        //币种
//        xmlStr.append("<currencyType>"+currencyType+"</currencyType>");
//        
//        
//        //请求参数
//        xmlStr.append("<refundModeCode></refundModeCode>");
//        
////      退款方式编码 ：RMIPSO-IPS（国际），RMTENCENTPAY-财付通，RMCITICPAY-中信在线，BBKTA-银行转帐，BCASH-现金，BPOS-POS刷卡.....
//        xmlStr.append("<refundReasonCode>"+refundReasonCode+"</refundReasonCode>");
//        
////      请求参数
//        xmlStr.append("<operator>52022</operator>");
//        
//        
////      请求参数
//        xmlStr.append("<accountName></accountName>");
//        
////      请求参数
//        xmlStr.append("<accountNo></accountNo>");
////      请求参数
//        xmlStr.append("<bankName></bankName>");
//        
////      请求参数
//        xmlStr.append("<created></created>");
//        
////      请求参数
//        xmlStr.append("<unCreated></unCreated>");
//        
//        //备注
//        xmlStr.append("<remark>此单金额转入新单:1006101006124 中</remark>");
//        
//        //加密方式
//        xmlStr.append("<signType>MD5</signType>");
//        
//        
//        //密文
//        xmlStr.append("<sign>2b9b8d6596962208a7ef29af9a5c2c48</sign>");
////      请求参数
//        xmlStr.append("</request>");
////      请求参数
//        xmlStr.append("</mpm>");
//        
//
//    	return xmlStr.toString();	
//    }
//    
//    
    
    
    /**
     * 获得密文
     * add by haibo.li
     */
    public static String buildSign(String customerId,String outTradeNoStr,String amount,String privateKey){
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("customerId", customerId);
    	params.put("outTradeNo", outTradeNoStr);
    	params.put("amount", amount);
    	return ServletSignUtil.buildSign(params, privateKey);
    	
    }
    
    
    
    

    /**
     * 
     * @return
     */
    public static Element setElementRoot(){
        // 创建根节点 list;  
        Element root = new Element("mpm");  
        // 将根节点添加到文档中；  
        Document Doc = new Document(root);  
        // 此处 for 循环可替换成 遍历 数据库表的结果集操作;  
        for (int i = 0; i < 5; i++) {  
            // 创建新节点 company;  
            Element elements = new Element("request");  
            // 给 company 节点添加属性 id;  
            elements.setAttribute("id", "" + i);  
            // 给 company 节点添加子节点并赋值  
            elements.addContent(new Element("customerId").setText("customerId" + i));  
            elements.addContent(new Element("outTradeNo").setText("outTradeNo" + i));  
            root.addContent(elements);  
        }  
        return root;
    }
}
