package com.mangocity.hotel.dreamweb.search.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.search.album.service.HotelQuotionQueryService;
import com.mangocity.hotel.search.album.service.impl.HotelQuotionQueryServiceImpl;
import com.mangocity.util.DateUtil;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.util.web.WebUtil;

public class HotelQuotionQueryServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(HotelQuotionQueryServlet.class);
	private static final String ERROR_CODE = "412";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);

	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();

			HotelQuotionQueryService hotelQuotionQueryService = (HotelQuotionQueryService) WebUtil.getBean(getServletContext(), "hotelQuotionQueryService");

			String hotelId = (String) request.getParameter("hotelId");
			String priceTypeId = (String) request.getParameter("priceTypeId");
			String inDate = (String) request.getParameter("inDate");
			String outDate = (String) request.getParameter("outDate");
			String mark = (String) request.getParameter("mark");
			Map params = request.getParameterMap();

			String jsonp = request.getParameter("jsonpcallback");
			if (jsonp == null || "null".equals(jsonp)) {
				jsonp = "";
			}

			if (!"quotation".equals(mark)) {
				out.print(jsonp + getErrorMessage("412#非法访问，你的ip将被记录！"));

			} else {
				if ((hotelId == null || "".equals(hotelId)) || (priceTypeId == null || "".equals(priceTypeId))) {
					out.print(jsonp + getErrorMessage("412#输入的酒店Id 或 价格类型Id 为空，请输入 酒店Id 或 价格类型Id"));
					return;
				}

				if (validateNumParam(hotelId) || validateNumParam(priceTypeId)) {
					out.print(jsonp + getErrorMessage("412#输入的酒店Id 或 价格类型Id 存在非法字符！"));
					return;
				}

				if (inDate != null) {
					if (!inDate.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
						inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1));
						outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 2));
					}

				}
				if (outDate != null) {
					if (!outDate.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
						inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1));
						outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 2));
					}
				}
				Date checkInDate = DateUtil.stringToDateMain(inDate, "yyyy-MM-dd");
				Date checkOutDate = DateUtil.stringToDateMain(outDate, "yyyy-MM-dd");
				
				//添加默认值
				if(checkInDate == null){
					checkInDate = DateUtil.getDate(DateUtil.getSystemDate(), 1);
					checkOutDate = DateUtil.getDate(DateUtil.getSystemDate(), 2);
				}
				
				if( DateUtil.getDay(checkInDate,checkOutDate ) >2 ){
					out.print(jsonp + getErrorMessage("412#查找天数不能超过2天"));
					return;
				}
				
				//add by hushunqiang 从芒果网首页获取渠道编码
				String projectcode = request.getParameter("projectcode");
				if(projectcode == null || "".equals(projectcode)){
					projectcode = CookieUtils.getCookieValue(request, "projectcode");
				}

				
				String quotionInfoJson = hotelQuotionQueryService.queryHotelQuotionInfoByHotelId(hotelId, priceTypeId, checkInDate, checkOutDate,projectcode);
				if (quotionInfoJson == null || "null".equals(quotionInfoJson)) {
					out.print(jsonp + getErrorMessage("412#输入的信息存在错误，请检查输入的参数！"));
				} else {

					int len = quotionInfoJson.length();
					quotionInfoJson = quotionInfoJson.substring(1, len - 1);

					out.print(jsonp + "(" + quotionInfoJson + ")");
				}
			}
		} catch (Exception e) {
			log.error("query quotioninfo has a wrong", e);
			out.print(getErrorMessage("412#查询发生异常！"));
			return;

		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	private String getErrorMessage(String errorMsg) {
		StringBuilder errorM = new StringBuilder();
		errorM.append("({ error: '");
		errorM.append(errorMsg);
		errorM.append("'})");
		return errorM.toString();
	}

	/**
	 * 校验输入的酒店Id，防止非法写入
	 * 
	 * @return
	 */
	private boolean validateNumParam(String numParam) {
		if (numParam != null) {
			if (!numParam.matches("\\d+")) {
				return true;
			}
		}

		return false;
	}

}
