package com.mangocity.hotel.dreamweb.ordercancel.action;

import java.util.Map;

import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.dreamweb.ordercancel.service.OrderCancelService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.util.log.MyLog;

public class OrderCancelAction extends GenericAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2841291669151197112L;
	
	private static final MyLog log = MyLog.getLogger(OrderCancelAction.class);
	
	private OrderCancelService orderCancelService;
	
	private IOrderService orderService;
	
	
	/**
	 * @param orderCancelService the orderCancelService to set
	 */
	public void setOrderCancelService(OrderCancelService orderCancelService) {
		this.orderCancelService = orderCancelService;
	}
	
    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }


	public String execute() {
		
		//请求类型变量初始化，分为main
		String requestType = null;
		//action跳转变量初始化
		String forward = null;
		//会员id变量初始化
		String memberCd = null;
		try{
			//会员id变量赋值
			memberCd = getMemberCdForWeb();
			//通过会员id获得会员实体类
			MemberDTO member = this.getMemberByMemberCd(memberCd);
			//从页面请求得到请求的类型值
			requestType = request.getParameter("requestType");

			//如果请求类型为main则不执行订单取消处理逻辑，而是保存一些关键参数然后直接跳转至订单取消主页面
			if(null != requestType &&"main".equalsIgnoreCase(requestType)){
				forward = requestType;
				orderCancelService.saveParams(request,member);
				return forward;
			}
			orderCancelService.service(request, member);
			forward = "success";	
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			return forwardError("网站取消订单报错 "+e);
		}

		return forward;
	}
	
	/**
	 * 判定会员是否处于有效登录状态
	 * 从cookie中获取网站登陆的会员资料,如memberId、memberCD
	 * @return result 如果为null则是有效登录中,否则提示重新登录
	 */
	private String validLoginStates(){
		String result = null;
        Map params = super.getParams();
        try {
            String memberCd = getMemberCdForWeb();
            if (null == memberCd || "".equals(memberCd)) {
                return forwardError("请登陆");
            } else {
            	MemberDTO member = getMemberSimpleInfoByMemberCd(memberCd, true);
                if (null == member) {
                    return forwardError("请登陆");
                } else {

                    if (null != params.get("memberCD") && null != params.get("memberId")) {
                        if (!member.getMembercd().equals(params.get("memberCD").toString())) {
                            return forwardError("请重新登陆");
                        }
                    }else{
                    	return forwardError("对不起，您访问的url有误!");
                    }
                }
            }
        } catch (Exception e) {
            log.error("getMemberInfoForWeb is Error" + e);
        }
        return result;
	}

}
