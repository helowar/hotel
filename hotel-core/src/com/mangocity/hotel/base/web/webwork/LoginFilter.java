package com.mangocity.hotel.base.web.webwork;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mangocity.util.log.MyLog;

/**
 */
public class LoginFilter implements Filter, Serializable {
	private static final MyLog log = MyLog.getLogger(LoginFilter.class);
    public void destroy() {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        // TODO Auto-generated method stub
    	//如果未登陆,提示先登陆
    	log.info("******************第一层************************");
        if (request instanceof HttpServletRequest) {
        	log.info("******************第二层************************");
        	String login = "N";
        	HttpServletRequest  req = (HttpServletRequest)request;
        	Cookie[] cookies = req.getCookies();
        	if(null!=cookies){
        		for(int i=0;i<cookies.length;i++){
        			if(cookies[i].getName().indexOf("agentCode") > -1){
        				log.info("******************第三层YYYYYYYYYYY************************");
        				login = "Y";
        				break;
        			}
        		}
        	}
        	if(login.equals("Y")){
        		log.info("******************YYYYYYYYYYYYYYYYYY************************");
        		 chain.doFilter(request, response);

        	}else{
        		log.info("******************NNNNNNNNNNNNNNNNNNNNNNNNN************************");
        		req.setAttribute("errorMessage", "您未登录，请先登录！");
       		 	HttpServletResponse rep = (HttpServletResponse)response;  
       		 	req.getRequestDispatcher("/hotel/error.jsp").forward(request, response);
        	}

        } else {
        	log.info("******************HTTP ERROR************************");
            chain.doFilter(request, response);
        }

    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
