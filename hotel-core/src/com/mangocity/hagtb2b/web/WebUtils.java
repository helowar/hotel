package com.mangocity.hagtb2b.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;

public class WebUtils {
	private static final MyLog log = MyLog.getLogger(WebUtils.class);
	public static String getAgentCode(HttpServletRequest request){
		return CookieUtils.getCookieValue(request, "agentCode");
	}
	public static String getAgentName(HttpServletRequest request){
//		return CookieUtils.getCookieValue(request, "orgName");
		try {
			String orgName =  CookieUtils.getCookieValue(request, "orgName");
			return URLDecoder.decode(orgName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(),e);
			return null;
		}
	}
	
	public static String getOperId(HttpServletRequest request){
		return CookieUtils.getCookieValue(request, "operaterId");
	}
	public static void setAgentCode(HttpServletRequest request, HttpServletResponse response){
		 CookieUtils.setCookie(request, response, "orgId", "8000050102",3600*24, "", "");
		 try {
			CookieUtils.setCookie(request, response, "agentCode", URLEncoder.encode("0300000001","UTF-8"),3600*24*3, "", "");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(),e);
		}
		 try {
			CookieUtils.setCookie(request, response, "orgName", URLEncoder.encode("招商中旅","UTF-8"),3600*24*3, "", "");
			CookieUtils.setCookie(request, response, "loginChnName",URLEncoder.encode("杰哥","UTF-8"),3600*24*3, "", "");	
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(),e);
		}
		 CookieUtils.setCookie(request, response, "operaterId", "zheng",3600*24*3, "", "");	
		 CookieUtils.setCookie(request, response, "manageSign", "0",3600*24*3, "", "");	
		 CookieUtils.setCookie(request, response, "showCommissionFlag", "0",3600*24*3, "", "");	
		 CookieUtils.setCookie(request, response, "qianTaoFlag", "1",3600*24*3, "", "");	
	}
}
