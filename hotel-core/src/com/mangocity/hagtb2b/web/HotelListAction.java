package com.mangocity.hagtb2b.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class HotelListAction extends ActionSupport {

	/**
	 * 
	 */
	public String execute() {

		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
		.get(ServletActionContext.HTTP_REQUEST);
        return SUCCESS;
	}
}
