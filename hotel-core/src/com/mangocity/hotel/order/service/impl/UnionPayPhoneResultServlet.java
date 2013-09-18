package com.mangocity.hotel.order.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mangocity.hotel.base.service.PayOutCallBackForUpdateOrderPrepayStatusService;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.util.HotelPayOnlieUtil;

/**
 * 银联电话支付财务回调处理Servlet
 * @author wupingxiang
 *
 */
public class UnionPayPhoneResultServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final MyLog logger = MyLog.getLogger(UnionPayPhoneResultServlet.class);
	
	/** 外部订单号 in */
	private String outTradeNo;

	/** 支付结果 in */
	private String payResult;

	/** 支付结果消息内容 in */
	private String payMessage;

	/** 支付时间 in */
	private String payTime;

	/** 支付金额 in */
	private String payAmount;

	/** 通知类型 in */
	private String notifyType;

	/** 签名类型 in */
	private String signType;

	/** 签名 in */
	private String sign;

	/** 操作员 in */
	private String operator;
	
	String result = "";
	private PayOutCallBackForUpdateOrderPrepayStatusService payOutCallBackForUpdateOrderPrepayStatusService;
	
	private IOrderService orderService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//初始化参数
		initParams(request);
        
		String flag = "S";
		String orderCD = outTradeNo.substring(4, outTradeNo.length() - 2);
		if (!validateParams()){
			OrOrder order = orderService.getOrOrderByOrderCd(orderCD);
			recordHandleLog(order,"银联电话支付回调处理失败：参数校验错误");
			logger.error(outTradeNo + result);
			flag = "F";
			response.getWriter().print(flag);
			return ;
		}
		//组装接收参数
		Map<String,String> params = new HashMap<String,String>();
		buildReceiveParams(params);
		
		//校验加密信息
		String buildSign = HotelPayOnlieUtil.buildSign(params, HotelPayOnlieUtil.PSD_KEY);
		logger.info("buildSign=" + buildSign);
		logger.info("sign="+sign);
		if (!sign.equals(buildSign)){
			OrOrder order = orderService.getOrOrderByOrderCd(orderCD);
			recordHandleLog(order,"银联电话支付回调处理失败：签名验证错误");
			logger.error(outTradeNo + ":加密信息错误！");
			flag = "F";
			response.getWriter().print(flag);
			return ;
		}
		
		//处理支付结果
		boolean isPaySucc = "success".equals(payResult) || "SUCCESS".equals(payResult);
		try {
			payOutCallBackForUpdateOrderPrepayStatusService.updateOrderUnionPayPhone(orderCD, isPaySucc,operator);
		} catch (Exception e) {
			OrOrder order = orderService.getOrOrderByOrderCd(orderCD);
			recordHandleLog(order,"银联电话支付回调处理失败：系统处理出错");
			flag = "F";
			logger.error(outTradeNo + ":回调存储失败：",e);
		}
		
		response.getWriter().print(flag);
	}

	/**
	 * 接收参数
	 * @param request
	 */
	private void initParams(HttpServletRequest request) {
		ServletContext sc = request.getSession().getServletContext();
        WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(sc);
        payOutCallBackForUpdateOrderPrepayStatusService = (PayOutCallBackForUpdateOrderPrepayStatusService)wc.getBean("payOutCallBackForUpdateOrderPrepayStatusService");
        orderService = (IOrderService) wc.getBean("orderService");
        try {
			outTradeNo = URLEncoder.encode(request.getParameter("outTradeNo"), "UTF-8");
			payResult = URLEncoder.encode(request.getParameter("payResult"), "UTF-8");
			payTime = request.getParameter("payTime");
			payAmount = URLEncoder.encode(request.getParameter("payAmount"), "UTF-8");
			notifyType = URLEncoder.encode(request.getParameter("notifyType"), "UTF-8");
			signType = URLEncoder.encode(request.getParameter("signType"), "UTF-8");
			sign = URLEncoder.encode(request.getParameter("sign"), "UTF-8");
			operator = URLEncoder.encode(request.getParameter("operator"), "UTF-8");
			payMessage = URLEncoder.encode(request.getParameter("payMessage"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("outTradeNo=" + outTradeNo + "payResult=" + payResult+ "payTime=" + payTime+ "payAmount=" + payAmount+ "notifyType=" + notifyType+ "operator=" + operator+ "payMessage=" + payMessage);
	}

	/**
	 * 参数校验
	 */
	private boolean validateParams() {
		boolean succ = true;
		if (null == outTradeNo || "".equals(outTradeNo)){
			result += "outTradeNo不能为空|";
			succ = false;
		}
		if (null == payResult || "".equals(payResult)){
			result += "payResult不能为空|";
			succ = false;
		}
		if (null == payMessage){
			result += "payMessage不能为空|";
			succ = false;
		}
		if (null == payTime || "".equals(payTime)){
			result += "payTime不能为空|";
			succ = false;
		}
		if (null == payAmount || "".equals(payAmount)){
			result += "payAmount不能为空|";
			succ = false;
		}
		if (null == operator || "".equals(operator)){
			result += "operator不能为空|";
			succ = false;
		}
		if (null == notifyType || "".equals(notifyType)){
			result += "outTradeNo不能为空|";
			succ = false;
		}
		if (null == signType || "".equals(signType)){
			result += "signType不能为空|";
			succ = false;
		}
		if (null == sign || "".equals(sign)){
			result += "sign不能为空|";
			succ = false;
		}
		return succ;
	}
	
	/**
	 * 组装接收参数
	 * @param params
	 */
	private void buildReceiveParams(Map<String, String> params) {
		params.put("outTradeNo", outTradeNo);
		params.put("payResult", payResult);
		params.put("payMessage", payMessage);
		params.put("payTime", payTime);
		params.put("payAmount", payAmount);
		params.put("operator", operator);
		params.put("notifyType", notifyType);
	}
	
	/**
	 * 记录日志
	 */
	private void recordHandleLog(OrOrder order,String content) {
		OrHandleLog handleLog = new OrHandleLog();
        handleLog.setOrder(order);
        handleLog.setModifiedTime(new Date());
        handleLog.setModifierName("system");
        handleLog.setModifierRole("system");
        handleLog.setContent(content);
        order.getLogList().add(handleLog);
        orderService.saveOrUpdate(order);
	}
}
