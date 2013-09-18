package com.mangocity.hagtb2b.web;

import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.base.util.B2BMember;
import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.B2bCookConstant;

public class B2bAgentOrderAction extends PaginateAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5036010534956963996L;
	

	
	@Override
	public String execute() {
		 Map params = super.getParams();
		 String tempStatus = (String) params.get("orderStatus");
		 String status1=null;//订单处理中
		 String status2=null;//订单预订成功
		 String status3=null;//已入住
		 String status4=null;//正常退房
		//String status5=null;//提前退房
		 String status6=null;//NoShow
		 String status7=null;//撤单
		 String status8=null;//已付款
		 String status9=null;//预订成功
		 String status10=null;//部分noshow

		 if("1".equals(tempStatus)){
			 status1="3,9,10,12";
		 }
		 if("2".equals(tempStatus)){
			// status2="7,8";
			 status2="7";
		 }
		 if("3".equals(tempStatus)){
			 status3="4";
		 }
		 if("4".equals(tempStatus)){
			 status4="6";
		 }
		 /*if("5".equals(tempStatus)){
			 status5="5";
		 }*/
		 if("6".equals(tempStatus)){
			 status6="13";
		 }
		 if("7".equals(tempStatus)){
			 status7="14";
		 }
		 if("8".equals(tempStatus)){
			 status8="8";
		 }
		 if("9".equals(tempStatus)){
			 status9="9";
		 }
		 if("10".equals(tempStatus)){
			 status10="5";
		 }
		 params.put("status1", status1);
		 params.put("status2", status2);
		 params.put("status3", status3);
		 params.put("status4", status4);
		 //params.put("status5", status5);
		 params.put("status6", status6);
		 params.put("status7", status7);
		 params.put("status8", status8);
		 params.put("status9", status9);
		 params.put("status10", status10);
		 //订单cd如果13位精确查询，少于13位模糊查询
		 String orderCD = (String) params.get("orderCD");
		 if(orderCD!=null && orderCD.length()==13){
			 params.put("isCorrectOrderCD", 1);
		 }else{
			 params.put("isCorrectOrderCD", 0);
		 }
		 
		 //WebUtils.setAgentCode(super.request, super.getHttpResponse()); //用于测试获取B2B代理  根据cookie获取会员信息
	     B2BMember b2bmemberinfo=new B2BMember();
		   //从cookie中获取B2B代理网站登陆的会员资料 add by shizhongwen 2009-1-5 
	     b2bmemberinfo=getB2BMemberInfoForWeb();
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
	     params.put("manageSign", 1);
		try{
			return super.execute();	
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return "forwardToError";
		}
	}
	
	/**
     * Refactor---这个方法是从GenericCCAction中迁移过来，因为这个方法当前只被当前类所调用
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
