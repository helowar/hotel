package com.mangocity.webco.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.webnew.util.action.GenericWebAction;

/**
 * 订单审核Action
 * @author chenjiajie
 *
 */
public class HotelCheckAction extends GenericWebAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2946171817207038145L;
	
	/**
	 * 订单审核service接口，包括一些对会员信息获取的方法封装 
	 */
	//private IHotelCheckOrderService hotelCheckOrderService;
	
	/**
	 * 会员id
	 */
	private String memberId;
	
    /**
     * 计算半个小时超时
     */
    private String overHalfDate;
    
    /**
     * 用于保存香港中科获取的新价格，以便后续生成订单明细 ADD BY WUYUN 2009-04-21
     */
    private String hkPrices;

    /**
     * 用于保存香港中科获取的底价，以便后续生成订单明细
     */
    private String hkBasePrices;
    
	/**
	 * 跳转到订单审核页面的方法
	 */
	public String execute() {
		Map params = super.getParams();
		//封装订单基本数据，还包括预订条款、客户确认方式、会员信息等 (不分面预付/面付担保单) 
		super.fillOrderBaseInfo();
		
		//如果是已登录会员 add by shengwei.zuo 2009-11-29
//		if(!hotelOrderFromBean.isDireckbook()){
//			
//			//写入联系人信息到cookie add by shengwei.zuo 2009-11-29
//		   setCityCookie();
//			
//		}
		
		OrFulfillment orFulfillment = new OrFulfillment();
		MyBeanUtil.copyProperties(orFulfillment, params);
		request.setAttribute("orFulfillment", orFulfillment);
		
		overHalfDate = DateUtil.formatDateToYMDHMS(DateUtil.getSystemDate());// 开始计时
		
        return SUCCESS;
	}
	
	/**
	 * 封装担保信息
	 */
	private void fillAssureInfo(){
		Map params = super.getParams();
		try {
            reservation.setReservSuretyPrice(0.0d);
            if (PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod())
                && !hotelOrderFromBean.hasPayToPrepay()) {
                String assureDetailString = (String) params.get("assureDetailStr");
                //计算订单的担保金额，并封装数据提供页面显示 
//                hotelCheckOrderService.calculateSuretyAmount(assureDetailString, hotelOrderFromBean, order, reservation);
            }
        } catch (Exception e) {
           log.error("ErrCode:65893 HotelCheckAction.fillAssureInfo() throw exception", e);
        }
	}
	
	//联系人信息 cookie 设置 add by shengwei.zuo 2009-11-27
    private void setCityCookie(){
    	
		try {
			
			if(hotelOrderFromBean.getLinkMan()!=null&&!"".equals(hotelOrderFromBean.getLinkMan())){
				//把 联系人 的姓名 放到cookie里
		    	Cookie linkManCookie = new Cookie("linkManCookie",URLEncoder.encode(hotelOrderFromBean.getLinkMan(), "UTF-8"));
		    	//生命周期    
		    	linkManCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(linkManCookie);
			}
			
			if(hotelOrderFromBean.getConfirmtype()!=null&&!"".equals(hotelOrderFromBean.getConfirmtype())){
				
				//把预定确认方式放到cookie里
		    	Cookie confirmtypeCookie = new Cookie("confirmtypeCookie",hotelOrderFromBean.getConfirmtype());
		    	//生命周期 
		    	confirmtypeCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(confirmtypeCookie);
			}
			
			if(hotelOrderFromBean.getMobile()!=null&&!"".equals(hotelOrderFromBean.getMobile())){
				
				//把手机号码 放到cookie里
		    	Cookie mobileCookie = new Cookie("mobileCookie",hotelOrderFromBean.getMobile());
		    	mobileCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(mobileCookie);
			}
			
			//固定电话  begin
			if(hotelOrderFromBean.getFixedDistrictNum()!=null&&!"".equals(hotelOrderFromBean.getFixedDistrictNum())){
							
				//把固定电话区号  放到cookie里
				Cookie fixedDistrictNumCookie = new Cookie("fixedDistrictNumCookie",hotelOrderFromBean.getFixedDistrictNum());
				fixedDistrictNumCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(fixedDistrictNumCookie);
			}
			
			if(hotelOrderFromBean.getFixedPhone()!=null&&!"".equals(hotelOrderFromBean.getFixedPhone())){
				
				//把固定电话 电话号码  放到cookie里
				Cookie fixedPhoneCookie = new Cookie("fixedPhoneCookie",hotelOrderFromBean.getFixedPhone());
				fixedPhoneCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(fixedPhoneCookie);
			}
			
			if(hotelOrderFromBean.getFixedExtension()!=null&&!"".equals(hotelOrderFromBean.getFixedExtension())){
				
				//把固定电话 分机号  放到cookie里
				Cookie fixedExtensionCookie = new Cookie("fixedExtensionCookie",hotelOrderFromBean.getFixedExtension());
				fixedExtensionCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(fixedExtensionCookie);
			}
			//固定电话  end
			
			
			if(hotelOrderFromBean.getEmail()!=null&&!"".equals(hotelOrderFromBean.getEmail())){
				
				//把Email码 放到cookie里
		    	Cookie emailCookie = new Cookie("emailCookie",hotelOrderFromBean.getEmail());
		    	emailCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(emailCookie);
			}
			
			//传真号码  begin
			if(hotelOrderFromBean.getFaxDistrictNum()!=null&&!"".equals(hotelOrderFromBean.getFaxDistrictNum())){
							
				//把传真号码区号  放到cookie里
				Cookie faxDistrictNumCookie = new Cookie("faxDistrictNumCookie",hotelOrderFromBean.getFaxDistrictNum());
				faxDistrictNumCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(faxDistrictNumCookie);
			}
			
			if(hotelOrderFromBean.getFaxPhone()!=null&&!"".equals(hotelOrderFromBean.getFaxPhone())){
				
				//把传真号码 电话号码  放到cookie里
				Cookie faxPhoneCookie = new Cookie("faxPhoneCookie",hotelOrderFromBean.getFaxPhone());
				faxPhoneCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(faxPhoneCookie);
			}
			
			if(hotelOrderFromBean.getFaxExtension()!=null&&!"".equals(hotelOrderFromBean.getFaxExtension())){
				
				//把传真号码 分机号  放到cookie里
				Cookie faxExtensionCookie = new Cookie("faxExtensionCookie",hotelOrderFromBean.getFaxExtension());
				faxExtensionCookie.setMaxAge(60*60*24*365);
		    	getHttpResponse().addCookie(faxExtensionCookie);
			}
			//传真号码  end
			
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}
    	
    }
	
	/** getter and setter **/
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOverHalfDate() {
		return overHalfDate;
	}

	public void setOverHalfDate(String overHalfDate) {
		this.overHalfDate = overHalfDate;
	}

	public String getHkPrices() {
		return hkPrices;
	}

	public void setHkPrices(String hkPrices) {
		this.hkPrices = hkPrices;
	}

	public String getHkBasePrices() {
		return hkBasePrices;
	}

	public void setHkBasePrices(String hkBasePrices) {
		this.hkBasePrices = hkBasePrices;
	}
}
