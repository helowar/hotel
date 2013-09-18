package com.mangocity.webnew.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.IHotelCheckOrderService;
import com.mangocity.webnew.util.action.GenericWebAction;
import com.mangocity.hotel.ext.member.dto.MemberDTO;

/**
 * 校验是否是已注册会员
 * @author zuoshengwei
 *
 */
public class MemberNoLogonAction extends  GenericWebAction {
	

	private static final long serialVersionUID = 46244827472442088L;

	/**
	 * 订单审核service接口，包括一些对会员信息获取的方法封装 
	 */
	private IHotelCheckOrderService hotelCheckOrderService;
	
	private List<MemberDTO> lstCurrMember = new ArrayList<MemberDTO>();
	
	//新注册的会员CD
	private String  registerNewMemberCD;
	
    // 联系人手机号码
    private String mobile;

    // 联系人邮箱
    private String email;
	
	public String execute(){
		
		Map params = super.getParams();
		
	    if (null == hotelOrderFromBean) {
	            hotelOrderFromBean = new HotelOrderFromBean();
	     }
	    
	     MyBeanUtil.copyProperties(hotelOrderFromBean, params);
	     
	     Cookie[] cookies = request.getCookies();
	     
	     //默认为老会员
	     memClass=1;
	     
	     hotelOrderFromBean.setEmail(email);
	    	 
	     hotelOrderFromBean.setMobile(mobile);
		
	     //根据输入的手机号或者Emain判断，是否是已注册过的会员。
		 lstCurrMember = hotelCheckOrderService.getMemberLst(hotelOrderFromBean);
		
		//如果为空，就说明该手机号或者Emain没有注册过，此时就要新注册一个会员，返回一个memeberCD;
		if(lstCurrMember==null||lstCurrMember.isEmpty()){
			
			try{
				
			//	registerNewMemberCD = hotelCheckOrderService.registerNewMember(hotelOrderFromBean,cookies);
				
				if(registerNewMemberCD==null||"".equals(registerNewMemberCD)){
					
					//如果注册返回的会员CD为空，则改为公共会员
					memClass=3;
					
				}else{
					//注册成功，就为新会员
					memClass=2;
					
				}
				
				
				
			}catch (Exception e) {
				
				//如果注册会员失败，就以公共会员下单
				memClass = 3;
				log.error("getNewMemberByMobile or getMemberByEmail error " + e);
				
			}
			
		}
		
		return "showMemberCD";
		
	}

	public IHotelCheckOrderService getHotelCheckOrderService() {
		return hotelCheckOrderService;
	}

	public void setHotelCheckOrderService(
			IHotelCheckOrderService hotelCheckOrderService) {
		this.hotelCheckOrderService = hotelCheckOrderService;
	}

	public List<MemberDTO> getLstCurrMember() {
		return lstCurrMember;
	}

	public void setLstCurrMember(List<MemberDTO> lstCurrMember) {
		this.lstCurrMember = lstCurrMember;
	}

	public String getRegisterNewMemberCD() {
		return registerNewMemberCD;
	}

	public void setRegisterNewMemberCD(String registerNewMemberCD) {
		this.registerNewMemberCD = registerNewMemberCD;
	}

	public HotelOrderFromBean getHotelOrderFromBean() {
		return hotelOrderFromBean;
	}

	public void setHotelOrderFromBean(HotelOrderFromBean hotelOrderFromBean) {
		this.hotelOrderFromBean = hotelOrderFromBean;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
