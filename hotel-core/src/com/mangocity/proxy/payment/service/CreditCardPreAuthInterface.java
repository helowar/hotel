package com.mangocity.proxy.payment.service;


public interface CreditCardPreAuthInterface {


    /**
     * 生成预授权工单到CTIIWeb系统
     * 
     * @param orderCode
     *            订单编号（不是ID）
     * @param orderType
     *            订单来源，酒店为(HOTEL)、机票请填(TICKET)
     * @param preAuthAmount
     *            担保金额（保留两位小数）
     * @param memberId
     *            （会员ID，不是CD）
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
            double preAuthAmount, String memberCD, String loginName,
            String creditCardIds, String sid, String description, String prePayType, String currencyType) throws Exception;
    
    /**
     * 重载createPreAuthList()方法
     * 
     * @return
     * @throws Exception 
     */
    public String createPreAuthList(String orderCode, String orderType,
            double preAuthAmount, String memberCD, String loginName,
            String creditCardIds, String sid, String description, String prePayType, int installment, String currencyType) throws Exception;
    
    /**
     * 返回指定订单编号的预授权工单对象
     * 
     * @param orderType
     *            订单类型，机票订单（TICKET）、酒店订单（HOTEL）
     * @param orderCode
     *            订单编码
     * @return List<PreAuthList>
     */

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
    public String getPreAuthSucceedFlag(String orderType, String orderCode);

    /**
     * 根据信用卡Id获取信用卡历史交易记录
     * 
     * @param creditCardId
     *            信用卡Id
     * @return 信用卡历史交易记录
     */
    public String getPreAuthTransactionRecordsByCreditcardId(long creditCardId);
    
    public  String ChangeCreditCard(String orderCode,String creditCardId);
    
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
    public String payCharge(double chargeSum,String orderCD);

}
