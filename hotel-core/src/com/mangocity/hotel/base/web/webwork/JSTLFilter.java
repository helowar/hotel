package com.mangocity.hotel.base.web.webwork;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 */
public class JSTLFilter implements Filter, Serializable {

    public void destroy() {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        // TODO Auto-generated method stub

        if (request instanceof HttpServletRequest) {
            ServletRequest wrapperRequest = new JSTLRequestWrapper((HttpServletRequest) request);

            chain.doFilter(wrapperRequest, response);
        } else {

            chain.doFilter(request, response);
        }

    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
