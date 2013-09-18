package com.mangocity.hotel.order.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.vch.app.service.VchService;
import com.mangocity.vch.app.service.exception.VCHException;

/**
 * 代金券管理接口 hotel2.9.3
 * @author chenjiajie
 *
 */
public interface IVoucherInterfaceService extends Serializable {
	
	/**
	 * CC渠道
	 */
	public final static String CHANNEL_CC = VchService.USED_BY_CC;
	
	/**
	 * 网站渠道
	 */
	public final static String CHANNEL_WEB = VchService.USED_BY_WEB;
	/**
	 * 繁体网站(代金券没有该渠道) add by diandian.hou
	 */
	public final static String CHANNEL_HKWEB = "USED_BY_HKWEB";
	
	/**
	 * 调用代金券接口预订确认接口 hotel2.9.3 add by chenjiajie 2009-09-02
	 * 
	 * @param orderData
	 * @param voucherCodeList
	 * @param order
	 * @param roleUser
	 * @return
	 * @throws VCHException
	 */
	public int callVchServiceOrder(Map<String, String> orderData,
			List<String> voucherCodeList, OrOrder order, UserWrapper roleUser)
			throws VCHException;

	/**
	 * 调用代金券接口取消接口 hotel2.9.3 add by chenjiajie 2009-09-02
	 * 
	 * @param order
	 * @param roleUser
	 */
	public void callVchServiceCancelOrder(OrOrder order, UserWrapper roleUser)
			throws VCHException;

	/**
	 * 调用代金券接口试预订接口 hotel2.9.3 add by chenjiajie 2009-09-02
	 * @param order
	 * @param roleUser
	 * @param member
	 * @param channel
	 * @return
	 * @throws VCHException
	 */
	public int callVchServicePreOrder(OrOrder order,UserWrapper roleUser,MemberDTO memberDTO,String channel) throws VCHException;

	/**
	 * 调用代金券接口失败的时候回滚曹错 hotel2.9.3 add by chenjiajie 2009-09-11
	 * 
	 * @param order
	 * @param reason
	 */
	public void rollBackVchOrderState(OrOrder order, String reason);

	/**
	 * 调用代金券接口预订确认接口 hotel2.9.3 add by chenjiajie 2009-09-02
	 * 
	 * @param order
	 * @param userName
	 * @return
	 * @throws VCHException
	 */
	public void callVchServiceOrder(OrOrder order, String userName)
			throws VCHException;
	
	/**
	 * 确认使用代金券的方法
	 * @param order
	 * @param roleUser
	 * @param member
	 */
	public void confirmVoucherState(OrOrder order, UserWrapper roleUser, MemberDTO memberDTO);
}
