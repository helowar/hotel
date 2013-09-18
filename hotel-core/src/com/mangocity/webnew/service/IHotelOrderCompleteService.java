/**
 * 
 */
package com.mangocity.webnew.service;

import java.io.Serializable;
import java.util.Map;


import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

/**
 * 新版网站处理订单完成Service接口
 * 主要功能是生成订单，订单提交中台，扣取会员信用卡费用，积分，锁定代金
 * @author chenjiajie
 *
 */
public interface IHotelOrderCompleteService extends Serializable {
    /**
     * 客人使用信用卡支付或担保，和订单绑定 信用卡id在信用卡填写页面财务返回
     */
    public void bindOrderCreditCard(OrOrder order,String cardID);
    
    /**
	 * 获取订单配额, 如果获取成功则同时填充orderItems
	 * 
	 * @param order
	 * @return true:获取配额成功, false:没有全部获取到配额
	 */
    public int[] fillOrderItems(OrOrder order,HotelOrderFromBean hotelOrderFromBean);
    
    /**
     * 拆分入住人到OrOrderItem v2.4.2 2008-12-30
     * 
     * @param order
     * @return 每天各个房间的入住人姓名数组
     * @author chenjiajie
     */
    public String[] fillFellowNamesToOrderItem(OrOrder order);
    
    /**
     * 调用会员积分接口
     * @param orderNew
     * @param acturalAmount 使用的积分所对应的人民币数量
     * @param particialPay  是否使用积分
     * @throws Exception
     */
    public boolean deductUlmPoint(OrOrder orderNew, double acturalAmount, boolean particialPay) throws Exception ;
    
    /**
     * 使用全额积分支付或全代金券时，订单订单扣配额后，如果没有特殊要求就要发“即时确认”信息
     * @param member
     * @param order
     */
    public void sendImediateMessage(MemberDTO member,OrOrder order);
    
    /**
     * 调用代金券接口，扣减代金券 hotel2.9.3 add by chenjiajie 2009-09-04
     * @param params
     * @param order
     * @param member
     */
    public void deductUsedCoupon(Map params, OrOrder order,MemberDTO memberDTO);
    
    
    /**
     * 调用代金券接口，安全扣减代金券 hotel2.9.3 add by diandian.hou 2011-11-03
     * @param params
     * @param order
     * @param member
     */
    public void deductUsedCouponAndConfirm(Map params, OrOrder order,MemberDTO member);
    
    /**
     * 同步配额明细中的总价格到订单表，防止变价，导致总金额和配额明细中的金额不相等 add by shengwei.zuo 2010-6-4
     * @param order
     * @param isMinPrice
     * @return
     */
    public void synchroSumPriceToOrder(OrOrder order, Boolean isMinPrice);
    
    //如果变价得重新计算实收金额，用于完成页面的显示
    public double reCountActSum(HotelOrderFromBean hotelOrderFromBean,double sumRmbSl);
    
    /**
     * 保存返现相关信息
     * @param order
     * @param orderCd
     * @param hotelOrderFromBean
     * @param member
     */
    public void fillCashInformation(OrOrder order, String orderCd, HotelOrderFromBean hotelOrderFromBean,MemberDTO memberDTO);
}
