package com.mangocity.hagtb2b.web;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.base.util.B2BMember;
import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.B2bCookConstant;
import com.mangocity.util.DateUtil;

public class B2bMemberUnCommitOrderAction extends PaginateAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5036010534956963996L;
	

	
	@Override
	public String execute() {
		
		 Map params = super.getParams();
		// WebUtils.setAgentCode(super.request, super.getHttpResponse()); //用于测试获取B2B代理  根据cookie获取会员信息
	     B2BMember b2bmemberinfo=new B2BMember();
		   //从cookie中获取B2B代理网站登陆的会员资料 add by shizhongwen 2009-1-5 
	     b2bmemberinfo=getB2BMemberInfoForWeb();
	     request.setAttribute("currutDate",DateUtil.formatDateToYMDHMS1(new Date()));
	     
	     if(null!=b2bmemberinfo&& null!=b2bmemberinfo.getAgentCode() && !"".equals(b2bmemberinfo.getAgentCode())){
	    	 params.put("memberCD", b2bmemberinfo.getAgentCode());
	    	 params.put("operaterId", b2bmemberinfo.getOperaterId());
	    	 params.put("manageSign", b2bmemberinfo.getManageSign());
		}else{			
			 return forwardError("请登陆");
		}
	     if(null!=b2bmemberinfo&& null!=b2bmemberinfo.getOperaterId() && !"".equals(b2bmemberinfo.getOperaterId())){
	    	 params.put("operaterId", b2bmemberinfo.getOperaterId());
	     }
		
		try{
			return super.execute();	
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return "forwardToError";
		}
	}
	
	/**
     * 从cookie中获取B2B代理网站登陆的会员资料
     * @return
     */
    private B2BMember getB2BMemberInfoForWeb(){ 
    	Cookie[] cookies = request.getCookies();    	
    	B2BMember b2bmember = new B2BMember();
		for (Cookie cookie : cookies) {				
			String sname = cookie.getName();	
			if (B2bCookConstant.agentId.equals(sname)) {
				//b2b代理商ID
				b2bmember.setAgentId(cookie.getValue());	
			}
			if (B2bCookConstant.agentName.equals(sname)) {
				//b2b代理商名称
				b2bmember.setAgentName(cookie.getValue());
			}
			if (B2bCookConstant.agentCode.equals(sname)) {
				//b2b代理商编码code (与会员绑定，即会员Code)
				b2bmember.setAgentCode(cookie.getValue());					
			}
			if (B2bCookConstant.saleCurrency.equals(sname)) {
				//b2b销售币种
				b2bmember.setSaleCurrency(cookie.getValue());
			}
			if (B2bCookConstant.balanceCurrency.equals(sname)) {
				//b2b结算币种
				b2bmember.setBalanceCurrency(cookie.getValue());
			}
			if (B2bCookConstant.rate.equals(sname)) {
				//b2b汇率
				b2bmember.setRate(cookie.getValue());
			}
			if (B2bCookConstant.footWay.equals(sname)) {
				//b2b类型
				b2bmember.setFootWay(cookie.getValue());
			}
			if (B2bCookConstant.operaterId.equals(sname)) {
				//b2b操作者
				b2bmember.setOperaterId(cookie.getValue());
			}
			if (B2bCookConstant.MANAGESING_KEY.equals(sname)) {
				//b2b操作者
				b2bmember.setManageSign(cookie.getValue());
			}
		}

		return b2bmember;
	}

}
