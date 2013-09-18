/**
 * 
 */
package com.mangocity.tmchotel.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.base.service.assistant.AssureInforAssistant;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.tmchotel.persistence.HotelOrderFromBean;

/**
 * 订单审核service接口，包括一些对会员信息获取的方法封装
 * 
 * @author chenjiajie
 * 
 */
public interface IHotelCheckOrderService extends Serializable {

	/**
	 * 已存在会员的返回结果常量
	 */
	public final String EXISTENT_MEMBER = "exist";

	/**
	 * 判断是继续预定还是已经登陆 如果是继续预订，则要搜索会员或者注册新会员 
	 * 
	 * @author chenjiajie
	 * @param hotelOrderFromBean 界面传递的订单Bean
	 * @param cookies request传递的cookies
	 * @param agentid 代理商id，可以为null
	 * @return
	 * <ol>
	 * <li>returnNewMemberCd=IHotelCheckOrderService.EXISTENT_MEMBER,会员已经存在</li>
	 * <li>returnNewMemberCd=新的会员CD,表明要注册会员,并注册了会员</li>
	 * </ol>
	 */
	public String searchRegisterMember(HotelOrderFromBean hotelOrderFromBean,
			Cookie[] cookies, String agentid);
	
	/**
	 * 计算订单的担保金额，并封装数据提供页面显示
	 * 
	 * @author chenjiajie
	 * @param assureDetailString 担保明细Str
	 * @param hotelOrderFromBean
	 * @param order
	 * @param reservation
	 */
//	public void calculateSuretyAmount(String assureDetailString,HotelOrderFromBean hotelOrderFromBean,
//			OrOrder order,OrReservation reservation) throws Exception;
	public double calculateSuretyAmount(
			List<AssureInforAssistant> assureDetailLit,
			HotelOrderFromBean hotelOrderFromBean);
	
	/**
	 * 根据输入的邮箱和手机号码，查询出会员CD List;
	 * @param hotelOrderFromBean
	 * @return
	 * add by shengwei.zuo 2009-11-15
	 */
	public List<MemberDTO>  getMemberLst(HotelOrderFromBean hotelOrderFromBean);
	
	/**
	 * 如果不存在重复的,则注册新的会员
	 * @param hotelOrderFromBean
	 * @param cookies
	 * @return
	 * add by shengwei.zuo 2009-11-15
	 */
	public String registerNewMember(HotelOrderFromBean hotelOrderFromBean,Cookie[] cookies);
	
}
