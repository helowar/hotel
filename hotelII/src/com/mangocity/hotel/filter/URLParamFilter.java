package com.mangocity.hotel.filter;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mangocity.hotel.base.web.InitServlet;

public class URLParamFilter implements Filter {
	private Logger log = Logger.getLogger(URLParamFilter.class);

	public void destroy() {

	}

	/**
	 * 过滤掉带有html代码的参数，提交的方式为get的url。带有不合法的参数，直接转到酒店网站首页
	 * 
	 * */	
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			String method = request.getMethod();
			request.setCharacterEncoding("utf-8");
			//if ("GET".equals(method)) {
				Map params = request.getParameterMap();
				if (hasIllegalparamValue(params)) {
					response.sendRedirect("http://hotel.mangocity.com");
					return ;
				//}
			}	
		  checkCookie(request,response);
			chain.doFilter(servletRequest, servletResponse);
		} catch (Exception e) {
		
			log.error("URL过滤器发生错误", e);
		}
		

	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@SuppressWarnings("unchecked")
	private boolean hasIllegalparamValue(Map params) {
		String[] paramValues = null;
		String illegalStrs=InitServlet.illegalInputRegex;
		if(illegalStrs!=null){
			String[] illegalparamValue=illegalStrs.split(";");
		/*
			String[] illegalparamValue = {"<.*>", 
				"script",
				"^.*--.*$",
				".*xp_.*",
				"^.*(and)|(or).*=.*$",
				"^.*[a-z].*:.*\\.(ini)|(bat)|(exe)"
				};
				*/
		Pattern pattern = null;
		Matcher matcher = null;
		if (params != null) {
			for (Object o : params.values()) {
				paramValues = (String[]) o;
				for (int i = 0; i < paramValues.length; i++) {
					for (int j = 0; j < illegalparamValue.length; j++) {
						pattern = Pattern.compile(illegalparamValue[j]);
						matcher = pattern.matcher(paramValues[i].toLowerCase());						
						if (matcher.find()) {
							return true;
						}
					}
				}
			}
		}
		}
		return false;

	}
	
	private void checkCookie(HttpServletRequest request,HttpServletResponse response ){
		Cookie cookies[] = request.getCookies();
		Pattern pattern = null;
		Matcher matcher = null;

		if (cookies == null) {
			return;
		}

		String illegalStrs = InitServlet.illegalInputRegex;
		if (illegalStrs != null) {
			String[] illegalparamValue = illegalStrs.split(";");

			for (Cookie cookie : cookies) {
				String value = cookie.getValue();
				if (value != null) {
					for (int j = 0; j < illegalparamValue.length; j++) {
						pattern = Pattern.compile(illegalparamValue[j]);
						matcher = pattern.matcher(value.toLowerCase());
						if (matcher.find()) {
							cookie.setValue("");
							response.addCookie(cookie);
						}
					}
				}
			}
			illegalparamValue=null;
		}
		cookies=null;
		
	}

}

