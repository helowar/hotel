package com.mangocity.hotel.dreamweb.booking.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.dreamweb.booking.service.IHotelBookQueryService;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.util.action.GenericWebAction;
import com.opensymphony.xwork2.ActionContext;

public class HotelBookQueryAction extends GenericWebAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6707205593779088939L;

	private static final String SIMPLEDATAFORMAT_STRING = "yyyy-MM-dd";

	private static final MyLog log = MyLog.getLogger(HotelBookQueryAction.class);

	private IHotelBookQueryService hotelBookQueryService;

	/**
	 * @return the hotelBookQueryService
	 */
	public IHotelBookQueryService getHotelBookQueryService() {
		return hotelBookQueryService;
	}

	/**
	 * @param hotelBookQueryService
	 *            the hotelBookQueryService to set
	 */
	public void setHotelBookQueryService(IHotelBookQueryService hotelBookQueryService) {
		this.hotelBookQueryService = hotelBookQueryService;
	}

	public String execute() {
		String result = null;
		ActionContext ctx = ActionContext.getContext();
		HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
		response.setStatus(1);
		// result = checkOrderDuplication();
		return result;
	}

	public void checkOrderDuplication() {
		// 初始化返回结果
		String result = null;
		// 设置需要转换的日期格式
		SimpleDateFormat sf = new SimpleDateFormat(HotelBookQueryAction.SIMPLEDATAFORMAT_STRING);
		// String chineseRegx = "^[\u4e00-\u9fa5]";
		// String chineseRegx = "[^x00-xff]* ";

		// HttpHeaderResult headResult = new HttpHeaderResult();

		// 初始化页面传来的参数值***************bengin
		Long hotelId = null;
		Long roomTypeId = null;
		String linkMan = "";
		String linkPhone = "";
		Date checkInDate = null;
		String checkInDateStr = null;
		Date checkOutDate = null;
		String checkOutDateStr = null;
		// 初始化页面传来的参数值***************end
		// 异常附加信息初始化
		String exceptionExtendInfo = "";
		// 从上下文中获取request和response对象
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
		try {
			// 解析从前台传入的各种参数的值*******************begin
			if (null != request.getParameter("hotelid") && !request.getParameter("hotelid").equalsIgnoreCase("undefined")) {
				hotelId = Long.parseLong(URLDecoder.decode(request.getParameter("hotelid"), "UTF-8"));
			}

			if (null != request.getParameter("roomtypeid") && !request.getParameter("roomtypeid").equalsIgnoreCase("undefined")) {
				roomTypeId = Long.parseLong(URLDecoder.decode(request.getParameter("roomtypeid"), "UTF-8"));
			}
			linkMan = request.getParameter("linkman");

			if (linkMan != null) {
				linkMan = URLDecoder.decode(linkMan, "UTF-8");
			}

			linkPhone = request.getParameter("mobile");

			if (StringUtil.isValidStr(linkPhone)) {
				linkPhone = URLDecoder.decode(linkPhone, "UTF-8");
			}

			checkInDateStr = request.getParameter("checkindate");
			if (StringUtil.isValidStr(checkInDateStr)) {
				checkInDateStr = URLDecoder.decode(checkInDateStr, "UTF-8");
			}

			checkOutDateStr = request.getParameter("checkoutdate");
			if (StringUtil.isValidStr(checkOutDateStr)) {
				checkOutDateStr = URLDecoder.decode(checkOutDateStr, "UTF-8");
			}
			// String json= request.getParameter("data");
			// JSONObject jsonObject = JSONObject.fromObject(json);
			// 解析从前台传入的各种参数的值*******************end
			
			if (checkInDateStr == null || checkOutDateStr == null) {
				sendMsg("-1", response);
			} else {
				checkInDate = sf.parse(checkInDateStr);
				checkOutDate = sf.parse(checkOutDateStr);

				// 异常附加信息
				exceptionExtendInfo = "附加值为：" + "hotelId=" + hotelId + ";roomTypeId=" + roomTypeId + ";linkMan=" + linkMan + ";linkTelephone=" + linkPhone
						+ ";checkInDate=" + checkInDateStr + ":checkOutDate=" + checkOutDateStr;
				// System.out.println("userId is :"+userId);

				result = hotelBookQueryService.checkOrderDuplication(hotelId, roomTypeId, linkMan, linkPhone, checkInDate, checkOutDate);
				// headResult.addHeader("meesage", result);
				sendMsg(result, response);
				log.info("*************订单重复校验输出值： " + result + exceptionExtendInfo);
			}
		} catch (ParseException e) {
			log.error(exceptionExtendInfo + ": ", e);
		} catch (UnsupportedEncodingException e) {
			log.error(exceptionExtendInfo + ": ", e);
		} catch (Exception e) {

			log.error(exceptionExtendInfo + ": ", e);
		}

	}

	public void sendMsg(String content, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			if (writer != null) {
				writer.write(content);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			log.error("sendMsg error", e);
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	public void sendJson(String content, HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {
		String callback = request.getParameter("callback");
		JSONObject res = new JSONObject();
		res.put("d", "dddd");
		// 可以避免前台显示出现乱码
		PrintWriter out = null;
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			if (out != null) {
				out = response.getWriter();
				out.print(callback + "(" + res.toString() + ")");
			}

		} catch (Exception e) {
			log.error("sendJson has a wrong:", e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

}
