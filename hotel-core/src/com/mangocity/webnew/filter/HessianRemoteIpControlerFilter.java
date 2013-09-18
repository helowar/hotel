package com.mangocity.webnew.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;


import com.mangocity.hotel.base.persistence.HtlHessianIpcontrol;
import com.mangocity.webnew.service.HotelQueryService;

public class HessianRemoteIpControlerFilter implements Filter {
	
	private HotelQueryService hotelQueryService;
	
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest requ, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		if(requ instanceof HttpServletRequest){
			HttpServletResponse response = (HttpServletResponse) resp; 
			HttpServletRequest request = (HttpServletRequest) requ; 
			String clientIp = getIpAddr(request);
			if (null == hotelQueryService) {
				hotelQueryService = (HotelQueryService) WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("hotelQueryService");
			}
			List queryIpList = hotelQueryService.queryHessianIpControl();
			//判断是否继续执行filter链，继续执行filter链表示ip地址允许调用hessian接口
			boolean has = false;
			if(null != queryIpList && !queryIpList.isEmpty()){
				for(Iterator it = queryIpList.iterator();it.hasNext();){
					HtlHessianIpcontrol htl = (HtlHessianIpcontrol)it.next();
					if( htl.getIp().equals(clientIp)){
							has=true;
							break;
						}
				}
				if(has){
					chain.doFilter(requ, resp);
				}else{
					response.sendError(500);
				}
			}else{
				response.sendError(500);
			}
			
		}

	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
	
	private String getIpAddr(HttpServletRequest request) {  
	      String ip = request.getHeader("x-forwarded-for");  
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	         ip = request.getHeader("Proxy-Client-IP");  
	     }  
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	         ip = request.getHeader("WL-Proxy-Client-IP");  
	      }  
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	          ip = request.getRemoteAddr();  
	     }  
	     return ip;  
	}

	public HotelQueryService getHotelQueryService() {
		return hotelQueryService;
	}

	public void setHotelQueryService(HotelQueryService hotelQueryService) {
		this.hotelQueryService = hotelQueryService;
	} 

}
