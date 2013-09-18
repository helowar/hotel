package com.mangocity.hotel.util;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author alfred
 */
public class BeanUtil implements ApplicationContextAware {
	
	public static ApplicationContext applicationContext;
	
	/**
	 * obtain a bean from beanFactory by beanName
	 * Notice:BeanUtil must be registed in the beanFactory,and is not lazy-init
	 * @see ApplicationContextAware
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return BeanUtil.applicationContext.getBean(beanName);
	}
	
	/**
	 * obtain a bean from beanFactory by beanName in servlet
	 * @see WebApplicationContextUtils
	 * 
	 * @param servletContext
	 * @param beanName
	 * @return
	 */
    public static Object getBean(ServletContext servletContext, String beanName) {
        WebApplicationContext context = WebApplicationContextUtils
            .getRequiredWebApplicationContext(servletContext);

        return context.getBean(beanName);

    }

    /**
     * get the BeanFactory(ApplicationContext) and bind to local variable
     */
    public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		BeanUtil.applicationContext = applicationContext;
		
	}

}
